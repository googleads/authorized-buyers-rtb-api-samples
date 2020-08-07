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
# This example patches a creative with the specified name.

require 'optparse'
require 'securerandom'

require_relative '../../../util'


def patch_creatives(realtimebidding, options)
  name = "buyers/#{options[:account_id]}/creatives/#{options[:creative_id]}"

  declared_click_through_urls = [
      "https://test.clickurl.com/#{SecureRandom.uuid}",
      "https://test.clickurl.com/#{SecureRandom.uuid}",
      "https://test.clickurl.com/#{SecureRandom.uuid}"
  ]

  body = Google::Apis::RealtimebiddingV1::Creative.new(
      advertiser_name: options[:advertiser_name],
      declared_click_through_urls: declared_click_through_urls,
  )

  update_mask = 'advertiserName,declaredClickThroughUrls'

  puts "Patching creative with name '#{name}'"

  creative = realtimebidding.patch_buyer_creative(
      name, creative_object=body, update_mask: update_mask)
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
      'The resource ID of the buyers resource under which the creative was created, This will be used to construct '\
      'the name used as a path parameter for the creatives.patch request.',
      type: Integer, short_alias: 'a', required: true,
      default_value: nil
    ),
    Option.new(
      'creative_id',
      'The resource ID of the buyers.creatives resource for which the creative was created. This will be used to '\
      'construct the name used as a path parameter for the creatives.patch request.',
      short_alias: 'c', required: true,
      default_value: nil
    ),
    Option.new(
      'advertiser_name', 'The name of the company being advertised in the creative.', required: false,
      default_value: "Advertiser ##{SecureRandom.uuid}"
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    patch_creatives(service, opts)
   rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
