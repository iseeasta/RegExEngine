package com.company.ProjectMaking.RegEx.main;

import java.util.HashSet;
import java.util.Set;

public class RegExSym {

    //Declaration/Initialisation of all symbols
    static class RegexToken
    {
        static final char ADD          = '+';
        static final char STAR         = '*';
        static final char QUESTION     = '?';
        static final char CARET        = '^';
        static final char DOLLAR       = '$';
        static final char DOT          = '.';
        static final char OPEN_PAREN   = '(';
        static final char CLOSE_PAREN  = ')';
        static final char OPEN_BRACK   = '[';
        static final char CLOSE_BRACK  = ']';
        static final char OPEN_CURLY   = '{';
        static final char CLOSE_CURLY  = '}';
        static final char PIPE         = '|';
        static final char BACKSLASH    = '\\';
        static final char HYPHEN       = '-';
    }

    //Builds the special character set
    public static Set<Character> buildSpecialSet()
    {
        Set<Character> special = new HashSet<>();
        special.add('+');
        special.add('*');
        special.add('?');
        special.add('^');
        special.add('$');
        special.add('.');
        special.add('('); special.add(')');
        special.add('['); special.add(']');
        special.add('{'); special.add('}');
        special.add('|');
        special.add('\\');
        special.add('-');
        return special;
    }
}
