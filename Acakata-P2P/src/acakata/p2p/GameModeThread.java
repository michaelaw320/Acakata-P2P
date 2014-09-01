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

import java.util.Timer;

/**
 *
 * @author Michael
 */
public class GameModeThread extends Thread {
    private String currentSoal;
    private Player thisPlayer;
    
    public GameModeThread(Player thisPlayer) {
        this.thisPlayer = thisPlayer;
    }
    
    @Override
    public void run() {
        //1. keluarkan soal
        //2. tunggu 10 detik
        //3. delet soal dan jawaban, keluarkan soal baru, repeat 2
        while(GameData.soalList.isEmpty()) {
            //wait
        }
        while(!GameData.soalList.isEmpty()) {
            currentSoal = GameData.soalList.get(0);
            System.out.println("Soal: "+currentSoal);
            if(!thisPlayer.soalAcak.equals(currentSoal)) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                }
                GameData.soalList.remove(0);
                GameData.jawabanList.remove(0);
                AcakataP2P.answered = false;
            } else {
                AcakataP2P.answered = true;
                System.out.println("Harap menunggu, soal anda sedang tampil");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                }
                GameData.soalList.remove(0);
                GameData.jawabanList.remove(0);
                AcakataP2P.answered = false;
            }
        }
        System.out.println("PERMAINAN SELESAI");
        AcakataP2P.gameSelesai = true;
        for (int i = 0; i < GameData.playingPlayers.size();i++) {
            GameData.playingPlayers.get(i).updatePlayer();
        }
        System.exit(0);
    }
}
