#!/usr/bin/env ruby
# Encoding: utf-8
#
# Copyright:: Copyright 2020 Google LLC
#
# License:: Licensed under the Apache License, Version 2.0 (the "License");
#           you may not use this file except in compliance with the License.
#           You may obtain a copy of the License at
#
#           http://www.apache.org/licenses/LICENSE-2.0
#
#           Unless required by applicable law or agreed to in writing, software
#           distributed under the License is distributed on an "AS IS" BASIS,
#           WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
#           implied.
#           See the License for the specific language governing permissions and
#           limitations under the License.
#
# Creates a creative with native content for the given buyer account ID.

require 'optparse'
require 'securerandom'

require_relative '../../../util'


def create_native_creatives(realtimebidding, options)
  parent = "buyers/#{options[:account_id]}"

  body = Google::Apis::RealtimebiddingV1::Creative.new(
      advertiser_name: options[:advertiser_name],
      creative_id: options[:creative_id],
      declared_attributes: options[:declared_attributes],
      declared_click_through_urls: options[:declared_click_urls],
      declared_resricted_categories: options[:declared_restricted_categories],
      declared_vendor_ids: options[:declared_vendor_ids],
      native: Google::Apis::RealtimebiddingV1::NativeContent.new(
          headline: options[:native_headline],
          body: options[:native_body],
          call_to_action: options[:native_call_to_action],
          advertiser_name: options[:native_advertiser_name],
          image: Google::Apis::RealtimebiddingV1::Image.new(
              url: options[:native_image_url],
              height: options[:native_image_height],
              width: options[:native_image_width]
          ),
          logo: Google::Apis::RealtimebiddingV1::Image.new(
              url: options[:native_logo_url],
              height: options[:native_logo_height],
              width: options[:native_logo_width]
          ),
          click_link_url: options[:native_click_link_url],
          click_tracking_url: options[:native_click_tracking_url]
      )
  )

  puts "Creating native creative for buyer account '#{parent}'"

  creative = realtimebidding.create_buyer_creative(parent, creative_object=body)
  print_creative(creative)
end


if __FILE__ == $0
  begin
    # Retrieve the service used to make API requests.
    service = get_service()
  rescue ArgumentError => e
    raise 'Unable to create service, with error message: #{e.message}'
  rescue Signet::AuthorizationError => e
    raise 'Unable to create service, was the KEY_FILE in util.rb set? Error message: #{e.message}'
  end

  # Set options and default values for fields used in this example.
  options = [
    Option.new(
      'account_id',
      'The resource ID of the buyers resource under which the creatives were created, This will be used to construct '\
      'the name used as a path parameter for the creatives.get request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'creative_id',
      'The user-specified creative ID. The maximum length of the creative ID is 128 bytes.',
      short_alias: 'c', required: false, default_value: "Native_Creative_#{SecureRandom.uuid}"
    ),
    Option.new(
      'advertiser_name', 'The name of the company being advertised in the creative.', required: false,
      default_value: 'Test'
    ),
    Option.new(
      'declared_attributes', 'The creative attributes being declared. Specify each attribute separated by a comma.',
      required: false, type: Array, default_value: ['NATIVE_ELIGIBILITY_ELIGIBLE']
    ),
    Option.new(
      'declared_click_urls', 'The click-through URLs being declared. Specify each URL separated by a comma.',
      required: false, type: Array, default_value: ['http://test.com']
    ),
    Option.new(
      'declared_restricted_categories',
      'The restricted categories being declared. Specify each category separated by a comma.',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'declared_vendor_ids', 'The vendor IDs being declared. Specify each ID separated by a space.',
      required: false, type: Array, default_value: []
    ),
    Option.new('native_headline', 'A short title for the ad.', required: false, default_value: 'Luxury Mars Cruises'),
    Option.new(
      'native_body', 'A long description of the ad.', required: false,
      default_value: 'Visit the planet in a luxury spaceship.'
    ),
    Option.new(
      'native_call_to_action', 'A label for the button that the user is supposed to click.', required: false,
      default_value: 'Book today'
    ),
    Option.new(
      'native_advertiser_name', 'The name of the advertiser or sponsor, to be displayed in the ad creative',
      required: false, default_value: 'Galactic Luxury Cruises'
    ),
    Option.new(
      'native_image_url', 'The URL of the large image to be included in the native ad.', required: false,
      default_value: 'https://native.test.com/image?id=123456'
    ),
    Option.new(
      'native_image_height', 'The height in pixels of the native ad\'s large image.', required: false,
      default_value: 627
    ),
    Option.new(
      'native_image_width', 'The width in pixels of the native ad\'s large image.', required: false,
      default_value: 1200
    ),
    Option.new(
      'native_logo_url', 'The URL of a smaller image to be included in the native ad.', required: false,
      default_value: 'https://native.test.com/logo?id=123456'
    ),
    Option.new(
      'native_logo_height', 'The height in pixels of the native ad\'s smaller image.', required: false,
      default_value: 100
    ),
    Option.new(
      'native_logo_width', 'The width in pixels of the native ad\'s smaller image.', required: false,
      default_value: 100
    ),
    Option.new(
      'native_click_link_url', 'The URL that the browser/SDK will load when the user clicks the ad.', required: false,
      default_value: 'https://www.google.com'
    ),
    Option.new(
      'native_click_tracking_url', 'The URL to use for click tracking.', required: false,
      default_value: 'https://native.test.com/click?id=123456'
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    create_native_creatives(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
