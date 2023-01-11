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
import com.google.api.services.realtimebidding.v1.model.CreativeDimensions;
import com.google.api.services.realtimebidding.v1.model.NumericTargetingDimension;
import com.google.api.services.realtimebidding.v1.model.PretargetingConfig;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.UUID;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** Patches a pretargeting configuration with a specified name. */
public class PatchPretargetingConfigs {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) throws IOException {
    Long accountId = parsedArgs.getLong("account_id");

    String name =
        String.format(
            "bidders/%s/pretargetingConfigs/%s",
            accountId, parsedArgs.getLong("pretargeting_config_id"));
    String updateMask =
        "displayName,includedFormats,geoTargeting.includedIds," + "includedCreativeDimensions";

    NumericTargetingDimension geoTargeting = new NumericTargetingDimension();
    geoTargeting.setIncludedIds(
        Arrays.asList(
            200635L, // Austin, TX
            1014448L, // Boulder, CO
            1022183L, // Hoboken, NJ
            200622L, // New Orleans, LA
            1023191L, // New York, NY
            9061237L, // Mountain View, CA
            1014221L // San Francisco, CA
            ));

    CreativeDimensions creativeDimensions1 = new CreativeDimensions();
    creativeDimensions1.setHeight(480L);
    creativeDimensions1.setWidth(320L);

    CreativeDimensions creativeDimensions2 = new CreativeDimensions();
    creativeDimensions2.setHeight(1080L);
    creativeDimensions2.setWidth(1920L);

    PretargetingConfig body = new PretargetingConfig();
    body.setDisplayName(parsedArgs.getString("display_name"));
    // Note that repeated fields such as this are completely overwritten by the contents included in
    // the patch request.
    body.setIncludedFormats(Arrays.asList("HTML", "VAST"));
    body.setGeoTargeting(geoTargeting);
    body.setIncludedCreativeDimensions(Arrays.asList(creativeDimensions1, creativeDimensions2));

    PretargetingConfig pretargetingConfig =
        client
            .bidders()
            .pretargetingConfigs()
            .patch(name, body)
            .setUpdateMask(updateMask)
            .execute();

    System.out.printf("Patched pretargeting configuration with name '%s':\n", name);
    Utils.printPretargetingConfig(pretargetingConfig);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("PatchPretargetingConfigs")
            .build()
            .defaultHelp(true)
            .description(("Patches a specified pretargeting configuration."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the bidders resource under which the pretargeting "
                + "configuration was created.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-p", "--pretargeting_config_id")
        .help("The resource ID of the pretargeting configuration to be patched.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-d", "--display_name")
        .help(
            "The display name to associate with the new configuration. Must be unique among "
                + "all of a bidder's pretargeting configurations.")
        .setDefault(String.format("TEST_PRETARGETING_CONFIG_%s", UUID.randomUUID()));

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
