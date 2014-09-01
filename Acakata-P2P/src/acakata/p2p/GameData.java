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
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Michael
 */
public class GameData {
    public static ArrayList<Player> playingPlayers = new ArrayList();
    public static volatile boolean gameStart;
    public static volatile int startPlayer;
    public static boolean typedStart;
    public static volatile ArrayList<String> soalList = new ArrayList();
    public static volatile ArrayList<String> jawabanList = new ArrayList();
    public static Player thisPeer;
    public static ArrayList<ArrayList<String>> forDistribution = new ArrayList(new ArrayList());
    
    public static synchronized void addPlayertoList(Player add) {
        playingPlayers.add(add);
    }
    public static synchronized void incStartPlayer() {
        startPlayer++;
    }
    
    public static void processAllSoal() {
        for(int i = 0; i < playingPlayers.size(); i++) {
            addSoalToList(playingPlayers.get(i));
        }
        shuffleSoal();
        forDistribution.add(soalList);
        forDistribution.add(jawabanList);
    }
    
    public static synchronized void addSoalToList(Player data) {
        soalList.add(data.soalAcak);
        jawabanList.add(data.soal);
    }
    
    public static void shuffleSoal() {
        long seed = System.nanoTime();
        Collections.shuffle(soalList, new Random(seed));
        Collections.shuffle(jawabanList, new Random(seed));
    }
    
}
