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

/**
 * This example illustrates how to list creatives for the given buyer's account ID.
 */
class ListCreatives extends BaseExample
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
                'display' => 'Buyer account ID',
                'required' => true,
                'description' =>
                    'The resource ID of the buyers resource under which the creatives were ' .
                    'created. This will be used to construct the parent used as a path ' .
                    'parameter for the creatives.list request.'
            ],
            [
                'name' => 'filter',
                'display' => 'Filter',
                'required' => false,
                'description' =>
                    'Query string to filter creatives. If no filter is specified, all active ' .
                    'creatives will be returned. To demonstrate usage, the default behavior of ' .
                    'this sample is to filter such that only approved HTML snippet creatives ' .
                    'are returned.',
                'default' =>
                    'creativeServingDecision.openAuctionServingStatus.status=APPROVED ' .
                    'AND creativeFormat=HTML'
            ],
            [
                'name' => 'view',
                'display' => 'View',
                'required' => false,
                'description' =>
                    'Controls the amount of information included in the response. By default, ' .
                    'the creatives.list method only includes creativeServingDecision. This ' .
                    'sample configures the view to return the full contents of the creatives by ' .
                    'setting this to "FULL".',
                'default' => 'FULL'
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $parentName = "buyers/$values[account_id]";

        $queryParams = [
            'filter' => $values['filter'],
            'pageSize' => 10,
            'view' => $values['view']
        ];

        $result = $this->service->buyers_creatives->listBuyersCreatives($parentName, $queryParams);

        print "<h2>Creatives found for '$parentName':</h2>";
        if (empty($result['creatives'])) {
            print '<p>No Creatives found</p>';
        } else {
            foreach ($result['creatives'] as $creative) {
                $this->printResult($creative);
            }
        }
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'List Buyer Creatives';
    }
}
