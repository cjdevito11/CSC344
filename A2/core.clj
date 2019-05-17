(ns a2micro.core)

(defn lookup [i m]
  (get m i i))


(defn deep-sub [l m]
  (map (fn [i]
         (if (seq? i)
           (deep-sub i m)
           (lookup i m)))
       l))

;(defn deep-sub [m l]
;  (map #(if (seq? %) (deep-sub m %)
;                   (lookup % m)) l))

(defn and-convert [l]
  (conj () (conj l 'nand)))

(defn or-convert [l]
  (map #(conj () % 'nand) l))

(defn base-convert [l]
  (cond
    (= (first l) 'and) (conj (and-convert (rest l)) 'nand)
    (= (first l) 'or) (conj (or-convert (rest l)) 'nand)
    (= (first l) 'not) (conj (rest l) 'nand)
    :else (map identity l)
    )
  )

(defn nand-convert [l]
  (let [x (base-convert l)]
    (map #(if
            (seq? %) (nand-convert %)
                     %)x))
  )

(defn simplify-convert [l]
  (let [x (base-simplify l)]
    (if (not (seq? x)) x
                       (map #(if (seq? %)
                               (simplify-convert %)
                                        (if (seq? %)
                                          (simplify-convert %)
                                                     %))x))
    )
  )

(defn base-simplify [argList]
  ;;(println argList)
  (if (seq? argList)
    (let [reducedArg (filter #(not (= 'true %)) (distinct (rest argList)))]
      (cond
        (some #(= % false) reducedArg) true
        (empty? reducedArg) false
        (= (count argList) 2) (if (seq? (nth argList 1))
                                (if (= (count (nth argList 1)) 2)
                                  (nth (nth argList 1) 1) (conj reducedArg 'nand))
                                (conj reducedArg 'nand))

        (= (count argList) 3) (if (seq? (nth argList 2))
                                (if (= (count (nth argList 2)) 2)
                                  (if (= (nth argList 1) (nth (nth argList 2) 1)) true (conj reducedArg 'nand))
                                  (conj reducedArg 'nand)) (conj reducedArg 'nand))
        :else argList
        )
      )
    argList)
  )

(defn simplify [x]
  (if (seq? x)
    (base-simplify (map #(simplify %) x))
    x)
  )



(def p1 '(and x (or x (and y (not z)))))
(def p2 '(and (and z false) (or x true false)))

(def p3 '(or true a))
(def p4 '(and x (true)))
(def p5 '(and z false))
(def p6 '(and (and z false)))

(def p7 '(not z))
(def p8 '(and y (not z)))
(def p9 '(and (or x (and y (not z)))))
(def p10 '(or x (and y (not z))))

(defn evalexp [exp bindings]
  (simplify (simplify (nand-convert (deep-sub bindings exp)))))

(defn evalexporig [exp bindings]
  (simplify (nand-convert (deep-sub exp bindings))))

(defn evalexp2 [exp bindings]
  (simplify (deep-sub exp bindings))
  )


(defn simpl[i]
  (cond
    (some false? i) true
    (every? true? (rest i)) false
    (and (map (number? i)) every? true?) (set i)
    )
  )

; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************

; START OF NOT MY CODE >>




(def ANSI-CODES
  {:reset              "[0m"
   :bright             "[1m"
   :blink-slow         "[5m"
   :underline          "[4m"
   :underline-off      "[24m"
   :inverse            "[7m"
   :inverse-off        "[27m"
   :strikethrough      "[9m"
   :strikethrough-off  "[29m"

   :default "[39m"
   :white   "[37m"
   :black   "[30m"
   :red     "[31m"
   :green   "[32m"
   :blue    "[34m"
   :yellow  "[33m"
   :magenta "[35m"
   :cyan    "[36m"

   :bg-default "[49m"
   :bg-white   "[47m"
   :bg-black   "[40m"
   :bg-red     "[41m"
   :bg-green   "[42m"
   :bg-blue    "[44m"
   :bg-yellow  "[43m"
   :bg-magenta "[45m"
   :bg-cyan    "[46m"
   })


(def ^:dynamic *use-ansi* "Rebind this to false if you don't want to see ANSI codes in some part of your code." true)

(defn ansi
  "Output an ANSI escape code using a style key.
   (ansi :blue)
   (ansi :underline)
  Note, try (style-test-page) to see all available styles.
  If *use-ansi* is bound to false, outputs an empty string instead of an
  ANSI code. You can use this to temporarily or permanently turn off
  ANSI color in some part of your program, while maintaining only 1
  version of your marked-up text.
  "
  [code]
  (if *use-ansi*
    (str \u001b (get ANSI-CODES code (:reset ANSI-CODES)))
    ""))

(defmacro without-ansi
  "Runs the given code with the use-ansi variable temporarily bound to
  false, to suppress the production of any ANSI color codes specified
  in the code."
  [& code]
  `(binding [*use-ansi* false]
     ~@code))

(defmacro with-ansi
  "Runs the given code with the use-ansi variable temporarily bound to
  true, to enable the production of any ANSI color codes specified in
  the code."
  [& code]
  `(binding [*use-ansi* true]
     ~@code))


(defn style
  "Applies ANSI color and style to a text string.
   (style \"foo\" :red)
   (style \"foo\" :red :underline)
   (style \"foo\" :red :bg-blue :underline)
 "
  [s & codes]
  (str (apply str (map ansi codes)) s (ansi :reset)))

(defn wrap-style
  "Wraps a base string with a stylized wrapper.
  If the wrapper is a string it will be placed on both sides of the base,
  and if it is a seq the first and second items will wrap the base.
  To wrap debug with red brackets => [debug]:
  (wrap-style \"debug\" [\"[\" \"]\"] :red)
  "
  [base wrapper & styles]
  (str (apply style wrapper styles)
       base
       (apply style wrapper styles)))

(defn style-test-page
  "Print the list of supported ANSI styles, each style name shown
  with its own style."
  []
  (doall
    (map #(println (style (name %) %)) (sort-by name (keys ANSI-CODES))))
  nil)

(def doc-style* (ref {:line  :blue
                      :title :bright
                      :args  :red
                      :macro :blue
                      :doc   :green}))

(defn print-special-doc-color
  "Print stylized special form documentation."
  [name type anchor]
  (println (style "-------------------------" (:line @doc-style*)))
  (println (style name (:title @doc-style*)))
  (println type)
  (println (style (str "  Please see http://clojure.org/special_forms#" anchor)
                  (:doc @doc-style*))))

(defn print-namespace-doc-color
  "Print stylized documentation for a namespace."
  [nspace]
  (println (style "-------------------------"    (:line @doc-style*)))
  (println (style (str (ns-name nspace))         (:title @doc-style*)))
  (println (style (str " " (:doc (meta nspace))) (:doc @doc-style*))))

(defn print-doc-color
  "Print stylized function documentation."
  [v]
  (println (style "-------------------------" (:line @doc-style*)))
  (println (style (str (ns-name (:ns (meta v))) "/" (:name (meta v)))
                  (:title @doc-style*)))
  (print "(")
  (doseq [alist (:arglists (meta v))]
    (print "[" (style (apply str (interpose " " alist)) (:args @doc-style*)) "]"))
  (println ")")

  (when (:macro (meta v))
    (println (style "Macro" (:macro @doc-style*))))
  (println "  " (style (:doc (meta v)) (:doc @doc-style*))))

(defmacro color-doc
  "A stylized version of clojure.core/doc."
  [v]
  `(binding [print-doc print-doc-color
             print-special-doc print-special-doc-color
             print-namespace-doc print-namespace-doc-color]
     (doc ~v)))

(defn colorize-docs
  []
  (intern 'clojure.core 'print-doc print-doc-color)
  (intern 'clojure.core 'print-special-doc print-special-doc-color)
  (intern 'clojure.core 'print-namespace-doc print-namespace-doc-color))


; **********************************

; END OF NOT MY CODE


; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************
; ***********************************


(defn testing[]
  ;(println[(simplify '(nand false))])
  ;(println[(simplify '(nand true))])
  ;(println[(simplify '(nand (nand x)))])
  ;(println[(simplify '(nand x (nand x)))])
  ;(println[(simplify '(nand x x))])
  ;(println[(simplify '(nand x y))])
  ;(println[(simplify '(nand x true))])
  ;(println[(simplify '(nand x false))])
  ;(println[(simplify '(nand true true))])
  ;(println[(simplify '(nand x y true))])
  ;(println[(simplify '(nand x true true))])
  ;(println[(simplify '(nand true true true))])
  ;(println[(simplify '(nand x y false))])
  ;(println[(simplify '(nand x y z))])

  (println (style "-------------------------------------------------------------------" :bg-cyan))
  (println "p1: "  p1)
  (println "p2: "  p2)
  (println "p3: "  p3)
  (println (style "-------------------------------------------------------------------" :bg-red))
  (println (style "-------------------------------------------------------------------" :bg-blue))
  (println "p1: " (style p1 :red) (style " eval -> " :cyan) (evalexp p1 '{}))
  (println (style "-------------------------------------------------------------------" :bg-cyan))
  (println (style p2 :red) (style " eval -> " :cyan) (evalexp p2 '{}))
  (println (style "-------------------------------------------------------------------" :bg-cyan))
  (println (style p3 :red) (style " eval -> " :cyan) (evalexp p3 '{}))
  (println (style "-------------------------------------------------------------------" :bg-cyan))
  (println (style p4 :red) (style " eval -> " :cyan) (evalexp p4 '{}))
  (println (style "-------------------------------------------------------------------" :bg-cyan))
  (println (style p5 :red) (style " eval -> " :cyan) (evalexp p5 '{}))
  (println (style "-------------------------------------------------------------------" :bg-cyan))
  (println "p6: " (style  p6 :red) (style " eval -> " :cyan) (evalexp p6 '{}))
  (println (style "-------------------------------------------------------------------" :bg-cyan))
  (println (style p7 :red) (style " eval -> " :cyan) (evalexp p7 '{}))
  (println (style "-------------------------------------------------------------------" :bg-cyan))
  (println (style p8 :red) (style " eval -> " :cyan) (evalexp p8 '{}))
  (println (style "-------------------------------------------------------------------" :bg-cyan))
  (println (style p9 :red) (style " eval -> " :cyan) (evalexp p9 '{}))
  (println (style "-------------------------------------------------------------------" :bg-cyan))
  (println (style p10 :red) (style " eval -> " :cyan) (evalexp p10 '{}))
  (println (style "-------------------------------------------------------------------" :bg-blue))

  (println "(evalexp p1 '{x false, z true}) : " (evalexp p1 '{x false, z true}))
  ;(println "(nand-convert p1)" (nand-convert p1))

  )



