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

"""Lists pretargeting configurations for the given bidder's account ID."""


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

  page_token = None
  more_pages = True

  print('Listing pretargeting configurations for bidder account: '
        f'"{account_id}".')
  while more_pages:
    try:
      # Construct and execute the request.
      response = realtimebidding.bidders().pretargetingConfigs().list(
          parent=_BIDDER_NAME_TEMPLATE % account_id, pageToken=page_token,
          pageSize=args.page_size).execute()
    except HttpError as e:
      print(e)
      sys.exit(1)

    pprint.pprint(response)

    page_token = response.get('nextPageToken')
    more_pages = bool(page_token)


if __name__ == '__main__':
  try:
    service = util.GetService(version='v1')
  except IOError as ex:
    print(f'Unable to create realtimebidding service - {ex}')
    print('Did you specify the key file in util.py?')
    sys.exit(1)

  parser = argparse.ArgumentParser(
      description=('Lists pretargeting configurations for the given bidder '
                   'account.'))
  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BIDDER_RESOURCE_ID,
      help=('The resource ID of the bidders resource under which the '
            'pretargeting configs were created by a buyer. This will be used '
            'to construct the parent used as a path parameter for the '
            'pretargetingConfigs.list request.'))
  # Optional fields.
  parser.add_argument(
      '-p', '--page_size', default=util.MAX_PAGE_SIZE,
      help=('The number of rows to return per page. The server may return '
            'fewer rows than specified.'))

  args = parser.parse_args()

  main(service, args)

