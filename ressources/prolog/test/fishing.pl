use_module(library(jpl)).

%a fish is free or caught
%a fisherman is fishing, bredouille or victorious

%initial state
fish(maurice).
free(maurice).

fisherman(tom).
fishing(tom).

victorious(X) :- 
	fisherman(X),
	fish(Y),
	caught(X,Y).
	
caught(X,Y) :-
	free(Y),
	fishing(X),
	%jpl_call('java.lang.System',getProperty,['user.dir'],F),
	%write(F).
	jpl_call('PrologCalls',test,[], @(void)),
	jpl_call('PrologCalls',hooked,[X,Y],R),
	jpl_is_true(R).

	
