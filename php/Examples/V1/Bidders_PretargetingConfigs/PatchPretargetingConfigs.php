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
use Google_Service_RealTimeBidding_PretargetingConfig;
use Google_Service_RealTimeBidding_AppTargeting;
use Google_Service_RealTimeBidding_CreativeDimensions;
use Google_Service_RealTimeBidding_NumericTargetingDimension;
use Google_Service_RealTimeBidding_StringTargetingDimension;

/**
 * Patches a pretargeting configuration with the specified name.
 */
class PatchPretargetingConfigs extends BaseExample
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
                    'The resource ID of the pretargeting configuration to be patched.',
                'required' => true,
            ],
            [
                'name' => 'display_name',
                'display' => 'Display name',
                'description' =>
                    'The patched display name to associate with the configuration. Must be ' .
                    'unique among all of a bidder\'s pretargeting configurations.',
                'required' => false,
                'default' => 'TEST_PRETARGETING_CONFIG_' . uniqid()
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

        $geoTargeting = new Google_Service_RealTimeBidding_NumericTargetingDimension();
        $geoTargeting->includedIds = [
            '200635',   // Austin, TX
            '1014448',  // Boulder, CO
            '1022183',  // Hoboken, NJ
            '200622',   // New Orleans, LA
            '1023191',  // New York, NY
            '9061237',  // Mountain View, CA
            '1014221'   // San Francisco, CA
        ];

        $includedCreativeDimensions1 = new Google_Service_RealTimeBidding_CreativeDimensions();
        $includedCreativeDimensions1->height = 480;
        $includedCreativeDimensions1->width = 320;

        $includedCreativeDimensions2 = new Google_Service_RealTimeBidding_CreativeDimensions();
        $includedCreativeDimensions2->height = 1080;
        $includedCreativeDimensions2->width = 1920;

        $body = new Google_Service_RealTimeBidding_PretargetingConfig();
        $body->displayName = $values['display_name'];
        $body->includedFormats = ['HTML', 'VAST'];
        $body->geoTargeting = $geoTargeting;
        $body->includedCreativeDimensions = [$includedCreativeDimensions1, $includedCreativeDimensions2];

        $queryParams = [
            'updateMask' =>
                'displayName,includedFormats,geoTargeting.includedIds,includedCreativeDimensions'
        ];

        print "<h2>Patching a pretargeting configuration with name '$name':</h2>";
        $result = $this->service->bidders_pretargetingConfigs->patch($name, $body, $queryParams);
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Patch Pretargeting Configuration';
    }
}
