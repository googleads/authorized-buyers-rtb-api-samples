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

package com.google.api.services.samples.authorizedbuyers.realtimebidding.v1.bidders;

import com.google.api.services.realtimebidding.v1.RealTimeBidding;
import com.google.api.services.realtimebidding.v1.model.Bidder;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * This sample illustrates how to get a single bidder for the specified bidder name.
 *
 * The bidder specified must be associated with the authorized service account specified in Utils.java.
 */
public class GetBidders {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) {
    Integer accountId = parsedArgs.getInt("account_id");
    String name = String.format("bidders/%s", accountId);

    Bidder bidder = null;

    try {
      bidder = client.bidders().get(name).execute();
    } catch(IOException ex) {
      System.out.printf("RealTimeBidding API returned error response:\n%s", ex);
      System.exit(1);
    }

    System.out.printf("Get bidder with ID '%s'.\n", accountId);
    Utils.printBidder(bidder);
  }

  public static void main(String[] args) {
    ArgumentParser parser = ArgumentParsers.newFor("GetBidders").build()
        .defaultHelp(true)
        .description(("Get a bidder for the given account ID."));
    parser.addArgument("-a", "--account_id")
        .help("The resource ID of the bidders resource that is being retrieved. This will be " +
            "used to construct the name used as a path parameter for the bidders.get request.")
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

    execute(client, parsedArgs);
  }
}