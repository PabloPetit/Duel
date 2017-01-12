package sma.actionsBehaviours;

import org.jpl7.Query;

import com.jme3.math.Vector3f;

import env.jme.Situation;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import sma.AbstractAgent;
import sma.agents.FinalAgent;

public class Attack extends TickerBehaviour{
	
	private static final long serialVersionUID = 4340498260100499547L;
	
	public static long FORGET_TIME = 4;
	
	FinalAgent agent;
	
	String enemy;
	long lastTimeSeen;
	Vector3f lastPosition;
	
	public static boolean openFire = false;

	public Attack(Agent a, long period, String enemy) {
		super(a, period);
		this.enemy = enemy;
		lastPosition = agent.getEnemyLocation(enemy);
		agent = (FinalAgent)((AbstractAgent)a);
		openFire = false;
	}

	

	@Override
	protected void onTick() {
		
		askForFirePermission();
		
		if(agent.isVisible(enemy, AbstractAgent.VISION_DISTANCE)){
			lastTimeSeen = System.currentTimeMillis();
			lastPosition = agent.getEnemyLocation(enemy);
			agent.lookAt(lastPosition);
			
			if (openFire){
				System.out.println("Enemy visible, FIRE !");
				agent.lastAction = Situation.SHOOT;
				agent.shoot(enemy);
				agent.stopMoving();
			}
			
			
		}else{
			
			if (System.currentTimeMillis() - lastTimeSeen > FORGET_TIME * getPeriod()){
				System.out.println("The enemy ran away");
				agent.removeBehaviour(this);
				agent.currentBehavior = null;
			}
			agent.lastAction = Situation.FOLLOW;
			agent.goTo(lastPosition);
			
		}
	}
	
	public static void askForFirePermission(){
		String query = "attack("+PrologBehavior.sit.life+","
					+PrologBehavior.sit.enemyInSight +","
					+PrologBehavior.sit.impactProba+")";
		
		openFire = Query.hasSolution(query);
	}

}














