<?php

/**
 * Copyright 2022 Google LLC
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

namespace Google\Ads\AuthorizedBuyers\RealTimeBidding\Examples\V1\Bidders_PublisherConnections;

use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\Config;

/**
 * Gets a single publisher connection for the given bidder's account ID.
 *
 * Note: This sample will only return a populated response for bidders who are
 * exchanges participating in Open Bidding.
 */
class GetPublisherConnections extends BaseExample
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
                    'The resource ID of the bidders resource under which the publisher ' .
                    'connection exists. This will be used to construct the name used as a path ' .
                    'parameter for the publisherConnections.get request.',
                'required' => true
            ],
            [
                'name' => 'publisher_connection_id',
                'display' => 'Publisher connection ID',
                'description' =>
                    'The resource ID of the publisher connection that is being retrieved. This ' .
                    'value is the publisher ID found in ads.txt or app-ads.txt, and is used to ' .
                    'construct the name used as a path parameter for the ' .
                    'publisherConnections.get request.',
                'required' => true,
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;
        $name = "bidders/$values[account_id]/publisherConnections/$values[publisher_connection_id]";

        print "<h2>Retrieving publisher connection with name '$name':</h2>";
        $result = $this->service->bidders_publisherConnections->get($name);
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Get Publisher Connection';
    }
}
