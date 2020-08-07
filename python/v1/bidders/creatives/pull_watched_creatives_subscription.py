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

"""Pulls creative status updates from a Google Cloud Pub/Sub subscription.

Note that messages do not expire until they are acknowledged; set the
acknowledged argument to True to acknowledge receiving all messages sent in the
response.

To learn more about Google Cloud Pub/Sub, read the developer documentation:
https://cloud.google.com/pubsub/docs/overview
https://cloud.google.com/pubsub/docs/reference/rest/v1/projects.subscriptions/list
https://cloud.google.com/pubsub/docs/reference/rest/v1/projects.subscriptions/acknowledge
"""


import argparse
import base64
import json
import os
import pprint
import sys

sys.path.insert(0, os.path.abspath('../../..'))

from googleapiclient.errors import HttpError
import util


def main(pubsub, args):
  subscription_name = args.subscription_name

  print(f'Retrieving messages from subscription: "{subscription_name}"')

  body = {'maxMessages': args.max_messages}

  subscriptions = pubsub.projects().subscriptions().pull(
      subscription=subscription_name, body=body).execute()

  pprint.pprint(subscriptions)

  if 'receivedMessages' in subscriptions:
    ack_ids = []

    for received_message in subscriptions['receivedMessages']:
      ack_ids.append(received_message['ackId'])
      message = received_message['message']
      account_id = message['attributes']['accountId']
      creative_id = message['attributes']['creativeId']

      print(f'* Creative found for buyer account ID "{account_id}" with '
            f'creative ID "{creative_id}" has been updated with the following '
            'creative status:')
      creative_serving_decision = parse_creative_serving_decision(
          message['data'])
      pprint.pprint(creative_serving_decision)
      print()

    if args.acknowledge:
      body = {'ackIds': ack_ids}

      print(f'Acknowledging all {len(ack_ids)} messages pulled from the '
            'subscription.')

      pubsub.projects().subscriptions().acknowledge(
          subscription=subscription_name, body=body).execute()
  else:
    print('No messages received from the subscription.')


def parse_creative_serving_decision(data):
  """Parses the Creative Serving Decision from the Cloud Pub/Sub response.

  Args:
    data: a base64-encoded JSON string representing a creative's
          CreativeServingDevision.

  Returns:
    A JSON representation of the creative's CreativeServingDecision.
  """
  return json.loads(base64.b64decode(data))


if __name__ == '__main__':
  try:
    service = util.GetCloudPubSubService(version='v1')
  except IOError as ex:
    print(f'Unable to create pubsub service - {ex}')
    print('Did you specify the key file in util.py?')
    sys.exit(1)

  parser = argparse.ArgumentParser(
      description=('Pulls creative status changes (if any) from a specified '
                   'Google Cloud Pub/Sub subscription.'))

  # Required fields.
  parser.add_argument(
      '-s', '--subscription_name', required=True, type=str,
      help=('The Google Cloud Pub/Sub subscription to be pulled. This value '
            'would be returned in the response from the '
            'bidders.creatives.watch method, and should be provided as-is in '
            'the form: "projects/realtimebidding-pubsub/subscriptions/'
            '{subscription_id}"))'))
  parser.add_argument(
      '-m', '--max_messages', default=100, type=int,
      help='The maximum number of messages to be returned in a single pull.')
  parser.add_argument(
      '-a', '--acknowledge', default=False, type=bool,
      help=('Whether to acknowledge the messages pulled from the '
            'subscription. Acknowledged messages won\'t appear in subsequent '
            'responses to pulls from the subscription.'))

  args = parser.parse_args()

  main(service, args)

