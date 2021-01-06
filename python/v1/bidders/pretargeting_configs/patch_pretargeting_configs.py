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

"""This example patches a pretargeting config with a specified name."""


import argparse
import os
import pprint
import sys
import uuid

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_PRETARGETING_CONFIG_NAME_TEMPLATE = 'bidders/%s/pretargetingConfigs/%s'

DEFAULT_BIDDER_RESOURCE_ID = 'ENTER_BIDDER_RESOURCE_ID_HERE'
DEFAULT_PRETARGETING_CONFIG_RESOURCE_ID = 'ENTER_CONFIG_RESOURCE_ID_HERE'


def main(realtimebidding, args):
  pretargeting_config_name = _PRETARGETING_CONFIG_NAME_TEMPLATE % (
      args.account_id, args.pretargeting_config_id)

  body = {
      'displayName': args.display_name,
      'includedFormats': ['HTML', 'VAST'],
      'geoTargeting': {
          # Note that repeated fields such as this are completely overwritten
          # by the contents included in the patch request.
          'includedIds': [
              '200635',   # Austin, TX
              '1014448',  # Boulder, CO
              '1022183',  # Hoboken, NJ
              '200622',   # New Orleans, LA
              '1023191',  # New York, NY
              '9061237',  # Mountain View, CA
              '1014221'   # San Francisco, CA
          ],
      },
      'includedCreativeDimensions': [
          {
              'height': 480,
              'width': 320
          },
          {
              'height': 1080,
              'width': 1920
          }
      ],
  }

  update_mask = ('displayName,includedFormats,geoTargeting.includedIds,'
                 'includedCreativeDimensions')

  print('Patching a pretargeting configuration with name: '
        f'"{pretargeting_config_name}".')
  try:
    response = realtimebidding.bidders().pretargetingConfigs().patch(
        name=pretargeting_config_name, body=body,
        updateMask=update_mask).execute()
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
      description='Patches a specified pretargeting configuration.')
  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BIDDER_RESOURCE_ID,
      help=('The resource ID of the bidders resource under which the '
            'pretargeting configuration was created.'))
  parser.add_argument(
      '-p', '--pretargeting_config_id',
      default=DEFAULT_PRETARGETING_CONFIG_RESOURCE_ID,
      help='The resource ID of the pretargeting configuration to be patched.')
  # Optional fields.
  parser.add_argument(
      '-d', '--display_name',
      default=f'TEST_PRETARGETING_CONFIG_{uuid.uuid4()}',
      help=('The display name to associate with the new configuration. Must '
            'be unique among all of a bidder\'s pretargeting configurations.'))

  args = parser.parse_args()

  main(service, args)

