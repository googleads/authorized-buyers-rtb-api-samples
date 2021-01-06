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
# Adds publisher IDs to a pretargeting configuration's publisher targeting.
#
# Note that this is the only way to append publisher IDs following a
# pretargeting configuration's creation. If a pretargeting configuration
# already targets publisher IDs, you must specify a targeting mode that is
# identical to the existing targeting mode.

require 'optparse'

require_relative '../../../util'


def add_targeted_publishers(realtimebidding, options)
  name = "bidders/#{options[:account_id]}/pretargetingConfigs/#{options[:pretargeting_config_id]}"

  puts "Updating publisher targeting with new publisher IDs for pretargeting configuraton with name: '#{name}'"

  body = Google::Apis::RealtimebiddingV1::AddTargetedPublishersRequest.new(
    publisher_ids: options[:publisher_ids],
    targeting_mode: options[:publisher_targeting_mode],
  )

  pretargeting_config = realtimebidding.add_pretargeting_config_targeted_publishers(name, body)

  print_pretargeting_config(pretargeting_config)
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
      'The resource ID of the bidders resource under which the pretargeting configurations were created.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'pretargeting_config_id', 'The resource ID of the pretargeting configuration that is being acted upon.',
      type: Integer, short_alias: 'p', required: true, default_value: nil
    ),
    Option.new(
      'publisher_targeting_mode',
      'The targeting mode for the configuration\'s publisher targeting. Valid values include: INCLUSIVE, and '\
      'EXCLUSIVE. Note that if the configuration already targets publisher IDs, you must specify an identical '\
      'targeting mode.',
      required: false, default_value: nil
    ),
    Option.new(
      'publisher_ids',
      'The publisher IDs specified for this configuration\'s publisher targeting, which allows one to target a '\
      'subset of publisher inventory. Specify each ID separated by a comma. Valid publisher IDs can be found in '\
      'Real-time Bidding bid requests, or alternatively in ads.txt / app-ads.txt. For more information, see: '\
      'https://iabtechlab.com/ads-txt/',
      required: false, type: Array, default_value: []
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    add_targeted_publishers(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
