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

namespace Google.Apis.RealTimeBidding.Examples.v1.Bidders.Endpoints
{
    /// <summary>
    /// Gets a single endpoint for the specified bidder and endpoint IDs.
    /// </summary>
    public class GetEndpoints : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public GetEndpoints()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "Get an endpoint for the given bidder and endpoint IDs.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "endpoint_id"};
            bool showHelp = false;

            string accountId = null;
            string endpointId = null;

            OptionSet options = new OptionSet {
                "Get an endpoint for the given bidder and endpoint IDs.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the bidders resource under which the " +
                     "endpoint exists. This will be used to construct the name used as a path " +
                     "parameter for the endpoints.get request."),
                    a => accountId = a
                },
                {
                    "e|endpoint_id=",
                    ("[Required] The resource ID of the endpoints resource that is being " +
                     "retrieved. This will be used to construct the name used as a path " +
                     "parameter for the endpoints.get request."),
                    e => endpointId = e
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
            parsedArgs["endpoint_id"] = endpointId;
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
            string endpointId = (string) parsedArgs["endpoint_id"];
            string name = $"bidders/{accountId}/endpoints/{endpointId}";

            BiddersResource.EndpointsResource.GetRequest request =
                rtbService.Bidders.Endpoints.Get(name);
            Endpoint response = null;

            Console.WriteLine("Getting endpoint with name: {0}", name);

            try
            {
                response = request.Execute();
            }
            catch (System.Exception exception)
            {
                throw new ApplicationException(
                    $"Real-time Bidding API returned error response:\n{exception.Message}");
            }

            Utilities.PrintEndpoint(response);
        }
    }
}
