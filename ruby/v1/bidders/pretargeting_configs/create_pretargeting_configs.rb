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
# Creates a pretargeting configuration for the given bidder account ID.

require 'optparse'
require 'securerandom'

require_relative '../../../util'


def create_pretargeting_configs(realtimebidding, options)
  parent = "bidders/#{options[:account_id]}"

  body = Google::Apis::RealtimebiddingV1::PretargetingConfig.new(
    display_name: options[:display_name],
    included_formats: options[:included_formats],
    geo_targeting: Google::Apis::RealtimebiddingV1::NumericTargetingDimension.new(
      included_ids: options[:included_geo_ids],
      excluded_ids: options[:excluded_geo_ids]
    ),
    user_list_targeting: Google::Apis::RealtimebiddingV1::NumericTargetingDimension.new(
      included_ids: options[:included_user_list_ids],
      excluded_ids: options[:excluded_user_list_ids]
    ),
    interstitial_targeting: options[:interstitial_targeting],
    allowed_user_targeting_modes: options[:allowed_user_targeting_modes],
    excluded_content_label_ids: options[:excluded_content_label_ids],
    included_user_id_types: options[:included_user_id_types],
    included_languages: options[:included_language_codes],
    included_mobile_operating_system_ids: options[:included_mobile_os_ids],
    vertical_targeting: Google::Apis::RealtimebiddingV1::NumericTargetingDimension.new(
      included_ids: options[:included_vertical_ids],
      excluded_ids: options[:excluded_vertical_ids]
    ),
    included_platforms: options[:included_platforms],
    included_creative_dimensions: [Google::Apis::RealtimebiddingV1::CreativeDimensions.new(
      height: options[:included_creative_dimension_height],
      width: options[:included_creative_dimension_width]
    )],
    included_environments: options[:included_environments],
    web_targeting: Google::Apis::RealtimebiddingV1::StringTargetingDimension.new(
      targeting_mode: options[:web_targeting_mode],
      values: options[:web_targeting_urls]
    ),
    app_targeting: Google::Apis::RealtimebiddingV1::AppTargeting.new(
      mobile_app_targeting: Google::Apis::RealtimebiddingV1::StringTargetingDimension.new(
        targeting_mode: options[:mobile_app_targeting_mode],
        values: options[:mobile_app_targeting_app_ids]
      ),
      mobile_app_category_targeting: Google::Apis::RealtimebiddingV1::NumericTargetingDimension.new(
        included_ids: options[:included_mobile_app_targeting_category_ids],
        excluded_ids: options[:excluded_mobile_app_targeting_category_ids]
      )
    ),
    publisher_targeting: Google::Apis::RealtimebiddingV1::StringTargetingDimension.new(
      targeting_mode: options[:publisher_targeting_mode],
      values: options[:publisher_ids]
    ),
    minimum_viewability_decile: options[:minimum_viewability_decile]
  )

  puts "Creating a pretargeting configuration for bidder account '#{parent}'"

  pretargeting_config = realtimebidding.create_bidder_pretargeting_config(parent, body)
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
      'display_name',
      'The display name to associate with the new configuration. Must be unique among all of a bidder\'s '\
      'pretargeting configurations.',
      short_alias: 'd', required: false, default_value: "TEST_PRETARGETING_CONFIG_#{SecureRandom.uuid}"
    ),
    Option.new(
      'included_formats',
      'Creative formats included by this configuration. Specify each ID separated by a comma. An unset value will '\
      'not filter any bid requests based on the format. Valid values include: HTML, NATIVE, and VAST.',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'included_geo_ids',
      'The geo IDs to include in targeting for this configuration. Specify each ID separated by a comma. Valid geo '\
      'IDs can be found in: https://storage.googleapis.com/adx-rtb-dictionaries/geo-table.csv',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'excluded_geo_ids',
      'The geo IDs to exclude in targeting for this configuration. Specify each ID separated by a comma. Valid geo '\
      'IDs can be found in: https://storage.googleapis.com/adx-rtb-dictionaries/geo-table.csv',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'included_user_list_ids',
      'The user list IDs to include in targeting for this configuration. Specify each ID separated by a comma. '\
      'Valid user list IDs would include any found under the buyers.userLists resource for a given bidder account, '\
      'or any buyer accounts under it.',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'excluded_user_list_ids',
      'The user list IDs to exclude in targeting for this configuration. Specify each ID separated by a comma. '\
      'Valid user list IDs would include any found under the buyers.userLists resource for a given bidder account, '\
      'or any buyer accounts under it.',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'interstitial_targeting',
      'The interstitial targeting specified for this configuration. By default, this will be set to '\
      'ONLY_NON_INTERSTITIAL_REQUESTS. Valid values include: ONLY_INTERSTITIAL_REQUESTS and '\
      'ONLY_NON_INTERSTITIAL_REQUESTS.',
      required: false, default_value: 'ONLY_NON_INTERSTITIAL_REQUESTS'
    ),
    Option.new(
      'allowed_user_targeting_modes',
      'The targeting modes to include in targeting for this configuration. Specify each value separated by a comma. '\
      'Valid targeting modes include: REMARKETING_ADS and INTEREST_BASED_TARGETING.',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'excluded_content_label_ids',
      'The sensitive content category IDs excluded in targeting for this configuration. Specify each value separated '\
      'by a comma. Valid sensitive content category IDs can be found in: '\
      'https://storage.googleapis.com/adx-rtb-dictionaries/content-labels.txt',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'included_user_id_types',
      'The user identifier types included in targeting for this configuration. Specify each value separated by a ' \
      'comma. Valid values include: HOSTED_MATCH_DATA, GOOGLE_COOKIE, and DEVICE_ID.',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'included_language_codes',
      'The languages represented by languages codes that are included in targeting for this configuration. Specify '\
      'each code separated by a comma. Valid language codes can be found in: '\
      'https://developers.google.com/adwords/api/docs/appendix/languagecodes.',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'included_mobile_os_ids',
      'The mobile OS IDs to include in targeting for this configuration. Specify each value separated by a comma. '\
      'Valid mobile OS IDs can be found in: https://storage.googleapis.com/adx-rtb-dictionaries/mobile-os.csv',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'included_vertical_ids',
      'The vertical IDs to include in targeting for this configuration. Specify each ID separated by a comma. Valid '\
      'vertical IDs can be found in: https://developers.google.com/authorized-buyers/rtb/downloads/'\
      'publisher-verticals',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'excluded_vertical_ids',
      'The vertical IDs to exclude in targeting for this configuration. Specify each ID separated by a comma. Valid '\
      'vertical IDs can be found in: https://developers.google.com/authorized-buyers/rtb/downloads/'\
      'publisher-verticals',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'included_platforms',
      'The platforms to include in targeting for this configuration. Specify each value separated by a comma. Valid '\
      'values include: PERSONAL_COMPUTER, PHONE, TABLET, and CONNECTED_TV.',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'included_creative_dimension_height',
      'A creative dimension\s height to be included in targeting for this configuration. By default, this example '\
      'will set the targeted height to 300. Note that while only a single set of dimensions are specified in this '\
      'sample, pretargeting configurations can target multiple creative dimensions.',
      required: false, default_value: 300
    ),
    Option.new(
      'included_creative_dimension_width',
      'A creative dimension\s width to be included in targeting for this configuration. By default, this example '\
      'will set the targeted width to 250. Note that while only a single set of dimensions are specified in this '\
      'sample, pretargeting configurations can target multiple creative dimensions.',
      required: false, default_value: 250
    ),
    Option.new(
      'included_environments',
      'The environments to include in targeting for this configuration. Specify each value separated by a comma. '\
      'Valid values include: APP, and WEB.',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'web_targeting_mode',
      'The targeting mode for this configuration\'s web targeting. Valid values include: INCLUSIVE, and EXCLUSIVE.',
      required: false, default_value: nil
    ),
    Option.new(
      'web_targeting_urls',
      'The URLs specified for this configuration\'s web targeting, which allows one to target a subset of site '\
      'inventory. Specify each value separated by a comma. Values specified must be valid URLs.',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'mobile_app_targeting_mode',
      'The targeting mode for the configuration\'s mobile app targeting. Valid values include: INCLUSIVE, and '\
      'EXCLUSIVE.',
      required: false, default_value: nil
    ),
    Option.new(
      'mobile_app_targeting_app_ids',
      'The mobile app IDs specified for this configuration\'s mobile app targeting, which allows one to target a '\
      'subset of mobile app inventory. Specify each value separated by a comma. Values specified must be valid '\
      'mobile App IDs, as found on their respective app stores.',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'included_mobile_app_targeting_category_ids',
      'The mobile app category IDs to include in targeting for this configuration. Specify each ID separated by a '\
      'comma. Valid category IDs can be found in: '\
      'https://developers.google.com/adwords/api/docs/appendix/mobileappcategories.csv',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'excluded_mobile_app_targeting_category_ids',
      'The mobile app category IDs to exclude in targeting for this configuration. Specify each ID separated by a '\
      'comma. Valid category IDs can be found in: '\
      'https://developers.google.com/adwords/api/docs/appendix/mobileappcategories.csv',
      required: false, type: Array, default_value: []
    ),
    Option.new(
      'publisher_targeting_mode',
      'The targeting mode for the configuration\'s publisher targeting. Valid values include: INCLUSIVE, and '\
      'EXCLUSIVE.',
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
    Option.new(
      'minimum_viewability_decile',
      'The targeted minimum viewability decile, ranging from 0 - 10. A value of "5" means that the configuration '\
      'will only match adslots for which we predict at least 50% viewability. Values > 10 will be rounded down to '\
      '10. An unset value, or a value of "0", indicates that bid requests should be sent regardless of viewability.',
      required: false, default_value: nil
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    create_pretargeting_configs(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
