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

"""Gets a single creative for the given account and creative IDs."""


import argparse
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_CREATIVES_NAME_TEMPLATE = 'buyers/%s/creatives/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'
DEFAULT_CREATIVE_RESOURCE_ID = 'ENTER_CREATIVE_RESOURCE_ID_HERE'


def main(realtimebidding, account_id, creative_id):
  print(f'Get Creative with ID "{creative_id}" for account "{account_id}":')
  try:
    # Construct and execute the request.
    response = realtimebidding.buyers().creatives().get(
        name=_CREATIVES_NAME_TEMPLATE % (account_id, creative_id)).execute()
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
      description=('Get a creative for the given buyer account ID and '
                   'creative ID.'))
  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
      help=('The resource ID of the buyers resource under which the '
            'creative was created. This will be used to construct the '
            'name used as a path parameter for the creatives.get request.'))
  parser.add_argument(
      '-c', '--creative_id', default=DEFAULT_CREATIVE_RESOURCE_ID,
      help=('The resource ID of the buyers.creatives resource for which the '
            'creative was created. This will be used to construct the name '
            'used as a path parameter for the creatives.get request.'))

  args = parser.parse_args()

  main(service, args.account_id, args.creative_id)

