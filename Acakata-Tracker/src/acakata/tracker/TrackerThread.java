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

package acakata.tracker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class TrackerThread extends Thread {
    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String Query;
    private String address;
    private ArrayList<String> toSend;
    
    public TrackerThread(Socket socket) {
        this.socket = socket;
        toSend = new ArrayList<>();
        start();
    }
    
    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println();
            System.out.println("New peer connected to tracker");
            address = socket.getInetAddress().toString().substring(1);
            System.out.println(address);
            AcakataTracker.connectedPeers.add(address);
            while(true) {
                Query = Receive();
                switch (Query) {
                    case "UPDATE":
                        SendRoom();
                        break;
                    case "EXIT":
                        throw new Exception();
                }
            }
        } catch (Throwable t) {
            System.out.println("Peer disconnected");
            System.out.println(address);
            for (int i = 0; i < AcakataTracker.connectedPeers.size(); i++) {
                if (AcakataTracker.connectedPeers.get(i).equals(address)) {
                    AcakataTracker.connectedPeers.remove(i);
                    break;
                }
            }
            try {
                socket.close();
            } catch (IOException ex) {

            }
        }
    }
    
    private void Send(ArrayList connectedPeers) throws IOException {
        out.writeObject(connectedPeers);
        out.flush();
        out.reset();
    }
    
    private String Receive() throws IOException {
        try {
            return (String) in.readObject();
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }
    
    private void SendRoom() throws IOException {
        toSend.clear();
        for (int i = 0; i < AcakataTracker.connectedPeers.size(); i++) {
            if (!AcakataTracker.connectedPeers.get(i).equals(address)) {
                toSend.add(AcakataTracker.connectedPeers.get(i));
            }
        }
        Send(toSend);
    }
}
