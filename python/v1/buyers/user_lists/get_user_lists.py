#!/usr/bin/python
#
# Copyright 2019 Google Inc. All Rights Reserved.
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

"""This example gets a single userLists resource via its name."""


import argparse
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError

import util


_USERLISTS_NAME_TEMPLATE = 'buyers/%s/userLists/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'
DEFAULT_USERLIST_RESOURCE_ID = 'ENTER_USERLIST_RESOURCE_ID_HERE'


def main(realtimebidding, args):
  account_id = args.account_id
  user_list_id = args.user_list_id

  print(f'Get userlist "{user_list_id}" for account "{account_id}":')
  try:
    # Construct and execute the request.
    response = realtimebidding.buyers().userLists().get(
        name=_USERLISTS_NAME_TEMPLATE % (account_id, user_list_id)).execute()
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
      description=('Get a user list for the given buyer account ID and user '
                   'list ID.'))
  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
      help=('The resource ID of the buyers resource under which the '
            'userList was created. This will be used to construct the '
            'name used as a path parameter for the userLists.get request.'))
  parser.add_argument(
      '-u', '--user_list_id', default=DEFAULT_USERLIST_RESOURCE_ID,
      help=('The resource ID of the buyers.userLists resource for which the '
            'user list was created. This will be used to construct the '
            'name used as a path parameter for the userLists.get request.'))

  args = parser.parse_args()

  main(service, args)

