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

import static net.sourceforge.argparse4j.impl.Arguments.storeTrue;

import com.google.api.services.pubsub.Pubsub;
import com.google.api.services.pubsub.model.AcknowledgeRequest;
import com.google.api.services.pubsub.model.PubsubMessage;
import com.google.api.services.pubsub.model.PullRequest;
import com.google.api.services.pubsub.model.PullResponse;
import com.google.api.services.pubsub.model.ReceivedMessage;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Pulls creative status updates from a Google Cloud Pub/Sub subscription.
 *
 * Note that messages do not expire until they are acknowledged; set the acknowledged argument to
 * True to acknowledge receiving all messages sent in the response.
 *
 * To learn more about Google Cloud Pub/Sub, read the developer documentation:
 * https://cloud.google.com/pubsub/docs/overview
 * https://cloud.google.com/pubsub/docs/reference/rest/v1/projects.subscriptions/list
 * https://cloud.google.com/pubsub/docs/reference/rest/v1/projects.subscriptions/acknowledge
 */
public class PullWatchedCreativesSubscription {

  public static void execute(Pubsub client, Namespace parsedArgs) {
    String subscriptionName = parsedArgs.getString("subscription_name");

    System.out.printf("Retrieving messages from subscription: '%s'\n", subscriptionName);

    PullRequest pullRequest = new PullRequest();
    pullRequest.setMaxMessages(parsedArgs.getInt("max_messages"));

    PullResponse response = null;

    try {
      response = client.projects().subscriptions().pull(subscriptionName, pullRequest).execute();

    } catch(IOException ex) {
      System.out.printf("Pubsub API returned error response while pulling subscription:\n%s\n",
          ex);
      System.exit(1);
    }

    List<String> ackIds = new ArrayList<>();
    List<ReceivedMessage> receivedMessages = response.getReceivedMessages();
    if (receivedMessages.isEmpty()) {
      System.out.println("No messages received from the subscription.");
    } else {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();

      for (ReceivedMessage receivedMessage : receivedMessages) {
        ackIds.add(receivedMessage.getAckId());
        PubsubMessage message = receivedMessage.getMessage();
        Map<String, String> messageAttributes = message.getAttributes();
        String accountId = messageAttributes.get("accountId");
        String creativeId = messageAttributes.get("creativeId");

        System.out.printf("* Creative found for buyer account ID '%s' with creative ID '%s' " +
            "has been updated with the following creative status:\n", accountId, creativeId);

        String decodedData = new String(message.decodeData());
        JsonElement jsonElement = JsonParser.parseString(decodedData);
        System.out.printf("%s\n\n", gson.toJson(jsonElement));
      }

      if (parsedArgs.getBoolean("acknowledge")) {
        AcknowledgeRequest acknowledgeRequest = new AcknowledgeRequest();
        acknowledgeRequest.setAckIds(ackIds);

        System.out.printf("Acknowledging all %d messages pulled from the subscription.",
            ackIds.size());

        try {
          client.projects().subscriptions().acknowledge(subscriptionName,
              acknowledgeRequest).execute();
        } catch(IOException ex) {
          System.out.printf("Pubsub API returned error response while acknowledging received " +
              "messages:\n%s\n", ex);
          System.exit(1);
        }
      }
    }
  }

  public static void main(String[] args) {
    ArgumentParser parser = ArgumentParsers.newFor("PullWatchedCreativesSubscription")
        .build()
        .defaultHelp(true)
        .description(("Pulls creative status changes (if any) from a specified Google Cloud " +
            "Pub/Sub subscription."));
    parser.addArgument("-s", "--subscription_name")
        .help("The Google Cloud Pub/Sub subscription to be pulled. This value would be returned " +
            "in the response from the bidders.creatives.watch method, and should be provided " +
            "as-is in the form: " +
            "\"projects/realtimebidding-pubsub/subscriptions/{subscription_id}\"")
        .required(true);
    parser.addArgument("-m", "--max_messages")
        .help("The maximum number of messages to be returned in a single pull.")
        .type(Integer.class)
        .setDefault(Utils.getMaximumPageSize());
    parser.addArgument("-a", "--acknowledge")
        .help("'Whether to acknowledge the messages pulled from the subscription. Acknowledged " +
            "messages won't appear in subsequent responses to pulls from the subscription.'")
        .type(Boolean.class)
        .action(storeTrue())
        .setDefault(false);

    Namespace parsedArgs = null;
    try {
      parsedArgs = parser.parseArgs(args);
    } catch (ArgumentParserException ex) {
      parser.handleError(ex);
      System.exit(1);
    }

    Pubsub client = null;
    try {
      client = Utils.getPubsubClient();
    } catch (IOException ex) {
      System.out.printf("Unable to create Pubsub API service:\n%s", ex);
      System.out.println("Did you specify a valid path to a service account key file?");
      System.exit(1);
    } catch (GeneralSecurityException ex) {
      System.out.printf("Unable to establish secure HttpTransport:\n%s", ex);
      System.exit(1);
    }

    execute(client, parsedArgs);
  }
}
