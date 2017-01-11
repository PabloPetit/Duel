package sma.actionsBehaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import sma.AbstractAgent;
import sma.agents.FinalAgent;

public class PrologBehavior extends TickerBehaviour {

	
	private static final long serialVersionUID = 5739600674796316846L;
	
	FinalAgent agent;
	
	
	public PrologBehavior(Agent a, long period) {
		super(a, period);
		agent = (FinalAgent)((AbstractAgent)a);
	}



	@Override
	protected void onTick() {
		

	}

}
