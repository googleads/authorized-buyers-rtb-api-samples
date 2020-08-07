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

/**
 * Authorizes with the ServiceAccount Authorization Flow and presents a menu of
 * Authorized Buyers Real-Time Bidding API samples to run.
 */

use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\Config;
use function Google\Ads\AuthorizedBuyers\RealTimeBidding\HtmlHelper\printExamplesIndex;
use function Google\Ads\AuthorizedBuyers\RealTimeBidding\HtmlHelper\printHtmlFooter;
use function Google\Ads\AuthorizedBuyers\RealTimeBidding\HtmlHelper\printHtmlHeader;
use function Google\Ads\AuthorizedBuyers\RealTimeBidding\HtmlHelper\printSampleHtmlFooter;

// Provide path to client library. See README.md for details.
require_once __DIR__ . '/vendor/autoload.php';
require_once __DIR__ . '/HtmlHelper.php';

// Require all example utilities.
foreach (glob(__DIR__ . "/ExampleUtil/*.php") as $util) {
    require_once $util;
}

$apiVersion = Config::getApiVersion();

// Require all examples.
foreach (glob(__DIR__ . "/Examples/" . $apiVersion . "/*/*.php") as $example) {
    require_once $example;
}

session_start();

/*
 * You can retrieve this file from the Google Developers Console.
 *
 * See README.md for details.
 */
$keyFileLocation = '<PATH_TO_JSON>';

if ($keyFileLocation === '<PATH_TO_JSON>') {
    echo '<h1>WARNING: Authorization details not provided!</h1>';
    exit(1);
}

$client = Config::getGoogleClient($keyFileLocation);
$_SESSION['service_token'] = $client->getAccessToken();

if ($client->getAccessToken()) {
    // Build the list of supported actions.
    $actions = Config::getSupportedActions();
    $service = Config::getGoogleServiceRealTimeBidding($client);

    $exampleClass = $_GET['example'];

    // If the exampleClass is selected, run it.
    if (isset($exampleClass)) {
        // Render the required action.
        $example = new $exampleClass($client);
        printHtmlHeader($example->getName());
        try {
            $example->execute();
        } catch (ApiException $ex) {
            printf('An error as occurred while calling the example:<br/>%s', $ex->getMessage());
        }
        printSampleHtmlFooter();
    } else {
        // Show the list of links to supported actions.
        printHtmlHeader('Real-Time Bidding API PHP usage examples.');
        printExamplesIndex($actions);
        printHtmlFooter();
    }

    // The access token may have been updated.
    $_SESSION['service_token'] = $client->getAccessToken();
}
