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
import com.google.api.services.realtimebidding.v1.model.RemoveTargetedSitesRequest;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Removes site URLs from pretargeting configuration's web targeting.
 *
 * Note that this is the only way to remove targeted URLs following a pretargeting
 * configuration's creation.
 */
public class RemoveTargetedSites {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) {
    String pretargetingConfigName = String.format("bidders/%s/pretargetingConfigs/%s",
        parsedArgs.getInt("account_id"), parsedArgs.getInt("pretargeting_config_id"));

    RemoveTargetedSitesRequest body = new RemoveTargetedSitesRequest();
    body.setSites(parsedArgs.<String>getList("web_targeting_urls"));

    System.out.printf("Removing site URLs from web targeting for pretargeting configuration with " +
        "name: '%s'\n", pretargetingConfigName);

    PretargetingConfig pretargetingConfig = null;
    try {
      pretargetingConfig = client.bidders().pretargetingConfigs().removeTargetedSites(
          pretargetingConfigName, body).execute();
    } catch(IOException ex) {
      System.out.printf("RealTimeBidding API returned error response:\n%s", ex);
      System.exit(1);
  }
    Utils.printPretargetingConfig(pretargetingConfig);
  }

  public static void main(String[] args) {
    ArgumentParser parser = ArgumentParsers.newFor("RemoveTargetedSites").build()
        .defaultHelp(true)
        .description(("Removes site URLs from pretargeting configuration's web targeting."));
    parser.addArgument("-a", "--account_id")
        .help("The resource ID of the bidders resource under which the pretargeting " +
            "configuration was created.")
        .required(true)
        .type(Integer.class);
    parser.addArgument("-p", "--pretargeting_config_id")
        .help("The resource ID of the pretargeting configuration that is being acted upon.")
        .required(true)
        .type(Integer.class);
    parser.addArgument("--web_targeting_urls")
        .help("The URLs to be removed from this configuration's web targeting. Specify each " +
            "value separated by a space. Values specified must be valid URLs.")
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

    execute(client, parsedArgs);
  }
}