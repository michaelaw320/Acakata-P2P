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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class SendManager {
    public static ArrayList<Sender> SendList = new ArrayList<>();
    
    public static synchronized void addSenderToList(Sender addMe) {
        SendList.add(addMe);
    }
    public static synchronized void removeSenderFromList(Sender remMe) {
        SendList.remove(remMe); 
    }
    
    public static void connectTo(String address) {
        Sender conn = new Sender(address,55555);
        addSenderToList(conn);
    }
    
    public static void connectToUnconnected() {
        new Thread (new Runnable () {
                @Override
                public void run() {
                    for (int i = 0; i < AcakataP2P.connectionQueue.size(); i++) {
                        String currentAddr = AcakataP2P.connectionQueue.get(i);
                        System.out.println("CONNECTING TO : "+currentAddr);
                        connectTo(currentAddr);
                        AcakataP2P.addConnectedClients(currentAddr);
                        AcakataP2P.removeQueue(currentAddr);
                    }
                }
            }
            ).start();
    }
    
    public static void sendToAll(String Query, Object toSend) {
        new Thread (new Runnable () {
                @Override
                public void run() {
                   for (Sender currentSender : SendList) {
                       currentSender.Send(Query, toSend);
                   }
                }
        }
        ).start();
    }
}
