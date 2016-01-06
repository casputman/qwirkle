package twee.server;

/**
 * Alle benodigde constanten voor commands tussen server en client. Op basis van het protocol van
 * vorig jaar, gemaakt door: Christian Versloot Aanpassingen door:
 * 
 * @author Martijn Gemmink
 * @author Bas Hendrikse
 * @version 1.2.1 (25-01-2015)
 */

public interface ProtocolConstants {

    /**
     * Commando en parameter delimeter. Zoals afgesproken worden de commando's en parameters
     * gescheiden door een spatie. Richting: [Server] --> [Client] || [Client] --> [Server]
     */
    String msgSeperator = " ";

    /**
     * Toegestane characters. Zoals afgesproken zijn alleen deze karakters in spelernamen
     * toegestaan. Te controleren met String.matches(charRegex) (return: true of false) Gebruikte
     * bron: http://stackoverflow.com/questions/5238491/check-if-string-contains-only-letters
     * Richting: n.v.t.
     */

    String charRegex = "[_a-zA-Z0-9]+";

    /**
     * De velden in het bord. Richting: n.v.t.
     */

    String red = "Mark.RED";
    String yellow = "Mark.YELLOW";
    String empty = "Mark.EMPTY";

    /**
     * De mogelijke redenen van endGame. Richting: [Server] -> [Client]
     */

    String connectionlost = "connectionlost";
    String unknownerror = "unknownerror";
    String winner = "winner";
    String draw = "draw";

    /**
     * De mogelijke te retourneren foutmeldingen. Richting: [Server] --> [Client]
     */
    String invalidUsername = "invalidUsername";
    String invalidMove = "invalidMove";
    String invalidCommand = "invalidCommand";
    String usernameInUse = "usernameInUse";
    String invalidUserTurn = "invalidUserTurn";
}
