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
import com.google.api.services.realtimebidding.v1.model.ActivatePretargetingConfigRequest;
import com.google.api.services.realtimebidding.v1.model.PretargetingConfig;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** Activates a specified pretargeting configuration. */
public class ActivatePretargetingConfigs {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) throws IOException {
    String name =
        String.format(
            "bidders/%s/pretargetingConfigs/%s",
            parsedArgs.getInt("account_id"), parsedArgs.getInt("pretargeting_config_id"));

    System.out.printf("Activating pretargeting configuration with name: %s\n", name);

    PretargetingConfig pretargetingConfig =
        client
            .bidders()
            .pretargetingConfigs()
            .activate(name, new ActivatePretargetingConfigRequest())
            .execute();

    Utils.printPretargetingConfig(pretargetingConfig);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("ActivatePretargetingConfigs")
            .build()
            .defaultHelp(true)
            .description(("Activates a specified pretargeting configuration."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the bidders resource under which the pretargeting "
                + "configuration was created.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-p", "--pretargeting_config_id")
        .help("The resource ID of the pretargeting configuration that is being activated.")
        .required(true)
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
