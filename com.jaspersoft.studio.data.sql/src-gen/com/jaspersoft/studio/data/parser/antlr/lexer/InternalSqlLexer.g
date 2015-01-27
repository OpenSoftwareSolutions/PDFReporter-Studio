
/*
* generated by Xtext
*/
lexer grammar InternalSqlLexer;


@header {
package com.jaspersoft.studio.data.parser.antlr.lexer;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}




KEYWORD_75 : ('F'|'f')('E'|'e')('T'|'t')('C'|'c')('H'|'h')' '('F'|'f')('I'|'i')('R'|'r')('S'|'s')('T'|'t');

KEYWORD_76 : ('I'|'i')('S'|'s')' '('N'|'n')('O'|'o')('T'|'t')' '('N'|'n')('U'|'u')('L'|'l')('L'|'l');

KEYWORD_77 : ('N'|'n')('O'|'o')('T'|'t')' '('B'|'b')('E'|'e')('T'|'t')('W'|'w')('E'|'e')('E'|'e')('N'|'n');

KEYWORD_71 : ('I'|'i')('N'|'n')('T'|'t')('E'|'e')('R'|'r')('S'|'s')('E'|'e')('C'|'c')('T'|'t');

KEYWORD_72 : ('R'|'r')('O'|'o')('W'|'w')('S'|'s')' '('O'|'o')('N'|'n')('L'|'l')('Y'|'y');

KEYWORD_73 : ('W'|'w')('I'|'i')('T'|'t')('H'|'h')' '('T'|'t')('I'|'i')('E'|'e')('S'|'s');

KEYWORD_74 : '['('B'|'b')('E'|'e')('T'|'t')('W'|'w')('E'|'e')('E'|'e')('N'|'n')']';

KEYWORD_63 : ('B'|'b')('E'|'e')('T'|'t')('W'|'w')('E'|'e')('E'|'e')('N'|'n')']';

KEYWORD_64 : ('D'|'d')('I'|'i')('S'|'s')('T'|'t')('I'|'i')('N'|'n')('C'|'c')('T'|'t');

KEYWORD_65 : ('G'|'g')('R'|'r')('O'|'o')('U'|'u')('P'|'p')' '('B'|'b')('Y'|'y');

KEYWORD_66 : ('N'|'n')('O'|'o')('T'|'t')' '('L'|'l')('I'|'i')('K'|'k')('E'|'e');

KEYWORD_67 : ('N'|'n')('O'|'o')('T'|'t')('E'|'e')('Q'|'q')('U'|'u')('A'|'a')('L'|'l');

KEYWORD_68 : ('O'|'o')('R'|'r')('D'|'d')('E'|'e')('R'|'r')' '('B'|'b')('Y'|'y');

KEYWORD_69 : '['('B'|'b')('E'|'e')('T'|'t')('W'|'w')('E'|'e')('E'|'e')('N'|'n');

KEYWORD_70 : '['('G'|'g')('R'|'r')('E'|'e')('A'|'a')('T'|'t')('E'|'e')('R'|'r');

KEYWORD_58 : ('B'|'b')('E'|'e')('T'|'t')('W'|'w')('E'|'e')('E'|'e')('N'|'n');

KEYWORD_59 : ('G'|'g')('R'|'r')('E'|'e')('A'|'a')('T'|'t')('E'|'e')('R'|'r');

KEYWORD_60 : ('I'|'i')('S'|'s')' '('N'|'n')('U'|'u')('L'|'l')('L'|'l');

KEYWORD_61 : ('N'|'n')('A'|'a')('T'|'t')('U'|'u')('R'|'r')('A'|'a')('L'|'l');

KEYWORD_62 : ('P'|'p')('E'|'e')('R'|'r')('C'|'c')('E'|'e')('N'|'n')('T'|'t');

KEYWORD_53 : ('E'|'e')('X'|'x')('C'|'c')('E'|'e')('P'|'p')('T'|'t');

KEYWORD_54 : ('H'|'h')('A'|'a')('V'|'v')('I'|'i')('N'|'n')('G'|'g');

KEYWORD_55 : ('N'|'n')('O'|'o')('T'|'t')' '('I'|'i')('N'|'n');

KEYWORD_56 : ('O'|'o')('F'|'f')('F'|'f')('S'|'s')('E'|'e')('T'|'t');

KEYWORD_57 : ('S'|'s')('E'|'e')('L'|'l')('E'|'e')('C'|'c')('T'|'t');

KEYWORD_41 : ('C'|'c')('A'|'a')('S'|'s')('T'|'t')'(';

KEYWORD_42 : ('C'|'c')('R'|'r')('O'|'o')('S'|'s')('S'|'s');

KEYWORD_43 : ('E'|'e')('Q'|'q')('U'|'u')('A'|'a')('L'|'l');

KEYWORD_44 : ('I'|'i')('N'|'n')('N'|'n')('E'|'e')('R'|'r');

KEYWORD_45 : ('L'|'l')('E'|'e')('S'|'s')('S'|'s')']';

KEYWORD_46 : ('L'|'l')('I'|'i')('M'|'m')('I'|'i')('T'|'t');

KEYWORD_47 : ('M'|'m')('I'|'i')('N'|'n')('U'|'u')('S'|'s');

KEYWORD_48 : ('N'|'n')('O'|'o')('T'|'t')('I'|'i')('N'|'n');

KEYWORD_49 : ('O'|'o')('U'|'u')('T'|'t')('E'|'e')('R'|'r');

KEYWORD_50 : ('R'|'r')('I'|'i')('G'|'g')('H'|'h')('T'|'t');

KEYWORD_51 : ('U'|'u')('N'|'n')('I'|'i')('O'|'o')('N'|'n');

KEYWORD_52 : ('W'|'w')('H'|'h')('E'|'e')('R'|'r')('E'|'e');

KEYWORD_29 : ('C'|'c')('A'|'a')('S'|'s')('E'|'e');

KEYWORD_30 : ('D'|'d')('E'|'e')('S'|'s')('C'|'c');

KEYWORD_31 : ('E'|'e')('L'|'l')('S'|'s')('E'|'e');

KEYWORD_32 : ('F'|'f')('R'|'r')('O'|'o')('M'|'m');

KEYWORD_33 : ('F'|'f')('U'|'u')('L'|'l')('L'|'l');

KEYWORD_34 : ('J'|'j')('O'|'o')('I'|'i')('N'|'n');

KEYWORD_35 : ('L'|'l')('E'|'e')('F'|'f')('T'|'t');

KEYWORD_36 : ('L'|'l')('E'|'e')('S'|'s')('S'|'s');

KEYWORD_37 : ('L'|'l')('I'|'i')('K'|'k')('E'|'e');

KEYWORD_38 : ('S'|'s')('O'|'o')('M'|'m')('E'|'e');

KEYWORD_39 : ('T'|'t')('H'|'h')('E'|'e')('N'|'n');

KEYWORD_40 : ('W'|'w')('H'|'h')('E'|'e')('N'|'n');

KEYWORD_23 : ('A'|'a')('L'|'l')('L'|'l');

KEYWORD_24 : ('A'|'a')('N'|'n')('D'|'d');

KEYWORD_25 : ('A'|'a')('N'|'n')('Y'|'y');

KEYWORD_26 : ('A'|'a')('S'|'s')('C'|'c');

KEYWORD_27 : ('E'|'e')('N'|'n')('D'|'d');

KEYWORD_28 : ('T'|'t')('O'|'o')('P'|'p');

KEYWORD_14 : '$'('X'|'x');

KEYWORD_15 : '<''=';

KEYWORD_16 : '<''>';

KEYWORD_17 : '>''=';

KEYWORD_18 : ('A'|'a')('S'|'s');

KEYWORD_19 : ('I'|'i')('N'|'n');

KEYWORD_20 : ('O'|'o')('N'|'n');

KEYWORD_21 : ('O'|'o')('R'|'r');

KEYWORD_22 : '|''|';

KEYWORD_1 : '(';

KEYWORD_2 : ')';

KEYWORD_3 : '+';

KEYWORD_4 : ',';

KEYWORD_5 : '-';

KEYWORD_6 : '.';

KEYWORD_7 : '/';

KEYWORD_8 : '<';

KEYWORD_9 : '=';

KEYWORD_10 : '>';

KEYWORD_11 : '{';

KEYWORD_12 : '|';

KEYWORD_13 : '}';



RULE_JRPARAM : '$P{' ( options {greedy=false;} : . )*'}';

RULE_JRNPARAM : '$P!{' ( options {greedy=false;} : . )*'}';

RULE_STAR : '*';

RULE_INT : '-'? ('0'..'9')+;

RULE_TIMESTAMP : RULE_DATE ' ' RULE_TIME;

RULE_DATE : '\'' '0'..'9' '0'..'9' '0'..'9' '0'..'9' '-' '0'..'1' '0'..'9' '-' '0'..'3' '0'..'9' '\'';

RULE_TIME : '\'' '0'..'9' '0'..'9' ':' '0'..'9' '0'..'9' ':' '0'..'1' '0'..'9' '.' '0'..'9' '0'..'9' '0'..'9' '\'';

RULE_SIGNED_DOUBLE : '-'? ('0'..'9')+ ('.' ('0'..'9')+)?;

RULE_STRING_ : '\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'';

RULE_STRING : '"' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'"')))* '"';

RULE_DBNAME : ('`' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'`')))* '`'|'[' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|']')))* ']');

RULE_ID : ('a'..'z'|'A'..'Z'|'\u00C0'..'\u00FF'|'\u0100'..'\u017F'|'\u0180'..'\u024F'|'\u0410'..'\u044F'|'_'|'-'|'\u3041'..'\u309F'|'\u30A0'..'\u30FF'|'\u31F0'..'\u31FF'|'\u4E00'..'\u9FFF'|'\u6B74'..'\u3059'|'\u30A2'..'\u30F3'|'\uF900'..'\uFAFF'|'\u3400'..'\u4DBF'|'0'..'9')*;

RULE_SL_COMMENT : ('--'|'#'|'//') ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

RULE_WS : (' '|'\t'|'\r'|'\n')+;

RULE_ANY_OTHER : .;



