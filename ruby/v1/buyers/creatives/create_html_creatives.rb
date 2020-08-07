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
# Creates a creative with HTML content for the given buyer account ID.

require 'optparse'
require 'securerandom'

require_relative '../../../util'


def create_html_creatives(realtimebidding, options)  
  parent = "buyers/#{options[:account_id]}"

  body = Google::Apis::RealtimebiddingV1::Creative.new(
    advertiser_name: options[:advertiser_name],
    creative_id: options[:creative_id],
    declared_attributes: options[:declared_attributes],
    declared_click_through_urls: options[:declared_click_urls],
    declared_resricted_categories: options[:declared_restricted_categories],
    declared_vendor_ids: options[:declared_vendor_ids],
    html: Google::Apis::RealtimebiddingV1::HtmlContent.new(
      snippet: options[:html_snippet],
      height: options[:html_height],
      width: options[:html_width]
    )
  )

  puts "Creating HTML creative for buyer account '#{parent}'"

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

  default_snippet_content = <<~SNIPPET
    <iframe marginwidth=0 marginheight=0 height=600 frameborder=0 width=160 scrolling=no
        src="https://test.com/ads?id=123456&curl=%%CLICK_URL_ESC%%&wprice=%%WINNING_PRICE_ESC%%">
    </iframe>
  SNIPPET

  # Set options and default values for fields used in this example.
  options = [
    Option.new(
      'account_id',
      'The resource ID of the buyers resource under which the creatives were created, This will be used to construct '\
      'the name used as a path parameter for the creatives.get request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'creative_id', 'The user-specified creative ID. The maximum length of the creative ID is 128 bytes.',
      short_alias: 'c', required: false, default_value: "HTML_Creative_#{SecureRandom.uuid}"
    ),
    Option.new(
      'advertiser_name', 'The name of the company being advertised in the creative.', required: false,
      default_value: 'Test'
    ),
    Option.new(
      'declared_attributes', 'The creative attributes being declared. Specify each attribute separated by a comma.',
      required: false, type: Array, default_value: ['CREATIVE_TYPE_HTML']
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
    Option.new(
      'html_snippet', 'The HTML snippet that displays the ad when inserted in the web page.', required: false,
      default_value: default_snippet_content
    ),
    Option.new('html_height', 'The height of the HTML snippet in pixels.', required: false, default_value: 250),
    Option.new('html_width', 'The width of the HTML snippet in pixels.', required: false, default_value: 300),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    create_html_creatives(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
