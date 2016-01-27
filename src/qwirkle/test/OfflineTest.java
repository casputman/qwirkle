
package qwirkle.test;

import static org.junit.Assert.*;

import org.junit.Test;

import qwirkle.*;
import qwirkle.core.*;
import qwirkle.player.*;
import qwirkle.protocol.*;
import qwirkle.server.*;
import qwirkle.strategy.*;
import qwirkle.test.*;

public class OfflineTest {

	Game game;
	Player one;
	Player two;
	Player three;
	Player four;
	Strategy stupid;
	Strategy smart;
	Client client;
	Bag bag;
	
	@Before
	public void setUp() throws Exception {
		client = new Client();
		one = Player.createPlayer("Cas");
		two = Player.createPlayer("Wouter");
		three =  new ComputerPlayer("stupid");
		four =  new ComputerPlayer("stupid");
		game = new Game(one, two);
		stupid = new StupidStrategy();
		smart = new SmartStrategy();
		game.reset();
	}
	
	@Test
	public void playerTest() {
		assertEquals("", true, one.getName().equals("Cas"));
		assertEquals("", true, one.getName().equals("Wouter"));
		
		assertEquals("", true, three.getName() != null);
		assertEquals("", true, four.getName() != null);
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