package com.company.ProjectMaking.RegEx.main;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.company.ProjectMaking.RegEx.main.RegExSym.buildSpecialSet;

public class RegExGen {

    static String generateFromPattern(char[] patternCh)
    {
        StringBuilder sb = new StringBuilder();
        String mixCh = "abcdefghijklmnopqrstuvwxyz";
        String mixNum = "1234567890";
        char[] mixAr  = mixCh.toCharArray();
        char[] numAr  = mixNum.toCharArray();
        Random rd = new Random();

        Set<Character> special = buildSpecialSet();

        for (int i = 0; i < patternCh.length; i++)
        {
            if (special.contains(patternCh[i]))
            {
                switch (patternCh[i])
                {
                    case '+':
                        char pChar = patternCh[i - 1];
                        int timesPlus = rd.nextInt(4) + 1; // 1 to 4
                        sb.append(String.valueOf(pChar).repeat(timesPlus));
                        break;

                    case '*':
                        char sChar = patternCh[i - 1];
                        int timesStar = rd.nextInt(4); // 0 to 3
                        sb.append(String.valueOf(sChar).repeat(timesStar));
                        break;

                    case '?':
                        char qChar = patternCh[i - 1];
                        int timesQ = rd.nextInt(2); // 0 or 1
                        sb.append(String.valueOf(qChar).repeat(timesQ));
                        break;

                    case '^':
                        // start anchor — no output, just marks position
                        break;

                    case '$':
                        // end anchor — no output
                        break;

                    case '.':
                        // any char — pick random from mixAr
                        sb.append(mixAr[rd.nextInt(mixAr.length)]);
                        break;

                    case '(':
                        i++;
                        StringBuilder groupGen = new StringBuilder();
                        while (i < patternCh.length && patternCh[i] != ')')
                        {
                            groupGen.append(patternCh[i]);
                            i++;
                        }
                        // check if next char is + * ?
                        if (i + 1 < patternCh.length)
                        {
                            char next = patternCh[i + 1];
                            if (next == '+')      { int t = rd.nextInt(3) + 1; sb.append(groupGen.toString().repeat(t)); i++; }
                            else if (next == '*') { int t = rd.nextInt(3);     sb.append(groupGen.toString().repeat(t)); i++; }
                            else if (next == '?') { int t = rd.nextInt(2);     sb.append(groupGen.toString().repeat(t)); i++; }
                            else                  { sb.append(groupGen); }
                        } else
                        {
                            sb.append(groupGen);
                        }
                        break;

                    case ')':
                        break;

                    case '[':
                        i++;
                        boolean negation = false;
                        if (i < patternCh.length && patternCh[i] == '^') { negation = true; i++; }

                        Set<Character> charSetGen = new HashSet<>();
                        while (i < patternCh.length && patternCh[i] != ']')
                        {
                            if (i + 1 < patternCh.length && patternCh[i + 1] == '-' && i + 2 < patternCh.length && patternCh[i + 2] != ']')
                            {
                                char from = patternCh[i];
                                char to   = patternCh[i + 2];
                                for (char c = from; c <= to; c++) { charSetGen.add(c); }
                                i += 2;
                            } else
                            {
                                charSetGen.add(patternCh[i]);
                            }
                            i++;
                        }

                        if (!negation)
                        {
                            // pick any 1 char from the set
                            Character[] arr = charSetGen.toArray(new Character[0]);
                            sb.append(arr[rd.nextInt(arr.length)]);
                        } else
                        {
                            // pick any 1 char NOT in the set
                            char picked;
                            do { picked = mixAr[rd.nextInt(mixAr.length)]; }
                            while (charSetGen.contains(picked));
                            sb.append(picked);
                        }
                        break;

                    case ']':
                        break;

                    case '{':
                        i++;
                        StringBuilder countSbGen = new StringBuilder();
                        while (i < patternCh.length && patternCh[i] != '}')
                        {
                            countSbGen.append(patternCh[i]);
                            i++;
                        }
                        String[] partsGen = countSbGen.toString().split(",");
                        int minGen = Integer.parseInt(partsGen[0].trim());
                        int maxGen = partsGen.length > 1 ? Integer.parseInt(partsGen[1].trim()) : minGen;
                        int repeatCount = minGen + rd.nextInt(maxGen - minGen + 1);

                        char beforeCurly = patternCh[i - countSbGen.length() - 2];
                        sb.append(String.valueOf(beforeCurly).repeat(repeatCount));
                        break;

                    case '}':
                        break;

                    case '|':
                        // pick left or right side randomly
                        String fullPat = new String(patternCh);
                        String[] sidesGen = fullPat.split("\\|");
                        if (sidesGen.length == 2)
                        {
                            int pick = rd.nextInt(2);
                            return generateFromPattern(sidesGen[pick].toCharArray()); // return directly
                        }
                        break;

                    case '\\':
                        if (i + 1 < patternCh.length)
                        {
                            i++;
                            switch (patternCh[i])
                            {
                                case 'd': sb.append(numAr[rd.nextInt(numAr.length)]); break;  // \d → random digit
                                case 'w': sb.append(mixAr[rd.nextInt(mixAr.length)]); break;  // \w → random word char
                                case 's': sb.append(' ');                              break;  // \s → space
                                default:  sb.append(patternCh[i]);                    break;  // literal
                            }
                        }
                        break;

                    case '-':
                        // handled inside [] case
                        break;

                    default:
                        System.out.println("Unsupported Expression: " + patternCh[i]);
                        break;
                }
            } else
            {
                // literal character — append as is
                sb.append(patternCh[i]);
            }
        }

        return sb.toString();
    }
}
