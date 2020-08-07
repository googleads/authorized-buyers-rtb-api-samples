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
# Enables monitoring of changes of a creative status for a given bidder.
#
# Watched creatives will have changes to their status posted to Google Cloud Pub/Sub. For more details on Google Cloud
# Pub/Sub, see: https://cloud.google.com/pubsub/docs
#
# For an example of pulling creative status changes from the Google Cloud Pub/Sub subscription, see
# pull_watched_creatives_subscription.rb.

require 'optparse'

require_relative '../../../util'


def watch_creatives(realtimebidding, options)
  parent = "bidders/#{options[:account_id]}"

  puts "Watching creative status changes for bidder account '#{parent}':"

  response = realtimebidding.watch_creatives(parent)

  puts "\tTopic: #{response.topic}"
  puts "\tSubscription: #{response.subscription}"
end


if __FILE__ == $0
  begin
    # Retrieve the service used to make API requests.
    service = get_service()
  rescue ArgumentError => e
    raise 'Unable to create service, with error message: #{e.message}'
  rescue Signet::AuthorizationError => e
    raise 'Unable to create service, was the KEY_FILE in util.rb set? Error message: #{e.message}'
  end

  # Set options and default values for fields used in this example.
  options = [
    Option.new(
      'account_id',
      'The resource ID of the bidder account. This will be used to construct the parent used as a path parameter for '\
      'the creatives.watch request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    watch_creatives(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
