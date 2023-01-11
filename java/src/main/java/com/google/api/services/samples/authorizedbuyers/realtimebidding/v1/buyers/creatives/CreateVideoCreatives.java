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

package com.google.api.services.samples.authorizedbuyers.realtimebidding.v1.buyers.creatives;

import com.google.api.services.realtimebidding.v1.RealTimeBidding;
import com.google.api.services.realtimebidding.v1.model.Creative;
import com.google.api.services.realtimebidding.v1.model.VideoContent;
import com.google.api.services.samples.authorizedbuyers.realtimebidding.Utils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.UUID;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** Creates a creative with video content for the given buyer account ID. */
public class CreateVideoCreatives {

  public static void execute(RealTimeBidding client, Namespace parsedArgs) throws IOException {
    Long accountId = parsedArgs.getLong("account_id");

    String parentBuyerName = String.format("buyers/%s", accountId);

    VideoContent videoContent = new VideoContent();
    videoContent.setVideoUrl(parsedArgs.getString("video_url"));

    Creative newCreative = new Creative();
    newCreative.setAdvertiserName(parsedArgs.getString("advertiser_name"));
    newCreative.setCreativeId(parsedArgs.getString("creative_id"));
    newCreative.setDeclaredAttributes(parsedArgs.<String>getList("declared_attributes"));
    newCreative.setDeclaredClickThroughUrls(parsedArgs.<String>getList("declared_click_urls"));
    newCreative.setDeclaredRestrictedCategories(
        parsedArgs.<String>getList("declared_restricted_categories"));
    newCreative.setDeclaredVendorIds(parsedArgs.<Integer>getList("declared_vendor_ids"));
    newCreative.setVideo(videoContent);

    Creative creative = client.buyers().creatives().create(parentBuyerName, newCreative).execute();

    System.out.printf("Created creative for buyer Account ID '%s':\n", accountId);
    Utils.printCreative(creative);
  }

  public static void main(String[] args) {
    ArgumentParser parser =
        ArgumentParsers.newFor("CreateVideoCreatives")
            .build()
            .defaultHelp(true)
            .description(("Creates a video creative for the given buyer account ID."));
    parser
        .addArgument("-a", "--account_id")
        .help("The resource ID of the buyers resource under which the creative is to be created. ")
        .required(true)
        .type(Long.class);
    parser
        .addArgument("--advertiser_name")
        .help("The name of the company being advertised in the creative.")
        .setDefault("Test");
    parser
        .addArgument("-c", "--creative_id")
        .help("The user-specified creative ID. The maximum length of the creative ID is 128 bytes.")
        .setDefault(String.format("Video_Creative_%s", UUID.randomUUID()));
    parser
        .addArgument("--declared_attributes")
        .help(
            "The creative attributes being declared. Specify each attribute separated by a "
                + "space.")
        .nargs("*")
        .setDefault(Collections.singletonList("CREATIVE_TYPE_VAST_VIDEO"));
    parser
        .addArgument("--declared_click_urls")
        .help("The click-through URLs being declared. Specify each URL separated by a space.")
        .nargs("*")
        .setDefault(Collections.singletonList("http://test.com"));
    parser
        .addArgument("--declared_restricted_categories")
        .help(
            "The restricted categories being declared. Specify each category separated by a "
                + "space.")
        .nargs("*");
    parser
        .addArgument("--declared_vendor_ids")
        .help("The vendor IDs being declared. Specify each ID separated by a space.")
        .type(Integer.class)
        .nargs("*");
    parser
        .addArgument("--video_url")
        .help("The URL to fetch a video ad.")
        .setDefault("https://video.test.com/ads?id=123456&wprice=%%WINNING_PRICE%%");

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
