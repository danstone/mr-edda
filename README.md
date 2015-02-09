# mr-edda

A little client for talking to [edda](http://github.com/Netflix/edda)

## Usage
Include in your project.clj
```clojure
[mixradio/mr-edda "0.1.0"]
```
```clojure
(require '[mr-edda.core :as edda]) ;;query
(require '[mr-edda.http :as edda-http]) ;;http client
(require '[mr-edda.collection :as edda-coll]) ;;collection resources
(require '[mr-edda.view :as edda-view]) ;;view resources
(require '[mr-edda.group :as edda-group]) ;;group resources
```

### create a client
```clojure
(def client (edda-http/client "http://localhost:8080"))
```
### query aws resources via edda
```clojure
(edda/query client edda-coll/images
                   {:filters {:imageOwnerAlias "amazon"
                              :public true})
;; => a seq of maps representing the images matching the filters
```

### query options
`:expand?` if true returns the entire document describing the resources
rather than just its id.

`:filters` a map containing field -> value pairs which will be restrict the result set of a query. Can specify sub fields using dot notation. 
e.g 
```clojure
(query client edda-coll/auto-scaling-groups 
              {:filters {:instances.availabilityZone "eu-west-1b"}})
```

`:fields` expands out to [field](https://developer.linkedin.com/documents/field-selectors) notation. In clojure this is represented as vectors and maps. If fields is passed, `:expand?` is implicitly passed (as otherwise it would have no effect).
```clojure
(query client edda-coll/auto-scaling-groups 
              {:fields [:status {:instances [:availabilityZone :healthStatus]}]})
```
`:limit` simply limits the result set

`:id` instead of filtering and returning a sequence, if you know the id of the resource, you can returning it directly.

```clojure
(query client edda-coll/images {:id "some-image"}) 
;; => either a singleton sequence with the full document describing the 
;; resource, or an empty sequence if the id didn't match any thing.
```

## License
Copyright © 2015 MixRadio

[mr-edda is released under the 3-clause license ("New BSD License" or "Modified BSD License").](http://github.com/mixradio/mr-edda/blob/master/LICENSE)

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
