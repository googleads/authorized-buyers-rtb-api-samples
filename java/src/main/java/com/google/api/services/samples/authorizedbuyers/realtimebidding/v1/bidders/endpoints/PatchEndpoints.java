/*
 * Copyright 2021 Google LLC
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

package com.google.api.services.samples.authorizedbuyers.realtimebidding.v1.bidders.endpoints;

import com.google.api.services.realtimebidding.v1.RealTimeBidding;
import com.google.api.services.realtimebidding.v1.model.Endpoint;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** Patches an endpoint with a specified name. */
public class PatchEndpoints {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) throws IOException {
    Long accountId = parsedArgs.getLong("account_id");

    String name =
        String.format("bidders/%s/endpoints/%s", accountId, parsedArgs.getLong("endpoint_id"));
    String updateMask = "maximumQps,tradingLocation,bidProtocol";

    Endpoint body = new Endpoint();
    body.setBidProtocol(parsedArgs.getString("bid_protocol"));
    body.setMaximumQps(parsedArgs.getLong("maximum_qps"));
    body.setTradingLocation(parsedArgs.getString("trading_location"));

    Endpoint endpoint =
        client.bidders().endpoints().patch(name, body).setUpdateMask(updateMask).execute();

    System.out.printf("Patched endpoint with name '%s':\n", name);
    Utils.printEndpoint(endpoint);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("PatchEndpoints")
            .build()
            .defaultHelp(true)
            .description(("Patches a specified endpoint."));
    parser
        .addArgument("-a", "--account_id")
        .help("The resource ID of the bidders resource under which the endpoint exists.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-e", "--endpoint_id")
        .help("The resource ID of the endpoint to be patched.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-b", "--bid_protocol")
        .help("The real-time bidding protocol that the endpoint is using.")
        .setDefault("GOOGLE_RTB");
    parser
        .addArgument("-m", "--maximum_qps")
        .help("The maximum number of queries per second allowed to be sent to the endpoint.")
        .type(Long.class)
        .setDefault(1L);
    parser
        .addArgument("-t", "--trading_location")
        .help(
            "Region where the endpoint and its infrastructure is located; corresponds to the "
                + "location of users that bid requests are sent for.")
        .setDefault("US_EAST");

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
