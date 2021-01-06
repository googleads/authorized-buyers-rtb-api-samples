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
    /// Removes publisher IDs from a pretargeting configuration's publisher targeting.
    ///
    /// Note that this is the only way to remove publisher IDs following a pretargeting
    /// configuration's creation.
    /// </summary>
    public class RemoveTargetedPublishers : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public RemoveTargetedPublishers()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example removes publisher IDs from a pretargeting configuration's " +
                   "publisher targeting.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {
                "account_id", "pretargeting_config_id", "publisher_ids"
            };
            bool showHelp = false;

            string accountId = null;
            long? pretargetingConfigId = null;
            IList<string> publisherIds = new List<string>();

            OptionSet options = new OptionSet {
                "Removes publisher IDs from a pretargeting configuration's publisher targeting.",
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
                    "publisher_ids=",
                    ("[Required] The publisher IDs to be removed from this configuration's " +
                     "publisher targeting. Specify this argument for each value you intend to " +
                     "include. Valid publisher IDs can be found in Real-time Bidding bid " +
                     "requests, or alternatively in ads.txt / app-ads.txt. For more " +
                     "information, see: https://iabtechlab.com/ads-txt/"),
                    publisher_ids => publisherIds.Add(publisher_ids)
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
            parsedArgs["publisher_ids"] = publisherIds.Count > 0 ? publisherIds : null;
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


            RemoveTargetedPublishersRequest body = new RemoveTargetedPublishersRequest();
            body.PublisherIds = (IList<string>) parsedArgs["publisher_ids"];

            BiddersResource.PretargetingConfigsResource.RemoveTargetedPublishersRequest request =
                rtbService.Bidders.PretargetingConfigs.RemoveTargetedPublishers(
                    body, pretargetingConfigName);
            PretargetingConfig response = null;

            Console.WriteLine("Removing publisher IDs from publisher targeting for pretargeting " +
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