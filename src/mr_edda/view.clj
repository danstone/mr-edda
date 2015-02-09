(ns mr-edda.view)

(defn view
  [name & {:as opts}]
  (merge {:name name
          :subpath "view"}
         opts))

(def instances
  (view "instances"))

(def load-balancer-instances
  (view "loadBalancerInstances"))

(def simple-queues
  (view "simpleQueues"))
