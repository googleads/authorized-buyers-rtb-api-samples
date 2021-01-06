#!/usr/bin/python
#
# Copyright 2020 Google Inc. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""This example creates a pretargeting config for the given bidder account ID.
"""


import argparse
import os
import pprint
import sys
import uuid

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_BIDDER_NAME_TEMPLATE = 'bidders/%s'

DEFAULT_BIDDER_RESOURCE_ID = 'ENTER_BIDDER_RESOURCE_ID_HERE'


def main(realtimebidding, args):
  account_id = args.account_id

  body = {
      'displayName': args.display_name,
      'includedFormats': args.included_formats,
      'geoTargeting': {
          'includedIds': args.included_geo_ids,
          'excludedIds': args.excluded_geo_ids
      },
      'userListTargeting': {
          'includedIds': args.included_user_list_ids,
          'excludedIds': args.excluded_user_list_ids
      },
      'interstitialTargeting': args.interstitial_targeting,
      'allowedUserTargetingModes': args.allowed_user_targeting_modes,
      'excludedContentLabelIds': args.excluded_content_label_ids,
      'includedUserIdTypes': args.included_user_id_types,
      'includedLanguages': args.included_language_codes,
      'includedMobileOperatingSystemIds': args.included_mobile_os_ids,
      'verticalTargeting': {
          'includedIds': args.included_vertical_ids,
          'excludedIds': args.excluded_vertical_ids
      },
      'includedPlatforms': args.included_platforms,
      'includedCreativeDimensions': [{
          'height': args.included_creative_dimension_height,
          'width': args.included_creative_dimension_width
      }],
      'includedEnvironments': args.included_environments,
      'webTargeting': {
          'targetingMode': args.web_targeting_mode,
          'values': args.web_targeting_urls
      },
      'appTargeting': {
          'mobileAppTargeting': {
              'targetingMode': args.mobile_app_targeting_mode,
              'values': args.mobile_app_targeting_app_ids
          },
          'mobileAppCategoryTargeting': {
              'includedIds': args.included_mobile_app_targeting_category_ids,
              'excludedIds': args.excluded_mobile_app_targeting_category_ids
          }
      },
      'publisherTargeting': {
          'targetingMode': args.publisher_targeting_mode,
          'values': args.publisher_ids
      },
      'minimumViewabilityDecile': args.minimum_viewability_decile
  }

  print('Creating a pretargeting configuration for account ID: '
        f'"{account_id}".')
  try:
    response = realtimebidding.bidders().pretargetingConfigs().create(
        parent=_BIDDER_NAME_TEMPLATE % account_id, body=body).execute()
  except HttpError as e:
    print(e)
    sys.exit(1)

  pprint.pprint(response)


if __name__ == '__main__':
  try:
    service = util.GetService(version='v1')
  except IOError as ex:
    print(f'Unable to create realtimebidding service - {ex}')
    print('Did you specify the key file in util.py?')
    sys.exit(1)

  parser = argparse.ArgumentParser(
      description=('Creates a pretargeting configuration for the given bidder '
                   'account.'))
  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BIDDER_RESOURCE_ID,
      help=('The resource ID of the bidders resource under which the '
            'pretargeting configuration is to be created.'))
  # Optional fields.
  parser.add_argument(
      '-d', '--display_name',
      default=f'TEST_PRETARGETING_CONFIG_{uuid.uuid4()}',
      help=('The display name to associate with the new configuration. Must '
            'be unique among all of a bidder\'s pretargeting configurations.'))
  parser.add_argument(
      '--included_formats', nargs='*', default=[],
      help=('Creative formats included by this configuration. An unset value '
            'will not filter any bid requests based on the format. Valid '
            'values include: HTML, NATIVE, and VAST.'))
  parser.add_argument(
      '--included_geo_ids', nargs='*', default=[],
      help=('The geo IDs to include in targeting for this configuration. '
            'Specify each ID separated by a space. Valid geo IDs can be found '
            'in: https://storage.googleapis.com/adx-rtb-dictionaries/'
            'geo-table.csv'))
  parser.add_argument(
      '--excluded_geo_ids', nargs='*', default=[],
      help=('The geo IDs to exclude in targeting for this configuration. '
            'Specify each ID separated by a space. Valid geo IDs can be found '
            'in: https://storage.googleapis.com/adx-rtb-dictionaries/'
            'geo-table.csv'))
  parser.add_argument(
      '--included_user_list_ids', nargs='*', default=[],
      help=('The user list IDs to include in targeting for this '
            'configuration. Specify each ID separated by a space. Valid user '
            'list IDs would include any found under the buyers.userLists '
            'resource for a given bidder account, or any buyer accounts under '
            'it.'))
  parser.add_argument(
      '--excluded_user_list_ids', nargs='*', default=[],
      help=('The user list IDs to exclude in targeting for this '
            'configuration. Specify each ID separated by a space. Valid user '
            'list IDs would include any found under the buyers.userLists '
            'resource for a given bidder account, or any buyer accounts under '
            'it.'))
  parser.add_argument(
      '--interstitial_targeting', default='ONLY_NON_INTERSTITIAL_REQUESTS',
      help=('The interstitial targeting specified for this configuration. By '
            'default, this will be set to ONLY_NON_INTERSTITIAL_REQUESTS. '
            'Valid values include: ONLY_INTERSTITIAL_REQUESTS and '
            'ONLY_NON_INTERSTITIAL_REQUESTS.'))
  parser.add_argument(
      '--allowed_user_targeting_modes', nargs='*', default=[],
      help=('The targeting modes to include in targeting for this '
            'configuration. Specify each value separated by a space. Valid '
            'targeting modes include: REMARKETING_ADS and '
            'INTEREST_BASED_TARGETING.'))
  parser.add_argument(
      '--excluded_content_label_ids', nargs='*', default=[],
      help=('The sensitive content category IDs excluded in targeting for '
            'this configuration. Valid sensitive content category IDs can be '
            'found in: https://storage.googleapis.com/adx-rtb-dictionaries/'
            'content-labels.txt'))
  parser.add_argument(
      '--included_user_id_types', nargs='*', default=[],
      help=('The user identifier types included in targeting for this '
            'configuration. Specify each value separated by a space. Valid '
            'values include: HOSTED_MATCH_DATA, GOOGLE_COOKIE, and DEVICE_ID.'
           ))
  parser.add_argument(
      '--included_language_codes', nargs='*', default=[],
      help=('The languages represented by languages codes that are included '
            'in targeting for this configuration. Specify each code separated '
            'by a space. Valid language codes can be found in: '
            'https://developers.google.com/adwords/api/docs/appendix/'
            'languagecodes.'))
  parser.add_argument(
      '--included_mobile_os_ids', nargs='*', default=[],
      help=('The mobile OS IDs to include in targeting for this '
            'configuration. Specify each value separated by a space. Valid '
            'mobile OS IDs can be found in: '
            'https://storage.googleapis.com/adx-rtb-dictionaries/mobile-os.csv'
           ))
  parser.add_argument(
      '--included_vertical_ids', nargs='*', default=[],
      help=('The vertical IDs to include in targeting for this configuration. '
            'Specify each ID separated by a space. Valid vertical IDs can be '
            'found in: '
            'https://developers.google.com/authorized-buyers/rtb/downloads/'
            'publisher-verticals'))
  parser.add_argument(
      '--excluded_vertical_ids', nargs='*', default=[],
      help=('The vertical IDs to exclude in targeting for this configuration. '
            'Specify each ID separated by a space. Valid vertical IDs can be '
            'found in: '
            'https://developers.google.com/authorized-buyers/rtb/downloads/'
            'publisher-verticals'))
  parser.add_argument(
      '--included_platforms', nargs='*', default=[],
      help=('The platforms to include in targeting for this configuration. '
            'Specify each value separated by a space. Valid values include: '
            'PERSONAL_COMPUTER, PHONE, TABLET, and CONNECTED_TV.'))
  parser.add_argument(
      '--included_creative_dimension_height', default=300,
      help=('A creative dimension\s height to be included in targeting for '
            'this configuration. By default, this example will set the '
            'targeted height to 300. Note that while only a single set of '
            'dimensions are specified in this sample, pretargeting '
            'configurations can target multiple creative dimensions.'))
  parser.add_argument(
      '--included_creative_dimension_width', default=250,
      help=('A creative dimension\s width to be included in targeting for '
            'this configuration. By default, this example will set the '
            'targeted height to 250. Note that while only a single set of '
            'dimensions are specified in this sample, pretargeting '
            'configurations can target multiple creative dimensions.'))
  parser.add_argument(
      '--included_environments', nargs='*', default=[],
      help=('The environments to include in targeting for this configuration. '
            'Specify each value separated by a space. Valid values include: '
            'APP, and WEB.'))
  parser.add_argument(
      '--web_targeting_mode', default=None,
      help=('The targeting mode for this configuration\'s web targeting. '
            'Valid values include: INCLUSIVE, and EXCLUSIVE.'))
  parser.add_argument(
      '--web_targeting_urls', nargs='*', default=[],
      help=('The URLs specified for this configuration\'s web targeting, '
            'which allows one to target a subset of site inventory. Specify '
            'each value separated by a space. Values specified must be valid '
            'URLs.'))
  parser.add_argument(
      '--mobile_app_targeting_mode', default=None,
      help=('The targeting mode for the configuration\'s mobile app '
            'targeting. Valid values include: INCLUSIVE, and EXCLUSIVE.'))
  parser.add_argument(
      '--mobile_app_targeting_app_ids', nargs='*', default=[],
      help=('The mobile app IDs specified for this configuration\'s mobile '
            'app targeting, which allows one to target a subset of mobile app '
            'inventory. Specify each value separated by a space. Values '
            'specified must be valid mobile App IDs, as found on their '
            'respective app stores.'))
  parser.add_argument(
      '--included_mobile_app_targeting_category_ids', nargs='*', default=[],
      help=('The mobile app category IDs to include in targeting for this '
            'configuration. Specify each ID separated by a space. Valid '
            'category IDs can be found in:'
            'https://developers.google.com/adwords/api/docs/appendix/'
            'mobileappcategories.csv'))
  parser.add_argument(
      '--excluded_mobile_app_targeting_category_ids', nargs='*', default=[],
      help=('The mobile app category IDs to exclude in targeting for this '
            'configuration. Specify each ID separated by a space. Valid '
            'category IDs can be found in:'
            'https://developers.google.com/adwords/api/docs/appendix/'
            'mobileappcategories.csv'))
  parser.add_argument(
      '--publisher_targeting_mode', default=None,
      help=('The targeting mode for the configuration\'s publisher targeting. '
            'Valid values include: INCLUSIVE, and EXCLUSIVE.'))
  parser.add_argument(
      '--publisher_ids', nargs='*', default=[],
      help=('The publisher IDs specified for this configuration\'s publisher '
            'targeting, which allows one to target a subset of publisher '
            'inventory. Specify each ID separated by a space. Valid publisher '
            'IDs can be found in Real-time Bidding bid requests, or '
            'alternatively in ads.txt / app-ads.txt. For more information, '
            'see: https://iabtechlab.com/ads-txt/'))
  parser.add_argument(
      '-m', '--minimum_viewability_decile', default=5,
      help=('The display name to associate with the new configuration. Must '
            'be unique among all of a bidder\'s pretargeting configurations.'))

  args = parser.parse_args()

  main(service, args)

