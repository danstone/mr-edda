(ns mr-edda.mock
  "Contains some mock clients useful for testing"
  (:require [mr-edda.core :refer :all]))

(defrecord AtomClient [state result]
  IEddaClient
  (query* [this resource query]
    (swap! (:state this) conj {:resource resource
                               :query query})
    result))

(defn atom-client
  "An edda client instance that simply conj's the resource and query
  as a map :resource, :query
  into an internal atom (:state) before returning the constant `result`"
  ([] (atom-client nil))
  ([result]
    (->AtomClient (atom []) result)))

(defn failing-client
  "An edda client instance that always throws an error when queried"
  []
  (reify IEddaClient
    (query* [this resource query]
      (throw (Exception. "An error occurred")))))
