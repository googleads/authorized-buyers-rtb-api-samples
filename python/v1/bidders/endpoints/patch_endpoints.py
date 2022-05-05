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

"""This example patches an endpoint with a specified name."""


import argparse
import os
import pprint
import sys
import uuid

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_ENDPOINT_NAME_TEMPLATE = 'bidders/%s/endpoints/%s'

DEFAULT_BIDDER_RESOURCE_ID = 'ENTER_BIDDER_RESOURCE_ID_HERE'
DEFAULT_ENDPOINT_RESOURCE_ID = 'ENTER_ENDPOINT_RESOURCE_ID_HERE'


def main(realtimebidding, args):
  endpoint_name = _ENDPOINT_NAME_TEMPLATE % (args.account_id, args.endpoint_id)

  body = {
      'maximumQps': args.maximum_qps,
      'tradingLocation': args.trading_location,
      'bidProtocol': args.bid_protocol
  }

  update_mask = 'maximumQps,tradingLocation,bidProtocol'

  print(f'Patching an endpoint with name: "{endpoint_name}":')
  try:
    response = realtimebidding.bidders().endpoints().patch(
        name=endpoint_name, body=body,
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
      description='Patches a specified endpoint.')
  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BIDDER_RESOURCE_ID,
      help=('The resource ID of the bidders resource under which the '
            'endpoint exists.'))
  parser.add_argument(
      '-e', '--endpoint_id', default=DEFAULT_ENDPOINT_RESOURCE_ID,
      help='The resource ID of the endpoint to be patched.')
  # Optional fields.
  parser.add_argument(
      '-b', '--bid_protocol', default='GOOGLE_RTB',
      help='The real-time bidding protocol that the endpoint is using.')
  parser.add_argument(
      '-m', '--maximum_qps', default='1',
      help=('The maximum number of queries per second allowed to be sent to '
            'the endpoint.'))
  parser.add_argument(
      '-t', '--trading_location', default='US_EAST',
      help=('Region where the endpoint and its infrastructure is located; '
            'corresponds to the location of users that bid requests are sent '
            'for.'))

  args = parser.parse_args()

  main(service, args)

