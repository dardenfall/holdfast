(ns holdfast.actors2
  (:require [clojure.test :refer :all]
             [clojure.spec.alpha :as s]
             [clojure.spec.test.alpha :as stest]))

(def NAME_LENGTH_LIMIT 85)

(defprotocol Agent
  "an actor in holdfast; enemy, user or npc"
  (is-alive? [_]))

(defrecord Actor [name max-hp hp job]
  Agent (is-alive? [this] (>= (:hp this) 0)))

(s/def ::name (s/and string? #(< (count(:name %)) NAME_LENGTH_LIMIT)))
(s/def ::max-hp (s/and int? #(> % 0)))
(s/def ::hp int?)
(s/def ::type #{:player :flies :npc})
(s/def ::job #{:tiller :herdsman :nurse :parson :pilgrim})
(s/def ::actor (s/cat :name (s/or ::npc_name ::name)
                      :hp ::hp
                      :max-hp ::max-hp
                      :job ::job))
(s/fdef ->Actor
        :args ::actor
        :ret Actor)

(deftest create-actor
  (testing "create an actor record"
    (is (not (nil? (->Actor "cloud" 25 25 :pilgrim))))))

(deftest is-alive
  (testing "is alive"
    (let [cloud (->Actor "cloud" 25 -1 :pilgrim)]
      (is (true? (is-alive? cloud))))))

;(deftest actor-spec
;  (testing "hp can't be negative"
;    (is (thrown? Exception (->Actor -1 25 :nurse)))))

(stest/instrument `->Actor)