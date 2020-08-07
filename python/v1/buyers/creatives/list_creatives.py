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

"""Lists creatives for the given buyer account ID."""


import argparse
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_BUYER_NAME_TEMPLATE = 'buyers/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'


def main(realtimebidding, args):
  account_id = args.account_id
  list_filter = args.filter
  view = args.view
  page_size = args.page_size

  page_token = None
  more_pages = True

  print(f'Listing creatives for buyer account: "{account_id}".')
  while more_pages:
    try:
      # Construct and execute the request.
      response = realtimebidding.buyers().creatives().list(
          parent=_BUYER_NAME_TEMPLATE % account_id, pageToken=page_token,
          filter=list_filter, view=view, pageSize=page_size).execute()
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

  default_list_filter = (
      'creativeServingDecision.openAuctionServingStatus.status=APPROVED '
      'AND creativeFormat=HTML')

  parser = argparse.ArgumentParser(
      description=('Lists creatives for the given buyer account.'))
  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
      help=('The resource ID of the buyers resource under which the '
            'creatives were created. This will be used to construct the '
            'parent used as a path parameter for the creatives.list request.'))
  parser.add_argument(
      '-p', '--page_size', default=util.MAX_PAGE_SIZE,
      help=('The number of rows to return per page. The server may return '
            'fewer rows than specified.'))
  # Optional fields.
  parser.add_argument(
      '-f', '--filter', default=default_list_filter,
      help=('Query string to filter creatives. If no filter is specified, all '
            'active creatives will be returned. To demonstrate usage, the '
            'default behavior of this sample is to filter such that only '
            'approved HTML snippet creatives are returned.'))
  parser.add_argument(
      '-v', '--view', default='FULL',
      help=('Controls the amount of information included in the response. By '
            'default, the creatives.list method only includes '
            'creativeServingDecision. This sample configures the view to '
            'return the full contents of the creatives by setting this to '
            '"FULL".'))

  args = parser.parse_args()

  main(service, args)

