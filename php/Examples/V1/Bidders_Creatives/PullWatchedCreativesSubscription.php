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
use Google_Service_Pubsub_AcknowledgeRequest;
use Google_Service_Pubsub_PullRequest;

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
class PullWatchedCreativesSubscription extends BaseExample
{

    public function __construct($client)
    {
        $this->service = Config::getGoogleServicePubSub($client);
    }

    /**
     * @see BaseExample::getInputParameters()
     */
    protected function getInputParameters()
    {
        return [
            [
                'name' => 'subscription_name',
                'display' => 'Subscription name',
                'required' => true,
                'description' =>
                    'The Google Cloud Pub/Sub subscription to be pulled. This value would be ' .
                    'returned in the response from the bidders.creatives.watch method, and ' .
                    'should be provided as-is in the form:</br>' .
                    '"projects/realtimebidding-pubsub/subscriptions/{subscription_id}"'
            ],
            [
                'name' => 'acknowledge',
                'display' => 'Acknowledge',
                'required' => false,
                'description' =>
                    'A boolean describing whether to acknowledge the messages pulled from the ' .
                    'subscription. Acknowledged messages won\'t appear in subsequent responses ' .
                    'to pulls from the subscription.',
                'default' => 'false'
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;

        $subscriptionName = $values['subscription_name'];
        $acknowledge = filter_var($values['acknowledge'], FILTER_VALIDATE_BOOLEAN);

        $pullRequest = new Google_Service_Pubsub_PullRequest();
        $pullRequest->maxMessages = 10;

        $ackIds = [];

        try {
            $response = $this->service->projects_subscriptions->pull($subscriptionName, $pullRequest);
            print "<h2>Pulled subscription '$subscriptionName' with results:</h2>";

            if (empty($response['receivedMessages'])) {
                print '<p>No messages received from the subscription.</p>';
                return;
            } else {
                print '<ul>';
                foreach ($response['receivedMessages'] as $receivedMessage) {
                    $ackIds[] = $receivedMessage['ackId'];
                    $message = $receivedMessage['message'];
                    $accountId = $message['attributes']['accountId'];
                    $creativeId = $message['attributes']['creativeId'];
                    $creativeServingDecision = $this->getPrettyPrintCreativeServingDecision(
                        $message['data']
                    );

                    print "<li>Creative found for buyer account ID '$accountId' with creative " .
                        "ID '$creativeId' has been updated with the following creative status:";

                    print '</br>';
                    print "<pre>$creativeServingDecision</pre>";
                    print '</li>';
                }
                print '</ul>';
            }
        } catch (Google_Service_Exception $ex) {
            if ($ex->getCode() === 404 || $ex->getCode() === 403) {
                print '<h1>Subscription not found or don\'t have access.</h1>';
            } else {
                throw $ex;
            }
        }

        if ($acknowledge) {
            $ackRequest = new Google_Service_Pubsub_AcknowledgeRequest();
            $ackRequest->ackIds = $ackIds;

            try {
                $ackResponse = $this->service->projects_subscriptions->acknowledge(
                    $subscriptionName,
                    $ackRequest
                );
                printf('Acknowledged %d messages pulled from the subscription.', count($ackIds));
                print '</br></br>';
            } catch (Google_Service_Exception $ex) {
                if ($ex->getCode() === 404 || $ex->getCode() === 403) {
                    print '<h1>AckIDs not found or do not have access.</h1>';
                } else {
                    throw $ex;
                }
            }
        }
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Pull Watched Creatives Subscription';
    }

    public function getPrettyPrintCreativeServingDecision($data)
    {
        return json_encode(json_decode(base64_decode($data)), JSON_PRETTY_PRINT);
    }
}
