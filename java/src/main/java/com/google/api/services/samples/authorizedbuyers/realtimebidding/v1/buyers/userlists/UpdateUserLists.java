/*
 * Copyright 2019 Google LLC
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

package com.google.api.services.samples.authorizedbuyers.realtimebidding.v1.buyers.userlists;

import com.google.api.services.realtimebidding.v1.RealTimeBidding;
import com.google.api.services.realtimebidding.v1.model.Date;
import com.google.api.services.realtimebidding.v1.model.UrlRestriction;
import com.google.api.services.realtimebidding.v1.model.UserList;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.joda.time.format.DateTimeFormatter;

/**
 * This sample illustrates how to update UserLists.
 *
 * <p>Note that a user list's status can not be adjusted with the update method.
 */
public class UpdateUserLists {
  public static void execute(RealTimeBidding client, Namespace parsedArgs) throws IOException {
    Long accountId = parsedArgs.getLong("account_id");
    String userListId = parsedArgs.getString("user_list_id");
    String userListName = String.format("buyers/%s/userLists/%s", accountId, userListId);
    DateTimeFormatter formatter = Utils.getDateTimeFormatterForLocalDate();
    String startDateStr = parsedArgs.getString("start_date");
    String endDateStr = parsedArgs.getString("end_date");
    Date startDate = null;
    Date endDate = null;

    if (startDateStr != null) {
      startDate = Utils.convertJodaLocalDateToRTBDate(formatter.parseLocalDate(startDateStr));
    }

    if (endDateStr != null) {
      endDate = Utils.convertJodaLocalDateToRTBDate(formatter.parseLocalDate(endDateStr));
    }

    UserList origUserList = client.buyers().userLists().get(userListName).execute();

    UrlRestriction origUrlRestriction = origUserList.getUrlRestriction();

    String displayName = parsedArgs.getString("display_name");
    if (displayName != null) {
      origUserList.setDisplayName(displayName);
    }

    String description = parsedArgs.getString("description");
    if (description != null) {
      origUserList.setDescription(description);
    }

    String url = parsedArgs.getString("url");
    if (url != null) {
      origUrlRestriction.setUrl(url);
    }

    String restrictionType = parsedArgs.getString("restriction_type");
    if (restrictionType != null) {
      origUrlRestriction.setRestrictionType(restrictionType);
    }

    if (startDate != null) {
      origUrlRestriction.setStartDate(startDate);
    }

    if (endDate != null) {
      origUrlRestriction.setEndDate(endDate);
    }

    Long membershipDurationDays = parsedArgs.getLong("membershipDurationDays");
    if (membershipDurationDays != null) {
      origUserList.setMembershipDurationDays(membershipDurationDays);
    }

    origUserList.setUrlRestriction(origUrlRestriction);

    UserList updatedUserList =
        client.buyers().userLists().update(userListName, origUserList).execute();

    System.out.printf("Updated UserList for buyer Account ID '%s':\n", accountId);
    Utils.printUserList(updatedUserList);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("CreateUserLists")
            .build()
            .defaultHelp(true)
            .description(("Updates the specified user list."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the user list was created. "
                + "This will be used to construct the name used as a path parameter for the "
                + "userlists.update request.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-u", "--user_list_id")
        .help(
            "The resource ID of the buyers.userlists resource for which the user list was created."
                + " This will be used to construct the name used as a path parameter for the"
                + " userlists.update request.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-n", "--display_name")
        .help("The user-specified display name of the user list.");
    parser
        .addArgument("-d", "--description")
        .help("The user-specified description of the user list.");
    parser
        .addArgument("--url")
        .help("The URL to use for applying the UrlRestriction on the user list.");
    parser
        .addArgument("-r", "--restriction_type")
        .help(
            "The restriction type for the specified URL. For more details on how to interpret the"
                + " different restriction types, see the reference documentation: "
                + "https://developers.google.com/authorized-buyers/apis/realtimebidding/reference/rest/"
                + "v1/buyers.userLists#UrlRestriction.FIELDS.restriction_type")
        .choices(
            "CONTAINS",
            "EQUALS",
            "STARTS_WITH",
            "ENDS_WITH",
            "DOES_NOT_EQUAL",
            "DOES_NOT_CONTAIN",
            "DOES_NOT_START_WITH",
            "DOES_NOT_END_WITH");
    parser
        .addArgument("--start_date")
        .help(
            "The start date for the URL restriction, specified as an ISO 8601 date "
                + "(yyyy/mm/dd. By default, this will be set to today.");
    parser
        .addArgument("--end_date")
        .help(
            "The end date for the URL restriction, specified as an ISO 8601 date "
                + "(yyyy/mm/dd. By default, this will be set to tomorrow.");
    parser
        .addArgument("-m", "--membership_duration_days")
        .help(
            "The number of days a user's cookie stays on the user list. The value must be "
                + "between 0 and 540 inclusive. This will be set to 30 by default.")
        .type(Long.class);

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
    } catch (IllegalArgumentException ex) {
      System.out.printf(
          "The specified start_date (%s) or end_date (%s) arguments could not " + "be parsed:\n%s",
          parsedArgs.getString("start_date"), parsedArgs.getString("end_date"), ex);
      System.exit(1);
    }
  }
}
