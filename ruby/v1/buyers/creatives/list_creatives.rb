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
# Retrieves creatives for a given buyer account ID.

require 'optparse'

require_relative '../../../util'


def list_creatives(realtimebidding, options)
  parent = "buyers/#{options[:account_id]}"
  filter = options[:filter]
  view = options[:view]
  page_size = options[:page_size]

  page_token = nil

  puts "Listing creatives for buyer account '#{parent}'"
  begin
    response = realtimebidding.list_buyer_creatives(
        parent, filter: filter, view: view, page_size: page_size,
        page_token: page_token
    )

    page_token = response.next_page_token

    unless response.creatives.nil?
      response.creatives.each do |creative|
        print_creative(creative)
      end
    else
      puts 'No creatives found for buyer account'
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

  default_list_filter = 'creativeServingDecision.openAuctionServingStatus.status=APPROVED AND creativeFormat=HTML'

  # Set options and default values for fields used in this example.
  options = [
    Option.new(
      'account_id',
      'The resource ID of the buyers resource under which the creatives were created, This will be used to construct'\
      'the parent used as a path parameter for the creatives.list request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'filter',
      'Query string to filter creatives. If no filter is specified, all active creatives will be returned. To '\
      'demonstrate usage, the default behavior of this sample is to filter such that only approved HTML snippet '\
      'creatives are returned.',
      short_alias: 'f', required: false, default_value: default_list_filter
    ),
    Option.new(
      'view',
      'Controls the amount of information included in the response. By default, the creatives.list method only '\
      'includes creativeServingDecision. This sample configures the view to return the full contents of creatives by '\
      'setting this to FULL.',
      short_alias: 'v', required: false, default_value: 'FULL'
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
    list_creatives(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
