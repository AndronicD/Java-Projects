parser grammar CoolParser;

options {
    tokenVocab = CoolLexer;
}

@header{
    package cool.parser;
}

program : (class SEMI)+ EOF;

class : CLASS ID (INHERITS type = (TYPE | ID))? LBRACE feature* RBRACE;

feature : ID LPAREN (args+=formal (COMMA args+=formal)*)? RPAREN COLON type = (TYPE | ID) LBRACE body = expr RBRACE SEMI  # method
        | ID COLON type = (TYPE | ID) (DARROW expr)? SEMI                                                          # attribute
        ;

formal : ID COLON type = (TYPE | ID);

local : ID COLON type = (TYPE | ID) (DARROW expr)?;

expr : expr (AT type = (TYPE | ID))? DOT id = ID LPAREN (expr (COMMA expr)*)? RPAREN                # dispatch
     | ID LPAREN (expr (COMMA expr)*)? RPAREN                # implicit_dispatch
     | IF cond = expr THEN then = expr ELSE else = expr FI   # if
     | WHILE cond = expr LOOP body = expr POOL               # while
     | LBRACE (expr SEMI)+ RBRACE                            # block
     | LET local (COMMA local)*? IN body = expr              # let
     | CASE cond = expr OF (case = expr SEMI)+ ESAC          # case_block
     | ID COLON type = (ID | TYPE) RE expr                   # case_expr
     | NEW type = (ID | TYPE)                                # new
     | ISVOID expr                                           # isvoid
     | leftExp = expr op = (MULT | DIV) rightExp = expr      # multdiv
     | leftExp = expr op = (MINUS | PLUS) rightExp = expr    # plusminus
     | TILDE expr                                            # negation
     | leftExp = expr LT rightExp = expr                     # less_than
     | leftExp = expr LE rightExp = expr                     # less_than_or_equal
     | leftExp = expr EQUAL rightExp = expr                  # equal
     | NOT expr                                              # not
     | ID DARROW expr                                        # assignment
     | LPAREN expr RPAREN                                    # parentheses
     | ID                                                    # identifier
     | INT                                                   # integer
     | STRING                                                # string
     | TRUE                                                  # true
     | FALSE                                                 # false
     | SELF                                                  # self
     ;
