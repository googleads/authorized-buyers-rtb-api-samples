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
import java.util.UUID;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

/**
 * This sample illustrates how to create UserLists.
 *
 * <p>Note that newly created user lists have their status set to OPEN by default.
 */
public class CreateUserLists {

  public static void execute(RealTimeBidding client, Namespace parsedArgs)
      throws IllegalArgumentException, IOException {
    Integer accountId = parsedArgs.getInt("account_id");
    DateTimeFormatter formatter = Utils.getDateTimeFormatterForLocalDate();
    String startDateStr = parsedArgs.getString("start_date");
    String endDateStr = parsedArgs.getString("end_date");
    Date startDate = null;
    Date endDate = null;

    startDate = Utils.convertJodaLocalDateToRTBDate(formatter.parseLocalDate(startDateStr));
    endDate = Utils.convertJodaLocalDateToRTBDate(formatter.parseLocalDate(endDateStr));

    String parentBuyerName = String.format("buyers/%s", accountId);

    UrlRestriction newUrlRestriction = new UrlRestriction();
    newUrlRestriction.setUrl(parsedArgs.getString("url"));
    newUrlRestriction.setRestrictionType(parsedArgs.getString("restriction_type"));
    newUrlRestriction.setStartDate(startDate);
    newUrlRestriction.setEndDate(endDate);

    UserList newUserList = new UserList();
    newUserList.setDisplayName(parsedArgs.getString("display_name"));
    newUserList.setDescription(parsedArgs.getString("description"));
    newUserList.setUrlRestriction(newUrlRestriction);
    newUserList.setMembershipDurationDays(parsedArgs.getLong("membership_duration_days"));

    UserList userList = client.buyers().userLists().create(parentBuyerName, newUserList).execute();

    System.out.printf("Created UserList for buyer Account ID '%s':\n", accountId);
    Utils.printUserList(userList);
  }

  public static void main(String[] args) {
    DateTimeFormatter formatter = Utils.getDateTimeFormatterForLocalDate();
    LocalDate defaultStartDate = new LocalDate();
    LocalDate defaultEndDate = defaultStartDate.plusDays(1);

    ArgumentParser parser =
        ArgumentParsers.newFor("CreateUserLists")
            .build()
            .defaultHelp(true)
            .description(("Creates a user list for the given buyer account ID."));
    parser
        .addArgument("-a", "--account_id")
        .help("The resource ID of the buyers resource under which the user list is to be created. ")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-n", "--display_name")
        .help("The user-specified display name of the user list.")
        .setDefault(String.format("Test_UserList_%s", UUID.randomUUID()));
    parser
        .addArgument("-d", "--description")
        .help("The user-specified description of the user list.");
    parser
        .addArgument("--url")
        .help("The URL to use for applying the UrlRestriction on the user list.")
        .setDefault("https://luxurymarscruises.com");
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
            "DOES_NOT_END_WITH")
        .setDefault("EQUALS");
    parser
        .addArgument("--start_date")
        .help(
            "The start date for the URL restriction, specified as an ISO 8601 date "
                + "(yyyy/mm/dd. By default, this will be set to today.")
        .setDefault(defaultStartDate.toString(formatter));
    parser
        .addArgument("--end_date")
        .help(
            "The end date for the URL restriction, specified as an ISO 8601 date "
                + "(yyyy/mm/dd. By default, this will be set to tomorrow.")
        .setDefault(defaultEndDate.toString(formatter));
    parser
        .addArgument("-m", "--membership_duration_days")
        .help(
            "The number of days a user's cookie stays on the user list. The value must be "
                + "between 0 and 540 inclusive. This will be set to 30 by default.")
        .type(Long.class)
        .setDefault(30);

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
