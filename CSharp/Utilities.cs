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

using Google.Apis.Pubsub.v1;
using Google.Apis.RealTimeBidding.v1;
using Google.Apis.RealTimeBidding.v1.Data;
using Google.Apis.Auth.OAuth2;
using Google.Apis.Http;
using Google.Apis.Json;
using Mono.Options;

using System;
using System.Collections.Generic;

namespace Google.Apis.RealTimeBidding.Examples
{
    /// <summary>
    /// Utilities used by the Authorized Buyers Real-time Bidding API samples.
    /// </summary>
    public class Utilities
    {
        /// <summary>
        /// Full path to a JSON key file to be used in the Service Account OAuth 2.0 flow. See
        /// README.md for more details.
        /// </summary>
        private static String ServiceKeyFilePath = "PATH TO JSON KEYFILE";

        /// <summary>
        /// The default maximum page size to be used for API calls featuring pagination.
        /// </summary>
        public static int MAX_PAGE_SIZE = 50;

        /// <summary>
        /// Print a human-readable representation of a single bidder.
        /// </summary>
        public static void PrintBidder(Bidder bidder)
        {
            Console.WriteLine("* Bidder name: {0}", bidder.Name);

            string cookieMatchingUrl = bidder.CookieMatchingUrl;
            if(cookieMatchingUrl != null)
            {
                Console.WriteLine("\t- Cookie Matching URL: {0}", cookieMatchingUrl);
            }

            string cookieMatchingNid = bidder.CookieMatchingNetworkId;
            if(cookieMatchingNid != null)
            {
                Console.WriteLine("\t- Cookie Matching Network ID: {0}", cookieMatchingNid);
            }

            bool? bypassNonGuaranteedDealsPretargeting = bidder.BypassNonguaranteedDealsPretargeting;
            if(bypassNonGuaranteedDealsPretargeting != null)
            {
                Console.WriteLine("\t- Bypass Non-Guaranteed Deals Pretargeting: {0}",
                                  bypassNonGuaranteedDealsPretargeting);
            }

            string dealsBillingId = bidder.DealsBillingId;
            if(dealsBillingId != null)
            {
                Console.WriteLine("\t- Deals Billing ID: {0}", dealsBillingId);
            }
        }

        /// <summary>
        /// Print a human-readable representation of a single buyer.
        /// </summary>
        public static void PrintBuyer(Buyer buyer)
        {
            Console.WriteLine("* Buyer name: {0}", buyer.Name);

            string displayName = buyer.DisplayName;
            if(displayName != null)
            {
                Console.WriteLine("\t- Display Name: {0}", displayName);
            }

            string bidder = buyer.Bidder;
            if(bidder != null)
            {
                Console.WriteLine("\t- Bidder: {0}", bidder);
            }

            long? activeCreativeCount = buyer.ActiveCreativeCount;
            if(activeCreativeCount != null)
            {
                Console.WriteLine("\t- Active Creative Count: {0}", activeCreativeCount);
            }

            long? maximumActiveCreativeCount = buyer.MaximumActiveCreativeCount;
            if(maximumActiveCreativeCount != null)
            {
                Console.WriteLine("\t- Maximum Active Creative Count: {0}", maximumActiveCreativeCount);
            }

            IList<string> billingIds = buyer.BillingIds;
            if(billingIds != null)
            {
                Console.WriteLine("\t- billingIds:\n\t\t" + String.Join("\n\t\t", billingIds));
            }
        }

        /// <summary>
        /// Print a human-readable representation of a single creative.
        /// </summary>
        public static void PrintCreative(Creative creative)
        {
            Console.WriteLine("* Creative ID: {0}", creative.CreativeId);

            int? version = creative.Version;
            if(version != null)
            {
                Console.WriteLine("\t- Version: {0}", version);
            }

            string advertiserName = creative.AdvertiserName;
            if(advertiserName != null)
            {
                Console.WriteLine("\t- Advertiser name: {0}", advertiserName);
            }

            string creativeFormat = creative.CreativeFormat;
            if(creativeFormat != null)
            {
                Console.WriteLine("\t- Creative format: {0}", creativeFormat);
            }

            CreativeServingDecision servingDecision = creative.CreativeServingDecision;
            if(servingDecision != null)
            {
                Console.WriteLine("\t- Creative serving decision");
                Console.WriteLine("\t\tDeals policy compliance: {0}",
                                  servingDecision.DealsPolicyCompliance.Status);
                Console.WriteLine("\t\tNetwork policy compliance: {0}",
                                  servingDecision.NetworkPolicyCompliance.Status);
                Console.WriteLine("\t\tPlatform policy compliance: {0}",
                                  servingDecision.PlatformPolicyCompliance.Status);
                Console.WriteLine("\t\tChina policy compliance: {0}",
                                  servingDecision.ChinaPolicyCompliance.Status);
                Console.WriteLine("\t\tRussia policy compliance: {0}",
                                  servingDecision.RussiaPolicyCompliance.Status);
            }

            IList<string> declaredClickThroughUrls = creative.DeclaredClickThroughUrls;
            if(declaredClickThroughUrls != null)
            {
                Console.WriteLine("\t- Declared click-through URLs:\n\t\t" +
                    String.Join("\n\t\t", declaredClickThroughUrls));
            }

            IList<string> declaredAttributes = creative.DeclaredAttributes;
            if(declaredAttributes != null)
            {
                Console.WriteLine("\t- Declared attributes:\n\t\t" +
                    String.Join("\n\t\t", declaredAttributes));
            }

            IList<int?> declaredVendorIds = creative.DeclaredVendorIds;
            if(declaredVendorIds != null)
            {
                Console.WriteLine("\t- Declared vendor IDs:\n\t\t" +
                    String.Join("\n\t\t", declaredVendorIds));
            }

            IList<string> declaredRestrictedCategories = creative.DeclaredRestrictedCategories;
            if(declaredRestrictedCategories != null)
            {
                Console.WriteLine("\t- Declared restricted categories:\n\t\t" +
                    String.Join("\n\t\t", declaredRestrictedCategories));
            }

            HtmlContent html = creative.Html;
            if(html != null)
            {
                Console.WriteLine("\t- HTML creative contents:");
                Console.WriteLine("\t\tSnippet: {0}", html.Snippet);
                Console.WriteLine("\t\tHeight: {0}", html.Height);
                Console.WriteLine("\t\tWidth: {0}", html.Width);
            }

            NativeContent native = creative.Native;
            if(native != null)
            {
                Console.WriteLine("\t- Native creative contents:");
                Console.WriteLine("\t\tHeadline: {0}", native.Headline);
                Console.WriteLine("\t\tBody: {0}", native.Body);
                Console.WriteLine("\t\tCall to action: {0}", native.CallToAction);
                Console.WriteLine("\t\tAdvertiser name: {0}", native.AdvertiserName);
                Console.WriteLine("\t\tStar rating: {0}", native.StarRating);
                Console.WriteLine("\t\tClick link URL: {0}", native.ClickLinkUrl);
                Console.WriteLine("\t\tClick tracking URL: {0}", native.ClickTrackingUrl);
                Console.WriteLine("\t\tPrice display text: {0}", native.PriceDisplayText);

                Image image = native.Image;
                if(image != null)
                {
                    Console.WriteLine("\t\tImage contents:");
                    Console.WriteLine("\t\t\tURL: {0}", image.Url);
                    Console.WriteLine("\t\t\tHeight: {0}", image.Height);
                    Console.WriteLine("\t\t\tWidth: {0}", image.Width);
                }

                Image logo = native.Logo;
                if(logo != null)
                {
                    Console.WriteLine("\t\tLogo contents:");
                    Console.WriteLine("\t\t\tURL: {0}", logo.Url);
                    Console.WriteLine("\t\t\tHeight: {0}", logo.Height);
                    Console.WriteLine("\t\t\tWidth: {0}", logo.Width);
                }

                Image appIcon = native.AppIcon;
                if(appIcon != null)
                {
                    Console.WriteLine("\t\tAppIcon contents:");
                    Console.WriteLine("\t\t\tURL: {0}", appIcon.Url);
                    Console.WriteLine("\t\t\tHeight: {0}", appIcon.Height);
                    Console.WriteLine("\t\t\tWidth: {0}", appIcon.Width);
                }
            }

            VideoContent video = creative.Video;
            if(video != null)
            {
                Console.WriteLine("\tVideo creative contents:");

                string videoUrl = video.VideoUrl;
                if(videoUrl != null)
                {
                    Console.WriteLine("\t\tVideo URL: {0}", videoUrl);
                }

                string videoVastXml = video.VideoVastXml;
                if(videoVastXml != null)
                {
                    Console.WriteLine("\t\tVideo VAST XML:\n{0}", videoVastXml);
                }
            }
        }

        /// <summary>
        /// Print a human-readable representation of a single endpoint.
        /// </summary>
        public static void PrintEndpoint(Endpoint endpoint)
        {
            Console.WriteLine("* Endpoint name: {0}", endpoint.Name);

            string url = endpoint.Url;
            if(url != null)
            {
                Console.WriteLine("\t- URL: {0}", url);
            }

            long? maximumQps = endpoint.MaximumQps;
            if(maximumQps != null)
            {
                Console.WriteLine("\t- Maximum QPS: {0}", maximumQps);
            }

            string tradingLocation = endpoint.TradingLocation;
            if(tradingLocation != null)
            {
                Console.WriteLine("\t- Trading Location: {0}", tradingLocation);
            }

            string bidProtocol = endpoint.BidProtocol;
            if(bidProtocol != null)
            {
                Console.WriteLine("\t- Bid Protocol: {0}", bidProtocol);
            }
        }

        /// <summary>
        /// Print a human-readable representation of a single pretargeting configuration.
        /// </summary>
        public static void PrintPretargetingConfiguration(PretargetingConfig configuration)
        {
            Console.WriteLine("* Pretargeting configuration name: {0}", configuration.Name);
            Console.WriteLine("\t- Display name: {0}", configuration.DisplayName);
            Console.WriteLine("\t- Billing ID: {0}", configuration.BillingId);
            Console.WriteLine("\t- State: {0}", configuration.State);

            long? maximumQps = configuration.MaximumQps;
            if(maximumQps != null)
            {
                Console.WriteLine("\t- Maximum QPS: {0}", maximumQps);
            }

            string interstitialTargeting = configuration.InterstitialTargeting;
            if(interstitialTargeting != null)
            {
                Console.WriteLine("\t- Interstitial targeting: {0}", interstitialTargeting);
            }

            int? minimumViewabilityDecile = configuration.MinimumViewabilityDecile;
            if(minimumViewabilityDecile != null)
            {
                Console.WriteLine("\t- Minimum viewability decile: {0}", minimumViewabilityDecile);
            }

            IList<string> includedFormats = configuration.IncludedFormats;
            if(includedFormats != null)
            {
                Console.WriteLine("\t- Included formats:\n\t\t" +
                    String.Join("\n\t\t", includedFormats));
            }

            NumericTargetingDimension geoTargeting = configuration.GeoTargeting;
            if(geoTargeting != null)
            {
                IList<long?> includedIds = geoTargeting.IncludedIds;
                if(includedIds != null)
                {
                    Console.WriteLine("\t- Included geo IDs:\n\t\t" +
                        String.Join("\n\t\t", includedIds));
                }

                IList<long?> excludedIds = geoTargeting.ExcludedIds;
                if(excludedIds != null)
                {
                    Console.WriteLine("\t- Excluded geo IDs:\n\t\t" +
                        String.Join("\n\t\t", excludedIds));
                }
            }

            IList<long?> invalidGeoIDs = configuration.InvalidGeoIds;
            if(invalidGeoIDs != null)
            {
                Console.WriteLine("\t- Invalid Geo IDs:\n\t\t" +
                    String.Join("\n\t\t", invalidGeoIDs));
            }

            NumericTargetingDimension userListTargeting = configuration.UserListTargeting;
            if(userListTargeting != null)
            {
                IList<long?> includedIds = userListTargeting.IncludedIds;
                if(includedIds != null)
                {
                    Console.WriteLine("\t- Included user list IDs:\n\t\t" +
                        String.Join("\n\t\t", includedIds));
                }

                IList<long?> excludedIds = userListTargeting.ExcludedIds;
                if(excludedIds != null)
                {
                    Console.WriteLine("\t- Excluded user list IDs:\n\t\t" +
                        String.Join("\n\t\t", excludedIds));
                }
            }

            IList<string> allowedUserTargetingModes = configuration.AllowedUserTargetingModes;
            if(allowedUserTargetingModes != null)
            {
                Console.WriteLine("\t- Allowed user targeting modes:\n\t\t" +
                    String.Join("\n\t\t", allowedUserTargetingModes));
            }

            IList<long?> excludedContentLabelIds = configuration.ExcludedContentLabelIds;
            if(excludedContentLabelIds != null)
            {
                Console.WriteLine("\t- Excluded content label IDs:\n\t\t" +
                    String.Join("\n\t\t", excludedContentLabelIds));
            }

            IList<string> includedUserIdTypes = configuration.IncludedUserIdTypes;
            if(includedUserIdTypes != null)
            {
                Console.WriteLine("\t- Included user ID types:\n\t\t" +
                    String.Join("\n\t\t", includedUserIdTypes));
            }

            IList<string> includedLanguages = configuration.IncludedLanguages;
            if(includedLanguages != null)
            {
                Console.WriteLine("\t- Included laguages:\n\t\t" +
                    String.Join("\n\t\t", includedLanguages));
            }

            IList<long?> includedMobileOsIds = configuration.IncludedMobileOperatingSystemIds;
            if(includedMobileOsIds != null)
            {
                Console.WriteLine("\t- Included mobile operating system IDs:\n\t\t" +
                    String.Join("\n\t\t", includedMobileOsIds));
            }

            NumericTargetingDimension verticalTargeting = configuration.VerticalTargeting;
            if(verticalTargeting != null)
            {
                IList<long?> includedIds = verticalTargeting.IncludedIds;
                if(includedIds != null)
                {
                    Console.WriteLine("\t- Included vertical IDs:\n\t\t" +
                        String.Join("\n\t\t", includedIds));
                }

                IList<long?> excludedIds = verticalTargeting.ExcludedIds;
                if(excludedIds != null)
                {
                    Console.WriteLine("\t- Excluded vertical IDs:\n\t\t" +
                        String.Join("\n\t\t", excludedIds));
                }
            }

            IList<string> includedPlatforms = configuration.IncludedPlatforms;
            if(includedPlatforms != null)
            {
                Console.WriteLine("\t- Included platforms:\n\t\t" +
                    String.Join("\n\t\t", includedPlatforms));
            }

            IList<CreativeDimensions> creativeDimensions =
                configuration.IncludedCreativeDimensions;
            if(creativeDimensions != null && creativeDimensions.Count > 0)
            {
                Console.WriteLine("\t- Included creative dimensions:");
                foreach(CreativeDimensions creativeDimension in creativeDimensions)
                {
                    Console.WriteLine("\t\tHeight: {0}; Width: {1}",
                        creativeDimension.Height, creativeDimension.Width);
                }
            }

            IList<string> includedEnvironments = configuration.IncludedEnvironments;
            if(includedEnvironments != null)
            {
                Console.WriteLine("\t- Included environments:\n\t\t" +
                    String.Join("\n\t\t", includedEnvironments));
            }

            StringTargetingDimension webTargeting = configuration.WebTargeting;
            if(webTargeting != null)
            {
                Console.WriteLine("\t- Web targeting:");
                Console.WriteLine("\t\t- Targeting mode: {0}", webTargeting.TargetingMode);
                Console.WriteLine("\t\t- Site URLs:\n\t\t\t" +
                    String.Join("\n\t\t\t", webTargeting.Values));
            }

            AppTargeting appTargeting = configuration.AppTargeting;
            if(appTargeting != null)
            {
                Console.WriteLine("\t- App targeting:");

                StringTargetingDimension mobileAppTargeting = appTargeting.MobileAppTargeting;
                if(mobileAppTargeting != null)
                {
                    Console.WriteLine("\t\t- Mobile app targeting:");
                    Console.WriteLine("\t\t\t- Targeting mode: {0}",
                        mobileAppTargeting.TargetingMode);
                    Console.WriteLine("\t\t\t- Mobile app IDs:\n\t\t\t\t" +
                        String.Join("\n\t\t\t\t", mobileAppTargeting.Values));
                }

                NumericTargetingDimension mobileAppCategoryTargeting =
                    appTargeting.MobileAppCategoryTargeting;
                if(mobileAppCategoryTargeting != null)
                {
                    Console.WriteLine("\t\t- Mobile app category targeting:");
                    IList<long?> includedIds = mobileAppCategoryTargeting.IncludedIds;
                    if(includedIds != null)
                    {
                        Console.WriteLine("\t\t\t- Included mobile app category targeting IDs:" +
                            "\n\t\t\t\t" + String.Join("\n\t\t\t\t", includedIds));
                    }

                    IList<long?> excludedIds = mobileAppCategoryTargeting.ExcludedIds;
                    if(excludedIds != null)
                    {
                        Console.WriteLine("\t\t\t- Excluded mobile app category targeting IDs:" +
                            "\n\t\t\t\t" + String.Join("\n\t\t\t\t", excludedIds));
                    }
                }
            }

            StringTargetingDimension publisherTargeting = configuration.PublisherTargeting;
            if(publisherTargeting != null)
            {
                Console.WriteLine("\t- Publisher targeting:");
                Console.WriteLine("\t\t- Targeting mode: {0}", publisherTargeting.TargetingMode);
                Console.WriteLine("\t\t- Publisher IDs:\n\t\t\t" +
                    String.Join("\n\t\t\t", publisherTargeting.Values));
            }
        }

        /// <summary>
        /// Print a human-readable representation of a single publisher connection.
        /// </summary>
        public static void PrintPublisherConnection(PublisherConnection connection)
        {
            Console.WriteLine("* Publisher connection name: {0}", connection.Name);

            string publisherPlatform = connection.PublisherPlatform;
            if(publisherPlatform != null)
            {
                Console.WriteLine("\t- Publisher platform: {0}", publisherPlatform);
            }

            string displayName = connection.DisplayName;
            if(displayName != null)
            {
                Console.WriteLine("\t- Display name: {0}", displayName);
            }

            string biddingState = connection.BiddingState;
            if(biddingState != null)
            {
                Console.WriteLine("\t- Bidding state: {0}", biddingState);
            }

            object createTime = connection.CreateTime;
            if(createTime != null)
            {
                Console.WriteLine("\t- Create time: {0}", createTime.ToString());
            }
        }

        /// <summary>
        /// Create a new service for the Google Cloud Pub/Sub API.
        /// </summary>
        /// <returns>A new API Service</returns>
        public static PubsubService GetPubSubService()
        {
            return new PubsubService(
                new Google.Apis.Services.BaseClientService.Initializer
                {
                    HttpClientInitializer = ServiceAccount(),
                    ApplicationName = "Real-time Bidding API C# Sample",
                }
            );
        }

        /// <summary>
        /// Create a new service for the Authorized Buyers Real-time Bidding API.
        /// </summary>
        /// <returns>A new API Service</returns>
        public static RealTimeBiddingService GetRealTimeBiddingService()
        {
            return new RealTimeBiddingService(
                new Google.Apis.Services.BaseClientService.Initializer
                {
                    HttpClientInitializer = ServiceAccount(),
                    ApplicationName = "Real-time Bidding API C# Sample",
                }
            );
        }

        /// <summary>
        /// Uses a JSON KeyFile to authenticate a service account and return credentials for
        /// accessing the API.
        /// </summary>
        /// <returns>Authentication object for API Requests</returns>
        private static IConfigurableHttpClientInitializer ServiceAccount()
        {
            var credentialParameters = NewtonsoftJsonSerializer.Instance
                .Deserialize<JsonCredentialParameters>(System.IO.File.ReadAllText(
                    ServiceKeyFilePath));

            return new ServiceAccountCredential(
                new ServiceAccountCredential.Initializer(credentialParameters.ClientEmail)
                {
                    Scopes = new[] {
                        PubsubService.Scope.Pubsub,
                        RealTimeBiddingService.Scope.RealtimeBidding
                    }
                }.FromPrivateKey(credentialParameters.PrivateKey));
        }

        /// <summary>
        /// Ensures that required options have been set, and that unknown options have not been
        /// specified. Otherwise exits the example with an error message.
        /// </summary>
        /// <returns>A new API Service</returns>
        public static void ValidateOptions(
            OptionSet options, Dictionary<string, object> parsedArgs, string[] requiredKeys,
            List<string> extras)
        {
            if(extras.Count > 0)
            {
                throw new ApplicationException("Unknown arguments specified:\n\t" +
                    String.Join("\t", extras));
            }

            foreach(string requiredKey in requiredKeys)
            {
                if(parsedArgs[requiredKey] == null)
                {
                    options.WriteOptionDescriptions(Console.Error);
                    throw new ApplicationException(String.Format(
                        @"Required argument ""{0}"" not specified.", requiredKey));
                }
            }
        }
    }
}
