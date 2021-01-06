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

"""Adds site URLs to a pretargeting configuration's web targeting.

Note that this is the only way to append URLs following a pretargeting
configuration's creation. If a pretargeting configuration already targets URLs,
you must specify a targeting mode that is identical to the existing targeting
mode.
"""


import argparse
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_PRETARGETING_CONFIG_NAME_TEMPLATE = 'bidders/%s/pretargetingConfigs/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BIDDER_RESOURCE_ID_HERE'
DEFAULT_PRETARGETING_CONFIG_RESOURCE_ID = 'ENTER_CONFIG_RESOURCE_ID_HERE'


def main(realtimebidding, args):
  pretargeting_config_name = _PRETARGETING_CONFIG_NAME_TEMPLATE % (
      args.account_id, args.pretargeting_config_id)

  body = {
      'sites': args.web_targeting_urls,
      'targetingMode': args.web_targeting_mode
  }

  print('Updating web targeting with new site URLs for pretargeting '
        f'configuration with name: "{pretargeting_config_name}".')
  try:
    response = realtimebidding.bidders().pretargetingConfigs().addTargetedSites(
        pretargetingConfig=pretargeting_config_name, body=body).execute()
  except HttpError as e:
    print(e)
    sys.exit(1)

  pprint.pprint(response)


if __name__ == '__main__':
  try:
    service = util.GetService(version='v1')
  except IOError as ex:
    print('Unable to create realtimebidding service - %s' % ex)
    print('Did you specify the key file in util.py?')
    sys.exit(1)

  parser = argparse.ArgumentParser(
      description=('Adds site URLs to a pretargeting configuration\'s web '
                   'targeting.'))
  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
      help=('The resource ID of the bidders resource under which the '
            'pretargeting configuration was created.'))
  parser.add_argument(
      '-p', '--pretargeting_config_id',
      default=DEFAULT_PRETARGETING_CONFIG_RESOURCE_ID,
      help=('The resource ID of the pretargeting configuration that is being '
            'acted upon.'))
  parser.add_argument(
      '--web_targeting_mode', required=True,
      help=('The targeting mode for this configuration\'s web targeting. '
            'Valid values include: INCLUSIVE, and EXCLUSIVE. Note '
            'that if the configuration already targets URLs, you must specify '
            'an identical targeting mode.'))
  # Optional fields.
  parser.add_argument(
      '--web_targeting_urls', nargs='*', required=True,
      help=('The URLs specified for this configuration\'s web targeting, '
            'which allows one to target a subset of site inventory. Specify '
            'each value separated by a space. Values specified must be valid '
            'URLs.'))

  args = parser.parse_args()

  main(service, args)

