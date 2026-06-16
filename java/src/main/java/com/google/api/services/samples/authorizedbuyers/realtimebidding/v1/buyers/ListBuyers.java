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

package com.google.api.services.samples.authorizedbuyers.realtimebidding.v1.buyers;

import com.google.api.services.realtimebidding.v1.RealTimeBidding;
import com.google.api.services.realtimebidding.v1.model.Buyer;
import com.google.api.services.realtimebidding.v1.model.ListBuyersResponse;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * This sample illustrates how to list buyers associated with the authorized service account.
 */
public class ListBuyers {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) {
    Integer pageSize = parsedArgs.getInt("page_size");
    String pageToken = null;

    System.out.println("Listing buyers associated with OAuth 2.0 credentials.");

    do {
      List<Buyer> buyers = null;

      try {
        ListBuyersResponse response = client.buyers().list()
            .setPageSize(pageSize)
            .setPageToken(pageToken)
            .execute();

        buyers = response.getBuyers();
        pageToken = response.getNextPageToken();
      } catch(IOException ex) {
        System.out.printf("RealTimeBidding API returned error response:\n%s", ex);
        System.exit(1);
      }
      if (buyers == null) {
        System.out.println("No buyers found.");
      } else {
        for(Buyer buyer: buyers) {
          Utils.printBuyer(buyer);
        }
      }
    } while (pageToken != null);
  }

  public static void main(String[] args) {
    ArgumentParser parser = ArgumentParsers.newFor("ListCreatives").build()
        .defaultHelp(true)
        .description(("Lists buyers associated with the service account specified for the OAuth " +
            "2.0 flow in Utils.java."));
    parser.addArgument("-p", "--page_size")
        .help("The number of rows to return per page. The server may return fewer rows than " +
            "specified.")
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

    execute(client, parsedArgs);
  }
}
