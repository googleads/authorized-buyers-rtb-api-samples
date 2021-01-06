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
    /// Creates a creative with native content for the given buyer account ID.
    /// </summary>
    public class CreateNativeCreatives : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public CreateNativeCreatives()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example creates a creative with native content for the given " +
                   "buyer account ID.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id"};
            bool showHelp = false;

            string accountId = null;
            string advertiserName = null;
            string creativeId = null;
            IList<string> declaredAttributes = new List<string>();
            IList<string> declaredClickUrls = new List<string>();
            IList<string> declaredRestrictedCategories = new List<string>();
            IList<int?> declaredVendorIds = new List<int?>();
            string nativeHeadline = null;
            string nativeBody = null;
            string nativeCallToAction = null;
            string nativeAdvertiserName = null;
            string nativeImageUrl = null;
            int? nativeImageHeight = null;
            int? nativeImageWidth = null;
            string nativeLogoUrl = null;
            int? nativeLogoHeight = null;
            int? nativeLogoWidth = null;
            string nativeClickLinkUrl = null;
            string nativeClickTrackingUrl = null;

            OptionSet options = new OptionSet {
                "Creates a creative with native content for the given buyer account ID.",
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
                    "native_headline=",
                    "A short title for the ad.",
                    native_headline => nativeHeadline = native_headline
                },
                {
                    "native_body=",
                    "A long description of the ad.",
                    native_body => nativeBody = native_body
                },
                {
                    "native_call_to_action=",
                    "A label for the button that the user is supposed to click.",
                    native_call_to_action => nativeCallToAction = native_call_to_action
                },
                {
                    "native_advertiser_name=",
                    "The name of the advertiser or sponsor, to be displayed in the ad creative.",
                    native_advertiser_name => nativeAdvertiserName = native_advertiser_name
                },
                {
                    "native_image_url=",
                    "The URL of the large image to be included in the native ad.",
                    native_image_url => nativeImageUrl = native_image_url
                },
                {
                    "native_image_height=",
                    "The height in pixels of the native ad's large image.",
                    (int native_image_height) => nativeImageHeight = native_image_height
                },
                {
                    "native_image_width=",
                    "The width in pixels of the native ad's large image.",
                    (int native_image_width) => nativeImageWidth = native_image_width
                },
                {
                    "native_logo_url=",
                    "The URL of a smaller image to be included in the native ad.",
                    native_logo_url => nativeLogoUrl = native_logo_url
                },
                {
                    "native_logo_height=",
                    "The height in pixels of the native ad's smaller image.",
                    (int native_logo_height) => nativeLogoHeight = native_logo_height
                },
                {
                    "native_logo_width=",
                    "The height in pixels of the native ad's smaller image.",
                    (int native_logo_width) => nativeLogoWidth = native_logo_width
                },
                {
                    "native_click_link_url=",
                    "The URL that the browser/SDK will load when the user clicks the ad.",
                    native_click_link_url => nativeClickLinkUrl = native_click_link_url
                },
                {
                    "native_click_tracking_url=",
                    "The URL to use for click tracking.",
                    native_click_tracking_url => nativeClickTrackingUrl = native_click_tracking_url
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
            parsedArgs["advertiser_name"] = advertiserName ?? "Test";
            parsedArgs["creative_id"] = creativeId ?? String.Format(
                "Native_Creative_{0}", System.Guid.NewGuid());

            if (declaredAttributes.Count == 0)
            {
                declaredAttributes.Add("NATIVE_ELIGIBILITY_ELIGIBLE");
            }
            parsedArgs["declared_attributes"] = declaredAttributes;

            if (declaredClickUrls.Count == 0)
            {
                declaredClickUrls.Add("http://test.com");
            }

            parsedArgs["declared_click_urls"] = declaredClickUrls;
            parsedArgs["declared_restricted_categories"] = declaredRestrictedCategories;
            parsedArgs["declared_vendor_ids"] = declaredVendorIds;
            parsedArgs["native_headline"] = nativeHeadline ?? "Luxury Mars Cruises";
            parsedArgs["native_body"] = nativeBody ?? "Visit the planet in a luxury spaceship.";
            parsedArgs["native_call_to_action"] = nativeHeadline ?? "Book today";
            parsedArgs["native_advertiser_name"] =
                nativeAdvertiserName ?? "Galactic Luxury Cruises";
            parsedArgs["native_image_url"] =
                nativeImageUrl ?? "https://native.test.com/image?id=123456";
            parsedArgs["native_image_height"] = nativeImageHeight ?? 627;
            parsedArgs["native_image_width"] = nativeImageWidth ?? 1200;
            parsedArgs["native_image_url"] = "https://native.test.com/image?id=123456";
            parsedArgs["native_image_height"] = 627;
            parsedArgs["native_image_width"] = 1200;
            parsedArgs["native_logo_url"] =
                nativeLogoUrl ?? "https://native.test.com/logo?id=123456";
            parsedArgs["native_logo_height"] = nativeLogoHeight ?? 100;
            parsedArgs["native_logo_width"] = nativeLogoWidth ?? 100;
            parsedArgs["native_click_link_url"] = nativeClickLinkUrl ?? "https://www.google.com";
            parsedArgs["native_click_tracking_url"] =
                nativeClickTrackingUrl ?? "https://native.test.com/click?id=123456";
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
            string parent = $"buyers/{accountId}";

            Image nativeImage = new Image();
            nativeImage.Url = (string) parsedArgs["native_image_url"];
            nativeImage.Height = (int?) parsedArgs["native_image_height"];
            nativeImage.Width = (int?) parsedArgs["native_image_width"];

            Image nativeLogo = new Image();
            nativeLogo.Url = (string) parsedArgs["native_logo_url"];
            nativeLogo.Height = (int?) parsedArgs["native_logo_height"];
            nativeLogo.Width = (int?) parsedArgs["native_logo_width"];

            NativeContent nativeContent = new NativeContent();
            nativeContent.Headline = (string) parsedArgs["native_headline"];
            nativeContent.Body = (string) parsedArgs["native_body"];
            nativeContent.CallToAction = (string) parsedArgs["native_call_to_action"];
            nativeContent.AdvertiserName = (string) parsedArgs["native_advertiser_name"];
            nativeContent.Image = nativeImage;
            nativeContent.Logo = nativeLogo;
            nativeContent.ClickLinkUrl = (string) parsedArgs["native_click_link_url"];
            nativeContent.ClickTrackingUrl = (string) parsedArgs["native_click_tracking_url"];

            Creative newCreative = new Creative();
            newCreative.AdvertiserName = (string) parsedArgs["advertiser_name"];
            newCreative.CreativeId = (string) parsedArgs["creative_id"];
            newCreative.DeclaredAttributes = (IList<string>) parsedArgs["declared_attributes"];
            newCreative.DeclaredClickThroughUrls = (IList<string>) parsedArgs[
                "declared_click_urls"];
            newCreative.DeclaredRestrictedCategories = (IList<string>) parsedArgs[
                "declared_restricted_categories"];
            newCreative.DeclaredVendorIds = (IList<int?>) parsedArgs["declared_vendor_ids"];
            newCreative.Native = nativeContent;

            BuyersResource.CreativesResource.CreateRequest request =
                rtbService.Buyers.Creatives.Create(newCreative, parent);
            Creative response = null;

            Console.WriteLine("Creating native creative for buyer: {0}", parent);

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
