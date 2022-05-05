#!/usr/bin/python
#
# Copyright 2022 Google Inc. All Rights Reserved.
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

"""Gets a single publisher connection for the given bidder.

Note: This sample will only return a populated response for bidders who are
exchanges participating in Open Bidding.
"""


import argparse
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_PUBLISHER_CONNECTION_NAME_TEMPLATE = 'bidders/%s/publisherConnections/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BIDDER_RESOURCE_ID_HERE'
DEFAULT_PUBLISHER_CONNECTION_RESOURCE_ID = 'ENTER_CONNECTION_RESOURCE_ID_HERE'


def main(realtimebidding, account_id, publisher_connection_id):
  publisher_connection_name = _PUBLISHER_CONNECTION_NAME_TEMPLATE % (
      account_id, publisher_connection_id)

  print('Retrieving a publisher connection with name: '
        f'"{publisher_connection_name}".')
  try:
    response = realtimebidding.bidders().publisherConnections().get(
        name=publisher_connection_name).execute()
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
      description=('Get a publisher connection for the given bidder and '
                   'publisher connection IDs.'))
  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID, required=True,
      help=('The resource ID of the bidders resource under which the '
            'publisher connection exists. This will be used to construct the '
            'name used as a path parameter for the publisherConnections.get '
            'request.'))
  parser.add_argument(
      '-p', '--publisher_connection_id',
      default=DEFAULT_PUBLISHER_CONNECTION_RESOURCE_ID, required=True,
      help=('The resource ID of the publisher connection that is being '
            'retrieved. This value is the publisher ID found in ads.txt or '
            'app-ads.txt, and is used to construct the name used as a path '
            'parameter for the publisherConnections.get request.'))

  args = parser.parse_args()

  main(service, args.account_id, args.pretargeting_config_id)

