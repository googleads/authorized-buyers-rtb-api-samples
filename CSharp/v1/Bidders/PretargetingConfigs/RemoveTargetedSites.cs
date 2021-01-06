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
    /// Removes site URLs from pretargeting configuration's web targeting.
    ///
    /// Note that this is the only way to remove targeted URLs following a pretargeting
    /// configuration's creation.
    /// </summary>
    public class RemoveTargetedSites : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public RemoveTargetedSites()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example removes site URLs from a pretargeting configuration's " +
                   "web targeting.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {
                "account_id", "pretargeting_config_id", "web_targeting_urls"
            };
            bool showHelp = false;

            string accountId = null;
            long? pretargetingConfigId = null;
            IList<string> webTargetingUrls = new List<string>();

            OptionSet options = new OptionSet {
                "Removes site URLs from a pretargeting configuration's web targeting.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the bidders resource under which the " +
                     "pretargeting configuration was created."),
                    a => accountId = a
                },
                {
                    "p|pretargeting_config_id=",
                    ("[Required] The resource ID of the pretargeting configuration that is " +
                     "being acted upon."),
                    (long p) => pretargetingConfigId = p
                },
                {
                    "web_targeting_urls=",
                    ("[Required] The URLs to be removed from this configuration's web " +
                     "targeting. Specify this argument for each value you intend to include. " +
                     "Values specified must be valid URLs."),
                    web_targeting_urls => webTargetingUrls.Add(web_targeting_urls)
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
            parsedArgs["pretargeting_config_id"] = pretargetingConfigId;
            parsedArgs["web_targeting_urls"] = webTargetingUrls.Count > 0 ? webTargetingUrls : null;
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
            long? pretargetingConfigId = (long?) parsedArgs["pretargeting_config_id"];
            string pretargetingConfigName =
                $"bidders/{accountId}/pretargetingConfigs/{pretargetingConfigId}";


            RemoveTargetedSitesRequest body = new RemoveTargetedSitesRequest();
            body.Sites = (IList<string>) parsedArgs["web_targeting_urls"];

            BiddersResource.PretargetingConfigsResource.RemoveTargetedSitesRequest request =
                rtbService.Bidders.PretargetingConfigs.RemoveTargetedSites(
                    body, pretargetingConfigName);
            PretargetingConfig response = null;

            Console.WriteLine("Removing site URLs from web targeting for pretargeting " +
                              "configuration with name: {0}", pretargetingConfigName);
            try
            {
                response = request.Execute();
            }
            catch (System.Exception exception)
            {
                throw new ApplicationException(
                    $"Real-time Bidding API returned error response:\n{exception.Message}");
            }

            Utilities.PrintPretargetingConfiguration(response);
        }
    }
}