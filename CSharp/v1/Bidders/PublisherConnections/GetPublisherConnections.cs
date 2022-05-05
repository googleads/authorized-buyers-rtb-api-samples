/* Copyright 2022 Google LLC
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

namespace Google.Apis.RealTimeBidding.Examples.v1.Bidders.PublisherConnections
{
    /// <summary>
    /// Gets a single publisher connection with a specified name.
    /// </summary>
    public class GetPublisherConnections : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public GetPublisherConnections()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example gets a specified publisher connection.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "publisher_connection_id"};
            bool showHelp = false;

            string accountId = null;
            string publisherConnectionId = null;

            OptionSet options = new OptionSet {
                "Gets a specified publisher connection.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the bidders resource under which the " +
                     "publisher connection exists. This will be used to construct the name used " +
                     "as a path parameter for the publisherConnections.get request."),
                    a => accountId = a
                },
                {
                    "p|publisher_connection_id=",
                    ("[Required] The resource ID of the publisher connection that is being " +
                     "retrieved. This value is the publisher ID found in ads.txt or " +
                     "app-ads.txt, and is used to construct the name used as a path parameter " +
                     "for the publisherConnections.get request."),
                    p => publisherConnectionId = p
                },
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
            parsedArgs["publisher_connection_id"] = publisherConnectionId;
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
            string accountId = (string) parsedArgs["account_id"];
            string publisherConnectionId = (string) parsedArgs["publisher_connection_id"];
            string name = $"bidders/{accountId}/publisherConnections/{publisherConnectionId}";

            BiddersResource.PublisherConnectionsResource.GetRequest request =
                rtbService.Bidders.PublisherConnections.Get(name);

            PublisherConnection response = null;

            Console.WriteLine("Retrieving publisher connection with name: '{0}'", name);

            try
            {
                response = request.Execute();
            }
            catch (System.Exception exception)
            {
                throw new ApplicationException(
                    $"Real-time Bidding API returned error response:\n{exception.Message}");
            }

            Utilities.PrintPublisherConnection(response);
        }
    }
}