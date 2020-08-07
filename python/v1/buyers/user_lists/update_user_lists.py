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

"""This example updates a userLists resource with the specified name.

Note that a user list's status can not be adjusted with the update method. See
close_user_lists.py and open_user_lists.py for examples of modifying the
state of existing user lists.
"""


import argparse
import datetime
import os
import pprint
import sys
import uuid

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError
import util


_USERLISTS_NAME_TEMPLATE = 'buyers/%s/userLists/%s'

DEFAULT_BUYER_RESOURCE_ID = 'ENTER_BUYER_RESOURCE_ID_HERE'
DEFAULT_USERLIST_RESOURCE_ID = 'ENTER_USERLIST_RESOURCE_ID_HERE'


def main(realtimebidding, args):
  account_id = args.account_id
  user_list_id = args.user_list_id

  user_list = {
      'displayName': args.display_name,
      'urlRestriction': {
          'url': args.url,
          'restrictionType': args.restriction_type,
          'startDate': {
              'year': args.start_year,
              'month': args.start_month,
              'day': args.start_day
          },
          'endDate': {
              'year': args.end_year,
              'month': args.end_month,
              'day': args.end_day
          }
      }
  }

  if args.description:
    user_list['description'] = args.description

  print(f'Updating userList "{user_list_id}" for buyer account ID '
        f'"{account_id}":')
  try:
    # Construct and execute the request.
    response = (realtimebidding.buyers().userLists().update(
                name=_USERLISTS_NAME_TEMPLATE % (account_id, user_list_id),
                body=user_list).execute())
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

  def valid_restriction_type(restriction_type):
    _VALID_RESTRICTION_TYPES = [
        'CONTAINS', 'EQUALS', 'STARTS_WITH', 'ENDS_WITH', 'DOES_NOT_EQUAL',
        'DOES_NOT_CONTAIN', 'DOES_NOT_START_WITH', 'DOES_NOT_END_WITH']

    if restriction_type.upper() in _VALID_RESTRICTION_TYPES:
      return restriction_type
    else:
      raise ValueError('Invalid URL restriction type specified. Must be one '
                       f'of: {_VALID_RESTRICTION_TYPES}')

  today = datetime.date.today()

  parser = argparse.ArgumentParser(
      description=('Updates the specified user list.')
  )

  # Required fields.
  parser.add_argument(
      '-a', '--account_id', default=DEFAULT_BUYER_RESOURCE_ID,
      help=('The resource ID of the buyers resource under which the '
            'userlist was created. This will be used to construct the '
            'name used as a path parameter for the userlists.update request.'))
  parser.add_argument(
      '-u', '--user_list_id', default=DEFAULT_USERLIST_RESOURCE_ID,
      help=('The resource ID of the buyers.userlists resource for which the '
            'userlist was created. This will be used to construct the '
            'name used as a path parameter for the userlists.update request.'))
  # Optional fields.
  parser.add_argument(
      '-n', '--display_name', default=f'Test UserList #{uuid.uuid4()}',
      help='The user-specified display name of the user list.')
  parser.add_argument(
      '-d', '--description', default=None,
      help='The user-specified description of the user list.')
  parser.add_argument(
      '--url', default='https://luxurymarscruises.com',
      help='The URL to use for applying the UrlRestriction on the user list.')
  parser.add_argument(
      '-r', '--restriction_type', default='EQUALS',
      type=valid_restriction_type,
      help=('The URL to use for applying the UrlRestriction on the user list. '
            'By default, this will be set to EQUALS. For more details on how '
            'to interpret the different restriction types, see: '
            'https://developers.google.com/authorized-buyers/apis/'
            'realtimebidding/reference/rest/v1/buyers.userLists?hl=en'
            '#UrlRestriction.FIELDS.restriction_type'))
  parser.add_argument(
      '--start_day', default=today.day, type=int,
      help=('Starting day of the month for the URL restriction. If default '
            'values are used, the startDate will be set to today.'))
  parser.add_argument(
      '--start_month', default=today.month, type=int,
      help=('Starting month (numeric) for the URL restriction. If default '
            'values are used, the startDate will be set to today.'))
  parser.add_argument(
      '--start_year', default=today.year, type=int,
      help=('Starting year for the URL restriction. If default values are '
            'used, the startDate will be set to today.'))
  parser.add_argument(
      '--end_day', default=today.day + 1, type=int,
      help=('Ending day of the month for the URL restriction. If default '
            'values are used, the endDate will be set to tomorrow.'))
  parser.add_argument(
      '--end_month', default=today.month, type=int,
      help=('Ending month (numeric) for the URL restriction. If default '
            'values are used, the endDate will be set to tomorrow.'))
  parser.add_argument(
      '--end_year', default=today.year, type=int,
      help=('Ending year for the URL restriction. If default values are used, '
            'the endDate will be set to tomorrow.'))

  args = parser.parse_args()

  main(service, args)

