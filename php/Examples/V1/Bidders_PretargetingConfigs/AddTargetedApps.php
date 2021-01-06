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
use Google_Service_RealTimeBidding_AddTargetedAppsRequest;

/**
 * Adds mobile application IDs to a pretargeting configuration's app targeting.
 *
 * Note that this is the only way to append mobile application IDs following a
 * pretargeting configuration's creation. If a pretargeting configuration
 * already targets mobile application IDs, you must specify a targeting mode
 * that is identical to the existing targeting mode.
 */
class AddTargetedApps extends BaseExample
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
                'name' => 'mobile_app_targeting_mode',
                'display' => 'Mobile app targeting mode',
                'description' =>
                    'The targeting mode for this configuration\'s mobile app targeting. Valid ' .
                    'values include: INCLUSIVE, and EXCLUSIVE. Note that if the configuration ' .
                    'already targets mobile app Ids, you must specify an identical targeting ' .
                    'mode.',
                'required' => false,
                'default' => null
            ],
            [
                'name' => 'mobile_app_targeting_app_ids',
                'display' => 'Mobile app targeting app IDs',
                'description' =>
                    'The mobile app IDs specified for this configuration\'s mobile app ' .
                    'targeting, which allows one to target a subset of mobile app inventory. ' .
                    'Specify each value separated by a comma. Values specified must be valid ' .
                    'mobile App IDs, as found on their respective app stores.',
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
        $addTargetedAppsRequest = new Google_Service_RealTimeBidding_AddTargetedAppsRequest();
        $addTargetedAppsRequest->targetingMode = $values['mobile_app_targeting_mode'];
        $addTargetedAppsRequest->appIds = $values['mobile_app_targeting_app_ids'];

        print "<h2>Updating mobile app targeting with new app IDs for pretargeting " .
            "configuration with name: '$name':</h2>";
        $result = $this->service->bidders_pretargetingConfigs->addTargetedApps(
            $name,
            $addTargetedAppsRequest
        );
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Add Targeted Apps';
    }
}
