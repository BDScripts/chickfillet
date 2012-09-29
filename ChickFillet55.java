package chickfillet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.interactive.NPC;

@SuppressWarnings("unused")
@Manifest(authors = { "Big Daddy" }, name = "BD Chick Fillet", description = "Collects chicken loot", vip = false)
public class ChickFillet55 extends ActiveScript implements PaintListener {

	private final Node[] array = new Node[] { new Pickpocket() };

	@Override
	public int loop() {
		if (runTime == null) {
			runTime = new Timer(0);
		}
		for (final Node node : array) {
			if (node.activate()) {
				node.execute();
			}
		}
		return 0;
	}

	private int[] TrainerID = { 11281 };
	private String Status;
	private int startExp = Skills.getExperience(Skills.THIEVING);
	private Timer runTime;
	private class Pickpocket extends Node {
		@Override
		public void execute() {
			Status = "Picpocketing";
			NPC Trainer = NPCs.getNearest(TrainerID);

			while (Trainer.getAnimation() == -1) {
				Trainer.interact("Pickpocket");
			}
			if (Trainer.getAnimation() != -1) {
				Status = "Stunned!";
				Task.sleep(Random.nextInt(4000, 4585));
			}
			if (!Trainer.isOnScreen()) {
				Camera.turnTo(Trainer);
				Trainer.interact("Pickpocket");
			}
		}
		@Override
		public boolean activate() {
			return Players.getLocal().getAnimation() == -1;
		}
	}

	@Override
	public void onRepaint(Graphics g) {
		if (runTime != null) {
			int xpHour = (int) ((float) (Skills.getExperience(Skills.THIEVING) - startExp)
					/ (float) runTime.getElapsed() * 3600000);
			final Graphics2D g2d = (Graphics2D) g;
			Rectangle bg = new Rectangle(5, 20, 260, 150);
			g2d.setColor(Color.CYAN);
			g2d.fill(bg);
			g2d.setColor(Color.PINK);
			g2d.fill(new Rectangle(Mouse.getX() + 1, Mouse.getY() - 4, 2, 15));
			g2d.fill(new Rectangle(Mouse.getX() - 6, Mouse.getY() + 2, 16, 2));
			g2d.setColor(Color.BLACK);
			g2d.drawString("Linear Guild Pickpocketer!", 80, 35);
			g2d.drawString(
					"Thieving Level:" + Skills.getLevel(Skills.THIEVING), 20,
					60);
			g2d.drawString(
					"Thieving Experience gained:"
							+ (Skills.getExperience(Skills.THIEVING) - startExp),
							20, 80);
			g2d.drawString("Runtime: " + runTime.toElapsedString(), 20, 100);
			g2d.drawString("XP/Hr: " + xpHour, 20, 120);
			g2d.drawString("Status: " + Status, 20, 140);
		}
	}
}