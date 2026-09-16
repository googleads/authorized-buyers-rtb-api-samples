/*
 * Copyright 2026 Google LLC
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

package com.google.api.services.samples.authorizedbuyers.realtimebidding.v1.buyers.creatives;

import com.google.api.services.realtimebidding.v1.RealTimeBidding;
import com.google.api.services.realtimebidding.v1.model.AddDealsRequest;
import com.google.api.services.realtimebidding.v1.model.Creative;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** Adds deals to a creative. */
public class AddDeals {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) throws IOException {
    Long accountId = parsedArgs.getLong("account_id");
    String creativeId = parsedArgs.getString("creative_id");
    String name = String.format("buyers/%s/creatives/%s", accountId, creativeId);
    List<String> deals = parsedArgs.<String>getList("deal_ids");

    AddDealsRequest request = new AddDealsRequest().setDealIds(deals);

    Creative creative = client.buyers().creatives().addDeals(name, request).execute();

    System.out.println("Added deals to the following creative:");
    Utils.printCreative(creative);
    System.out.println("Deal IDs added:");
    for (String deal : deals) {
      System.out.printf("\t- Deal: %s\n", deal);
    }
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("AddDeals")
            .build()
            .defaultHelp(true)
            .description(("Adds deals to a creative."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the buyers resource under which the creative was created. "
                + "This will be used to construct the parent used as a path parameter for the "
                + "creatives.addDeals request.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-c", "--creative_id")
        .help(
            "The resource ID of the buyers.creatives resource for which the creative was created."
                + " This will be used to construct the name used as a path parameter for the"
                + " creatives.addDeals request.")
        .required(true);
    parser
        .addArgument("-d", "--deal_ids")
        .help(
            "The deal IDs of the deals, auction packages, or marketplace packages to add to the"
                + " creative. Specified as a space-separated list of deal IDs.")
        .nargs("*")
        .required(true);

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
