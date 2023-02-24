sieve_cycle(J, I, N) :-
    J =< N, assert(comp(J)),
    J1 is J + I, sieve_cycle(J1, I, N).

sieve(I, N) :-
    \+ comp(I), I1 is I * I, sieve_cycle(I1, I, N).
sieve(I, N) :-
    I * I =< N, !, I1 is I + 1, sieve(I1, N).

init(MAX_N) :- sieve(2, MAX_N).

prime(N) :- N > 1, \+ comp(N).

composite(N) :- comp(N).

min_divisors_list([]) :- !.
min_divisors_list([H]) :- prime(H), !.
min_divisors_list([H1, H2 | T]) :-
    H1 =< H2, prime(H1), !,
    min_divisors_list([H2 | T]).

mult([], 1) :- !.
mult([H], H) :- !.
mult([H | T], M) :- mult(T, M1), M is H * M1.

min_divisor(N, I, R) :-
    prime(I), I * I =< N, 0 is N mod I, !, R is I.
min_divisor(N, I, R) :-
    I * I =< N, I1 is I + 1, min_divisor(N, I1, R).

prime_divisors(1, []) :- !.
prime_divisors(P, [P]) :- prime(P), !.
prime_divisors(N, [DIV | T]) :-
    number(N), N > 1, !,
    min_divisor(N, 2, DIV), N1 is N / DIV,
    prime_divisors(N1, T).
prime_divisors(N, Divisors) :-
    mult(Divisors, N), min_divisors_list(Divisors).

merge([], L, L) :- !.
merge(L, [], L) :- !.
merge([H1 | T1], [H2 | T2], R) :-
    H1 = H2, !, merge(T1, T2, R1),
    R = [H1 | R1].
merge([H1 | T1], [H2 | T2], R) :-
    H1 < H2, !, merge(T1, [H2 | T2], R1),
    R = [H1 | R1].
merge([H1 | T1], [H2 | T2], R) :-
merge([H2 | T2], [H1 | T1], R).

lcm(A, B, LCM) :-
    prime_divisors(A, DA), prime_divisors(B, DB),
    merge(DA, DB, D), mult(D, LCM).

