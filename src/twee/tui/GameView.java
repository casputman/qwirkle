package twee.tui;

import java.util.Observer;

public interface GameView extends Observer {

    public void start();

    public void stop();

    public void reset();
    
    public void checkInput(String input);
}
