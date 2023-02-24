;--------------------------------------------------------HW10----------------------------------------------------------
(def constant constantly)
(defn variable [name] (fn [args] (args name)))

(defn divide-func ([a] (/ 1.0 (double a)))
      ([a & args] (reduce #(/ (double %1) (double %2)) a args)))
(defn sumexp-func [& operands] (apply + (map #(Math/exp %) operands)))
(defn softmax-func [& operands] (let [exp (map #(Math/exp %) operands)]
                                     (/ (first exp) (double (apply + exp)))))

(defn makeFuncOperation [func] (fn [& operands] (fn [args] (apply func (map #(% args) operands)))))
(def add (makeFuncOperation +))
(def subtract (makeFuncOperation -))
(def multiply (makeFuncOperation *))
(def divide (makeFuncOperation divide-func))
(defn negate [operand] (subtract operand))
(def sumexp (makeFuncOperation sumexp-func))
(def softmax (makeFuncOperation softmax-func))

(def OPERATIONS-FUNCTIONS {"+"       add,
                           "-"       subtract,
                           "*"       multiply,
                           "/"       divide,
                           "negate"  negate
                           "sumexp"  sumexp
                           "softmax" softmax})

(defn parser [constType varType tokens]
      (fn [expr]
          (letfn [(parseExpression [x]
                                   (cond
                                     (number? x) (constType x)
                                     (symbol? x) (varType (str x))
                                     (list? x) (apply (tokens (str (first x)))
                                                      (map parseExpression (rest x)))))]
                 (parseExpression (read-string expr)))))

(def parseFunction (parser constant variable OPERATIONS-FUNCTIONS))

;--------------------------------------------------------HW11----------------------------------------------------------
(load-file "proto.clj")

(def evaluate (method :evaluate))
(def toString (method :toString))
(def diff (method :diff))
(def toStringInfix (method :toStringInfix))

(def _value (field :value))
(def _name (field :name))
(def _func (field :func))
(def _sign (field :sign))
(def _diffRule (field :diffRule))
(def _operands (field :operands))

(defn makeExpression
      [ctor evaluate toString diff toStringInfix]
      (constructor
        ctor
        {:evaluate      evaluate
         :toString      toString
         :diff          diff
         :toStringInfix toStringInfix}))

(declare ZERO)
(def Constant
  (makeExpression
    (fn [this value]
        (assoc this :value value))
    (fn [this _] (_value this))
    (fn [this] (str (_value this)))
    (fn [_ _] ZERO)
    toString))
(def ZERO (Constant 0))
(def ONE (Constant 1))

(def Variable
  (makeExpression
    (fn [this name]
        (assoc this :name name))
    (fn [this args] (args (clojure.string/lower-case (str (first (_name this))))))
    (fn [this] (_name this))
    (fn [this name] (if (= name (_name this)) ONE ZERO))
    toString))

(def AbstractOperation
  (makeExpression
    (fn [this & operands] (assoc this :operands operands))
    (fn [this args] (apply (_func this) (map #(evaluate % args) (_operands this))))
    (fn [this] (str "(" (_sign this) " " (clojure.string/join " " (map toString (_operands this))) ")"))
    (fn [this name] ((_diffRule this) (_operands this) (map #(diff % name) (_operands this))))
    (fn [this] (if (== (count (_operands this)) 1)
                 (str (_sign this) "(" (toStringInfix (first (_operands this))) ")")
                 (str "(" (toStringInfix (first (_operands this)))
                      " " (_sign this) " "
                      (toStringInfix (second (_operands this))) ")")))))

(defn makeOperation
      [func sign diffRule]
      (fn [& operands] (assoc (apply AbstractOperation operands)
                              :func func
                              :sign sign
                              :diffRule diffRule)))

(defn AddDiffRule
      [operation]
      (fn [_ diffOperands] (operation diffOperands)))

(def Add (makeOperation + "+" (AddDiffRule #(apply Add %))))
(def Subtract (makeOperation - "-" (AddDiffRule #(apply Subtract %))))
(def Negate (makeOperation - "negate" (AddDiffRule #(apply Negate %))))

(declare Multiply)
(defn MultiplyDiffRule
      [operands diffOperands]
      (apply Add (map #(apply Multiply (assoc (vec operands) % (nth diffOperands %)))
                      (range (count operands)))))

(def Multiply (makeOperation * "*" MultiplyDiffRule))

(declare Divide)
(defn DivideDiffRule
      [operands diffOperands]
      (let [a (first operands) da (first diffOperands)
            b (apply Multiply (rest operands))
            db (MultiplyDiffRule (rest operands) (rest diffOperands))]
           (if (== (count operands) 1)
             (Negate (Divide da a a))
             (Divide
               (Subtract (Multiply da b) (Multiply a db))
               b b))))

(def Divide (makeOperation divide-func "/" DivideDiffRule))

(declare Sumexp)
(defn SumexpDiffRule
      [operands diffOperands]
      (apply Add (map #(Multiply (Sumexp %1) %2) operands diffOperands)))

(def Sumexp (makeOperation sumexp-func "sumexp" SumexpDiffRule))

(declare Softmax)
(defn SoftmaxDiffRule
      [operands diffOperands]
      (Multiply (apply Softmax operands)
                (Subtract (first diffOperands)
                          (Divide
                            (SumexpDiffRule operands diffOperands)
                            (apply Sumexp operands)))))

(def Softmax (makeOperation softmax-func "softmax" SoftmaxDiffRule))

(defn bitwise [operation]
      #(Double/longBitsToDouble (apply operation (mapv (fn [x] (Double/doubleToLongBits x)) %&))))
(def BitAnd (makeOperation (bitwise bit-and) "&" nil))
(def BitOr (makeOperation (bitwise bit-or) "|" nil))
(def BitXor (makeOperation (bitwise bit-xor) "^" nil))
(def BitImpl (makeOperation (bitwise #(bit-or (bit-not %1) %2)) "=>" nil))
(def BitIff (makeOperation (bitwise #(bit-not (bit-xor %1 %2))) "<=>" nil))

(def OPERATIONS_OBJECT {"+"       Add,
                        "-"       Subtract,
                        "*"       Multiply,
                        "/"       Divide,
                        "negate"  Negate
                        "sumexp"  Sumexp
                        "softmax" Softmax
                        "&"       BitAnd
                        "|"       BitOr
                        "^"       BitXor
                        "=>"      BitImpl
                        "<=>"     BitIff})

(def parseObject (parser Constant Variable OPERATIONS_OBJECT))

;--------------------------------------------------------HW12----------------------------------------------------------
(load-file "parser.clj")

(def *all-chars (mapv char (range 0 128)))
(def *digit (+char (apply str (filter #(Character/isDigit %) *all-chars))))
(def *space (+char (apply str (filter #(Character/isWhitespace %) *all-chars))))
(def *ws (+ignore (+star *space)))
(def *number (+map Constant (+map read-string (+str (+seqf #(flatten %&)
                                                           (+opt (+char "-"))
                                                           (+plus *digit)
                                                           (+opt (+seq (+char ".") (+plus *digit))))))))
(def *variable (+map Variable (+str (+plus (+char "xyzXYZ")))))
(defn *string [s] (apply +seqf str (mapv #(+char (str %)) s)))
(defn *operation-sign [operations]
      (+map OPERATIONS_OBJECT (+seqn 0 *ws (apply +or (mapv *string operations)) *ws)))

(declare *expression)
(defn *unary-operation [operations priority]
      (+seqf (fn [operations fact] (reduce #(%2 %1) fact (reverse operations)))
             (+star (*operation-sign operations))
             (+or *variable *number (+seqn 1 (+char "(") (delay (*expression (dec priority))) (+char ")")))))
(defn *left-operation [operations priority]
      (+seqf (fn [first rest] (reduce (fn [first [op second]] (op first second)) first rest))
             (*expression (dec priority))
             (+star (+seq
                      (*operation-sign operations)
                      (*expression (dec priority))))))
(defn *right-operation [operations priority]
      (letfn [(fun [first [op second]] (if (nil? op) first (op first second)))
              (rec []
                   (+seqf fun
                          (*expression (dec priority))
                          (+opt (+seq (*operation-sign operations)
                                      (delay (rec))))))]
             (rec)))

(def OPERATIONS [[*unary-operation ["negate"]]
                 [*left-operation ["*" "/"]]
                 [*left-operation ["+" "-"]]
                 [*left-operation ["&"]]
                 [*left-operation ["|"]]
                 [*left-operation ["^"]]
                 [*right-operation ["=>"]]
                 [*left-operation ["<=>"]]])
(def START_IND (dec (count OPERATIONS)))
(defn *expression [priority]
      (+seqn 0 *ws
             (if (>= priority 0) ((first (OPERATIONS priority)) (second (OPERATIONS priority)) priority) (*expression START_IND))
             *ws))

(def parseObjectInfix (+parser (*expression START_IND)))