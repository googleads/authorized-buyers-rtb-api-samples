# Authorized Buyers Real-time Bidding API .NET Samples

These samples demonstrate how to access the Real-time Bidding API with the
[Google APIs .NET client library](https://github.com/googleapis/google-api-dotnet-client). 

The client library enables developers to more easily write clients in .NET used
to programmatically access Authorized Buyers accounts. To learn more about the
Real-time Bidding API, see the [documentation](https://developers.google.com/authorized-buyers/apis/realtimebidding/reference/rest).

## Features

- Support for .NET Core 3.0 and above.

## Announcements and updates

For the latest news concerning the Authorized Buyers Real-time Bidding API, see
the [release notes](https://developers.google.com/authorized-buyers/apis/relnotes#real-time-bidding-api)
and follow the [Google Ads Developer Blog](http://googleadsdeveloper.blogspot.com/).

For any feedback or questions concerning the API, you may contact us via our
[support forum](https://groups.google.com/forum/#!forum/google-doubleclick-ad-exchange-buyer-api).

## Running the examples

### Install dependencies

If you have not done so already, download and install the [.NET Core SDK](https://dotnet.microsoft.com/download)
on your preferred development environment.

### Configure your OAuth 2.0 credentials

The Real-time Bidding API uses OAuth 2.0 to authorize users for access to
Authorized Buyers accounts. You can learn more about the different OAuth 2.0
flows in the [OAuth 2.0 documentation](https://developers.google.com/accounts/docs/OAuth2).

It is recommended that you use the [Service Account OAuth 2.0 flow](https://developers.google.com/identity/protocols/oauth2/service-account)
when accessing the Real-time Bidding API. Consequently, these samples will only
demonstrate how to use this flow.

If you don't already have a Service Account and corresponding JSON key file

 * Go to the [Google Developers Console](https://console.developers.google.com),
   and navigate to the Google Developer Project you intend to create the
   Service Account under. Alternatively, if you don't yet have a Google
   Developer Project, create one now.
 * Click **Credentials** menu option.
 * Click the button labeled **+CREATE CREDENTIALS**, and select
   **Service account** from the drop-down menu.
 * On the next page, enter a name, ID, and description for the new Service
   Account. Then click the **Create** button.
 * Optionally configure a role for the Service Account and click **CONTINUE**.
 * Optionally grant users or groups access to the service account and click
   **DONE**.
 * You should now see the newly created Service account displayed in the
   Service Account table. Under the **Actions** column, click the elipses to
   open a drop-down menu and select **Create key**. In the resulting pop-up
   dialog, select the **JSON** key type. The key file will be downloaded. Note
   that beyond the purpose of running these samples, steps should be taken to
   secure access to the key file. Once you are finished running these samples,
   it is recommended that you delete it.
 * In order for the Service Account to be used to programmatically access an
   Authorized Buyers account, it must be linked to it. In order to do this, go
   to the [Authorized Buyers UI](https://google.com/authorizedbuyers).
 * Expand the **Admin** menu in the side-panel and click **Account Users**.
 * Click the **Link service account** button, and input the Service Account's
   email address. In the JSON key file downloaded earlier, this can be found
   under **client_email**. Click the **SAVE** button.
 * Set the path to the downloaded JSON file as the **ServiceKeyFilePath** value
   in **Utilities.cs**.

### Run the examples

You can run examples by navigating to the **CSharp** directory and running a
command such as the following on the command-line:

```
dotnet run --framework netcoreapp3.0 -- --example path.to.Example [--ex_arg1 ...]
```

For example, you can run an example that lists a buyer account's creatives with
a command such as the following:

```
dotnet run --framework netcoreapp3.0 -- --example v1.Buyers.Creatives.ListCreatives --account_id <ACCOUNT_ID>
```

**Note:** If you are unsure of the arguments for a given example, run it while
providing the **--help** argument for a summary.
