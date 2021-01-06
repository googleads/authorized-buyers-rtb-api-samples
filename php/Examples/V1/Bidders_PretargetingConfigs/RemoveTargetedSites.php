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
use Google_Service_RealTimeBidding_RemoveTargetedSitesRequest;

/**
 * Removes site URLs from a pretargeting configuration's web targeting.
 *
 * Note that this is the only way to remove URLs following a pretargeting
 * configuration's creation.
 */
class RemoveTargetedSites extends BaseExample
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
                'name' => 'web_targeting_urls',
                'display' => 'Web targeting URLs',
                'description' =>
                    'The URLs to be removed from this configuration\'s web targeting. Specify ' .
                    'each value separated by a comma. Values specified must be valid URLs.',
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
        $removeTargetedSitesRequest = new Google_Service_RealTimeBidding_RemoveTargetedSitesRequest();
        $removeTargetedSitesRequest->sites = $values['web_targeting_urls'];

        print "<h2>Removing site URLs from web targeting for pretargeting configuration with " .
            "name: '$name':</h2>";
        $result = $this->service->bidders_pretargetingConfigs->removeTargetedSites(
            $name,
            $removeTargetedSitesRequest
        );
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Remove Targeted Sites';
    }
}
