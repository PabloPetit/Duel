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
import env.jme.NewEnv;
import env.jme.Situation;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import sma.actionsBehaviours.LegalActions.LegalAction;
import sma.agents.FinalAgent;

public class AbstractAgent extends Agent implements EnvironmentManager {
	
	
	private static final long serialVersionUID = 1L;
	private NewEnv realEnv;
	
	
	public static final float VISION_ANGLE = 180f;
	public static final float VISION_DISTANCE = 35f;
	
	public static final float NEIGHBORHOOD_DISTANCE = 8f;
	
	public static final int CLOSE_PRECISION = 100;
	public static final int FAR_PRECISION = 1000;
	
	public static final int MAX_LIFE = 9;
	public static final int SHOT_DAMAGE = 3;
	
	
	public AbstractAgent() {
		registerO2AInterface(EnvironmentManager.class, this);
	}

	public Vector3f getCurrentPosition() {
		return this.realEnv.getCurrentPosition(getLocalName());
	}
	
	public Vector3f getDestination() {
		return this.realEnv.getDestination(getLocalName());
	}

	public  ArrayList<Tuple2<Vector3f, String>> getVisibleAgents(float range, float angle){
		return realEnv.getVisibleAgents(getLocalName(), range, angle);
	}


	public boolean moveTo(Vector3f myDestination) {
		return this.realEnv.moveTo(getLocalName(), myDestination);
	}
	
	public void teleport(Vector3f dest){
		realEnv.teleport(getLocalName(), dest);
	}
	
	public Vector3f getRandomPosition(){
		return realEnv.getRandomPosition();
	}
	
	public void drawBox(Vector3f pos, int color){
		realEnv.drawBox(pos, color);
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

	public boolean isVisible(String agent, String enemy, float distance){
		return realEnv.isVisible(agent, enemy, distance);
	}
	
	
	public ArrayList<Vector3f> sphereCast(Spatial sp, float distance,  int N, float angle){
		return realEnv.goldenSphereCast(sp, distance, N, angle);
	}
	
	public ArrayList<Vector3f> sphereCast(Spatial sp, float distance,  int N){
		return realEnv.goldenSphereCast(sp, distance, N);
	}
	

	public float impactProba(Vector3f origin, Vector3f target){
		return realEnv.impactProba(origin, target);
	}
	
	public  boolean isVisible(String enemy, float distance){
		return realEnv.isVisible(getLocalName(), enemy, distance);
	}

	/**
	 * Deploy an agent tagged as a player
	 */
	public void deployAgent(NewEnv args) {
		this.realEnv = args;
		this.realEnv.deployAgent(getLocalName(), "player");
	}

	/**
	 * Deploy an agent tagged as an enemy
	 */
	public void deployEnemy(NewEnv env) {
		this.realEnv = env;
		this.realEnv.deployAgent(getLocalName(), "enemy");
	}

	protected void setup() {
		super.setup();
	}
	
	public void addToAgents(FinalAgent agent){
		realEnv.addAgent(agent);
	}
	

	@Override
	public void deployAgent(Environment paramEnvironment) {
		// TODO Auto-generated method stub	
	}
	
	public Vector3f getEnemyLocation(String enemy){
		return realEnv.getCurrentPosition(enemy);
	}
	
}
