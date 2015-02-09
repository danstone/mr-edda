(ns mr-edda.http
  "Contains the edda http client"
  (:require [clj-http.client :as http]
            [clj-http.util :refer [url-encode]]
            [camel-snake-kebab.core :as csk]
            [clojure.string :as str]
            [mr-edda.core :refer :all]))

(declare field->string)

(defn eddaize
  "Transform a string into one suitable for an edda url parameter"
  [str]
  (url-encode (csk/->camelCase (name str))))

(defn fields->string
  "Takes a sequence of fields and transforms them into a field selector
  as used by the edda api.

  e.g [:x :y {:a [:b :c]}] => \":(x, y, a:(b, c))\"
  meaning select fields x, y and the members b and c of a."
  [fields]
  (format ":(%s)" (str/join "," (map field->string fields))))

(defn- field->string
  [field]
  (if (map? field)
    (str/join ","
              (for [[k v] field]
                (str (eddaize k) (fields->string v))))
    (eddaize field)))

(defn filters->string
  "Takes a filter map and transforms it into the matrix selector form used by the edda api
  e.g  {:x \"foo\", :y.z \"bar\"} => x=foo;y.z=bar  "
  [filter]
  (str/join ";" (for [[k v] filter]
                  (str (eddaize k) "=" (url-encode (str v))))))

(defn query->string
  "Takes an edda query map and turns it into a string
   to be appended to the query url"
  [query]
  (str

   (when-let [id (:id query)]
     (str "/" (url-encode id)))

   ";"

   (when (:all? query)
     "_all;")

   (when (:expand? query)
     "_expand;")

   (when-let [at (:at query)]
     (str "_at=" at ";"))

   (when-let [filters (:filters query)]
     (filters->string filters))

   (when-let [limit (:limit query)]
     (str "_limit=" limit ";"))

   (when-let [fields (:fields query)]
     (fields->string fields))))


(defn build-url
  "Based on the resource type returns a url to hit with a GET request in order
  to retrieve a sequence describing that resource"
  [base-uri resource query]
  (format "%s/edda/api/v2/%s/%s%s" base-uri
          (:subpath resource)
          (:name resource)
          (query->string query)))

(defn force-sequential
  "If x is not already sequential, return as a singleton vec"
  [x]
  (if (or (nil? x) (sequential? x))
    x
    [x]))

(defn force-map-like
  "If x is not a map, make sure to return it under the specified key in a singleton map"
  [x key]
  (if (or (nil? x) (map? x))
    x
    {key x}))

(defn idiomize-names
  [m]
  (clojure.walk/postwalk
   #(if (keyword? %)
      (csk/->kebab-case %)
      %)
   m))

(defrecord EddaHttpClient [base-url]
  IEddaClient
  (query* [this resource query]
    (->> (http/get (build-url base-url resource query)
                   {:as :json
                    :content-type "application/json"})
         :body
         force-sequential
         (map (comp
               idiomize-names
               #(force-map-like % :id))))))

(defn client
  "Returns an edda http client"
  [base-url]
  (->EddaHttpClient base-url))

(def local-client
  "A default edda http client that can be used for testing
   expects edda to be served from localhost:8080"
  (client "http://localhost:8080"))
