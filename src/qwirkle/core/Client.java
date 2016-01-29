package qwirkle.core;

import qwirkle.player.*;

import qwirkle.core.*;
import qwirkle.server.*;
import qwirkle.protocol.*;
import qwirkle.strategy.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client extends Thread {

	public static void main(String[] args) throws Exception {
		new Client();
	}

	/**
	 * Opens the inputstream and continuously checks it.
	 */
	// @ requires in != null;
	public void run() {
		try {
			String message = in.readLine();
			while (message != null) {
				if (message.equalsIgnoreCase("EXIT")) {
					shutdown();
				}
				System.out.println(message);
				message = in.readLine();
			}
		} catch (IOException e) {
			System.err.println("Something went wrong");
			menu();
		} catch (NullPointerException e) {
			System.out.println("There is no input.");
		}
		shutdown();
	}

	private /* @ spec_public @ */ Socket sock;
	private /* @ spec_public @ */ BufferedReader in;
	private /* @ spec_public @ */ BufferedWriter out;
	public boolean online;
	private /* @ spec_public @ */ Player you;
	private /* @ spec_public @ */ Player player2;
	private /* @ spec_public @ */ Player player3;
	private /* @ spec_public @ */ Player player4;
	private Game game;

	/**
	 * Launches the startup method
	 */
	public Client() {
		startup();
	}

	/**
	 * This method generates a random name for a computerPlayer using the
	 * current time in millis.
	 * 
	 * @return clientName;
	 */
	// @ensures getClientName() == clientName;
	public String nameGenerator() {
		long millis = System.currentTimeMillis() % 1000;
		String clientName = ("computerPlayer" + (millis));
		return clientName;
	}

	/**
	 * This method asks the player against how many opponents he wish to play
	 * against. He does this by asking the player a simple question, which the
	 * player has to answer by responding with a number between 1 and 3. Once
	 * the player has chosen the number of opponents he wish to play against,
	 * the game will be started.
	 */
	// @ requires input != null;
	// @ ensures player1 != null;
	// @ ensures player2 != null;
	// @ ensures player3 != null;
	// @ ensures player4 != null;
	// @ ensures game != null;
	public void numberOfPlayers() {
		String input = "";
		input = getInput("How many opponents do you want to play against?");
		if (input.equals("1")) {
			player2 = new ComputerPlayer(nameGenerator());
			game = new Game(you, player2);
		} else if (input.equals("2")) {
			player2 = new ComputerPlayer(nameGenerator());
			player3 = new ComputerPlayer(nameGenerator());
			game = new Game(you, player2, player3);
		} else if (input.equals("3")) {
			player2 = new ComputerPlayer(nameGenerator());
			player3 = new ComputerPlayer(nameGenerator());
			player4 = new ComputerPlayer(nameGenerator());
			game = new Game(you, player2, player3, player4);
		}
	}

	/**
	 * This method checks whether the client wants to play online or offline
	 * using the isOnline() method If the player wants to play online we call
	 * the method initializeClient() If the player wants to play offline a new
	 * game will be created and started
	 */
	// @ requires !isOnline();
	// @ ensures game.getRunning();
	// @ ensures game.offline;
	private void startup() {
		if (isOnline()) {
			initializeClient();
		} else {
			makePlayer();
			numberOfPlayers();
			game.offline = true;
			game.run();
		}
	}

	/**
	 * This method asks the user for input whether the newly to be created
	 * player will be a computer or a human player. If it is a human player, the
	 * user can enter a name. The method also checks whether the chosen name
	 * doesn't contain any illegal characters.
	 */
	// @requires getInput() != null;
	// @ensures you != null;
	// @ensures you.getName() != null;
	private void makePlayer() {
		String playerType = getInput(
				"\nIs this player a human or computer player?" + "\nPlease input either 'human' or 'computer'");
		if (playerType.equalsIgnoreCase("human")) {
			String input = getInput("Please enter your name: ");
			if (input.contains(" ")) {
				System.err.println("Your name can not have a space in it");
				makePlayer();
			} else {
				you = new HumanPlayer(input);
			}
		} else if (playerType.equalsIgnoreCase("computer")) {
			you = new ComputerPlayer("RandomComputerName");
		} else {
			initializeClient();
		}
		you.getName();

	}

	/**
	 * This assigns player variables to instances of ComputerPlayers.
	 */
	// @ensures player2 != null;
	// @ensures player3 != null;
	// @ensures player4 != null;
	public void assignPlayer() {
		System.out.println("Player2: ");
		player2 = new ComputerPlayer();
		System.out.println("Player3: ");
		player3 = new ComputerPlayer();
		System.out.println("Player4: ");
		player4 = new ComputerPlayer();
	}

	/**
	 * This will ask for user input using the method getInput(String msg) to
	 * find out if the players wants to play online of offline It uses the
	 * checkOnline(String input) method to see what the player wants This method
	 * is used in the startup() method
	 * 
	 * @return true when player wants to play online
	 * @return false when player wants to play offline
	 */
	// @ pure
	private boolean isOnline() {
		String input = "";
		input = getInput("Do you want to play online or offline?");
		if (!checkOnline(input)) {
			isOnline();
		}
		return online;
	}

	/**
	 * Reads it's input to find out if the player wanted to play online or
	 * offline It sets online to true or false depending on what the player
	 * inputted
	 * 
	 * @param input
	 * @return true when the input is equal to either "online" or "offline"
	 */
	// @ requires input != "";
	// @ requires input == "ONLINE";
	// @ ensures online;
	// @ ensures \result;
	// @ also
	// @ requires input == "OFFLINE";
	// @ ensures !online;
	// @ ensures !\result;
	public boolean checkOnline(String input) {
		boolean checked;
		if ("ONLINE".equalsIgnoreCase(input)) {
			online = true;
			checked = true;
		} else if ("OFFLINE".equalsIgnoreCase(input)) {
			online = false;
			checked = true;
		} else {
			System.err.println("Please enter either 'online' or 'offline'");
			checked = false;
		}
		return checked;
	}

	/**
	 * This will in chronological order: Ask the client if they want the player
	 * to be human or computer if the client wants a computer player it will
	 * create a new ComputerPlayer() if the client wants a human player it will
	 * create a new HumanPlayer() It will ask what the IP address of the server
	 * is and store that information It will ask what the port of the server is
	 * (if nothing is inputted it will use port 1337) and store that information
	 * It will connect to the server It will initialize the inputstream It will
	 * initialize the outputstream
	 */
	// @ ensures you != null;
	// @ ensures clientName != "";
	public void initializeClient() {
		String playerType = getInput(
				"\nIs this player a human or computer player?" + "\nPlease input either 'human' or 'computer'");
		if (playerType.equalsIgnoreCase("human")) {
			you = new HumanPlayer("RandomHumanName");
		} else if (playerType.equalsIgnoreCase("computer")) {
			you = new ComputerPlayer("RandomComputerName");
		} else {
			initializeClient();
		}
		you.getName();
		InetAddress host = null;
		int port = 0;

		try {
			host = InetAddress
					.getByName(getInput("\nPlease enter the hostname of the server " + "(leave blank for localhost"));
		} catch (UnknownHostException e) {
			System.err.println("ERROR: not a valid hostname!");
			startup();
		}

		try {
			String portInput = getInput("\nPlease enter the port of the server " + "(leave blank for port 1337)");
			if (portInput.equals("")) {
				port = 1337;
			} else {
				port = Integer.parseInt(portInput);
			}
		} catch (NumberFormatException e) {
			System.err.println("ERROR: not a valid portnummer!");
			startup();
		}

		try {
			sock = new Socket(host, port);
		} catch (IOException e) {
			System.err.println("ERROR: socket could not be created!");
			startup();
		}

		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException e) {
			System.err.println("Something went wrong");
			menu();
		}
		connectToServer();
	}

	// @ requires getInput() != null;
	private void doSomething() {
		String input2;
		input2 = getInput("What do you want to do now?");
		this.sendMessage(input2);
	}

	private void connectToServer() {
		this.sendMessage(Protocol.CLIENT_CORE_JOIN);
		try {
			this.readResponse();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		doSomething();
		do {
			String input = getInput("");
			this.sendMessage(input);
		} while (true);
	}

	/**
	 * This method will read whatever input it receives and process it depending
	 * on the input different methods will be called and different variables
	 * will be set whenever processing is done it will read the new input
	 */
	// @ requires in != null;
	private void readResponse() throws InterruptedException {
		try {
			String reply = in.readLine();

			while (reply != null) {
				String[] antw = reply.split(Protocol.MESSAGESEPERATOR);
				if (reply.startsWith(Protocol.SERVER_CORE_JOIN_ACCEPTED)) {
					System.out.println("You have succesfully joined the server");
					doSomething();
				} else if (antw[1].equals(Protocol.SERVER_CORE_MOVE_DENIED)) {
					System.out.println("This is not a valid move, please try again");
				} else if (reply.startsWith(Protocol.SERVER_CORE_START)) {
					System.err.println("THE GAME IS STARTING");
					if (antw.length == 5) {
						Player player1 = Player.createPlayer(antw[1]);
						Player player2 = Player.createPlayer(antw[2]);
						Player player3 = Player.createPlayer(antw[3]);
						Player player4 = Player.createPlayer(antw[4]);
						game = new Game(player1, player2, player3, player4);
					} else if (antw.length == 4) {
						Player player1 = Player.createPlayer(antw[1]);
						Player player2 = Player.createPlayer(antw[2]);
						Player player3 = Player.createPlayer(antw[3]);
						game = new Game(player1, player2, player3);
					} else if (antw.length == 3) {
						Player player1 = Player.createPlayer(antw[1]);
						Player player2 = Player.createPlayer(antw[2]);
						game = new Game(player1, player2);
					}
					game.getBoard().printBoard();

				} else if (reply.startsWith(Protocol.SERVER_CORE_MOVE_MADE)) {
					String coords = (antw[1] + Protocol.MESSAGESEPERATOR + antw[2]);
					Tile tile = game.getBoard().makeTile(Integer.parseInt(antw[3]), Integer.parseInt(antw[4]));
					game.getBoard().makeMove(coords, tile, game);
					Map<String, Tile> moveMap = new HashMap<String, Tile>();
					moveMap.put(coords, tile);
					game.calculateScore(moveMap);
					game.getBoard().printBoard();
				} else if (reply.startsWith(Protocol.SERVER_CORE_GAME_ENDED)) {
					game.getBoard().printBoard();
					// game.getRules().listScores();
					System.out.println("The game has ended!");

				}
				reply = in.readLine();
			}
			readResponse();
			doSomething();
		} catch (IOException e) {
			System.err.println("Something went wrong at the server, so we lost our connection");
			menu();
		}
	}

	/**
	 * This method will close the socket and thus disconnect from the server
	 * afterwards it will give the client the opportunity to call startup() and
	 * thus make all the startup decisions again or to terminate the client
	 */
	// @ ensures !sock.isClosed();
	public void menu() {
		try {
			sock.close();
		} catch (IOException e1) {
			shutdown();
		}
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			System.out.println("WHO AWAKENS ME FROM MY SLUMBER");
			System.out.println("No seriously who and how, this isn't supposed to happen");
			System.out.println("It's actually quite awkward this...");
		}
		String menu = getInput("What now? Type either menu or exit");
		while (menu != null) {
			if (menu.equalsIgnoreCase("menu")) {
				startup();
			} else if (menu.equalsIgnoreCase("exit")) {
				shutdown();
			} else {
				System.err.println("No, don't type '" + menu + "' that was not one of the possibilities...");
				menu();
			}
		}
	}

	/**
	 * Writes the String msg to the outputstream
	 * 
	 * @param msg
	 */
	// @ requires out != null;
	public void sendMessage(String msg) {
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			System.err.println("Something went wrong at the server, so we lost our connection");
			menu();
		} catch (NullPointerException e) {
			System.out.println("There is no output.");
		}

	}

	/**
	 * Closes the socket and terminates the client
	 */
	// @ requires sock != null;
	// @ ensures sock.isClosed();
	public void shutdown() {
		try {
			sock.close();
		} catch (IOException e) {
			sendMessage("Connection.Shutdown");
		}
		System.err.println("Shutting down game...");
		System.exit(0);
	}

	/**
	 * Prints the String variable and returns the user input
	 * 
	 * @param variable
	 * @return
	 */
	// @ requires in != null;
	// @ ensures \result != "";
	public String getInput(String variable) {
		String input = null;
		try {
			if (variable != "") {
				System.out.println(variable);
			}
			input = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();

		} catch (IOException e) {
			System.err.println("   Oopsie, something went wrong");
			menu();
		}
		if (input.equalsIgnoreCase("exit")) {
			shutdown();
		}
		if (input.equalsIgnoreCase("start")) {
			this.sendMessage(input);
		}
		return input;

	}
}