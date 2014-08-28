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
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class AcakataTracker {
    
    public static int PORT;
    
    public static ArrayList<String> connectedPeers;
    
    public AcakataTracker(int port) throws IOException {
        ServerSocket ss = new ServerSocket(port);
        System.out.println("TRACKER STARTED");
        while (true) {
            new TrackerThread(ss.accept());
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            PORT = 6969;
            connectedPeers = new ArrayList<>();
            System.out.println("STARTING SERVER");
            new AcakataTracker(PORT);
        } catch (IOException ex) {
            System.err.println("CANNOT START SERVER");
            System.exit(1);
        }
    }
    
}
