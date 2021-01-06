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

namespace Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil;

use DateTimeImmutable;
use Google_Client;
use Google_Service_Pubsub;
use Google_Service_RealTimeBidding;
use Google_Service_RealTimeBidding_Date;

/**
 * Defines configuration of the sample application.
 */
class Config
{

    /*
     * The current version of the Real-time Bidding API.
     */
    private static $apiVersion = 'V1';

    /**
     * Date format used when converting between DateTime instances and strings.
     */
    private static $dateFormat = 'Y/m/d';

    /**
     * Returns the current supported API version.
     */
    public static function getApiVersion()
    {
        return self::$apiVersion;
    }

    /**
     * Converts a string containing an ISO 8601 date to a corresponding DateTimeImmutable instance.
     *
     * If the specified string is set to null, the value returned will be null.
     */
    public static function getDateTimeImmutableFromDateString($str)
    {
        return is_null($str) ? null : DateTimeImmutable::createFromFormat(self::$dateFormat, $str);
    }

    /**
     * Converts a DateTimeImmutable instance to a corresponding Google_Service_RealTimeBidding_Date
     * instance.
     *
     * If the specified DateTimeImmutable is null, the value returned will be null.
     */
    public static function getRealTimeBiddingDateFromDateTimeImmutable($dateTime)
    {
        if (is_null($dateTime)) {
            return null;
        }

        $date = new Google_Service_RealTimeBidding_Date();
        $date->year = $dateTime->format('Y');
        $date->month = $dateTime->format('m');
        $date->day = $dateTime->format('d');
        return $date;
    }

    /**
     * Steps through OAuth 2.0 and returns a Google_Client instance.
     */
    public static function getGoogleClient($keyFileLocation)
    {
        $client = new Google_Client();
        $client->setApplicationName('Authorized Buyers Real-Time Bidding API PHP Samples');

        if (isset($_SESSION['service_token'])) {
            $client->setAccessToken($_SESSION['service_token']);
        }

        $client->setAuthConfig($keyFileLocation);
        $client->addScope('https://www.googleapis.com/auth/realtime-bidding');
        $client->addScope('https://www.googleapis.com/auth/pubsub');

        if ($client->isAccessTokenExpired()) {
            $client->refreshTokenWithAssertion();
        }

        return $client;
    }

    /**
     * Returns Google_Service_PubSub, instantiated with the given Google_Client.
     */
    public static function getGoogleServicePubSub($client)
    {
        return new Google_Service_Pubsub($client);
    }

    /**
     * Returns Google_Service_RealTimeBidding, instantiated with the given Google_Client.
     */
    public static function getGoogleServiceRealTimeBidding($client)
    {
        return new Google_Service_RealTimeBidding($client);
    }

    /**
     * Builds an array containing the supported actions.
     */
    public static function getSupportedActions()
    {
        return [
           self::$apiVersion => [
               'Bidders_Creatives' => [
                   'ListCreatives',
                   'WatchCreatives',
                   'PullWatchedCreativesSubscription'
               ],
               'Bidders_PretargetingConfigs' => [
                   'GetPretargetingConfigs',
                   'ListPretargetingConfigs',
                   'CreatePretargetingConfigs',
                   'PatchPretargetingConfigs',
                   'AddTargetedApps',
                   'AddTargetedPublishers',
                   'AddTargetedSites',
                   'RemoveTargetedApps',
                   'RemoveTargetedPublishers',
                   'RemoveTargetedSites',
                   'ActivatePretargetingConfigs',
                   'SuspendPretargetingConfigs',
                   'DeletePretargetingConfigs'
               ],
               'Buyers_Creatives' => [
                   'GetCreatives',
                   'ListCreatives',
                   'CreateHtmlCreatives',
                   'CreateNativeCreatives',
                   'CreateVideoCreatives',
                   'PatchCreatives'
               ],
               'Buyers_UserLists' => [
                   'ListUserLists',
                   'CreateUserLists',
                   'UpdateUserLists'
               ],
           ],
        ];
    }
}
