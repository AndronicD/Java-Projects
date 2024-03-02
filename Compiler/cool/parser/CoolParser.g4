parser grammar CoolParser;

options {
    tokenVocab = CoolLexer;
}

@header{
    package cool.parser;
}

program : (class SEMI)+ EOF ;

class : CLASS type = TYPE (INHERITS inherit = TYPE)? LBRACE (feature SEMI)* RBRACE ;

formal : id = ID COLON type = TYPE;

feature
    :
        id = ID LPAREN (formalParams+=formal (COMMA formalParams+=formal)*)? RPAREN COLON type = TYPE LBRACE methodBody=expr RBRACE     #method
        | id = ID COLON type = TYPE (ASSIGN expr)?                                                                                      #attribute
    ;

local : ID COLON type = (TYPE | ID) (ASSIGN expr)?;

expr
    :   expr (AT type = (TYPE | ID))? DOT id = ID LPAREN (expr (COMMA expr)*)? RPAREN                # dispatch
        | ID LPAREN (expr (COMMA expr)*)? RPAREN                # implicit_dispatch
        | IF cond = expr THEN then = expr ELSE else = expr FI   # if
        | WHILE cond = expr LOOP body = expr POOL               # while
        | LBRACE (expr SEMI)+ RBRACE                            # block
        | LET local (COMMA local)*? IN body = expr              # let
        | CASE cond = expr OF (case = expr SEMI)+ ESAC          # case_block
        | ID COLON type = (ID | TYPE) RESULTS_CASE expr                   # case_expr
        | NEW type = (ID | TYPE)                                # new
        | ISVOID expr                                           # isvoid
        | TILDA expr                                            # negation
        | leftExp = expr op = (MULTIPLY | DIVIDE) rightExp = expr      # multdiv
        | leftExp = expr op = (MINUS | PLUS) rightExp = expr    # plusminus
        | leftExp = expr LT rightExp = expr                     # less_than
        | leftExp = expr LE rightExp = expr                     # less_than_or_equal
        | leftExp = expr EQ rightExp = expr                     # equal
        | NOT expr                                              # not
        | ID ASSIGN expr                                        # assignment
        | LPAREN expr RPAREN                                    # parentheses
        | ID                                                    # identifier
        | INT                                                   # integer
        | STRING                                                # string
        | TRUE                                                  # true
        | FALSE                                                 # false
    ;
