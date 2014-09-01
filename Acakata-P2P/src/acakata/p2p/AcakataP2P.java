/*
 * Copyright (C) 2014 Michael
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package acakata.p2p;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class AcakataP2P {
    private Scanner userInput;
    private TrackerConn trackerConnection;
    public static volatile ArrayList<String> connectedClients;
    public static volatile ArrayList<String> connectionQueue;
    private Thread peerCon;
    private Thread gameStartMonitor;
    private GameModeThread disp;
    public static volatile boolean answered;
    public static volatile boolean gameSelesai;

    
    private AcakataP2P() {
        userInput = new Scanner(System.in);
        connectedClients = new ArrayList<>();
        connectionQueue = new ArrayList<>();
        GameData.gameStart = false;
        GameData.typedStart = false;
        GameData.startPlayer = 0;
        answered = false;
        gameSelesai = false;
        
        /* Thread construction */
        peerCon = new Thread(new Runnable() {
           @Override
           public void run() {
               while(!GameData.gameStart) {
                    try {
                        SendManager.connectToUnconnected();
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
               }
           }
        });
        
        gameStartMonitor = new Thread(new Runnable() {
            @Override
            public void run () {
                boolean stop = false;
                while(!stop) {
                    if((GameData.startPlayer == (AcakataP2P.connectedClients.size()+1)) && (GameData.startPlayer != 1)) {
                        if(GameData.playingPlayers.get(0).equals(GameData.thisPeer)) {
                            GameData.processAllSoal();
                            SendManager.sendToAll("SOAL", GameData.forDistribution);
                        }
                        GameData.gameStart = true;
                        stop = true;
                    }
                }
            }
        });
        
        
        
        System.out.println("Masukkan screen name:");
        GameData.thisPeer = new Player(userInput.nextLine());
        System.out.println("Masukkan soal anda:");
        GameData.thisPeer.soal = userInput.nextLine();
        GameData.thisPeer.shuffle();
        System.out.println("Soal anda setelah diacak: "+GameData.thisPeer.soalAcak);
        
        disp = new GameModeThread(GameData.thisPeer);
        
        //connect to tracker
        trackerConnection = new TrackerConn();
        trackerConnection.start();
        //establish serversocket
        RecvManager.startManager();
        //open socket connection
        
    }
    
    private void PrepMode() {
        peerCon.start();
        gameStartMonitor.start();
        while(!GameData.gameStart) {
            if(!GameData.typedStart) {
                System.out.println("Type in 'start' to start the game");
                String input = userInput.nextLine();
                if(input.equalsIgnoreCase("start")) {
                    GameData.incStartPlayer();
                    GameData.addPlayertoList(GameData.thisPeer);
                    SendManager.sendToAll("START", GameData.thisPeer);
                    GameData.typedStart = true;
                }
            } else {
                System.out.println("Waiting other player to type start");
                try {
                    gameStartMonitor.join();
                } catch (InterruptedException ex) {
                }
            }
        }
        GameMode();
    }
    
    private void GameMode() {
        System.out.println("You're in game mode!");
        String input;
        disp.start();
        while(!gameSelesai) {
            if(!GameData.soalList.isEmpty()) {
                if(!GameData.thisPeer.soalAcak.equals(GameData.soalList.get(0))) {
                    if(!answered) {
                        System.out.print("Answer: ");
                        input = userInput.nextLine();
                        if(isAnswerCorrect(input)) {
                            answered = true;
                            System.out.println("JAWABAN ANDA BENAR!");
                            GameData.thisPeer.incScore();
                            SendManager.sendToAll("UPDATEPLAYER", GameData.thisPeer);
                        }
                    }
                }
            }
        }
    }
    
    public static void main(String[] args) {
        AcakataP2P Game = new AcakataP2P();
        Game.PrepMode();
    }
    
    public static synchronized void addConnectedClients(String add) {
        connectedClients.add(add);
    }
    public static synchronized void removeConnectedClients(String rem) {
        connectedClients.remove(rem);
    }
    public static synchronized void addQueue(String add) {
        connectionQueue.add(add);
    }
    public static synchronized void removeQueue(String rem) {
        connectionQueue.remove(rem);
    }
    
    private boolean isAnswerCorrect(String input) {
        return GameData.jawabanList.get(0).equals(input);
    }
    
}
