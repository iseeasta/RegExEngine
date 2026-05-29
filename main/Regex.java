package com.company.ProjectMaking.RegEx.main;
import java.util.Scanner;

import static com.company.ProjectMaking.RegEx.main.RegExGen.generateFromPattern;
import static com.company.ProjectMaking.RegEx.main.RegExMatch.isMatch;

public class Regex {
    public static void main(String[] args) {
        Scanner scAn = new Scanner(System.in);

        System.out.println("Welcome to the Regex Engine!");

        //Just empty line,to make output look clean
        System.out.println("   ");
        //System.out.println("\n");  escapes more line

        //to wait,after printing welcome
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Choose options from below:");
        //System.out.println("1.)Pattern Matching and 2.)Pattern Generation");
        System.out.print("1.)Pattern Matching\t");
        System.out.println("2.)Pattern Generation");

        //option
        System.out.print("Option: ");
        int choice = scAn.nextInt();
        //consume leftover newline after nextInt()
        //for safety (it didnt affect even when i didnt used it)
        scAn.nextLine();

        System.out.println("\nYou have chosen option: " + choice);

        char[] patternCh = null;
        char[] textCh = null;

        if (choice == 1) {
            System.out.print("Please enter the RegEx pattern (eg- a*b):");
            String pattern = scAn.nextLine();

            System.out.print("Now, enter the text to match against (eg- aab):");
            String text = scAn.nextLine();

            patternCh = pattern.toCharArray();
            textCh = text.toCharArray();

            boolean result = isMatch(patternCh, textCh);
            System.out.println("Match result: " + result);
        }

        if (choice == 2) {
            System.out.print("Please enter the RegEx pattern:");
            String pattern = scAn.nextLine();

            patternCh = pattern.toCharArray();

            String generated = generateFromPattern(patternCh);
            System.out.println("Generated string: " + generated);
        }

        scAn.close();
    }

}


