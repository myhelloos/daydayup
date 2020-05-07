#lang racket

(define (square x) (* x x))

(define (average x y) (/ (+ x y) 2))

(define (sum-of-square x y) (+ (square x) (square y)))

(define (mean-square x y) (average (square x) (square y)))

(define (sum-larger a b c)
  (cond
    ((and (>= a c) (>= b c)) (+ a b))
    ((and (>= a b) (>= c b)) (+ a c))
    ((and (>= c a) (>= b a)) (+ b c))
    ))


(define (sqrt x)
  (define (sqrt-iter guess)
    (if (good-enough? guess)
        guess
        (sqrt-iter (improve guess))))
  (define (good-enough? guess)
    (= (improve guess) guess))
  (define (improve guess)
    (average guess (/ x guess)))
  (sqrt-iter 1.0))

(define (cubic x) (* x x x))

(define (cubic-root x)
  (define (cubic-root-iter guess)
    (if (good-enough? guess)
        guess
        (cubic-root-iter (improve guess))))
  (define (good-enough? guess)
    (= (improve guess) guess))
  (define (improve guess)
    (/ (+ (/ x (square guess)) (* 2 guess)) 3))
  (cubic-root-iter 1.1))
