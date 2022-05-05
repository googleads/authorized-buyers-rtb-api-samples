/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.api.services.samples.authorizedbuyers.realtimebidding.v1.bidders.pretargetingconfigs;

import com.google.api.services.realtimebidding.v1.RealTimeBidding;
import com.google.api.services.realtimebidding.v1.model.AppTargeting;
import com.google.api.services.realtimebidding.v1.model.CreativeDimensions;
import com.google.api.services.realtimebidding.v1.model.NumericTargetingDimension;
import com.google.api.services.realtimebidding.v1.model.PretargetingConfig;
import com.google.api.services.realtimebidding.v1.model.StringTargetingDimension;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.UUID;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** Creates a pretargeting configuration for the given bidder account ID. */
public class CreatePretargetingConfigs {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) throws IOException {
    Integer accountId = parsedArgs.getInt("account_id");

    String parentBidderName = String.format("bidders/%s", accountId);

    NumericTargetingDimension geoTargeting = new NumericTargetingDimension();
    geoTargeting.setIncludedIds(parsedArgs.<Long>getList("included_geo_ids"));
    geoTargeting.setExcludedIds(parsedArgs.<Long>getList("excluded_geo_ids"));

    NumericTargetingDimension userListTargeting = new NumericTargetingDimension();
    userListTargeting.setIncludedIds(parsedArgs.<Long>getList("included_user_list_ids"));
    userListTargeting.setExcludedIds(parsedArgs.<Long>getList("excluded_user_list_ids"));

    NumericTargetingDimension verticalTargeting = new NumericTargetingDimension();
    verticalTargeting.setIncludedIds(parsedArgs.<Long>getList("included_vertical_ids"));
    verticalTargeting.setExcludedIds(parsedArgs.<Long>getList("excluded_vertical_ids"));

    CreativeDimensions dimensions = new CreativeDimensions();
    dimensions.setHeight(parsedArgs.getLong("included_creative_dimension_height"));
    dimensions.setWidth(parsedArgs.getLong("included_creative_dimension_width"));

    StringTargetingDimension webTargeting = new StringTargetingDimension();
    webTargeting.setTargetingMode(parsedArgs.getString("web_targeting_mode"));
    webTargeting.setValues(parsedArgs.<String>getList("web_targeting_urls"));

    StringTargetingDimension mobileAppTargeting = new StringTargetingDimension();
    mobileAppTargeting.setTargetingMode(parsedArgs.getString("mobile_app_targeting_mode"));
    mobileAppTargeting.setValues(parsedArgs.<String>getList("mobile_app_targeting_app_ids"));

    NumericTargetingDimension mobileAppCategoryTargeting = new NumericTargetingDimension();
    mobileAppCategoryTargeting.setIncludedIds(
        parsedArgs.<Long>getList("included_mobile_app_targeting_category_ids"));
    mobileAppCategoryTargeting.setExcludedIds(
        parsedArgs.<Long>getList("excluded_mobile_app_targeting_category_ids"));

    AppTargeting appTargeting = new AppTargeting();
    appTargeting.setMobileAppTargeting(mobileAppTargeting);
    appTargeting.setMobileAppCategoryTargeting(mobileAppCategoryTargeting);

    StringTargetingDimension publisherTargeting = new StringTargetingDimension();
    publisherTargeting.setTargetingMode(parsedArgs.getString("publisher_targeting_mode"));
    publisherTargeting.setValues(parsedArgs.<String>getList("publisher_ids"));

    PretargetingConfig newPretargetingConfig = new PretargetingConfig();
    newPretargetingConfig.setDisplayName(parsedArgs.getString("display_name"));
    newPretargetingConfig.setIncludedFormats(parsedArgs.<String>getList("included_formats"));
    newPretargetingConfig.setGeoTargeting(geoTargeting);
    newPretargetingConfig.setUserListTargeting(userListTargeting);
    newPretargetingConfig.setInterstitialTargeting(parsedArgs.getString("interstitial_targeting"));
    newPretargetingConfig.setAllowedUserTargetingModes(
        parsedArgs.<String>getList("allowed_user_targeting_modes"));
    newPretargetingConfig.setExcludedContentLabelIds(
        parsedArgs.<Long>getList("excluded_content_label_ids"));
    newPretargetingConfig.setIncludedUserIdTypes(
        parsedArgs.<String>getList("included_user_id_types"));
    newPretargetingConfig.setIncludedLanguages(parsedArgs.<String>getList("included_languages"));
    newPretargetingConfig.setIncludedMobileOperatingSystemIds(
        parsedArgs.<Long>getList("included_mobile_os_ids"));
    newPretargetingConfig.setVerticalTargeting(verticalTargeting);
    newPretargetingConfig.setIncludedPlatforms(parsedArgs.<String>getList("included_platforms"));
    newPretargetingConfig.setIncludedCreativeDimensions(Collections.singletonList(dimensions));
    newPretargetingConfig.setIncludedEnvironments(
        parsedArgs.<String>getList("included_environments"));
    newPretargetingConfig.setWebTargeting(webTargeting);
    newPretargetingConfig.setAppTargeting(appTargeting);
    newPretargetingConfig.setPublisherTargeting(publisherTargeting);
    newPretargetingConfig.setMinimumViewabilityDecile(
        parsedArgs.getInt("minimum_viewability_decile"));

    PretargetingConfig pretargetingConfig =
        client
            .bidders()
            .pretargetingConfigs()
            .create(parentBidderName, newPretargetingConfig)
            .execute();

    System.out.printf(
        "Created pretargeting configuration for bidder Account ID '%s':\n", accountId);
    Utils.printPretargetingConfig(pretargetingConfig);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("CreatePretargetingConfigs")
            .build()
            .defaultHelp(true)
            .description(("Creates a pretargeting configuration for the given bidder account ID."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the bidders resource under which the pretargeting "
                + "configuration is to be created.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-d", "--display_name")
        .help(
            "The display name to associate with the new configuration. Must be unique among "
                + "all of a bidder's pretargeting configurations.")
        .setDefault(String.format("TEST_PRETARGETING_CONFIG_%s", UUID.randomUUID()));
    parser
        .addArgument("--included_formats")
        .help(
            "Creative formats included by this configuration. Specify each ID separated by a space."
                + " An unset value will not filter any bid requests based on the format. Valid"
                + " values include: HTML, NATIVE, and VAST.")
        .type(String.class)
        .nargs("*");
    parser
        .addArgument("--included_geo_ids")
        .help(
            "The geo IDs to include in targeting for this configuration. Specify each ID "
                + "separated by a space. Valid geo IDs can be found in:"
                + "https://storage.googleapis.com/adx-rtb-dictionaries/geo-table.csv")
        .type(Long.class)
        .nargs("*");
    parser
        .addArgument("--excluded_geo_ids")
        .help(
            "The geo IDs to exclude in targeting for this configuration. Specify each ID "
                + "separated by a space. Valid geo IDs can be found in:"
                + "https://storage.googleapis.com/adx-rtb-dictionaries/geo-table.csv")
        .type(Long.class)
        .nargs("*");
    parser
        .addArgument("--included_user_list_ids")
        .help(
            "The user list IDs to include in targeting for this configuration. Specify each ID"
                + " separated by a space. Valid user list IDs would include any found under the"
                + " buyers.userLists resource for a given bidder account, or any buyer accounts"
                + " under it.")
        .type(Long.class)
        .nargs("*");
    parser
        .addArgument("--excluded_user_list_ids")
        .help(
            "The user list IDs to exclude in targeting for this configuration. Specify each ID"
                + " separated by a space. Valid user list IDs would include any found under the"
                + " buyers.userLists resource for a given bidder account, or any buyer accounts"
                + " under it.")
        .type(Long.class)
        .nargs("*");
    parser
        .addArgument("--interstitial_targeting")
        .help(
            "The interstitial targeting specified for this configuration. By default, this "
                + "will be set to ONLY_NON_INTERSTITIAL_REQUESTS. Valid values include: "
                + "ONLY_INTERSTITIAL_REQUESTS and ONLY_NON_INTERSTITIAL_REQUESTS.")
        .setDefault("ONLY_NON_INTERSTITIAL_REQUESTS");
    parser
        .addArgument("--allowed_user_targeting_modes")
        .help(
            "The targeting modes to include in targeting for this configuration. Specify each "
                + "value separated by a space. Valid targeting modes include: REMARKETING_ADS and "
                + "INTEREST_BASED_TARGETING.")
        .type(String.class)
        .nargs("*");
    parser
        .addArgument("--excluded_content_label_ids")
        .help(
            "The sensitive content category IDs excluded in targeting for this configuration."
                + " Specify each value separated by a space. Valid sensitive content category IDs"
                + " can be found in:"
                + " https://storage.googleapis.com/adx-rtb-dictionaries/content-labels.txt")
        .type(Long.class)
        .nargs("*");
    parser
        .addArgument("--included_user_id_types")
        .help(
            "The user identifier types included in targeting for this configuration. Specify "
                + "each value separated by a space. Valid values include: HOSTED_MATCH_DATA, "
                + "GOOGLE_COOKIE, and DEVICE_ID.")
        .type(String.class)
        .nargs("*");
    parser
        .addArgument("--included_language_codes")
        .help(
            "The languages represented by languages codes that are included in targeting for this"
                + " configuration. Specify each code separated by a space. Valid language codes can"
                + " be found in: "
                + "https://developers.google.com/adwords/api/docs/appendix/languagecodes.")
        .type(String.class)
        .nargs("*");
    parser
        .addArgument("--included_mobile_os_ids")
        .help(
            "The mobile OS IDs to include in targeting for this configuration. Specify each "
                + "value separated by a space. Valid mobile OS IDs can be found in: "
                + "https://storage.googleapis.com/adx-rtb-dictionaries/mobile-os.csv")
        .type(Long.class)
        .nargs("*");
    parser
        .addArgument("--included_vertical_ids")
        .help(
            "The vertical IDs to include in targeting for this configuration. Specify each ID"
                + " separated by a space. Valid vertical IDs can be found in: "
                + "https://developers.google.com/authorized-buyers/rtb/downloads/publisher-verticals")
        .type(Long.class)
        .nargs("*");
    parser
        .addArgument("--excluded_vertical_ids")
        .help(
            "The vertical IDs to exclude in targeting for this configuration. Specify each ID"
                + " separated by a space. Valid vertical IDs can be found in: "
                + "https://developers.google.com/authorized-buyers/rtb/downloads/publisher-verticals")
        .type(Long.class)
        .nargs("*");
    parser
        .addArgument("--included_platforms")
        .help(
            "The platforms to include in targeting for this configuration. Specify each value"
                + " separated by a space. Valid values include: PERSONAL_COMPUTER, PHONE, TABLET,"
                + " and CONNECTED_TV.")
        .type(String.class)
        .nargs("*");
    parser
        .addArgument("--included_creative_dimension_height")
        .help(
            "A creative dimension's height to be included in targeting for this configuration. By"
                + " default, this example will set the targeted height to 300. Note that while only"
                + " a single set of dimensions are specified in this sample, pretargeting"
                + " configurations can target multiple creative dimensions.")
        .type(Long.class)
        .setDefault(300L);
    parser
        .addArgument("--included_creative_dimension_width")
        .help(
            "A creative dimension's width to be included in targeting for this configuration. By"
                + " default, this example will set the targeted width to 250. Note that while only"
                + " a single set of dimensions are specified in this sample, pretargeting"
                + " configurations can target multiple creative dimensions.")
        .type(Long.class)
        .setDefault(250L);
    parser
        .addArgument("--included_environments")
        .help(
            "The environments to include in targeting for this configuration. Specify each "
                + "value separated by a space. Valid values include: APP, and WEB.")
        .type(String.class)
        .nargs("*");
    parser
        .addArgument("--web_targeting_mode")
        .help(
            "The targeting mode for this configuration's web targeting. Valid values include: "
                + "INCLUSIVE, and EXCLUSIVE.")
        .type(String.class);
    parser
        .addArgument("--web_targeting_urls")
        .help(
            "The URLs specified for this configuration's web targeting, which allows one to target"
                + " a subset of site inventory. Specify each value separated by a space. Values"
                + " specified must be valid URLs.")
        .type(String.class)
        .nargs("*");
    parser
        .addArgument("--mobile_app_targeting_mode")
        .help(
            "The targeting mode for the configuration's mobile app targeting. Valid values "
                + "include: INCLUSIVE, and EXCLUSIVE.")
        .type(String.class);
    parser
        .addArgument("--mobile_app_targeting_app_ids")
        .help(
            "The mobile app IDs specified for this configuration's mobile app targeting, which"
                + " allows one to target a subset of mobile app inventory. Specify each value"
                + " separated by a space. Values specified must be valid mobile App IDs, as found"
                + " on their respective app stores.")
        .type(String.class)
        .nargs("*");
    parser
        .addArgument("--included_mobile_app_targeting_category_ids")
        .help(
            "The mobile app category IDs to include in targeting for this configuration. "
                + "Specify each ID separated by a space. Valid category IDs can be found in: "
                + "https://developers.google.com/adwords/api/docs/appendix/mobileappcategories.csv")
        .type(Long.class)
        .nargs("*");
    parser
        .addArgument("--excluded_mobile_app_targeting_category_ids")
        .help(
            "The mobile app category IDs to exclude in targeting for this configuration. "
                + "Specify each ID separated by a space. Valid category IDs can be found in: "
                + "https://developers.google.com/adwords/api/docs/appendix/mobileappcategories.csv")
        .type(Long.class)
        .nargs("*");
    parser
        .addArgument("--publisher_targeting_mode")
        .help(
            "The targeting mode for the configuration's publisher targeting. Valid values "
                + "include: INCLUSIVE, and EXCLUSIVE.")
        .type(String.class);
    parser
        .addArgument("--publisher_ids")
        .help(
            "The publisher IDs specified for this configuration's publisher targeting, which allows"
                + " one to target a subset of publisher inventory. Specify each ID separated by a"
                + " space. Valid publisher IDs can be found in Real-time Bidding bid requests, or"
                + " alternatively in ads.txt / app-ads.txt. For more information, see: "
                + "https://iabtechlab.com/ads-txt/")
        .type(String.class)
        .nargs("*");
    parser
        .addArgument("--minimum_viewability_decile")
        .help(
            "The targeted minimum viewability decile, ranging from 0 - 10. A value of '5' means"
                + " that the configuration will only match adslots for which we predict at least"
                + " 50% viewability. Values > 10 will be rounded down to 10. An unset value, or a"
                + " value of '0', indicates that bid requests should be sent regardless of"
                + " viewability.")
        .type(String.class)
        .setDefault(5);

    Namespace parsedArgs = null;
    try {
      parsedArgs = parser.parseArgs(args);
    } catch (ArgumentParserException ex) {
      parser.handleError(ex);
      System.exit(1);
    }

    RealTimeBidding client = null;
    try {
      client = Utils.getRealTimeBiddingClient();
    } catch (IOException ex) {
      System.out.printf("Unable to create RealTimeBidding API service:\n%s", ex);
      System.out.println("Did you specify a valid path to a service account key file?");
      System.exit(1);
    } catch (GeneralSecurityException ex) {
      System.out.printf("Unable to establish secure HttpTransport:\n%s", ex);
      System.exit(1);
    }

    try {
      execute(client, parsedArgs);
    } catch (IOException ex) {
      System.out.printf("RealTimeBidding API returned error response:\n%s", ex);
      System.exit(1);
    }
  }
}
