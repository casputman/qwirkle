package twee.server;

/**
 * Protocol voor de te gebruiken commando's tussen server en client. Op basis van het protocol van
 * vorig jaar, gemaakt door: Christian Versloot Aanpassingen door:
 * 
 * @author Martijn Gemmink
 * @author Bas Hendrikse
 * @version 1.2.1 (25-01-2015)
 * 
 * 
 *          Delimeter beschrijving: Scheid ALLE commando's en parameters met de delimeter zoals
 *          vermeld in ProtocolConstants.java
 * 
 *          Gebruikersnaamcriteria: Controleer ALLE gebruikersnamen op de regular expression zoals
 *          vermeld in ProtocolConstants.java
 * 
 *          Param beschrijving: Een @param bij een string geeft aan welke parameters het commando
 *          daarnaast ook verwacht. Volgorde van de @params per commando (van boven naar beneden)
 *          geeft de volgorde van versturen aan.
 */
public interface ProtocolControl {

    /**
     * Vraag het bord op aan de server. Richting: [Client] -> [Server]
     */

    String getBoard = "getBoard";

    /**
     * Stuur het bord naar een client. Richting: [Server] -> [Client]
     * 
     * @param 42 keer een veld string gescheiden door een msgSeperator (zie ProtocolConstants.java)
     */
    String sendBoard = "sendBoard";

    /**
     * Verbinden van client met de server Richting: [Client] -> [Server]
     * 
     * @param naam
     *            - je naam
     */
    String joinRequest = "joinRequest";

    /**
     * Geef een kleur aan de client mits er geen fouten optreden. NOTE: bij het optreden van een
     * fout wordt er een invalidCommand door de server gestuurd. (zie ProtocolConstants.java)
     * Richting: [Server] -> [Client]
     * 
     * @param kleur
     *            - je kleur (zie ProtocolConstants.java)
     */
    String acceptRequest = "acceptRequest";

    /**
     * Vertel de spelers dat het spel kan beginnen. Richting: [Server] -> [Client]
     * 
     * @param clientname1
     * @param clientname2
     */
    String startGame = "startGame";

    /**
     * Vertel de server dat je een zet hebt gedaan. Richting: [Client] -> [Server]
     * 
     * @param indexcijfer
     *            van 0 tot 41, het te zetten vakje.
     */
    String doMove = "doMove";

    /**
     * Vertel een client dat een zet is gedaan. Richting: [Server] -> [Client]
     * 
     * @param index
     *            - het gezette vakje
     * @param naam
     *            - naam van de speler die de zet doet
     * @param valid
     *            - boolean of de zet valide is
     * @param nextplayer
     *            - (naam) geeft de beurt aan de volgende speler.
     */
    String moveResult = "moveResult";

    /**
     * Client wil weten welke speler aan zet is. Richting: [Client] -> [Server]
     */
    String playerTurn = "playerTurn";

    /**
     * Vertelt de client wie er aan zet is. Richting: [Server] -> [Client]
     * 
     * @param naam
     *            - naam van de speler die aan zet is
     */
    String turn = "turn";

    /**
     * Vertel een client dat het spel afgelopen is. Richting: [Server] -> [Client]
     * 
     * @param resultString
     *            - wie het spel heeft gewonnen
     * @param reden
     *            - wat de reden is van het stoppen van het spel
     */
    String endGame = "endGame";

    // ---- Extensions ----

    /**
     * Extension - Chatbox Richting: [Client] -> [Server]
     * 
     * @param message
     *            - het bericht dat je wilt sturen.
     */
    String sendMessage = "sendMessage";

    /**
     * Extension - Chatbox Richting: [Server] -> [Client]
     * 
     * @param name
     *            - de naam van de client die het bericht verstuurd
     * @param message
     *            - het bericht dat naar alle spelers verbonden aan de server wordt verstuurd
     */
    String broadcastMessage = "broadcastMessage";

    /**
     * Extension - Leaderboard Client vraagt het leaderboard op. Richting: [Client] -> [Server]
     */
    String getLeaderboard = "getLeaderboard";

    /**
     * Extension - Leaderboard Server stuurt het leaderboard terug. Richting: [Server] -> [Client]
     */
    String sendLeaderboard = "sendLeaderboard";

    /**
     * Een rematch request van de client, als beide clients een rematch request hebben gestuurd,
     * reset de game. Richting: [Client] -> [Server]
     */
    String rematch = "rematch";

    /**
     * Een bevestiging van de server dat de twee spelers nog een keer willen spelen. De server
     * stuurt, als hij van beide Clients een rematch command binnen krijgt rematchConfirm, hierna
     * zal de game van de server resetten en de games van de Clients, zodat ze beide in dezelfde
     * staat zijn. Richting: [Server] -> [Client]
     */
    String rematchConfirm = "rematchConfirm";

}
