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
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;

public class Banking extends Node {
	
	public static boolean walkToFarm = false;
	
	public final Area lumLode = new Area(new Tile[] {
			new Tile(3237, 3214, 0), new Tile(3224, 3216, 0),
			new Tile(3228, 3225, 0), new Tile(3229, 3230, 0),
			new Tile(3232, 3234, 0), new Tile(3236, 3231, 0),
			new Tile(3237, 3226, 0) 
	});
	/*public final Tile[] northStairs = {
			new Tile(3225, 3219, 0), new Tile(3215, 3219, 0),
			new Tile(3214, 3227, 0), new Tile(3206, 3228, 0)
	};
	
	public final Tile[] southStairs = {
			new Tile(3223, 3220, 0), new Tile(3215, 3220, 0),
			new Tile(3213, 3211, 0), new Tile(3206, 3210, 0)
	};*/
	
	//North = 0, South = 1
	public final Tile[][] stairPath = {
	{
		new Tile(3225, 3219, 0), new Tile(3215, 3219, 0),
		new Tile(3214, 3227, 0), new Tile(3206, 3228, 0)
	},
	{
		new Tile(3223, 3220, 0), new Tile(3215, 3220, 0),
		new Tile(3213, 3211, 0), new Tile(3206, 3210, 0)
	}};
	
	
	@Override
	public boolean activate() {
		return Inventory.isFull();
	}
	
	private void traverseBank() {
		boolean baseClimbed = false;
		boolean allClimbed = false;
		int stairs = Random.nextInt(0, 1);
		int[][] stairEnts = {{36776, 36777, 36778}, {36773, 36774, 36775}};
		
			Walking.newTilePath(stairPath[stairs]).traverse();
			Task.sleep(498, 750);
			SceneObject baseStair = SceneEntities.getNearest(stairEnts[stairs][0]);
			SceneObject midStair = SceneEntities.getNearest(stairEnts[stairs][1]);
			SceneObject topStair = SceneEntities.getNearest(stairEnts[stairs][2]);
			/* To Do: Add cases for stair not being on screen, and turning to stair + interacting */
			if(baseStair.isOnScreen() && baseClimbed == false) {
				/*&& !Players.getLocal.isMoving()*/
				baseStair.interact("Climb-up");//add a random to choose between this and just clicking the base
				baseClimbed = true;
			}
			if(midStair.isOnScreen() && baseClimbed) {
				midStair.interact("Climb-up");
				if(baseClimbed && topStair.isOnScreen()) {
					allClimbed = true;
					baseClimbed = false;
			}
			if(topStair.isOnScreen() && allClimbed) {
				//walk to bank
				allClimbed = false;
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
		//Switch between banker + bank booth
		if(booth != null) {
			if(!booth.isOnScreen()) {
				Camera.turnTo(booth);
			}
			booth.interact("Bank");//Check Message
			Task.sleep(200, 310);
			Bank.depositInventory();
		}
	}
	
	public void execute() {
		telePrevious();
		Task.sleep(1000, 3000);
		traverseBank();
		Task.sleep(300, 520);
		depositItems();
		Task.sleep(400, 600);
		telePrevious();
		walkToFarm = true;
	}

}
