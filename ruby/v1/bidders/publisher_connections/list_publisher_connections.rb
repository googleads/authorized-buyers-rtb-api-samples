#!/usr/bin/env ruby
# Encoding: utf-8
#
# Copyright:: Copyright 2022 Google LLC
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
# Lists a bidder's publisher connections.
#
# Note: This sample will only return a populated response for bidders who are
# exchanges participating in Open Bidding.

require 'optparse'

require_relative '../../../util'


def list_publisher_connections(realtimebidding, options)
  parent = "bidders/#{options[:account_id]}"
  filter = options[:filter]
  order_by = options[:order_by]
  page_size = options[:page_size]

  page_token = nil

  puts "Listing publisher connections for bidder with name '#{parent}'"
  begin
    response = realtimebidding.list_bidder_publisher_connections(
        parent,
        filter: filter,
        order_by: order_by,
        page_size: page_size,
        page_token: page_token,
    )

    page_token = response.next_page_token

    unless response.publisher_connections.nil?
      response.publisher_connections.each do |publisher_connection|
        print_publisher_connection(publisher_connection)
      end
    else
      puts 'No publisher connections found for buyer account'
    end
  end until page_token == nil
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
      'The resource ID of the bidders resource under which the publisher connections exist. This will be used to '\
      'construct the parent used as a path parameter for the publisherConnections.list request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'filter',
      'Query string used to filter publisher connections. To demonstrate usage, this sample will default to '\
      'filtering by publisherPlatform.',
      type: String, short_alias: 'f', required: false, default_value: 'publisherPlatform = GOOGLE_AD_MANAGER'
    ),
    Option.new(
      'order_by',
      'A string specifying the order by which results should be sorted. To demonstrate usage, this sample will '\
      'default to ordering by createTime.',
      type: String, short_alias: 'o', required: false, default_value: 'createTime DESC'
    ),
    Option.new(
      'page_size', 'The number of rows to return per page. The server may return fewer rows than specified.',
      type: Array, short_alias: 'u', required: false, default_value: MAX_PAGE_SIZE
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    list_publisher_connections(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
