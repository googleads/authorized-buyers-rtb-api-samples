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

"""Gets a single endpoint for the specified bidder and endpoint IDs."""


import argparse
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_ENDPOINTS_NAME_TEMPLATE = 'bidders/%s/endpoints/%s'

DEFAULT_BIDDER_RESOURCE_ID = 'ENTER_BIDDER_RESOURCE_ID_HERE'
DEFAULT_ENDPOINT_RESOURCE_ID = 'ENTER_ENDPOINT_RESOURCE_ID_HERE'


def main(realtimebidding, account_id, endpoint_id):
  print(f'Get endpoint with ID "{endpoint_id}" for bidder account with ID '
        f'"{account_id}":')
  try:
    # Construct and execute the request.
    response = realtimebidding.bidders().endpoints().get(
        name=_ENDPOINTS_NAME_TEMPLATE % (account_id, endpoint_id)).execute()
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
      description=('Get an endpoint for the given bidder and endpoint IDs.'))
  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BIDDER_RESOURCE_ID,
      help=('The resource ID of the bidders resource under which the endpoint '
            'exists. This will be used to construct the name used as a path '
            'parameter for the endpoints.get request.'))
  parser.add_argument(
      '-e', '--endpoint_id', default=DEFAULT_ENDPOINT_RESOURCE_ID,
      help=('The resource ID of the endpoints resource that is being '
            'retrieved. This will be used to construct the name used as a '
            'path parameter for the endpoints.get request.'))

  args = parser.parse_args()

  main(service, args.account_id, args.endpoint_id)

