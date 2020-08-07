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

namespace Google\Ads\AuthorizedBuyers\RealTimeBidding\Examples\V1\Buyers_Creatives;

use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\Config;
use Google_Service_RealTimeBidding_Creative;

/**
 * This example illustrates how to patch a creative with the specified account and creative IDs.
 */
class PatchCreatives extends BaseExample
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
                    'The resource ID of the buyers resource under which the creative was ' .
                    'created. This will be used to construct the name used as a path parameter ' .
                    'for the creatives.patch request.',
                'required' => true
            ],
            [
                'name' => 'creative_id',
                'display' => 'Creative ID',
                'description' =>
                    'The resource ID of the buyers.creatives resource for which the creative ' .
                    'was created. This will be used to construct the name used as a path ' .
                    'parameter for the creatives.patch request.',
                'required' => true
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $name = "buyers/$values[account_id]/creatives/$values[creative_id]";

        $patchedCreative = new Google_Service_RealTimeBidding_Creative();
        $patchedCreative->advertiserName = 'Test-Advertiser-' . uniqid();
        $patchedCreative->declaredClickThroughUrls = [
            'https://test.clickurl.com/' . uniqid(),
            'https://test.clickurl.com/' . uniqid(),
            'https://test.clickurl.com/' . uniqid()
        ];

        $queryParams = [
            'updateMask' => 'advertiserName,declaredClickThroughUrls'
        ];

        print "<h2>Patching Creative '$name':</h2>";
        $result = $this->service->buyers_creatives->patch($name, $patchedCreative, $queryParams);
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Patch Buyer Creative';
    }
}
