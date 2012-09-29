package chickfillet.jobs;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;


public class Collect extends Node {

    public static final Area farm = new Area(new Tile(3236, 3300, 0), new Tile(3236, 3292, 0), new Tile(3225, 3295, 0),
            new Tile(3225, 3301, 0));
	
	@Override
	public boolean activate() {
		return !Inventory.isFull() && farm.contains(Players.getLocal());
	}

	@Override
	public void execute() {
		GroundItem collectable = GroundItems.getNearest(1944, 526, 2138, 314);
		if(collectable != null && collectable.isOnScreen()) {
			collectable.interact("Take", collectable.getGroundItem().getName());
			Task.sleep(200, 263);
		} else if (collectable != null & !collectable.isOnScreen()) {
			Camera.turnTo(collectable);
			collectable.interact("Take", collectable.getGroundItem().getName());
		}
	}
}
