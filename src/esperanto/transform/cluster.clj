(ns esperanto.transform.cluster
  (:use [esperanto.transform :only [Transformer]])
  (:import (org.elasticsearch.action.admin.cluster.health
            ClusterHealthResponse)))

(defn transform-health
  "===  public org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse  ===
   [ 0] <init> (String,List)
   [ 1] activePrimaryShards : int ()
   [ 2] activeShards : int ()
   [ 3] allValidationFailures : List ()
   [ 4] clusterName : String ()
   [ 5] equals : boolean (Object)
   [ 6] getActivePrimaryShards : int ()
   [ 7] getActiveShards : int ()
   [ 8] getAllValidationFailures : List ()
   [ 9] getClass : Class ()
   [10] getClusterName : String ()
   [11] getIndices : Map ()
   [12] getInitializingShards : int ()
   [13] getNumberOfDataNodes : int ()
   [14] getNumberOfNodes : int ()
   [15] getRelocatingShards : int ()
   [16] getStatus : ClusterHealthStatus ()
   [17] getUnassignedShards : int ()
   [18] getValidationFailures : List ()
   [19] hashCode : int ()
   [20] indices : Map ()
   [21] initializingShards : int ()
   [22] isTimedOut : boolean ()
   [23] iterator : Iterator ()
   [24] notify : void ()
   [25] notifyAll : void ()
   [26] numberOfDataNodes : int ()
   [27] numberOfNodes : int ()
   [28] readFrom : void (StreamInput)
   [29] relocatingShards : int ()
   [30] status : ClusterHealthStatus ()
   [31] timedOut : boolean ()
   [32] toString : String ()
   [33] unassignedShards : int ()
   [34] validationFailures : List ()
   [35] wait : void ()
   [36] wait : void (long)
   [37] wait : void (long,int)
   [38] writeTo : void (StreamOutput)
   "
  [obj]
  (bean obj))

(extend ClusterHealthResponse
  Transformer
  {:transform transform-health})

