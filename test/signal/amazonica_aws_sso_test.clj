(ns signal.amazonica-aws-sso-test
  (:require [amazonica.aws.securitytoken :as sts]
            [amazonica.core :as amazonica]
            [clojure.test :refer [deftest is use-fixtures]]
            [signal.amazonica-aws-sso :as amazonica-aws-sso])
  (:import [com.signalai.aws SSOCompatibleDefaultAWSCredentialsProviderChain SSOCompatibleProfileCredentialsProvider]))

(defn after-each [f]
  (f)
  (amazonica/defcredential {}))

(use-fixtures :each after-each)

(defn- check-sts-get-caller-identity []
  (let [result (sts/get-caller-identity)]
    (is (some? (:account result)))
    (is (some? (:arn result)))
    (is (some? (:user-id result)))))

(deftest amazonica-fails-without-fix
  (is (thrown? com.amazonaws.SdkClientException
               (check-sts-get-caller-identity))))

(deftest SSOCompatibleProfileCredentialsProvider-returns-credentials-from-profile
  (let [credentials (bean (.getCredentials (new SSOCompatibleProfileCredentialsProvider)))]
    (is (some? (:AWSAccessKeyId credentials)))
    (is (some? (:AWSSecretKey credentials)))
    (is (some? (:sessionToken credentials)))))

(deftest SSOCompatibleDefaultAWSCredentialsProviderChain-returns-credentials-from-profile
  (let [credentials (bean (.getCredentials (SSOCompatibleDefaultAWSCredentialsProviderChain/getInstance)))]
    (is (some? (:AWSAccessKeyId credentials)))
    (is (some? (:AWSSecretKey credentials)))
    (is (some? (:sessionToken credentials)))))

(deftest with-sso-credential-picks-up-sso-credentials
  (amazonica-aws-sso/with-sso-credential
    (is (some? (check-sts-get-caller-identity)))))

(deftest init!-globally-adds-sso-credential-support
  (amazonica-aws-sso/init!)
  (is (some? (check-sts-get-caller-identity))))

(deftest reset!-resets-amazonica-credentials
  (amazonica-aws-sso/init!)
  (amazonica-aws-sso/reset!)
  (is (thrown? com.amazonaws.SdkClientException
               (check-sts-get-caller-identity))))
