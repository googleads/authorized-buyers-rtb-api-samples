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

"""Creates a creative with native content for the given buyer account ID."""


import argparse
import datetime
import os
import pprint
import sys
import uuid

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError
import util


_BUYER_NAME_TEMPLATE = 'buyers/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'


def main(realtimebidding, args):
  account_id = args.account_id

  creative = {
    'advertiserName': args.advertiser_name,
    'creativeId': args.creative_id,
    'declaredAttributes': args.declared_attributes,
    'declaredClickThroughUrls': args.declared_click_urls,
    'declaredRestrictedCategories': args.declared_restricted_categories,
    'declaredVendorIds': [int(id) for id in args.declared_vendor_ids],
    'native': {
        'headline': args.native_headline,
        'body': args.native_body,
        'callToAction': args.native_call_to_action,
        'advertiserName': args.native_advertiser_name,
        'image': {
            'url': args.native_image_url,
            'height': args.native_image_height,
            'width': args.native_image_width
        },
        'logo': {
            'url': args.native_logo_url,
            'height': args.native_logo_height,
            'width': args.native_logo_width
        },
        'clickLinkUrl': args.native_click_link_url,
        'clickTrackingUrl': args.native_click_tracking_url
    }
  }

  print(f'Creating native creative for buyer account ID "{account_id}":')
  try:
    # Construct and execute the request.
    response = (realtimebidding.buyers().creatives().create(
        parent=_BUYER_NAME_TEMPLATE % account_id, body=creative).execute())
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
      description=('Creates a native creative for the given buyer account ID.')
  )

  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
      help=('The resource ID of the buyers resource under which the '
            'creative is to be created.'))
  # Optional fields.
  parser.add_argument(
      '--advertiser_name', default='Test',
      help='The name of the company being advertised in the creative.')
  parser.add_argument(
      '-c', '--creative_id', default='Native_Creative_%s' % uuid.uuid4(),
      help=('The user-specified creative ID. The maximum length of the '
            'creative ID is 128 bytes.'))
  parser.add_argument(
      '--declared_attributes', nargs='*',
      default=['NATIVE_ELIGIBILITY_ELIGIBLE'],
      help=('The creative attributes being declared. Specify each attribute '
            'separated by a space.'))
  parser.add_argument(
      '--declared_click_urls', nargs='*', default=['http://test.com'],
      help=('The click-through URLs being declared. Specify each URL '
            'separated by a space.'))
  parser.add_argument(
      '--declared_restricted_categories', nargs='*', default=[],
      help=('The restricted categories being declared. Specify each category '
            'separated by a space.'))
  parser.add_argument(
      '--declared_vendor_ids', nargs='*', default=[],
      help=('The vendor IDs being declared. Specify each ID separated by a '
            'space.'))
  parser.add_argument(
      '--native_headline', default='Luxury Mars Cruises',
      help=('A short title for the ad.'))
  parser.add_argument(
      '--native_body', default='Visit the planet in a luxury spaceship.',
      help='A long description of the ad.'),
  parser.add_argument(
      '--native_call_to_action', default='Book today',
      help='A label for the button that the user is supposed to click.')
  parser.add_argument(
      '--native_advertiser_name', default='Galactic Luxury Cruises',
      help=('The name of the advertiser or sponsor, to be displayed in the ad '
            'creative.')),
  parser.add_argument(
      '--native_image_url', default='https://native.test.com/image?id=123456',
      help='The URL of the large image to be included in the native ad.'),
  parser.add_argument(
      '--native_image_height', default=627,
      help='The height in pixels of the native ad\'s large image.'),
  parser.add_argument(
      '--native_image_width', default=1200,
      help='The width in pixels of the native ad\'s large image.'),
  parser.add_argument(
      '--native_logo_url', default='https://native.test.com/logo?id=123456',
      help='The URL of a smaller image to be included in the native ad.'),
  parser.add_argument(
      '--native_logo_height', default=100,
      help='The height in pixels of the native ad\'s smaller image.'),
  parser.add_argument(
      '--native_logo_width', default=100,
      help='The width in pixels of the native ad\'s smaller image.'),
  parser.add_argument(
      '--native_click_link_url', default='https://www.google.com',
      help=('The URL that the browser/SDK will load when the user clicks the '
           'ad.')),
  parser.add_argument(
      '--native_click_tracking_url',
      default='https://native.test.com/click?id=123456',
      help='The URL to use for click tracking.'),

  args = parser.parse_args()

  main(service, args)

