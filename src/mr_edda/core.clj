(ns mr-edda.core)

(defprotocol IEddaClient
  (query*
    [this resource query]
    "Runs a query against the resource
     e.g a collection like mr-edda.collections/instances
     returning a seq of results for the given query
     see mr-edda.core/query for options"))

(defn auto-expand
  "Takes an edda query map and returns a new query that will cause
   the _expand flag to be passed to edda if :fields selectors have been defined."
  [m]
  (if (:fields m)
    (assoc m :expand? true)
    m))

(defn query
  "Runs a query against the given resource

  e.g (query client images {:fields [:name, :imageId]})
  => returns a sequence of maps describing aws images currently cached by edda.

  valid query options are:
  :id -> a resource identity, this is a shortcut for filters on identity
  :filters -> a map that will restrict the result set e.g {:name \"some-string\"}
  :fields -> a vector describing the fields to select out
             e.g [:a, :b, {:c [:x :d]}] maps are used to describe sub fields.
  :limit -> limits the result set by n
  :expand? -> fully expand resultset, otherwise sometimes only the id will be returned.
              note: if you select fields via :fields, :expand will always be true"
  [client resource query]
  (query client resource (auto-expand query)))
