(ns holdfast.actors
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.spec.gen.alpha :as gen]))

(defprotocol Agent
  "an actor in holdfast; enemy, user or npc"
  (is-alive? [_]))

(defrecord Actor [name type max-hp hp job]
  Agent (is-alive? [this] (>= (:hp this) 0)))

(def NPC-NAMES #{"Eliza" "Evelyn" "Ravian" "Sedrik" "Coro"})
(def NAME_LENGTH_LIMIT 85)

(defn actor
  "creates an Actor record"
  [name type max-hp hp job]
  {:pre [(or (and (= type :npc) (contains? NPC-NAMES name))
             (and (contains? #{:player :flies} type) (string? name)))]}
  (->Actor name type max-hp hp job))

;(s/def ::type #{:player :flies :npc})
;(s/def ::name (s/and string? #(< (count(:name %)) NAME_LENGTH_LIMIT)))
;(s/def ::npc_name (s/and :name ::npc_names))
;(s/def ::max-hp (s/and int? #(> % 0)))
;(s/def ::hp int?)
;(s/def ::job #{:tiller :herdsman :nurse :parson :pilgrim})
;(s/def ::actor (s/cat :name (s/or ::npc_name ::name)
;                      :type ::type
;                      :hp ::hp
;                      :max-hp ::max-hp
;                      :job ::job))
;(s/fdef ->Actor
;        :args ::actor
;        :ret Actor)

;(stest/instrument `->Actor)

;(defmulti actor-type :type)
;(defmethod actor-type :player [_]
;  (s/keys :req-un [::name ::type ::hp ::max-hp ::job]))
;(defmethod actor-type :npc [_]
;  (s/keys :req-un [::npc_name ::type ::hp ::max-hp ::job]))
;(s/def ::actor (s/multi-spec actor-type ::type))

;(print (s/explain ::actor (actor "Ravian" :npc 390199199 1 :pilgrim)))
(actor "Ravian" :npc 390199199 1 :pilgrim)
;(s/explain (s/or :name ::name
;                 :npcname (s/and string? #(< (count(:name %)) NAME_LENGTH_LIMIT) ::npc_names))
;           (
;(s/explain ::actor (->Actor "Ravian" :player 390199199 1 :pilgrim))

; (print (s/conform ::actor (->Actor "Ravian" :npc 390199199 1 :pilgrim)))
; (print (gen/generate (s/gen ::actor)))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;;  test for record
;(defrecord Pet [name type])
;(->Pet "Meowy" :cat)
