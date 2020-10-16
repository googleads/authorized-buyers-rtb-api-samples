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

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.realtimebidding.v1.model.Creative;
import com.google.api.services.realtimebidding.v1.model.CreativeServingDecision;
import com.google.api.services.realtimebidding.v1.model.Date;
import com.google.api.services.realtimebidding.v1.model.HtmlContent;
import com.google.api.services.realtimebidding.v1.model.Image;
import com.google.api.services.realtimebidding.v1.model.NativeContent;
import com.google.api.services.realtimebidding.v1.model.UrlRestriction;
import com.google.api.services.realtimebidding.v1.model.UserList;
import com.google.api.services.realtimebidding.v1.model.VideoContent;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.pubsub.Pubsub;
import com.google.api.services.pubsub.PubsubScopes;
import com.google.api.services.realtimebidding.v1.RealTimeBidding;
import com.google.api.services.realtimebidding.v1.RealTimeBiddingScopes;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Utilities used by the Authorized Buyers Real-time Bidding API samples.
 */
public class Utils {
  /**
   * Specify the name of your application. If the application name is {@code null} or blank, the
   * application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
   */
  private static final String APPLICATION_NAME = "";

  /** Full path to JSON Key file - include file name */
  private static final java.io.File JSON_FILE =
      new java.io.File("INSERT_PATH_TO_JSON_FILE");

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

  /** Authorizes the application to access the user's protected data.
   *
   * @throws IOException if the {@code JSON_FILE} can not be read.
   * @return An instantiated GoogleCredentials instance.
   */
  private static GoogleCredentials authorize() throws IOException {
    GoogleCredentials credentials;

    try (FileInputStream serviceAccountStream = new FileInputStream((JSON_FILE))) {
      Set<String> scopes = new HashSet<>(RealTimeBiddingScopes.all());
      scopes.add(PubsubScopes.PUBSUB);

      credentials = ServiceAccountCredentials
          .fromStream(serviceAccountStream)
          .createScoped(scopes);
    }

    return credentials;
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
   *   Real-time Bidding API client library.
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

    return new Pubsub.Builder(
        httpTransport, JSON_FACTORY, requestInitializer)
        .setApplicationName(APPLICATION_NAME).build();
  }

  /**
   * Retrieve the default maximum page size.
   *
   * @return An Integer representing the default maximum page size for samples with pagination.
   */
  public static Integer getMaximumPageSize() { return MAXIMUM_PAGE_SIZE; }

  /**
   * Performs all necessary setup steps for running requests against the Real-time Bidding API.
   *
   * @return An initialized RealTimeBidding service object.
   */
  public static RealTimeBidding getRealTimeBiddingClient() throws IOException,
      GeneralSecurityException {
    GoogleCredentials credentials = authorize();
    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

    return new RealTimeBidding.Builder(
        httpTransport, JSON_FACTORY, requestInitializer)
        .setApplicationName(APPLICATION_NAME).build();
  }

  /**
   * Prints a {@code Creative} instance in a human-readable format.
   */
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
      System.out.printf("\t\t- Deals policy compliance status: %s\n",
          servingDecision.getDealsPolicyCompliance().getStatus());
      System.out.printf("\t\t- Network policy compliance status: %s\n",
          servingDecision.getNetworkPolicyCompliance().getStatus());
      System.out.printf("\t\t- Platform policy compliance status: %s\n",
          servingDecision.getPlatformPolicyCompliance().getStatus());
      System.out.printf("\t\t- China policy compliance status: %s\n",
          servingDecision.getChinaPolicyCompliance().getStatus());
      System.out.printf("\t\t- Russia policy compliance status: %s\n",
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

  /**
   * Prints a {@code UserList} instance in a human-readable format.
   */
  public static void printUserList(UserList userList) {
    System.out.printf("* UserList name: '%s'\n", userList.getName());

    String displayName = userList.getDisplayName();
    if(displayName != null) {
      System.out.printf("\tUserList display name: '%s'\n", displayName);
    }

    String description = userList.getDescription();
    if(description != null) {
      System.out.printf("\tUserList description: '%s'\n", description);
    }

    UrlRestriction urlRestriction = userList.getUrlRestriction();
    if(urlRestriction != null) {
      System.out.println("\tURL Restriction:");
      System.out.printf("\t\tURL: '%s'\n", urlRestriction.getUrl());
      System.out.printf("\t\tRestriction Type: '%s'\n", urlRestriction.getRestrictionType());

      Date startDate = urlRestriction.getStartDate();
      if(startDate != null) {
        System.out.printf("\t\tStart Date: '%s'\n", convertDateToString(startDate));
      }

      Date endDate = urlRestriction.getEndDate();
      if(endDate != null) {
        System.out.printf("\t\tEnd Date: '%s'\n", convertDateToString(endDate));
      }
    }

    System.out.printf("\tUserList status: '%s'\n", userList.getStatus());
    System.out.printf("\tMembership duration days: %s\n\n",
        userList.getMembershipDurationDays());
  }
}
