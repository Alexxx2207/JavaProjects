package com.company;

import com.company.engines.Engine2Player;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        Engine2Player gameEngine2Player = new Engine2Player(reader);

        gameEngine2Player.Run();

    }

}
