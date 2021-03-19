/* Copyright 2021 Google LLC
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

namespace Google.Apis.RealTimeBidding.Examples.v1.Bidders
{
    /// <summary>
    /// Lists bidders associated with the authorized service account.
    /// </summary>
    public class ListBidders : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public ListBidders()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example lists all bidders associated with the service account " +
                   "specified for the OAuth 2.0 flow in Utilities.cs.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {};
            bool showHelp = false;
            int? pageSize = null;

            OptionSet options = new OptionSet {
                "List bidders associated with the authorized service account.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "p|page_size=",
                    ("The number of rows to return per page. The server may return fewer rows " +
                     "than specified."),
                    (int p) => pageSize =  p
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
            parsedArgs["page_size"] = pageSize ?? Utilities.MAX_PAGE_SIZE;
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
            string pageToken = null;

            Console.WriteLine("Listing bidders for the authorized service account:");
            do
            {
                BiddersResource.ListRequest request = rtbService.Bidders.List();
                request.PageSize = (int) parsedArgs["page_size"];
                request.PageToken = pageToken;

                ListBiddersResponse page = null;

                try
                {
                    page = request.Execute();
                }
                catch (System.Exception exception)
                {
                    throw new ApplicationException(
                        $"Real-time Bidding API returned error response:\n{exception.Message}");
                }

                var bidders = page.Bidders;
                pageToken = page.NextPageToken;

                if(bidders == null)
                {
                    Console.WriteLine("No bidders found.");
                }
                else
                {
                    foreach (Bidder bidder in bidders)
                        {
                            Utilities.PrintBidder(bidder);
                        }
                }
            }
            while(pageToken != null);
        }
    }
}
