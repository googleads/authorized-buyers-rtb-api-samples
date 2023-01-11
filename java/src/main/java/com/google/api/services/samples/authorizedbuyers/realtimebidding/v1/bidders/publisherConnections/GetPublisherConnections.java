/*
 * Copyright 2022 Google LLC
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

package com.google.api.services.samples.authorizedbuyers.realtimebidding.v1.bidders.publisherConnections;

import com.google.api.services.realtimebidding.v1.RealTimeBidding;
import com.google.api.services.realtimebidding.v1.model.PublisherConnection;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * This sample illustrates how to get a single publisher connection for the given bidder.
 *
 * <p>Note: This sample will only return a populated response for bidders who are exchanges
 * participating in Open Bidding.
 */
public class GetPublisherConnections {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) throws IOException {
    Long accountId = parsedArgs.getLong("account_id");
    String publisherConnectionId = parsedArgs.getString("publisher_connection_id");
    String name =
        String.format("bidders/%d/publisherConnections/%s", accountId, publisherConnectionId);

    PublisherConnection publisherConnection =
        client.bidders().publisherConnections().get(name).execute();

    System.out.printf("Get publisher connection with name \"%s\":\n", name);
    Utils.printPublisherConnection(publisherConnection);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("GetPublisherConnections")
            .build()
            .defaultHelp(true)
            .description(
                ("Get a publisher connection for the given bidder and publisher connection IDs"));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the bidders resource under which the publisher connection exists. "
                + "This will be used to construct the name used as a path parameter for the "
                + "publisherConnections.get request.")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("-p", "--publisher_connection_id")
        .help(
            "The resource ID of the publisher connection that is being retrieved. This value is the"
                + " publisher ID found in ads.txt or app-ads.txt, and is used to construct the name"
                + " used as a path parameter for the publisherConnections.get request.")
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
