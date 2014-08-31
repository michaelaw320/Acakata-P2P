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
import java.net.ServerSocket;

/**
 *
 * @author Michael
 */
public class RecvManager {

    public static void startManager() {
        new Thread(new Runnable() {
            @Override
                public void run() {
                try {
                    ServerSocket ss = new ServerSocket(55555);
                    while(true) {
                        new Receiver(ss.accept());
                    }
                } catch (IOException ex) {
                    System.err.println("Cannot bind to port 55555, exiting");
                    System.exit(1);
                }
            }
        }
        ).start();
    }
}
