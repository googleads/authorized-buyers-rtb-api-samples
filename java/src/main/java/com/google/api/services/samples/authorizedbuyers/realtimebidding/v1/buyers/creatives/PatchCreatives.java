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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** Patches a creative with the specified name. */
public class PatchCreatives {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) throws IOException {
    Integer accountId = parsedArgs.getInt("account_id");
    String creativeId = parsedArgs.getString("creative_id");
    String name = String.format("buyers/%s/creatives/%s", accountId, creativeId);

    List<String> declaredClickThroughUrls =
        Arrays.asList(
            String.format("https://test.clickurl.com/%s", UUID.randomUUID()),
            String.format("https://test.clickurl.com/%s", UUID.randomUUID()),
            String.format("https://test.clickurl.com/%s", UUID.randomUUID()));

    Creative update = new Creative();
    update.setAdvertiserName(String.format("Test-Advertiser-%s", UUID.randomUUID()));
    update.setDeclaredClickThroughUrls(declaredClickThroughUrls);

    String uMask = "advertiserName,declaredClickThroughUrls";

    Creative creative =
        client.buyers().creatives().patch(name, update).setUpdateMask(uMask).execute();

    System.out.printf("Patched creative for buyer Account ID '%s':\n", accountId);
    Utils.printCreative(creative);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("PatchCreatives")
            .build()
            .defaultHelp(true)
            .description(
                ("Patches the specified creative's advertiserName and "
                    + "declaredClickThroughUrls."));
    parser
        .addArgument("-a", "--account_id")
        .help("The resource ID of the buyers resource under which the creative is to be created.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-c", "--creative_id")
        .help(
            "The resource ID of the buyers.creatives resource for which the creative was created."
                + " This will be used to construct the name used as a path parameter for the"
                + " creatives.patch request.")
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
