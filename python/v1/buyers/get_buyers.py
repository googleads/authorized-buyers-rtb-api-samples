#!/usr/bin/python
#
# Copyright 2021 Google Inc. All Rights Reserved.
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

"""Gets a single buyer for the specified buyer name.

The buyer specified must be associated with the authorized service account.
"""


import argparse
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../..'))

from googleapiclient.errors import HttpError

import util


_BUYERS_NAME_TEMPLATE = 'buyers/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'


def main(realtimebidding, account_id):
  print(f'Get Buyer with ID "{account_id}":')
  try:
    # Construct and execute the request.
    response = realtimebidding.buyers().get(
        name=_BUYERS_NAME_TEMPLATE % account_id).execute()
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
      description=('Get a buyer for the given account ID.'))
  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
      help=('The resource ID of the buyers resource that is being retrieved. '
            'This will be used to construct the name used as a path parameter '
            'for the buyers.get request.'))

  args = parser.parse_args()

  main(service, args.account_id)

