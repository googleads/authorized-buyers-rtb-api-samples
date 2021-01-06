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
    /// Creates a pretargeting configuration for the given bidder account ID.
    /// </summary>
    public class CreatePretargetingConfigs : ExampleBase
    {
        private RealTimeBiddingService rtbService;

        /// <summary>
        /// Constructor.
        /// </summary>
        public CreatePretargetingConfigs()
        {
            rtbService = Utilities.GetRealTimeBiddingService();
        }

        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public override string Description
        {
            get => "This code example creates a pretargeting configuration for the given " +
                   "bidder account ID.";
        }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected override Dictionary<string, object> ParseArguments(List<string> exampleArgs) {
            string[] requiredOptions = new string[] {"account_id"};
            bool showHelp = false;

            string accountId = null;
            string displayName = null;
            IList<string> includedFormats = new List<string>();
            IList<long?> includedGeoIds = new List<long?>();
            IList<long?> excludedGeoIds = new List<long?>();
            IList<long?> includedUserListIds = new List<long?>();
            IList<long?> excludedUserListIds = new List<long?>();
            string interstitialTargeting = null;
            IList<string> allowedUserTargetingModes = new List<string>();
            IList<long?> excludedContentLabelIds = new List<long?>();
            IList<string> includedUserIdTypes = new List<string>();
            IList<string> includedLanguageCodes = new List<string>();
            IList<long?> includedMobileOsIds = new List<long?>();
            IList<long?> includedVerticalIds = new List<long?>();
            IList<long?> excludedVerticalIds = new List<long?>();
            IList<string> includedPlatforms = new List<string>();
            long? includedCreativeDimensionHeight = null;
            long? includedCreativeDimensionWidth = null;
            IList<string> includedEnvironments = new List<string>();
            string webTargetingMode = null;
            IList<string> webTargetingUrls = new List<string>();
            string mobileAppTargetingMode = null;
            IList<string> mobileAppTargetingAppIds = new List<string>();
            IList<long?> includedMobileAppTargetingCategoryIds = new List<long?>();
            IList<long?> excludedMobileAppTargetingCategoryIds = new List<long?>();
            string publisherTargetingMode = null;
            IList<string> publisherIds = new List<string>();
            int? minimumViewabilityDecile = null;

            OptionSet options = new OptionSet {
                "Creates a pretargeting configuration for the given bidder account ID.",
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "a|account_id=",
                    ("[Required] The resource ID of the bidders resource under which the " +
                     "pretargeting configuration is to be created."),
                    a => accountId = a
                },
                {
                    "display_name=",
                    "The display name to associate with the new configuration. Must be unique " +
                    "among all of a bidder's pretargeting configurations.",
                    display_name => displayName = display_name
                },
                {
                    "included_formats=",
                    ("Creative formats included by this configuration. An unset value will not " +
                     "filter any bid requests based on the format. Specify this argument for " +
                     "each value you intend to include.Valid values include: HTML, NATIVE, and " +
                     "VAST."),
                    included_formats => includedFormats.Add(included_formats)
                },
                {
                    "included_geo_ids=",
                    ("The geo IDs to include in targeting for this configuration. Specify this " +
                     "argument for each value you intend to include. Valid geo IDs can be found " +
                     "in: https://storage.googleapis.com/adx-rtb-dictionaries/geo-table.csv "),
                    (long included_geo_ids) => includedGeoIds.Add(included_geo_ids)
                },
                {
                    "excluded_geo_ids=",
                    ("The geo IDs to exclude in targeting for this configuration. Specify this " +
                     "argument for each value you intend to include. Valid geo IDs can be found " +
                     "in: https://storage.googleapis.com/adx-rtb-dictionaries/geo-table.csv "),
                    (long excluded_geo_ids) => excludedGeoIds.Add(excluded_geo_ids)
                },
                {
                    "included_user_list_ids=",
                    ("The user list IDs to include in targeting for this configuration. Specify " +
                    "this argument for each value you intend to include. Valid user list IDs " +
                    "would include any found under the buyers.userLists resource for a given " +
                    "bidder account, or any buyer accounts under it. "),
                    (long included_user_list_ids) => includedUserListIds.Add(
                        included_user_list_ids)
                },
                {
                    "excluded_user_list_ids=",
                    ("The user list IDs to exclude in targeting for this configuration. Specify " +
                    "this argument for each value you intend to include. Valid user list IDs " +
                    "would include any found under the buyers.userLists resource for a given " +
                    "bidder account, or any buyer accounts under it. "),
                    (long excluded_user_list_ids) => excludedUserListIds.Add(
                        excluded_user_list_ids)
                },
                {
                    "interstitial_targeting=",
                    ("The interstitial targeting specified for this configuration. By default, " +
                     "this will be set to ONLY_NON_INTERSTITIAL_REQUESTS. Valid values include: " +
                     "ONLY_INTERSTITIAL_REQUESTS and ONLY_NON_INTERSTITIAL_REQUESTS."),
                    interstitial_targeting => interstitialTargeting = interstitial_targeting
                },
                {
                    "allowed_user_targeting_modes=",
                    ("The targeting modes to include in targeting for this configuration. " +
                     "Specify this argument for each value you intend to include. Valid " +
                     "targeting modes include: REMARKETING_ADS and INTEREST_BASED_TARGETING."),
                    allowed_user_targeting_modes => allowedUserTargetingModes.Add(
                        allowed_user_targeting_modes)
                },
                {
                    "excluded_content_label_ids=",
                    ("The sensitive content category IDs excluded in targeting for this " +
                     "configuration. Specify this argument for each value you intend to  " +
                     "include. Valid sensitive content category IDs can be found in: " +
                     "https://storage.googleapis.com/adx-rtb-dictionaries/content-labels.txt"),
                    (long excluded_content_label_ids) => excludedContentLabelIds.Add(
                        excluded_content_label_ids)
                },
                {
                    "included_user_id_types=",
                    ("The user identifier types included in targeting for this configuration. " +
                     "Specify this argument for each value you intend to include. Valid values " +
                     "include: HOSTED_MATCH_DATA, GOOGLE_COOKIE, and DEVICE_ID."),
                    included_user_id_types => includedUserIdTypes.Add(included_user_id_types)
                },
                {
                    "included_language_codes=",
                    ("The languages represented by languages codes that are included in " +
                     "targeting for this configuration. Specify this argument for each value " +
                     "you intend to include. Valid language codes can be found in: " +
                     "https://developers.google.com/adwords/api/docs/appendix/languagecodes."),
                    included_language_codes => includedLanguageCodes.Add(included_language_codes)
                },
                {
                    "included_mobile_os_ids=",
                    ("The mobile OS IDs to include in targeting for this configuration. Specify " +
                     "this argument for each value you intend to include. Valid mobile OS IDs " +
                     "can be found in: " +
                     "https://storage.googleapis.com/adx-rtb-dictionaries/mobile-os.csv"),
                    (long included_mobile_os_ids) => includedMobileOsIds.Add(
                        included_mobile_os_ids)
                },
                {
                    "included_vertical_ids=",
                    ("The vertical IDs to include in targeting for this configuration. Specify " +
                     "this argument for each value you intend to include. Valid vertical IDs " +
                     "can be found in: https://developers.google.com/authorized-buyers/rtb/" +
                     "downloads/publisher-verticals"),
                    (long included_vertical_ids) => includedVerticalIds.Add(included_vertical_ids)
                },
                {
                    "excluded_vertical_ids=",
                    ("The vertical IDs to exclude in targeting for this configuration. Specify " +
                     "this argument for each value you intend to include. Valid vertical IDs " +
                     "can be found in: https://developers.google.com/authorized-buyers/rtb/" +
                     "downloads/publisher-verticals"),
                    (long excluded_vertical_ids) => excludedVerticalIds.Add(excluded_vertical_ids)
                },
                {
                    "included_platforms=",
                    ("The platforms to include in targeting for this configuration. Specify " +
                     "this argument for each value you intend to include. Valid values include: " +
                     "PERSONAL_COMPUTER, PHONE, TABLET, and CONNECTED_TV."),
                    included_platforms => includedPlatforms.Add(included_platforms)
                },
                {
                    "included_creative_dimension_height=",
                    ("A creative dimension's height to be included in targeting for this " +
                     "configuration. By default, this example will set the targeted height to " +
                     "300. Note that while only a single set of dimensions are specified in " +
                     "this sample, pretargeting configurations can target multiple creative " +
                     "dimensions."),
                    (long included_creative_dimension_height) => includedCreativeDimensionHeight = (
                        included_creative_dimension_height)
                },
                {
                    "included_creative_dimension_width=",
                    ("A creative dimension's height to be included in targeting for this " +
                     "configuration. By default, this example will set the targeted height to " +
                     "300. Note that while only a single set of dimensions are specified in " +
                     "this sample, pretargeting configurations can target multiple creative " +
                     "dimensions."),
                    (long included_creative_dimension_width) => includedCreativeDimensionWidth = (
                        included_creative_dimension_width)
                },
                {
                    "included_environments=",
                    ("The environments to include in targeting for this configuration. Specify " +
                     "this argument for each value you intend to include. Valid values include: " +
                     "APP, and WEB."),
                    included_environments => includedEnvironments.Add(included_environments)
                },
                {
                    "web_targeting_mode=",
                    ("The targeting mode for this configuration's web targeting. Valid values " +
                     "include: INCLUSIVE, and EXCLUSIVE."),
                    web_targeting_mode => webTargetingMode = web_targeting_mode
                },
                {
                    "web_targeting_urls=",
                    ("The URLs specified for this configuration's web targeting, which allows " +
                     "one to target a subset of site inventory. Specify this argument for each " +
                     "value you intend to include. Values specified must be valid URLs."),
                    web_targeting_urls => webTargetingUrls.Add(web_targeting_urls)
                },
                {
                    "mobile_app_targeting_mode=",
                    ("The targeting mode for this configuration's mobile app targeting. Valid " +
                     "values include: INCLUSIVE, and EXCLUSIVE."),
                    mobile_app_targeting_mode => mobileAppTargetingMode = mobile_app_targeting_mode
                },
                {
                    "mobile_app_targeting_app_ids=",
                    ("The mobile app IDs specified for this configuration's mobile app " +
                     "targeting, which allows one to target a subset of mobile app inventory. " +
                     "Specify this argument for each value you intend to include. Values " +
                     "specified must be valid URLs."),
                    mobile_app_targeting_app_ids => mobileAppTargetingAppIds.Add(
                        mobile_app_targeting_app_ids)
                },
                {
                    "included_mobile_app_targeting_category_ids=",
                    ("The mobile app category IDs to include in targeting for this " +
                     "configuration. Specify this argument for each value you intend to " +
                     "include. Valid category IDs can be found in: " +
                     "https://developers.google.com/adwords/api/docs/appendix/" +
                     "mobileappcategories.csv"),
                    (long included_mobile_app_targeting_category_ids) =>
                        includedMobileAppTargetingCategoryIds.Add(
                            included_mobile_app_targeting_category_ids)
                },
                {
                    "excluded_mobile_app_targeting_category_ids=",
                    ("The mobile app category IDs to exclude in targeting for this " +
                     "configuration. Specify this argument for each value you intend to " +
                     "include. Valid category IDs can be found in: " +
                     "https://developers.google.com/adwords/api/docs/appendix/" +
                     "mobileappcategories.csv"),
                    (long excluded_mobile_app_targeting_category_ids) =>
                        excludedMobileAppTargetingCategoryIds.Add(
                            excluded_mobile_app_targeting_category_ids)
                },
                {
                    "publisher_targeting_mode=",
                    ("The targeting mode for this configuration's publisher targeting. Valid " +
                     "values include: INCLUSIVE, and EXCLUSIVE."),
                    publisher_targeting_mode => publisherTargetingMode = publisher_targeting_mode
                },
                {
                    "publisher_ids=",
                    ("The publisher IDs specified for this configuration's publisher targeting, " +
                     "which allows one to target a subset of publisher inventory. Specify this " +
                     "argument for each value you intend to include. Valid publisher IDs can be " +
                     "found in Real-time Bidding bid requests, or alternatively in " +
                     "ads.txt / app-ads.txt. For more information, see: " +
                     "https://iabtechlab.com/ads-txt/"),
                    publisher_ids => publisherIds.Add(publisher_ids)
                },
                {
                    "minimum_viewability_decile=",
                    ("The targeted minimum viewability decile, ranging from 0 - 10. A value of " +
                     "'5' means that the configuration will only match adslots for which we " +
                     "predict at least 50% viewability. Values > 10 will be rounded down to 10. " +
                     "An unset value, or a value of '0', indicates that bid requests should be " +
                     "sent regardless of viewability."),
                    (int minimum_viewability_decile) => minimumViewabilityDecile = (
                        minimum_viewability_decile)
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
            parsedArgs["display_name"] = displayName ?? String.Format(
                "TEST_PRETARGETING_CONFIG_{0}",
                System.Guid.NewGuid());
            parsedArgs["interstitial_targeting"] = interstitialTargeting ?? (
                "ONLY_NON_INTERSTITIAL_REQUESTS");
            parsedArgs["included_creative_dimension_height"] = includedCreativeDimensionHeight ?? (
                300L);
            parsedArgs["included_creative_dimension_width"] = includedCreativeDimensionWidth ?? (
                250L);
            parsedArgs["minimum_viewability_decile"] = minimumViewabilityDecile ?? 5;

            parsedArgs["included_formats"] = includedFormats;
            parsedArgs["included_geo_ids"] = includedGeoIds;
            parsedArgs["excluded_geo_ids"] = excludedGeoIds;
            parsedArgs["included_user_list_ids"] = includedUserListIds;
            parsedArgs["excluded_user_list_ids"] = excludedUserListIds;
            parsedArgs["allowed_user_targeting_modes"] = allowedUserTargetingModes;
            parsedArgs["excluded_content_label_ids"] = excludedContentLabelIds;
            parsedArgs["included_user_id_types"] = includedUserIdTypes;
            parsedArgs["included_language_codes"] = includedLanguageCodes;
            parsedArgs["included_mobile_os_ids"] = includedMobileOsIds;
            parsedArgs["included_vertical_ids"] = includedVerticalIds;
            parsedArgs["excluded_vertical_ids"] = excludedVerticalIds;
            parsedArgs["included_platforms"] = includedPlatforms;
            parsedArgs["included_environments"] = includedEnvironments;
            parsedArgs["web_targeting_mode"] = webTargetingMode;
            parsedArgs["web_targeting_urls"] = webTargetingUrls;
            parsedArgs["mobile_app_targeting_mode"] = mobileAppTargetingMode;
            parsedArgs["mobile_app_targeting_app_ids"] = mobileAppTargetingAppIds;
            parsedArgs["included_mobile_app_targeting_category_ids"] = (
                includedMobileAppTargetingCategoryIds);
            parsedArgs["excluded_mobile_app_targeting_category_ids"] = (
                excludedMobileAppTargetingCategoryIds);
            parsedArgs["publisher_targeting_mode"] = publisherTargetingMode;
            parsedArgs["publisher_ids"] = publisherIds;
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
            string parent = $"bidders/{accountId}";

            NumericTargetingDimension geoTargeting = new NumericTargetingDimension();
            geoTargeting.IncludedIds = (IList<long?>) parsedArgs["included_geo_ids"];
            geoTargeting.ExcludedIds = (IList<long?>) parsedArgs["excluded_geo_ids"];

            NumericTargetingDimension userListTargeting = new NumericTargetingDimension();
            userListTargeting.IncludedIds = (IList<long?>) parsedArgs["included_user_list_ids"];
            userListTargeting.ExcludedIds = (IList<long?>) parsedArgs["excluded_user_list_ids"];

            NumericTargetingDimension verticalTargeting = new NumericTargetingDimension();
            verticalTargeting.IncludedIds = (IList<long?>) parsedArgs["included_vertical_ids"];
            verticalTargeting.ExcludedIds = (IList<long?>) parsedArgs["excluded_vertical_ids"];

            CreativeDimensions dimensions = new CreativeDimensions();
            dimensions.Height = (long?) parsedArgs["included_creative_dimension_height"];
            dimensions.Width = (long?) parsedArgs["included_creative_dimension_width"];

            StringTargetingDimension webTargeting = new StringTargetingDimension();
            webTargeting.TargetingMode = (string) parsedArgs["web_targeting_mode"];
            webTargeting.Values = (IList<string>) parsedArgs["web_targeting_urls"];

            StringTargetingDimension mobileAppTargeting = new StringTargetingDimension();
            mobileAppTargeting.TargetingMode = (string) parsedArgs["mobile_app_targeting_mode"];
            mobileAppTargeting.Values = (IList<string>) parsedArgs["mobile_app_targeting_app_ids"];

            NumericTargetingDimension mobileAppCategoryTargeting = new NumericTargetingDimension();
            mobileAppCategoryTargeting.IncludedIds = (IList<long?>) parsedArgs[
                "included_mobile_app_targeting_category_ids"];
            mobileAppCategoryTargeting.ExcludedIds = (IList<long?>) parsedArgs[
                "excluded_mobile_app_targeting_category_ids"];

            AppTargeting appTargeting = new AppTargeting();
            appTargeting.MobileAppTargeting = mobileAppTargeting;
            appTargeting.MobileAppCategoryTargeting = mobileAppCategoryTargeting;

            StringTargetingDimension publisherTargeting = new StringTargetingDimension();
            publisherTargeting.TargetingMode = (string) parsedArgs["publisher_targeting_mode"];
            publisherTargeting.Values = (IList<string>) parsedArgs["publisher_ids"];

            PretargetingConfig newConfig = new PretargetingConfig();
            newConfig.DisplayName = (string) parsedArgs["display_name"];
            newConfig.IncludedFormats = (IList<string>) parsedArgs["included_formats"];
            newConfig.GeoTargeting = geoTargeting;
            newConfig.UserListTargeting = userListTargeting;
            newConfig.InterstitialTargeting = (string) parsedArgs["interstitial_targeting"];
            newConfig.AllowedUserTargetingModes = (IList<string>) parsedArgs[
                "allowed_user_targeting_modes"];
            newConfig.ExcludedContentLabelIds = (IList<long?>) parsedArgs[
                "excluded_content_label_ids"];
            newConfig.IncludedUserIdTypes = (IList<string>) parsedArgs["included_user_id_types"];
            newConfig.IncludedLanguages = (IList<string>) parsedArgs["included_language_codes"];
            newConfig.IncludedMobileOperatingSystemIds = (IList<long?>) parsedArgs[
                "included_mobile_os_ids"];
            newConfig.VerticalTargeting = verticalTargeting;
            newConfig.IncludedPlatforms = (IList<string>) parsedArgs["included_platforms"];
            newConfig.IncludedCreativeDimensions = new List<CreativeDimensions> {dimensions};
            newConfig.IncludedEnvironments = (IList<string>) parsedArgs["included_environments"];
            newConfig.WebTargeting = webTargeting;
            newConfig.AppTargeting = appTargeting;
            newConfig.PublisherTargeting = publisherTargeting;
            newConfig.MinimumViewabilityDecile = (int?) parsedArgs["minimum_viewability_decile"];

            BiddersResource.PretargetingConfigsResource.CreateRequest request =
                rtbService.Bidders.PretargetingConfigs.Create(newConfig, parent);
            PretargetingConfig response = null;

            Console.WriteLine("Creating pretargeting configuration for bidder: {0}", parent);

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