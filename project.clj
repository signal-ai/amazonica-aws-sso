(defproject com.signal-ai/amazonica-aws-sso "1.0.2-SNAPSHOT"
  :description "Amazonica AWS SSO"
  :url "http://github.com/signal-ai/amazonica-aws-sso"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1" :scope "provided"]

                 [amazonica "0.3.163" :scope "provided"]

                 [com.amazonaws/aws-java-sdk-sts "1.12.453" :scope "provided"]

                 ;; used by SSOCompatibleProfileCredentialsProvider to use the V2
                 ;; AWS SDK as a compatibility shim for the v1 AWS SDK
                 [software.amazon.awssdk/auth "2.20.50"]
                 [software.amazon.awssdk/sso "2.20.50"]]
  :java-source-paths ["java"]
  :profiles {:dev {:dependencies [[amazonica "0.3.163"]
                                  [com.amazonaws/aws-java-sdk-sts "1.12.453"]

                                  [ch.qos.logback/logback-classic "1.2.3"]
                                  [org.slf4j/slf4j-api "1.7.36"]
                                  [org.slf4j/jul-to-slf4j "1.7.36"]
                                  [org.slf4j/jcl-over-slf4j "1.7.36"]
                                  [org.slf4j/log4j-over-slf4j "1.7.36"]
                                  [org.slf4j/osgi-over-slf4j "1.7.36"]

                                  [lambdaisland/kaocha "1.82.1306"]
                                  [lambdaisland/kaocha-junit-xml "1.17.101"]
                                  [lambdaisland/kaocha-cloverage "1.1.89"]]

                   :exclude [commons-logging
                             log4j
                             org.apache.logging.log4j/log4j
                             org.slf4j/simple
                             org.slf4j/slf4j-jcl
                             org.slf4j/slf4j-nop
                             org.slf4j/slf4j-log4j12
                             org.slf4j/slf4j-log4j13]}
             :resource-paths ["dev-resources"]}

  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/clojars_username
                                     :password :env/clojars_token
                                     :sign-releases false}]
                        ["snapshots" {:url "https://clojars.org/repo"
                                      :username :env/clojars_username
                                      :password :env/clojars_token}]]
  :aliases {"test" ["with-profile" "dev,test" "run" "-m" "kaocha.runner"]})
