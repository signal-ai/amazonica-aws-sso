(ns signal.amazonica-aws-sso
  (:refer-clojure :exclude [reset!])
  (:require [amazonica.core :as amazonica])
  (:import [com.signalai.aws SSOCompatibleDefaultAWSCredentialsProviderChain]))

(defn- default-credentials-provider*
  []
  (SSOCompatibleDefaultAWSCredentialsProviderChain/getInstance))

(def default-credentials-provider
  "Returns a new instance of com.signalai.awsSSOCompatibleDefaultAWSCredentialsProviderChain.
   
   This can be used as a drop in replacement for com.amazonaws.authDefaultAWSCredentialsProviderChain."
  (memoize default-credentials-provider*))

(defn provider->amazonica-credentials [creds]
  {;; we have to assign to the "cred" key, 
   ;; else overriding on a per-client basis fails when merging credentials
   :cred creds})

(defn init!
  "Sets Amazonica to use SSOCompatibleDefaultAWSCredentialsProviderChain for credentials.
   
   This enables compatibility with amazonica and AWS SSO credentials."
  []
  (amazonica/defcredential (provider->amazonica-credentials (default-credentials-provider))))

(defn reset!
  "Resets amazonica to default credentials providers."
  []
  (amazonica/defcredential {}))

(defmacro with-sso-credential
  "Per invocation binding of credentials for ad-hoc
  service calls using alternate user/password combos
  (and endpoints)."
  [& body]
  `(amazonica/with-credential (provider->amazonica-credentials (default-credentials-provider)) ~@body))
