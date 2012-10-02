package chickfillet.jobs;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.Task;

import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;

import chickfillet.ChickFillet;
import chickfillet.consts.*;

public class Banking extends Node {
	
	public final Area lumLode = new Area(new Tile(3230, 3227, 0), new Tile(3237, 3227, 0),
			new Tile(3237, 3217, 0), new Tile(3227, 3217, 0));
	
	public final Tile[] toStairs = {
			new Tile(3225, 3219, 0), new Tile(3215, 3219, 0),
			new Tile(3214, 3227, 0), new Tile(3206, 3228, 0)
	};

	//north = 0, south = 1
	/*public final Tile[][] stairPath = {
	{
		new Tile(3225, 3219, 0), new Tile(3215, 3219, 0),
		new Tile(3214, 3227, 0), new Tile(3206, 3228, 0)
	},
	{
		new Tile(3223, 3220, 0), new Tile(3215, 3220, 0),
		new Tile(3213, 3211, 0), new Tile(3206, 3210, 0)
	}};*/
	
	
	@Override
	public boolean activate() {
		return Inventory.isFull() && Collect.farm.contains(Players.getLocal());
	}
	
	SceneObject baseStair = SceneEntities.getNearest(36776);
	SceneObject midStair = SceneEntities.getNearest(36777);
	SceneObject topStair = SceneEntities.getNearest(36778);
	boolean baseClimbed = false;
	boolean allClimbed = false;
	
	private void climbBase() {
		if(!Players.getLocal().isMoving()) {
			if(baseStair.isOnScreen() && baseStair != null) {
				baseStair.interact("Climb-up");
				Task.sleep(1000, 2000);
			} else if (!baseStair.isOnScreen() && baseStair != null) {
				Camera.turnTo(baseStair);
				Task.sleep(500, 750);
				baseStair.interact("Climb-up");
				Task.sleep(1000, 2000);
				baseClimbed = true;
			}
		}
	}
	
	private void climbMid() {
		if(baseClimbed && !allClimbed) {
			if(midStair.isOnScreen() && midStair != null) {
				midStair.interact("Climb-up");
				Task.sleep(1000, 2000);
			}
			if(!midStair.isOnScreen() && midStair != null) {
				Camera.turnTo(midStair);
				Task.sleep(500, 750);
				midStair.interact("Climb-up");
				Task.sleep(1000, 2000);
				baseClimbed = false;
				allClimbed = true;
			}
		} 
	}
	
	private void traverseBank() {		
			Walking.newTilePath(toStairs).traverse();
			Task.sleep(2500, 3000);
			if(!Players.getLocal().isMoving()) {
				climbBase();
				climbMid();
				if(topStair != null) {
					allClimbed = false;
					Walking.walk(new Tile(3209, 3219, 2));
					Task.sleep(5000, 7000);
				}
			}
	}
	
	private void telePrevious() {
		org.powerbot.game.api.methods.Tabs.MAGIC.open(false);
		Task.sleep(300, 400);
		Widgets.get(192, 24).interact("Previous-destination");
	}
	
	private void depositItems() {
		SceneObject booth = SceneEntities.getNearest(Bank.BANK_BOOTH_IDS);
		if(booth != null) {
			if(!booth.isOnScreen()) {
				Camera.turnTo(booth);
			}
			booth.interact("Bank");
			Task.sleep(200, 310);
			Bank.depositInventory();
		}
	}
	
	public void execute() {
		ChickFillet.status = "Teleporting";
		telePrevious();
		Task.sleep(11000, 13000);
		ChickFillet.status = "Walking to bank";
		traverseBank();
		if(!Players.getLocal().isMoving()) {
			ChickFillet.status = "Banking";
			depositItems();
			Task.sleep(400, 600);
			ChickFillet.status = "Teleporting";
			telePrevious();
			Task.sleep(11000, 13000);
			ChickFillet.status = "Walking to Farm";
			Vars.walkToFarm = true;
		}
	}
}
