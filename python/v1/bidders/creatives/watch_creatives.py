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

"""Enables monitoring of changes of a creative status for a given bidder.

Watched creatives will have changes to their status posted to Google Cloud
Pub/Sub. For more details on Google Cloud Pub/Sub, see:
https://cloud.google.com/pubsub/docs

For an example of pulling creative status changes from a Google Cloud Pub/Sub
subscription, see pull_watched_creatives_subscription.py.
"""


import argparse
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_BIDDER_NAME_TEMPLATE = 'bidders/%s'

DEFAULT_BIDDER_RESOURCE_ID = 'ENTER_BIDDER_RESOURCE_ID_HERE'


def main(realtimebidding, args):
  account_id = args.account_id

  print(f'Watching creative status changes for bidder account "{account_id}":')

  try:
    # Construct and execute the request.
    response = realtimebidding.bidders().creatives().watch(
        parent=_BIDDER_NAME_TEMPLATE % account_id).execute()
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
      description=('Enables watching creative status changes for the given '
                   'bidder account.'))
  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BIDDER_RESOURCE_ID,
      help=('The resource ID of a bidder account. This will be used to '
            'construct the parent used as a path parameter for the '
            'creatives.watch request.'))

  args = parser.parse_args()

  main(service, args)

