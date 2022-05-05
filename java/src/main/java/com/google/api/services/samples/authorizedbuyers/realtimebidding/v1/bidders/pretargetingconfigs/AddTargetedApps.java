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
import com.google.api.services.realtimebidding.v1.model.AddTargetedAppsRequest;
import com.google.api.services.realtimebidding.v1.model.PretargetingConfig;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Adds mobile application IDs to a pretargeting configuration's app targeting.
 *
 * <p>Note that this is the only way to append mobile application IDs following a pretargeting
 * configuration's creation. If a pretargeting configuration already targets mobile application IDs,
 * you must specify a targeting mode that is identical to the existing targeting mode.
 */
public class AddTargetedApps {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) throws IOException {
    String pretargetingConfigName =
        String.format(
            "bidders/%s/pretargetingConfigs/%s",
            parsedArgs.getInt("account_id"), parsedArgs.getInt("pretargeting_config_id"));

    AddTargetedAppsRequest body = new AddTargetedAppsRequest();
    body.setTargetingMode(parsedArgs.getString("mobile_app_targeting_mode"));
    body.setAppIds(parsedArgs.<String>getList("mobile_app_targeting_app_ids"));

    System.out.printf(
        "Updating mobile app targeting with new app IDs for pretargeting "
            + "configuration with name: '%s'\n",
        pretargetingConfigName);

    PretargetingConfig pretargetingConfig =
        client
            .bidders()
            .pretargetingConfigs()
            .addTargetedApps(pretargetingConfigName, body)
            .execute();

    Utils.printPretargetingConfig(pretargetingConfig);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("AddTargetedApps")
            .build()
            .defaultHelp(true)
            .description(
                ("Adds mobile application IDs to a pretargeting configuration's app "
                    + "targeting."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the bidders resource under which the pretargeting "
                + "configuration was created.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-p", "--pretargeting_config_id")
        .help("The resource ID of the pretargeting configuration that is being acted upon.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("--mobile_app_targeting_mode")
        .help(
            "The targeting mode for the configuration's mobile app targeting. Valid values include:"
                + " INCLUSIVE, and EXCLUSIVE. Note that if the configuration already targets mobile"
                + " app Ids, you must specify an identical targeting mode.")
        .required(true)
        .type(String.class);
    parser
        .addArgument("--mobile_app_targeting_app_ids")
        .help(
            "The mobile app IDs specified for this configuration's mobile app targeting, which"
                + " allows one to target a subset of mobile app inventory. Specify each value"
                + " separated by a space. Values specified must be valid mobile App IDs, as found"
                + " on their respective app stores.")
        .required(true)
        .type(String.class)
        .nargs("*");

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
