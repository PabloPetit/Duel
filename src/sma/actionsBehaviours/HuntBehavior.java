package sma.actionsBehaviours;

import java.util.ArrayList;

import com.jme3.math.Vector3f;

import dataStructures.tuple.Tuple2;
import env.jme.NewEnv;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import sma.AbstractAgent;
import sma.InterestPoint;
import sma.agents.FinalAgent;

public class HuntBehavior extends TickerBehaviour {

	
	FinalAgent agent;
	
	
	InterestPoint target;
	
	
	public HuntBehavior(Agent a, long period) {
		super(a, period);
		agent = (FinalAgent)((AbstractAgent)a);
	}

	
	private static final long serialVersionUID = -183650362971906511L;

	@Override
	protected void onTick() {
		
		
		Tuple2<Vector3f, String> enemy = checkEnemyInSight();
		
		if (enemy != null){
			// ALL WEAPONS OUT !!
		}
		
		if (target == null){
			
			InterestPoint point = findNextInterestPoint();
			
			if (point != null){
				
				//goto next
			}else{
				// Go back to explore
			}
		}else if (true ){// distance target < X
			//set time
		}
	}
	
	
	public Tuple2<Vector3f, String> checkEnemyInSight(){
		ArrayList<Tuple2<Vector3f, String>> enemies = agent.getVisibleAgents(AbstractAgent.VISION_DISTANCE, AbstractAgent.VISION_ANGLE);
		
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
			if (tmp > value  && (target == null || (point != target))){
				best = point;
				value = tmp;
			}
			
		}
		
		return best;
		
	}
	
	public float evalutateInterestPoint(InterestPoint point, long time){
		
		float dist = agent.getSpatial().getWorldTranslation().distance(point.position);
		long idleness = (time - point.lastVisit) / 1000;;
		return NewEnv.MAX_DISTANCE - dist + 5 * Math.max(30 - idleness, 0); // Au pif
		
	}
	
	
	

}
