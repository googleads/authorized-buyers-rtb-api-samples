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
# Adds site URLs to a pretargeting configuration's web targeting.
#
# Note that this is the only way to append URLs following a pretargeting
# configuration's creation. If a pretargeting configuration already targets
# URLs, you must specify a targeting mode that is identical to the existing
# targeting mode.

require 'optparse'

require_relative '../../../util'


def add_targeted_sites(realtimebidding, options)
  name = "bidders/#{options[:account_id]}/pretargetingConfigs/#{options[:pretargeting_config_id]}"

  puts "Updating web targeting with new URLs for pretargeting configuraton with name: '#{name}'"

  body = Google::Apis::RealtimebiddingV1::AddTargetedSitesRequest.new(
    sites: options[:web_targeting_urls],
    targeting_mode: options[:web_targeting_mode],
  )

  pretargeting_config = realtimebidding.add_pretargeting_config_targeted_sites(name, body)

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
      'web_targeting_mode',
      'The targeting mode for this configuration\'s web targeting. Valid values include: INCLUSIVE, and EXCLUSIVE. '\
      'Note that if the configuration already targets URLs, you must specify an identical targeting mode.',
      required: false, default_value: nil
    ),
    Option.new(
      'web_targeting_urls',
      'The URLs specified for this configuration\'s web targeting, which allows one to target a subset of site '\
      'inventory. Specify each value separated by a comma. Values specified must be valid URLs.',
      required: false, type: Array, default_value: []
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    add_targeted_sites(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
