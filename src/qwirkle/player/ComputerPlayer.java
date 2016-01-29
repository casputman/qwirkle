package qwirkle.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import qwirkle.core.Game;
import qwirkle.core.Tile;
import qwirkle.strategy.Strategy;
import qwirkle.strategy.SmartStrategy;
import qwirkle.strategy.StupidStrategy;

public class ComputerPlayer extends Player {

	public static final List<String> NAMES = Collections.unmodifiableList(Arrays.asList("Bolhuis", "Putman" + ""));

	public Strategy strategy = new StupidStrategy();
	private String ai;

	public ComputerPlayer(String name) {
		this.name = nameGenerator();
		try {
			System.out.println("\nPlease choose an ai (either Smart or Stupid)");
			this.ai = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
		} catch (IOException e) {
			System.out.println("That didn't work...please try again.");
		}
		if (ai.equalsIgnoreCase("smart")) {
			System.err.println("Smart strategy is currently non-functional");
			this.strategy = new StupidStrategy();
		} else if (ai.equalsIgnoreCase("stupid")) {
			this.strategy = new StupidStrategy();
		} else {
			System.err.println("'" + ai + "' is not one of the possibilities");
			new ComputerPlayer();
		}
	}

	public ComputerPlayer() {
		new ComputerPlayer(nameGenerator());
	}

	public String nameGenerator() {
		long millis = System.currentTimeMillis() % 1000;
		String clientName = ("computerPlayer" + (millis));
		return clientName;
	}

	@Override
	public Map<String, Tile> determineMove(Game game) {
		return this.strategy.determineMove(game, game.current.getHand());
	}

}
