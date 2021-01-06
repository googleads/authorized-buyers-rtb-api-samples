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
use Google_Service_RealTimeBidding_RemoveTargetedPublishersRequest;

/**
 * Removes publisher IDs from a pretargeting configuration's publisher targeting.
 *
 * Note that this is the only way to remove publisher IDs following a
 * pretargeting configuration's creation.
 */
class RemoveTargetedPublishers extends BaseExample
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
                'name' => 'publisher_ids',
                'display' => 'Publisher IDs',
                'description' =>
                    'The publisher IDs to be removed from this configuration\'s publisher ' .
                    'targeting. Specify each ID separated by a comma. Valid publisher IDs can ' .
                    'be found in Real-time Bidding bid requests, or alternatively in ads.txt / ' .
                    'app-ads.txt. For more information, see: https://iabtechlab.com/ads-txt/',
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
        $removeTargetedPublishersRequest = new Google_Service_RealTimeBidding_RemoveTargetedPublishersRequest();
        $removeTargetedPublishersRequest->targetingMode = $values['publisher_targeting_mode'];
        $removeTargetedPublishersRequest->publisherIds = $values['publisher_ids'];

        print "<h2>Removing publisher IDs from publisher targeting for pretargeting " .
            "configuration with name: '$name':</h2>";
        $result = $this->service->bidders_pretargetingConfigs->removeTargetedPublishers(
            $name,
            $removeTargetedPublishersRequest
        );
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Remove Targeted Publishers';
    }
}
