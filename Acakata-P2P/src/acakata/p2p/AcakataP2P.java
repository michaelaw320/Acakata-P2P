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

/**
 *
 * @author Michael
 */
public class AcakataP2P {
    private Scanner userInput;
    private TrackerConn trackerConnection;
    /**
     * @param args the command line arguments
     */
    
    private AcakataP2P() {
        userInput = new Scanner(System.in);
        
        System.out.println("Masukkan screen name:");
        GameData.thisPeer = new Player(userInput.nextLine());
        System.out.println("Masukkan soal anda:");
        GameData.soal = userInput.nextLine();
        GameData.shuffle();
        
        //connect to tracker
        trackerConnection = new TrackerConn();
        trackerConnection.start();
        //establish serversocket
        RecvManager.startManager();
        //open socket connection
        
    }
    
    private void GameMode() {
        String input = userInput.nextLine();
        while(!input.equalsIgnoreCase("exit")) {
            System.out.println(input);
            input = userInput.nextLine();
        }
        
    }
    
    public static void main(String[] args) {
        AcakataP2P Game = new AcakataP2P();
        // TODO code application logic here
        Game.GameMode();
    }
    
}
