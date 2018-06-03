(ns holdfast.actors3
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]))

(def NAME_LENGTH_LIMIT 85)
(def NPC-NAMES #{"Eliza" "Evelyn" "Ravian" "Sedrik" "Coro"})

(defprotocol Agent
  "an actor in holdfast; enemy, user or npc"
  (is-alive? [_]))

(defrecord Actor [name type max-hp hp job]
  Agent (is-alive? [this] (>= (:hp this) 0)))

(defn actor
  "creates an Actor record"
  [name type max-hp hp job]
  {:pre [(or (and (= type :npc) (contains? NPC-NAMES name))
             (and (contains? #{:player :flies} type) (string? name)))]}
  (->Actor name type max-hp hp job))

(s/def ::name (s/and string? #(< (count(:name %)) NAME_LENGTH_LIMIT)))
(s/def ::type #{:player :flies :npc})
(s/def ::max-hp (s/and int? #(> % 0)))
(s/def ::hp int?)
(s/def ::job #{:tiller :herdsman :nurse :parson :pilgrim})
(s/def ::actor (s/cat :name (s/or ::npc_name ::name)
                      :type ::type
                      :max-hp ::max-hp
                      :hp ::hp
                      :job ::job))
(s/fdef ->Actor
        :args ::actor
        :ret Actor)
(s/fdef actor
        :args ::actor
        :ret Actor)

(deftest create-actor
  (testing "create an actor record with constructor"
    (is (not (nil? (->Actor "cloud" :player 25 25 :pilgrim)))))
  (testing "create an actor record with custom constructor"
    (is (not (nil? (actor "Ravian" :npc 390199199 1 :pilgrim)))))
  (testing "fail to create an npc record with invalid namer"
    (is (thrown? java.lang.AssertionError (actor "cloud" :npc 25 25 :pilgrim)))))

(deftest is-alive
  (testing "is alive"
    (let [cloud (->Actor "cloud" :player 25 25 :pilgrim)]
      (is (true? (is-alive? cloud))))))

(deftest actor-spec
  (testing "hp can't be negative"
    (is (thrown? Exception (->Actor "witcher" :player -1 25 :nurse)))))

(stest/instrument [`->Actor `actor])