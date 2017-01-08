package sma;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class InterestPoint {
	
	public static enum Type {
		Offensive,
		Defensive;
	}
	
	public static float EVAL_PRECISION = 2f;
	public static float EVAL_DISTANCE = 35f;
	public static float EVAL_ANGLE = 180f;
	
	public static float HEIGHT_COEFF = 1f;
	
	public static float INFLUENCE_ZONE = 4f;
	
	public Type type;
	public Vector3f position;
	public float value;
	
	public InterestPoint(Type type, Spatial sp){
		this.type = type;
		this.position = sp.getWorldTranslation();
		
		if(type == Type.Offensive)
			EvalDefendValue(sp);
		else
			EvalDefendValue(sp);
	}
	

	public void EvalOffendValue(Spatial sp){
		value = sp.getWorldTranslation().y;
	}
	
	public void EvalDefendValue(Spatial sp){
		value = -sp.getWorldTranslation().y;
	}
	
	public boolean isInInfluenceZone(Vector3f oth, Type othType){
		return type == othType & position.distance(oth) < INFLUENCE_ZONE; 
	}
	
	
	
}
