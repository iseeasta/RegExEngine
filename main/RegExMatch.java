package com.company.ProjectMaking.RegEx.main;

import java.util.HashSet;
import java.util.Set;

import static com.company.ProjectMaking.RegEx.main.RegExSym.buildSpecialSet;

public class RegExMatch {

    //isMatch: checks if textCh matches the pattern
    static boolean isMatch(char[] patternCh, char[] textCh)
    {
        StringBuilder sb = new StringBuilder();
        String mixCh = "abcdefghijklmnopqrstuvwxyz1234567890";
        char[] mixAr = mixCh.toCharArray();

        Set<Character> special = buildSpecialSet();

        for (int i = 0; i < patternCh.length; i++)
        {
            if (special.contains(patternCh[i]))
            {
                long count;
                char preChar;

                switch (patternCh[i])
                {
                    // + means 1 or more
                    case '+':
                        //— check textCh has at least 1 of preChar
                        if (textCh.length == 0) { return false; }

                        preChar = patternCh[i - 1];
                        count = new String(textCh).chars().filter(c -> c == preChar).count();

                        // + requires at least 1
                        if (count < 1) { return false; }
                        break;

                    // * means 0 or more
                    case '*':
                        preChar = patternCh[i - 1];
                        count = new String(textCh).chars().filter(c -> c == preChar).count();

                        // no return false needed
                        break;

                    //  ? means 0 or 1 , not more than 1
                    case '?':
                        preChar = patternCh[i - 1];
                        count = new String(textCh).chars().filter(c -> c == preChar).count();

                        //? allows max 1
                        if (count > 1) { return false; }
                        break;

                    // ^ start anchor
                    case '^':
                        if (i == 0)
                        {
                            // anchor: pattern must match from start of text
                            if (i + 1 < patternCh.length && textCh.length > 0)
                            {
                                if (textCh[0] != patternCh[i + 1]) { return false; }
                            }

                        } else if (patternCh[i - 1] == '[')
                        {
                            // [^ == skip until ] comes
                            // collect the negated set then check
                            Set<Character> negSet = new HashSet<>();
                            i++;
                            while (i < patternCh.length && patternCh[i] != ']')
                            {
                                if (i + 1 < patternCh.length && patternCh[i + 1] == '-' && i + 2 < patternCh.length && patternCh[i + 2] != ']')
                                {
                                    // range inside negation like [^a-z]
                                    char from = patternCh[i];
                                    char to   = patternCh[i + 2];
                                    for (char c = from; c <= to; c++) { negSet.add(c); }
                                    i += 2;
                                } else
                                {
                                    negSet.add(patternCh[i]);
                                }
                                i++;
                            }
                            // check: no char in textCh should be in negSet
                            for (char c : textCh)
                            {
                                if (negSet.contains(c)) { return false; }
                            }
                        }
                        break;

                    // $ = end anchor — text must end with previous char
                    case '$':
                        preChar = patternCh[i - 1];
                        if (textCh[textCh.length - 1] != preChar) { return false; } // FIX: actual end check
                        break;

                    // . = any single character — just check text has a char at this position
                    case '.':
                        //  dot just means any char exists at particular position
                        // track text position separately ideally, but basic check:
                        if (textCh.length == 0) { return false; }
                        break;

                    // ( = start group, collect until )
                    case '(':
                        i++;
                        StringBuilder group = new StringBuilder();
                        while (i < patternCh.length && patternCh[i] != ')')
                        {
                            group.append(patternCh[i]);
                            i++;
                        }
                        // FIX: check if group content exists in text
                        if (!new String(textCh).contains(group.toString())) { return false; }
                        break;

                    // ) = handled inside ( case, skip
                    case ')':
                        break;

                    // [ = character class, collect set then check one match
                    case '[':
                        i++;
                        Set<Character> charSet = new HashSet<>();
                        while (i < patternCh.length && patternCh[i] != ']')
                        {
                            // FIX: handle range like [a-z]
                            if (i + 1 < patternCh.length && patternCh[i + 1] == '-' && i + 2 < patternCh.length && patternCh[i + 2] != ']')
                            {
                                char from = patternCh[i];
                                char to   = patternCh[i + 2];
                                for (char c = from; c <= to; c++) { charSet.add(c); }
                                i += 2;
                            } else
                            {
                                charSet.add(patternCh[i]);
                            }
                            i++;
                        }
                        // FIX: check at least one char in textCh is in charSet
                        boolean foundOne = false;
                        for (char c : textCh)
                        {
                            if (charSet.contains(c)) { foundOne = true; break; }
                        }
                        if (!foundOne) { return false; }
                        break;

                    // ] handled inside [ case
                    case ']':
                        break;

                    // { = exact count quantifier
                    case '{':
                        i++;
                        StringBuilder countSb = new StringBuilder();
                        while (i < patternCh.length && patternCh[i] != '}')
                        {
                            countSb.append(patternCh[i]);
                            i++;
                        }
                        String[] parts = countSb.toString().split(",");
                        int min = Integer.parseInt(parts[0].trim());
                        int max = parts.length > 1 ? Integer.parseInt(parts[1].trim()) : min;

                        preChar = patternCh[i - countSb.length() - 2]; // char before {
                        final char preCharFinal = preChar;
                        long actualCount = new String(textCh).chars().filter(c -> c == preCharFinal).count();

                        if (actualCount < min || actualCount > max) { return false; } // FIX: range check
                        break;

                    // } handled inside { case
                    case '}':
                        break;

                    // | = OR — check if either side matches
                    case '|':
                        // FIX: split pattern at | and check both sides
                        String fullPattern = new String(patternCh);
                        String[] sides = fullPattern.split("\\|");
                        if (sides.length == 2)
                        {
                            boolean leftMatch  = isMatch(sides[0].toCharArray(), textCh);
                            boolean rightMatch = isMatch(sides[1].toCharArray(), textCh);
                            return leftMatch || rightMatch; // FIX: return OR result directly
                        }
                        break;

                    // \ = escape, treat next char as literal
                    case '\\':
                        if (i + 1 < patternCh.length)
                        {
                            i++;
                            char escaped = patternCh[i];
                            switch (escaped)
                            {
                                case 'd': // \d = digit
                                    boolean hasDigit = false;
                                    for (char c : textCh) { if (Character.isDigit(c)) { hasDigit = true; break; } }
                                    if (!hasDigit) { return false; }
                                    break;
                                case 'w': // \w = word char
                                    boolean hasWord = false;
                                    for (char c : textCh) { if (Character.isLetterOrDigit(c) || c == '_') { hasWord = true; break; } }
                                    if (!hasWord) { return false; }
                                    break;
                                case 's': // \s = whitespace
                                    boolean hasSpace = false;
                                    for (char c : textCh) { if (Character.isWhitespace(c)) { hasSpace = true; break; } }
                                    if (!hasSpace) { return false; }
                                    break;
                                default: // \. \( etc — literal
                                    boolean hasEscaped = false;
                                    for (char c : textCh) { if (c == escaped) { hasEscaped = true; break; } }
                                    if (!hasEscaped) { return false; }
                                    break;
                            }
                        }
                        break;

                    // - handled inside [] case
                    case '-':
                        break;

                    default:
                        System.out.println("Unsupported Expression: " + patternCh[i]);
                        break;
                }
            } else
            {
                // FIX: literal character — check it exists in text
                final char literal = patternCh[i];
                boolean found = false;
                for (char c : textCh) { if (c == literal) { found = true; break; } }
                if (!found) { return false; }
            }
        }

        return true; // FIX: return true only after ALL checks pass, not mid-loop
    }
}
