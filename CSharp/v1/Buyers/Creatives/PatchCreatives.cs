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
    /// Patches a creative with the specified name.
    /// </summary>
    public class PatchCreatives : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public PatchCreatives()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example patches a creative having the specified name.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "creative_id"};
            bool showHelp = false;

            string accountId = null;
            string creativeId = null;

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
                },
                {
                    "c|creative_id=",
                    ("[Required] The resource ID of the buyers.creatives resource for which the " +
                     "creative was created. This will be used to construct the name used as a " +
                     "path parameter for the creatives.patch request."),
                    c => creativeId = c
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
            parsedArgs["creative_id"] = creativeId;
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
            var creativeId = (string) parsedArgs["creative_id"];
            var name = $"buyers/{accountId}/creatives/{creativeId}";
            IList<String> declaredClickThroughUrls = new List<string>(new string[] {
                $"https://test.clickurl.com/{System.Guid.NewGuid()}",
                $"https://test.clickurl.com/{System.Guid.NewGuid()}",
                $"https://test.clickurl.com/{System.Guid.NewGuid()}"
            });

            Creative update = new Creative();
            update.AdvertiserName = $"Test-Advertiser-{System.Guid.NewGuid()}";
            update.DeclaredClickThroughUrls = declaredClickThroughUrls;

            BuyersResource.CreativesResource.PatchRequest request =
                rtbService.Buyers.Creatives.Patch(update, name);
            // Configure the update mask such that only the advertiserName and
            // declaredClickThroughUrls fields are updated. If not set, the patch method would
            // overwrite all other writable fields with a null value.
            request.UpdateMask = "advertiserName,declaredClickThroughUrls";

            Creative response = null;

            Console.WriteLine("Patching creative with name: {0}", name);

            try
            {
                response = request.Execute();
            }
            catch (System.Exception exception)
            {
                throw new ApplicationException(
                    $"Real-time Bidding API returned error response:\n{exception.Message}");
            }

            Utilities.PrintCreative(response);
        }
    }
}
