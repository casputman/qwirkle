/*
package twee.test;

import twee.*;
import twee.player.*;
import twee.server.ClientHandler;
import twee.server.Server;
import twee.strategy.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class OfflineTest {

	Game game;
	Player one;
	Player two;
	Player three;
	Player four;
	Strategy stupid;
	Strategy smart;
	Client client;
	
	@Before
	public void setUp() throws Exception {
		client = new Client("test");
		one = Player.createPlayer(Mark.YELLOW, "Sebastiaan");
		two =  new ComputerPlayer(Mark.RED, "smart");
		three =  new ComputerPlayer(Mark.YELLOW, "stupid");
		four =  new ComputerPlayer(Mark.RED, "stupid");
		game = new Game(one, two);
		stupid = new Stupid();
		smart = new Smart();
		game.reset();
	}
	
	@Test
	public void playerTest() {
		assertEquals("", true, one.getName().equals("Sebastiaan"));
		assertEquals("", true, one.getMark().equals(Mark.YELLOW));
		assertEquals("", false, one.getMark().equals(Mark.RED));
		assertEquals("", false, one.getMark().equals(Mark.EMPTY));
		
		assertEquals("", true, ComputerPlayer.NAMES.contains(two.getName()));
		assertEquals("", true, two.getMark().equals(Mark.RED));
		assertEquals("", false, two.getMark().equals(Mark.YELLOW));
		assertEquals("", false, two.getMark().equals(Mark.EMPTY));
	}
	
	@Test
	public void strategyTest() {
		stupid.determineMoveInt(Server.getStringArray(game), game.one.getMark());
		stupid.determineMoveInt(Server.getStringArray(game), game.two.getMark());
		smart.determineMoveInt(Server.getStringArray(game), game.one.getMark());
		smart.determineMoveInt(Server.getStringArray(game), game.two.getMark());
		game.takeTurn(4);
		game.takeTurn(4);
		game.takeTurn(4);
		game.takeTurn(4);
		game.takeTurn(4);
		game.takeTurn(4);
		game.takeTurn(4);
		smart.determineMoveInt(Server.getStringArray(game), game.one.getMark());
		smart.determineMoveInt(Server.getStringArray(game), game.two.getMark());
		assertEquals("", true, Integer.class.cast(Smart.gravity(4, Server.getStringArray(game))).getClass().getName().equals(Integer.class.getName()));
		assertEquals("", true, two.determineMove(game));
	}
	
	@Test
	public void gameTest() {
		assertEquals("", true, Integer.class.cast(Board.getColumn(4)).getClass().getName().equals(Integer.class.getName()));
		assertEquals("", true, game.getRules() instanceof Rules);
		Board.getColumn(6);
		game.view.checkInput("RESTART");
		game.view.checkInput("HELP");
		game.view.checkInput("EXIT");
		game.view.checkInput("RESTART");
	}
	
	@Test
	public void clientTest() {
		client.start();
		client.online = true;
		client.checkOnline("online");
		client.online = false;
		client.checkOnline("offline");
		client.checkOnline("test");
	}
	
	@Test
	public void horizontalTest() {
		game.takeTurn(1);
		game.takeTurn(7);
		game.takeTurn(2);
		game.takeTurn(7);
		game.takeTurn(3);
		game.takeTurn(7);
		smart.determineMoveInt(Server.getStringArray(game), game.one.getMark());
		smart.determineMoveInt(Server.getStringArray(game), game.two.getMark());
		game.takeTurn(4);
	}
	
	@Test
	public void verticalTest() {
		game.takeTurn(7);
		game.takeTurn(1);
		game.takeTurn(7);
		game.takeTurn(2);
		game.takeTurn(7);
		game.takeTurn(3);
		smart.determineMoveInt(Server.getStringArray(game), game.one.getMark());
		smart.determineMoveInt(Server.getStringArray(game), game.two.getMark());
		game.takeTurn(7);
	}
	
	@Test
	public void bufferTest() {
		game.start();
	}
	
	@Test
	public void diagonalTest() {		
		game.takeTurn(1);
		game.takeTurn(2);
		game.takeTurn(2);
		game.takeTurn(4);
		game.takeTurn(3);
		game.takeTurn(3);
		game.takeTurn(3);
		game.takeTurn(4);
		game.takeTurn(4);
		game.takeTurn(5);
		smart.determineMoveInt(Server.getStringArray(game), game.one.getMark());
		smart.determineMoveInt(Server.getStringArray(game), game.two.getMark());
		game.takeTurn(4);
		game.view.checkInput("RESTART");
		
		game.takeTurn(7);
		game.takeTurn(6);
		game.takeTurn(6);
		game.takeTurn(4);
		game.takeTurn(5);
		game.takeTurn(5);
		game.takeTurn(5);
		game.takeTurn(4);
		game.takeTurn(4);
		game.takeTurn(1);
		smart.determineMoveInt(Server.getStringArray(game), game.one.getMark());
		smart.determineMoveInt(Server.getStringArray(game), game.two.getMark());
		game.takeTurn(4);
	}
	
	@Test
	public void fullBoardTest() {
		game.takeTurn(1);
		game.takeTurn(2);
		game.takeTurn(3);
		game.takeTurn(4);
		game.takeTurn(5);
		game.takeTurn(6);
		game.takeTurn(7);
		game.takeTurn(7);
		game.takeTurn(6);
		game.takeTurn(5);
		game.takeTurn(4);
		game.takeTurn(3);
		game.takeTurn(2);
		game.takeTurn(1);
		game.takeTurn(1);
		game.takeTurn(2);
		game.takeTurn(3);
		game.takeTurn(4);
		game.takeTurn(5);
		game.takeTurn(6);
		game.takeTurn(7);
		game.takeTurn(2);
		game.takeTurn(1);
		game.takeTurn(4);
		game.takeTurn(3);
		game.takeTurn(6);
		game.takeTurn(5);
		game.takeTurn(1);
		game.takeTurn(7);
		game.takeTurn(3);
		game.takeTurn(2);
		game.takeTurn(5);
		game.takeTurn(4);
		game.takeTurn(7);
		game.takeTurn(6);
		game.takeTurn(3);
		game.takeTurn(2);
		game.takeTurn(5);
		game.takeTurn(4);
		game.takeTurn(7);
		game.takeTurn(6);
		smart.determineMoveInt(Server.getStringArray(game), game.one.getMark());
		smart.determineMoveInt(Server.getStringArray(game), game.two.getMark());
		game.takeTurn(1);
	}
}
*/