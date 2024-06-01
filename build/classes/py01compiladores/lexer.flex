/* JFlex example: partial Java language lexer specification */
package py01compiladores;
import java_cup.runtime.*;

/**
 * This class is a simple example lexer.
 */
%%

%class Lexer
%unicode
%cup
%line
%column

%{
  StringBuffer string = new StringBuffer();

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
  private Symbol symbol(int type, int yyline, int yycolumn, String value) {
      return new Symbol(type, yyline, yycolumn, value);
  }
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*

Identifier = [a-zA-Z]([a-zA-Z0-9])* 
DecIntegerLiteral = 0 | [1-9][0-9]*
/*================================Lexemas propios================================*/
flotante = ([0-9]*[.])?[0-9]+


/*=========================================================Lexemas propios=============================================================================*/
%state STRING
%state STRING_SINGLE

%%

/* keywords */
<YYINITIAL> {WhiteSpace}         { /* ignore */ }
<YYINITIAL> "+"                  { return symbol(sym.PLUS, yyline, yycolumn, yytext()); }
<YYINITIAL> "break"              { return symbol(sym.BREAK, yyline, yycolumn, yytext()); }
<YYINITIAL> "*"                  { return symbol(sym.TIMES, yyline, yycolumn, yytext()); }

/*================================reglas de lexematizacion propias================================*/
<YYINITIAL> {DecIntegerLiteral}  { return symbol(sym.INTEGER_LITERAL, yyline, yycolumn, yytext()); }
<YYINITIAL> {flotante}           { return symbol(sym.FLOTANTE, yyline, yycolumn, yytext()); }
<YYINITIAL> "bool"               { return symbol(sym.BOOOLEANF, yyline, yycolumn, yytext()); }
<YYINITIAL> "true"               { return symbol(sym.TRUE, yyline, yycolumn, yytext()); }
<YYINITIAL> "false"              { return symbol(sym.FALSE, yyline, yycolumn, yytext()); }
<YYINITIAL> "if"                 { return symbol(sym.IF, yyline, yycolumn, yytext()); }
<YYINITIAL> "else"               { return symbol(sym.ELSE, yyline, yycolumn, yytext()); }
<YYINITIAL> "("                  { return symbol(sym.PARENTA, yyline, yycolumn, yytext()); }
<YYINITIAL> ")"                  { return symbol(sym.PARENTC, yyline, yycolumn, yytext()); }
<YYINITIAL> "["                  { return symbol(sym.PARENTCA, yyline, yycolumn, yytext()); }
<YYINITIAL> "]"                  { return symbol(sym.PARENTCC, yyline, yycolumn, yytext()); }
<YYINITIAL> "int"                { return symbol(sym.INT, yyline, yycolumn, yytext()); }
<YYINITIAL> ";"                  { return symbol(sym.ENDLINE, yyline, yycolumn, yytext()); }
<YYINITIAL> "float"              { return symbol(sym.FLOAT, yyline, yycolumn, yytext()); }
<YYINITIAL> "char"               { return symbol(sym.CHAR, yyline, yycolumn, yytext()); }
<YYINITIAL> "string"             { return symbol(sym.STRINGC, yyline, yycolumn, yytext()); }
<YYINITIAL> "while"              { return symbol(sym.WHILE, yyline, yycolumn, yytext()); }
<YYINITIAL> "switch"              { return symbol(sym.SWITCH, yyline, yycolumn, yytext()); }
<YYINITIAL> "case"               { return symbol(sym.CASE, yyline, yycolumn, yytext()); }
<YYINITIAL> "for"                { return symbol(sym.FOR, yyline, yycolumn, yytext()); }
<YYINITIAL> "in"                 { return symbol(sym.IN, yyline, yycolumn, yytext()); }
<YYINITIAL> "range"              { return symbol(sym.RANGE, yyline, yycolumn, yytext()); }
<YYINITIAL> ">"                  { return symbol(sym.GREATERT, yyline, yycolumn, yytext()); }
<YYINITIAL> "<"                  { return symbol(sym.LOWERT, yyline, yycolumn, yytext()); }
<YYINITIAL> ">="                 { return symbol(sym.GREATERE, yyline, yycolumn, yytext()); }
<YYINITIAL> "<="                 { return symbol(sym.LOWERE, yyline, yycolumn, yytext()); }
<YYINITIAL> "!"                  { return symbol(sym.NOT, yyline, yycolumn, yytext()); }
<YYINITIAL> "!="                 { return symbol(sym.NOTEQUALS, yyline, yycolumn, yytext()); }
<YYINITIAL> "&&"                  { return symbol(sym.CONJUNTION, yyline, yycolumn, yytext()); }
<YYINITIAL> "||"                  { return symbol(sym.DISJUNTION, yyline, yycolumn, yytext()); }
<YYINITIAL> ":"                  { return symbol(sym.SEP, yyline, yycolumn, yytext()); }
<YYINITIAL> "glob"               { return symbol(sym.GLOB, yyline, yycolumn, yytext()); }
<YYINITIAL> "loc"                { return symbol(sym.LOC, yyline, yycolumn, yytext()); }
<YYINITIAL> "return"             { return symbol(sym.RETURN, yyline, yycolumn, yytext()); }
<YYINITIAL> "print"             { return symbol(sym.PRINTF, yyline, yycolumn, yytext()); }
<YYINITIAL> "func"               { return symbol(sym.FUNC, yyline, yycolumn, yytext()); }
<YYINITIAL> "main"               { return symbol(sym.MAIN, yyline, yycolumn, yytext()); }
<YYINITIAL> "param"              { return symbol(sym.PARAM, yyline, yycolumn, yytext()); }
<YYINITIAL> "read"               { return symbol(sym.READ, yyline, yycolumn, yytext()); }
<YYINITIAL> "default"            { return symbol(sym.DEFAULT, yyline, yycolumn, yytext()); }
<YYINITIAL> "_"                  { return symbol(sym.UNDERS, yyline, yycolumn, yytext()); }
<YYINITIAL> ","                  { return symbol(sym.COMA, yyline, yycolumn, yytext()); }


/*================================reglas de lexematizacion propias================================*/

<YYINITIAL> {
  /* identifiers */
  {Identifier}                   { return symbol(sym.IDENTIFIER, yyline, yycolumn, yytext()); }

  /* literals */
  
  \"                             { string.setLength(0); yybegin(STRING); }
  \'                             { string.setLength(0); yybegin(STRING_SINGLE); }

  /* operators */
  "="                            { return symbol(sym.EQ, yyline, yycolumn, yytext()); }
  "=="                           { return symbol(sym.EQEQ, yyline, yycolumn, yytext()); }
  
  "++"                           { return symbol(sym.PLUSP, yyline, yycolumn, yytext()); }
  "-"                            { return symbol(sym.MINUS, yyline, yycolumn, yytext()); }
  "--"                           { return symbol(sym.MINUSM, yyline, yycolumn, yytext()); }
  "/"                            { return symbol(sym.DIV, yyline, yycolumn, yytext()); }
  "^"                            { return symbol(sym.POT, yyline, yycolumn, yytext()); }
  "%"                            { return symbol(sym.MOD, yyline, yycolumn, yytext()); }

  

  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  
}


<STRING> {
  \"                             { yybegin(YYINITIAL); 
                                   return symbol(sym.STRING_LITERAL, 
                                   string.toString()); }
  [^\n\r\"\\]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }

  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }
  \\                             { string.append('\\'); }
}

<STRING_SINGLE> {
  \'                             { yybegin(YYINITIAL); 
                                   return symbol(sym.STRING_SINGLE, 
                                   string.toString()); }
  [^\n\r\'\\]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }
  \\r                            { string.append('\r'); }
  \\\'                           { string.append('\''); }
  \\                             { string.append('\\'); }
}

<YYINITIAL> . {
    String errLex = "Error léxico : "+ yytext() + "en la linea: " +  (yyline+1) +  "y columna: " +  (yycolumn+1);
    //System.out.println(errLex);
}