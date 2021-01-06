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

package com.google.api.services.samples.authorizedbuyers.realtimebidding.v1.bidders.creatives;

import com.google.api.services.realtimebidding.v1.RealTimeBidding;
import com.google.api.services.realtimebidding.v1.model.WatchCreativesRequest;
import com.google.api.services.realtimebidding.v1.model.WatchCreativesResponse;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Enables monitoring of changes of a creative status for a given bidder.
 *
 * Watched creatives will have changes to their status posted to Google Cloud Pub/Sub. For more
 * details on Google Cloud Pub/Sub, see: https://cloud.google.com/pubsub/docs
 *
 * For an example of pulling creative status changes from a Google Cloud Pub/Sub subscription, see
 * PullWatchedCreativesSubscription.java.
 */
public class WatchCreatives {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) {
    Integer accountId = parsedArgs.getInt("account_id");
    String parentBidderName = String.format("bidders/%s", accountId);

    WatchCreativesResponse response = null;
    try {
      response = client.bidders().creatives().watch(parentBidderName, new WatchCreativesRequest())
          .execute();
    } catch(IOException ex) {
      System.out.printf("RealTimeBidding API returned error response:\n%s", ex);
      System.exit(1);
    }

    System.out.printf("Watching creative status changes for bidder account '%s'.':\n",
        accountId);
    System.out.printf("- Topic: %s\n", response.getTopic());
    System.out.printf("- Subscription: %s\n", response.getSubscription());
  }

  public static void main(String[] args) {
    ArgumentParser parser = ArgumentParsers.newFor("WatchCreatives").build()
        .defaultHelp(true)
        .description(("Enables watching creative status changes for the given bidder account."));
    parser.addArgument("-a", "--account_id")
        .help("The resource ID of a bidder account. This will be used to construct the parent " +
            "used as a path parameter for the creatives.watch request.")
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
