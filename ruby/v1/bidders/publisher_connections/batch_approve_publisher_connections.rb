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
# Batch approves one or more publisher connections.
#
# Approving a publisher connection means that the bidder agrees to receive bid
# requests from the publisher.

require 'optparse'

require_relative '../../../util'


def batch_approve_publisher_connections(realtimebidding, options)
  account_id = options[:account_id]
  parent = "bidders/#{account_id}"
  publisher_connection_names = options[:publisher_connection_ids].map{
      |publisher_connection_id| "bidders/#{account_id}/publisherConnections/#{publisher_connection_id}"}

  puts "Batch approving publisher connections for bidder with name: '#{parent}'"

  body = Google::Apis::RealtimebiddingV1::BatchApprovePublisherConnectionsRequest.new(
    names: publisher_connection_names
  )

  response = realtimebidding.batch_approve_publisher_connections(parent, body)

  unless response.publisher_connections.nil?
    response.publisher_connections.each do |publisher_connection|
      print_publisher_connection(publisher_connection)
    end
  end
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
      'construct the parent used as a path parameter for the publisherConnections.batchApprove request, as well as '\
      'the publisher connection names passed in the request body.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'publisher_connection_ids',
      'One or more resource IDs for the bidders.publisherConnections resource that are being approved. Specify each '\
      'value separated by a comma. These will be used to construct the publisher connection names passed in the '\
      'publisherConnections.batchApprove request body.',
      type: Array, short_alias: 'p', required: true, default_value: nil
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    batch_approve_publisher_connections(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
