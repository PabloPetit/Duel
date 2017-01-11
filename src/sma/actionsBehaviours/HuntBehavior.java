package sma.actionsBehaviours;

import java.util.ArrayList;

import com.jme3.math.Vector3f;

import dataStructures.tuple.Tuple2;
import env.jme.NewEnv;
import env.jme.Situation;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import sma.AbstractAgent;
import sma.InterestPoint;
import sma.agents.FinalAgent;

public class HuntBehavior extends TickerBehaviour {

	
	FinalAgent agent;
	
	
	InterestPoint target;
	InterestPoint lastTarget;
	
	
	public HuntBehavior(Agent a, long period) {
		super(a, period);
		agent = (FinalAgent)((AbstractAgent)a);
	}
	
	public HuntBehavior(Agent a, long period, InterestPoint firstPoint) { // Called when getting back from Attack
		super(a, period);
		agent = (FinalAgent)((AbstractAgent)a);
		target = firstPoint;
		System.out.println("Starting the Hunt");
	}

	
	private static final long serialVersionUID = -183650362971906511L;

	@Override
	protected void onTick() {
		
		
		Tuple2<Vector3f, String> enemy = checkEnemyInSight(false);
		agent.lastAction = Situation.HUNT;
		
		if(target != null){
			System.out.println("Distance to target : "+agent.getSpatial().getWorldTranslation().distance(target.position));
		}else{
			System.out.println("no target");
		}
		
		if (target != null && agent.getSpatial().getWorldTranslation().distance(target.position) < 5f){
			
			target.lastVisit = System.currentTimeMillis();
			lastTarget = target;
			target = null;
			enemy = checkEnemyInSight(true);
			System.out.println("CheckPoint !");
			
		}
		
		if (enemy != null){
			System.out.println("Enemy in sight");
			//ASK prolog to chose between { Attack , Follow }
			return;
		}
		
		if (target == null){
			System.out.println("Looking for new target ...");
			
			InterestPoint point = findNextInterestPoint();
			
			if (point != null){
				target = point;
				agent.goTo(point.position);
				System.out.println("Found it : "+point.position);
				
			}else{
				System.out.println("Back to explore");
				// Go back to exploration
				agent.addBehaviour(agent.explore);
				stop();
			}
		}
		
	}
	
	
	public Tuple2<Vector3f, String> checkEnemyInSight(boolean fullVision){
		ArrayList<Tuple2<Vector3f, String>> enemies = agent.getVisibleAgents((fullVision)?360f:AbstractAgent.VISION_DISTANCE, AbstractAgent.VISION_ANGLE);
		
		Tuple2<Vector3f, String> best = null;
		float value = -1f;
		
		for(Tuple2<Vector3f, String> enemy : enemies){
			float tmp = evaluateEnemy(enemy);
			if (tmp > value){
				best = enemy;
				value = tmp;
			}
		}
		return best;
	}
	
	public float evaluateEnemy(Tuple2<Vector3f, String> enemy){
		return NewEnv.MAX_DISTANCE - agent.getSpatial().getWorldTranslation().distance(enemy.getFirst());
	}
	
	public InterestPoint findNextInterestPoint(){
		
		float value = -1f;
		InterestPoint best = null;
		long time = System.currentTimeMillis();
		
		for (InterestPoint point : agent.offPoints){
			
			float tmp = evalutateInterestPoint(point, time);
			if (tmp > value  && point != lastTarget){
				best = point;
				value = tmp;
			}
			
		}
		
		return best;
		
	}
	
	public float evalutateInterestPoint(InterestPoint point, long time){
		
		float dist = agent.getSpatial().getWorldTranslation().distance(point.position);
		long idleness = (time - point.lastVisit) / 1000;
		return NewEnv.MAX_DISTANCE - dist + 5 * Math.max(30 - idleness, 0); // Au pif
		
	}

}
