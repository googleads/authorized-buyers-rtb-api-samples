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

"""Batch rejects one or more publisher connections.

A bidder will not receive bid requests from publishers associated with rejected
publisher connections.
"""


import argparse
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_BIDDER_NAME_TEMPLATE = 'bidders/%s'
_PUBLISHER_CONNECTION_NAME_TEMPLATE = 'bidders/%s/publisherConnections/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BIDDER_RESOURCE_ID_HERE'


def main(realtimebidding, args):
  account_id = args.account_id
  parent = _BIDDER_NAME_TEMPLATE % account_id

  body = {
      "names": [
          _PUBLISHER_CONNECTION_NAME_TEMPLATE % (
              account_id, publisher_connection_id)
          for publisher_connection_id in args.publisher_connection_ids
      ]
  }

  print('Batch rejecting publisher connections for bidder with name: '
        f'"{parent}".')
  try:
    response = realtimebidding.bidders().publisherConnections().batchReject(
        parent=parent, body=body).execute()
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
      description='Batch rejects one or more publisher connections.')
  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
      help=('The resource ID of the bidders resource under which the '
            'publisher connections exist. This will be used to construct the '
            'parent used as a path parameter for the '
            'publisherConnections.batchReject request, as well as the '
            'publisher connection names passed in the request body.'))
  parser.add_argument(
      '-p', '--publisher_connection_ids', nargs='+', required=True,
      help=('One or more resource IDs for the bidders.publisherConnections '
            'resource that are being rejected. Specify each value separated by '
            'a space. These will be used to construct the publisher connection '
            'names passed in the publisherConnections.batchReject request '
            'body.'))

  args = parser.parse_args()

  main(service, args)
