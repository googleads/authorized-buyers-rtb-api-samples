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
            if(declaredClickThroughUrls != null && declaredClickThroughUrls.Count > 0)
            {
                Console.WriteLine("\t- Declared click-through URLs:");
                foreach(string declaredClickUrl in declaredClickThroughUrls)
                {
                    Console.WriteLine("\t\t{0}", declaredClickUrl);
                }
            }

            IList<string> declaredAttributes = creative.DeclaredAttributes;
            if(declaredAttributes != null && declaredAttributes.Count > 0)
            {
                Console.WriteLine("\t- Declared attributes:");
                foreach(string declaredAttribute in declaredAttributes)
                {
                    Console.WriteLine("\t\t{0}", declaredAttribute);
                }
            }

            IList<int?> declaredVendorIds = creative.DeclaredVendorIds;
            if(declaredVendorIds != null && declaredVendorIds.Count > 0)
            {
                Console.WriteLine("\t- Declared vendor IDs:");
                foreach(int? declaredVendorId in declaredVendorIds)
                {
                    Console.WriteLine("\t\t{0}", declaredVendorId.ToString());
                }
            }

            IList<string> declaredRestrictedCategories = creative.DeclaredRestrictedCategories;
            if(declaredRestrictedCategories != null && declaredRestrictedCategories.Count > 0)
            {
                Console.WriteLine("\t- Declared restricted categories:");
                foreach(string declaredRestrictedCategory in declaredRestrictedCategories)
                {
                    Console.WriteLine("\t\t{0}", declaredRestrictedCategory);
                }
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
    }
}