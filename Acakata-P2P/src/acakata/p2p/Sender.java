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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class Sender{
    private Socket connection;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final String peerAddress;
    private final int peerPort;
    
    public Sender(String peerAddress, int peerPort) {
        this.peerAddress = peerAddress;
        this.peerPort = peerPort;
        this.connection = null;
        try {
            connect();
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void connect() throws IOException {
        connection = new Socket(peerAddress,peerPort);
        connection.setSoTimeout(15000);
        out = new ObjectOutputStream(connection.getOutputStream());
        in = new ObjectInputStream(connection.getInputStream());
        System.out.println("CONNECTED TO: "+peerAddress);
    }
    
    public void Send(String Query, Object toSend) {
        try {
            out.writeObject(Query);
            out.writeObject(toSend);
            out.flush();
            out.reset();
        } catch (IOException ex) {
            SendManager.removeSenderFromList(this);
            try {
                connection.close();
            } catch (IOException ex1) {
                //nothing
            }
        } 
    }
    
    public String getSendAddress() {
        return peerAddress;
    }
    
}
