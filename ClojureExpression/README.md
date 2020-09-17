# Parser combinator

### Functions

- Functions `constant`, `variable`, `add`, `subtract`, `multiply`, `divide`, `sumexp` and `softmax`, that takes an arbitrary number of arguments
- An expression parser, that reads expressions in Clojure form.


For example:  
Expression `2x-3` representation:
```clj
  (def expr
   (subtract
    (multiply
      (constant 2)
      (variable "x"))
    (constant 3)))
```
`(parseFunction "(- (* 2 x) 3)")` is equivalent to `expr`.

### Objects

- Developed constructors `Constant`, `Variable`, `Add`, `Subtract`, `Multiply` и `Divide` to represent expressions with variables.
- A function `evaluate` , that calculates an expression
- A function `toString` , that displays an expression in Clojure form
- A function `parseObject` , that parses expressions in Clojure form
- A function `diff` , that returns an expression representing the derivative with respect to a given variable.

Examples:

```clj
;; 2x - 3
(def expr
  (Subtract
    (Multiply
      (Constant 2)
      (Variable "x"))
    (Const 3)))
    
(evaluate expr {"x" 2}) ;; 1

(toString expr) ;; "(- (* 2 x) 3)"

(parseObject "(- (* 2 x) 3)") ;; expr

(diff expr "x") ;; (Constant 2),

```

### Parser combinator

- A library of functions, that allows to create a parser
For example:
```clj

(def *expression
  (reduce (fn [*value [+seq_assoc_op ops]]
            (+seq_assoc_op *value ops))
          *single_value
          [[+left_assoc ["*" "/"]]
           [+left_assoc ["+" "-"]]
           [+left_assoc ["&"]]
           [+left_assoc ["|"]]
           [+left_assoc ["^"]]
           [+right_assoc ["=>"]]
           [+left_assoc ["<=>"]]]))

(def parseObjectInfix (+parser *expression))

```
That's how you create a parser of arithmetic and logic operations in infix form

- A function `parseObjectInfix` parses expressions written in infix form.
- A function `toStringInfix`, that di splays an expression in infix form
- The system of operator priorities has been implemented, as well as left-associative and right-associative operations.

Examples:

```clj

(toStringInfix (parseObjectInfix "2 * x - 3")) ;; "((2.0 * x) - 3.0)"

(toStringInfix (parseObjectInfix "2 * x + 3 * z")) ;; "((2.0 * x) - (3.0 * z))"

(toStringInfix (parseObjectInfix "2 ** 3 ** 4")) ;; "(2.0 ** (3.0 ** 4.0))"

(toStringInfix (parseObjectInfix "2 + 3 + 4")) ;; "((2.0 + 3.0) + 4.0)"

```
