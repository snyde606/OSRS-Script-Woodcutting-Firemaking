import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(name = "Jacobian's FireChopper", author = "Jacobian", version = 1.0, info = "", logo = "")
public class WoodFire extends Script {
	
	private GUI gui;

	String currentStat = "Idle";
	private long startTime;
	private Position startSpot;

	private enum State {
		CHOP, BURN
	};

	@Override
	public void onStart() throws InterruptedException {
		log("");
		log("Thank you for using my Script!");
		log("-Jacobian");
		log("");
		log("Start your character in an area that contains the selected tree type.");
		log("");
		log("You'll need an AXE and a TINDERBOX, equipped or in your inventory.");
		log("Remember to select a wood type that you have adequate skills to CUT AND BURN.");
		log("");

		startTime = System.currentTimeMillis();
		gui = new GUI();
		gui.createGUI();
		while (!GUI.started)
			sleep(random(300, 600));

		startSpot = myPlayer().getPosition();
		
		for (Skill skill : new Skill[] { Skill.FIREMAKING, Skill.WOODCUTTING })
			getExperienceTracker().start(skill);
	}

	private State getState() {

		if (getInventory().isFull()) {
			return State.BURN;
		}

		return State.CHOP;

	}

	@Override
	public int onLoop() throws InterruptedException {

		switch (getState()) {

		case BURN:
			currentStat = "Burning logs";
			if (GUI.getStringTree() != "Tree") {
				while (getInventory().contains(GUI.getStringTree() + " logs")) {
					Position playerPos = myPlayer().getPosition();
					getInventory().interact("Use", "Tinderbox");
					getInventory().interact("Use", GUI.getStringTree() + " logs");
					sleep(random(2250, 2750));
					while (myPlayer().isAnimating())
						sleep(random(750, 1250));
					sleep(random(1250, 1750));
					if (myPlayer().getPosition().equals(playerPos)) {
						playerPos.translate(random(-3, 3), random(-3, 3)).interact(bot, "Walk here");
						sleep(random(1750, 2250));
					}
				}
			} else {
				while (getInventory().contains("Logs")) {
					Position playerPos = myPlayer().getPosition();
					getInventory().interact("Use", "Tinderbox");
					getInventory().interact("Use", "Logs");
					sleep(1500);
					while (myPlayer().isAnimating())
						sleep(random(750, 1250));
					sleep(1000);
					if (myPlayer().getPosition().equals(playerPos)) {
						playerPos.translate(random(-3, 3), random(-3, 3)).interact(bot, "Walk here");
						sleep(random(1750, 2250));
					}
				}
			}
			
			getWalking().webWalk(startSpot);
			
			break;

		case CHOP:
			currentStat = "Chopping";
			RS2Object tree;
			if (!myPlayer().isAnimating()) {
				if (GUI.getStringTree() != "Magic")
					tree = getObjects().closest(GUI.getStringTree());
				else
					tree = getObjects().closest("Magic tree");
				if (tree != null) {
					if (tree.interact("Chop down"))
						sleep(random(1000, 1500));
				}
			}

			break;
		}

		return random(400, 700);

	}

	public void onPaint(Graphics2D g) {

		if (GUI.started) {

			Font font = new Font("Comic Sans", Font.BOLD, 14);
			g.setFont(font);
			g.setColor(Color.white);
			g.drawString("Woodcutting XP Gained: \t "
					+ String.valueOf(getExperienceTracker().getGainedXP(Skill.WOODCUTTING)), 10, 55);

			g.drawString("Woodcutting XP / HR: \t"
					+ String.valueOf(getExperienceTracker().getGainedXPPerHour(Skill.WOODCUTTING)), 10, 70);

			g.drawString(
					"Firemaking XP Gained: \t" + String.valueOf(getExperienceTracker().getGainedXP(Skill.FIREMAKING)),
					10, 85);

			g.drawString("Firemaking XP / HR:\t "
					+ String.valueOf(getExperienceTracker().getGainedXPPerHour(Skill.FIREMAKING)), 10, 100);

			g.setColor(new Color(255, 255, 255, 255));
			g.drawRect(6, 30, 235, 105);
			final long runTime = System.currentTimeMillis() - startTime;
			g.drawString("Time Ran: " + formatTime(runTime), 40, 43);
			Point mP = getMouse().getPosition();
			g.setColor(Color.CYAN);
			g.drawString("Tree: " + GUI.getStringTree(), 10, 115);
			g.setColor(Color.GREEN);
			g.drawString("State: " + String.valueOf(currentStat), 10, 130);

			g.setColor(Color.WHITE);
			g.drawLine(mP.x - 10, mP.y + 10, mP.x + 10, mP.y - 10);
			g.drawLine(mP.x + 10, mP.y + 10, mP.x - 10, mP.y - 10);
			g.drawRect(mP.x - 10, mP.y - 10, 20, 20);

		}
	}

	public final String formatTime(final long ms) {
		long s = ms / 1000, m = s / 60, h = m / 60;
		s %= 60;
		m %= 60;
		h %= 24;
		return String.format("%02d:%02d:%02d", h, m, s);
	}

}