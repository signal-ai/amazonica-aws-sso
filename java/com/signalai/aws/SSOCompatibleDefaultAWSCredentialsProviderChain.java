package com.signalai.aws;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider;

// Add compatiblity to the DefaultAWSCredentialsProviderChain by replacing ProfileCredentialsProvider with SSOCompatibleCredentialsProvider
public class SSOCompatibleDefaultAWSCredentialsProviderChain extends AWSCredentialsProviderChain {

    private static final SSOCompatibleDefaultAWSCredentialsProviderChain INSTANCE
        = new SSOCompatibleDefaultAWSCredentialsProviderChain();

    public SSOCompatibleDefaultAWSCredentialsProviderChain() {
        super(new EnvironmentVariableCredentialsProvider(),
              new SystemPropertiesCredentialsProvider(),
              WebIdentityTokenCredentialsProvider.create(),
              new SSOCompatibleProfileCredentialsProvider(),
              new EC2ContainerCredentialsProviderWrapper());
    }

    public static SSOCompatibleDefaultAWSCredentialsProviderChain getInstance() {
        return INSTANCE;
    }
}
