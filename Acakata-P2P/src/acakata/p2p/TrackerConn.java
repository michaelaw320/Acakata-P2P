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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class TrackerConn extends Thread {
    public static Socket connection = null;
    public static ObjectOutputStream out;
    public static ObjectInputStream in;
    public static String trackerAddress;
    public static int trackerPort;
    
    public TrackerConn() {
        
    }
    
    public static void connect() throws IOException {
        /* Read trakcer configuration */
        
        try {
            File tracker = new File ("tracker.txt");
            Scanner sc = new Scanner(tracker);
            trackerAddress = sc.nextLine();
        } catch (FileNotFoundException ex) {
            System.err.println("Can't find tracker.txt, using default value of localhost");
            trackerAddress = "localhost";
        } finally {
            trackerPort = 6969;
            connection = new Socket(trackerAddress,trackerPort);
            connection.setSoTimeout(15000);
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
        }
    }
    public static void Send(String toSend) {
        try {
            out.writeObject(toSend);
            out.flush();
            out.reset();
        } catch (IOException ex) {
            System.out.println("Tracker disconnected, can't send anything to tracker.");
        }
    }
    public static ArrayList ReceiveRoomlist() {
        try {
            return (ArrayList) in.readObject();
        } catch (ClassNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            System.out.println("Tracker disconnected, can't receive anything from tracker.");
            return null;
        }
    }
    
    
    @Override
    public void run() {
        try {
            connect();
        } catch (IOException ex) {
            System.out.println("Cannot establish connection to tracker");
        }
        while (true) {
            //wait
        }
    }
}