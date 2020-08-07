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

namespace Google\Ads\AuthorizedBuyers\RealTimeBidding\Examples\V1\Bidders_Creatives;

use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\Config;
use Google_Service_RealTimeBidding_WatchCreativesRequest;

/**
 * Enables monitoring of changes in status of a given bidder's creatives.
 *
 * Watched creatives will have changes to their status posted to Google Cloud Pub/Sub. For more
 * details on Google Cloud Pub/Sub, see:
 * https://cloud.google.com/pubsub/docs
 *
 * For an example of pulling creative status changes from a Google Cloud Pub/Sub subscription, see
 * PullWatchedCreativesSubscription.php.
 */
class WatchCreatives extends BaseExample
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
                    'The resource ID of a bidder account. This will be used to construct the ' .
                    'parent used as a path parameter for the creatives.watch request.'
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

        $watch_request = new Google_Service_RealTimeBidding_WatchCreativesRequest();

        try {
            $response = $this->service->bidders_creatives->watch($parentName, $watch_request);
            print "<h2>Watching creative status for bidder '$parentName'.</h2>";
            $this->printResult($response);
        } catch (Google_Service_Exception $ex) {
            if ($ex->getCode() === 404 || $ex->getCode() === 403) {
                print '<h1>Bidder not found or can\'t access account.</h1>';
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
        return 'Watch Bidder Creatives';
    }
}
