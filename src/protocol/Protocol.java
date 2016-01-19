package protocol;
/**
 * Protocol groep 2 (BIT) 2016 
 * Interface Protocol met daarin de gemaakte protocol afspraken
 * @author  Wouter Bolhuis & Cas Putman
 * @version 1.3.0 (18-01-2016)
 * 
 * @changelog
 * - Commands waarmee kan worden opgevraagd hoeveel spelers er in de game zitten.
 * - SERVER_CORE_DONE wordt niet meer gebruikt na het doorgeven van de moves naar andere spelers, ipv daarvan wordt meteen SERVER_CORE_TURN gestuurd.
 * 
 * Vragen of opmerkingen? 
 * Stuur ons een email: 
 * w.t.c.bolhuis@student.utwente.nl
 * c.g.j.putman@student.utwente.nl 
 */


public interface Protocol {
	
	/* ---------------------------CORE COMMANDS ---------------------------	 */
	
	/* Wordt gezien als de scheiding tussen alle individuele delen van het commando
	 * Na dit teken komt het volgende deel van het commando
	 * bijvoorbeeld de volgende naam of zoiets.
	 */
	String MESSAGESEPERATOR = " ";	
	
	/* De poort die als standaard wordt gebruikt om verbinding te maken met een server
	 */
	String PORT = "1337";
	
	
	
	/* Wordt gebruikt door de client direct na verbinding te maken met de server.
	 * Om alle extensies die door de client worden ondersteund door te geven.
	 * De namen van de extensies staan onderaan aan gegeven
	 * Richting: Client -> Server.
	 * 
	 * @param extension = Naam van de ondersteunde extensies
	 * Dit kunnen meerdere extensies zijn, met spaties tussen elke extensie
	 * 
	 * @require extension bevat geen spaties
	 */
	String CLIENT_CORE_EXTENSION = "ex";
	
	/* Wordt gebruikt om vanuit de server aan de client te melden welke extensies gebruikt kunnen worden.
	 * De namen van de extensies staan onderaan aan gegeven
	 * Richting: Server -> Client
	 * 
	 * @param extension = naam van de extensies die de server en de client overeenkomen 
	 * Dit kunnen meerdere extensies zijn, met spaties tussen elke extensie
	 * Dit kunnen er nooit meer zijn dan de client heeft gestuurd.
	 *  
	 * @require extension bevat geen spaties
	 */
	String SERVER_CORE_EXTENSION = "returnEx";
	
	/* Manier om zelf een naam de definieren bij de server. Nodig voor bijvoorbeeld challenge en leaderboard plugins.
	 * Richting: Client -> Server
	 * 
	 * @param name = Naam waarmee men bekend wilt staan bij de server.
	 * 
	 * @require name bevat geen spaties.
	 */
	String CLIENT_CORE_LOGIN = "login";
	
	/* Als de login succesvol is.
	 * Richting: Server -> Client
	 */
	String SERVER_CORE_LOGIN_ACCEPTED = "loginAccepted";
	
	/* Als de login niet succesvol is.
	 * bijvoorbeeld als er al iemand is met die naam.
	 * of als deze naam alleen in kan loggen met secure login.
	 * Richting: Server -> Client 
	 */
	String SERVER_CORE_LOGIN_DENIED = "loginDenied";
	
	/* Het commando om deel te nemen aan een game zonder daarbij eerst zelf een naam te definieren
	 * Richting: Client -> Server
	 */
	String CLIENT_CORE_JOIN = "join";
	
	/* Als het join request van de client gelukt is
	 * Richting: Server -> Client
	 * 
	 * @param name = Naam die de server vanaf nu hanteert voor de client
	 * 
	 * @require name bevat geen spaties
	 */
	String SERVER_CORE_JOIN_ACCEPTED = "joinAccepted";
	
	/* Als het join request van de client niet uitgevoerd kan worden
	 * Richting: Server -> Client 
	 */
	String SERVER_CORE_JOIN_DENIED = "joinDenied";
	
	/* Het verzoek om te weten welke spelers er in de game zitten.
	 * Richting: Client -> Server 
	 */
	String CLIENT_CORE_PLAYERS = "getPlayers";
	
	/* Om de client te laten weten wie er in de game zitten. 
	 * Richting: Server -> Client
	 * 
	 * @param name = naam van een spelers die in de game zit
	 * Dit kunnen meerdere namen zijn, met spaties tussen elke naam
	 * 
	 * @require name bevat geen spaties.
	 */
	String SERVER_CORE_PLAYERS = "sendPlayers";
	
	/* Het verzoek van de client naar de server om te beginnen.
	 * Richting: Client -> Server 
	 */
	String CLIENT_CORE_START = "start";	
	
	/* Het bericht dat de server stuurt als er begonnen wordt. 
	 * Richting: Server -> Client (ALL)
	 * 
	 * @param name = naam van een spelers die meespeelt
	 * Dit kunnen meerdere namen zijn, met spaties tussen elke naam
	 * 
	 * @require name bevat geen spaties.
	 */
	String SERVER_CORE_START = "starting";
	
	/* Het bericht van de server aan een client als respons op het start commando indien de server niet meteen gaat beginnen
	 * Richting: Server -> Client 
	 */
	String SERVER_CORE_START_DENIED = "startDenied";
	
	/* De manier waarop de server duidelijk maakt wie er aan de beurt is.
	 * Dit wordt onder andere gestuurd nadat alle moves door de server zijn doorgegeven.
	 * Richting: Server -> Client (ALL)
	 * 
	 * @param name = naam van de client die aan de beurt is
	 * 
	 * @require name bevat geen spaties
	 */
	String SERVER_CORE_TURN = "turn";
	
	/* Het commando om een move door te geven aan de server.
	 * Nadat alle moves zijn doorgegeven stuurt de client ook het done command.
	 * Richting: Client -> Server
	 * 
	 * @param x = x coordinaat van de plaatsing
	 * @param y = y coordinaat van de plaatsin
	 * @param shape = de gekozen vorm
	 * @param colour = de gekozen kleur
	 * 
	 * @require x = integer
	 * @require y = integer
	 * @require shape = integer
	 * @require colour = integer
	 */
	String CLIENT_CORE_MOVE = "move";
	
	/* Na het move bericht is dit één van de mogelijke responses
	 * Richting: Server -> Client
	 */
	String SERVER_CORE_MOVE_ACCEPTED = "moveAccepted";
	
	/* Dit is de andere mogelijke response.
	 * Dit bijvoorbeeld als de tile niet in de inventory van de client zit.
	 * Of als er al een tile op die coordinaten ligt.
	 * Richting: Server -> Client
	 */
	String SERVER_CORE_MOVE_DENIED = "moveDenied";
	
	/* Als de move geaccepteerd is door de server stuurt deze ook meteen dit command naar alle spelers.
	 * Nadat de server alle gemaakt moves door een speler heeft doorgegeven stuurt de server ook het done command.
	 * Richting: Server -> Client (ALL)
	 * 
	 * @param x = x coordinaat van de plaatsing
	 * @param y = y coordinaat van de plaatsing
	 * @param shape = vorm van de tile
	 * @param colour = kleur van de tile
	 * 
	 * @require x = integer
	 * @require y = integer
	 * @require shape = integer
	 * @require colour = integer
	 */
	String SERVER_CORE_MOVE_MADE = "moveMade";
	
	/* Voor de client om aan te geven dat ze klaar zijn met een reeks commando's sturen
	 * Dit wordt gebruikt na:
	 * - het doorgeven van alle moves aan de server
	 * - het doorgeven van alle steentjes die ze willen vervangen.
	 * Richting: Client -> Server
	 */
	String CLIENT_CORE_DONE = "done";
	
	/* Voor de server om aan te geven dat ze klaar is met een reeks commando's sturen
	 * Dit wordt gebruikt na:
	 * - de server klaar is met een client tiles sturen 
	 * Richting: Server -> Client
	 */
	String SERVER_CORE_DONE = "done";
	
	/* Het commando om de scores van alle spelers door te sturen.
	 * Dit commando wordt gebruikt nadat een client klaar is met alle moves maken.
	 * Richting: Server -> Client (ALL)
	 * 
	 * @param name = naam van de speler
	 * @param score = score van de speler
	 * Dit gaat altijd over meerdere spelers, met spaties tussen elk deel
	 * 
	 * @require name bevat geen spaties
	 * @require score = integer
	 */
	String SERVER_CORE_SCORE = "score";
	
	/* Nadat een speler moves heeft gedaan, of tiles heeft geswapt stuurt de server deze clients genoeg tiles om ervoor te zorgen dat de speler weer zes tiles heeft.
	 * Nadat alle tiles zijn doorgegeven gebruikt de server ook het done command.
	 * Richting: Server -> Client
	 * 
	 * @param shape = vorm van de tile
	 * @param colour = kleur van de tile
	 * 
	 * @require shape = integer
	 * @require colour = integer
	 */
	String SERVER_CORE_SEND_TILE = "receiveTile";
	
	/* Als een speler in plaats van een move wilt doen van tiles af wilt
	 * Nadat alle tiles zijn doorgegeven gebruikt de client ook het done command.
	 * Richting: Client -> Server
	 * 
	 * @param shape = vorm van de tile
	 * @param colour = kleur van de tile
	 * 
	 * @require shape = integer
	 * @require colour = integer
	 */
	String CLIENT_CORE_SWAP = "swap";
	
	/* Als de swap van de client met succes is afgerond.
	 * Richting: Server -> Client
	 */
	String SERVER_CORE_SWAP_ACCEPTED = "swapAccepted";
	
	/* Als de swap van de client niet mogelijk is
	 * Bijvoorbeeld als de tile niet in de inventory is of als er geen tiles meer in de pot zitten
	 * Richting: Server -> Client
	 */
	String SERVER_CORE_SWAP_DENIED = "swapDenied";
	
	/* Als de game is afgelopen stuurt de server dit command.
	 * Daarbij stuurt de server ook meteen de score mee zoals bij het score command
	 * Richting: Server -> Client (ALL)
	 * 
	 * @param name = naam van de speler
	 * @param score = score van de speler
 	 * Dit gaat altijd over meerdere spelers, met spaties tussen elk deel
	 * 
	 * @require name bevat geen spaties
	 * @require score = integer
	 */
	String SERVER_CORE_GAME_ENDED = "gameEnded";
	
	/* Als één van de spelers om één of andere reden verbinding verliest dan kan de server dat aan de clients doorgeven.
	 * Richting: Server -> Client (ALL)
	 * 
	 * @param name = naam van de client die verbinding verloor
	 * 
	 * @require name bevat geen spaties
	 */
	String SERVER_CORE_TIMEOUT_EXCEPTION = "exception timeout";
	
	/* Als één van de spelers om één of andere reden het spel wilt verlaten dan kan de client dit melden.
	 * Richting: Client -> Server
	 */
	String SERVER_CORE_EXIT = "exit";
	
	/* De server zal reageren met een disconnect commando, gevolgd door de naam:
	 *
	 * Richting: Server -> Client (ALL)
	 * 
	 * @param name = naam van de client die verbinding verbrak.
	 * 
	 * @require name bevat geen spaties
	 */
	
	String SERVER_CORE_DISCONNECT = "disconnect";
	
	/* ---------------------------EXTENSION COMMANDS ---------------------------	 */
	
	/* Voor het opvragen van het gehele leaderboard
	 * Richting: Client -> Server
	 */
	String CLIENT_LEADERBOARD_GET_ALL = "leaderboard leaderboard";
	
	/* Het opsturen van het gehele leaderboard na een verzoek van de client
	 * Richting: Server -> Client
	 * 
	 * @param name = naam van de speler
	 * @param gameswon = het aantal games wat deze speler heeft gewonnen
	 * @param gameslost = het aantal games wat deze speler heeft verloren
	 * @param gamesplayed = het aantal games wat deze speler heeft gespeeld
 	 * Dit gaat altijd over de top 5 spelers, met spaties tussen elk deel
	 * 
	 * @require name bevat geen spaties
	 * @require gameswon = integer
	 * @require gameslost = integer
	 * @require gamesplayed = integer
	 */
	String SERVER_LEADERBOARD_SEND_ALL = "leaderboard sendLeaderboard";
	
	/* De client kan ook de score van één speler opvragen.
	 * Richting: Client -> Server
	 * 
	 * @param name = naam van de speler wiens score men wilt zien
	 * 
	 * @require name bevat geen spaties
	 */
	String CLIENT_LEADERBOARD_GET_PERSON_TOTAL = "leaderboard score";
	
	/* Het terugsturen van de score van één speler
	 * Richting: Server -> Client
	 * 
	 * @param name = naam van de speler
	 * @param gameswon = het aantal games wat deze speler heeft gewonnen
	 * @param gameslost = het aantal games wat deze speler heeft verloren
	 * @param gamesplayed = het aantal games wat de ze speler heeft gespeeld
	 * 
	 * @require name bevat geen spaties
	 * @require gameswon = integer
	 * @require gameslost = integer
	 * @require gamesplayed = integer
	 */
	String SERVER_LEADERBOARD_SEND_PERSON_TOTAL = "leaderboard sendScore";
	
	/* Het opvragen van alle games van één speler.
	 * Richting: Client -> Server
	 * 
	 * @param name = naam van de speler
	 * 
	 * @require name bevat geen spaties 
	 */
	String CLIENT_LEADERBOARD_GET_PERSON_HISTORY = "leaderboard games";
	
	/* Het terugsturen van alle resultaten van één speler.
	 * Richting: Server -> Client
	 * 
	 * @param gameName = hoe de game bekend staat bij de server
	 * @param date = datum waarop de game gespeeld is
	 * @param Boolean = het resultaat van deze game voor de speler
	 * True = gewonnen, False = verloren
 	 * Dit kunnen meerdere games zijn, met spaties tussen elk deel
	 * 
	 * @require gameName bevat geen spaties
	 * @require date bevat geen spaties
	 * @require Boolean = boolean
	 */
	String SERVER_LEADERBOARD_SEND_PERSON_HISTORY = "leaderboard sendGames";
	
	
	/* Het versturen van een bericht naar alle spelers
	 * Richting: Client -> Server
	 * 
	 * @param message = bericht wat de client wilt versturen
	 */
	String CLIENT_CHAT_MESSAGE_SEND = "chat sendMessage";
	
	/* Het versturen van een bericht aan één speler
	 * Richting: Client -> Server
	 * 
	 * @param name = naam van de ontvanger
	 * @param message = bericht wat de client wilt versturen
	 * 
	 * @require name bevat geen spaties
	 */
	String CLIENT_CHAT_WHISPER_SEND = "chat whisper";
	
	/* Het doorsturen van een bericht naar een client
	 * Dit wordt gebruikt bij een gewoon bericht, dan wordt dit commando aan alle spelers gericht
	 * Dit wordt gebruikt bij een whisper, dan wordt dit commando aan één speler gericht
	 * Richting: Server -> Client
	 * 
	 * @param name = naam van de speler die het bericht verstuurde
	 * @param message = het bericht van de speler
	 * 
	 * @require name bevat geen spaties
	 */
	String SERVER_CHAT_MESSAGE_SEND = "chat returnMessage";
	
	/* Het blokkeren van een gebruiker.
	 * Dan kan de client geen berichten meer van deze speler ontvangen.
	 * Dan kan de client geen berichten naar deze speler sturen.
	 * Richting: Client -> Server
	 * 
	 * @param name = naam van de speler die geblokkeerd moet worden
	 * 
	 * @require name bevat geen spaties 
	 */
	String CLIENT_CHAT_BLOCK = "chat block";
	
	/* Het deblokkeren van een gebruiker
	 * Richting: Client -> Server
	 * 
	 * @param name = naam van de speler die gedeblokkeerd moet worden
	 * 
	 * @require name bevat geen spaties
	 */
	String CLIENT_CHAT_UNBLOCK = "chat unblock";
	
	
	/* Het uitdagen van een aantal spelers
	 * Richting: Client -> Server
	 * 
	 * @param name = naam van de speler die uitgedaagd wordt
	 * Dit kunnen meerdere namen zijn met spaties tussen elke naam
	 * 
	 * @require name bevat geen spaties
	 */
	String CLIENT_CHALLENGE_NEW = "challenge new";
	
	/* Het doorgeven dat een client is uitgedaagd door een andere client
	 * Richting: Server -> Client
	 * 
	 * @param name = naam van de uitdager
	 * Dit kunnen meerdere namen zijn, met spaties tussen elke naam
	 * 
	 * @require name bevat geen spaties
	 */
	String SERVER_CHALLENGE_NOTIFY = "challenge notify";
	
	/* Het accepteren van een uitdaging
	 * Richting: Client -> Server
	 * 
	 * @param name = naam van de speler die de client heeft uitgedaagd
	 * 
	 * @require name bevat geen spaties
	 */
	String CLIENT_CHALLENGE_ACCEPT = "challenge accept";
	
	/* Het afslaan van een uitdaging
	 * Richting: Client -> Server
	 * 
	 * @param name = naam van de speler die de client heeft uitgedaagd
	 * 
	 * @require name bevat geen spaties
	 */
	String CLIENT_CHALLENGE_DENY = "challenge denied";
	
	/* Als de challenge niet succesvol is
	 * Bijvoorbeeld als één van de namen niet op de server online is
	 * Bijvoorbeeld als één van de speler de uitdaging heeft afgeslagen
	 * Als alle spelers de challenge accepteren wordt gewoon het starting command van de core gegeven.
	 * Richting: Server -> Client
	 * 
	 * @param name = naam van speler die de uitdaging hebben geaccepteerd
	 * Dit kunnen meerdere namen zijn, met spaties tussen elke naam
	 * 
	 * @require name bevat geen spaties
	 */
	String SERVER_CHALLENGE_FAIL = "challenge failed";
	
	/* Het opvragen van alle spelers die uit te dagen zijn
	 * Richting: Client -> Server
	 */
	String CLIENT_CHALLENGE_LOBBY_GET = "challenge lobby";
	
	/* Het terugsturen van alle spelers die uit te dagen zijn
	 * Richting: Server -> Client
	 * 
	 * @param name = naam van een uitdaagbare speler
	 * Dit kunnen meerdere namen zijn, met spaties tussen elke naam
	 * 
	 * @require name bevat geen spaties
	 */
	String SERVER_CHALLENGE_LOBBY_SEND = "challenge returnLobby";
	
	
	/* Het inloggen met gebruik van secure login en een wachtwoord
	 * Richting: Client -> Server
	 * 
	 * @param name = naam waarmee men wilt inloggen
	 * @param password = het wachtwoord wat bij de naam hoort
	 * 
	 * @require name bevat geen spaties
	 * @require password is gehashed met behulp van sha-256
	 */
	String CLIENT_SECURE_LOGIN_LOGIN = "secure login";
	
	/* Het accepteren van een login
	 * Als de login gegevens nog niet bekend zijn bij de server en er is niet al iemand online met die naam
	 * dan worden de login gegevens gewoon toegevoegd aan de login database, zodat er geen apart register command nodig is.
	 * Richting: Server -> Client
	 */
	String SERVER_SECURE_LOGIN_ACCEPT = "secure loginAccepted";
	
	
	/* Als de login niet succesvol is
	 * Bijvoorbeeld als het wachtwoord niet overeenkomt
	 * Richting: Server -> Client 
	 */
	String SERVER_SECURE_LOGIN_DENIED = "secure loginDenied";
	
	/* ---------------------------EXTENSION NAMES ---------------------------	 */
	
	/* Leaderboard
	 */
	String LEADERBOARD_NAME = "leaderboard";
	
	/* Chat
	 */
	String CHAT_NAME = "chat";
	
	/* Challenge
	 */
	String CHALLENGE_NAME = "challenge";
	
	/* Secure Login
	 */
	String SECURE_LOGIN_NAME = "secureLogin";

} 
