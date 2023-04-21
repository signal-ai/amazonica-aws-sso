package com.signalai.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;

// Compatibility with AWS SSO by using the V2 AWS SDK
// https://github.com/aws/aws-sdk-java/issues/803#issuecomment-593530484
public class SSOCompatibleProfileCredentialsProvider implements AWSCredentialsProvider {
    private final ProfileCredentialsProvider delegate;

    public SSOCompatibleProfileCredentialsProvider() {
        this.delegate = ProfileCredentialsProvider.create();
    }

    public SSOCompatibleProfileCredentialsProvider(String profileName) {
        this.delegate = ProfileCredentialsProvider.create(profileName);
    }

    @Override
    public AWSCredentials getCredentials() {
        AwsCredentials credentials = delegate.resolveCredentials();

        if (credentials instanceof AwsSessionCredentials) {
            AwsSessionCredentials sessionCredentials = (AwsSessionCredentials) credentials;
            return new BasicSessionCredentials(sessionCredentials.accessKeyId(),
                                               sessionCredentials.secretAccessKey(),
                                               sessionCredentials.sessionToken());
        }

        return new BasicAWSCredentials(credentials.accessKeyId(), credentials.secretAccessKey());
    }

    @Override
    public void refresh() {
        throw new UnsupportedOperationException();
    }
}
