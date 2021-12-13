package com.company;

import com.company.engines.Engine2Player;
import com.company.engines.EnginePlayerBot;

import java.util.Scanner;

public class Main {

    boolean b[] = new boolean[3];
    int cnt = 0;

    public static void main(String[] args) throws ArithmeticException {
        Scanner reader = new Scanner(System.in);

        EnginePlayerBot engine = new EnginePlayerBot(reader);

        engine.Run();
    }


}
