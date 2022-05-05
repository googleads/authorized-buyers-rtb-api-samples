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
import com.google.api.services.realtimebidding.v1.model.BatchApprovePublisherConnectionsRequest;
import com.google.api.services.realtimebidding.v1.model.BatchApprovePublisherConnectionsResponse;
import com.google.api.services.realtimebidding.v1.model.PublisherConnection;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Batch approves one or more publisher connections.
 *
 * <p>Approving a publisher connection means that the bidder agrees to receive bid requests from the
 * publisher.
 */
public class BatchApprovePublisherConnections {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) throws IOException {
    Integer bidderAccountId = parsedArgs.getInt("account_id");
    String parent = String.format("bidders/%d", bidderAccountId);

    String publisherConnectionNameTemplate = "bidders/%d/publisherConnections/%s";
    List<String> publisherConnectionIds = parsedArgs.getList("publisher_connection_ids");
    List<String> publisherConnectionNames = new ArrayList<>(publisherConnectionIds.size());

    // Populate list of publisher connection names that are to be approved.
    for (String publisherConnectionId : publisherConnectionIds) {
      publisherConnectionNames.add(
          String.format(publisherConnectionNameTemplate, bidderAccountId, publisherConnectionId));
    }

    // Set list of publisher connection names to the API request body.
    BatchApprovePublisherConnectionsRequest body = new BatchApprovePublisherConnectionsRequest();
    body.setNames(publisherConnectionNames);

    System.out.printf("Batch approving publisher connections for bidder with name: '%s'\n", parent);

    BatchApprovePublisherConnectionsResponse batchApprovePublisherConnectionsResponse =
        client.bidders().publisherConnections().batchApprove(parent, body).execute();

    for (PublisherConnection publisherConnection :
        batchApprovePublisherConnectionsResponse.getPublisherConnections()) {
      Utils.printPublisherConnection(publisherConnection);
    }
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("BatchApprovePublisherConnections")
            .build()
            .defaultHelp(true)
            .description(
                ("Batch approves one or more publisher connections to a given bidder account."));
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The resource ID of the bidders resource for which the publisher connections are "
                + "being approved.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-p", "--publisher_connection_ids")
        .help(
            "One or more resource IDs for the bidders.publisherConnections resource that are "
                + "being approved. Specify each ID separated by a space. These will be used to "
                + "construct the publisher connection names passed in the "
                + "publisherConnections.batchApprove request body.")
        .required(true)
        .nargs("+");

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
