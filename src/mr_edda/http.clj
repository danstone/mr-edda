(ns mr-edda.http
  "Contains the edda http client"
  (:require [clj-http.client :as http]
            [clojure.string :as str]
            [mr-edda.core :refer :all]))

(declare field->string)

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
                (str (name k) (fields->string v))))
    (name field)))

(defn filters->string
  "Takes a filter map and transforms it into the matrix selector form used by the edda api
  e.g  {:x \"foo\", :y.z \"bar\"} => x=foo;y.z=bar  "
  [filter]
  (str/join ";" (for [[k v] filter]
                  (str (name k) "=" v))))

(defn query->string
  "Takes an edda query map and turns it into a string
   to be appended to the query url"
  [query]
  (str

   (when-let [id (:id query)]
     (str "/" id))

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

(defrecord EddaHttpClient [base-url]
  IEddaClient
  (query* [this resource query]
    (-> (http/get (build-url base-url resource query)
                  {:as :json
                   :content-type "application/json"})
        :body)))

(defn http-client
  "Returns an edda http client"
  [base-url]
  (->EddaHttpClient base-url))

(def local-http-client
  "A default edda http client that can be used for testing
   expects edda to be served from localhost:8080"
  (http-client "http://localhost:8080"))
