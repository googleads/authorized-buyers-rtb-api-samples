# Authorized Buyers Real-Time Bidding API Java Samples

These samples demonstrate basic usage of the Authorized Buyers Real-Time
Bidding API.

The Authorized Buyers Real-Time Bidding API Java Client Library makes it
easier to write Java clients to programmatically access Authorized Buyer
accounts. The reference documentation for the Authorized Buyers Real-Time
Bidding API is available from
<https://developers.google.com/authorized-buyers/apis/realtimebidding/reference/rest>.

## Prerequisites
- [`Java 7+`](http://java.com)
- [`Maven`](http://maven.apache.org)

## Announcements and updates

For API and client library updates and news, please follow the Google Ads
Developers blog: <http://googleadsdeveloper.blogspot.com/>.

For questions and support visit our support forum:
<https://groups.google.com/forum/#!forum/authorized-buyers-api>.


## Running the examples

### Download the repository contents

To download the contents of the repository, you can use the command

```
git clone https://github.com/googleads/authorized-buyers-rtb-api-samples
```

or browse to <https://github.com/googleads/authorized-buyers-rtb-api-samples>
and download a zip.

### Getting started

This sample uses the OAuth 2.0 Service Account flow for security, which is the
recommended workflow when working with the Real-time Bidding API. You can learn
more about OAuth 2.0 and alternative workflows at:
<https://developers.google.com/accounts/docs/OAuth2>

If you don't already have a Service Account and corresponding JSON key file

 * Launch the Google Developers Console <https://console.developers.google.com>
 * Access the menu and click the **APIs and Services** option, and then the
   **Credentials** tab. From here, you can either click the
   **Create credentials** button to create a new Service Account for use with
   this project, or find an existing one under **Manage service accounts**.
 * In the `Manage service accounts` page, you can generate a new key for the
   Service Account by clicking the ellipses in the proper row and selecting
   `Create key` in the resulting menu. This library only supports the JSON key
   files, so select that as the `Key type`. Click the `create` button and the
   file will be generated and downloaded to your computer. Place the JSON file
   in the sample directory. You will need the Service Account email on this
   page for the next step.
 * To authorize a Service Account to access your Authorized Buyers account via
   the API, go to the
   [Authorized Buyers UI](https://www.google.com/authorizedbuyers)
   and click the `Admin` drop-down menu in the in the navigation bar corner, and
   then click the `Account users` option. Click the `Link Service Account`
   button on this page and enter the Service Account's email to associate it
   with your Authorized Buyers Account.
 * Open **Utils.java** and update the `JSON_FILE` field to represent the path to
   the JSON key file you downloaded earlier. If you placed it in the sample
   directory, this should just be the filename.
 * Before attempting to run any of the samples, you can update the default
   values of arguments used to construct the API request(s). Alternatively, you
   can also provide values for these fields at run-time as command-line
   arguments.

## Setup the environment
### Via the command line

Execute the following command:

```bash
$ mvn compile
```

**Note:** IDEs such as [IntelliJ IDEA](https://www.jetbrains.com/idea/)
automatically handle importing Maven dependencies and compilation.

## Running the Examples

If you are running the examples via the command line, you can run individual
examples with a command such as the following:

```bash
mvn exec:java -Dexec.mainClass=<CLASSPATH_TO_EXAMPLE>
-Dexec.args="<INSERT_ARGUMENTS_HERE>"
```

For example, running the bidders ListCreatives example would look like:

```bash
mvn exec:java -Dexec.mainClass=com.google.api.services.samples.authorizedbuyers.realtimebidding.v1.bidders.ListCreatives
-Dexec.args="--account_id <INSERT_ACCOUNT_ID>>"
```