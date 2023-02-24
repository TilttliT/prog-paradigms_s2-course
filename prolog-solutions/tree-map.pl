%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%---------AVL_Tree---------%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

max(A, B, A) :- A > B, !.
max(A, B, B).

depth(null, 0) :- !.
depth(node(_, _, Depth, _, _, _), Depth).

size(null, 0) :- !.
size(node(_, _, _, Size, _, _), Size).

diffDepth(node(_, _, _, _, Left, Right), Diff) :-
  depth(Left, DL), depth(Right, DR), Diff is DR - DL.

node(Key, Value, Left, Right, node(Key, Value, Depth, Size, Left, Right)) :-
  depth(Left, DL), depth(Right, DR),
  size(Left, SL), size(Right, SR),
  max(DL, DR, D), Depth is D + 1, Size is SL + SR + 1.

map_get(node(Key, Value, _, _, _, _), Key, Value) :- !.
map_get(node(K, _, _, _, L, _), Key, Value) :-
  Key < K, !, map_get(L, Key, Value).
map_get(node(_, _, _, _, _, R), Key, Value) :-
  map_get(R, Key, Value).

rotateLeft(node(K, V, _, _, L, node(K1, V1, _, _, L1, R1)), Result) :-
  node(K, V, L, L1, NewL),
  node(K1, V1, NewL, R1, Result).
rotateRight(node(K, V, _, _, node(K1, V1, _, _, L1, R1), R), Result) :-
  node(K, V, R1, R, NewR),
  node(K1, V1, L1, NewR, Result).

balance(node(K, V, _, _, L, R), Result) :-
  diffDepth(node(K, V, _, _, L, R), 2), !,
  balance(R, NewR),
  rotateLeft(node(K, V, _, _, L, NewR), Result).
balance(node(K, V, _, _, L, R), Result) :-
  diffDepth(node(K, V, _, _, L, R), -2), !,
  balance(L, NewL),
  rotateRight(node(K, V, _, _, NewL, R), Result).
balance(Map, Result) :-
  diffDepth(Map, Diff), Diff < 0, !,
  rotateRight(Map, Result).
balance(Map, Result) :-
  diffDepth(Map, Diff), Diff > 0, !,
  rotateLeft(Map, Result).
balance(Result, Result).

minimum(Map, Map) :- Map = node(_, _, _, _, null, _), !.
minimum(node(_, _, _, _, L, _), Result) :- minimum(L, Result).

map_remove(null, _, null) :- !.
map_remove(Map, Key, Result) :- map_remove_(Map, Key, Map1), balance(Map1, Result).

map_remove_(node(K, V, _, _, L, R), Key, Result) :-
  Key < K, !, map_remove(L, Key, NewL), node(K, V, NewL, R, Result).
map_remove_(node(K, V, _, _, L, R), Key, Result) :-
  Key > K, !, map_remove(R, Key, NewR), node(K, V, L, NewR, Result).
map_remove_(node(_, _, _, _, L, null), _, L) :- !.
map_remove_(node(_, _, _, _, null, R), _, R) :- !.
map_remove_(node(_, _, _, _, L, R), _, Result) :-
  minimum(R, node(MinK, MinV, _, _, _, _)),
  map_remove(R, MinK, NewR), node(MinK, MinV, L, NewR, Result).

map_put(null, Key, Value, Result) :- !, node(Key, Value, null, null, Result).
map_put(node(Key, _, _, _, L, R), Key, Value, Result) :- !, node(Key, Value, L, R, Result).
map_put(Map, Key, Value, Result) :- map_put_(Map, Key, Value, Map1), balance(Map1, Result).

map_put_(node(K, V, _, _, L, R), Key, Value, Result) :-
  Key < K, !, map_put(L, Key, Value, NewL), node(K, V, NewL, R, Result).
map_put_(node(K, V, _, _, L, R), Key, Value, Result) :-
  map_put(R, Key, Value, NewR), node(K, V, L, NewR, Result).

map_build([], null) :- !.
map_build([(HeadK, HeadV) | T], Result) :-
  map_build(T, Result1), map_put(Result1, HeadK, HeadV, Result).

not_equal(A, A, 0) :- !.
not_equal(_, _, 1).

map_headMapSize(null, _, 0) :- !.
map_headMapSize(node(K, _, _, _, L, _), Key, Size) :-
  K > Key, !, map_headMapSize(L, Key, Size).
map_headMapSize(node(K, _, _, _, L, R), Key, Size) :-
  size(L, S1), not_equal(K, Key, S2), map_headMapSize(R, Key, Size1),
  Size is Size1 + S1 + S2.

map_subMapSize(Map, FromKey, ToKey, 0) :- FromKey >= ToKey, !.
map_subMapSize(Map, FromKey, ToKey, Size) :-
  map_headMapSize(Map, FromKey, S1), map_headMapSize(Map, ToKey, S2),
  Size is S2 - S1.