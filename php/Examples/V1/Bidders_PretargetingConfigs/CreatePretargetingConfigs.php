<?php

/**
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

namespace Google\Ads\AuthorizedBuyers\RealTimeBidding\Examples\V1\Bidders_PretargetingConfigs;

use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\Config;
use Google_Service_RealTimeBidding_PretargetingConfig;
use Google_Service_RealTimeBidding_AppTargeting;
use Google_Service_RealTimeBidding_CreativeDimensions;
use Google_Service_RealTimeBidding_NumericTargetingDimension;
use Google_Service_RealTimeBidding_StringTargetingDimension;

/**
 * Creates a pretargeting configuration for the given bidder account ID.
 */
class CreatePretargetingConfigs extends BaseExample
{

    public function __construct($client)
    {
        $this->service = Config::getGoogleServiceRealTimeBidding($client);
    }

    /**
     * @see BaseExample::getInputParameters()
     */
    protected function getInputParameters()
    {
        return [
            [
                'name' => 'account_id',
                'display' => 'Account ID',
                'description' =>
                    'The resource ID of the bidders resource under which the pretargeting ' .
                    'configuration is to be created.',
                'required' => true
            ],
            [
                'name' => 'display_name',
                'display' => 'Display name',
                'description' =>
                    'The display name to associate with the new configuration. Must be unique ' .
                    'among all of a bidder\'s pretargeting configurations.',
                'required' => false,
                'default' => 'TEST_PRETARGETING_CONFIG_' . uniqid()
            ],
            [
                'name' => 'included_formats',
                'display' => 'Included formats',
                'description' =>
                    'Creative formats included by this configuration. Specify each value ' .
                    'separated by a comma. An unset value will not filter any bid requests ' .
                    'based on the format. Valid values include: HTML, NATIVE, and VAST.',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'included_geo_ids',
                'display' => 'Included geo IDs',
                'description' =>
                    'The geo IDs to include in targeting for this configuration. Specify each ' .
                    'ID separated by a comma. Valid geo IDs can be found in: ' .
                    'https://storage.googleapis.com/adx-rtb-dictionaries/geo-table.csv',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'excluded_geo_ids',
                'display' => 'Excluded geo IDs',
                'description' =>
                    'The geo IDs to exclude in targeting for this configuration. Specify each ' .
                    'ID separated by a comma. Valid geo IDs can be found in: ' .
                    'https://storage.googleapis.com/adx-rtb-dictionaries/geo-table.csv',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'included_user_list_ids',
                'display' => 'Included user list IDs',
                'description' =>
                    'The user list IDs to include in targeting for this configuration. Specify ' .
                    'each ID separated by a comma. Valid user list IDs would include any found ' .
                    'under the buyers.userLists resource for a given bidder account, or any ' .
                    'buyer accounts under it.',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'excluded_user_list_ids',
                'display' => 'Excluded user list IDs',
                'description' =>
                    'The user list IDs to exclude in targeting for this configuration. Specify ' .
                    'each ID separated by a comma. Valid user list IDs would include any found ' .
                    'under the buyers.userLists resource for a given bidder account, or any ' .
                    'buyer accounts under it.',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'interstitial_targeting',
                'display' => 'Interstitial targeting',
                'description' =>
                    'The interstitial targeting specified for this configuration. By default, ' .
                    'this will be set to ONLY_NON_INTERSTITIAL_REQUESTS. Valid values include: ' .
                    'ONLY_INTERSTITIAL_REQUESTS and ONLY_NON_INTERSTITIAL_REQUESTS.',
                'required' => false,
                'default' => 'ONLY_NON_INTERSTITIAL_REQUESTS'
            ],
            [
                'name' => 'allowed_user_targeting_modes',
                'display' => 'Allowed user targeting modes',
                'description' =>
                    'The targeting modes to include in targeting for this configuration. ' .
                    'Specify each value separated by a comma. Valid targeting modes include: ' .
                    'REMARKETING_ADS and INTEREST_BASED_TARGETING.',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'excluded_content_label_ids',
                'display' => 'Excluded content label IDs',
                'description' =>
                    'The sensitive content category IDs excluded in targeting for this ' .
                    'configuration. Specify each value separated by a comma. Valid sensitive ' .
                    'content category IDs can be found in: ' .
                    'https://storage.googleapis.com/adx-rtb-dictionaries/content-labels.txt',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'included_user_id_types',
                'display' => 'Included user ID types',
                'description' =>
                    'The user identifier types included in targeting for this configuration. ' .
                    'Specify each value separated by a comma. Valid values include: ' .
                    'HOSTED_MATCH_DATA, GOOGLE_COOKIE, and DEVICE_ID.',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'included_language_codes',
                'display' => 'Included language codes',
                'description' =>
                    'The languages represented by languages codes that are included in ' .
                    'targeting for this configuration. Specify each code separated by a comma. ' .
                    'Valid language codes can be found in: ' .
                    'https://developers.google.com/adwords/api/docs/appendix/languagecodes.',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'included_mobile_os_ids',
                'display' => 'Included mobile OS IDs',
                'description' =>
                    'The mobile OS IDs to include in targeting for this configuration. Specify ' .
                    'each value separated by a comma. Valid mobile OS IDs can be found in: ' .
                    'https://storage.googleapis.com/adx-rtb-dictionaries/mobile-os.csv',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'included_vertical_ids',
                'display' => 'Included vertical IDs',
                'description' =>
                    'The vertical IDs to include in targeting for this configuration. Specify ' .
                    'each ID separated by a comma. Valid vertical IDs can be found in: ' .
                    'https://developers.google.com/authorized-buyers/rtb/downloads/' .
                    'publisher-verticals',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'excluded_vertical_ids',
                'display' => 'Excluded vertical IDs',
                'description' =>
                    'The vertical IDs to exclude in targeting for this configuration. Specify ' .
                    'each ID separated by a comma. Valid vertical IDs can be found in: ' .
                    'https://developers.google.com/authorized-buyers/rtb/downloads/' .
                    'publisher-verticals',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'included_platforms',
                'display' => 'Included platforms',
                'description' =>
                    'The platforms to include in targeting for this configuration. Specify each ' .
                    'value separated by a comma. Valid values include: PERSONAL_COMPUTER, ' .
                    'PHONE, TABLET, and CONNECTED_TV.',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'included_creative_dimension_height',
                'display' => 'Included creative dimension height',
                'description' =>
                    'A creative dimension\'s height to be included in targeting for this ' .
                    'configuration. By default, this example will set the targeted height to ' .
                    '300. Note that while only a single set of dimensions are specified in this ' .
                    'sample, pretargeting configurations can target multiple creative dimensions.',
                'required' => false,
                'default' => 300
            ],
            [
                'name' => 'included_creative_dimension_width',
                'display' => 'Included creative dimension width',
                'description' =>
                    'A creative dimension\'s width to be included in targeting for this ' .
                    'configuration. By default, this example will set the targeted height to ' .
                    '250. Note that while only a single set of dimensions are specified in this ' .
                    'sample, pretargeting configurations can target multiple creative dimensions.',
                'required' => false,
                'default' => 250
            ],
            [
                'name' => 'included_environments',
                'display' => 'Included environments',
                'description' =>
                    'The environments to include in targeting for this configuration. Specify ' .
                    'each value separated by a comma. Valid values include: APP, and WEB.',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'web_targeting_mode',
                'display' => 'Web targeting mode',
                'description' =>
                    'The targeting mode for this configuration\'s web targeting. Valid values ' .
                    'include: INCLUSIVE, and EXCLUSIVE.',
                'required' => false,
                'default' => null
            ],
            [
                'name' => 'web_targeting_urls',
                'display' => 'Web targeting URLs',
                'description' =>
                    'The URLs specified for this configuration\'s web targeting, which allows ' .
                    'one to target a subset of site inventory. Specify each value separated by ' .
                    'a comma. Values specified must be valid URLs.',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'mobile_app_targeting_mode',
                'display' => 'Mobile app targeting mode',
                'description' =>
                    'The targeting mode for this configuration\'s mobile app targeting. Valid ' .
                    'values include: INCLUSIVE, and EXCLUSIVE.',
                'required' => false,
                'default' => null
            ],
            [
                'name' => 'mobile_app_targeting_app_ids',
                'display' => 'Mobile app targeting app IDs',
                'description' =>
                    'The mobile app IDs specified for this configuration\'s mobile app ' .
                    'targeting, which allows one to target a subset of mobile app inventory. ' .
                    'Specify each value separated by a comma. Values specified must be valid ' .
                    'mobile App IDs, as found on their respective app stores.',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'included_mobile_app_targeting_category_ids',
                'display' => 'Included mobile app targeting category IDs',
                'description' =>
                    'The mobile app category IDs to include in targeting for this ' .
                    'configuration. Specify each ID separated by a comma. Valid category IDs ' .
                    'can be found in: https://developers.google.com/adwords/api/docs/appendix/' .
                    'mobileappcategories.csv',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'excluded_mobile_app_targeting_category_ids',
                'display' => 'Excluded mobile app targeting category IDs',
                'description' =>
                    'The mobile app category IDs to exclude in targeting for this ' .
                    'configuration. Specify each ID separated by a comma. Valid category IDs ' .
                    'can be found in: https://developers.google.com/adwords/api/docs/appendix/' .
                    'mobileappcategories.csv',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'publisher_targeting_mode',
                'display' => 'Publisher targeting mode',
                'description' =>
                    'The targeting mode for this configuration\'s publisher targeting. Valid ' .
                    'values include: INCLUSIVE, and EXCLUSIVE.',
                'required' => false,
                'default' => null
            ],
            [
                'name' => 'publisher_ids',
                'display' => 'Publisher IDs',
                'description' =>
                    'The publisher IDs specified for this configuration\'s publisher targeting, ' .
                    'which allows one to target a subset of publisher inventory. Specify each ' .
                    'ID separated by a comma. Valid publisher IDs can be found in Real-time ' .
                    'Bidding bid requests, or alternatively in ads.txt / app-ads.txt. For more ' .
                    'information, see: https://iabtechlab.com/ads-txt/',
                'required' => false,
                'is_array' => true,
                'default' => []
            ],
            [
                'name' => 'minimum_viewability_decile',
                'display' => 'Minimum viewability decile',
                'description' =>
                    'The targeted minimum viewability decile, ranging from 0 - 10. A value of ' .
                    '"5" means that the configuration will only match adslots for which we ' .
                    'predict at least 50% viewability. Values > 10 will be rounded down to 10. ' .
                    'An unset value, or a value of "0", indicates that bid requests should be ' .
                    'sent regardless of viewability.',
                'required' => false,
                'default' => null
            ],
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;
        $parentName = "bidders/$values[account_id]";

        $geoTargeting = new Google_Service_RealTimeBidding_NumericTargetingDimension();
        $geoTargeting->includedIds = $values['included_geo_ids'];
        $geoTargeting->excludedIds = $values['excluded_geo_ids'];

        $userListTargeting = new Google_Service_RealTimeBidding_NumericTargetingDimension();
        $userListTargeting->includedIds = $values['included_user_list_ids'];
        $userListTargeting->excludedIds = $values['excluded_user_list_ids'];

        $verticalTargeting = new Google_Service_RealTimeBidding_NumericTargetingDimension();
        $verticalTargeting->includedIds = $values['included_vertical_ids'];
        $verticalTargeting->excludedIds = $values['excluded_vertical_ids'];

        $includedCreativeDimensions = new Google_Service_RealTimeBidding_CreativeDimensions();
        $includedCreativeDimensions->height = $values['included_creative_dimension_height'];
        $includedCreativeDimensions->width = $values['included_creative_dimension_width'];

        $webTargeting = new Google_Service_RealTimeBidding_StringTargetingDimension();
        $webTargeting->targetingMode = $values['web_targeting_mode'];
        $webTargeting->values = $values['web_targeting_urls'];

        $mobileAppTargeting = new Google_Service_RealTimeBidding_StringTargetingDimension();
        $mobileAppTargeting->targetingMode = $values['mobile_app_targeting_mode'];
        $mobileAppTargeting->values = $values['mobile_app_targeting_app_ids'];

        $mobileAppCategoryTargeting = new Google_Service_RealTimeBidding_NumericTargetingDimension();
        $mobileAppCategoryTargeting->includedIds = $values['included_mobile_app_targeting_category_ids'];
        $mobileAppCategoryTargeting->excludedIds = $values['excluded_mobile_app_targeting_category_ids'];

        $appTargeting = new Google_Service_RealTimeBidding_AppTargeting();
        $appTargeting->mobileAppTargeting = $mobileAppTargeting;
        $appTargeting->mobileAppCategoryTargeting = $mobileAppCategoryTargeting;

        $publisherTargeting = new Google_Service_RealTimeBidding_StringTargetingDimension();
        $publisherTargeting->targetingMode = $values['publisher_targeting_mode'];
        $publisherTargeting->values = $values['publisher_ids'];

        $newPretargetingConfig = new Google_Service_RealTimeBidding_PretargetingConfig();
        $newPretargetingConfig->displayName = $values['display_name'];
        $newPretargetingConfig->includedFormats = $values['included_formats'];
        $newPretargetingConfig->geoTargeting = $geoTargeting;
        $newPretargetingConfig->userListTargeting = $userListTargeting;
        $newPretargetingConfig->interstitialTargeting = $values['interstitial_targeting'];
        $newPretargetingConfig->allowedUserTargetingModes = $values['allowed_user_targeting_modes'];
        $newPretargetingConfig->excludedContentLabelIds = $values['excluded_content_label_ids'];
        $newPretargetingConfig->includedUserIdTypes = $values['included_user_id_types'];
        $newPretargetingConfig->includedLanguages = $values['included_languages'];
        $newPretargetingConfig->includedMobileOperatingSystemIds = $values['included_mobile_os_ids'];
        $newPretargetingConfig->verticalTargeting = $verticalTargeting;
        $newPretargetingConfig->includedPlatforms = $values['included_platforms'];
        $newPretargetingConfig->includedCreativeDimensions = [$includedCreativeDimensions];
        $newPretargetingConfig->includedEnvironments = $values['included_environments'];
        $newPretargetingConfig->webTargeting = $webTargeting;
        $newPretargetingConfig->appTargeting = $appTargeting;
        $newPretargetingConfig->publisherTargeting = $publisherTargeting;
        $newPretargetingConfig->minimumViewabilityDecile = $values['minimum_viewability_decile'];

        print "<h2>Creating pretargeting configuration for '$parentName':</h2>";
        $result = $this->service->bidders_pretargetingConfigs->create($parentName, $newPretargetingConfig);
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Create Pretargeting Configuration';
    }
}
