explore_points(OffSize, DefSize):-OffSize>2*DefSize.

being_attacked(Time):-Time<10.

areaCovered(MapWidth, Radius, Size):-MapWidth*MapWidth-2*pi*Radius*Size.

inGoodHealth(life):-life>3.

toShoot(Probability):-Probability>0.6.

/* DECISIONS */

explore_off(OffSize, DefSize, MapWidth, Radius):
	not(being_attacked(Time)),
	explore_points(OffSize, DefSize),
	areaCovered(MapWidth, Radius, OffSize)<20,
	jpl_call('PrologCalls', executeExploreOff, [], @(void)).

explore_def(OffSize, DefSize, MapWidth, Radius):
	not(being_attacked(Time)),
	not(explore_points(offSize, DefSize))
	areaCovered(MapWidth, Radius, DefSize)<20,
	jpl_call('PrologCalls', executeExploreDef, [], @(void)).

hunt(Life, Time, OffSize, DefSize, MapWidth, Radius):
	inGoodHealth(Life),
	being_attacked(Time),
	not(areaCovered(MapWidth, Radius, OffSize)<20),
	not(areaCovered(MapWidth, Radius, DefSize)<20);
	being_attacked(Time),
	EnemyInSight,
	jpl_call('PrologCalls', executeHunt, [], @(void)).

follow(EnemyInSight, P):
	inGoodHealth(Life),
	being_attacked(Time),
	EnemyInSight,
	not(toShoot(P)),
	jpl_call('PrologCalls', executeFollow, [], @(void)).

shoot(EnemyInSight, P):
	inGoodHealth(Life),
	being_attacked(Time),
	EnemyInSight,
	toShoot(P),
	jpl_call('PrologCalls', executeShoot, [], @(void)).

retreat(Life, Time):
	not(inGoodHealth(Life)),
	being_attacked(Time),
	jpl_call('PrologCalls', executeRetreat, [], @(void)).