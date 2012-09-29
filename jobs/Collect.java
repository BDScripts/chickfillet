package chickfillet.jobs;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.node.GroundItem;


public class Collect extends Node {
	
	public final int[] chickenID = {41, 1017};
	public final int[] groundID = { /*raw, feather, bone, egg*/ };
	private SceneObject lastClicked = null;
	
	private boolean inFarm() {
		return false;
	}
	
	@Override
	public boolean activate() {
		return !Inventory.isFull();
	}

	@Override
	public void execute() {
		/*GroundItem egg = GroundItems.getNearest(1944);
		GroundItem bones = GroundItems.getNearest(526);
		GroundItem rChicken = GroundItems.getNearest(2138);
		GroundItem feather = GroundItems.getNearest(314);*/
		GroundItem collectable = GroundItems.getNearest(1944, 526, 2138, 314);
		if(collectable != null && collectable.isOnScreen()) {
			collectable.interact("Take", collectable.getGroundItem().getName());//get rid of the last part?
		}
		
		//NPC farmer NPC
		//if(farmer != null) && inFarm() {
	}
}
