package sma.actionsBehaviours;

import org.jpl7.Query;

import env.jme.NewEnv;
import env.jme.Situation;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import sma.AbstractAgent;
import sma.InterestPoint;
import sma.agents.FinalAgent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrologBehavior extends TickerBehaviour {

	
	private static final long serialVersionUID = 5739600674796316846L;
	
	FinalAgent agent;
	
	
	public PrologBehavior(Agent a, long period) {
		super(a, period);
		agent = (FinalAgent)((AbstractAgent)a);
	}



	@Override
	protected void onTick() {
		String prolog = "consult('/requete.pl')";
		if (!Query.hasSolution(prolog)) {
			System.out.println("Cannot open file " + prolog);
		}
		else {
			Situation sit = Situation.getCurrentSituation(agent);
			List<String> behavior = Arrays.asList("explore_off", "explore_def", "hunt", "follow", "shoot", "retreat");
			ArrayList<Object> terms = new ArrayList<Object>();

			for (String b : behavior) {
				terms.clear();
				// Get parameters 
				if (b.equals("explore_off") || b.equals("explore_def")) {
					terms.add(sit.offSize);
					terms.add(sit.defSize);
					terms.add(NewEnv.MAX_DISTANCE);
					terms.add(InterestPoint.INFLUENCE_ZONE);
				}
				else if (b.equals("hunt")) {
					terms.add(sit.life);
					terms.add(sit.timeSinceLastShot);
					terms.add(sit.offSize);
					terms.add(sit.defSize);
					terms.add(NewEnv.MAX_DISTANCE);
					terms.add(InterestPoint.INFLUENCE_ZONE);
				}
				else if (b.equals("follow") || b.equals("shoot")) {
					terms.add(sit.enemyInSight);
					terms.add(sit.impactProba);
				}
				else {
					terms.add(sit.life);
					terms.add(sit.timeSinceLastShot);
				}

				//String query = prologQuery(b, terms);
				//if (Query.hasSolution(query)) {
					// addBehavior
					// break
				//}
			}
		}
	}
	
	public String prologQuery(String behavior, ArrayList<Object> terms) {
		String query = "(" + behavior;
		for (Object t: terms) {
			query += t + ";";
		}
		return query.substring(0,query.length() - 1) + ")";
	}

	public void executeExploreOff() {
		agent.addBehaviour(agent.explore);
	}

	public void executeExploreDef() {
		agent.addBehaviour(agent.explore);
	}

	public void executeHunt() {
		agent.addBehaviour(agent.hunt);
	}

	public void executeFollow() {
		//agent.addBehaviour(agent.follow);
	}

	public void executeShoot() {
		//agent.addBehaviour(agent.shoot);
	}

	public void executeRetreat() {
		//agent.addBehaviour(agent.retreat);
	}

}