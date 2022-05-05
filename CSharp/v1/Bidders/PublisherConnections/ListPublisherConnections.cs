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
    /// Lists publisher connections for a given bidder account ID.
    /// </summary>
    public class ListPublisherConnections : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public ListPublisherConnections()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example lists all publisher connections for a given bidder " +
                   "account.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id"};
            bool showHelp = false;

            string accountId = null;
            string defaultFilter = "publisherPlatform = GOOGLE_AD_MANAGER";
            string defaultOrderBy = "createTime DESC";
            string filter = null;
            string orderBy = null;
            int? pageSize = null;

            OptionSet options = new OptionSet {
                "List publisher connections for the given bidder account.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the bidders resource under which the " +
                     "pretargeting configurations were created. This will be used to construct " +
                     "the parent used as a path parameter for the pretargetingConfigs.list " +
                     "request."),
                    a => accountId = a
                },
                {
                    "f|filter=",
                    ("Query string to filter publisher connections. To demonstrate usage, this " +
                     "sample will default to filtering by publisherPlatform."),
                    f => filter =  f
                },
                {
                    "o|order_by=",
                    ("A string specifying the order by which results should be sorted. To " +
                     "demonstrate usage, this sample will default to ordering by createTime."),
                    o => orderBy =  o
                },
                {
                    "p|page_size=",
                    ("The number of rows to return per page. The server may return fewer rows " +
                     "than specified."),
                    (int p) => pageSize =  p
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
            parsedArgs["filter"] = filter ?? defaultFilter;
            parsedArgs["order_by"] = orderBy ?? defaultOrderBy;
            parsedArgs["pageSize"] = pageSize ?? Utilities.MAX_PAGE_SIZE;
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
            string pageToken = null;

            Console.WriteLine(@"Listing publisher connections for bidder account ""{0}""", parent);
            do
            {
                BiddersResource.PublisherConnectionsResource.ListRequest request =
                   rtbService.Bidders.PublisherConnections.List(parent);
                request.Filter = (string) parsedArgs["filter"];
                request.OrderBy = (string) parsedArgs["order_by"];
                request.PageSize = (int) parsedArgs["pageSize"];
                request.PageToken = pageToken;

                ListPublisherConnectionsResponse page = null;

                try
                {
                    page = request.Execute();
                }
                catch (System.Exception exception)
                {
                    throw new ApplicationException(
                        $"Real-time Bidding API returned error response:\n{exception.Message}");
                }

                var publisherConnections = page.PublisherConnections;
                pageToken = page.NextPageToken;

                if(publisherConnections == null)
                {
                    Console.WriteLine("No publisher connections found for bidder account.");
                }
                else
                {
                    foreach (PublisherConnection connection in publisherConnections)
                        {
                            Utilities.PrintPublisherConnection(connection);
                        }
                }
            }
            while(pageToken != null);
        }
    }
}