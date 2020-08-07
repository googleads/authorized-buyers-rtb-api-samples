<?php

/**
 * Copyright 2019 Google LLC
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

namespace Google\Ads\AuthorizedBuyers\RealTimeBidding\Examples\V1\Buyers_UserLists;

use DateInterval;
use DateTimeImmutable;
use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\Config;
use Google_Service_RealTimeBidding_Date;
use Google_Service_RealTimeBidding_UrlRestriction;
use Google_Service_RealTimeBidding_UserList;

/**
 * This example illustrates how to create UserLists for a given buyer account.
 */
class CreateUserLists extends BaseExample
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
                    'The resource ID of the buyers resource under which the user list is to be ' .
                    'created.',
                'required' => true
            ],
            [
                'name' => 'display_name',
                'display' => 'Display name',
                'description' =>
                    'Display name of the user list. This must be unique across all user lists ' .
                    'for a given account.',
                'required' => false,
                'default' => 'Test UserList #' . uniqid()
            ],
            [
                'name' => 'url',
                'display' => 'URL Restriction\'s URL',
                'description' =>
                    'The URL to use for applying the UrlRestriction on the user list.',
                'required' => false,
                'default' => 'https://luxurymarscruises.com'
            ],
            [
                'name' => 'url_restriction_type',
                'display' => 'URL Restriction\'s type',
                'description' =>
                    'The restriction type for the specified URL. For more details on how to ' .
                    'interpret the different restriction types, see the ' .
                    '<a href="https://developers.google.com/authorized-buyers/apis/' .
                    'realtimebidding/reference/rest/v1/buyers.userLists' .
                    '#UrlRestriction.FIELDS.restriction_type"> reference documentation.</a>',
                'required' => false,
                'default' => 'EQUALS'
            ],
            [
                'name' => 'start_date',
                'display' => 'Start date',
                'description' =>
                    'The start date for the URL restriction, specified as an ISO 8601 date ' .
                    '(yyyy/mm/dd). By default, this will be set to today.',
                'required' => false
            ],
            [
                'name' => 'end_date',
                'display' => 'End date',
                'description' =>
                    'The end date for the URL restriction, specified as an ISO 8601 date ' .
                    '(yyyy/mm/dd). By default, this will be set to a week following the start ' .
                    'date.',
                'required' => false
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

        // Use provided dates to instantiate DateTimeImmutable instances that will be used to
        // configure the UserList's start and end dates. If no dates are specified, the start date
        // will be today, and the end date will be 1 week later.
        $startDateTime = Config::getDateTimeImmutableFromDateString($values['start_date']);
        $startDate = Config::getRealTimeBiddingDateFromDateTimeImmutable($startDateTime);
        if (is_null($startDate)) {
            $startDateTime = new DateTimeImmutable('today');
            $startDate = Config::getRealTimeBiddingDateFromDateTimeImmutable($startDateTime);
        }
        $endDate = Config::getRealTimeBiddingDateFromDateTimeImmutable(
            Config::getDateTimeImmutableFromDateString($values['end_date'])
        );
        if (is_null($endDate)) {
            $endDate = Config::getRealTimeBiddingDateFromDateTimeImmutable($startDateTime->add(
                new DateInterval('P7D')
            ));
        }

        $urlRestriction = new Google_Service_RealTimeBidding_UrlRestriction();
        $urlRestriction->url = $values['url'];
        $urlRestriction->restrictionType = $values['url_restriction_type'];
        $urlRestriction->setStartDate($startDate);
        $urlRestriction->setEndDate($endDate);

        $newUserList = new Google_Service_RealTimeBidding_UserList();
        $newUserList->displayName = $values['display_name'];
        $newUserList->setUrlRestriction($urlRestriction);

        print "<h2>Creating UserList for '$parentName':</h2>";
        $result = $this->service->buyers_userLists->create($parentName, $newUserList);
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Create Buyer UserList';
    }
}
