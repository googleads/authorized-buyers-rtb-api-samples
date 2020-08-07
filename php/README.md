# Authorized Buyers Real-Time Bidding API PHP Samples

These samples demonstrate basic usage of the Authorized Buyers Real-Time
Bidding API.

## Setup

To run these samples, you'll need to do the following:

1.  If you haven't done so already, download and install
    [google-api-php-client](https://github.com/google/google-api-php-client).

1.  Setup a service account:

    *   **If you do not have a service account:**

        *   Launch the [Google Developers
            Console](https://console.developers.google.com)
        *   select a project
        *   Open the menu (icon in the upper-left corner of page)
        *   Click **APIs and Services tab**
        *   Click the **Credentials** tab
        *   Click **Create credentials**
        *   Select **Service account key** from the drop-down menu
        *   Under **Service account** select the **New service account** option
        *   under **Key type** select the **JSON** option for use with these
            samples
        *   Click the **Create** button
        *   The downloaded JSON file is used in Step 5

1.  Allow your service account to access your data:

    *   Go to the [Authorized Buyers UI](https://www.google.com/authorizedbuyers)
    *   In the left navigation menu, click the **Admin** drop-down menu.
    *   Click the **Account users** menu item.
    *   Click the **Link service account** button
    *   Enter the service account email in the dialog to associate it with your
        Authorized Buyers Account

1.  Open **index.php** and update the template include path per the instructions
    at [google-api-php-client](https://github.com/google/google-api-php-client).

    *   If you are using composer: `require_once
        '/path/to/your-project/vendor/autoload.php';`

    *   If you are using the zip: `require_once
        '/path/to/google-api-php-client/vendor/autoload.php';`

1.  Update the `key_file_location` field in index.php to match the path to the
    .json file from step 2

You should now be able to start the sample by running the following command in
the project directory:

```
$ php -S localhost:8080
```

To run the sample, visit localhost:8080 in your browser.

