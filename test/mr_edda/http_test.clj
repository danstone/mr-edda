(ns mr-edda.http-test
  (:require [mr-edda.http :refer :all]
            [mr-edda.mock :refer :all]
            [mr-edda.collection :as c]
            [clojure.test :refer :all]))

(deftest test-field-expansion
  (testing "fields are turned into strings correctly"
    (are [a b] (= (fields->string a) b)
      [:x :y {:a [:b :c]}] ":(x,y,a:(b,c))"
      [:foo {:camel-case [:baz]} :qux] ":(foo,camelCase:(baz),qux)")))


(deftest test-filter-expansion
  (testing "filters are turned into strings correctly"
    (are [a b] (= (filters->string a) b)
         (array-map :x "foo"
                    :y.z "bar"
                    :y.y "baz") "x=foo;y.z=bar;y.y=baz")))

(deftest test-query-expansion
  (testing "queries are expanded into strings correctly"
    (are [a b] (= (query->string a) b)
         {:id "foo qux+bar"} "/foo+qux%2Bbar;"
         {:id "bar"
          :at 1002333} "/bar;_at=1002333;")))
