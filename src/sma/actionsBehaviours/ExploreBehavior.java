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
import weka.core.Debug;

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
		
		if (target == null && !setTarget()){ 
			System.out.println("No target found");
			randomMove();
			return;
		}
		
		System.out.println("Distance to target : "+agent.getCurrentPosition().distance(target));
		
		if (agent.getCurrentPosition().distance(target) < AbstractAgent.NEIGHBORHOOD_DISTANCE / 2f){
			Vector3f nei = findInterestingNeighbor();
			if(nei != null && false){
				target = nei;
				agent.moveTo(target);
			}else{
				addInterestPoint();
				target = null;
			}
		}
		
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
			return findLowestNeighbor();
		}
	}

	
	Vector3f findHighestNeighbor(){
		ArrayList<Vector3f> points = agent.sphereCast(agent.getSpatial(), AbstractAgent.VISION_DISTANCE, AbstractAgent.CLOSE_PRECISION, AbstractAgent.VISION_ANGLE);
		return getHighest(points);
	}
	
	Vector3f findLowestNeighbor(){
		ArrayList<Vector3f> points = agent.sphereCast(agent.getSpatial(), AbstractAgent.VISION_DISTANCE, AbstractAgent.CLOSE_PRECISION, AbstractAgent.VISION_ANGLE);
		return getLowest(points);
	}
	
	boolean setTarget(){
		
		Type t = getNextTargetType();
		
		if(t == Type.Offensive){
			target = findOffensiveTarget();
		}else{
			target = findDefensiveTarget();
		}
		
		if(target != null){
			agent.goTo(target, MoveMode.NORMAL);
			System.out.println("New Target : "+target.toString());
		}
		
		return target != null;
	}
	
	Vector3f getHighest(ArrayList<Vector3f> points){
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
	
	Vector3f getLowest(ArrayList<Vector3f> points){
		float minHeight = 256;
		Vector3f best = null;
		
		for(Vector3f v3 : points){
			if (v3.getY() < minHeight){
				best = v3;
				minHeight = v3.getY();
			}
		}
		return best;
	}
	
	
	Vector3f findOffensiveTarget(){
		ArrayList<Vector3f> points = agent.sphereCast(agent.getSpatial(), AbstractAgent.VISION_DISTANCE, AbstractAgent.FAR_PRECISION, AbstractAgent.VISION_ANGLE);
		
		ArrayList<Vector3f> toRemove = new ArrayList<>();
		
		for(InterestPoint intPoint : agent.offPoints)
			for(Vector3f point : points)
				if(intPoint.isInInfluenceZone(point, Type.Offensive)){
					toRemove.add(point);
					System.out.println("In influence zone");
				}
					
		
		System.out.println("Removing done : "+points.size()+" R : "+toRemove.size());
		
		for (Vector3f v3 : toRemove){ points.remove(v3); }
		
		return getHighest(points);
	}
	
	
	Vector3f findDefensiveTarget(){
		ArrayList<Vector3f> points = agent.sphereCast(agent.getSpatial(), AbstractAgent.VISION_DISTANCE, AbstractAgent.FAR_PRECISION, AbstractAgent.VISION_ANGLE);
		
		ArrayList<Vector3f> toRemove = new ArrayList<>();
		
		for(InterestPoint intPoint : agent.defPoints)
			for(Vector3f point : points)
				if(intPoint.isInInfluenceZone(point, Type.Defensive))
					toRemove.add(point);
				
		
		for (Vector3f v3 : toRemove){ points.remove(v3); }
		
		return getLowest(points);
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























