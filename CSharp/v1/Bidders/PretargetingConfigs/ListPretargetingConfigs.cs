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

namespace Google.Apis.RealTimeBidding.Examples.v1.Bidders.PretargetingConfigs
{
    /// <summary>
    /// Lists pretargeting configurations for a given bidder account ID.
    /// </summary>
    public class ListPretargetingConfigs : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public ListPretargetingConfigs()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example lists all pretargeting configurations for a given bidder " +
                   "account.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id"};
            bool showHelp = false;

            string accountId = null;
            int? pageSize = null;

            OptionSet options = new OptionSet {
                "List pretargeting configurations for the given bidder account.",
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

            Console.WriteLine(@"Listing pretargeting configurations for bidder account ""{0}""",
                              parent);
            do
            {
                BiddersResource.PretargetingConfigsResource.ListRequest request =
                   rtbService.Bidders.PretargetingConfigs.List(parent);
                request.PageSize = (int) parsedArgs["pageSize"];
                request.PageToken = pageToken;

                ListPretargetingConfigsResponse page = null;

                try
                {
                    page = request.Execute();
                }
                catch (System.Exception exception)
                {
                    throw new ApplicationException(
                        $"Real-time Bidding API returned error response:\n{exception.Message}");
                }

                var pretargetingConfigs = page.PretargetingConfigs;
                pageToken = page.NextPageToken;

                if(pretargetingConfigs == null)
                {
                    Console.WriteLine("No pretargeting configurations found for bidder account.");
                }
                else
                {
                    foreach (PretargetingConfig config in pretargetingConfigs)
                        {
                            Utilities.PrintPretargetingConfiguration(config);
                        }
                }
            }
            while(pageToken != null);
        }
    }
}