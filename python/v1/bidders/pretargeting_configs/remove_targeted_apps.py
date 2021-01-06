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

"""Removes mobile app IDs from a pretargeting configuration's app targeting.

Note that this is the only way to remove mobile application IDs following a
pretargeting configuration's creation.
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
      'appIds': args.mobile_app_targeting_app_ids
  }

  print('Removing mobile app IDs from mobile app targeting for pretargeting '
        f'configuration with name: "{pretargeting_config_name}".')
  try:
    response = realtimebidding.bidders().pretargetingConfigs().removeTargetedApps(
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
      description=('Removes mobile app IDs from a pretargeting '
                   'configuration\'s app targeting.'))
  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID, required=True,
      help=('The resource ID of the bidders resource under which the '
            'pretargeting configuration was created.'))
  parser.add_argument(
      '-p', '--pretargeting_config_id',
      default=DEFAULT_PRETARGETING_CONFIG_RESOURCE_ID,
      help=('The resource ID of the pretargeting configuration that is being '
            'acted upon.'))
  # Optional fields.
  parser.add_argument(
      '--mobile_app_targeting_app_ids', nargs='*', default=[],
      help=('The mobile app IDs to be removed from this configuration\'s '
            'mobile app targeting. Specify each value separated by a space. '
            'Values specified must be valid mobile app IDs, as found on their '
            'respective app stores.'))

  args = parser.parse_args()

  main(service, args)

