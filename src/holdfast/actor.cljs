(ns holdfast.actors4
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
  ([name type max-hp hp job]
   (actor name nil type max-hp hp job))
  ([name age type max-hp hp job]
   {:pre [(or (and (= type :npc) (contains? NPC-NAMES name))
              (and (contains? #{:player :flies} type) (string? name)))]}
   (assoc (->Actor name type max-hp hp job) :age age)))

(s/def ::name (s/and string? #(< (count(:name %)) NAME_LENGTH_LIMIT)))
(s/def ::type #{:player :flies :npc})
(s/def ::age (s/or :age-supplied int? :no-age nil?))
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
        :args (s/alt :normal-actor ::actor
                     :aged-actor (s/cat :name ::name
                                        :age ::age
                                        :type ::type
                                        :max-hp ::max-hp
                                        :hp ::hp
                                        :job ::job))
        :ret Actor)

(deftest create-actor
  (testing "create an actor record with constructor"
    (is (not (nil? (->Actor "cloud" :player 25 25 :pilgrim)))))
  (testing "create an actor record with custom constructor"
    (is (not (nil? (actor "Ravian" 13 :npc 390199199 1 :pilgrim)))))
  (testing "create an actor record with custom constructor"
    (is (not (nil? (actor "Ravian" :npc 390199199 1 :pilgrim)))))
  (testing "fail to create an npc record with invalid name"
    (is (thrown? java.lang.AssertionError (actor "cloud" :npc 25 25 :pilgrim)))))

(deftest is-alive
  (testing "is alive"
    (let [cloud (->Actor "cloud" :player 25 25 :pilgrim)]
      (is (true? (is-alive? cloud)))))
  (testing "is alive... nope"
    (let [cloud (->Actor "cloud" :player 25 -1 :pilgrim)]
    (is (false? (is-alive? cloud))))))

(deftest actor-spec
  (testing "hp can't be negative"
    (is (thrown? Exception (->Actor "witcher" :player -1 25 :nurse)))))

(stest/instrument [`->Actor `actor])