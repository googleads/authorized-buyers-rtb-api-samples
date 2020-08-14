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

package com.google.api.services.samples.authorizedbuyers.realtimebidding.v1.bidders;

import com.google.api.services.realtimebidding.v1.RealTimeBidding;
import com.google.api.services.realtimebidding.v1.model.Creative;
import com.google.api.services.realtimebidding.v1.model.ListCreativesResponse;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * This sample illustrates how to list Creatives for a given bidder account ID.
 *
 * Note that unless filtered, this will include creatives from all buyer accounts under the bidder.
 */
public class ListCreatives {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) {
    Integer accountId = parsedArgs.getInt("account_id");
    Integer pageSize = parsedArgs.getInt("page_size");
    String parentBuyerName = String.format("bidders/%s", accountId);
    String pageToken = null;

    System.out.printf("Found Creatives for bidder Account ID '%d':\n", accountId);

    do {
      List<Creative> creatives = null;

      try {
        ListCreativesResponse response = client.bidders().creatives().list(parentBuyerName)
            .setFilter(parsedArgs.getString("filter"))
            .setView(parsedArgs.getString("view"))
            .setPageSize(pageSize)
            .setPageToken(pageToken)
            .execute();

        creatives = response.getCreatives();
        pageToken = response.getNextPageToken();
      } catch(IOException ex) {
        System.out.printf("RealTimeBidding API returned error response:\n%s", ex);
        System.exit(1);
      }
      if (creatives.isEmpty()) {
        System.out.println("No creatives found.");
      } else {
        for(Creative creative: creatives) {
          Utils.printCreative(creative);
        }
      }
    } while (pageToken != null);
  }

  public static void main(String[] args) {
    ArgumentParser parser = ArgumentParsers.newFor("ListCreatives").build()
        .defaultHelp(true)
        .description(("Lists creatives for the given bidder account."));
    parser.addArgument("-a", "--account_id")
        .help("The resource ID of the bidders resource under which the creatives were created by " +
            "buyers. This will be used to construct the parent used as a path parameter for the " +
            "creatives.list request.")
        .required(true)
        .type(Integer.class);
    parser.addArgument("-p", "--page_size")
        .help("The resource ID of the buyers resource under which the user lists were created. " +
            "This will be used to construct the parent used as a path parameter for the " +
            "userLists.list request.")
        .setDefault(Utils.getMaximumPageSize())
        .type(Integer.class);
    parser.addArgument("-f", "--filter")
        .help("Query string to filter creatives. If no filter is specified, all active creatives " +
            "will be returned. To demonstrate usage, the default behavior of this sample is to " +
            "filter such that only approved HTML snippet creatives are returned.")
        .setDefault("creativeServingDecision.openAuctionServingStatus.status=APPROVED " +
            "AND creativeFormat=HTML");
    parser.addArgument("-v", "--view")
        .help("Controls the amount of information included in the response. By default, the " +
            "creatives.list method only includes creativeServingDecision. This sample configures " +
            "the view to return the full contents of the creatives by setting this to 'FULL'.")
        .choices("FULL", "SERVING_DECISION_ONLY")
        .setDefault("FULL");

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

    execute(client, parsedArgs);
  }
}