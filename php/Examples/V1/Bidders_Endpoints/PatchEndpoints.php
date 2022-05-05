<?php

/**
 * Copyright 2021 Google LLC
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

namespace Google\Ads\AuthorizedBuyers\RealTimeBidding\Examples\V1\Bidders_Endpoints;

use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\Config;
use Google_Service_RealTimeBidding_Endpoint;

/**
 * Patches an endpoint with the specified name.
 */
class PatchEndpoints extends BaseExample
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
                    'The resource ID of the bidders resource under which the endpoint ' .
                    'exists.',
                'required' => true
            ],
            [
                'name' => 'endpoint_id',
                'display' => 'Endpoint ID',
                'description' =>
                    'The resource ID of the endpoint to be patched.',
                'required' => true,
            ],
            [
                'name' => 'bid_protocol',
                'display' => 'Bid protocol',
                'description' =>
                    'The real-time bidding protocol that the endpoint is using.',
                'required' => false,
                'default' => 'GOOGLE_RTB'
            ],
            [
                'name' => 'maximum_qps',
                'display' => 'Maximum QPS',
                'description' =>
                    'The maximum number of queries per second allowed to be sent to the endpoint.',
                'required' => false,
                'default' => '1'
            ],
            [
                'name' => 'trading_location',
                'display' => 'Trading location',
                'description' =>
                    'Region where the endpoint and its infrastructure is located; corresponds ' .
                    'to the location of users that bid requests are sent for.',
                'required' => false,
                'default' => 'US_EAST'
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;
        $name = "bidders/$values[account_id]/endpoints/$values[endpoint_id]";

        $body = new Google_Service_RealTimeBidding_Endpoint();
        $body->bidProtocol = $values['bid_protocol'];
        $body->maximumQps = $values['maximum_qps'];
        $body->tradingLocation = $values['trading_location'];

        $queryParams = [
            'updateMask' =>
                'maximumQps,tradingLocation,bidProtocol'
        ];

        print "<h2>Patching an endpoint with name '$name':</h2>";
        $result = $this->service->bidders_endpoints->patch($name, $body, $queryParams);
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Patch Endpoint';
    }
}
