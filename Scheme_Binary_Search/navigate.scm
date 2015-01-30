; Eric Hotinger

(define path '(73( 49( 15( 10 () () )( 20( 17 () () )( 30()( 42 () () ))))( 53 ()( 64 () () )))
			  ( 134( 133( 94( 82( 75 () () )())( 108( 103 () () )( 110 () () )))())( 135()( 136()( 152( 141 () () )())))))
) ; end path definition


(define found_list '("found:")) ; list to keep track of visited nodes when searching for a value

 (define (appendList list1 list2)
	(cond ((null? list1) list2)
		(else 
			(cons (car list1)
				(appendList (cdr list1) list2)
			) ; end cons
		) ; end else
	) ; end cond
) ; end appendList


(define (search path value output)
	(cond 
		((null? path) ; NULL NODE (not found)
		(cons '"not found:" (cons value null)))

		((< value (car path)) ; LEFT subtree (robot turns "right")
			(search (cadr path) value (appendList output (cons (car path) (cons 'R null)))))
		
		((> value (car path)) ; RIGHT subtree (robot turns "left")
			(search (caddr path) value (appendList output (cons (car path) (cons 'L null)))))
		
		((= value (car path)) ; NODE matches VALUE (robot doesn't turn)
			(appendList output (cons (car path) null)))
	) ; end cond
) ; end search

(define (navigate value path)
	(search path value found_list)
); end navigate
  
(navigate 42 path)
(navigate 141 path)
(navigate 103 path)
(navigate 81 path)
(navigate 73 path)