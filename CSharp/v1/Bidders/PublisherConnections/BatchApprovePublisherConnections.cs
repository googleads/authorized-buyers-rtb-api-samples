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
    /// Batch approves one or more publisher connections.
    ///
    /// Approving a publisher connection means that the bidder agrees to receive bid requests from
    /// the publisher.
    /// </summary>
    public class BatchApprovePublisherConnections : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public BatchApprovePublisherConnections()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example batch approves one or more publisher connections.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "publisher_connection_ids"};
            bool showHelp = false;

            string accountId = null;
            IList<string> publisherConnectionIds = new List<string>();

            OptionSet options = new OptionSet {
                "Batch approves one or more publisher connections to a given bidder account.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the bidders resource for which the " +
                     "publisher connections are being approved."),
                    a => accountId = a
                },
                {
                    "p|publisher_connection_ids=",
                    ("[Required] One or more resource IDs for the bidders.publisherConnections " +
                     "resource that are being approved. Specify this argument for each value " +
                     "you intend to include. Values specified must be valid URLs. These will be " +
                     "used to construct the publisher connection names passed in the " +
                     "publisherConnections.batchApprove request body"),
                    p => publisherConnectionIds.Add(p)
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
            parsedArgs["publisher_connection_ids"] =
                publisherConnectionIds.Count > 0 ? publisherConnectionIds : null;
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
            string parent = $"bidders/{accountId}";

            IList<string> publisherConnectionIds =
                (IList<string>) parsedArgs["publisher_connection_ids"];
            IList<string> publisherConnectionNames = new List<string>();

            foreach (string publisherConnectionId in publisherConnectionIds)
            {
                publisherConnectionNames.Add(
                    $"bidders/{accountId}/publisherConnections/{publisherConnectionId}");
            }

            BatchApprovePublisherConnectionsRequest body =
                new BatchApprovePublisherConnectionsRequest();
            body.Names = publisherConnectionNames;

            BiddersResource.PublisherConnectionsResource.BatchApproveRequest request =
                rtbService.Bidders.PublisherConnections.BatchApprove(body, parent);
            BatchApprovePublisherConnectionsResponse response = null;

            Console.WriteLine(
                "Batch approving publisher connections for bidder with name: {0}",
                parent);

            try
            {
                response = request.Execute();
            }
            catch (System.Exception exception)
            {
                throw new ApplicationException(
                    $"Real-time Bidding API returned error response:\n{exception.Message}");
            }

            foreach (PublisherConnection publisherConnection in response.PublisherConnections)
            {
                Utilities.PrintPublisherConnection(publisherConnection);
            }
        }
    }
}
