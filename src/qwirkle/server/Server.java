package qwirkle.server;

import qwirkle.core.Game;
import qwirkle.core.Tile;
import qwirkle.protocol.Protocol;
import qwirkle.player.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Wouter Bolhuis & Cas Putman
 * @version 1.6
 */
public class Server {

	public static void main(String[] args) {
		new Server();
	}

	private int port = 1337;
	private /* @ spec_public @ */ List<ClientHandler> threads;
	private /* @ spec_public @ */ List<ClientHandler> joined;
	private /* @ spec_public @ */ List<ClientHandler> playing;
	private /* @ spec_public @ */ Map<Game, ClientHandler[]> gameMap = new HashMap<Game, ClientHandler[]>();
	private /* @ spec_public @ */ ServerSocket sock;

	/**
	 * Waits for user input on the port if the user inputs nothing it will use
	 * port 1337 calls startServer() and displays the used IP and port
	 */
	// @ ensures threads != null;
	// @ ensures joined != null;
	// @ ensures playing != null;
	public Server() {
		threads = new ArrayList<ClientHandler>();
		joined = new ArrayList<ClientHandler>();
		playing = new ArrayList<ClientHandler>();
		try {
			String portInput = getInput("Please enter the port of the server " + "(leave empty for port 1337)");
			if (portInput.equals("")) {
				port = 1337;
			} else {
				port = Integer.parseInt(portInput);
			}
			try {
				System.err.println("          STARTING SERVER WITH IP: " + InetAddress.getLocalHost()
						+ "\n               AND PORT: " + port + "\n");
			} catch (UnknownHostException e) {
				System.err.println("ERROR: couldn't figure out and display the ip, " + "please try again");
				new Server();
			}
			startServer();
		} catch (NumberFormatException e) {
			System.err.println("ERROR: not a valid portnummer!");
			new Server();
		}
	}

	/**
	 * Opens the ServerSocket Whenever someone connects it will assign a
	 * clienthandler to them and output a join message
	 */
	// @ ensures sock != null;
	public void startServer() {
		try {
			sock = new ServerSocket(port);
			int telInt = 0;
			while (true) {
				Socket socket = sock.accept();
				ClientHandler handler = new ClientHandler(this, socket);
				handler.start();
				addHandler(handler);
				System.out.println("\n   CONNECTING: [Client no. " + (++telInt) + "]" + "connected\n");
			}
		} catch (IOException e) {
			System.err.println("ERROR: couldn't create a socket, \n" + "is the port already in use?");
			new Server();
		}
	}

	/**
	 * This message gets called by a handler to analyze the message from the
	 * client Depending on the input from said client the server will call
	 * methods and/or change variable values
	 * 
	 * @param handler
	 * @param msg
	 */
	int playerAmount = 0;
	Player player1 = null;
	Player player2 = null;
	Player player3 = null;
	Player player4 = null;

	// @ requires !threads.isEmpty() || !joined.isEmpty() || !playing.isEmpty();
	public synchronized void analyzeString(ClientHandler handler, String msg) {
		System.err.println("\nINPUT FROM " + handler.getClientName() + ": " + msg);
		String[] input = msg.split(Protocol.MESSAGESEPERATOR);
		if (input[0].equals(Protocol.CLIENT_CORE_JOIN)) {
			threads.remove(handler);
			if (joined.size() < 4) {
				joined.add(handler);
				if (playerAmount == 0) {
					player1 = Player.createPlayer(handler.getClientName());
					handler.setPlayer(player1);
					playerAmount++;
				} else if (playerAmount == 1) {
					player2 = Player.createPlayer(handler.getClientName());
					handler.setPlayer(player2);
					playerAmount++;
				} else if (playerAmount == 2) {
					player3 = Player.createPlayer(handler.getClientName());
					handler.setPlayer(player3);
					playerAmount++;
				} else if (playerAmount == 3) {
					player4 = Player.createPlayer(handler.getClientName());
					handler.setPlayer(player4);
					playerAmount = 0;
				}
				acceptRequest(handler);
				sendMessage(handler, "Waiting for game to start.");
				System.out.println("JOINING: " + handler.getClientName() + " is waiting for a game!");
			}
			/*
			 * } else if
			 * (!joined.get(0).getClientName().equals(handler.getClientName()))
			 * { joined.add(handler); Player two =
			 * Player.createPlayer(handler.getClientName());
			 * handler.setPlayer(two); acceptRequest(handler);
			 * sendMessage(handler, Protocol.SERVER_CORE_JOIN_ACCEPTED);
			 * System.out.println("JOINING: " + handler.getClientName() +
			 * " is waiting for a game."); }
			 */
		} else if (input[0].equals(Protocol.CLIENT_CORE_START)) {
			Game game = null;
			ClientHandler[] clients = null;
			if (joined.size() == 1) {
				sendMessage(handler, "I pity the fool who starts with only 1 player!");
			}
			if (joined.size() == 2) {
				game = new Game(player1, player2);
			} else if (joined.size() == 3) {
				game = new Game(player1, player2, player3);
			} else if (joined.size() == 4) {
				game = new Game(player1, player2, player3, player4);
			}
			clients = new ClientHandler[joined.size()];
			for (ClientHandler handle : joined) {
				for (int i = 0; i < joined.size(); i++) {
					clients[i] = handle;
				}
			}

			gameMap.put(game, clients);
			for (ClientHandler handle : joined) {
				System.out.println(handle);
				handle.setGame(game);
				for (int i = 0; i < clients.length; i++) {
					sendMessage(clients[i], Protocol.SERVER_CORE_START + Protocol.MESSAGESEPERATOR + player1.getName()
							+ Protocol.MESSAGESEPERATOR + player2.getName());
					playing.add(handle);
				}
			}
			joined.clear();
			game.run();

		} else if (input[0].equals(Protocol.CLIENT_CORE_MOVE)) {
			doMove(handler, input[1]);
		} else if (input[0].equals(Protocol.CLIENT_CORE_PLAYERS)) {
			for (int i = 0; i < joined.size(); i++) {
				sendMessage(handler,
						Protocol.SERVER_CORE_PLAYERS + Protocol.MESSAGESEPERATOR + joined.get(i).getClientName());
			}
		} else if (input[0].equals(Protocol.CLIENT_CHAT_WHISPER_SEND)) {
			for (int n = 0; n < input.length; n++) {
				sendMessage(handler, input[n]);
			}
		} else if (input[0].equals(Protocol.CLIENT_CORE_DONE)) {
			for (int i = 0; i < joined.size(); i++) {
				sendMessage(joined.get(i), "score");
			}

		} else if (input[0].equals(Protocol.CLIENT_CORE_SWAP)) {
			ArrayList<Tile> hand = handler.getPlayer().getHand();
			int shape = Integer.parseInt(input[1]);
			int colour = Integer.parseInt(input[2]);
			Tile tile = handler.getGame().getBoard().makeTile(shape, colour);
			if (!(handler.getGame().getBag().tileBag.containsValue(1))
					|| !(handler.getGame().getBag().tileBag.containsValue(2))
					|| !(handler.getGame().getBag().tileBag.containsValue(3)) || (!hand.contains(tile))) {
				sendMessage(handler, Protocol.SERVER_CORE_SWAP_DENIED);
			} else {
				handler.getPlayer().addTile(handler.getGame().getBag().drawTile());
				sendMessage(handler, Protocol.SERVER_CORE_SWAP_ACCEPTED);
			}
		} else if (input[0].equals(Protocol.CLIENT_CORE_EXIT)) {
			removeHandler(handler);
		}
	}

	/**
	 * send ProtocolControl.acceptRequest to the handler
	 * 
	 * @param handler
	 * @param mark
	 */
	private synchronized void acceptRequest(ClientHandler handler) {
		sendMessage(handler, Protocol.SERVER_CORE_JOIN_ACCEPTED + Protocol.MESSAGESEPERATOR + handler.getName());
	}

	/**
	 * Whenever a new client connects the handler assigned to that client gets
	 * added to the handler list threads
	 * 
	 * @param handler
	 */
	// @ requires !threads.contains(handler);
	public synchronized void addHandler(ClientHandler handler) {
		threads.add(handler);
	}

	/**
	 * Whenever a clients disconnects the clients ClientHandler gets remove from
	 * whatever list it was in
	 * 
	 * @param handler
	 */
	// @ requires threads.contains(handler) || joined.contains(handler) ||
	// playing.contains(handler);
	/*
	 * @ ensures !threads.contains(handler) || !joined.contains(handler) ||
	 * !playing.contains(handler);@
	 */
	public synchronized void removeHandler(ClientHandler handler) {
		if (threads.contains(handler)) {
			threads.remove(handler);
		} else if (playing.contains(handler)) {
			playing.remove(handler);
		} else if (joined.contains(handler)) {
			joined.remove(handler);
		}
		System.out.println("\n   DISCONNECTING: " + handler.getClientName() + " has left the game\n");
		sendMessage(handler(handler), Protocol.SERVER_CORE_GAME_ENDED + Protocol.MESSAGESEPERATOR
				+ handler.getClientName() + Protocol.MESSAGESEPERATOR + Protocol.SERVER_CORE_DISCONNECT);
	}

	/**
	 * Checks whether the client requesting the move is the client which should
	 * be making a move If not it will send that client
	 * ProtocolConstants.invalidCommand ProtocolControl.invalidUserTurn If it is
	 * it will check if it is a valid move To do so it will first change the
	 * index to the respective column and then check if there is still space
	 * left in this column If there is no space left it will return
	 * ProtocolConstants.invalidMove ProtocolConstants.invalidMove if there is
	 * space left it will make the move and return the
	 * ProtocolControl.moveResult command to the client
	 * 
	 * @param handler
	 * @param decision
	 */
	// @ requires handler != null;
	public synchronized void doMove(ClientHandler handler, String coordinates) {
		String coordinatesArray[] = coordinates.split(Protocol.MESSAGESEPERATOR);
		int xCoordinate = Integer.parseInt(coordinatesArray[0]);
		int yCoordinate = Integer.parseInt(coordinatesArray[1]);
		int shape = Integer.parseInt(coordinatesArray[2]);
		int color = Integer.parseInt(coordinatesArray[3]);
		Tile tile = handler.getGame().getBoard().makeTile(shape, color);
		boolean moveAllowed = false;
		if (handler.getGame().current.equals(handler.getPlayer())) {
			moveAllowed = (handler.getGame().getRules().isMoveAllowed(coordinates, tile));
			{
			}

			if (moveAllowed) {
				handler.getGame().takeTurn(xCoordinate, yCoordinate, tile);
				ClientHandler[] doMove = gameMap.get(handler.getGame());
				if (!handler.getGame().getRunning()) {
					gameEnded(handler.getGame());
				}
				for (ClientHandler handle : doMove) {
					sendMessage(handle, Protocol.SERVER_CORE_MOVE_ACCEPTED);
					sendMessage(handle, Protocol.SERVER_CORE_MOVE_MADE + Protocol.MESSAGESEPERATOR + xCoordinate
							+ Protocol.MESSAGESEPERATOR + yCoordinate + Protocol.MESSAGESEPERATOR + tile);
				}
			} else {
				sendMessage(handler, Protocol.CLIENT_CORE_DONE + Protocol.MESSAGESEPERATOR
						+ Protocol.SERVER_CORE_MOVE_DENIED + Protocol.MESSAGESEPERATOR + handler.getClientName());

			}
		} else {
			sendMessage(handler, Protocol.SERVER_CORE_DENIED + Protocol.MESSAGESEPERATOR + Protocol.SERVER_CORE_TURN
					+ Protocol.MESSAGESEPERATOR + (handler.getGame().current.getName()));
		}
	}

	/**
	 * Checks whether the game has ended and sends the ProtocolControl.endGame
	 * command with the correct results to both participating clients
	 * 
	 * @param game
	 */
	// @ requires !game.getRunning();
	private void gameEnded(Game game) {
		ClientHandler[] clients = gameMap.get(game);
		if (!game.getRunning()) {
			if (game.getRules().hasWinner()) {
				for (ClientHandler client : clients) {
					if (game.getRules().isWinner(client.getPlayer())) {
						sendMessage(client, Protocol.SERVER_CORE_GAME_ENDED + Protocol.MESSAGESEPERATOR
								+ client.getClientName() + Protocol.MESSAGESEPERATOR + "WINNER");
						sendMessage(handler(client), Protocol.SERVER_CORE_GAME_ENDED + Protocol.MESSAGESEPERATOR
								+ client.getClientName() + Protocol.MESSAGESEPERATOR + "WINNER");
					}
				}
			} else {
				for (ClientHandler client : clients) {
					sendMessage(client, Protocol.SERVER_CORE_GAME_ENDED + Protocol.MESSAGESEPERATOR
							+ client.getClientName() + Protocol.MESSAGESEPERATOR + "DRAW");
				}
			}
		}
	}

	/**
	 * returns the player who is currently making a turn
	 * 
	 * @param handler
	 */
	// @pure
	public synchronized void turn(ClientHandler handler) {
		sendMessage(handler,
				Protocol.SERVER_CORE_TURN + Protocol.MESSAGESEPERATOR + handler.getGame().current.getName());
	}

	/**
	 * Closes the socket and terminates the server
	 */
	// @ ensures sock.isClosed();
	public void shutdown() {
		System.err.println("          STOPPING SERVER");
		try {
			sock.close();
		} catch (IOException e) {
			System.exit(0);
		}
		System.exit(0);
	}

	/**
	 * Prints the String variable and returns the user input
	 * 
	 * @param variable
	 * @return
	 */
	// @ensures \result != "";
	public String getInput(String variable) {
		String input = "";
		try {
			if (variable != "") {
				System.out.println(variable);
			}
			input = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
			if (input.equalsIgnoreCase("exit")) {
				shutdown();
			}

		} catch (IOException e) {
			System.err.println("Something went wrong");
			System.out.println("But you know shit happens");
			shutdown();
		}

		return input;

	}

	/**
	 * Sends the String message to the ClientHandler handler
	 * 
	 * @param handler
	 * @param message
	 */
	// @pure
	public void sendMessage(ClientHandler handler, String message) {
		if (joined.contains(handler) || playing.contains(handler) || threads.contains(handler)) {
			System.err.println("    SENDING TO " + handler.getClientName() + ": " + message);
			handler.sendMessage(message);
		}
	}

	/**
	 * Returns the other handler
	 * 
	 * @param handler
	 * @return
	 */
	// @ requires gameMap.get(handler.getGame())[0] == handler;
	// @ ensures \result == gameMap.get(handler.getGame())[1];
	// @ also
	// @ requires gameMap.get(handler.getGame())[0] != handler;
	// @ ensures \result == gameMap.get(handler.getGame())[0];
	// @pure
	public ClientHandler handler(ClientHandler handler) {
		ClientHandler[] handlers = gameMap.get(handler.getGame());
		ClientHandler toReturn = null;
		int player = 0;

		if (handlers[player].equals(handler) || !handler.equals(null)) {
			player += 1;
			player %= 4;
			toReturn = handlers[player];
		} else if (handler.equals(null)) {
			int random = (int) (Math.random() * 4 + 1);
			toReturn = handlers[random];
		} else {
			for (int i = 0; i < handlers.length; i++) {
				if (handlers[i].equals(handler)) {
					i += 1;
					i %= 4;
					toReturn = handlers[i];
				}
			}
		}
		return toReturn;
	}
}
