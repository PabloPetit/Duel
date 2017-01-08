package sma.actionsBehaviours;

import java.util.ArrayList;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import sma.AbstractAgent;
import sma.InterestPoint;
import sma.InterestPoint.Type;
import sma.actionsBehaviours.LegalActions.LegalAction;
import sma.agents.FinalAgent;
import sma.agents.FinalAgent.MoveMode;

public class ExploreBehavior extends TickerBehaviour {

	
	private static final long serialVersionUID = 4958939169231338495L;
	public static final float RANDOM_MAX_DIST = 10f;
	public static final int RANDOM_REFRESH = 20;
	
	public static final float VISION_ANGLE = 360f;
	public static final float VISION_DISTANCE = 350f;
	public static final float CAST_PRECISION = 2f;
	
	
	FinalAgent agent;
	
	private Vector3f target;
	private Type targetType;
	
	private long randDate;
	
	long time;
	
	public ExploreBehavior(Agent a, long period) {
		super(a, period);
		agent = (FinalAgent)((AbstractAgent)a); // I know, i know ...
		target = null;
		randDate = 0;
		time = System.currentTimeMillis();
		
	}

	
	protected void onTick(){
		
		
		
		if (target == null && !setTarget()){ // if no target and no available target
			randomMove();
			return;
		}
		/*
		if (agent.getCurrentPosition().distance(target) < 1f){
			Vector3f nei = findInterestingNeighbor();
			if(nei != null){
				target = nei;
				agent.moveTo(target);
			}else{
				addInterestPoint();
				target = null;
			}
		}
		*/
		
		
	}
	
	
	
	void addInterestPoint(){
		if(targetType == Type.Offensive){
			agent.offPoints.add(new InterestPoint(Type.Offensive,agent.getSpatial()));
		}else{
			agent.defPoints.add(new InterestPoint(Type.Defensive,agent.getSpatial()));
		}
	}
	
	
	Vector3f findInterestingNeighbor(){
		if(targetType == Type.Offensive){
			return findHighestNeighbor();
		}else{
			// find lower neighbor
		}
		return null;
	}
	

	
	Vector3f findHighestNeighbor(){
		return null;
	}
	
	boolean setTarget(){
		
		
		if(agent.getCurrentPosition().getY() > -250f){
			return false;
		}
		
		
		Type t = getNextTargetType();
		
		if(t == Type.Offensive){
			target = findOffensiveTarget();
		}else{
			target = findDefensiveTarget();
		}
		
		if(target != null){
			agent.goTo(target, MoveMode.NORMAL);
			System.out.println("New Target : "+target.toString());
			System.out.println("From pos   : "+agent.getCurrentPosition().toString());
		}
		
		return target != null;
	}
	
	
	Vector3f findOffensiveTarget(){
		//ArrayList<Vector3f> points = agent.sphereCast(agent.getSpatial(), VISION_ANGLE, VISION_DISTANCE, CAST_PRECISION);
		ArrayList<Vector3f> points = agent.goldenSphereCast(agent.getSpatial(), 3000f, 1000);
		
		
		float maxHeight = -256;
		Vector3f best = null;
		
		for(Vector3f v3 : points){
			if (v3.getY() > maxHeight){
				best = v3;
				maxHeight = v3.getY();
			}
		}
		
		return best;
		
	}
	
	Vector3f findDefensiveTarget(){
		return null;
	}
	
	
	Type getNextTargetType(){
		//Ask prolog which kind of interest Point we should look for
		return Type.Offensive;
	}
	
	
	void randomMove(){
		long time = System.currentTimeMillis();
		if(time - randDate > RANDOM_REFRESH * getPeriod()){
			agent.randomMove(); // Should be something in the neighborhound of the agent, and not some random point in the all map
			randDate = time;
			//agent.getEnvironement().drawDebug(agent.getCurrentPosition(), agent.getDestination());
		}
	}
}























