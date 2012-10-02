package chickfillet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.util.Random;

import chickfillet.jobs.Banking;
import chickfillet.jobs.Collect;
import chickfillet.jobs.WalkToFarm;


@Manifest(authors = { "pancakes100" }, name = "Chick Fillet", description = "Loots chicken droppings" )
public class ChickFillet extends ActiveScript implements PaintListener {

	public static String status;
	private static long startTime = 0;

	private final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
	private Tree jobContainer = null;

	public final void provide(final Node... jobs) {
		for (final Node job : jobs) {
			if(!jobsCollection.contains(job)) {
				jobsCollection.add(job);
			}
		}
		jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
	}

	@Override
	public void onStart() {
		startTime = System.currentTimeMillis();
		status = "N/A";
		provide(new Collect(), new Banking(), new WalkToFarm());
	}

	@Override
	public int loop() {
		if (jobContainer != null) {
			final Node job = jobContainer.state();
			if (job != null) {
				jobContainer.set(job);
				getContainer().submit(job);
				job.join();
			}
		}
		return Random.nextInt(10, 50);
	}

	@Override
	public void onRepaint(Graphics g) {
		final Graphics2D canvas = (Graphics2D) g;
		Rectangle bg = new Rectangle(5, 20, 260, 150);
		canvas.setColor(Color.CYAN);
		canvas.fill(bg);
		canvas.setColor(Color.PINK);
		canvas.fill(new Rectangle(Mouse.getX() + 1, Mouse.getY() - 4, 2, 15));
		canvas.fill(new Rectangle(Mouse.getX() - 6, Mouse.getY() + 2, 16, 2));
		canvas.setColor(Color.BLACK);
		canvas.drawString("BD Chick Fillet", 80, 35);
		canvas.drawString("Run Time: " + (startTime - System.currentTimeMillis()), 20, 60);
		canvas.drawString("Status :" + status, 20, 80);
	}

}