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
    /// Lists creatives for a given bidder account ID.
    /// </summary>
    public class ListCreatives : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public ListCreatives()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get { return "This code example lists all creatives for a given bidder account."; }
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            bool showHelp = false;
            string defaultFilter = "creativeServingDecision.networkPolicyCompliance.status=" +
                                   "APPROVED AND creativeFormat=HTML";
            string accountId = null;
            string filter = null;
            int? pageSize = null;
            string view = null;

            OptionSet options = new OptionSet {
                "List creatives for the given buyer account.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the bidders resource under which the " +
                     "creatives were created. This will be used to construct the parent used as " +
                     "a path parameter for the creatives.list request."),
                    a => accountId = a
                },
                {
                    "p|page_size=",
                    ("The number of rows to return per page. The server may return fewer rows " +
                     "than specified."),
                    (int p) => pageSize =  p
                },
                {
                    "f|filter=",
                    ("Query string to filter creatives. If no filter is specified, all active " +
                     "creatives will be returned. To demonstrate usage, the default behavior of" +
                     "this sample is to filter such that only approved HTML snippet creatives " +
                     "are returned."),
                    f => filter = f
                },
                {
                    "v|view=",
                    ("Controls the amount of information included in the response. By default, " +
                     "the creatives.list method only includes creativeServingDecision. This " +
                     "sample configures the view to return the full contents of the creatives " +
                     "by setting this to \"FULL\"."),
                    v => view = v
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
            // Handle error conditions.
            if(extras.Count > 0)
            {
                Console.Error.WriteLine("Unknown arguments specified:");
                foreach(string arg in extras)
                {
                    Console.Error.WriteLine(arg);
                }
                Environment.Exit(1);
            }
            // Verify required arguments were set.
            if(accountId == null)
            {
                Console.Error.WriteLine("Required argument \"account_id\" not specified.");
                options.WriteOptionDescriptions(Console.Error);
                Environment.Exit(1);
            }
            else
            {
                parsedArgs["accountId"] = accountId;
            }
            // Set optional arguments.
            parsedArgs["pageSize"] = pageSize ?? Utilities.MAX_PAGE_SIZE;
            parsedArgs["filter"] = filter ?? defaultFilter;
            parsedArgs["view"] = view ?? "FULL";

            return parsedArgs;
        }

        /// <summary>
        /// Run the example.
        /// </summary>
        /// <param name="parsedArgs">Parsed arguments for the example.</param>
        protected override void Run(Dictionary<string, object> parsedArgs)
        {
            string accountId = (string) parsedArgs["accountId"];
            string parent = $"bidders/{accountId}";
            string pageToken = null;

            Console.WriteLine("Listing creatives for bidder account \"{0}\"", parent);
            do
            {
                BiddersResource.CreativesResource.ListRequest request =
                   rtbService.Bidders.Creatives.List(parent);
                request.Filter = (string) parsedArgs["filter"];
                request.PageSize = (int) parsedArgs["pageSize"];
                request.PageToken = pageToken;
                request.View = (BiddersResource.CreativesResource.ListRequest.ViewEnum) Enum.Parse(
                    typeof(BiddersResource.CreativesResource.ListRequest.ViewEnum),
                    (string) parsedArgs["view"],
                    false);

                ListCreativesResponse page = null;

                try
                {
                    page = request.Execute();
                }
                catch (System.Exception exception)
                {
                    Console.Error.WriteLine("Real-time Bidding API returned error response:\n{0}",
                                            exception.Message);
                    Environment.Exit(1);
                }

                var creatives = page.Creatives;
                pageToken = page.NextPageToken;

                if(creatives.Count == 0)
                {
                    Console.WriteLine("No creatives found for buyer account.");
                }
                else
                {
                    foreach (Creative creative in creatives)
                        {
                            Utilities.PrintCreative(creative);
                        }
                }
            }
            while(pageToken != null);
        }
    }
}