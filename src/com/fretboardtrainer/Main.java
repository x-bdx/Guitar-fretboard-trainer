package com.fretboardtrainer;

import com.fretboardtrainer.logic.Trainer;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Select strings to practice (1-6, separated by commas): ");
        String[] stringInputs = sc.nextLine().split(",");
        List<Integer> strings = new java.util.ArrayList<>();
        for (String s : stringInputs) {
            try {
                int val = Integer.parseInt(s.trim());
                if (val >= 1 && val <= 6) strings.add(val);
            } catch (NumberFormatException ignored) {}
        }

        System.out.print("Enter interval between notes (seconds): ");
        double interval = sc.nextDouble();

        System.out.print("Enter number of notes: ");
        int count = sc.nextInt();

        System.out.print("Enable note detection? (y/n): ");
        boolean enableDetection = sc.next().trim().equalsIgnoreCase("y");

        Trainer trainer = new Trainer();
        trainer.startSession(strings, interval, count, enableDetection);

        sc.close();
    }
}
