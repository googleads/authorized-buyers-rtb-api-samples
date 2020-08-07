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

namespace Google\Ads\AuthorizedBuyers\RealTimeBidding\Examples\V1\Buyers_Creatives;

use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\BaseExample;
use Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil\Config;
use Google_Service_RealTimeBidding_Creative;
use Google_Service_RealTimeBidding_Image;
use Google_Service_RealTimeBidding_NativeContent;

/**
 * This example illustrates how to create native creatives for a given buyer account.
 */
class CreateNativeCreatives extends BaseExample
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
                    'The resource ID of the buyers resource under which the creative is to be ' .
                    'created.',
                'required' => true
            ],
            [
                'name' => 'advertiser_name',
                'display' => 'Advertiser name',
                'description' => 'The name of the company being advertised in the creative.',
                'required' => false,
                'default' => 'Test'
            ],
            [
                'name' => 'creative_id',
                'display' => 'Creative ID',
                'description' =>
                    'The user-specified creative ID. The maximum length of the creative ID is ' .
                    '128 bytes',
                'required' => false,
                'default' => 'Native_Creative_' . uniqid()
            ],
            [
                'name' => 'declared_attributes',
                'display' => 'Declared attributes',
                'description' =>
                    'The creative attributes being declared. Specify each attribute separated ' .
                    'by a comma.',
                'required' => false,
                'is_array' => true,
                'default' => ['NATIVE_ELIGIBILITY_ELIGIBLE']
            ],
            [
                'name' => 'declared_click_urls',
                'display' => 'Declared click URLs',
                'description' =>
                    'The click-through URLs being declared. Specify each URL separated by a ' .
                    'comma.',
                'required' => false,
                'is_array' => true,
                'default' => ['http://test.com']
            ],
            [
                'name' => 'declared_restricted_categories',
                'display' => 'Declared restricted categories',
                'description' =>
                    'The restricted categories being declared. Specify each category separated ' .
                    'by a comma.',
                'required' => false,
                'is_array' => true
            ],
            [
                'name' => 'declared_vendor_ids',
                'display' => 'Declared vendor IDs',
                'description' =>
                    'The vendor IDs being declared. Specify each ID separated by a comma.',
                'required' => false,
                'is_array' => true
            ],
            [
                'name' => 'native_headline',
                'display' => 'Native ad headline',
                'description' => 'A short title for the ad.',
                'required' => false,
                'default' => 'Luxury Mars Cruises'
            ],
            [
                'name' => 'native_body',
                'display' => 'Native ad body',
                'description' => 'A long description of the ad.',
                'required' => false,
                'default' => 'Visit the planet in a luxury spaceship.'
            ],
            [
                'name' => 'native_call_to_action',
                'display' => 'Native ad call to action',
                'description' => 'A label for the button that the user is supposed to click.',
                'required' => false,
                'default' => 'Book today'
            ],
            [
                'name' => 'native_advertiser_name',
                'display' => 'Native ad advertiser name',
                'description' =>
                    'The name of the advertiser or sponsor, to be displayed in the ad creative.',
                'required' => false,
                'default' => 'Galactic Luxury Cruises'
            ],
            [
                'name' => 'native_image_url',
                'display' => 'Native ad image url',
                'description' => 'The URL of the large image to be included in the native ad.',
                'required' => false,
                'default' => 'https://native.test.com/image?id=123456'
            ],
            [
                'name' => 'native_image_height',
                'display' => 'Native ad image height',
                'description' => 'The height in pixels of the native ad\'s large image.',
                'required' => false,
                'default' => 627
            ],
            [
                'name' => 'native_image_width',
                'display' => 'Native ad image width',
                'description' => 'The width in pixels of the native ad\'s large image.',
                'required' => false,
                'default' => 1200
            ],
            [
                'name' => 'native_logo_url',
                'display' => 'Native ad logo url',
                'description' => 'The URL of a smaller image to be included in the native ad.',
                'required' => false,
                'default' => 'https://native.test.com/logo?id=123456'
            ],
            [
                'name' => 'native_logo_height',
                'display' => 'Native ad logo height',
                'description' => 'The height in pixels of the native ad\'s smaller image.',
                'required' => false,
                'default' => 100
            ],
            [
                'name' => 'native_logo_width',
                'display' => 'Native ad logo width',
                'description' => 'The width in pixels of the native ad\'s smaller image.',
                'required' => false,
                'default' => 100
            ],
            [
                'name' => 'native_click_link_url',
                'display' => 'Native ad click link URL',
                'description' =>
                    'The URL that the browser/SDK will load when the user clicks ' .
                    'the ad.',
                'required' => false,
                'default' => 'https://www.google.com'
            ],
            [
                'name' => 'native_click_tracking_url',
                'display' => 'Native ad click tracking URL',
                'description' => 'The URL to use for click tracking.',
                'required' => false,
                'default' => 'https://native.test.com/click?id=123456'
            ]
        ];
    }

    /**
     * @see BaseExample::run()
     */
    public function run()
    {
        $values = $this->formValues;
        $parentName = "buyers/$values[account_id]";

        $image = new Google_Service_RealTimeBidding_Image();
        $image->url = $values['native_image_url'];
        $image->height = $values['native_image_height'];
        $image->width = $values['native_image_width'];

        $logo = new Google_Service_RealTimeBidding_Image();
        $logo->url = $values['native_logo_url'];
        $logo->height = $values['native_logo_height'];
        $logo->width = $values['native_logo_width'];

        $native = new Google_Service_RealTimeBidding_NativeContent();
        $native->headline = $values['native_headline'];
        $native->body = $values['native_body'];
        $native->callToAction = $values['native_call_to_action'];
        $native->advertiserName = $values['native_advertiser_name'];
        $native->setImage($image);
        $native->setLogo($logo);

        $newCreative = new Google_Service_RealTimeBidding_Creative();
        $newCreative->advertiserName = $values['advertiser_name'];
        $newCreative->creativeId = $values['creative_id'];
        $newCreative->declaredAttributes = $values['declared_attributes'];
        $newCreative->declaredClickThroughUrls = $values['declared_click_urls'];
        $newCreative->declaredRestrictedCategories = $values['declared_restricted_categories'];
        $newCreative->declaredVendorIds = $values['declared_vendor_ids'];
        $newCreative->setNative($native);

        print "<h2>Creating Creative for '$parentName':</h2>";
        $result = $this->service->buyers_creatives->create($parentName, $newCreative);
        $this->printResult($result);
    }

    /**
     * @see BaseExample::getName()
     */
    public function getName()
    {
        return 'Create Buyer Native Creative';
    }
}
