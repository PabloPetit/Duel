package sma.agents;
import java.util.ArrayList;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import env.jme.Environment;
import env.jme.NewEnv;
import sma.AbstractAgent;
import sma.InterestPoint;
import sma.actionsBehaviours.ExploreBehavior;
import sma.actionsBehaviours.TempSphereCast;

public class FinalAgent extends AbstractAgent{

	
	private static final long serialVersionUID = 5215165765928961044L;
	
	public static final long EXPLORE_PERIOD = 1000;
	
	public enum MoveMode {
		
		NORMAL,
		LEGAL;
		
	}
	
	public boolean friendorFoe;// ?
	
	public ArrayList<InterestPoint> offPoints;
	public ArrayList<InterestPoint> defPoints;
	
	ExploreBehavior explore;
	
	
	protected void setup(){
		super.setup();
		
		deploiment();
		
		offPoints = new ArrayList<>();
		defPoints = new ArrayList<>();
		
		explore = new ExploreBehavior(this, EXPLORE_PERIOD);
		
		addBehaviour(explore); // Prolog should order that
		//addBehaviour(new TempSphereCast(this, 20));
	}
	
	public void goTo(Vector3f target, MoveMode mode){
		if (mode == MoveMode.NORMAL){
			if (getDestination() != null && getDestination().equals(target)){
				return; // ??
			}
			moveTo(target);
		}else{
			moveTo(target); // NIAHAHAHAHAHahahahahahHAHAhahHAhHAhAhahaha !!!
		}
	}
	
	public void lookAt(Vector3f target, MoveMode mode){
		if (mode == MoveMode.NORMAL){
			((Camera)getSpatial().getUserData("cam")).lookAt(target, Vector3f.UNIT_Y); // UNIT_Y should be alright ...
		}else{
			((Camera)getSpatial().getUserData("cam")).lookAt(target, Vector3f.UNIT_Y);
		}
	}
	
	void deploiment(){
		final Object[] args = getArguments();
		if(args[0]!=null && args[1]!=null){
			
			this.friendorFoe = ((boolean)args[1]);
			
			if (friendorFoe) {
				deployAgent((NewEnv) args[0]);
			} else {
				deployEnemy((NewEnv) args[0]);
			}
			
		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}
	}
	
	
}
