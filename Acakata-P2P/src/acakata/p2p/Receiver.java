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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class Receiver extends Thread {
    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String Query;
    private String address;
    private Object receivedObject;
    
    public Receiver(Socket socket){
        this.socket = socket;
        System.out.println("Peer connected"+socket.getInetAddress());
        start();
    }
    
    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println();
            System.out.println("New peer connected to peer");
            address = socket.getInetAddress().toString().substring(1);
            System.out.println(address);
            if(!AcakataP2P.connectedClients.contains(address) && !AcakataP2P.connectionQueue.contains(address)) {
                SendManager.connectTo(address);
            } else if (!AcakataP2P.connectedClients.contains(address) && AcakataP2P.connectionQueue.contains(address)) {
                SendManager.connectTo(address);
                AcakataP2P.removeQueue(address);
            }
            //to do something with the address
            while(true) {
                Query = (String) Receive();
                receivedObject = Receive();
                switch (Query) {
                    /* Do something here */
                    case "START":
                        GameData.addPlayertoList(((Player)receivedObject));
                        System.out.println(((Player)receivedObject).getUserName()+" wanted to start the game, type start to start playing");
                        GameData.incStartPlayer();
                        break;
                    case "SOAL":
                        GameData.forDistribution = (ArrayList) receivedObject;
                        GameData.soalList = GameData.forDistribution.get(0);
                        GameData.jawabanList = GameData.forDistribution.get(1);
                        System.out.println("SOAL RECEIVED");
                        break;
                    case "TEST":
                        System.out.println(receivedObject);
                        break;
                    default:
                    System.out.println(Query);
                    break;
                }
            }
        } catch (Throwable t) {
            System.out.println("Peer disconnected");
            System.out.println(address);
            try {
                socket.close();
            } catch (IOException ex) {
                System.err.println(ex);
            }
            for (int i = 0; i < SendManager.SendList.size(); i++) {
                if (SendManager.SendList.get(i).getSendAddress().equals(address)) {
                    SendManager.SendList.get(i).Send("X", "X");
                    AcakataP2P.removeConnectedClients(address);
                    //this should throw exception and sender closes itself
                    break;
                }
            }
        }        
    }
    
    private Object Receive() throws IOException {
        try {
            return in.readObject();
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }
    
}
