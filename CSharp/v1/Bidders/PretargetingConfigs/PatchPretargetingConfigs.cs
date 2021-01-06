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
    /// Patches a pretargeting configuration with a specified name.
    /// </summary>
    public class PatchPretargetingConfigs : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public PatchPretargetingConfigs()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example patches a specified pretargeting configuration.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "pretargeting_config_id"};
            bool showHelp = false;

            string accountId = null;
            long? pretargetingConfigId = null;
            string displayName = null;

            OptionSet options = new OptionSet {
                "Patches a specified pretargeting configuration.",
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
                    ("[Required] The resource ID of the pretargeting configuration to be " +
                     "patched."),
                    (long p) => pretargetingConfigId = p
                },
                {
                    "display_name=",
                    "The display name to associate with the new configuration. Must be unique " +
                    "among all of a bidder's pretargeting configurations.",
                    display_name => displayName = display_name
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
            parsedArgs["display_name"] = displayName ?? String.Format(
                "TEST_PRETARGETING_CONFIG_{0}",
                System.Guid.NewGuid());
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
            string name = $"bidders/{accountId}/pretargetingConfigs/{pretargetingConfigId}";

            NumericTargetingDimension geoTargeting = new NumericTargetingDimension();
            geoTargeting.IncludedIds = new List<long?>() {
                200635L,  // Austin, TX
                1014448L,  // Boulder, CO
                1022183L,  // Hoboken, NJ
                200622L,   // New Orleans, LA
                1023191L,  // New York, NY
                9061237L,  // Mountain View, CA
                1014221L   // San Francisco, CA
            };

            CreativeDimensions dimensions1 = new CreativeDimensions();
            dimensions1.Height = 480L;
            dimensions1.Width = 320L;

            CreativeDimensions dimensions2 = new CreativeDimensions();
            dimensions2.Height = 1080L;
            dimensions2.Width = 1920L;

            List<CreativeDimensions> creativeDimensions = new List<CreativeDimensions>() {
                dimensions1,
                dimensions2
            };

            PretargetingConfig body = new PretargetingConfig();
            body.DisplayName = (string) parsedArgs["display_name"];
            // Note that repeated fields such as this are completely overwritten by the contents
            // included in the patch request.
            body.IncludedFormats = new List<string>() {"HTML", "VAST"};
            body.GeoTargeting = geoTargeting;
            body.IncludedCreativeDimensions = creativeDimensions;

            BiddersResource.PretargetingConfigsResource.PatchRequest request =
                rtbService.Bidders.PretargetingConfigs.Patch(body, name);
            PretargetingConfig response = null;

            Console.WriteLine("Patching pretargeting configuration with name: {0}", name);

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