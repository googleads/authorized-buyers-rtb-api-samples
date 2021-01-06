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

/**
 * Lists pretargeting configurations for a given bidder account ID.
 */
class ListPretargetingConfigs extends BaseExample
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
                    'The resource ID of the bidders resource under which the pretargeting ' .
                    'configurations were created. This will be used to construct the parent ' .
                    'used as a path parameter for the pretargetingConfig.list request.'
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
        $queryParams = ['pageSize' => $values['page_size']];

        $result = $this->service->bidders_pretargetingConfigs->listBiddersPretargetingConfigs($parentName, $queryParams);

        print "<h2>Pretargeting configurations found for '$parentName':</h2>";
        if (empty($result['pretargetingConfigs'])) {
            print '<p>No pretargeting configurations found</p>';
        } else {
            foreach ($result['pretargetingConfigs'] as $pretargetingConfig) {
                $this->printResult($pretargetingConfig);
            }
        }
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'List Bidder Pretargeting Configurations';
    }
}
