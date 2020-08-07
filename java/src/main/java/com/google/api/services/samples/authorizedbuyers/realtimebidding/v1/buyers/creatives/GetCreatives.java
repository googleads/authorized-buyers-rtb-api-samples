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

package com.google.api.services.samples.authorizedbuyers.realtimebidding.v1.buyers.creatives;

import com.google.api.services.realtimebidding.v1.RealTimeBidding;
import com.google.api.services.realtimebidding.v1.model.Creative;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * This sample illustrates how to get a single creative for the given buyer account ID and creative
 * ID.
 */
public class GetCreatives {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) {
    Integer accountId = parsedArgs.getInt("account_id");
    String creativeId = parsedArgs.getString("creative_id");
    String name = String.format("buyers/%s/creatives/%s", accountId, creativeId);

    Creative creative = null;

    try {
      creative = client.buyers().creatives().get(name)
          .setView(parsedArgs.getString("view"))
          .execute();
    } catch(IOException ex) {
      System.out.printf("RealTimeBidding API returned error response:\n%s", ex);
      System.exit(1);
    }

    System.out.printf("Found Creative with ID '%s' for buyer account ID '%d':\n", creativeId,
        accountId);
    Utils.printCreative(creative);
  }

  public static void main(String[] args) {
    ArgumentParser parser = ArgumentParsers.newFor("GetCreatives").build()
        .defaultHelp(true)
        .description(("Get a creative for the given buyer account ID and creative ID."));
    parser.addArgument("-a", "--account_id")
        .help("The resource ID of the buyers resource under which the creatives were created. " +
            "This will be used to construct the parent used as a path parameter for the " +
            "creatives.list request.")
        .required(true)
        .type(Integer.class);
    parser.addArgument("-c", "--creative_id")
        .help("The resource ID of the buyers.creatives resource for which the creative was " +
            "created. This will be used to construct the name used as a path parameter for the " +
            "creatives.get request.")
        .required(true);
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