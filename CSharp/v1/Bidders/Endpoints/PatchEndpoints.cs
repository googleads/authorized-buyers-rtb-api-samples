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
    /// Patches an endpoint with a specified name.
    /// </summary>
    public class PatchEndpoints : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public PatchEndpoints()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example patches a specified endpoint.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id", "endpoint_id"};
            bool showHelp = false;

            string accountId = null;
            string endpointId = null;
            string bidProtocol = null;
            long? maximumQps = null;
            string tradingLocation = null;


            OptionSet options = new OptionSet {
                "Patches a specified endpoint.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the bidders resource under which the " +
                     "endpoint exists."),
                    a => accountId = a
                },
                {
                    "e|endpoint_id=",
                    "[Required] The resource ID of the endpoint to be patched.",
                    e => endpointId = e
                },
                {
                    "b|bid_protocol=",
                    "The real-time bidding protocol that the endpoint is using.",
                    b => bidProtocol = b
                },
                {
                    "m|maximum_qps=",
                    "The maximum number of queries per second allowed to be sent to the endpoint.",
                    (long m) => maximumQps = m
                },
                {
                    "t|trading_location=",
                    ("Region where the endpoint and its infrastructure is located; corresponds " +
                     "to the location of users that bid requests are sent for."),
                    t => tradingLocation = t
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
            parsedArgs["bid_protocol"] = bidProtocol ?? "GOOGLE_RTB";
            parsedArgs["maximum_qps"] = maximumQps ?? 1L;
            parsedArgs["trading_location"] = tradingLocation ?? "US_EAST";
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

            Endpoint body = new Endpoint();
            body.BidProtocol = (string) parsedArgs["bid_protocol"];
            body.MaximumQps = (long?) parsedArgs["maximum_qps"];
            body.TradingLocation = (string) parsedArgs["trading_location"];

            BiddersResource.EndpointsResource.PatchRequest request =
                rtbService.Bidders.Endpoints.Patch(body, name);
            Endpoint response = null;

            Console.WriteLine("Patching endpoint with name {0}:", name);

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