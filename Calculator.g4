grammar Calculator;

// Definición del programa principal
prog:   stat+ ;

// Definición de una declaración
stat:   expr NEWLINE                # printExpr
    |   ID '=' expr NEWLINE         # assign
    |   NEWLINE                     # blank
    ;

// Definición de las expresiones
expr:   expr op=('*'|'/') expr      # MulDiv
    |   expr op=('+'|'-') expr      # AddSub
    |   INT                         # int
    |   ID                          # id
    |   '(' expr ')'                # parens
    ;

// Definición de tokens
MUL :   '*' ; 
DIV :   '/' ;
ADD :   '+' ;
SUB :   '-' ;
ID  :   [a-zA-Z]+ ;                 // Match identifiers
INT :   [0-9]+ ;                    // Match integers
NEWLINE: '\r'? '\n' ;               // Match newline characters
WS  :   [ \t]+ -> skip ;            // Skip spaces and tabs
