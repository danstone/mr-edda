(ns mr-edda.group)

(defn group
  [name & {:as opts}]
  (merge {:name name
          :subpath "group"}
         opts))

(def auto-scaling-groups
  "The edda auto-scaling-groups group"
  (group "autoScalingGroups"))
