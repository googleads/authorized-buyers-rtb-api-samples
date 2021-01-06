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

using Google.Apis.RealTimeBidding.v1;
using Google.Apis.RealTimeBidding.v1.Data;
using Mono.Options;

using System;
using System.Collections.Generic;

namespace Google.Apis.RealTimeBidding.Examples.v1.Bidders.Creatives
{
    /// <summary>
    /// Enables monitoring of changes in creative statuses for a given bidder.
    ///
    /// Watched creatives will have changes to their status posted to Google Cloud Pub/Sub. For
    /// more details on Google Cloud Pub/Sub, see: https://cloud.google.com/pubsub/docs
    ///
    /// For an example of pulling creative status changes from a Google Cloud Pub/Sub subscription,
    /// see PullWatchedCreativesSubscription.cs.
    /// </summary>
    public class WatchCreatives : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public WatchCreatives()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "Enables watching creative status changes for the given bidder account.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id"};
            bool showHelp = false;

            string accountId = null;

            OptionSet options = new OptionSet {
                "Patches the specified creative.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the buyers resource under which the " +
                     "creatives were created. This will be used to construct the name used as " +
                     "a path parameter for the creatives.get request."),
                    a => accountId = a
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
            parsedArgs["account_id"] = accountId;
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
            var accountId = (string) parsedArgs["account_id"];
            var parentBidderName = $"bidders/{accountId}";

            WatchCreativesRequest body = new WatchCreativesRequest();

            BiddersResource.CreativesResource.WatchRequest request =
                rtbService.Bidders.Creatives.Watch(body, parentBidderName);

            WatchCreativesResponse response = null;

            Console.WriteLine("Watching creative status changes for bidder account with name: {0}",
                              parentBidderName);

            try
            {
                response = request.Execute();
            }
            catch (System.Exception exception)
            {
                throw new ApplicationException(
                    $"Real-time Bidding API returned error response:\n{exception.Message}");
            }

            Console.WriteLine("- Topic: {0}", response.Topic);
            Console.WriteLine("- Subscription: {0}", response.Subscription);
        }
    }
}
