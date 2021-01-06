<?php

/**
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

namespace Google\Ads\AuthorizedBuyers\RealTimeBidding\Examples\V1\Bidders_PretargetingConfigs;

use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\Config;
use Google_Service_RealTimeBidding_AddTargetedPublishersRequest;

/**
 * Adds publisher IDs to a pretargeting configuration's publisher targeting.
 *
 * Note that this is the only way to append publisher IDs following a
 * pretargeting configuration's creation. If a pretargeting configuration
 * already targets publisher IDs, you must specify a targeting mode that is
 * identical to the existing targeting mode.
 */
class AddTargetedPublishers extends BaseExample
{

    public function __construct($client)
    {
        $this->service = Config::getGoogleServiceRealTimeBidding($client);
    }

    /**
     * @see BaseExample::getInputParameters()
     */
    protected function getInputParameters()
    {
        return [
            [
                'name' => 'account_id',
                'display' => 'Account ID',
                'description' =>
                    'The resource ID of the bidders resource under which the pretargeting ' .
                    'configuration was created.',
                'required' => true
            ],
            [
                'name' => 'pretargeting_config_id',
                'display' => 'Pretargeting configuration ID',
                'description' =>
                    'The resource ID of the pretargeting configuration that is being acted upon.',
                'required' => true,
            ],
            [
                'name' => 'publisher_targeting_mode',
                'display' => 'Publisher targeting mode',
                'description' =>
                    'The targeting mode for this configuration\'s publisher targeting. Valid ' .
                    'values include: INCLUSIVE, and EXCLUSIVE. Note that if the configuration ' .
                    'already targets publisher Ids, you must specify an identical targeting mode.',
                'required' => false,
                'default' => null
            ],
            [
                'name' => 'publisher_ids',
                'display' => 'Publisher IDs',
                'description' =>
                    'The publisher IDs specified for this configuration\'s publisher targeting, ' .
                    'which allows one to target a subset of publisher inventory. Specify each ' .
                    'ID separated by a comma. Valid publisher IDs can be found in Real-time ' .
                    'Bidding bid requests, or alternatively in ads.txt / app-ads.txt. For more ' .
                    'information, see: https://iabtechlab.com/ads-txt/',
                'required' => false,
                'is_array' => true,
                'default' => []
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;
        $name = "bidders/$values[account_id]/pretargetingConfigs/$values[pretargeting_config_id]";
        $addTargetedPublishersRequest = new Google_Service_RealTimeBidding_AddTargetedPublishersRequest();
        $addTargetedPublishersRequest->targetingMode = $values['publisher_targeting_mode'];
        $addTargetedPublishersRequest->publisherIds = $values['publisher_ids'];

        print "<h2>Updating publisher targeting with new publisher IDs for pretargeting " .
            "configuration with name: '$name':</h2>";
        $result = $this->service->bidders_pretargetingConfigs->addTargetedPublishers(
            $name,
            $addTargetedPublishersRequest
        );
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Add Targeted Publishers';
    }
}
