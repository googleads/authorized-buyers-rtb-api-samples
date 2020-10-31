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
# Sample application that authenticates and makes an API request.

require 'google/apis/realtimebidding_v1'
require 'googleauth/service_account'

# You can download the JSON keyfile used for authentication from the Google
# Developers Console.
KEY_FILE = 'path_to_key'  # Path to JSON file containing your private key.

# Name of the buyer resource for which the API call is being made.
BUYER_NAME = 'insert_buyer_resource_name'


def first_api_request()
  # Create credentials using the JSON key file.
  auth_options = {
    :json_key_io => File.open(KEY_FILE, "r"),
    :scope => 'https://www.googleapis.com/auth/realtime-bidding'
  }

  oauth_credentials = Google::Auth::ServiceAccountCredentials.make_creds(
    options=auth_options
  )

  # Create the service and set credentials
  realtimebidding = (
    Google::Apis::RealtimebiddingV1::RealtimeBiddingService.new
  )
  realtimebidding.authorization = oauth_credentials
  realtimebidding.authorization.fetch_access_token!

  begin
    # Call the buyers.creatives.list method to get list of creatives for given buyer.
    creatives_list = realtimebidding.list_buyer_creatives(
        BUYER_NAME, view: 'FULL')

    if creatives_list.creatives.any?
      puts "Found the following creatives for buyer '%s':" % BUYER_NAME
      creatives_list.creatives.each do |creative|
        puts "* Creative name: #{creative.name}"
      end
    else
      puts "No creatives were found that were associated with buyer '%s'" % BUYER_NAME
    end
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n%s" % e.message
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n%s" % e.message
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n%s" % e.message
  end
end

if __FILE__ == $0
  begin
    first_api_request()
  end
end

