(ns mr-edda.collection)

(defn collection
  "Returns a map describing the collection"
  [name & {:as opts}]
  (merge
   {:name (clojure.core/name name)
    :subpath "aws"}
   opts))

(def addresses
  "The edda addresses collection"
  (collection "addresses"))

(def alarms
  "The edda alarms collection"
  (collection "alarms"))

(def auto-scaling-groups
  "The edda auto scaling groups collection"
  (collection "autoScalingGroups"))

(def buckets
  "The edda buckets collection"
  (collection "buckets"))

(def databases
  "The edda databases collection"
  (collection "databases"))

(def iam-groups
  "The edda iam-groups collection"
  (collection "iamGroups"))

(def iam-roles
  "The edda iam-roles collection"
  (collection "iamRoles"))

(def iam-users
  "The edda iam-users collection"
  (collection "iamUsers"))

(def iam-virtual-mfa-devices
  "The edda iam-virtual-mfa-devices"
  (collection "iamVirtualMFADevices"))

(def images
  "The edda images collection"
  (collection "images"))

(def launch-configurations
  "The edda launch-configurations collection"
  (collection "launchConfigurations"))

(def load-balancers
  "The edda load-balancers collection"
  (collection "loadBalancers"))

(def reserved-instances
  "The edda reserved instances collection"
  (collection "reservedInstances"))

(def scaling-policies
  "The edda scaling policies collection"
  (collection "scalingPolicies"))

(def security-groups
  "The edda security groups collection"
  (collection "security-groups"))

(def snapshots
  "The edda snapshots collection"
  (collection "snapshots"))

(def tags
  "The edda tags collection"
  (collection "tags"))

(def volumes
  "The edda volumes collection"
  (collection "volumes"))
