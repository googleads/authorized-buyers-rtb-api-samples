# Authorized Buyers Real-time Bidding API Ruby Samples

These samples demonstrate basic usage of the Authorized Buyers Real-time
Bidding API.

# Setup

To run these samples, you'll need to do the following:

1. If you haven't done so already, download and install the
  **Google API Ruby Client** by running the following command from the
  project's root directory:

```
bundle
```

1. Go to the [Google Developers Console](https://console.developers.google.com/)
   and open the menu. Click the `APIs & Services` option and then the
   `Credentials` tab. From here, you can either click the `Create Credentials`
   button to create a new Service Account for use with this project, or find an
   existing one under `Manage service accounts`.
1. In the `Manage service accounts` page, you can generate a new key for the
   Service Account by clicking the ellipses in the proper row and selecting
   `Create key` in the resulting menu. This library only supports the JSON key
   files, so select that as the `Key type`. Click the `create` button and the
   file will be generated and downloaded to your computer. Place the JSON file
   in the sample directory. You will need the Service Account email found on
   this page for the next step.
1. To authorize a Service Account to access your Authorized Buyers account via
   the API, go to the [Authorized Buyers UI](https://www.google.com/authorizedbuyers)
   and click the `Admin` drop-down menu in the in the navigation bar corner,
   and then click the `Account users` option. Click the `Link Service Account`
   button on this page and enter the Service Account's email to associate it
1. Open **util.rb**, and set the `KEY_FILE` field to the path of the JSON key
   file you downloaded earlier. If you placed it in the sample directory, this
   should just be the filename.
1. Before attempting to run any of the samples, you may update any fields
   containing a template value or set them via keyword arguments.

You should now be able to start any of the samples by navigating to their
directory and running them from the command line.

