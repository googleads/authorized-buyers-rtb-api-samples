/* Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

using Google.Apis.Pubsub.v1;
using Google.Apis.Pubsub.v1.Data;
using Mono.Options;

using System;
using System.Collections.Generic;
using System.Text;
using System.Text.Json;

namespace Google.Apis.RealTimeBidding.Examples.v1.Bidders.Creatives
{
    /// <summary>
    /// Pulls creative status changes from a Google Cloud Pub/Sub subscription.
    ///
    /// Note that messages do not expire until they are acknowledged; set the acknowledgement
    /// argument to "true" to acknowledge receiving all messages sent in the response.
    ///
    /// To learn more about Google Cloud Pub/Sub, read the developer documentation:
    /// https://cloud.google.com/pubsub/docs/overview
    /// https://cloud.google.com/pubsub/docs/reference/rest/v1/projects.subscriptions/list
    /// https://cloud.google.com/pubsub/docs/reference/rest/v1/projects.subscriptions/acknowledge
    /// </summary>
    public class PullWatchedCreativesSubscription : ExampleBase
    {
        private PubsubService pubSubService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public PullWatchedCreativesSubscription()
        {
            pubSubService = Utilities.GetPubSubService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example pulls creative status changes (if any) from a specified " +
                   "Google Cloud Pub/Sub subscription.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"subscription_name"};
            bool showHelp = false;

            string subscriptionName = null;
            int? maxMessages  = null;
            bool? acknowledge = null;

            OptionSet options = new OptionSet {
                ("Pulls creative status changes (if any) from a specified Google Cloud Pub/Sub " +
                 "subscription."),
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "s|subscription_name=",
                    ("[Required] The Google Cloud Pub/Sub subscription to be pulled. This value " +
                     "would be returned in the response from the bidders.creatives.watch " +
                     "method, and should be provided as-is in the form: " +
                     @"""projects/realtimebidding-pubsub/subscriptions/{subscription_id}"""),
                    s => subscriptionName = s
                },
                {
                    "m|max_messages=",
                    "The maximum number of messages to be returned in a single pull.",
                    (int m) => maxMessages =  m
                },
                {
                    "a|acknowledge",
                    ("Specify this argument to indicate that you want to acknowledge messages " +
                     "pulled from the subscription. Acknowledged messages won't appear in the " +
                     "subsequent responses to pulls from the subscription. By default, messages " +
                     "will not be acknowledged."),
                    a => acknowledge = a != null
                }
            };

            List<string> extras = options.Parse(exampleArgs);
            var parsedArgs = new Dictionary<string, object>();

            // Show help message.
            if(showHelp == true)
            {
                options.WriteOptionDescriptions(Console.Out);
                Environment.Exit(0);
            }
            // Set arguments.
            parsedArgs["subscription_name"] = subscriptionName;
            parsedArgs["max_messages"] = maxMessages ?? Utilities.MAX_PAGE_SIZE;
            parsedArgs["acknowledge"] = acknowledge ?? false;
            // Validate that options were set correctly.
            Utilities.ValidateOptions(options, parsedArgs, requiredOptions, extras);

            return parsedArgs;
        }

        /// <summary>
        /// Run the example.
        /// </summary>
        /// <param name="parsedArgs">Parsed arguments for the example.</param>
        protected override void Run(Dictionary<string, object> parsedArgs)
        {
            string subscriptionName = (string) parsedArgs["subscription_name"];

            Console.WriteLine(@"Retrieving messages from subscription: ""{0}""", subscriptionName);

            PullRequest pullRequest = new PullRequest();
            pullRequest.MaxMessages = (int?) parsedArgs["max_messages"];

            PullResponse response = null;

            try
            {
                response = pubSubService.Projects.Subscriptions.Pull(
                    pullRequest, subscriptionName).Execute();
            }
            catch (System.Exception exception)
            {
                throw new ApplicationException(
                    $"Google Cloud Pub/Sub API returned error response:\n{exception.Message}");
            }

            var ackIds = new List<string>();
            IList<ReceivedMessage> receivedMessages = response.ReceivedMessages;

            if(receivedMessages.Count == 0)
            {
                Console.Out.WriteLine("No messages received from the subscription.");
            }
            else
            {
                var options = new JsonSerializerOptions();
                options.WriteIndented = true;

                foreach(ReceivedMessage receivedMessage in receivedMessages)
                {
                ackIds.Add(receivedMessage.AckId);
                PubsubMessage message = receivedMessage.Message;
                IDictionary<string, string> messageAttributes = message.Attributes;
                var accountId = messageAttributes["accountId"];
                var creativeId = messageAttributes["creativeId"];

                Console.Out.WriteLine(@"* Creative found for buyer account ID ""{0}"" with " +
                                      @"creative ID ""{1}"" has been updated with the following " +
                                      "creative status:\n", accountId, creativeId);

                byte[] data = Convert.FromBase64String(message.Data);
                var decodedString = Encoding.UTF8.GetString(data);
                JsonElement jsonData = JsonSerializer.Deserialize<JsonElement>(decodedString);

                Console.Out.WriteLine(JsonSerializer.Serialize(jsonData, options));
                }

                if((bool) parsedArgs["acknowledge"])
                {
                    AcknowledgeRequest acknowledgeRequest = new AcknowledgeRequest();
                    acknowledgeRequest.AckIds = ackIds;

                    Console.Out.WriteLine(
                        "Acknowledging all {0} messages pulled from the subscription.",
                        ackIds.Count);

                    try
                    {
                        pubSubService.Projects.Subscriptions.Acknowledge(acknowledgeRequest,
                                                                         subscriptionName);
                    }
                    catch (System.Exception exception)
                    {
                        throw new ApplicationException(
                            "Google Cloud Pub/Sub API returned error response:\n" +
                            $"{exception.Message}");
                    }
                }
            }

        }
    }
}
