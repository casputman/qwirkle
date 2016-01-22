package qwirkle.server;

import qwirkle.core.Game;
import qwirkle.player.Player;
import qwirkle.protocol.Protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.net.Socket;

public class ClientHandler extends Thread implements ProtocolControl{

    private Server server;
    private Socket sock;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientName;
    private Game game;
    private Player player;
    public boolean rematch;

    /**
     * Constructs a ClientHandler object
     * Initialises both Data streams.
     *@ requires server != null && sock != null;
     */
    public ClientHandler(Server serverArg, Socket sockArg) throws IOException {
        server = serverArg;
        sock = sockArg;
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
    }

    /**
         * This method takes care of sending messages from the Client.
         * Every message that is received, is preprended with the name
         * of the Client, and the new message is offered to the Server
         * for broadcasting. If an IOException is thrown while reading
         * the message, the method concludes that the socket connection is
         * broken and shutdown() will be called. 
    */
    public void run() {
        try {
            while (true) {
                String input = in.readLine().trim();
                if (input.equals("Connection.Shutdown")) {
                    shutdown();
                } else if (input.startsWith(Protocol.CLIENT_CORE_JOIN)){
                	long millis = System.currentTimeMillis() % 1000;
                    clientName = ("player" +(millis));
                }
                server.analyzeString(this, input);
             }
        } catch (IOException e) {
            shutdown();
        }
    }

    /**
     * This method can be used to send a message over the socket
     * connection to the Client. If the writing of a message fails,
     * the method concludes that the socket connection has been lost
     * and shutdown() is called.
     */
    public void sendMessage(String msg) {
        try {
            out.write(msg);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            shutdown();
        }
    }

    /**
     * This ClientHandler signs off from the Server and subsequently
         * sends a last broadcast to the Server to inform that the Client
         * is no longer participating in the chat. 
     */
    private void shutdown() {
        server.removeHandler(this);
    }

    //@ pure
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getClientName( ) {
        return clientName;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
