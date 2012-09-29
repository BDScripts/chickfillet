package chickfillet.jobs;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.Task;

import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import chickfillet.ChickFillet;


public class FarmWalk extends Node{

	private final Tile[] toFarm = {
			new Tile(3235, 3227, 0), new Tile(3258, 3227, 0),
			new Tile(3255, 3247, 0), new Tile(3247, 3262, 0),
			new Tile(3246, 3273, 0), new Tile(3239, 3282, 0),
			new Tile(3239, 3292, 0) 
	};

	@Override
	public boolean activate() {
		return Banking.walkToFarm;
	}

	private void openGate() {
		//SceneObject gateOpen = SceneEntities.getNearest(45207);//or 45209
		SceneObject gateClosed = SceneEntities.getNearest(45206);//or 45208
		if(gateClosed.isOnScreen() && gateClosed != null) {
			gateClosed.interact("Open");
		} else if(!gateClosed.isOnScreen() && gateClosed != null) {
			//walk in
		} else {
			//walk to gate. open gate.
		}
	}

	@Override
	public void execute() {
		ChickFillet.status = "Walking To Farm";
		Walking.newTilePath(toFarm).traverse();
		ChickFillet.status = "Opening Gate";
		openGate();
		Banking.walkToFarm = false;
	}

}
