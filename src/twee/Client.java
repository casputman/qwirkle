package twee;

import twee.player.ComputerPlayer;
import twee.player.HumanPlayer;
import twee.player.Mark;
import twee.player.Player;
import twee.server.ProtocolConstants;
import twee.server.ProtocolControl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 * @author Wouter Bolhuis & Sebastiaan den Boer
 * @version 1.6
 */

public class Client extends Thread {

    public static void main(String[] args) throws Exception {
        new Client();
    }
  
    /**
     * Opens the inputstream and continuously checks it
     */
    //@ requires in != null;
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

    private /*@ spec_public @*/ String clientName;
    private /*@ spec_public @*/ Socket sock;
    private /*@ spec_public @*/ BufferedReader in;
    private /*@ spec_public @*/ BufferedWriter out;
    public boolean online;
    private /*@ spec_public @*/ String[] savedBoard = new String[42];
    private /*@ spec_public @*/ Player you;
    private Mark mark;
    private Game game;

    /**
     * Launches the startup method
     */
    public Client() {
        startup();
    }
    
    /**
     * This method is used to test initialization of the TUI
     * @param beta
     */
    public Client(String beta) {
        /*this constructor is used to test the initialization of the TUI when creating a client*/
    }

    /**
     * This method is used for testing as well
     */
    public void play() {
        //this method is used for testing as well
    }
    
    /**
     * This method checks whether the client wants to play online or offline 
     * using the isOnline() method
     * If the player wants to play online we call the method initializeClient()
     * If the player wants to play offline a new game will be created and started
     */
    //@ requires !isOnline();
    //@ ensures game != null;
    //@ ensures game.getRunning();
    //@ ensures game.offline;
    private void startup() {
        if (isOnline()) {
            initializeClient();
        } else {
            game = new Game();
            game.start();
            game.offline = true;
        }
    }
    
    /**
     * This will ask for user input using the method getInput(String msg) 
     * to find out if the players wants to play online of offline
     * It uses the checkOnline(String input) method to see what the player wants
     * This method is used in the startup() method
     * @return true when player wants to play online
     * @return false when player wants to play offline
     */
    //@ pure    
    private boolean isOnline() {
        String input = "";
        input = getInput("Do you want to play online or offline?");
        if (!checkOnline(input)) {
            isOnline();
        }

        return online;
    }
    
    /**
     * Reads it's input to find out if the player wanted to play online or offline
     * It sets online to true or false depending on what the player inputted
     * @param input
     * @return true when the input is equal to either "online" or "offline"
     */
    //@ requires input != "";
    //@ requires input == "ONLINE";
    //@ ensures online;
    //@ ensures \result;
    //@ also
    //@ requires input == "OFFLINE";
    //@ ensures !online;
    //@ ensures !\result;
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
     * This will in chronological order:
     * Ask the client if they want the player to be human or computer
     * if the client wants a computer player it will create a new ComputerPlayer()
     * if the client wants a human player it will create a new HumanPlayer()
     * It will ask what the IP address of the server is and store that information
     * It will ask what the port of the server is (if nothing is inputted it will use port 1337)
     * and store that information
     * It will connect to the server
     * It will initialize the inputstream
     * It will initialize the outputstream 
     */
    //@ ensures you != null;
    //@ ensures clientName != "";
    public void initializeClient() {
        String soortPlayer = getInput("\nIs this player a human or computer player?"
                + "\nPlease input either 'human' or 'computer'");
        if (soortPlayer.equalsIgnoreCase("human")) {
            you = new HumanPlayer(Mark.YELLOW);
        } else if (soortPlayer.equalsIgnoreCase("computer")) {
            you = new ComputerPlayer(Mark.YELLOW);
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
            startup();
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
    
    /**
     * This method will read whatever input it receives and process it
     * depending on the input different methods will be called
     * and different variables will be set
     * whenever processing is done it will read the new input
     */
    //@ requires in != null;
    private void readResponse() {
        try {
            String reply = in.readLine();
            while (reply != null) {
                String[] antw = reply.split(ProtocolConstants.msgSeperator);
                if (reply.startsWith(ProtocolControl.acceptRequest)) {
                    System.out.println("You have succesfully joined the server as " + antw[1]);
                    System.out.println("Please wait until the server has found you an opponent");
                    if (antw[1].equals("YELLOW")) {
                        mark = Mark.YELLOW;
                    } else {
                        mark = Mark.RED;
                    }
                } else if (reply.startsWith(ProtocolConstants.invalidCommand)) {
                    if (antw[1].equals(ProtocolConstants.invalidMove)) {
                        System.out.println("This is not a valid move, please try again");
                        makeMove(clientName);
                    } else if (antw[1].equals(ProtocolConstants.invalidUserTurn)) {
                        System.out.println("It is not your turn, please have patience");
                    } else if (antw[1].equals(ProtocolConstants.invalidUsername)) {
                        System.out.println("Your username is invalid, please think of a new one");
                        initializeClient();
                    } else if (antw[1].equals(ProtocolConstants.usernameInUse)) {
                        System.out.println("Someone on the server is already using this name");
                        initializeClient();
                    } else {
                        System.out.println("An unknown error occured");
                    }
                } else if (reply.startsWith(ProtocolControl.startGame)) {
                    System.err.println("THE GAME IS STARTING");
                    sleep(1000);
                    sendMessage(ProtocolControl.getBoard);
                    String board = in.readLine();
                    if (board != null) {
                        if (board.startsWith(ProtocolControl.sendBoard)) {
                            String[] fields = board.substring(
                                    ProtocolControl.sendBoard.length() + 1, board.length()).split(
                                    ProtocolConstants.msgSeperator);
                            savedBoard = fields;
                            System.out.println(printBoard(fields));
                        }
                    }
                    sendMessage(ProtocolControl.playerTurn);
                    readResponse();
                } else if (reply.startsWith(ProtocolControl.turn)) {
                    makeMove(antw[1]);
                } else if (reply.startsWith(ProtocolControl.moveResult)) {
                    if (antw[3].equals("true")) {
                        System.out.println(antw[2].replace(clientName, "You")
                                + " placed a token in column "
                                + Board.getColumn(Integer.parseInt(antw[1])));
                        sendMessage(ProtocolControl.getBoard);
                        String board = in.readLine();
                        if (board != null) {
                            if (board.startsWith(ProtocolControl.sendBoard)) {
                                String[] fields = board.substring(
                                        ProtocolControl.sendBoard.length() + 1, board.length())
                                        .split(ProtocolConstants.msgSeperator);
                                savedBoard = fields;
                                System.out.println(printBoard(fields));
                            }
                        }
                            makeMove(antw[4]);
                    } else {
                        System.out.println("This is not a valid move, please try again");
                        makeMove(clientName);
                    }
                } else if (reply.startsWith(ProtocolControl.endGame)) {
                    sendMessage(ProtocolControl.getBoard);
                    String board = in.readLine();
                    if (board != null) {
                        if (board.startsWith(ProtocolControl.sendBoard)) {
                            String[] fields = board.substring(
                                    ProtocolControl.sendBoard.length() + 1, board.length()).split(
                                    ProtocolConstants.msgSeperator);
                            savedBoard = fields;
                            System.out.println(printBoard(fields));
                        }
                    }
                    System.out.println("The game has ended");
                    if (antw[2].equals(ProtocolConstants.winner)) {
                        if (antw[1].equals(clientName)) {
                            System.out.println("You have won, congratulations");
                            replayMenu();
                        } else {
                            System.out.println("You have lost :(");
                            replayMenu();
                        }
                    } else if (antw[2].equals(ProtocolConstants.draw)) {
                        System.out.println("The game ended in a draw");
                        replayMenu();
                    } else if (antw[2].equals(ProtocolConstants.connectionlost)) {
                        System.out.println("The other player has left the game");
                        replayMenu();
                    } else {
                        System.out.println("An unknown error occured");
                        replayMenu();
                    }
                } else if (reply.startsWith(ProtocolControl.sendBoard)) {
                    String[] fields = new String[42];
                    fields = reply.replace(
                            ProtocolControl.sendBoard + ProtocolConstants.msgSeperator, "").split(
                            ProtocolConstants.msgSeperator);
                    savedBoard = fields;
                    System.out.println(printBoard(fields));
                } else if (reply.startsWith(ProtocolControl.rematchConfirm)) {
                    System.err.println("THE GAME IS RESTARTING");
                    sleep(1000);
                    sendMessage(ProtocolControl.getBoard);
                    String board = in.readLine();
                    if (board != null) {
                        if (board.startsWith(ProtocolControl.sendBoard)) {
                            String[] fields = board.substring(
                                    ProtocolControl.sendBoard.length() + 1, board.length()).split(
                                    ProtocolConstants.msgSeperator);
                            savedBoard = fields;
                            System.out.println(printBoard(fields));
                        }
                    }
                    sendMessage(ProtocolControl.playerTurn);
                    readResponse();
                }
                reply = in.readLine();
            }
        } catch (IOException e) {
            System.err.println("Something went wrong at the server, so we lost our connection");
            menu();
        } catch (InterruptedException e) {
            System.out.println("Something went wrong");
            menu();
        }
    }
    
    /**
     * This method will close the socket and thus disconnect from the server
     * afterwards it will give the client the opportunity to call startup()
     * and thus make all the startup decisions again or to terminate the client
     */
    //@ ensures !sock.isClosed();
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
                System.err.println("No, don't type '" + menu
                        + "' that was not one of the possibilities...");
                menu();
            }
        }
    }
    
    /**
     * This is the method called immediately after the game has ended
     * It allows the user to make the decision to replay the same game
     * when the other player decides to replay it will replay.
     * It will start a timer as soon as the user decides to replay
     * If one minute passed the user will be given this same menu again with the notice
     * that one minute has passed since the first decision was made
     */
    //@ requires in != null;
    public void replayMenu() {
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("WHO AWAKENS ME FROM MY SLUMBER");
            System.out.println("No seriously who and how, this isn't supposed to happen");
            System.out.println("It's actually quite awkward this...");
        }
        String rematch = getInput("Do you want to replay?");
        while (rematch != null) {
            if (rematch.contains("y")) {
                   sendMessage(ProtocolControl.rematch);
                   StartTimer timer = new StartTimer(this);
                   String input;
                try {
                    input = in.readLine();
                    if (input != null) {
                        if (input.startsWith(ProtocolControl.rematchConfirm)) {
                            System.err.println("The other player agreed to restart");
                            readResponse();
                            timer.done = true;
                        }
                    }
                } catch (IOException e) {
                    shutdown();
                }
                   
            } else if (rematch.contains("n")) {
                sendMessage("Rematch.Cancel");
                menu();
            } else {
                System.err.println("No, don't type '" + rematch
                        + "' that was not one of the possibilities...");
                menu();
            }
        }
    }

    /**
     * Prints the board whenever readResponse() reads ProtocolControl.sendBoard
     * @param fields
     * @return a String of the entire board
     */
    //@ requires fields.length == 42;
    //@ ensures \result != "";
    public String printBoard(String[] fields) {
        String toReturn = "\n";
        toReturn = toReturn + Board.SEPERATION + Board.TOP + Board.SEPERATION;
        int telInt = 0;

        while (telInt < Board.ROWS) {
            toReturn = toReturn + "|";
            for (int j = 0; j < Board.COLUMNS; j++) {
                toReturn = toReturn
                        + fields[(telInt * Board.COLUMNS + j)].toUpperCase().toString()
                        .replace("EMPTY", "   ").replace("YELLOW", "YEL").replace("MARK.", "")
                        + "|";
            }
            toReturn = toReturn + "\n";
            telInt++;
        }

        return toReturn + Board.SEPERATION;
    }

    /**
     * Sends the ProtocolControl.joinRequest command to the server
     */
    //@pure
    private void connectToServer() {
        this.sendMessage(ProtocolControl.joinRequest + ProtocolConstants.msgSeperator + clientName);
        this.readResponse();

        do {
            String input = getInput("");
            this.sendMessage(input);
        } while (true);
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
        
        /**
         * 
         */
        //@ requires !done;
        //@ ensures done;
        public void run() {
            try {
                Thread.sleep(60000);
                if (!done) {
                    System.err.println("The opposing player hasn't decided to replay yet");
                    client.replayMenu();
                    done = true;
                }
            } catch (InterruptedException e) {
               System.err.println("interrupted");
            }
        
        }
    }
}