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
# Activates a specified pretargeting configuration.

require 'optparse'

require_relative '../../../util'


def activate_pretargeting_configs(realtimebidding, options)
  name = "bidders/#{options[:account_id]}/pretargetingConfigs/#{options[:pretargeting_config_id]}"

  puts "Activating a pretargeting configuraton with name: '#{name}'"

  pretargeting_config = realtimebidding.activate_pretargeting_config(name)
  print_pretargeting_config(pretargeting_config)
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
      'The resource ID of the bidders resource under which the pretargeting configurations were created, This will '\
      'be used to construct the parent used as a path parameter for the pretargetingConfigs.activate request.',
      type: Integer, short_alias: 'a', required: true, default_value: nil
    ),
    Option.new(
      'pretargeting_config_id', 'The resource ID of the pretargeting configuration that is being activated.',
      type: Integer, short_alias: 'p', required: true, default_value: nil
    ),
  ]

  # Parse options.
  parser = Parser.new(options)
  opts = parser.parse(ARGV)

  begin
    activate_pretargeting_configs(service, opts)
  rescue Google::Apis::ServerError => e
    raise "The following server error occured:\n#{e.message}"
  rescue Google::Apis::ClientError => e
    raise "Invalid client request:\n#{e.message}"
  rescue Google::Apis::AuthorizationError => e
    raise "Authorization error occured:\n#{e.message}"
  end
end
