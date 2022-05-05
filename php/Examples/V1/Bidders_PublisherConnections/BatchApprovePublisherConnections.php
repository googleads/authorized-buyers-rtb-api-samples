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
use Google_Service_RealTimeBidding_BatchApprovePublisherConnectionsRequest;

/**
 * Batch approves one or more publisher connections.
 *
 * Approving a publisher connection means that the bidder agrees to receive
 * bid requests from the publisher.
 */
class BatchApprovePublisherConnections extends BaseExample
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
                    'connections exist. This will be used to construct the parent used as a ' .
                    'path parameter for the publisherConnections.batchApprove request, as well ' .
                    'as the publisher connection names passed in the request body.',
                'required' => true
            ],
            [
                'name' => 'publisher_connection_ids',
                'display' => 'Publisher connection IDs',
                'description' =>
                    'One or more resource IDs for the bidders.publisherConnections resource ' .
                    'that are being approved. Specify each value separated by a comma. These ' .
                    'will be used to construct the publisher connection names passed in the ' .
                    'publisherConnections.batchApprove request body.',
                'required' => true,
                'is_array' => true
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;
        $accountId = $values[account_id];
        $parent = "bidders/$accountId";

        $pubConnIds = $values[publisher_connection_ids];
        $batchApproveRequest = new Google_Service_RealTimeBidding_BatchApprovePublisherConnectionsRequest();
        $batchApproveRequest->names = array_map(
            function ($pubConnId) use ($accountId) {
                    return "$parent/publisherConnections/$pubConnId";
            },
            $pubConnIds
        );

        print "<h2>Batch approving publisher connections for bidder with name: \"$parent\":";
        $result = $this->service->bidders_publisherConnections->batchApprove(
            $parent,
            $batchApproveRequest
        );
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Batch Approve Publisher Connections';
    }
}
