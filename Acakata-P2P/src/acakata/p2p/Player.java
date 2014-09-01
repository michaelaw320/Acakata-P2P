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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Michael
 */
public class Player implements Serializable {
    public String soalAcak;
    public String soal;
    private final String userName;
    private int score;
    
    private int idx = -999; /*for update purpose only*/
    
    public Player(String playerName) {
        userName = playerName;
        queryPlayer();
        System.out.println("PLAYER DATA:");
        System.out.println("Name: " + userName);
        System.out.println("Score: " + score);
        System.out.println();
    }
    
    public String getUserName() {
        return userName;
    }
    
    public int getPlayerScore() {
        return score;
    }
    
    public void incScore() {
        score++;
    }
    
    private synchronized void queryPlayer() {
        File highscore = new File("highscore.txt");
        boolean found = false;
        try {
            Scanner sc = new Scanner(highscore);
            String[] processing;

            while (sc.hasNextLine() && !found) {
                processing = sc.nextLine().split(";");
                if (processing[0].equals(userName)) {
                    score = Integer.parseInt(processing[1]);
                    found = true;
                }
            }

        } catch (FileNotFoundException ex) {
            System.err.println("Highscore.txt not found");
            try {
                highscore.createNewFile();
            } catch (IOException ex1) {
                
            }
        }
    }
    
    public synchronized void updatePlayer() {
        if (idx == -999) {
            try {
                File highscore = new File("highscore.txt");
                FileWriter fw = new FileWriter(highscore,true);
                BufferedWriter writer = new BufferedWriter(fw);
                String add = userName + ";" + Integer.toString(score);
                writer.write(add);
                writer.newLine();
                writer.close();
            } catch (IOException ex) {
                    System.err.println(ex);
                }
        } else {
            if (isDifferent()) {
                try {
                    ArrayList<String> Data = new ArrayList<>();
                    String replacement = userName + ";" + Integer.toString(score);

                    File highscore = new File("highscore.txt");
                    Scanner sc = new Scanner(highscore);

                    while (sc.hasNextLine()) {
                        Data.add(sc.nextLine());
                    }

                    Data.remove(idx);
                    Data.add(replacement);

                    FileWriter fw = new FileWriter(highscore);
                    BufferedWriter writer = new BufferedWriter(fw);

                    for (String Data1 : Data) {
                        writer.write(Data1);
                        writer.newLine();
                    }

                    writer.close();

                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
        }
        }
    
    
    private synchronized boolean isDifferent() {
        File highscore = new File("highscore.txt");
        boolean diff = true;
        try {
            Scanner sc = new Scanner(highscore);
            String[] processing;
            boolean stop = false;
            int i = 0;
            while (sc.hasNextLine() && !stop) {
                processing = sc.nextLine().split(";");
                if (processing[0].equals(userName)) {
                    diff = score != Integer.parseInt(processing[1]);
                    stop = true;
                    idx = i;
                } else {
                    i++;
                }
            }

        } catch (FileNotFoundException ex) {
            System.err.println("Highscore.txt not found");
            highscore.createNewFile();
        } finally {
            return diff;
        }
    }
    
    public void shuffle() {
        char[] characters = soal.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = (int) (Math.random() * characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        soalAcak = new String(characters);
    }
    
}
