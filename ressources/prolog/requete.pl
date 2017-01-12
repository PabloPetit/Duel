explore_points(OffSize,DefSize):-
	OffSize>2*DefSize.

being_attacked(Time):-
	Time<10.

areaCovered(MapWidth,Radius,Size):-
	MapWidth*MapWidth-2*pi*Radius*Size.

inGoodHealth(life):-
	life>3.

toShoot(Probability):-
	Probability>0.6.

/* DECISIONS */

explore_off(Time,OffSize,DefSize,MapWidth,Radius):-
	not(being_attacked(Time)),
	explore_points(OffSize,DefSize),
	areaCovered(MapWidth,Radius,OffSize)<20,
	jpl_call('PrologBehavior',executeExploreOff,[],@(void)).

explore_def(Time,OffSize,DefSize,MapWidth,Radius):-
	not(being_attacked(Time)),
	not(explore_points(OffSize,DefSize)),
	areaCovered(MapWidth,Radius,DefSize)<20,
	jpl_call('PrologBehavior',executeExploreDef,[],@(void)).

hunt(Life,Time,OffSize,DefSize,MapWidth,Radius,EnemyInSight):-
	not(being_attacked(Time)),
	not(areaCovered(MapWidth,Radius,OffSize)<20),
	not(areaCovered(MapWidth,Radius,DefSize)<20);
	inGoodHealth(Life),
	being_attacked(Time),
	not(EnemyInSight),
	jpl_call('PrologBehavior',executeHunt,[],@(void)).

follow(Life,Time,EnemyInSight,P):-
	inGoodHealth(Life),
	being_attacked(Time),
	EnemyInSight,
	not(toShoot(P)),
	jpl_call('PrologBehavior',executeFollow,[],@(void)).

shoot(Life,Time,EnemyInSight,P):-
	inGoodHealth(Life),
	being_attacked(Time),
	EnemyInSight,
	toShoot(P),
	jpl_call('PrologBehavior',executeShoot,[],@(void)).

retreat(Life,Time):-
	not(inGoodHealth(Life)),
	being_attacked(Time),
	jpl_call('PrologBehavior',executeRetreat,[],@(void)).
