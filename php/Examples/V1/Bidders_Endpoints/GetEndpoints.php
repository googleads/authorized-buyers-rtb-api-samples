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

/**
 * This example illustrates how to get a single endpoint for the specified bidder and endpoint IDs.
 */
class GetEndpoints extends BaseExample
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
                'required' => true,
                'description' =>
                    'The resource ID of the bidders resource under which the endpoint exists. ' .
                    'This will be used to construct the name used as a path parameter for the ' .
                    'endpoints.get request.'
            ],
            [
                'name' => 'endpoint_id',
                'display' => 'Endpoint ID',
                'required' => true,
                'description' =>
                    'The resource ID of the endpoints resource that is being retrieved. This ' .
                    'will be used to construct the name used as a path parameter for the ' .
                    'endpoints.get request.'
            ],
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $name = "bidders/$values[account_id]/endpoints/$values[endpoint_id]";

        try {
            $endpoint = $this->service->bidders_endpoints->get($name);
            print '<h2>Found endpoint.</h2>';
            $this->printResult($endpoint);
        } catch (Google_Service_Exception $ex) {
            if ($ex->getCode() === 404 || $ex->getCode() === 403) {
                print '<h1>Endpoint not found or can\'t access endpoint.</h1>';
            } else {
                throw $ex;
            }
        }
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Get Endpoint';
    }
}
