package sma.actionsBehaviours;

import java.util.Random;

import com.jme3.math.Vector3f;

import dataStructures.tuple.Tuple2;
import env.jme.Situation;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import sma.AbstractAgent;
import sma.actionsBehaviours.LegalActions.LegalAction;

public class FollowBehavior extends TickerBehaviour{

	
	private static final long serialVersionUID = 1354354342L;
	
	
	
	public FollowBehavior(Agent a, long period) {
		super(a, period);
		
	}

	

	@Override
	protected void onTick() {
		
		AbstractAgent agent = ((AbstractAgent)this.myAgent);
		Situation current = null;
		
		current = ((AbstractAgent)this.myAgent).observeAgents();
		
		Tuple2<Vector3f, String> en = null;
		System.out.println("Observe size : "+current.agents.size());
		for(Tuple2<Vector3f, String> tuple : current.agents){
			if (!tuple.getSecond().equals(myAgent.getLocalName())){
				en = tuple;
			}
		}
		
		if (en == null){ // If nobody in sight, random walk
			System.out.println("Random Move");
			//agent.randomMove(); 
			LegalAction[] actions = LegalAction.values();
			LegalAction action = actions[9+(int)(Math.random()*8)];
			agent.lookAt(action);
			return;
		}
		else
		{
			System.out.println("direction : " + current.direction);
			int card = getLegalAction(agent.getCurrentPosition(), en.getFirst());
			LegalAction[] actions = LegalAction.values();
			LegalAction action = actions[card];
			System.out.println("Following : "+action.id+" CRAD : "+card);
			agent.cardinalMove(action);
			agent.lookAt(actions[card+8]);
		}
		
	}
	
	public static int getLegalAction(Vector3f self, Vector3f target){
		
		//float produitScalaire = self.x * target.x + self.y * target.y + self.z * target.z;
		Vector3f north = new Vector3f(0, 0, 1);
		Vector3f orientation = target.subtract(self);
		float produitScalaire = north.dot(orientation);
		float norme = north.length() * orientation.length();
		
		System.out.println("self : " + self.toString() + " , target : " + target.toString());
		//float angle = self.angleBetween(target);
		
		float angle = (float) (Math.acos(produitScalaire / norme) + Math.PI);
		System.out.println("Angle : " + angle);
		return (int)(angle/Math.PI * 4)+1;
	}

}
