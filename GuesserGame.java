import java.util.Scanner;

		class Master {
			int key;
			int noOfPlayers = 2;
			int noOfChances = 3;
			String username = "master@door.com";
			String password = "master123";
			boolean flag;
			
			boolean authentication() {
				
				Scanner sc=new Scanner(System.in);
				boolean usernameFlag = false;
				boolean passwordFlag = false;
				
				System.out.println("Hello master!! before entering the key to the door, please verify that its you.");
				
				System.out.print("Master, please enter your username: ");
				while(!usernameFlag) {
					String userEnteredUsername = sc.next();
					if(userEnteredUsername.toLowerCase().equals(username.toLowerCase())) usernameFlag = true;
					else System.err.print("Wrong Username, TRY AGAIN: ");
				}
				
				System.out.print("Master, please enter your password: ");
				while(!passwordFlag) {
					String userEnteredPassword = sc.next();
					if(userEnteredPassword.equals(password)) passwordFlag = true;
					else System.err.print("Wrong Password, TRY AGAIN: ");
				}
				
				if(usernameFlag && passwordFlag) return true;
				else return false;
			}
			
			int getKey(){
				
				int masterKey = 0;
				Scanner sc=new Scanner(System.in);
				
				if(authentication()) {
					System.out.print("Greetings master. please enter the key to the door (0-10): ");
					
					while(!flag) {
						masterKey = sc.nextInt();
						if(masterKey <= 10 && masterKey >= 0) flag = true;
						else System.err.print("Master you are breaking your own rules Number must be b/w (0-10): ");
					}
					
					key = masterKey;
				}
				return key;
			}
		}

		class Player {
			
			int key;
			boolean flag;
			Messages message = new Messages();
			
			int getKey(){
				
				int playerKey = 0;
				Scanner sc=new Scanner(System.in);
				
				while(!flag) {
					playerKey = sc.nextInt();
					if(playerKey <= 10 && playerKey >= 0) flag = true;
					else message.rangeError();;
				}
				
				key = playerKey;
				flag = false;
				return key;
			}
		}

		class Umpire {
			
			int masterKey;
			int[][] playerKeys;
			int[] playerScores;
			int[] goldWon;
			int playersWon;
			Master master = new Master();
			Player player = new Player();
			Door door = new Door();
			Messages message = new Messages();
			
			//getting key from the master
			int getMasterKey(){
				
				Master master = new Master();
				masterKey = master.getKey();
				return masterKey;
			}
			
			//getting key from the players
			int[][] getPlayerKeys(){
				
				playerKeys = new int[master.noOfPlayers][master.noOfChances];
				
				for(int i=0; i< playerKeys.length; i++) {
					
					boolean flag = false;
					message.newPlayerMessage(i+1, master.noOfPlayers);
					
					for(int j=0; j< playerKeys[i].length; j++) {
						message.newChanceMessage(i+1, playerKeys[i].length, j+1);
						int key = player.getKey();
						boolean compare = checkKey(key);
						if(compare) {
							flag = true;
							door.isDoorOpen = true;	
							playerKeys[i][j] = 1;
							message.congratulationsMessage(j+1, master.noOfChances);
							break;
						}
					}
					if(!flag) message.lostMessage();
				}
				return playerKeys;
			}
			
			//checking key
			boolean checkKey(int playerKey) {
				
				if(masterKey == playerKey) return true;
				else return false;
			}
			
			//getting scores of the players
			int[] getScores(int[][] arr) {
				Master master = new Master();
				playerScores= new int[arr.length];
				for(int i=0; i<arr.length; i++) {
					for(int j=0; j< arr[i].length; j++) {
						if(arr[i][j] == 1) {
							playersWon++;
							playerScores[i] = ((master.noOfChances)-j);
						}
					}
				}
				return playerScores;
			}
			
			//splitting the gold based on scores
			int[] splitGold() {
				goldWon = new int[master.noOfPlayers];
				if(door.isDoorOpen) {
					float scoreSum =0;
					
					for(int i: playerScores) scoreSum += i;
					if(scoreSum >= 1) {
						for(int i=0; i<goldWon.length; i++) {
							float per = (playerScores[i]/scoreSum)*100;
							goldWon[i] = (int) ((per/100) * door.noOfGold);
						}
					}	
				}
				return goldWon;
			}
		}

		class Door {
			
			int noOfGold = 20;
			int key;
			boolean isDoorOpen;
		}

		class Messages {
			void rangeError() {
				
				System.err.print("--------\"Key must be b/w 0-10\" TRY AGAIN: ");
			}
			
			void newChanceMessage(int playerNum, int noOfChances, int chanceNo) {
				
				System.out.print("Attempt "+chanceNo+"/"+noOfChances+": "); 
			}
			
			void newPlayerMessage(int playerNum, int noOfPlayers) {
				
				System.out.println();
				System.out.println("------------------------");
				System.out.println();
				System.out.println("\"PLAYER "+playerNum+"/"+noOfPlayers+"\" Its your time to guess the key, Good Luck!");
				System.out.println();
			}
			
			void congratulationsMessage(int chanceNum, int numOfChances) {
				
				String happy = "😃";
				System.out.println();
				
				if(chanceNum == numOfChances) 
					System.out.println(happy+" Uhhhh!! Its so close, Congratulations nonetheless you have found the key");
				
				else if(chanceNum == 1)
					System.out.println(happy+" What! you must be some kind of mind reader to guess it on the 1st attempt lets see if there are more mind readers");
				
				else System.out.println(happy+ " Congratulations you have found the key in Attempt " + chanceNum);
			}
			
			void lostMessage() {
				
				System.out.println();
				System.err.println("You have lost the game. Better luck next time");
			}
			
			void leaderBoard(int[] score, int[] goldWon) {
				
				System.out.println();
				System.out.println("-------------------------");
				System.out.println();
				System.out.println("LeaderBoard");
				System.out.println();
				System.out.println("|    Player    Score    Gold Won    |");
				System.out.println("-------------------------------------");
				
				for(int i=0; i< goldWon.length; i++) {
					System.out.println("     "+(i+1)+"         "+score[i]+"        "+goldWon[i]+"           ");
				}
				
			}
			
			void playersWon(int playersWon, int noOfPlayers){
				
				System.out.println();
				System.out.println("------------------------");
				System.out.println();
				
				if(playersWon == 1)
					System.out.println("Only 1 Player has found the key to the door");
				
				else if(playersWon == noOfPlayers)
					System.out.println("wow all the Players have found the key \"EXCELLENT\"");
				
				else System.out.println("I am sorry. No one found the key");
			}
		}

		public class GuesserGame {

			public static void main(String[] args) {
				
				Umpire umpire = new Umpire();
				Messages message = new Messages();
				Master master = new Master();
				
				umpire.getMasterKey();
				
				System.out.println();
				System.out.println("Game Starts Now!!!!!!");
				
				umpire.getPlayerKeys();
				
				int[] scores = umpire.getScores(umpire.playerKeys);
				int[] gold = umpire.splitGold();
				
				message.playersWon(umpire.playersWon, master.noOfPlayers);
				message.leaderBoard(scores, gold);
			}
		}

