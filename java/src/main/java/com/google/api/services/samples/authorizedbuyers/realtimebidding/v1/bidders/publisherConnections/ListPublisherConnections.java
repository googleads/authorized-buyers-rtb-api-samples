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
import com.google.api.services.realtimebidding.v1.model.ListPublisherConnectionsResponse;
import com.google.api.services.realtimebidding.v1.model.PublisherConnection;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * This sample illustrates how to list a given bidder's publisher connections.
 *
 * <p>Note: This sample will only return a populated response for bidders who are exchanges
 * participating in Open Bidding.
 */
public class ListPublisherConnections {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) throws IOException {
    String parent = String.format("bidders/%d", parsedArgs.getInt("account_id"));
    Integer pageSize = parsedArgs.getInt("page_size");
    String pageToken = null;

    System.out.printf("Listing publisher connections for bidder with name \"%s\":%n", parent);

    do {
      List<PublisherConnection> publisherConnections = null;

      ListPublisherConnectionsResponse response = client
          .bidders()
          .publisherConnections()
          .list(parent)
          .setPageSize(pageSize)
          .setPageToken(pageToken)
          .execute();

      publisherConnections = response.getPublisherConnections();
      pageToken = response.getNextPageToken();

      if (publisherConnections == null) {
        System.out.println("No publisher connections found.");
      } else {
        for (PublisherConnection publisherConnection : publisherConnections) {
          Utils.printPublisherConnection(publisherConnection);
        }
      }
    } while (pageToken != null);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("ListPublisherConnections")
            .build()
            .defaultHelp(true)
            .description("Lists publisher connections associated with the given bidder account.");
    parser
        .addArgument("-a", "--account_id")
        .help(
            "The number of rows to return per page. The server may return fewer rows than "
                + "specified.")
        .required(true)
        .type(Integer.class);
    parser
        .addArgument("-f", "--filter")
        .help(
            "Query string to filter publisher connections. To demonstrate usage, this sample "
                + "will default to filtering by publisherPlatform.")
        .setDefault("publisherPlatform = GOOGLE_AD_MANAGER");
    parser
        .addArgument("-o", "--order_by")
        .help(
            "A string specifying the order by which results should be sorted. To demonstrate "
                + "usage, this sample will default to ordering by createTime.")
        .setDefault("createTime DESC");
    parser
        .addArgument("-p", "--page_size")
        .help(
            "The number of rows to return per page. The server may return fewer rows than "
                + "specified.")
        .setDefault(Utils.getMaximumPageSize())
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
