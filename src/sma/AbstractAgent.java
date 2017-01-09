package sma;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import dataStructures.tuple.Tuple2;
import env.EnvironmentManager;
import env.jme.Environment;
import env.jme.Situation;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import sma.actionsBehaviours.LegalActions.LegalAction;

public class AbstractAgent extends Agent implements EnvironmentManager {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NewEnv realEnv;
	
	
	public AbstractAgent() {
		registerO2AInterface(EnvironmentManager.class, this);
	}

	public Vector3f getCurrentPosition() {
		return this.realEnv.getCurrentPosition(getLocalName());
	}
	
	public Vector3f getDestination() {
		return this.realEnv.getDestination(getLocalName());
	}

	public  ArrayList<Tuple2<Vector3f, String>> getVisibleAgents(float range){
		return realEnv.getVisibleAgents(range);
	}


	public void lookAt(LegalAction direction) {
		this.realEnv.lookAt(getLocalName(), direction);
	}

	public boolean moveTo(Vector3f myDestination) {
		return this.realEnv.moveTo(getLocalName(), myDestination);
	}
	

	public boolean randomMove() {
		return this.realEnv.randomMove(getLocalName());
	}

	public boolean shoot(String target) {
		return this.realEnv.shoot(getLocalName(), target);
	}
	
	
	public Spatial getSpatial(){
		return realEnv.getSpatial(getLocalName());
	}
	
	public Vector3f adjusteHeight(Vector3f in){
		return realEnv.adjusteHeight(in);
	}

	public boolean isVisible(String agent, String enemy){
		return realEnv.isVisble(agent, enemy);
	}
	
	
	public ArrayList<Vector3f> sphereCast(Spatial sp, float angle, float distance, float precision){
		return realEnv.sphereCast(sp, angle, distance, precision);
	}
	
	public ArrayList<Vector3f> goldenSphereCast(Spatial sp, float distance, int N){
		return realEnv.goldenSphereCast(sp, distance, N);
	}
	

	public float impactProba(Vector3f origin, Vector3f target){
		return realEnv.impactProba(origin, target);
	}

	/**
	 * Deploy an agent tagged as a player
	 */
	public void deployAgent(Environment env) {
		this.realEnv = env;
		this.realEnv.deployAgent(getLocalName(), "player");
	}

	/**
	 * Deploy an agent tagged as an enemy
	 */
	public void deployEnemy(Environment env) {
		this.realEnv = env;
		this.realEnv.deployAgent(getLocalName(), "enemy");
	}

	protected void setup() {
		super.setup();
	}
	
}
