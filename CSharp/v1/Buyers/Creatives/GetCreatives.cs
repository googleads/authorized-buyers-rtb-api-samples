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

namespace Google.Apis.RealTimeBidding.Examples.v1.Buyers.Creatives
{
    /// <summary>
    /// Gets a single creative for the given buyer account ID and creative ID.
    /// </summary>
    public class GetCreatives : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public GetCreatives()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get { return "This code example gets a specific creative for a buyer account."; }
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            bool showHelp = false;

            string accountId = null;
            string creativeId = null;
            string view = null;

            OptionSet options = new OptionSet {
                "Get a creative for a given buyer account ID and creative ID.",
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
                },
                {
                    "c|creative_id=",
                    ("[Required] The resource ID of the buyers.creatives resource for which the " +
                     "creative was created. This will be used to construct the name used as a " +
                     "path parameter for the creatives.get request."),
                    c => creativeId = c
                },
                {
                    "v|view=",
                    ("Controls the amount of information included in the response. By default, " +
                     "the creatives.get method only includes creativeServingDecision. This " +
                     "sample configures the view to return the full contents of the creative " +
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

            if(creativeId == null)
            {
                Console.Error.WriteLine("Required argument \"creative_id\" not specified.");
                options.WriteOptionDescriptions(Console.Error);
                Environment.Exit(1);
            }
            else
            {
                parsedArgs["creative"] = creativeId;
            }
            // Set optional arguments.
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
            string creativeId = (string) parsedArgs["creativeId"];
            string name = $"buyers/{accountId}/creatives/{creativeId}";

            BuyersResource.CreativesResource.GetRequest request =
                rtbService.Buyers.Creatives.Get(name);
            request.View = (BuyersResource.CreativesResource.GetRequest.ViewEnum) Enum.Parse(
                typeof(BuyersResource.CreativesResource.ListRequest.ViewEnum),
                (string) parsedArgs["view"],
                false);
            Creative response = null;

            Console.WriteLine("Getting creative with name: {0}", name);

            try
            {
                response = request.Execute();
            }
            catch (System.Exception exception)
            {
                Console.Error.WriteLine("Real-time Bidding API returned error response:\n{0}",
                                        exception.Message);
                Environment.Exit(1);
            }

            Utilities.PrintCreative(response);
        }
    }
}