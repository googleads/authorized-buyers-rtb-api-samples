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

"""This example patches a creative with the specified name."""


import argparse
import datetime
import os
import pprint
import sys
import uuid

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError
import util


_CREATIVES_NAME_TEMPLATE = 'buyers/%s/creatives/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'
DEFAULT_CREATIVE_RESOURCE_ID = 'ENTER_USERLIST_RESOURCE_ID_HERE'


def main(realtimebidding, args):
  account_id = args.account_id
  creative_id = args.creative_id

  creative = {
      'advertiserName': f'Test-Advertiser-{uuid.uuid4()}',
      'declaredClickThroughUrls': [f'https://test.clickurl.com/{uuid.uuid4()}'
                                   for _ in range(3)]
  }

  update_mask = ','.join(creative.keys())

  print(f'Patching creative with ID {creative_id} for buyer account ID '
        f'{account_id}:')
  try:
    # Construct and execute the request.
    response = (realtimebidding.buyers().creatives().patch(
                name=_CREATIVES_NAME_TEMPLATE % (account_id, creative_id),
                body=creative, updateMask=update_mask).execute())
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
      description=('Patches the specified creative.')
  )

  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
      help=('The resource ID of the buyers resource under which the '
            'creative was created. This will be used to construct the '
            'name used as a path parameter for the creatives.patch request.'))
  parser.add_argument(
      '-c', '--creative_id', default=DEFAULT_CREATIVE_RESOURCE_ID,
      help='The resource ID of the buyers.creatives resource for which the '
            'creative was created. This will be used to construct the '
            'name used as a path parameter for the creatives.patch request.')

  args = parser.parse_args()

  main(service, args)

