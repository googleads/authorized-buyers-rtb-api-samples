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
# Patches a pretargeting configuration for the given bidder account and pretargeting configuration IDs.

require 'optparse'
require 'securerandom'

require_relative '../../../util'


def patch_pretargeting_configs(realtimebidding, options)
  name = "bidders/#{options[:account_id]}/pretargetingConfigs/#{options[:pretargeting_config_id]}"

  body = Google::Apis::RealtimebiddingV1::PretargetingConfig.new(
    display_name: options[:display_name],
    included_formats: ['HTML', 'VAST'],
    # Note that repeated fields such as geo_targeting are completely overwritten by the contents included in the patch
    # request.
    geo_targeting: Google::Apis::RealtimebiddingV1::NumericTargetingDimension.new(
      included_ids: [
        '200635',   # Austin, TX
        '1014448',  # Boulder, CO
        '1022183',  # Hoboken, NJ
        '200622',   # New Orleans, LA
        '1023191',  # New York, NY
        '9061237',  # Mountain View, CA
        '1014221',   # San Francisco, CA
      ],
    ),
    included_creative_dimensions: [
      Google::Apis::RealtimebiddingV1::CreativeDimensions.new(
        height: 480,
        width: 320
      ),
      Google::Apis::RealtimebiddingV1::CreativeDimensions.new(
        height: 1080,
        width: 1920
      ),
    ],
  )

  update_mask = 'displayName,includedFormats,geoTargeting.includedIds,includedCreativeDimensions'

  puts "Patching a pretargeting configuration with name: '#{name}'"

  pretargeting_config = realtimebidding.patch_bidder_pretargeting_config(name, body)
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
      'The resource ID of the bidders resource under which the pretargeting configuration is to be created, This '\
      'will be used to construct the name used as a path parameter for the pretargetingConfig.create request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'pretargeting_config_id', 'The resource ID of the pretargeting configuration to be patched.',
      type: Integer, required: true, default_value: nil
    ),
    Option.new(
      'display_name',
      'The patched display name to associate with the configuration. Must be unique among all of a bidder\'s '\
      'pretargeting configurations.',
      short_alias: 'd', required: false, default_value: "TEST_PRETARGETING_CONFIG_#{SecureRandom.uuid}"
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
