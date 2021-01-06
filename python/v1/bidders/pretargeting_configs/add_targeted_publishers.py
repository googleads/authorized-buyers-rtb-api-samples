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

"""Adds publisher IDs to a pretargeting configuration's publisher targeting.

Note that this is the only way to append publisher IDs following a pretargeting
configuration's creation. If a pretargeting configuration already targets
publisher IDs, you must specify a targeting mode that is identical to the
existing targeting mode.
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
      'publisherIds': args.publisher_ids,
      'targetingMode': args.publisher_targeting_mode
  }

  print('Updating publisher targeting with new publisher IDs for pretargeting '
        f'configuration with name: "{pretargeting_config_name}".')
  try:
    response = realtimebidding.bidders().pretargetingConfigs().addTargetedPublishers(
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
      description=('Adds publisher IDs to a pretargeting configuration\'s '
                   'publisher targeting.'))
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
      '--publisher_targeting_mode', required=True,
      help=('The targeting mode for the configuration\'s publisher targeting. '
            'Valid values include: INCLUSIVE, and EXCLUSIVE. Note that if the '
            'configuration already targets publisher Ids, you must specify an '
            'identical targeting mode.'))
  # Optional fields.
  parser.add_argument(
      '--publisher_ids', nargs='*', required=True,
      help=('The publisher IDs specified for this configuration\'s publisher '
            'targeting, which allows one to target a subset of publisher '
            'inventory. Specify each ID separated by a space. Valid publisher '
            'IDs can be found in Real-time Bidding bid requests, or '
            'alternatively in ads.txt / app-ads.txt. For more information, '
            'see: https://iabtechlab.com/ads-txt/'))

  args = parser.parse_args()

  main(service, args)

