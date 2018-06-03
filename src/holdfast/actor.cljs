(ns holdfast.actors1
  (:require [clojure.test :refer :all]))

(defprotocol Agent
  "an actor in holdfast; enemy, user or npc"
  (is-alive? [_]))

(defrecord Actor [name max-hp hp job]
  Agent (is-alive? [this] (>= (:hp this) 0)))

(deftest create-actor
  (testing "create an actor record"
    (is (not (nil? (->Actor :player 25 25 :pilgrim))))))

(deftest is-alive
  (testing "is alive"
    (let [cloud (->Actor :player 25 25 :pilgrim)]
      (is (true? (is-alive? cloud))))))

;; https://stackoverflow.com/questions/4575170/where-should-i-use-defrecord-in-clojure