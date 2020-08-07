#!/usr/bin/env ruby
# Encoding: utf-8
#
# Copyright:: Copyright 2020 Google LLC
#
# License:: Licensed under the Apache License, Version 2.0 (the "License");
#           you may not use this file except in compliance with the License.
#           You may obtain a copy of the License at
#
#           http://www.apache.org/licenses/LICENSE-2.0
#
#           Unless required by applicable law or agreed to in writing, software
#           distributed under the License is distributed on an "AS IS" BASIS,
#           WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
#           implied.
#           See the License for the specific language governing permissions and
#           limitations under the License.
#
# Pulls creative status updates from a Google Cloud Pub/Sub subscription.
#
# Note that messages do not expire until they are acknowledged; set the acknowledged argument to true to acknowledge
# receiving all messages sent in the response.
#
# To learn more about Google Cloud Pub/Sub, read the developer documentation:
# https://cloud.google.com/pubsub/docs/overview
# https://cloud.google.com/pubsub/docs/reference/rest/v1/projects.subscriptions/list
# https://cloud.google.com/pubsub/docs/reference/rest/v1/projects.subscriptions/acknowledge

require 'base64'
require 'json'
require 'optparse'

require_relative '../../../util'


def pull_watched_creatives_subscription(pubsub, options)
  subscription_name = options[:subscription_name]
  pull_request_object = Google::Apis::PubsubV1::PullRequest.new(max_messages: options[:max_messages])

  puts "Retrieving messages from subscription '#{subscription_name}':"

  response = pubsub.pull_subscription(subscription_name, pull_request_object)

  unless response.received_messages.nil?
    ack_ids = []
    response.received_messages.each do |received_message|
      message = received_message.message
      account_id = message.attributes['accountId']
      creative_id = message.attributes['creativeId']
      ack_ids << received_message.ack_id
      creative_serving_decision = JSON.parse(message.data)

      puts "* Creative found for buyer account ID '#{account_id}' with creative ID '#{creative_id}' has been "\
           "updated with the following creative status:"
      puts JSON.pretty_generate(creative_serving_decision)

      if options[:acknowledge]
        ack_req_object = Google::Apis::PubsubV1::AcknowledgeRequest.new(ack_ids: ack_ids)

        puts "Acknowledging all #{ack_ids.length} messages pulled from the subscription."

        pubsub.acknowledge_subscription(subscription_name, ack_req_object)
      end
    end
  else
    puts 'No messages received from the subscription.'
  end
end


if __FILE__ == $0
  begin
    # Retrieve the service used to make API requests.
    service = get_cloud_pub_sub_service()
  rescue ArgumentError => e
    raise 'Unable to create service, with error message: #{e.message}'
  rescue Signet::AuthorizationError => e
    raise ('Unable to create service, was the KEY_FILE in util.rb set? ' +
           'Error message: #{e.message}')
  end

  # Set options and default values for fields used in this example.
  options = [
    Option.new(
      'subscription_name',
      'The Google Cloud Pub/Sub subscription to be pulled. This value would be returned in the response from the '\
      'bidders.creatives.watch method, and should be provided as-is in the form:'\
      'projects/realtimebidding-pubsub/subscriptions/{subscription_id}',
      short_alias: 's', required: true, default_value: nil
    ),
    Option.new(
      'max_messages',
      'The maximum number of messages to be returned in a single pull.',
      type: Integer, short_alias: 'm', required: false, default_value: 100
    ),
    Option.new(
      'acknowledge',
      'Whether to acknowledge the messages pulled from the subscription. Acknowledged messages won\'t appear in '\
      'subsequent responses to pulls from the subscription. By default, this sample will not acknowledge messages '\
      'pulled from the subscription.',
      type: FalseClass, short_alias: 'a', required: false, default_value: false
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    pull_watched_creatives_subscription(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
