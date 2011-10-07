(ns esperanto.transform.index
  (:require [cheshire.core :as json])
  (:use [esperanto.transform :only [Transformer]])
  (:import (org.elasticsearch.action.bulk BulkResponse BulkItemResponse)
           (org.elasticsearch.action.count CountResponse)
           (org.elasticsearch.action.index IndexResponse)
           (org.elasticsearch.search.internal InternalSearchHit)
           (org.elasticsearch.action.search SearchResponse)))

(defn transform-bulk
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

(extend BulkResponse
  Transformer
  {:transform transform-bulk})

(defn transform-bulk-item
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

(extend BulkItemResponse
  Transformer
  {:transform transform-bulk-item})

(defn transform-count
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

(extend CountResponse
  Transformer
  {:transform transform-count})

(defn transform-hit
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
  (let [src (.sourceAsString hit)]
    (with-meta (merge {:id (.getId hit)}
                      (if src
                        (json/parse-string src :kw)
                        {:ERROR "_source is not enabled"}))
      {:index (.getIndex hit)
       :node (-> hit .getShard .getNodeId)
       :shard (-> hit .getShard .getShardId)
       :sort-vals (seq (.getSortValues hit))})))

(extend InternalSearchHit
  Transformer
  {:transform transform-hit})

(defn transform-index
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

(extend IndexResponse
  Transformer
  {:transform transform-index})

(defn transform-search
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

(extend SearchResponse
  Transformer
  {:transform transform-search})

