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
# Gets a single publisher connection for the given bidder.
#
# Note: This sample will only return a populated response for bidders who are
# exchanges participating in Open Bidding.

require 'optparse'

require_relative '../../../util'


def get_publisher_connections(realtimebidding, options)
  name = "bidders/#{options[:account_id]}/publisherConnections/#{options[:publisher_connection_id]}"

  puts "Get publisher connection with name '#{name}'"

  publisher_connection = realtimebidding.get_bidder_publisher_connection(name)
  print_publisher_connection(publisher_connection)
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
      'The resource ID of the bidders resource under which the publisher connection exists. This will be used to '\
      'construct the name used as a path parameter for the publisherConnections.get request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'publisher_connection_id',
      'The resource ID of the publisher connection that is being retrieved. This value is the publisher ID found in '\
      'ads.txt or app-ads.txt, and is used to construct the name used as a path parameter for the '\
      'publisherConnections.get request.',
      type: String, short_alias: 'p', required: true, default_value: nil
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    get_publisher_connections(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
