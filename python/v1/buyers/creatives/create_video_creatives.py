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

"""Creates a creative with video content for the given buyer account ID."""


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
    'video': {
        'videoUrl': args.video_url
    }
  }

  print(f'Creating video creative for buyer account ID "{account_id}":')
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
      description=('Creates a video creative for the given buyer account ID.')
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
      '-c', '--creative_id', default='HTML_Creative_%s' % uuid.uuid4(),
      help=('The user-specified creative ID. The maximum length of the '
            'creative ID is 128 bytes.'))
  parser.add_argument(
      '--declared_attributes', nargs='*', default=['CREATIVE_TYPE_VAST_VIDEO'],
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
      '--video_url', help='The URL to fetch a video ad.',
      default='https://video.test.com/ads?id=123456&wprice=%%WINNING_PRICE%%')

  args = parser.parse_args()

  main(service, args)

