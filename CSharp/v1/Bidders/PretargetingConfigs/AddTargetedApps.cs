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
    /// Adds mobile application IDs to a pretargeting configuration's app targeting.
    ///
    /// Note that this is the only way to append mobile application IDs following a pretargeting
    /// configuration's creation. If a pretargeting configuration already targets mobile
    /// application IDs, you must specify a targeting mode that is identical to the existing
    /// targeting mode.
    /// </summary>
    public class AddTargetedApps : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public AddTargetedApps()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example adds mobile application IDs to a pretargeting " +
                   "configuration's app targeting.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {
                "account_id", "pretargeting_config_id", "mobile_app_targeting_mode",
                "mobile_app_targeting_app_ids"
            };
            bool showHelp = false;

            string accountId = null;
            long? pretargetingConfigId = null;
            string mobileAppTargetingMode = null;
            IList<string> mobileAppTargetingAppIds = new List<string>();

            OptionSet options = new OptionSet {
                "Adds mobile application IDs to a pretargeting configuration's app targeting.",
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
                    "mobile_app_targeting_mode=",
                    ("[Required] The targeting mode for this configuration's mobile app " +
                     "targeting. Valid values include: INCLUSIVE, and EXCLUSIVE. Note that if " +
                     "the configuration already targets mobile app Ids, you must specify an " +
                     "identical targeting mode."),
                    mobile_app_targeting_mode => mobileAppTargetingMode = mobile_app_targeting_mode
                },
                {
                    "mobile_app_targeting_app_ids=",
                    ("[Required] The mobile app IDs specified for this configuration's mobile " +
                     "app targeting, which allows one to target a subset of mobile app " +
                     "inventory. Specify this argument for each value you intend to include. " +
                     "Values specified must be valid URLs."),
                    mobile_app_targeting_app_ids => mobileAppTargetingAppIds.Add(
                        mobile_app_targeting_app_ids)
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
            parsedArgs["mobile_app_targeting_mode"] = mobileAppTargetingMode;
            parsedArgs["mobile_app_targeting_app_ids"] =
                mobileAppTargetingAppIds.Count > 0 ? mobileAppTargetingAppIds : null;
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


            AddTargetedAppsRequest body = new AddTargetedAppsRequest();
            body.TargetingMode = (string) parsedArgs["mobile_app_targeting_mode"];
            body.AppIds = (IList<string>) parsedArgs["mobile_app_targeting_app_ids"];

            BiddersResource.PretargetingConfigsResource.AddTargetedAppsRequest request =
                rtbService.Bidders.PretargetingConfigs.AddTargetedApps(body, pretargetingConfigName);
            PretargetingConfig response = null;

            Console.WriteLine("Updating mobile app targeting with new app IDs for pretargeting " +
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