(ns signal.amazonica-aws-sso
  (:refer-clojure :exclude [reset!])
  (:require [amazonica.core :as amazonica])
  (:import [com.signalai.aws SSOCompatibleDefaultAWSCredentialsProviderChain]))

(defn init!
  "Sets Amazonica to use SSOCompatibleDefaultAWSCredentialsProviderChain for credentials.
   
   This enables compatibility with amazonica and AWS SSO credentials."
  []
  (amazonica/defcredential (SSOCompatibleDefaultAWSCredentialsProviderChain/getInstance)))

(defn reset!
  "Resets amazonica to default credentials providers."
  []
  (amazonica/defcredential {}))

(defmacro with-sso-credential
  "Per invocation binding of credentials for ad-hoc
  service calls using alternate user/password combos
  (and endpoints)."
  [& body]
  `(amazonica/with-credential (SSOCompatibleDefaultAWSCredentialsProviderChain/getInstance) ~@body))
