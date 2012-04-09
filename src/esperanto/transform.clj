(ns esperanto.transform
  "Traverse ElasticSearch world from this side."
  (:require [clojure.reflect :as ref]
            [cheshire.core :as json]))

(defprotocol Transformer
  (transform [this] "More than meets the eye!"))

(defn default [obj]
  (doseq [x (sort (for [m (->> "foo" ref/reflect :members
                               (filter #(instance? clojure.reflect.Method %)))]
                    (format "%s : %s (%s)"
                            (:name m)
                            (:return-type m)
                            (:parameter-types m))))]
    (println x))
  obj)

(extend Object Transformer {:transform default})

(defmacro deftransform [klass & body]
  (let [body (if (string? (first body)) (rest body) body)
        [sym] (first body)
        f (symbol (.replaceAll (str "transform-" klass) "\\." "_"))]
    `(do
       (defn ~f [~sym]
         ~@body)
       (extend ~klass
         Transformer
         {:transform ~f}))))

(deftransform org.elasticsearch.common.unit.TimeValue
  "===  public org.elasticsearch.common.unit.TimeValue  ===
   [ 0] static parseTimeValue : TimeValue (String,TimeValue)
   [ 1] static readTimeValue : TimeValue (StreamInput)
   [ 2] static timeValueHours : TimeValue (long)
   [ 3] static timeValueMillis : TimeValue (long)
   [ 4] static timeValueMinutes : TimeValue (long)
   [ 5] static timeValueSeconds : TimeValue (long)
   [ 6] <init> (long)
   [ 7] <init> (long,TimeUnit)
   [ 8] days : long ()
   [ 9] daysFrac : double ()
   [10] equals : boolean (Object)
   [11] format : String ()
   [12] format : String (PeriodType)
   [13] getClass : Class ()
   [14] getDays : long ()
   [15] getDaysFrac : double ()
   [16] getHours : long ()
   [17] getHoursFrac : double ()
   [18] getMicros : long ()
   [19] getMicrosFrac : double ()
   [20] getMillis : long ()
   [21] getMillisFrac : double ()
   [22] getMinutes : long ()
   [23] getMinutesFrac : double ()
   [24] getNanos : long ()
   [25] getSeconds : long ()
   [26] getSecondsFrac : double ()
   [27] hashCode : int ()
   [28] hours : long ()
   [29] hoursFrac : double ()
   [30] micros : long ()
   [31] microsFrac : double ()
   [32] millis : long ()
   [33] millisFrac : double ()
   [34] minutes : long ()
   [35] minutesFrac : double ()
   [36] nanos : long ()
   [37] notify : void ()
   [38] notifyAll : void ()
   [39] readFrom : void (StreamInput)
   [40] seconds : long ()
   [41] secondsFrac : double ()
   [42] toString : String ()
   [43] wait : void ()
   [44] wait : void (long)
   [45] wait : void (long,int)
   [46] writeTo : void (StreamOutput)
  "
  [obj]
  (bean obj))

(deftransform org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse
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

(deftransform org.elasticsearch.action.admin.indices.create.CreateIndexResponse
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

(deftransform org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse
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

(deftransform org.elasticsearch.action.admin.indices.refresh.RefreshResponse
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

(deftransform org.elasticsearch.action.admin.indices.status.IndicesStatusResponse
  "===  public org.elasticsearch.action.admin.indices.status.IndicesStatusResponse  ===
   [ 0] static EMPTY_PARAMS : Params
   [ 1] equals : boolean (Object)
   [ 2] failedShards : int ()
   [ 3] getAt : ShardStatus (int)
   [ 4] getClass : Class ()
   [ 5] getFailedShards : int ()
   [ 6] getIndices : Map ()
   [ 7] getShardFailures : List ()
   [ 8] getShards : ShardStatus[] ()
   [ 9] getSuccessfulShards : int ()
   [10] getTotalShards : int ()
   [11] hashCode : int ()
   [12] index : IndexStatus (String)
   [13] indices : Map ()
   [14] notify : void ()
   [15] notifyAll : void ()
   [16] readFrom : void (StreamInput)
   [17] shardFailures : List ()
   [18] shards : ShardStatus[] ()
   [19] successfulShards : int ()
   [20] toString : String ()
   [21] toXContent : XContentBuilder (XContentBuilder,Params)
   [22] toXContent : XContentBuilder (XContentBuilder,Params,SettingsFilter)
   [23] totalShards : int ()
   [24] wait : void ()
   [25] wait : void (long)
   [26] wait : void (long,int)
   [27] writeTo : void (StreamOutput)
  "
  [obj]
  (let [t transform]
    (merge
     (bean obj)
     {:shards (map t (.getShards obj))})))

(deftransform org.elasticsearch.action.admin.indices.status.ShardStatus
  "===  public org.elasticsearch.action.admin.indices.status.ShardStatus  ===
   [ 0] static readIndexShardStatus : ShardStatus (StreamInput)
   [ 1] docs : DocsStatus ()
   [ 2] equals : boolean (Object)
   [ 3] gatewayRecoveryStatus : GatewayRecoveryStatus ()
   [ 4] gatewaySnapshotStatus : GatewaySnapshotStatus ()
   [ 5] getClass : Class ()
   [ 6] getDocs : DocsStatus ()
   [ 7] getGatewayRecoveryStatus : GatewayRecoveryStatus ()
   [ 8] getGatewaySnapshotStatus : GatewaySnapshotStatus ()
   [ 9] getIndex : String ()
   [10] getMergeStats : MergeStats ()
   [11] getPeerRecoveryStatus : PeerRecoveryStatus ()
   [12] getRefreshStats : RefreshStats ()
   [13] getShardId : int ()
   [14] getShardRouting : ShardRouting ()
   [15] getState : IndexShardState ()
   [16] getStoreSize : ByteSizeValue ()
   [17] getTranslogId : long ()
   [18] getTranslogOperations : long ()
   [19] hashCode : int ()
   [20] index : String ()
   [21] mergeStats : MergeStats ()
   [22] notify : void ()
   [23] notifyAll : void ()
   [24] peerRecoveryStatus : PeerRecoveryStatus ()
   [25] readFrom : void (StreamInput)
   [26] refreshStats : RefreshStats ()
   [27] shardId : int ()
   [28] shardRouting : ShardRouting ()
   [29] state : IndexShardState ()
   [30] storeSize : ByteSizeValue ()
   [31] toString : String ()
   [32] translogId : long ()
   [33] translogOperations : long ()
   [34] wait : void ()
   [35] wait : void (long)
   [36] wait : void (long,int)
   [37] writeTo : void (StreamOutput)
  "
  [obj]
  (merge
   (bean obj)
   {:docs (transform (.getDocs obj))
    :refreshStats (transform (.getRefreshStats obj))}))

(deftransform org.elasticsearch.action.admin.indices.status.DocsStatus
  "===  public org.elasticsearch.action.admin.indices.status.DocsStatus  ===
   [ 0] <init> ()
   [ 1] deletedDocs : int ()
   [ 2] equals : boolean (Object)
   [ 3] getClass : Class ()
   [ 4] getDeletedDocs : int ()
   [ 5] getMaxDoc : int ()
   [ 6] getNumDocs : int ()
   [ 7] hashCode : int ()
   [ 8] maxDoc : int ()
   [ 9] notify : void ()
   [10] notifyAll : void ()
   [11] numDocs : int ()
   [12] toString : String ()
   [13] wait : void ()
   [14] wait : void (long)
   [15] wait : void (long,int)
  "
  [obj]
  (bean obj))

(deftransform org.elasticsearch.index.refresh.RefreshStats
  "===  public org.elasticsearch.index.refresh.RefreshStats  ===
   [ 0] static EMPTY_PARAMS : Params
   [ 1] static readRefreshStats : RefreshStats (StreamInput)
   [ 2] <init> ()
   [ 3] <init> (long,long)
   [ 4] add : void (RefreshStats)
   [ 5] add : void (long,long)
   [ 6] equals : boolean (Object)
   [ 7] getClass : Class ()
   [ 8] hashCode : int ()
   [ 9] notify : void ()
   [10] notifyAll : void ()
   [11] readFrom : void (StreamInput)
   [12] toString : String ()
   [13] toXContent : XContentBuilder (XContentBuilder,Params)
   [14] total : long ()
   [15] totalTime : TimeValue ()
   [16] totalTimeInMillis : long ()
   [17] wait : void ()
   [18] wait : void (long)
   [19] wait : void (long,int)
   [20] writeTo : void (StreamOutput)
  "
  [obj]
  {:secs (.total obj)
   :time (transform (.totalTime obj))
   :millis (.totalTimeInMillis obj)})

(deftransform org.elasticsearch.action.bulk.BulkResponse
  "===  public org.elasticsearch.action.bulk.BulkResponse  ===
   [ 0] <init> (BulkItemResponse[],long)
   [ 1] buildFailureMessage : String ()
   [ 2] equals : boolean (Object)
   [ 3] getClass : Class ()
   [ 4] getTook : TimeValue ()
   [ 5] getTookInMillis : long ()
   [ 6] hasFailures : boolean ()
   [ 7] hashCode : int ()
   [ 8] items : BulkItemResponse[] ()
   [ 9] iterator : Iterator ()
   [10] notify : void ()
   [11] notifyAll : void ()
   [12] readFrom : void (StreamInput)
   [13] toString : String ()
   [14] took : TimeValue ()
   [15] tookInMillis : long ()
   [16] wait : void ()
   [17] wait : void (long)
   [18] wait : void (long,int)
   [19] writeTo : void (StreamOutput)
  "
  [obj]
  (merge
   {:failures? (.hasFailures obj)
    :items (map esperanto.transform/transform (.items obj))}
   (bean obj)))

(deftransform org.elasticsearch.action.bulk.BulkItemResponse
  "===  public org.elasticsearch.action.bulk.BulkItemResponse  ===
   [ 0] static readBulkItem : BulkItemResponse (StreamInput)
   [ 1] <init> (int,String,ActionResponse)
   [ 2] <init> (int,String,Failure)
   [ 3] equals : boolean (Object)
   [ 4] failed : boolean ()
   [ 5] failure : Failure ()
   [ 6] failureMessage : String ()
   [ 7] getClass : Class ()
   [ 8] getFailure : Failure ()
   [ 9] getFailureMessage : String ()
   [10] getId : String ()
   [11] getIndex : String ()
   [12] getType : String ()
   [13] hashCode : int ()
   [14] id : String ()
   [15] index : String ()
   [16] isFailed : boolean ()
   [17] itemId : int ()
   [18] notify : void ()
   [19] notifyAll : void ()
   [20] opType : String ()
   [21] readFrom : void (StreamInput)
   [22] response : ActionResponse ()
   [23] toString : String ()
   [24] type : String ()
   [25] version : long ()
   [26] wait : void ()
   [27] wait : void (long)
   [28] wait : void (long,int)
   [29] writeTo : void (StreamOutput)
  "

  [obj]
  (merge
   {:op-type (.opType obj)
    :failed? (.isFailed obj)}
   (bean obj)))

(deftransform org.elasticsearch.action.count.CountResponse
  "===  public org.elasticsearch.action.count.CountResponse  ===
   [ 0] count : long ()
   [ 1] equals : boolean (Object)
   [ 2] failedShards : int ()
   [ 3] getClass : Class ()
   [ 4] getCount : long ()
   [ 5] getFailedShards : int ()
   [ 6] getShardFailures : List ()
   [ 7] getSuccessfulShards : int ()
   [ 8] getTotalShards : int ()
   [ 9] hashCode : int ()
   [10] notify : void ()
   [11] notifyAll : void ()
   [12] readFrom : void (StreamInput)
   [13] shardFailures : List ()
   [14] successfulShards : int ()
   [15] toString : String ()
   [16] totalShards : int ()
   [17] wait : void ()
   [18] wait : void (long)
   [19] wait : void (long,int)
   [20] writeTo : void (StreamOutput)
  "
  [obj]
  (bean obj))

(deftransform org.elasticsearch.search.internal.InternalSearchHit
  "===  public org.elasticsearch.search.internal.InternalSearchHit  ===
   [ 0] static EMPTY_PARAMS : Params
   [ 1] static readSearchHit : InternalSearchHit (StreamInput,StreamContext)
   [ 2] <init> (int,String,String,byte[],Map)
   [ 3] docId : int ()
   [ 4] equals : boolean (Object)
   [ 5] explanation : Explanation ()
   [ 6] explanation : void (Explanation)
   [ 7] field : SearchHitField (String)
   [ 8] fields : Map ()
   [ 9] fields : void (Map)
   [10] fieldsOrNull : Map ()
   [11] getClass : Class ()
   [12] getExplanation : Explanation ()
   [13] getFields : Map ()
   [14] getHighlightFields : Map ()
   [15] getId : String ()
   [16] getIndex : String ()
   [17] getMatchedFilters : String[] ()
   [18] getScore : float ()
   [19] getShard : SearchShardTarget ()
   [20] getSortValues : Object[] ()
   [21] getSource : Map ()
   [22] getType : String ()
   [23] getVersion : long ()
   [24] hashCode : int ()
   [25] highlightFields : Map ()
   [26] highlightFields : void (Map)
   [27] id : String ()
   [28] index : String ()
   [29] isSourceEmpty : boolean ()
   [30] iterator : Iterator ()
   [31] matchedFilters : String[] ()
   [32] matchedFilters : void (String[])
   [33] notify : void ()
   [34] notifyAll : void ()
   [35] readFrom : void (StreamInput)
   [36] readFrom : void (StreamInput,StreamContext)
   [37] score : float ()
   [38] score : void (float)
   [39] shard : SearchShardTarget ()
   [40] shard : void (SearchShardTarget)
   [41] shardTarget : void (SearchShardTarget)
   [42] sortValues : Object[] ()
   [43] sortValues : void (Object[])
   [44] source : byte[] ()
   [45] sourceAsMap : Map ()
   [46] sourceAsString : String ()
   [47] toString : String ()
   [48] toXContent : XContentBuilder (XContentBuilder,Params)
   [49] type : String ()
   [50] version : long ()
   [51] version : void (long)
   [52] wait : void ()
   [53] wait : void (long)
   [54] wait : void (long,int)
   [55] writeTo : void (StreamOutput)
   [56] writeTo : void (StreamOutput,StreamContext)
  "
  [hit]
  (let [sas (.sourceAsString hit)
        src (when sas (json/parse-string sas :kw))]
    (with-meta (merge {:id (.getId hit)} src)
      (merge
       {
        :fields (reduce (fn [acc [k v]] (merge acc (transform v)))
                        {} (.getFields hit))
        :filters (seq (.getMatchedFilters hit))
        :hl-fields (.getHighlightFields hit)
        :index (.getIndex hit)
        :node (-> hit .getShard .getNodeId)
        :score (.getScore hit)
        :shard (-> hit .getShard .getShardId)
        :sort-vals (seq (.getSortValues hit))
        :source (when src :present)
        :type (.getType hit)
        }))))

(deftransform org.elasticsearch.search.internal.InternalSearchHitField
  "===  public org.elasticsearch.search.internal.InternalSearchHitField  ===
   [ 0] static readSearchHitField : InternalSearchHitField (StreamInput)
   [ 1] <init> (String,List)
   [ 2] equals : boolean (Object)
   [ 3] getClass : Class ()
   [ 4] getName : String ()
   [ 5] getValue : Object ()
   [ 6] getValues : List ()
   [ 7] hashCode : int ()
   [ 8] iterator : Iterator ()
   [ 9] name : String ()
   [10] notify : void ()
   [11] notifyAll : void ()
   [12] readFrom : void (StreamInput)
   [13] toString : String ()
   [14] value : Object ()
   [15] values : List ()
   [16] wait : void ()
   [17] wait : void (long)
   [18] wait : void (long,int)
   [19] writeTo : void (StreamOutput)
  "
  [obj]


  {(.getName obj)
   (let [vs (.getValues obj)]
     (if (= 1 (count vs))
       (first vs)
       (seq vs)))})

(deftransform org.elasticsearch.action.index.IndexResponse
  "===  public org.elasticsearch.action.index.IndexResponse  ===
   [ 0] <init> ()
   [ 1] <init> (String,String,String,long)
   [ 2] equals : boolean (Object)
   [ 3] getClass : Class ()
   [ 4] getId : String ()
   [ 5] getIndex : String ()
   [ 6] getMatches : List ()
   [ 7] getType : String ()
   [ 8] getVersion : long ()
   [ 9] hashCode : int ()
   [10] id : String ()
   [11] index : String ()
   [12] matches : List ()
   [13] matches : void (List)
   [14] notify : void ()
   [15] notifyAll : void ()
   [16] readFrom : void (StreamInput)
   [17] toString : String ()
   [18] type : String ()
   [19] version : long ()
   [20] wait : void ()
   [21] wait : void (long)
   [22] wait : void (long,int)
   [23] writeTo : void (StreamOutput)
  "
  [obj]
  (bean obj))

(deftransform org.elasticsearch.action.search.SearchResponse
  "===  public org.elasticsearch.action.search.SearchResponse  ===
   [ 0] static EMPTY_PARAMS : Params
   [ 1] static readSearchResponse : SearchResponse (StreamInput)
   [ 2] <init> (InternalSearchResponse,String,int,int,long,ShardSearchFailure[])
   [ 3] equals : boolean (Object)
   [ 4] facets : Facets ()
   [ 5] failedShards : int ()
   [ 6] getClass : Class ()
   [ 7] getFacets : Facets ()
   [ 8] getFailedShards : int ()
   [ 9] getHits : SearchHits ()
   [10] getScrollId : String ()
   [11] getShardFailures : ShardSearchFailure[] ()
   [12] getSuccessfulShards : int ()
   [13] getTook : TimeValue ()
   [14] getTookInMillis : long ()
   [15] getTotalShards : int ()
   [16] hashCode : int ()
   [17] hits : SearchHits ()
   [18] isTimedOut : boolean ()
   [19] notify : void ()
   [20] notifyAll : void ()
   [21] readFrom : void (StreamInput)
   [22] scrollId : String ()
   [23] shardFailures : ShardSearchFailure[] ()
   [24] status : RestStatus ()
   [25] successfulShards : int ()
   [26] timedOut : boolean ()
   [27] toString : String ()
   [28] toXContent : XContentBuilder (XContentBuilder,Params)
   [29] took : TimeValue ()
   [30] tookInMillis : long ()
   [31] totalShards : int ()
   [32] wait : void ()
   [33] wait : void (long)
   [34] wait : void (long,int)
   [35] writeTo : void (StreamOutput)
  "
  [obj]
  (with-meta (map esperanto.transform/transform
                  (.getHits obj))
    {:facets (.getFacets obj)
     :scroll-id (.getScrollId obj)
     :shards (.getTotalShards obj)
     :shards-bad (.getFailedShards obj)
     :shards-good (.getSuccessfulShards obj)
     :status (bean (.status obj))
     :timed-out? (.isTimedOut obj)
     :took (.getTookInMillis obj)
     :total (-> obj .getHits .getTotalHits)}))
