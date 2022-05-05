#!/usr/bin/env ruby
# Encoding: utf-8
#
# Copyright:: Copyright 2021 Google LLC
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
# Patches an endpoint for the given bidder account and endpoint IDs.

require 'optparse'
require 'securerandom'

require_relative '../../../util'


def patch_pretargeting_configs(realtimebidding, options)
  name = "bidders/#{options[:account_id]}/endpoints/#{options[:endpoint_id]}"

  body = Google::Apis::RealtimebiddingV1::Endpoint.new(
    maximum_qps: options[:maximum_qps],
    trading_location: options[:trading_location],
    bid_protocol: options[:bid_protocol]
  )

  update_mask = 'maximumQps,tradingLocation,bidProtocol'

  puts "Patching an endpoint with name: '#{name}'"

  endpoint = realtimebidding.patch_bidder_endpoint(name, body, update_mask: update_mask)
  print_endpoint(endpoint)
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
      'The resource ID of the bidders resource under which the endpoint exists, This will be used to construct the '\
      'name used as a path parameter for the endpoints.patch request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'endpoint_id',
      'The resource ID of the endpoint to be patched. This will be used to construct the name used as a path '\
      'parameter for the endpoints.patch request.',
      type: Integer, short_alias: 'e', required: true, default_value: nil
    ),
    Option.new(
      'bid_protocol', 'The real-time bidding protocol that the endpoint is using.',
      short_alias: 'b', required: false, default_value: 'GOOGLE_RTB'
    ),
    Option.new(
      'maximum_qps',
      'The maximum number of queries per second allowed to be sent to the endpoint.',
      short_alias: 'm', required: false, default_value: '1'
    ),
    Option.new(
      'trading_location',
      'Region where the endpoint and its infrastructure is located; corresponds to the location of users that bid '\
      'requests are sent for.',
      short_alias: 't', required: false, default_value: 'US_EAST'
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    patch_pretargeting_configs(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
