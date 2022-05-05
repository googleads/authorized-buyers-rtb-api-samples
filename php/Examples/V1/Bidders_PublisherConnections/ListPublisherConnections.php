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
 * Lists publisher connections for a given bidder account ID.
 *
 * Note: This sample will only return a populated response for bidders who are
 * exchanges participating in Open Bidding.
 */
class ListPublisherConnections extends BaseExample
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
                'display' => 'Bidder account ID',
                'required' => true,
                'description' =>
                    'The resource ID of the bidders resource under which the publisher ' .
                    'connections exist. This will be used to construct the parent ' .
                    'used as a path parameter for the publisherConnections.list request.'
            ],
            [
                'name' => 'filter',
                'display' => 'Filter',
                'required' => false,
                'description' =>
                    'Query string used to filter publisher connections. To demonstrate usage, ' .
                    'this sample will default to filtering by publisherPlatform.',
                'default' => 'publisherPlatform = GOOGLE_AD_MANAGER'
            ],
            [
                'name' => 'order_by',
                'display' => 'Order by',
                'required' => false,
                'description' =>
                    'A string specifying the order by which results should be sorted. To ' .
                    'demonstrate usage, this sample will default to ordering by createTime.',
                'default' => 'createTime DESC'
            ],
            [
                'name' => 'page_size',
                'display' => 'Page size',
                'required' => false,
                'description' =>
                    'The number of rows to return per page. The server may return fewer rows ' .
                    'than specified.',
                'default' => 10
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $parentName = "bidders/$values[account_id]";
        $queryParams = [
            'filter' => $values['filter'],
            'orderBy' => $values['order_by'],
            'pageSize' => $values['page_size']
        ];

        $result = $this->service->bidders_publisherConnections->listBiddersPublisherConnections(
            $parentName,
            $queryParams
        );

        print "<h2>Publisher connections found for '$parentName':</h2>";
        if (empty($result['publisherConnections'])) {
            print '<p>No publisher connections found</p>';
        } else {
            foreach ($result['publisherConnections'] as $publisherConnection) {
                $this->printResult($publisherConnection);
            }
        }
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'List Bidder Publisher Connections';
    }
}
