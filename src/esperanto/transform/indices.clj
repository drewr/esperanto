(ns esperanto.transform.indices
  (:use [esperanto.transform :only [Transformer]])
  (:import (org.elasticsearch.action.admin.indices.create CreateIndexResponse)
           (org.elasticsearch.action.admin.indices.delete DeleteIndexResponse)
           (org.elasticsearch.action.admin.indices.refresh RefreshResponse)
           (org.elasticsearch.action.admin.indices.status
            IndicesStatusResponse)))

(defn transform-create
  "===  public org.elasticsearch.action.admin.indices.create.CreateIndexResponse  ===
   [ 0] acknowledged : boolean ()
   [ 1] equals : boolean (Object)
   [ 2] getAcknowledged : boolean ()
   [ 3] getClass : Class ()
   [ 4] hashCode : int ()
   [ 5] notify : void ()
   [ 6] notifyAll : void ()
   [ 7] readFrom : void (StreamInput)
   [ 8] toString : String ()
   [ 9] wait : void ()
   [10] wait : void (long)
   [11] wait : void (long,int)
   [12] writeTo : void (StreamOutput)
  "
  [obj]
  (bean obj))

(defn transform-delete
  "===  public org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse  ===
   [ 0] acknowledged : boolean ()
   [ 1] equals : boolean (Object)
   [ 2] getAcknowledged : boolean ()
   [ 3] getClass : Class ()
   [ 4] hashCode : int ()
   [ 5] notify : void ()
   [ 6] notifyAll : void ()
   [ 7] readFrom : void (StreamInput)
   [ 8] toString : String ()
   [ 9] wait : void ()
   [10] wait : void (long)
   [11] wait : void (long,int)
   [12] writeTo : void (StreamOutput)
  "
  [obj]
  (bean obj))

(defn transform-refresh
  "===  public org.elasticsearch.action.admin.indices.refresh.RefreshResponse  ===
   [ 0] equals : boolean (Object)
   [ 1] failedShards : int ()
   [ 2] getClass : Class ()
   [ 3] getFailedShards : int ()
   [ 4] getShardFailures : List ()
   [ 5] getSuccessfulShards : int ()
   [ 6] getTotalShards : int ()
   [ 7] hashCode : int ()
   [ 8] notify : void ()
   [ 9] notifyAll : void ()
   [10] readFrom : void (StreamInput)
   [11] shardFailures : List ()
   [12] successfulShards : int ()
   [13] toString : String ()
   [14] totalShards : int ()
   [15] wait : void ()
   [16] wait : void (long)
   [17] wait : void (long,int)
   [18] writeTo : void (StreamOutput)
  "
  [obj]
  (bean obj))

(defn transform-status [obj]
  {:transformed obj})

(extend CreateIndexResponse
  Transformer
  {:transform transform-create})

(extend DeleteIndexResponse
  Transformer
  {:transform transform-delete})

(extend IndicesStatusResponse
  Transformer
  {:transform transform-status})

(extend RefreshResponse
  Transformer
  {:transform transform-refresh})

