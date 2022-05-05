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
import com.google.api.services.realtimebidding.v1.model.PretargetingConfig;
import com.google.api.services.realtimebidding.v1.model.RemoveTargetedPublishersRequest;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Removes publisher IDs from a pretargeting configuration's publisher targeting.
 *
 * <p>Note that this is the only way to remove publisher IDs following a pretargeting
 * configuration's creation.
 */
public class RemoveTargetedPublishers {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) throws IOException {
    String pretargetingConfigName =
        String.format(
            "bidders/%s/pretargetingConfigs/%s",
            parsedArgs.getInt("account_id"), parsedArgs.getInt("pretargeting_config_id"));

    RemoveTargetedPublishersRequest body = new RemoveTargetedPublishersRequest();
    body.setPublisherIds(parsedArgs.<String>getList("publisher_ids"));

    System.out.printf(
        "Removing publisher IDs from publisher targeting for pretargeting "
            + "configuration with name: '%s'\n",
        pretargetingConfigName);

    PretargetingConfig pretargetingConfig =
        client
            .bidders()
            .pretargetingConfigs()
            .removeTargetedPublishers(pretargetingConfigName, body)
            .execute();

    Utils.printPretargetingConfig(pretargetingConfig);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("RemoveTargetedPublishers")
            .build()
            .defaultHelp(true)
            .description(
                ("Removes publisher IDs from a pretargeting configuration's publisher "
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
        .addArgument("--publisher_ids")
        .help(
            "The publisher IDs to be removed from this configuration's publisher targeting. Specify"
                + " each ID separated by  space. Valid publisher IDs can be found in Real-time"
                + " Bidding bid requests, or alternatively in ads.txt / app-ads.txt. For more"
                + " information, see: https://iabtechlab.com/ads-txt/")
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
