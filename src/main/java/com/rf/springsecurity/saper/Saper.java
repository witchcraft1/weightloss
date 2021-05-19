package com.rf.springsecurity.saper;

import java.util.*;

public class Saper {
    private static final String[] ABC = {"A", "B", "C", "D", "E", "F", "G", "H", "K", "L", "M", "N"};

    private int measure;
    private int bombs;
    private Set<Integer> bombs_position;

    private String[][] area;//only bombs
    private String[][] user_area;
    private Random rand;

    private boolean gameOver;
    public Saper(int measure, int bombs){
        this.measure = measure;
        this.bombs = bombs;
        area = new String[measure][measure];
        user_area = new String[measure][measure];
    }

    public  void print2DArray(String[][] arr){

        System.out.print("   ");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(i + "  ");
        }
        System.out.println();
        for (int i = 0; i < arr.length; i++) {
            System.out.println(ABC[i]  + " " + Arrays.toString(arr[i]));
        }
    }

    private Set<Integer> bombs_pos(){
        rand = new Random();
        int random_pos = 0;
        Set<Integer> bombs_pos = new TreeSet<>();
        for (int i = 0; i < bombs; i++) {
            while(bombs_pos.contains((random_pos = rand.nextInt(measure*measure))));
            bombs_pos.add(random_pos);
        }
        return bombs_pos;
    }

    public String[][] emptyGenerate(){
        for (int i = 0; i < area.length; i++) {
            for (int j = 0; j < area[0].length; j++) {
                area[i][j] = user_area[i][j]  = "O";
            }
        }
        return area;
    }

//    public void closeToBomb(int x, int y){
//        if(Integer.parseInt(area[x][y])
//    }

    public String[][] generate(){
//        int probability = bombs/(measure*measure);
        emptyGenerate();
        Set<Integer> bombs_pos =  bombs_position = bombs_pos();

        for (Integer bomb_pos : bombs_pos) {
            int x = bomb_pos/measure;
            int y = bomb_pos % measure;
            area[x][y] = "X";
        }

       /* for (int i = 0; i < area.length; i++) {
            for (int j = 0; j < area[0].length; j++) {
                if(area[i][j].equals("X")){

                }
            }
        }*/

        return area;
    }

    public String[][] user_area(int x, int y){
        if(bombs_position.contains(x*measure + y)) {
            gameOver = true;
            user_area[x][y] = "X";
        } else {
            user_area[x][y] = "+";
        }
        return user_area;
    }


    public void startGame(){
        int bombs_remain = bombs;
        int space_remain = measure*measure - bombs;

        Scanner sc = new Scanner(System.in);
        while(!gameOver && bombs_remain != 0 && space_remain != 0){
            print2DArray(user_area(sc.nextInt(),sc.nextInt()));
            space_remain--;//TODO change
        }
    }
}
