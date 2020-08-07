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

use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\Config;
use Google_Service_RealTimeBidding_UrlRestriction;
use Google_Service_RealTimeBidding_UserList;

/**
 * This example illustrates how to update a UserList for a given buyer account.
 *
 * The specified buyer account ID and UserList ID will be used to retrieve an existing UserList,
 * which will have its status modified.
 */
class UpdateUserLists extends BaseExample
{

    /**
     * Date format used in this example when converting between DateTime instances
     * and Strings.
     */
    private static $dateFormat = 'Y/m/d';

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
                    'The resource ID of the buyers resource under which the user list was created. ' .
                    'This will be used to construct the name used as a path parameter for the ' .
                    'userLists.update request.',
                'required' => true
            ],
            [
                'name' => 'user_list_id',
                'display' => 'Buyer user list ID',
                'description' =>
                    'The resource ID of the buyers.userLists resource under which the user list ' .
                    'was created. This will be used to construct the name used as a path ' .
                    'parameter for the userLists.update request.',
                'required' => true
            ],
            [
                'name' => 'display_name',
                'display' => 'Display name',
                'description' =>
                    'Display name of the user list. This must be unique across all user lists ' .
                    'for a given account.',
                'required' => false,
                'default' => 'Test UserList Update #' . uniqid()
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
                'required' => false
            ],
            [
                'name' => 'start_date',
                'display' => 'Start date',
                'description' =>
                    'The start date for the URL restriction, specified as an ISO 8601 date ' .
                    '(yyyy/mm/dd).',
                'required' => false
            ],
            [
                'name' => 'end_date',
                'display' => 'End date',
                'description' =>
                    'The end date for the URL restriction, specified as an ISO 8601 date ' .
                    '(yyyy/mm/dd).',
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

        $userListName = "buyers/$values[account_id]/userLists/$values[user_list_id]";

        // Retrieve a copy of the current configuration of the user list to be updated. This will
        // be modified with the specified changes and then submitted in the update request.
        $oldUserList = $this->getUserList($userListName);

        $displayName = $values['display_name'];
        if (isset($displayName)) {
            $oldUserList->displayName = $displayName;
        }

        $oldUrlRestriction = $oldUserList->getUrlRestriction();

        // Use provided dates to instantiate DateTimeImmutable instances that will be used to
        // update the user list's start and end dates.
        $startDateTime = Config::getDateTimeImmutableFromDateString($values['start_date']);
        $startDate = Config::getRealTimeBiddingDateFromDateTimeImmutable($startDateTime);
        if (isset($startDate)) {
            $oldUrlRestriction->setStartDate($startDate);
        }
        $endDateTime = Config::getDateTimeImmutableFromDateString($values['end_date']);
        $endDate = Config::getRealTimeBiddingDateFromDateTimeImmutable($endDateTime);
        if (isset($endDate)) {
            $oldUrlRestriction->setEndDate($endDate);
        }

        $urlRestrictionType = $values['url_restriction_type'];
        if (isset($urlRestrictionType)) {
            $oldUrlRestriction->restrictionType = $urlRestrictionType;
        }

        $url = $values['url'];
        if (isset($url)) {
            $oldUrlRestriction->url = $url;
        }

        print "<h2>Updating UserList '$userListName':</h2>";
        $result = $this->service->buyers_userLists->update($userListName, $oldUserList);
        $this->printResult($result);
    }

    /**
     * Retrieves a Google_Service_RealTimeBidding_UserList instance for the given name.
     */
    public function getUserList($userListName)
    {
        return $this->service->buyers_userLists->get($userListName);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Update Buyer UserList';
    }
}
