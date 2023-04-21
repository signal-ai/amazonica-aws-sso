# Amazonica AWS SSO

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/signal-ai/amazonica-aws-sso/tree/main.svg?style=shield&circle-token=ffb6546d8fa32712fcd2b69042dbe409ef81decb)](https://dl.circleci.com/status-badge/redirect/gh/signal-ai/amazonica-aws-sso/tree/main)
[![Clojars Project](https://img.shields.io/clojars/v/com.signal-ai/amazonica-aws-sso.svg)](https://clojars.org/com.signal-ai/amazonica-aws-sso)

A Clojure library which adds support for AWS SSO to [amazonica](https://github.com/mcohen01/amazonica).

This is done by shimming [`software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider`](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/auth/credentials/ProfileCredentialsProvider.html) from the [AWS SDK V2](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html) to [`com.amazonaws.auth.AWSCredentialsProvider`](https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/auth/AWSCredentialsProvider.html).

This approach was taken from this github issue response: <https://github.com/aws/aws-sdk-java/issues/803#issuecomment-593530484>.

## Usage

Add the libary to your project from [clojars](https://clojars.org/com.signal-ai/amazonica-aws-sso):

```clj
[com.signal-ai/amazonica-aws-sso "<version>"]
```

The library requires [amazonica](https://github.com/mcohen01/amazonica) to be available to your project.

```clj
(require '[signal.amazonica-aws-sso :as amazonica-aws-sso]
         '[amazonica.aws.securitytoken :as sts])

;; Use SSO credentials for a single call
(amazonica-aws-sso/with-sso-credential
  (sts/get-caller-identity))

;; Use SSO credentials for all subsequent calls
(amazonica-aws-sso/init!)
(sts/get-caller-identity)

;; Reset amazonica to use it's default AWS credentials provider
(amazonica-aws-sso/reset!)
```

## Testing

This is a pain to test on CI as it requires an AWS Profile to be set up using SSO, and no other providers in the AWS credentials provider chain to be configured. As such, it's recommended to just test locally.

```shell
make test
```

this assumes you have a valid AWS Credentials profile set up using SSO, e.g. in `~/.aws/config`:

```aws-config
[profile test-sso]
sso_start_url = ...
sso_region = eu-west-1
sso_registration_scopes = sso:account:access
sso_account_id = ...
sso_role_name = ...
region = eu-west-1

[profile test-sso.Access]
sso_start_url = ...
sso_region = eu-west-1
sso_account_name = test-sso
sso_account_id = ...
sso_role_name = ...
region = eu-west-1
credential_process = aws-sso-util credential-process --profile test-sso.Access
sso_auto_populated = true
```

At signal you can use the `signal-prod` profile for this, else you should set one up and run `AWS_PROFILE=test-sso lein test`.

Tests are only configured to use `sts/get-caller-identity` which is a non-mutating API call.

## Publishing

Push to `main` to publish a new version.
