lexer grammar CoolLexer;

tokens { ERROR }

@header{
    package cool.lexer;	
}

@members{    
    private void raiseError(String msg) {
        setText(msg);
        setType(ERROR);
    }
}

WS
    :   [ \n\f\r\t]+ -> skip
    ;


STRING
    : '"' (ESC_SEQ | '\\"' | .)*?
    ('"' {
            String text = getText();
            text = text.substring(1, text.length() - 1);

            StringBuilder processedText = new StringBuilder();
            boolean escapeMode = false;
            for (char c : text.toCharArray()) {
                if (escapeMode) {
                    switch (c) {
                        case 'n':
                            processedText.append('\n');
                            break;
                        case 't':
                            processedText.append('\t');
                            break;
                        case 'b':
                            processedText.append('\b');
                            break;
                        case 'f':
                            processedText.append('\f');
                            break;
                        default:
                            processedText.append(c);
                            break;
                    }
                    escapeMode = false;
                } else if (c == '\\') {
                    escapeMode = true;
                } else {
                    processedText.append(c);
                }
            }
            setText(processedText.toString());
        }
        )
    ;

IF : 'if';
THEN : 'then';
ELSE : 'else';
FI : 'fi';
CLASS : 'class';
FALSE : 'false';
TRUE : 'true';
IN : 'in';
INHERITS : 'inherits';
ISVOID : 'isvoid';
LET : 'let';
LOOP : 'loop';
POOL : 'pool';
WHILE : 'while';
CASE : 'case';
ESAC : 'esac';
NEW : 'new';
OF : 'of';
NOT : 'not';
AT : '@';
DOT : '.';
COLON : ':';
TILDE : '~';
SELF : 'self';
TYPE : 'Int' | 'Bool' | 'String' | 'Object' | 'IO' | 'SELF_TYPE';

SEMI : ';';
COMMA : ',';
LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
PLUS : '+';
MINUS : '-';
MULT : '*';
DIV : '/';
EQUAL : '=';
LT : '<';
LE : '<=';
RE : '=>';
DARROW : '<-';

fragment ESC_SEQ
    : '\\' ('n' | 't' | 'b' | 'f')
    ;


fragment LETTER : [a-zA-Z];
ID : (LETTER | '_')(LETTER | '_' | DIGIT)*;

fragment DIGIT : [0-9];
INT : DIGIT+;

fragment DIGITS : DIGIT+;
fragment EXPONENT : 'e' ('+' | '-')? DIGITS;
FLOAT : (DIGITS ('.' DIGITS?)? | '.' DIGITS) EXPONENT?;

LINE_COMMENT
    :   '--' ~[\r\n]* -> skip
    ;

BLOCK_COMMENT
    : '(*'
      (BLOCK_COMMENT | .)*?
      ('*)' | EOF { raiseError("EOF in comment"); }) -> skip
    ;

INVALID_CHAR : . {
    raiseError("Invalid character: " + getText());
};

