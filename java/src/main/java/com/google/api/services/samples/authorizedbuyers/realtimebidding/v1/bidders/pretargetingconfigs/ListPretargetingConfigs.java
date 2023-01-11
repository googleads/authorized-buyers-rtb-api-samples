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
import com.google.api.services.realtimebidding.v1.model.ListPretargetingConfigsResponse;
import com.google.api.services.realtimebidding.v1.model.PretargetingConfig;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * This sample illustrates how to list pretargeting configurations for a given bidder account ID.
 */
public class ListPretargetingConfigs {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) throws IOException {
    Long accountId = parsedArgs.getLong("account_id");
    Integer pageSize = parsedArgs.getInt("page_size");
    String parentBidderName = String.format("bidders/%s", accountId);
    String pageToken = null;

    System.out.printf("Found pretargeting configurations for bidder Account ID '%d':\n", accountId);

    do {
      List<PretargetingConfig> pretargetingConfigs = null;

      ListPretargetingConfigsResponse response =
          client
              .bidders()
              .pretargetingConfigs()
              .list(parentBidderName)
              .setPageSize(pageSize)
              .setPageToken(pageToken)
              .execute();

      pretargetingConfigs = response.getPretargetingConfigs();
      pageToken = response.getNextPageToken();

      if (pretargetingConfigs == null) {
        System.out.println("No pretargeting configurations found.");
      } else {
        for (PretargetingConfig pretargetingConfig : pretargetingConfigs) {
          Utils.printPretargetingConfig(pretargetingConfig);
        }
      }
    } while (pageToken != null);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("ListPretargetingConfigs")
            .build()
            .defaultHelp(true)
            .description(("Lists pretargeting configurations for the given bidder account."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the bidders resource under which the pretargeting configurations"
                + " were created. This will be used to construct the parent used as a path"
                + " parameter for the pretargetingConfigs.list request.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-p", "--page_size")
        .help(
            "The resource ID of the buyers resource under which the user lists were created. "
                + "This will be used to construct the parent used as a path parameter for the "
                + "userLists.list request.")
        .setDefault(Utils.getMaximumPageSize())
        .type(Integer.class);

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
