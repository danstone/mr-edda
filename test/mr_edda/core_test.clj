(ns mr-edda.core-test
  (:require [clojure.test :refer :all]
            [mr-edda.core :refer :all]
            [mr-edda.collection :as c]
            [mr-edda.mock :refer :all]))

(deftest test-auto-expand
  (testing "If I passed a :fields value with my query, the :expand field is auto assoc'd into the query"
    (let [client (atom-client)]
      (query client c/images {:fields []})
      (is (-> @(:state client) last :query :expand?))))
  (testing "If I do not pass fields with my query, the expand field is *not* auto assoc'd into the query"
    (let [client (atom-client)]
      (query client c/images {:filters {:foo "bar"}})
      (is (not (-> @(:state client) :query :expand?))))))
