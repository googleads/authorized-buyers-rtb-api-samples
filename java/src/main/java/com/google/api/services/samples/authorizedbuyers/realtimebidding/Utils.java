/*
 * Copyright (c) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.services.samples.authorizedbuyers.realtimebidding;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.pubsub.Pubsub;
import com.google.api.services.pubsub.PubsubScopes;
import com.google.api.services.realtimebidding.v1.RealTimeBidding;
import com.google.api.services.realtimebidding.v1.RealTimeBiddingScopes;
import com.google.api.services.realtimebidding.v1.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/** Utilities used by the Authorized Buyers Real-time Bidding API samples. */
public class Utils {
  /**
   * Specify the name of your application. If the application name is {@code null} or blank, the
   * application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
   */
  private static final String APPLICATION_NAME = "";

  /** Full path to JSON Key file - include file name */
  private static final java.io.File JSON_FILE = new java.io.File("INSERT_PATH_TO_JSON_FILE");

  /**
   * Global instance of a DateTimeFormatter used to parse LocalDate instances and convert them to
   * String.
   */
  private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("Y/M/d");

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  /**
   * Global instance of the maximum page size, which will be the default page size for samples with
   * pagination.
   */
  private static final Integer MAXIMUM_PAGE_SIZE = 50;

  /**
   * Authorizes the application to access the user's protected data.
   *
   * @throws IOException if the {@code JSON_FILE} can not be read.
   * @return An instantiated GoogleCredentials instance.
   */
  private static GoogleCredentials authorize() throws IOException {
    GoogleCredentials credentials;

    try (FileInputStream serviceAccountStream = new FileInputStream((JSON_FILE))) {
      Set<String> scopes = new HashSet<>(RealTimeBiddingScopes.all());
      scopes.add(PubsubScopes.PUBSUB);

      credentials = ServiceAccountCredentials.fromStream(serviceAccountStream).createScoped(scopes);
    }

    return credentials;
  }

  /** Helper method to produce an appropriate indent for the given indentLevel. */
  private static String getIndent(int indentLevel) {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < indentLevel; i++) {
      builder.append('\t');
    }

    return builder.toString();
  }

  /** Helper method to produce a prefix for a printed field. */
  private static String getPrefix(int indentLevel) {
    StringBuilder builder = new StringBuilder();

    builder.append(getIndent(indentLevel));

    // Message names will have no indent and be denoted with an asterisk.
    if (indentLevel == 0) {
      builder.append("* ");
    } else {
      builder.append("- ");
    }

    return builder.toString();
  }

  /** Helper method to print a {@code String} field. */
  private static void printField(String fieldDesc, String field, int indentLevel) {
    if (field != null) {
      String prefix = getPrefix(indentLevel);

      System.out.printf("%s%s: %s%n", prefix, fieldDesc, field);
    }
  }

  /** Helper method to print an {@code Integer} field. */
  private static void printField(String fieldDesc, Integer field, int indentLevel) {
    if (field != null) {
      String prefix = getPrefix(indentLevel);

      System.out.printf("%s%s: %d%n", prefix, fieldDesc, field);
    }
  }

  /** Helper method to print an {@code Long} field. */
  private static void printField(String fieldDesc, Long field, int indentLevel) {
    if (field != null) {
      String prefix = getPrefix(indentLevel);

      System.out.printf("%s%s: %d%n", prefix, fieldDesc, field);
    }
  }

  /** Helper method to print an {@code Double} field. */
  private static void printField(String fieldDesc, Double field, int indentLevel) {
    if (field != null) {
      String prefix = getPrefix(indentLevel);

      System.out.printf("%s%s: %f%n", prefix, fieldDesc, field);
    }
  }

  /** Helper method to print an {@code Boolean} field. */
  private static void printField(String fieldDesc, Boolean field, int indentLevel) {
    if (field != null) {
      String prefix = getPrefix(indentLevel);

      System.out.printf("%s%s: %b%n", prefix, fieldDesc, field);
    }
  }

  /** Helper method to print a {@code List} of {@code String} values. */
  private static void printStringList(String fieldDesc, List<String> values, int indentLevel) {
    if (values != null) {
      String descPrefix = getPrefix(indentLevel);
      String itemPrefix = getPrefix(++indentLevel);
      System.out.printf("%s%s:%n", descPrefix, fieldDesc);

      for (String item : values) {
        System.out.printf("%s%s%n", itemPrefix, item);
      }
    }
  }

  /** Helper method to print a {@code List} of {@code Integer} values. */
  private static void printIntegerList(String fieldDesc, List<Integer> values, int indentLevel) {
    if (values != null) {
      String descPrefix = getPrefix(indentLevel);
      String itemPrefix = getPrefix(++indentLevel);
      System.out.printf("%s%s:%n", descPrefix, fieldDesc);

      for (Integer item : values) {
        System.out.printf("%s%d%n", itemPrefix, item);
      }
    }
  }

  /** Helper method to print a {@code List} of {@code Long} values. */
  private static void printLongList(String fieldDesc, List<Long> values, int indentLevel) {
    if (values != null) {
      String descPrefix = getPrefix(indentLevel);
      String itemPrefix = getPrefix(++indentLevel);
      System.out.printf("%s%s:%n", descPrefix, fieldDesc);

      for (Long item : values) {
        System.out.printf("%s%d%n", itemPrefix, item);
      }
    }
  }

  /**
   * Converts a {@code Date} instance into a human-readable String format.
   *
   * @param date a {@code Date} instance.
   * @return A human-readable {@code String} representation of the given {@code Date} instance.
   */
  public static String convertDateToString(Date date) {
    return String.format("%d/%02d/%02d", date.getYear(), date.getMonth(), date.getDay());
  }

  /**
   * Converts a {@code LocalDate} instance to a corresponding {@code Date} instance.
   *
   * @param date A {code LocalDate} instance to be converted to the {@code Date} type used by the
   *     Real-time Bidding API client library.
   * @return An instantiated {@code Date} instance.
   */
  public static Date convertJodaLocalDateToRTBDate(LocalDate date) {
    return new Date()
        .setDay(date.getDayOfMonth())
        .setMonth(date.getMonthOfYear())
        .setYear(date.getYear());
  }

  /**
   * Retrieve a {@code DateTimeFormatter} instance used to parse and serialize {@code LocalDate}.
   *
   * @return An initialized {@code DateTimeFormatter} instance.
   */
  public static DateTimeFormatter getDateTimeFormatterForLocalDate() {
    return dateFormatter;
  }

  /**
   * Performs all necessary setup steps for running requests against the Google Cloud Pubsub API.
   *
   * @return An initialized {@code Pubsub} service object.
   */
  public static Pubsub getPubsubClient() throws IOException, GeneralSecurityException {
    GoogleCredentials credentials = authorize();
    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

    return new Pubsub.Builder(httpTransport, JSON_FACTORY, requestInitializer)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  /**
   * Retrieve the default maximum page size.
   *
   * @return An Integer representing the default maximum page size for samples with pagination.
   */
  public static Integer getMaximumPageSize() {
    return MAXIMUM_PAGE_SIZE;
  }

  /**
   * Performs all necessary setup steps for running requests against the Real-time Bidding API.
   *
   * @return An initialized RealTimeBidding service object.
   */
  public static RealTimeBidding getRealTimeBiddingClient()
      throws IOException, GeneralSecurityException {
    GoogleCredentials credentials = authorize();
    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

    return new RealTimeBidding.Builder(httpTransport, JSON_FACTORY, requestInitializer)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  /** Prints a {@code Bidder} instance in a human-readable format. */
  public static void printBidder(Bidder bidder) {
    System.out.printf("* Bidder name: %s\n", bidder.getName());

    String cookieMatchingUrl = bidder.getCookieMatchingUrl();
    if (cookieMatchingUrl != null) {
      System.out.printf("\t- Cookie Matching URL: %s\n", cookieMatchingUrl);
    }

    String cookieMatchingNetworkId = bidder.getCookieMatchingNetworkId();
    if (cookieMatchingNetworkId != null) {
      System.out.printf("\t- Cookie Matching Network ID: %s\n", cookieMatchingNetworkId);
    }

    Boolean bypassNonGuaranteedDealsPretargeting = bidder.getBypassNonguaranteedDealsPretargeting();
    if (bypassNonGuaranteedDealsPretargeting != null) {
      System.out.printf(
          "\t- Bypass Non-Guaranteed Deals Pretargeting: %s\n",
          bypassNonGuaranteedDealsPretargeting);
    }

    String dealsBillingId = bidder.getDealsBillingId();
    if (dealsBillingId != null) {
      System.out.printf("\t- Deals ID: %s\n", dealsBillingId);
    }
  }

  /** Prints a {@code Buyer} instance in a human-readable format. */
  public static void printBuyer(Buyer buyer) {
    System.out.printf("* Buyer name: %s\n", buyer.getName());

    String displayName = buyer.getDisplayName();
    if (displayName != null) {
      System.out.printf("\t- Display name: %s\n", displayName);
    }

    String bidder = buyer.getBidder();
    if (bidder != null) {
      System.out.printf("\t- Bidder: %s\n", bidder);
    }

    Long activeCreativeCount = buyer.getActiveCreativeCount();
    if (activeCreativeCount != null) {
      System.out.printf("\t- Active creative count: %s\n", activeCreativeCount);
    }

    Long maximumActiveCreativeCount = buyer.getMaximumActiveCreativeCount();
    if (maximumActiveCreativeCount != null) {
      System.out.printf("\t- Maximum active creative count: %s\n", maximumActiveCreativeCount);
    }

    List<String> billingIds = buyer.getBillingIds();
    if (billingIds != null && !billingIds.isEmpty()) {
      System.out.println("\t- Billing IDs:");
      for (String billingId : billingIds) {
        System.out.printf("\t\t%s\n", billingId);
      }
    }
  }

  /** Prints a {@code Creative} instance in a human-readable format. */
  public static void printCreative(Creative creative) {
    System.out.printf("* Creative name: %s\n", creative.getName());

    String advertiserName = creative.getAdvertiserName();
    if (advertiserName != null) {
      System.out.printf("\t- Advertiser name: %s\n", advertiserName);
    }

    Integer version = creative.getVersion();
    if (version != null) {
      System.out.printf("\t- Version: %d\n", version);
    }

    System.out.printf("\t- Creative format: %s\n", creative.getCreativeFormat());

    CreativeServingDecision servingDecision = creative.getCreativeServingDecision();
    if (servingDecision != null) {
      System.out.println("\t- Creative serving decision");
      System.out.printf(
          "\t\t- Deals policy compliance status: %s\n",
          servingDecision.getDealsPolicyCompliance().getStatus());
      System.out.printf(
          "\t\t- Network policy compliance status: %s\n",
          servingDecision.getNetworkPolicyCompliance().getStatus());
      System.out.printf(
          "\t\t- Platform policy compliance status: %s\n",
          servingDecision.getPlatformPolicyCompliance().getStatus());
      System.out.printf(
          "\t\t- China policy compliance status: %s\n",
          servingDecision.getChinaPolicyCompliance().getStatus());
      System.out.printf(
          "\t\t- Russia policy compliance status: %s\n",
          servingDecision.getRussiaPolicyCompliance().getStatus());
    }

    List<String> declaredClickThroughUrls = creative.getDeclaredClickThroughUrls();
    if (declaredClickThroughUrls != null && !declaredClickThroughUrls.isEmpty()) {
      System.out.println("\t- Declared click-through URLs:");
      for (String declaredClickThroughUrl : declaredClickThroughUrls) {
        System.out.printf("\t\t%s\n", declaredClickThroughUrl);
      }
    }

    List<String> declaredAttributes = creative.getDeclaredAttributes();
    if (declaredAttributes != null && !declaredAttributes.isEmpty()) {
      System.out.println("\t- Declared attributes:");
      for (String declaredAttribute : declaredAttributes) {
        System.out.printf("\t\t%s\n", declaredAttribute);
      }
    }

    List<Integer> declaredVendorIds = creative.getDeclaredVendorIds();
    if (declaredVendorIds != null && !declaredVendorIds.isEmpty()) {
      System.out.println("\t- Declared vendor IDs:");
      for (Integer declaredVendorId : declaredVendorIds) {
        System.out.printf("\t\t%d\n", declaredVendorId);
      }
    }

    List<String> declaredRestrictedCategories = creative.getDeclaredRestrictedCategories();
    if (declaredRestrictedCategories != null && !declaredRestrictedCategories.isEmpty()) {
      System.out.println("\t- Declared restricted categories:");
      for (String declaredRestrictedCategory : declaredRestrictedCategories) {
        System.out.printf("\t\t%s\n", declaredRestrictedCategory);
      }
    }

    HtmlContent htmlContent = creative.getHtml();
    if (htmlContent != null) {
      System.out.println("\t- HTML creative contents:");
      System.out.printf("\t\tSnippet: %s\n", htmlContent.getSnippet());
      System.out.printf("\t\tHeight: %d\n", htmlContent.getHeight());
      System.out.printf("\t\tSnippet: %d\n", htmlContent.getWidth());
    }

    NativeContent nativeContent = creative.getNative();
    if (nativeContent != null) {
      System.out.println("\t- Native creative contents:");
      System.out.printf("\t\tHeadline: %s\n", nativeContent.getHeadline());
      System.out.printf("\t\tBody: %s\n", nativeContent.getBody());
      System.out.printf("\t\tCallToAction: %s\n", nativeContent.getCallToAction());
      System.out.printf("\t\tAdvertiser name: %s\n", nativeContent.getAdvertiserName());
      System.out.printf("\t\tStar rating: %f\n", nativeContent.getStarRating());
      System.out.printf("\t\tClick link URL: %s\n", nativeContent.getClickLinkUrl());
      System.out.printf("\t\tClick tracking URL: %s\n", nativeContent.getClickTrackingUrl());
      System.out.printf("\t\tPrice display text: %s\n", nativeContent.getPriceDisplayText());

      Image image = nativeContent.getImage();
      if (image != null) {
        System.out.println("\t\tImage contents:");
        System.out.printf("\t\t\tURL: %s\n", image.getUrl());
        System.out.printf("\t\t\tHeight: %d\n", image.getHeight());
        System.out.printf("\t\t\tWidth: %d\n", image.getWidth());
      }

      Image logo = nativeContent.getLogo();
      if (logo != null) {
        System.out.println("\t\tLogo contents:");
        System.out.printf("\t\t\tURL: %s\n", logo.getUrl());
        System.out.printf("\t\t\tHeight: %d\n", logo.getHeight());
        System.out.printf("\t\t\tWidth: %d\n", logo.getWidth());
      }

      Image appIcon = nativeContent.getAppIcon();
      if (appIcon != null) {
        System.out.println("\t\tApp icon contents:");
        System.out.printf("\t\t\tURL: %s\n", appIcon.getUrl());
        System.out.printf("\t\t\tHeight: %d\n", appIcon.getHeight());
        System.out.printf("\t\t\tWidth: %d\n", appIcon.getWidth());
      }
    }

    VideoContent videoContent = creative.getVideo();
    if (videoContent != null) {
      System.out.println("\t- Video creative contents:");

      String videoUrl = videoContent.getVideoUrl();
      if (videoUrl != null) {
        System.out.printf("\t\tVideo URL: %s\n", videoUrl);
      }

      String videoVastXML = videoContent.getVideoVastXml();
      if (videoVastXML != null) {
        System.out.printf("\t\tVideo VAST XML: %s\n", videoVastXML);
      }
    }
  }

  /** Prints a {@code Endpoint} instance in a human-readable format. */
  public static void printEndpoint(Endpoint endpoint) {
    System.out.printf("* Endpoint name: %s\n", endpoint.getName());

    String url = endpoint.getUrl();
    if (url != null) {
      System.out.printf("\t- URL: %s\n", url);
    }

    Long maximumQps = endpoint.getMaximumQps();
    if (maximumQps != null) {
      System.out.printf("\t- Maximum QPS: %d\n", maximumQps);
    }

    String tradingLocation = endpoint.getTradingLocation();
    if (tradingLocation != null) {
      System.out.printf("\t- Trading location: %s\n", tradingLocation);
    }

    String bidProtocol = endpoint.getBidProtocol();
    if (bidProtocol != null) {
      System.out.printf("\t- Bid protocol: %s\n", bidProtocol);
    }
  }

  /** Prints a {@code PretargetingConfig} instance in a human-readable format. */
  public static void printPretargetingConfig(PretargetingConfig pretargetingConfig) {
    System.out.printf("* Pretargeting configuration name: %s\n", pretargetingConfig.getName());
    System.out.printf("\t- Display name: %s\n", pretargetingConfig.getDisplayName());
    System.out.printf("\t- Billing ID: %s\n", pretargetingConfig.getBillingId());
    System.out.printf("\t- State: %s\n", pretargetingConfig.getState());

    Long maximumQps = pretargetingConfig.getMaximumQps();
    if (maximumQps != null) {
      System.out.printf("\t- Maximum QPS: %s\n", maximumQps);
    }

    String interstitialTargeting = pretargetingConfig.getInterstitialTargeting();
    if (interstitialTargeting != null) {
      System.out.printf("\t- Interstitial targeting: %s\n", interstitialTargeting);
    }

    Integer minimumViewabilityDecile = pretargetingConfig.getMinimumViewabilityDecile();
    if (minimumViewabilityDecile != null) {
      System.out.printf("\t- Minimum viewability decile: %s\n", minimumViewabilityDecile);
    }

    List<String> includedFormats = pretargetingConfig.getIncludedFormats();
    if (includedFormats != null && !includedFormats.isEmpty()) {
      System.out.println("\t- Included formats:");
      for (String includedFormat : includedFormats) {
        System.out.printf("\t\t%s\n", includedFormat);
      }
    }

    NumericTargetingDimension geoTargeting = pretargetingConfig.getGeoTargeting();
    if (geoTargeting != null) {
      System.out.println("\t- Geo targeting:");

      List<Long> includedIds = geoTargeting.getIncludedIds();
      if (includedIds != null && !includedIds.isEmpty()) {
        System.out.println("\t\t- Included geo IDs:");
        for (Long id : includedIds) {
          System.out.printf("\t\t\t%s\n", id);
        }
      }

      List<Long> excludedIds = geoTargeting.getExcludedIds();
      if (excludedIds != null && !excludedIds.isEmpty()) {
        System.out.println("\t\t- Excluded geo IDs:");
        for (Long id : excludedIds) {
          System.out.printf("\t\t\t%s\n", id);
        }
      }
    }

    List<Long> invalidGeoIds = pretargetingConfig.getInvalidGeoIds();
    if (invalidGeoIds != null && !invalidGeoIds.isEmpty()) {
      System.out.println("\t- Invalid geo IDs:");
      for (Long id : invalidGeoIds) {
        System.out.printf("\t\t%s\n", id);
      }
    }

    NumericTargetingDimension userListTargeting = pretargetingConfig.getUserListTargeting();
    if (userListTargeting != null) {
      System.out.println("\t- User list targeting:");

      List<Long> includedIds = userListTargeting.getIncludedIds();
      if (includedIds != null && !includedIds.isEmpty()) {
        System.out.println("\t\t- Included user list IDs:");
        for (Long id : includedIds) {
          System.out.printf("\t\t\t%s\n", id);
        }
      }

      List<Long> excludedIds = userListTargeting.getExcludedIds();
      if (excludedIds != null && !excludedIds.isEmpty()) {
        System.out.println("\t\t- Excluded user list IDs:");
        for (Long id : excludedIds) {
          System.out.printf("\t\t\t%s\n", id);
        }
      }
    }

    List<String> allowedUserTargetingModes = pretargetingConfig.getAllowedUserTargetingModes();
    if (allowedUserTargetingModes != null && !allowedUserTargetingModes.isEmpty()) {
      System.out.println("\t- Allowed user targeting modes:");
      for (String userTargetingMode : allowedUserTargetingModes) {
        System.out.printf("\t\t%s\n", userTargetingMode);
      }
    }

    List<Long> excludedContentLabelIds = pretargetingConfig.getExcludedContentLabelIds();
    if (excludedContentLabelIds != null && !excludedContentLabelIds.isEmpty()) {
      System.out.println("\t- Excluded content label IDs:");
      for (Long id : excludedContentLabelIds) {
        System.out.printf("\t\t%s\n", id);
      }
    }

    List<String> includedUserIdTypes = pretargetingConfig.getIncludedUserIdTypes();
    if (includedUserIdTypes != null && !includedUserIdTypes.isEmpty()) {
      System.out.println("\t- Included user ID types:");
      for (String userIdType : includedUserIdTypes) {
        System.out.printf("\t\t%s\n", userIdType);
      }
    }

    List<String> includedLanguages = pretargetingConfig.getIncludedLanguages();
    if (includedLanguages != null && !includedLanguages.isEmpty()) {
      System.out.println("\t- Included languages:");
      for (String language : includedLanguages) {
        System.out.printf("\t\t%s\n", language);
      }
    }

    List<Long> includedMobileOSIds = pretargetingConfig.getIncludedMobileOperatingSystemIds();
    if (includedMobileOSIds != null && !includedMobileOSIds.isEmpty()) {
      System.out.println("\t- Included mobile operating system IDs:");
      for (Long id : includedMobileOSIds) {
        System.out.printf("\t\t%s\n", id);
      }
    }

    NumericTargetingDimension verticalTargeting = pretargetingConfig.getVerticalTargeting();
    if (verticalTargeting != null) {
      System.out.println("\t- Vertical targeting:");

      List<Long> includedIds = verticalTargeting.getIncludedIds();
      if (includedIds != null && !includedIds.isEmpty()) {
        System.out.println("\t\t- Included vertical IDs:");
        for (Long id : includedIds) {
          System.out.printf("\t\t\t%s\n", id);
        }
      }

      List<Long> excludedIds = verticalTargeting.getExcludedIds();
      if (excludedIds != null && !excludedIds.isEmpty()) {
        System.out.println("\t\t- Excluded vertical IDs:");
        for (Long id : excludedIds) {
          System.out.printf("\t\t\t%s\n", id);
        }
      }
    }

    List<String> includedPlatforms = pretargetingConfig.getIncludedPlatforms();
    if (includedPlatforms != null && !includedPlatforms.isEmpty()) {
      System.out.println("\t- Included platforms:");
      for (String platform : includedPlatforms) {
        System.out.printf("\t\t%s\n", platform);
      }
    }

    List<CreativeDimensions> creativeDimensions =
        pretargetingConfig.getIncludedCreativeDimensions();
    if (creativeDimensions != null && !creativeDimensions.isEmpty()) {
      System.out.println("\t- Included creative dimensions:");
      for (CreativeDimensions dimensions : creativeDimensions) {
        System.out.printf(
            "\t\tHeight: %s; Width: %s\n", dimensions.getHeight(), dimensions.getWidth());
      }
    }

    List<String> includedEnvironments = pretargetingConfig.getIncludedEnvironments();
    if (includedEnvironments != null && !includedEnvironments.isEmpty()) {
      System.out.println("\t- Included environments:");
      for (String environment : includedEnvironments) {
        System.out.printf("\t\t%s\n", environment);
      }
    }

    StringTargetingDimension webTargeting = pretargetingConfig.getWebTargeting();
    if (webTargeting != null) {
      System.out.println("\t- Web targeting:");
      System.out.printf("\t\t- Targeting mode: %s\n", webTargeting.getTargetingMode());
      System.out.println("\t\t- Site URLs:");
      for (String siteUrl : webTargeting.getValues()) {
        System.out.printf("\t\t\t%s\n", siteUrl);
      }
    }

    AppTargeting appTargeting = pretargetingConfig.getAppTargeting();
    if (appTargeting != null) {
      System.out.println("\t- App targeting:");

      StringTargetingDimension mobileAppTargeting = appTargeting.getMobileAppTargeting();
      if (mobileAppTargeting != null) {
        System.out.println("\t\t- Mobile app targeting:");
        System.out.printf("\t\t\t- Targeting mode: %s\n", mobileAppTargeting.getTargetingMode());
        System.out.println("\t\t\t- Mobile App IDs:");
        for (String appId : mobileAppTargeting.getValues()) {
          System.out.printf("\t\t\t\t%s\n", appId);
        }
      }

      NumericTargetingDimension mobileAppCategoryTargeting =
          appTargeting.getMobileAppCategoryTargeting();
      if (mobileAppCategoryTargeting != null) {
        System.out.println("\t\t- Mobile app category targeting:");

        List<Long> includedIds = mobileAppCategoryTargeting.getIncludedIds();
        if (includedIds != null && !includedIds.isEmpty()) {
          System.out.println("\t\t- Included mobile app category targeting IDs:");
          for (Long id : includedIds) {
            System.out.printf("\t\t\t%s\n", id);
          }
        }

        List<Long> excludedIds = mobileAppCategoryTargeting.getExcludedIds();
        if (excludedIds != null && !excludedIds.isEmpty()) {
          System.out.println("\t\t- Excluded mobile app category targeting IDs:");
          for (Long id : excludedIds) {
            System.out.printf("\t\t\t%s\n", id);
          }
        }
      }
    }

    StringTargetingDimension publisherTargeting = pretargetingConfig.getPublisherTargeting();
    if (publisherTargeting != null) {
      System.out.println("\t- Publisher targeting:");
      System.out.printf("\t\t- Targeting mode: %s\n", publisherTargeting.getTargetingMode());
      System.out.println("\t\t- Publisher IDs:");
      for (String publisherId : publisherTargeting.getValues()) {
        System.out.printf("\t\t\t%s\n", publisherId);
      }
    }
  }

  /** Prints a {@code PublisherConnection} instance in a human-readable format. */
  public static void printPublisherConnection(PublisherConnection publisherConnection) {
    printField("Publisher connection name", publisherConnection.getName(), 0);
    printField("Publisher platform", publisherConnection.getPublisherPlatform(), 1);
    printField("Display name", publisherConnection.getDisplayName(), 1);
    printField("State", publisherConnection.getBiddingState(), 1);
    printField("Create time", publisherConnection.getCreateTime(), 1);
  }

  /** Prints a {@code UserList} instance in a human-readable format. */
  public static void printUserList(UserList userList) {
    System.out.printf("* UserList name: '%s'\n", userList.getName());

    String displayName = userList.getDisplayName();
    if (displayName != null) {
      System.out.printf("\tUserList display name: '%s'\n", displayName);
    }

    String description = userList.getDescription();
    if (description != null) {
      System.out.printf("\tUserList description: '%s'\n", description);
    }

    UrlRestriction urlRestriction = userList.getUrlRestriction();
    if (urlRestriction != null) {
      System.out.println("\tURL Restriction:");
      System.out.printf("\t\tURL: '%s'\n", urlRestriction.getUrl());
      System.out.printf("\t\tRestriction Type: '%s'\n", urlRestriction.getRestrictionType());

      Date startDate = urlRestriction.getStartDate();
      if (startDate != null) {
        System.out.printf("\t\tStart Date: '%s'\n", convertDateToString(startDate));
      }

      Date endDate = urlRestriction.getEndDate();
      if (endDate != null) {
        System.out.printf("\t\tEnd Date: '%s'\n", convertDateToString(endDate));
      }
    }

    System.out.printf("\tUserList status: '%s'\n", userList.getStatus());
    System.out.printf("\tMembership duration days: %s\n\n", userList.getMembershipDurationDays());
  }
}
