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

public class Client extends Thread {

	public static void main(String[] args) throws Exception { 
		new Client();
	}
	public void run(){
		try {
			String message = in.readLine();
			while (message != null){
				if (message.equalsIgnoreCase("EXIT")){
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
	private String clientName;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	public boolean online;
	private Player you;
	private Player player1;
	private Player player2;
	private Player player3;
	private Player player4;
	private Game game;

	public Client(){
		startUp();
	}

	private void startUp(){
		if (isOnline()){
			initializeClient();
		} else {
			game = new Game(player1, player2, player3, player4);
			game.start();
			game.offline = true;
		}
	}

	private boolean isOnline() {
		String input = "";
		input = getInput("Do you want to play online or offline?");
		if (!checkOnline(input)) {
			isOnline();
		}
		return online;
	}

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

	public void initializeClient(){
		String playerType = getInput("\nIs this player a human or computer player?"
				+ "\nPlease input either 'human' or 'computer'");
		if (playerType.equalsIgnoreCase("human")){
			you = new HumanPlayer("RandomHumanName");
		} else if (playerType.equalsIgnoreCase("computer")){
			you = new ComputerPlayer("RandomComputerName");
		} else {
			initializeClient();
		}
		clientName = you.getName();
		InetAddress host = null;
		int port = 0;

		try {
			host = InetAddress.getByName(getInput("\nPlease enter the hostname of the server "
					+ "(leave blank for localhost"));
		} catch (UnknownHostException e) {
			System.err.println("ERROR: not a valid hostname!");
			startUp();
		}

		try {
			String portInput = getInput("\nPlease enter the port of the server "
					+ "(leave blank for port 1337)");
			if (portInput.equals("")) {
				port = 1337;
			} else {
				port = Integer.parseInt(portInput);
			}
		} catch (NumberFormatException e) {
			System.err.println("ERROR: not a valid portnummer!");
			startUp();
		}

		try {
			sock = new Socket(host, port);
		} catch (IOException e) {
			System.err.println("ERROR: socket could not be created!");
			startUp();
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

	private void readResponse(){
		try {
			String reply = in.readLine();
			while (reply != null){
				String[] antw = reply.split(Protocol.MESSAGESEPERATOR);
				if (reply.startsWith(Protocol.SERVER_CORE_JOIN_ACCEPTED)){
					System.out.println("You have succesfully joined the server");
				} else if (antw[1].equals(Protocol.SERVER_CORE_MOVE_DENIED)) {
					System.out.println("This is not a valid move, please try again");
				} else if (reply.startsWith(Protocol.SERVER_CORE_START)) {
					System.err.println("THE GAME IS STARTING");
					sleep(1000);
					game.getBoard().printBoard(); 
				} else if (reply.startsWith(Protocol.SERVER_CORE_MOVE_MADE)){
					String coords = (antw[1] + Protocol.MESSAGESEPERATOR + antw[2]);
					Tile tile = game.getBoard().makeTile(Integer.parseInt(antw[3]), Integer.parseInt(antw[4]));
					game.getBoard().makeMove(coords, tile, game);
					Map<String, Tile> moveMap = new HashMap<String, Tile>();
					moveMap.put(coords, tile);
					game.calculateScore(moveMap);
					game.getBoard().printBoard();
				} else if (reply.startsWith(Protocol.SERVER_CORE_GAME_ENDED)){
					game.getBoard().printBoard();
					game.getRules().listScores();
					System.out.println("The ame has ended!");

				}
			}
			readResponse();
		} catch (IOException e) {
			System.err.println("Something went wrong at the server, so we lost our connection");
			menu();
		} catch (InterruptedException e) {
			System.out.println("Something went wrong");
			menu();
		}
	}

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
				startUp();
			} else if (menu.equalsIgnoreCase("exit")) {
				shutdown();
			} else {
				System.err.println("No, don't type '" + menu
						+ "' that was not one of the possibilities...");
				menu();
			}
		}
	}

	/**
	 * Writes the String msg to the outputstream
	 * @param msg
	 */
	//@ requires out != null;
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
	//@ requires sock != null;
	//@ ensures sock.isClosed();
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
	 * @param variable
	 * @return
	 */
	//@ requires in != null;
	//@ ensures \result != "";
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
		return input;

	}

	public class StartTimer extends Thread{

		public boolean done = false;
		private Client client;

		public StartTimer(Client client) {
			this.client = client;
		}
	}
	  private void connectToServer() {
	        this.sendMessage(Protocol.CLIENT_CORE_JOIN + Protocol.MESSAGESEPERATOR + clientName);
	        this.readResponse();

	        do {
	            String input = getInput("");
	            this.sendMessage(input);
	        } while (true);
	    }

}
