package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

import gui.ServerFrame;

/**
 * 
 * @author Julian Hultgren, Lukas Persson, Erik Johansson, Simon Börjesson
 * Version 2.0
 */

public class GameServer implements Runnable{

	private ServerSocket serverSocket;
	private Thread serverThread = new Thread(this);
	private HashMap<String, ClientHandler> clientMap = new HashMap<>();
	private HashMap<Integer, String> clientMapid = new HashMap<>();
	private HashMap<String, client.Character> characterMap = new HashMap<>();
	private Random rad = new Random();
	private ServerFrame ui;
	
	private boolean svulloAvailable = true;
	private boolean tjoPangAvailable = true;
	private boolean theRatAvailable = true;
	private boolean hannibalAvailable = true;
	private boolean markisenAvailable = true;
	private boolean hookAvailable = true;
	
	private int counter = 1; //Whose turn it is
	private int id = 1; // OF WHO
	private int treasurePos = 0;
	
	/**
	 * Constructor starts up the server
	 * 
	 * @param 	{@Link int} port
	 * @param	{@Link ServerFrame}	ui
	 */
	
	public GameServer(int port, ServerFrame ui){
		this.ui = ui;
		try{
			serverSocket = new ServerSocket(port);
			serverThread.start();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts the server thread
	 */
	
	public void run() {
		System.out.println("Server running...");
		System.out.println("Server: Listening for clients...");
		while(true){
			try{
				Socket socket = serverSocket.accept();
				if (clientMap.size() <= 6){
					System.out.println("Server: Client connected from..." +socket.getRemoteSocketAddress());
					new ClientHandler(socket).start();
				}else{
					System.out.println("Server: Client can't connected from..." +socket.getRemoteSocketAddress() + " due to game being full");
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Starts the game
	 */
	
	public void startGame(){

		//Stop accepting connections to gameserver.
		serverThread.suspend();

		System.out.println("Server: starta spelet på servern " + id);
		System.out.println("Server: Spelet startat för spelarna " + clientMap.values().stream().map(c -> c.username).collect(Collectors.joining(", ")));

		for(int i = 1; i < id; i++){
			clientMap.get(clientMapid.get(i)).createCharacter(clientMapid.get(i));
			clientMap.get(clientMapid.get(i)).startCountdown();
		}
		clientMap.get(clientMapid.get(1)).clientsTurn(true);
	}
	
	/**
	 * Inner class that handles the clients
	 */
	
	
	private class ClientHandler extends Thread{
		private Socket socket;
		public ObjectInputStream input;
		public ObjectOutputStream output;
		private String sInput; //?
		private String username;
		private String character; //Name of character?
		private int nbrOfPlayers;
		private int playerid; //Id of player
		private CountDown cd = new CountDown(this);
		
		/**
		 * Constructor starts up a output stream and a input stream
		 * 
		 * @param {@Link Socket} socket
		 */
		
		public ClientHandler(Socket socket) {
			this.socket = socket;
			try {
				output = new ObjectOutputStream(socket.getOutputStream());
				input = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Starts the "ClientHandeler" thread
		 */
		
		public void run() {

			while(socket.isConnected()){
				try{
					Object object = input.readObject(); //Change to take from queue
					if(object instanceof String){
						sInput = (String)object;

						//TODO: Switch-case

						if(sInput.equals("ENDTURN")){
							//Handles turn order. 
							cd.suspend(); //Puts CD thread on hold
							clientsTurn(true);
						}else if(sInput.equals("set character")){
							
							//Sets a character as already picked and cannot be chosen by others.
							
							character = (String)input.readObject();
							
							if(character.equals("Svullo")){
								svulloAvailable = false;
							}
							if(character.equals("TjoPang")){
								tjoPangAvailable = false;
							}
							if(character.equals("TheRat")){
								theRatAvailable = false;
							}
							if(character.equals("Hannibal")){
								hannibalAvailable = false;
							}
							if(character.equals("Markisen")){
								markisenAvailable = false;
							}
							if(character.equals("Hook")){
								hookAvailable = false;
							}
							for (ClientHandler ch : clientMap.values()) {//Notifies players about what character have been picked
								ch.output.writeObject("updateUserInfo");
								ch.output.writeBoolean(svulloAvailable);
								ch.output.writeBoolean(tjoPangAvailable);
								ch.output.writeBoolean(theRatAvailable);
								ch.output.writeBoolean(markisenAvailable);
								ch.output.writeBoolean(hannibalAvailable);
								ch.output.writeBoolean(hookAvailable);
								ch.output.flush();
								
							}
							
							
						}else if(sInput.equals("show treasure")){ //When player has found treasure, give its position to that player
							int player; //Player that has found the treasure
							if(counter == 1){
								player = clientMap.size();
							}else{
								player = counter -1;
							}
							if(treasurePos == 0){ //Generate position of treasure when it is first found. 
								Random rand = new Random();
								treasurePos = rand.nextInt(9)+1;
							}
							for (ClientHandler ch : clientMap.values()){
								ch.output.writeObject("treasure position");
								ch.output.writeObject(treasurePos);
								if(ch == clientMap.get(clientMapid.get(player))){
									ch.output.writeBoolean(true);
								} else{
									ch.output.writeBoolean(false);
								}
								ch.output.flush();
							}
						}else if(sInput.equals("playershot")){
							for (ClientHandler ch : clientMap.values()) {
								ch.output.writeObject("playershot");
								ch.output.flush();
							}
						}

						else if(sInput.equals("pieces stolen")){
							client.Character tempChar = characterMap.get((String)input.readObject()); //Person whose pieces have been stolen
							tempChar.setPieces(0); //Player now has 0 pieces.
							for (ClientHandler ch : clientMap.values()){ //Tell all other clients a persons pieces have been stolen.
								ch.output.writeObject("steal pieces");
								ch.output.writeObject(tempChar.getName());
								ch.output.flush();
							}
						}else if(sInput.equals("victory")){ 
							String winner = (String) input.readObject();
							for (ClientHandler ch : clientMap.values()) {
								ch.output.writeObject("winner");
								ch.output.writeObject(winner);
								//TODO: LOCK PLAYER BUTTONS
								ch.output.flush();
							}
				
						}else {
						
							//Only having else seems dangerous. 

							//Accepts a new client to the game server. 
							System.out.println("Server: Mottagit username");
							clientMap.put(sInput, this);
							clientMapid.put(id, sInput);
							this.username = sInput;
							playerid = id;
							id++;
							ui.addUser(sInput);
							
							
							for (ClientHandler ch : clientMap.values()) {
																
								for (int i = 1; i < clientMapid.size() + 1; i++) {
							
									System.out.println("Server: uppdaterar connectedUsers med: " + clientMapid.get(i));
									
									ch.output.writeObject(clientMapid.get(i));
									
								}
								ch.output.writeObject(null);
								ch.output.flush();
								
							}
							
							//Shows the newly connected user which characters can be chosen.
							this.output.writeObject("Choose character");
							this.output.writeBoolean(svulloAvailable);
							this.output.writeBoolean(tjoPangAvailable);
							this.output.writeBoolean(theRatAvailable);
							this.output.writeBoolean(markisenAvailable);
							this.output.writeBoolean(hannibalAvailable);
							this.output.writeBoolean(hookAvailable);
							this.output.flush();
							
						}

					}
					if(object instanceof client.Character) {
						//Synchronizing of characters. 
						client.Character character = (client.Character) object;
						updateCharPos(character);


					}
					
				}catch (IOException | ClassNotFoundException e) {
					closeSocket();
					Thread.currentThread().stop();
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * Start the"Countdown" thread
		 */
		
		public void startCountdown(){
			cd.start();
		}
		
		/**
		 * Inner class that will send out a way to end your turn if you are stuck after 60 seconds
		 *
		 */
		
		private class CountDown extends Thread{
			private int counter;
			private ClientHandler clienthandler;
			
			/**
			 * Constructor
			 * 
			 * @param	{@Link ClientHandler}	ch
			 */
			
			public CountDown(ClientHandler ch){
				clienthandler = ch;
				counter = 60; //Less time maybe?
			}
			
			/**
			 * Start the"Countdown" thread
			 */
			
			public void run(){
				while(!Thread.interrupted()){
					try {
						this.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					counter--;
					if(counter == 0){
						clienthandler.timeout();
					}
				}
			}
			
			/**
			 * Resets the timer (to 60 seconds)
			 */
			
			public void reset(){
				counter = 60;
			}
		}
		
		/**
		 * Sends a out a message to the client that it has timed out
		 */
		
		public void timeout(){
			System.out.println("Srever: Time out");
			try {
				output.writeObject("time out");
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Method to enable the buttons for the next player
		 * goes through the number of players and when it reaches
		 * the number of players it goes back down to 1
		 * 
		 * @param	{@Link boolean}		enableButton
		 */
		
		public void clientsTurn(boolean enableButtons){
			nbrOfPlayers = clientMap.size();
			System.out.println("Server: sleeping variabeln: " + characterMap.get(clientMapid.get(counter)).sleeping());
			while(characterMap.get(clientMapid.get(counter)).sleeping() != 0){ //While player is shot.
				ClientHandler ch = clientMap.get(clientMapid.get(counter)); //Get clientHandler of player whose turn it is
				client.Character charr = characterMap.get(clientMapid.get(counter)); //Get their character object
				charr.passATurn(); //Reduce counter for after being shot cooldown.
				try {
					System.out.println("SERVER: Värde på counter i clientsTurn: " + counter);
					System.out.println("SERVER: Character-objekt: " + charr);
					ch.output.writeObject(charr); //Sync server character object with clients character object. 
					ch.output.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (characterMap.get(clientMapid.get(counter)).sleeping() == 0){
					for(int i = 1; i <= nbrOfPlayers; i++){
						String username = clientMapid.get(i); //Get username
						ch = clientMap.get(username); //Get ClientHandler from a username
						try {
							System.out.println("SERVER: sleeping variabel: " + characterMap.get(clientMapid.get(counter)).sleeping() + " användare: " + username);
							ch.output.writeObject(characterMap.get(username).getRow()); 
							ch.output.writeObject(characterMap.get(username).getCol());
							ch.output.writeObject(charr);
							ch.output.flush();
							
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				if (counter == nbrOfPlayers){
					counter = 1;
				}
				else{
					counter++;
				}
			} 
			
			String username = clientMapid.get(counter);
			ClientHandler ch = clientMap.get(username);
			try {
				ch.output.writeObject("Enable buttons"); //????? why is this on the server???
				ch.output.writeBoolean(enableButtons);
				ch.output.flush();
				ch.cd.resume();
				ch.cd.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (counter == nbrOfPlayers) {
				counter = 1;
			} else {
				counter++;
			}

		}
		
		/**
		 * Creates a character and places it on a empty starting position
		 * 
		 * @param	{@Link String}	name
		 */

		public synchronized void createCharacter(String name) {
			System.out.println("createCaracter");
			client.Character myCharacter = new client.Character(name, -1, -1);
			while (myCharacter.getRow() == -1) {
				System.out.println(myCharacter.getRow());
				int startPos = rad.nextInt(6);
				System.out.println("-------------------------------\n" + startPos);
				switch (startPos) {
				case 0:
					if (!checkCharacterAtPos(9, 4)) {
						myCharacter.setPos(9, 4);
					}
					break;
				case 1:
					if (!checkCharacterAtPos(6, 22)) {
						myCharacter.setPos(6, 22);
					}
					break;
				case 2:
					if (!checkCharacterAtPos(9, 35)){
						myCharacter.setPos(9, 35);
						}
					break;
				case 3:
					if(!checkCharacterAtPos(23, 4)){
						myCharacter.setPos(23, 4);
						}
					break;
				case 4:
					if(!checkCharacterAtPos(30, 44)){
						myCharacter.setPos(30, 44);
						}
					break;
				case 5:
					if(!checkCharacterAtPos(36, 37)){
						myCharacter.setPos(36, 37);
						}
					break;
				}
			}
			System.out.println("Server: Karaktär skapad namn: " + name + " Row: " + myCharacter.getRow() + " Col: " + myCharacter.getCol());
			characterMap.put(name, myCharacter);
			myCharacter.setCharacter(character);
			updateCharPos(myCharacter);
		}
		
		/**
		 * Checks if there is a character at the given position, returns true if
		 * there is and false if there is not
		 * 
		 * @param row	int	row
		 * @param col	int	col
		 * @return 		boolean
		 */

		public boolean checkCharacterAtPos(int row, int col) {
			String userID;
			
			if(characterMap.isEmpty()){
				return false;
			}
			
			for (int i = 1; i <= characterMap.size(); i++) {
				System.out.println(i);
				if(clientMapid.containsKey(i)){
					userID = clientMapid.get(i);
					if (characterMap.get(userID).getRow() == row && characterMap.get(userID).getCol() == col) {
						return true;
					}
				}
			}
			System.out.println("Tilldelad plats");
			return false;
		}
		
		/**
		 * Send out a character that has been updated to all clients
		 * 
		 * @param @{Link Character} charr
		 */
		
		public void updateCharPos(client.Character charr) {
			ArrayList<String> connectedUsers = new ArrayList<String>();
			connectedUsers.addAll(clientMap.keySet());
			characterMap.put(charr.getName(), charr);
			for(ClientHandler ch : clientMap.values()){
				try{
					ch.output.writeObject(charr);
					ch.output.flush();
					System.out.println("Server: character uppdaterad");
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * Closes the soket
		 */

		public void closeSocket() {
			try {
				socket.close();
				input.close();
				characterMap.remove(sInput);
				clientMapid.remove(playerid);
				clientMap.remove(sInput);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
