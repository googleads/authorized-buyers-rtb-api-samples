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
    /// Creates a creative with HTML content for the given buyer account ID.
    /// </summary>
    public class CreateHtmlCreatives : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public CreateHtmlCreatives()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get { return "This code example creates a creative with HTML content for the given " +
                         "buyer account ID."; }
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            bool showHelp = false;

            string accountId = null;
            string advertiserName = null;
            string creativeId = null;
            IList<string> declaredAttributes = new List<string>();
            IList<string> declaredClickUrls = new List<string>();
            IList<string> declaredRestrictedCategories = new List<string>();
            IList<int?> declaredVendorIds = new List<int?>();
            string htmlSnippet = null;
            int? htmlHeight = null;
            int? htmlWidth = null;

            var defaultHtmlSnippet = "<iframe marginwidth=0 marginheight=0 height=600 " +
                "frameborder=0 width=160 scrolling=no " +
                "src=\"https://test.com/ads?id=123456&curl=%%CLICK_URL_ESC%%" +
                "&wprice=%%WINNING_PRICE_ESC%%\"></iframe>";

            OptionSet options = new OptionSet {
                "Creates a creative with HTML content for the given buyer account ID.",
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
                    "advertiser_name=",
                    "The name of the company being advertised in the creative.",
                    advertiser_name => advertiserName = advertiser_name
                },
                {
                    "c|creative_id=",
                    ("The user-specified creative ID. The maximum length of the creative ID is " +
                     "128 bytes."),
                    c => creativeId = c
                },
                {
                    "declared_attributes=",
                    ("The creative attributes being declared. Specify this argument for each " +
                     "value you intend to include."),
                    declared_attributes => declaredAttributes.Add(declared_attributes)
                },
                {
                    "declared_click_urls=",
                    ("The click-through URLs being declared. Specify this argument for each " +
                     "value you intend to include."),
                    declared_click_through_urls => declaredClickUrls.Add(
                        declared_click_through_urls)
                },
                {
                    "declared_restricted_categories=",
                    ("The restricted categories being declared. Specify this argument for each " +
                    "value you intend to include."),
                    declared_restricted_categories => declaredRestrictedCategories.Add(
                        declared_restricted_categories)
                },
                {
                    "declared_vendor_ids=",
                    ("The vendor IDs being declared. Specify this argument for each value you " +
                     "intend to include."),
                    (int declared_vendor_ids) => declaredVendorIds.Add(declared_vendor_ids)
                },
                {
                    "html_snippet=",
                    "The HTML snippet that displays the ad when inserted in the web page.",
                    html_snippet => htmlSnippet = html_snippet
                },
                {
                    "html_height=",
                    "The height of the HTML snippet in pixels.",
                    (int html_height) => htmlHeight = html_height
                },
                {
                    "html_width=",
                    "The width of the HTML snippet in pixels.",
                    (int html_width) => htmlWidth = html_width
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
            parsedArgs["advertiser_name"] = advertiserName ?? "Test";
            parsedArgs["creative_id"] = creativeId ?? String.Format(
                "HTML_Creative_{0}",
                System.Guid.NewGuid());

            if (declaredAttributes.Count == 0)
            {
                declaredAttributes.Add("CREATIVE_TYPE_HTML");
            }
            parsedArgs["declared_attributes"] = declaredAttributes;

            if (declaredClickUrls.Count == 0)
            {
                declaredClickUrls.Add("http://test.com");
            }

            parsedArgs["declared_click_urls"] = declaredClickUrls;
            parsedArgs["declared_restricted_categories"] = declaredRestrictedCategories;
            parsedArgs["declared_vendor_ids"] = declaredVendorIds;
            parsedArgs["html_snippet"] = htmlSnippet ?? defaultHtmlSnippet;
            parsedArgs["html_height"] = htmlHeight ?? 250;
            parsedArgs["html_width"] = htmlWidth ?? 300;

            return parsedArgs;
        }

        /// <summary>
        /// Run the example.
        /// </summary>
        /// <param name="parsedArgs">Parsed arguments for the example.</param>
        protected override void Run(Dictionary<string, object> parsedArgs)
        {
            string accountId = (string) parsedArgs["accountId"];
            string parent = $"buyers/{accountId}";

            HtmlContent htmlContent = new HtmlContent();
            htmlContent.Snippet = (string) parsedArgs["html_snippet"];
            htmlContent.Height = (int) parsedArgs["html_height"];
            htmlContent.Width = (int) parsedArgs["html_width"];

            Creative newCreative = new Creative();
            newCreative.AdvertiserName = (string) parsedArgs["advertiser_name"];
            newCreative.CreativeId = (string) parsedArgs["creative_id"];
            newCreative.DeclaredAttributes = (IList<string>) parsedArgs["declared_attributes"];
            newCreative.DeclaredClickThroughUrls = (IList<string>) parsedArgs[
                "declared_click_urls"];
            newCreative.DeclaredRestrictedCategories = (IList<string>) parsedArgs[
                "declared_restricted_categories"];
            newCreative.DeclaredVendorIds = (IList<int?>) parsedArgs["declared_vendor_ids"];
            newCreative.Html = htmlContent;

            BuyersResource.CreativesResource.CreateRequest request =
                rtbService.Buyers.Creatives.Create(newCreative, parent);
            Creative response = null;

            Console.WriteLine("Creating HTML creative for buyer: {0}", parent);

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