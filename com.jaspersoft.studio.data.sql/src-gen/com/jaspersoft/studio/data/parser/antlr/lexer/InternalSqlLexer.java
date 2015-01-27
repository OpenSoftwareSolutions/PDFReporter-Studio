/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.data.parser.antlr.lexer;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalSqlLexer extends Lexer {
    public static final int RULE_ID=92;
    public static final int RULE_JRNPARAM=82;
    public static final int RULE_DATE=85;
    public static final int RULE_ANY_OTHER=96;
    public static final int KEYWORD_56=27;
    public static final int KEYWORD_55=26;
    public static final int KEYWORD_54=25;
    public static final int KEYWORD_53=24;
    public static final int KEYWORD_52=40;
    public static final int KEYWORD_51=39;
    public static final int KEYWORD_50=38;
    public static final int EOF=-1;
    public static final int KEYWORD_59=20;
    public static final int KEYWORD_58=19;
    public static final int KEYWORD_57=28;
    public static final int KEYWORD_65=13;
    public static final int KEYWORD_64=12;
    public static final int KEYWORD_67=15;
    public static final int RULE_SIGNED_DOUBLE=88;
    public static final int KEYWORD_66=14;
    public static final int KEYWORD_61=22;
    public static final int KEYWORD_60=21;
    public static final int KEYWORD_63=11;
    public static final int KEYWORD_62=23;
    public static final int KEYWORD_69=17;
    public static final int KEYWORD_68=16;
    public static final int KEYWORD_30=42;
    public static final int KEYWORD_34=46;
    public static final int KEYWORD_33=45;
    public static final int KEYWORD_32=44;
    public static final int KEYWORD_31=43;
    public static final int KEYWORD_38=50;
    public static final int KEYWORD_37=49;
    public static final int KEYWORD_36=48;
    public static final int KEYWORD_35=47;
    public static final int RULE_ML_COMMENT=94;
    public static final int KEYWORD_39=51;
    public static final int RULE_STRING=90;
    public static final int KEYWORD_41=29;
    public static final int KEYWORD_40=52;
    public static final int KEYWORD_43=31;
    public static final int KEYWORD_42=30;
    public static final int KEYWORD_45=33;
    public static final int KEYWORD_44=32;
    public static final int KEYWORD_47=35;
    public static final int KEYWORD_46=34;
    public static final int KEYWORD_49=37;
    public static final int KEYWORD_48=36;
    public static final int KEYWORD_19=64;
    public static final int KEYWORD_17=62;
    public static final int KEYWORD_18=63;
    public static final int KEYWORD_15=60;
    public static final int KEYWORD_16=61;
    public static final int KEYWORD_13=80;
    public static final int KEYWORD_14=59;
    public static final int RULE_STRING_=89;
    public static final int KEYWORD_11=78;
    public static final int KEYWORD_12=79;
    public static final int KEYWORD_10=77;
    public static final int KEYWORD_6=73;
    public static final int KEYWORD_7=74;
    public static final int KEYWORD_8=75;
    public static final int KEYWORD_9=76;
    public static final int KEYWORD_28=58;
    public static final int RULE_TIME=86;
    public static final int KEYWORD_29=41;
    public static final int RULE_INT=84;
    public static final int RULE_TIMESTAMP=87;
    public static final int KEYWORD_24=54;
    public static final int KEYWORD_25=55;
    public static final int RULE_DBNAME=91;
    public static final int KEYWORD_26=56;
    public static final int KEYWORD_27=57;
    public static final int KEYWORD_20=65;
    public static final int KEYWORD_21=66;
    public static final int KEYWORD_22=67;
    public static final int KEYWORD_23=53;
    public static final int KEYWORD_71=7;
    public static final int KEYWORD_72=8;
    public static final int KEYWORD_73=9;
    public static final int KEYWORD_74=10;
    public static final int KEYWORD_75=4;
    public static final int KEYWORD_76=5;
    public static final int KEYWORD_77=6;
    public static final int KEYWORD_1=68;
    public static final int KEYWORD_5=72;
    public static final int KEYWORD_4=71;
    public static final int KEYWORD_70=18;
    public static final int KEYWORD_3=70;
    public static final int KEYWORD_2=69;
    public static final int RULE_SL_COMMENT=93;
    public static final int RULE_STAR=83;
    public static final int RULE_JRPARAM=81;
    public static final int RULE_WS=95;

    // delegates
    // delegators

    public InternalSqlLexer() {;} 
    public InternalSqlLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InternalSqlLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g"; }

    // $ANTLR start "KEYWORD_75"
    public final void mKEYWORD_75() throws RecognitionException {
        try {
            int _type = KEYWORD_75;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:19:12: ( ( 'F' | 'f' ) ( 'E' | 'e' ) ( 'T' | 't' ) ( 'C' | 'c' ) ( 'H' | 'h' ) ' ' ( 'F' | 'f' ) ( 'I' | 'i' ) ( 'R' | 'r' ) ( 'S' | 's' ) ( 'T' | 't' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:19:14: ( 'F' | 'f' ) ( 'E' | 'e' ) ( 'T' | 't' ) ( 'C' | 'c' ) ( 'H' | 'h' ) ' ' ( 'F' | 'f' ) ( 'I' | 'i' ) ( 'R' | 'r' ) ( 'S' | 's' ) ( 'T' | 't' )
            {
            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            match(' '); 
            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_75"

    // $ANTLR start "KEYWORD_76"
    public final void mKEYWORD_76() throws RecognitionException {
        try {
            int _type = KEYWORD_76;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:21:12: ( ( 'I' | 'i' ) ( 'S' | 's' ) ' ' ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'T' | 't' ) ' ' ( 'N' | 'n' ) ( 'U' | 'u' ) ( 'L' | 'l' ) ( 'L' | 'l' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:21:14: ( 'I' | 'i' ) ( 'S' | 's' ) ' ' ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'T' | 't' ) ' ' ( 'N' | 'n' ) ( 'U' | 'u' ) ( 'L' | 'l' ) ( 'L' | 'l' )
            {
            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            match(' '); 
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            match(' '); 
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_76"

    // $ANTLR start "KEYWORD_77"
    public final void mKEYWORD_77() throws RecognitionException {
        try {
            int _type = KEYWORD_77;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:23:12: ( ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'T' | 't' ) ' ' ( 'B' | 'b' ) ( 'E' | 'e' ) ( 'T' | 't' ) ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'N' | 'n' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:23:14: ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'T' | 't' ) ' ' ( 'B' | 'b' ) ( 'E' | 'e' ) ( 'T' | 't' ) ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            match(' '); 
            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_77"

    // $ANTLR start "KEYWORD_71"
    public final void mKEYWORD_71() throws RecognitionException {
        try {
            int _type = KEYWORD_71;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:25:12: ( ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'T' | 't' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:25:14: ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'T' | 't' )
            {
            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_71"

    // $ANTLR start "KEYWORD_72"
    public final void mKEYWORD_72() throws RecognitionException {
        try {
            int _type = KEYWORD_72;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:27:12: ( ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'W' | 'w' ) ( 'S' | 's' ) ' ' ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'L' | 'l' ) ( 'Y' | 'y' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:27:14: ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'W' | 'w' ) ( 'S' | 's' ) ' ' ( 'O' | 'o' ) ( 'N' | 'n' ) ( 'L' | 'l' ) ( 'Y' | 'y' )
            {
            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            match(' '); 
            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_72"

    // $ANTLR start "KEYWORD_73"
    public final void mKEYWORD_73() throws RecognitionException {
        try {
            int _type = KEYWORD_73;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:29:12: ( ( 'W' | 'w' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'H' | 'h' ) ' ' ( 'T' | 't' ) ( 'I' | 'i' ) ( 'E' | 'e' ) ( 'S' | 's' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:29:14: ( 'W' | 'w' ) ( 'I' | 'i' ) ( 'T' | 't' ) ( 'H' | 'h' ) ' ' ( 'T' | 't' ) ( 'I' | 'i' ) ( 'E' | 'e' ) ( 'S' | 's' )
            {
            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            match(' '); 
            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_73"

    // $ANTLR start "KEYWORD_74"
    public final void mKEYWORD_74() throws RecognitionException {
        try {
            int _type = KEYWORD_74;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:31:12: ( '[' ( 'B' | 'b' ) ( 'E' | 'e' ) ( 'T' | 't' ) ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'N' | 'n' ) ']' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:31:14: '[' ( 'B' | 'b' ) ( 'E' | 'e' ) ( 'T' | 't' ) ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'N' | 'n' ) ']'
            {
            match('['); 
            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_74"

    // $ANTLR start "KEYWORD_63"
    public final void mKEYWORD_63() throws RecognitionException {
        try {
            int _type = KEYWORD_63;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:33:12: ( ( 'B' | 'b' ) ( 'E' | 'e' ) ( 'T' | 't' ) ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'N' | 'n' ) ']' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:33:14: ( 'B' | 'b' ) ( 'E' | 'e' ) ( 'T' | 't' ) ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'N' | 'n' ) ']'
            {
            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_63"

    // $ANTLR start "KEYWORD_64"
    public final void mKEYWORD_64() throws RecognitionException {
        try {
            int _type = KEYWORD_64;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:35:12: ( ( 'D' | 'd' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'C' | 'c' ) ( 'T' | 't' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:35:14: ( 'D' | 'd' ) ( 'I' | 'i' ) ( 'S' | 's' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'C' | 'c' ) ( 'T' | 't' )
            {
            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_64"

    // $ANTLR start "KEYWORD_65"
    public final void mKEYWORD_65() throws RecognitionException {
        try {
            int _type = KEYWORD_65;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:37:12: ( ( 'G' | 'g' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'P' | 'p' ) ' ' ( 'B' | 'b' ) ( 'Y' | 'y' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:37:14: ( 'G' | 'g' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'P' | 'p' ) ' ' ( 'B' | 'b' ) ( 'Y' | 'y' )
            {
            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            match(' '); 
            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_65"

    // $ANTLR start "KEYWORD_66"
    public final void mKEYWORD_66() throws RecognitionException {
        try {
            int _type = KEYWORD_66;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:39:12: ( ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'T' | 't' ) ' ' ( 'L' | 'l' ) ( 'I' | 'i' ) ( 'K' | 'k' ) ( 'E' | 'e' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:39:14: ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'T' | 't' ) ' ' ( 'L' | 'l' ) ( 'I' | 'i' ) ( 'K' | 'k' ) ( 'E' | 'e' )
            {
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            match(' '); 
            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_66"

    // $ANTLR start "KEYWORD_67"
    public final void mKEYWORD_67() throws RecognitionException {
        try {
            int _type = KEYWORD_67;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:41:12: ( ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'Q' | 'q' ) ( 'U' | 'u' ) ( 'A' | 'a' ) ( 'L' | 'l' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:41:14: ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'Q' | 'q' ) ( 'U' | 'u' ) ( 'A' | 'a' ) ( 'L' | 'l' )
            {
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='Q'||input.LA(1)=='q' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_67"

    // $ANTLR start "KEYWORD_68"
    public final void mKEYWORD_68() throws RecognitionException {
        try {
            int _type = KEYWORD_68;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:43:12: ( ( 'O' | 'o' ) ( 'R' | 'r' ) ( 'D' | 'd' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ' ' ( 'B' | 'b' ) ( 'Y' | 'y' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:43:14: ( 'O' | 'o' ) ( 'R' | 'r' ) ( 'D' | 'd' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ' ' ( 'B' | 'b' ) ( 'Y' | 'y' )
            {
            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            match(' '); 
            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_68"

    // $ANTLR start "KEYWORD_69"
    public final void mKEYWORD_69() throws RecognitionException {
        try {
            int _type = KEYWORD_69;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:45:12: ( '[' ( 'B' | 'b' ) ( 'E' | 'e' ) ( 'T' | 't' ) ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'N' | 'n' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:45:14: '[' ( 'B' | 'b' ) ( 'E' | 'e' ) ( 'T' | 't' ) ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'N' | 'n' )
            {
            match('['); 
            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_69"

    // $ANTLR start "KEYWORD_70"
    public final void mKEYWORD_70() throws RecognitionException {
        try {
            int _type = KEYWORD_70;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:47:12: ( '[' ( 'G' | 'g' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:47:14: '[' ( 'G' | 'g' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' )
            {
            match('['); 
            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_70"

    // $ANTLR start "KEYWORD_58"
    public final void mKEYWORD_58() throws RecognitionException {
        try {
            int _type = KEYWORD_58;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:49:12: ( ( 'B' | 'b' ) ( 'E' | 'e' ) ( 'T' | 't' ) ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'N' | 'n' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:49:14: ( 'B' | 'b' ) ( 'E' | 'e' ) ( 'T' | 't' ) ( 'W' | 'w' ) ( 'E' | 'e' ) ( 'E' | 'e' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_58"

    // $ANTLR start "KEYWORD_59"
    public final void mKEYWORD_59() throws RecognitionException {
        try {
            int _type = KEYWORD_59;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:51:12: ( ( 'G' | 'g' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:51:14: ( 'G' | 'g' ) ( 'R' | 'r' ) ( 'E' | 'e' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' )
            {
            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_59"

    // $ANTLR start "KEYWORD_60"
    public final void mKEYWORD_60() throws RecognitionException {
        try {
            int _type = KEYWORD_60;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:53:12: ( ( 'I' | 'i' ) ( 'S' | 's' ) ' ' ( 'N' | 'n' ) ( 'U' | 'u' ) ( 'L' | 'l' ) ( 'L' | 'l' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:53:14: ( 'I' | 'i' ) ( 'S' | 's' ) ' ' ( 'N' | 'n' ) ( 'U' | 'u' ) ( 'L' | 'l' ) ( 'L' | 'l' )
            {
            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            match(' '); 
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_60"

    // $ANTLR start "KEYWORD_61"
    public final void mKEYWORD_61() throws RecognitionException {
        try {
            int _type = KEYWORD_61;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:55:12: ( ( 'N' | 'n' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'U' | 'u' ) ( 'R' | 'r' ) ( 'A' | 'a' ) ( 'L' | 'l' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:55:14: ( 'N' | 'n' ) ( 'A' | 'a' ) ( 'T' | 't' ) ( 'U' | 'u' ) ( 'R' | 'r' ) ( 'A' | 'a' ) ( 'L' | 'l' )
            {
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_61"

    // $ANTLR start "KEYWORD_62"
    public final void mKEYWORD_62() throws RecognitionException {
        try {
            int _type = KEYWORD_62;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:57:12: ( ( 'P' | 'p' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'C' | 'c' ) ( 'E' | 'e' ) ( 'N' | 'n' ) ( 'T' | 't' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:57:14: ( 'P' | 'p' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'C' | 'c' ) ( 'E' | 'e' ) ( 'N' | 'n' ) ( 'T' | 't' )
            {
            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_62"

    // $ANTLR start "KEYWORD_53"
    public final void mKEYWORD_53() throws RecognitionException {
        try {
            int _type = KEYWORD_53;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:59:12: ( ( 'E' | 'e' ) ( 'X' | 'x' ) ( 'C' | 'c' ) ( 'E' | 'e' ) ( 'P' | 'p' ) ( 'T' | 't' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:59:14: ( 'E' | 'e' ) ( 'X' | 'x' ) ( 'C' | 'c' ) ( 'E' | 'e' ) ( 'P' | 'p' ) ( 'T' | 't' )
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_53"

    // $ANTLR start "KEYWORD_54"
    public final void mKEYWORD_54() throws RecognitionException {
        try {
            int _type = KEYWORD_54;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:61:12: ( ( 'H' | 'h' ) ( 'A' | 'a' ) ( 'V' | 'v' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'G' | 'g' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:61:14: ( 'H' | 'h' ) ( 'A' | 'a' ) ( 'V' | 'v' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'G' | 'g' )
            {
            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_54"

    // $ANTLR start "KEYWORD_55"
    public final void mKEYWORD_55() throws RecognitionException {
        try {
            int _type = KEYWORD_55;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:63:12: ( ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'T' | 't' ) ' ' ( 'I' | 'i' ) ( 'N' | 'n' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:63:14: ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'T' | 't' ) ' ' ( 'I' | 'i' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            match(' '); 
            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_55"

    // $ANTLR start "KEYWORD_56"
    public final void mKEYWORD_56() throws RecognitionException {
        try {
            int _type = KEYWORD_56;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:65:12: ( ( 'O' | 'o' ) ( 'F' | 'f' ) ( 'F' | 'f' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'T' | 't' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:65:14: ( 'O' | 'o' ) ( 'F' | 'f' ) ( 'F' | 'f' ) ( 'S' | 's' ) ( 'E' | 'e' ) ( 'T' | 't' )
            {
            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_56"

    // $ANTLR start "KEYWORD_57"
    public final void mKEYWORD_57() throws RecognitionException {
        try {
            int _type = KEYWORD_57;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:67:12: ( ( 'S' | 's' ) ( 'E' | 'e' ) ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'T' | 't' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:67:14: ( 'S' | 's' ) ( 'E' | 'e' ) ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'C' | 'c' ) ( 'T' | 't' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_57"

    // $ANTLR start "KEYWORD_41"
    public final void mKEYWORD_41() throws RecognitionException {
        try {
            int _type = KEYWORD_41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:69:12: ( ( 'C' | 'c' ) ( 'A' | 'a' ) ( 'S' | 's' ) ( 'T' | 't' ) '(' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:69:14: ( 'C' | 'c' ) ( 'A' | 'a' ) ( 'S' | 's' ) ( 'T' | 't' ) '('
            {
            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_41"

    // $ANTLR start "KEYWORD_42"
    public final void mKEYWORD_42() throws RecognitionException {
        try {
            int _type = KEYWORD_42;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:71:12: ( ( 'C' | 'c' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'S' | 's' ) ( 'S' | 's' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:71:14: ( 'C' | 'c' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'S' | 's' ) ( 'S' | 's' )
            {
            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_42"

    // $ANTLR start "KEYWORD_43"
    public final void mKEYWORD_43() throws RecognitionException {
        try {
            int _type = KEYWORD_43;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:73:12: ( ( 'E' | 'e' ) ( 'Q' | 'q' ) ( 'U' | 'u' ) ( 'A' | 'a' ) ( 'L' | 'l' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:73:14: ( 'E' | 'e' ) ( 'Q' | 'q' ) ( 'U' | 'u' ) ( 'A' | 'a' ) ( 'L' | 'l' )
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='Q'||input.LA(1)=='q' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_43"

    // $ANTLR start "KEYWORD_44"
    public final void mKEYWORD_44() throws RecognitionException {
        try {
            int _type = KEYWORD_44;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:75:12: ( ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'N' | 'n' ) ( 'E' | 'e' ) ( 'R' | 'r' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:75:14: ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'N' | 'n' ) ( 'E' | 'e' ) ( 'R' | 'r' )
            {
            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_44"

    // $ANTLR start "KEYWORD_45"
    public final void mKEYWORD_45() throws RecognitionException {
        try {
            int _type = KEYWORD_45;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:77:12: ( ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'S' | 's' ) ( 'S' | 's' ) ']' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:77:14: ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'S' | 's' ) ( 'S' | 's' ) ']'
            {
            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_45"

    // $ANTLR start "KEYWORD_46"
    public final void mKEYWORD_46() throws RecognitionException {
        try {
            int _type = KEYWORD_46;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:79:12: ( ( 'L' | 'l' ) ( 'I' | 'i' ) ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'T' | 't' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:79:14: ( 'L' | 'l' ) ( 'I' | 'i' ) ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'T' | 't' )
            {
            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_46"

    // $ANTLR start "KEYWORD_47"
    public final void mKEYWORD_47() throws RecognitionException {
        try {
            int _type = KEYWORD_47;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:81:12: ( ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'U' | 'u' ) ( 'S' | 's' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:81:14: ( 'M' | 'm' ) ( 'I' | 'i' ) ( 'N' | 'n' ) ( 'U' | 'u' ) ( 'S' | 's' )
            {
            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_47"

    // $ANTLR start "KEYWORD_48"
    public final void mKEYWORD_48() throws RecognitionException {
        try {
            int _type = KEYWORD_48;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:83:12: ( ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'N' | 'n' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:83:14: ( 'N' | 'n' ) ( 'O' | 'o' ) ( 'T' | 't' ) ( 'I' | 'i' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_48"

    // $ANTLR start "KEYWORD_49"
    public final void mKEYWORD_49() throws RecognitionException {
        try {
            int _type = KEYWORD_49;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:85:12: ( ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:85:14: ( 'O' | 'o' ) ( 'U' | 'u' ) ( 'T' | 't' ) ( 'E' | 'e' ) ( 'R' | 'r' )
            {
            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_49"

    // $ANTLR start "KEYWORD_50"
    public final void mKEYWORD_50() throws RecognitionException {
        try {
            int _type = KEYWORD_50;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:87:12: ( ( 'R' | 'r' ) ( 'I' | 'i' ) ( 'G' | 'g' ) ( 'H' | 'h' ) ( 'T' | 't' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:87:14: ( 'R' | 'r' ) ( 'I' | 'i' ) ( 'G' | 'g' ) ( 'H' | 'h' ) ( 'T' | 't' )
            {
            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_50"

    // $ANTLR start "KEYWORD_51"
    public final void mKEYWORD_51() throws RecognitionException {
        try {
            int _type = KEYWORD_51;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:89:12: ( ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:89:14: ( 'U' | 'u' ) ( 'N' | 'n' ) ( 'I' | 'i' ) ( 'O' | 'o' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_51"

    // $ANTLR start "KEYWORD_52"
    public final void mKEYWORD_52() throws RecognitionException {
        try {
            int _type = KEYWORD_52;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:91:12: ( ( 'W' | 'w' ) ( 'H' | 'h' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'E' | 'e' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:91:14: ( 'W' | 'w' ) ( 'H' | 'h' ) ( 'E' | 'e' ) ( 'R' | 'r' ) ( 'E' | 'e' )
            {
            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_52"

    // $ANTLR start "KEYWORD_29"
    public final void mKEYWORD_29() throws RecognitionException {
        try {
            int _type = KEYWORD_29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:93:12: ( ( 'C' | 'c' ) ( 'A' | 'a' ) ( 'S' | 's' ) ( 'E' | 'e' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:93:14: ( 'C' | 'c' ) ( 'A' | 'a' ) ( 'S' | 's' ) ( 'E' | 'e' )
            {
            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_29"

    // $ANTLR start "KEYWORD_30"
    public final void mKEYWORD_30() throws RecognitionException {
        try {
            int _type = KEYWORD_30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:95:12: ( ( 'D' | 'd' ) ( 'E' | 'e' ) ( 'S' | 's' ) ( 'C' | 'c' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:95:14: ( 'D' | 'd' ) ( 'E' | 'e' ) ( 'S' | 's' ) ( 'C' | 'c' )
            {
            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_30"

    // $ANTLR start "KEYWORD_31"
    public final void mKEYWORD_31() throws RecognitionException {
        try {
            int _type = KEYWORD_31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:97:12: ( ( 'E' | 'e' ) ( 'L' | 'l' ) ( 'S' | 's' ) ( 'E' | 'e' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:97:14: ( 'E' | 'e' ) ( 'L' | 'l' ) ( 'S' | 's' ) ( 'E' | 'e' )
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_31"

    // $ANTLR start "KEYWORD_32"
    public final void mKEYWORD_32() throws RecognitionException {
        try {
            int _type = KEYWORD_32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:99:12: ( ( 'F' | 'f' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'M' | 'm' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:99:14: ( 'F' | 'f' ) ( 'R' | 'r' ) ( 'O' | 'o' ) ( 'M' | 'm' )
            {
            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_32"

    // $ANTLR start "KEYWORD_33"
    public final void mKEYWORD_33() throws RecognitionException {
        try {
            int _type = KEYWORD_33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:101:12: ( ( 'F' | 'f' ) ( 'U' | 'u' ) ( 'L' | 'l' ) ( 'L' | 'l' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:101:14: ( 'F' | 'f' ) ( 'U' | 'u' ) ( 'L' | 'l' ) ( 'L' | 'l' )
            {
            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_33"

    // $ANTLR start "KEYWORD_34"
    public final void mKEYWORD_34() throws RecognitionException {
        try {
            int _type = KEYWORD_34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:103:12: ( ( 'J' | 'j' ) ( 'O' | 'o' ) ( 'I' | 'i' ) ( 'N' | 'n' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:103:14: ( 'J' | 'j' ) ( 'O' | 'o' ) ( 'I' | 'i' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='J'||input.LA(1)=='j' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_34"

    // $ANTLR start "KEYWORD_35"
    public final void mKEYWORD_35() throws RecognitionException {
        try {
            int _type = KEYWORD_35;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:105:12: ( ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'F' | 'f' ) ( 'T' | 't' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:105:14: ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'F' | 'f' ) ( 'T' | 't' )
            {
            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_35"

    // $ANTLR start "KEYWORD_36"
    public final void mKEYWORD_36() throws RecognitionException {
        try {
            int _type = KEYWORD_36;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:107:12: ( ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'S' | 's' ) ( 'S' | 's' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:107:14: ( 'L' | 'l' ) ( 'E' | 'e' ) ( 'S' | 's' ) ( 'S' | 's' )
            {
            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_36"

    // $ANTLR start "KEYWORD_37"
    public final void mKEYWORD_37() throws RecognitionException {
        try {
            int _type = KEYWORD_37;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:109:12: ( ( 'L' | 'l' ) ( 'I' | 'i' ) ( 'K' | 'k' ) ( 'E' | 'e' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:109:14: ( 'L' | 'l' ) ( 'I' | 'i' ) ( 'K' | 'k' ) ( 'E' | 'e' )
            {
            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_37"

    // $ANTLR start "KEYWORD_38"
    public final void mKEYWORD_38() throws RecognitionException {
        try {
            int _type = KEYWORD_38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:111:12: ( ( 'S' | 's' ) ( 'O' | 'o' ) ( 'M' | 'm' ) ( 'E' | 'e' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:111:14: ( 'S' | 's' ) ( 'O' | 'o' ) ( 'M' | 'm' ) ( 'E' | 'e' )
            {
            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_38"

    // $ANTLR start "KEYWORD_39"
    public final void mKEYWORD_39() throws RecognitionException {
        try {
            int _type = KEYWORD_39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:113:12: ( ( 'T' | 't' ) ( 'H' | 'h' ) ( 'E' | 'e' ) ( 'N' | 'n' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:113:14: ( 'T' | 't' ) ( 'H' | 'h' ) ( 'E' | 'e' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_39"

    // $ANTLR start "KEYWORD_40"
    public final void mKEYWORD_40() throws RecognitionException {
        try {
            int _type = KEYWORD_40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:115:12: ( ( 'W' | 'w' ) ( 'H' | 'h' ) ( 'E' | 'e' ) ( 'N' | 'n' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:115:14: ( 'W' | 'w' ) ( 'H' | 'h' ) ( 'E' | 'e' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_40"

    // $ANTLR start "KEYWORD_23"
    public final void mKEYWORD_23() throws RecognitionException {
        try {
            int _type = KEYWORD_23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:117:12: ( ( 'A' | 'a' ) ( 'L' | 'l' ) ( 'L' | 'l' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:117:14: ( 'A' | 'a' ) ( 'L' | 'l' ) ( 'L' | 'l' )
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_23"

    // $ANTLR start "KEYWORD_24"
    public final void mKEYWORD_24() throws RecognitionException {
        try {
            int _type = KEYWORD_24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:119:12: ( ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'D' | 'd' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:119:14: ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'D' | 'd' )
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_24"

    // $ANTLR start "KEYWORD_25"
    public final void mKEYWORD_25() throws RecognitionException {
        try {
            int _type = KEYWORD_25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:121:12: ( ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'Y' | 'y' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:121:14: ( 'A' | 'a' ) ( 'N' | 'n' ) ( 'Y' | 'y' )
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_25"

    // $ANTLR start "KEYWORD_26"
    public final void mKEYWORD_26() throws RecognitionException {
        try {
            int _type = KEYWORD_26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:123:12: ( ( 'A' | 'a' ) ( 'S' | 's' ) ( 'C' | 'c' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:123:14: ( 'A' | 'a' ) ( 'S' | 's' ) ( 'C' | 'c' )
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_26"

    // $ANTLR start "KEYWORD_27"
    public final void mKEYWORD_27() throws RecognitionException {
        try {
            int _type = KEYWORD_27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:125:12: ( ( 'E' | 'e' ) ( 'N' | 'n' ) ( 'D' | 'd' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:125:14: ( 'E' | 'e' ) ( 'N' | 'n' ) ( 'D' | 'd' )
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_27"

    // $ANTLR start "KEYWORD_28"
    public final void mKEYWORD_28() throws RecognitionException {
        try {
            int _type = KEYWORD_28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:127:12: ( ( 'T' | 't' ) ( 'O' | 'o' ) ( 'P' | 'p' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:127:14: ( 'T' | 't' ) ( 'O' | 'o' ) ( 'P' | 'p' )
            {
            if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_28"

    // $ANTLR start "KEYWORD_14"
    public final void mKEYWORD_14() throws RecognitionException {
        try {
            int _type = KEYWORD_14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:129:12: ( '$' ( 'X' | 'x' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:129:14: '$' ( 'X' | 'x' )
            {
            match('$'); 
            if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_14"

    // $ANTLR start "KEYWORD_15"
    public final void mKEYWORD_15() throws RecognitionException {
        try {
            int _type = KEYWORD_15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:131:12: ( '<' '=' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:131:14: '<' '='
            {
            match('<'); 
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_15"

    // $ANTLR start "KEYWORD_16"
    public final void mKEYWORD_16() throws RecognitionException {
        try {
            int _type = KEYWORD_16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:133:12: ( '<' '>' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:133:14: '<' '>'
            {
            match('<'); 
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_16"

    // $ANTLR start "KEYWORD_17"
    public final void mKEYWORD_17() throws RecognitionException {
        try {
            int _type = KEYWORD_17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:135:12: ( '>' '=' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:135:14: '>' '='
            {
            match('>'); 
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_17"

    // $ANTLR start "KEYWORD_18"
    public final void mKEYWORD_18() throws RecognitionException {
        try {
            int _type = KEYWORD_18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:137:12: ( ( 'A' | 'a' ) ( 'S' | 's' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:137:14: ( 'A' | 'a' ) ( 'S' | 's' )
            {
            if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_18"

    // $ANTLR start "KEYWORD_19"
    public final void mKEYWORD_19() throws RecognitionException {
        try {
            int _type = KEYWORD_19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:139:12: ( ( 'I' | 'i' ) ( 'N' | 'n' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:139:14: ( 'I' | 'i' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_19"

    // $ANTLR start "KEYWORD_20"
    public final void mKEYWORD_20() throws RecognitionException {
        try {
            int _type = KEYWORD_20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:141:12: ( ( 'O' | 'o' ) ( 'N' | 'n' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:141:14: ( 'O' | 'o' ) ( 'N' | 'n' )
            {
            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_20"

    // $ANTLR start "KEYWORD_21"
    public final void mKEYWORD_21() throws RecognitionException {
        try {
            int _type = KEYWORD_21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:143:12: ( ( 'O' | 'o' ) ( 'R' | 'r' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:143:14: ( 'O' | 'o' ) ( 'R' | 'r' )
            {
            if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_21"

    // $ANTLR start "KEYWORD_22"
    public final void mKEYWORD_22() throws RecognitionException {
        try {
            int _type = KEYWORD_22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:145:12: ( '|' '|' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:145:14: '|' '|'
            {
            match('|'); 
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_22"

    // $ANTLR start "KEYWORD_1"
    public final void mKEYWORD_1() throws RecognitionException {
        try {
            int _type = KEYWORD_1;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:147:11: ( '(' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:147:13: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_1"

    // $ANTLR start "KEYWORD_2"
    public final void mKEYWORD_2() throws RecognitionException {
        try {
            int _type = KEYWORD_2;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:149:11: ( ')' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:149:13: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_2"

    // $ANTLR start "KEYWORD_3"
    public final void mKEYWORD_3() throws RecognitionException {
        try {
            int _type = KEYWORD_3;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:151:11: ( '+' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:151:13: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_3"

    // $ANTLR start "KEYWORD_4"
    public final void mKEYWORD_4() throws RecognitionException {
        try {
            int _type = KEYWORD_4;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:153:11: ( ',' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:153:13: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_4"

    // $ANTLR start "KEYWORD_5"
    public final void mKEYWORD_5() throws RecognitionException {
        try {
            int _type = KEYWORD_5;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:155:11: ( '-' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:155:13: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_5"

    // $ANTLR start "KEYWORD_6"
    public final void mKEYWORD_6() throws RecognitionException {
        try {
            int _type = KEYWORD_6;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:157:11: ( '.' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:157:13: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_6"

    // $ANTLR start "KEYWORD_7"
    public final void mKEYWORD_7() throws RecognitionException {
        try {
            int _type = KEYWORD_7;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:159:11: ( '/' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:159:13: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_7"

    // $ANTLR start "KEYWORD_8"
    public final void mKEYWORD_8() throws RecognitionException {
        try {
            int _type = KEYWORD_8;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:161:11: ( '<' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:161:13: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_8"

    // $ANTLR start "KEYWORD_9"
    public final void mKEYWORD_9() throws RecognitionException {
        try {
            int _type = KEYWORD_9;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:163:11: ( '=' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:163:13: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_9"

    // $ANTLR start "KEYWORD_10"
    public final void mKEYWORD_10() throws RecognitionException {
        try {
            int _type = KEYWORD_10;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:165:12: ( '>' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:165:14: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_10"

    // $ANTLR start "KEYWORD_11"
    public final void mKEYWORD_11() throws RecognitionException {
        try {
            int _type = KEYWORD_11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:167:12: ( '{' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:167:14: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_11"

    // $ANTLR start "KEYWORD_12"
    public final void mKEYWORD_12() throws RecognitionException {
        try {
            int _type = KEYWORD_12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:169:12: ( '|' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:169:14: '|'
            {
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_12"

    // $ANTLR start "KEYWORD_13"
    public final void mKEYWORD_13() throws RecognitionException {
        try {
            int _type = KEYWORD_13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:171:12: ( '}' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:171:14: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "KEYWORD_13"

    // $ANTLR start "RULE_JRPARAM"
    public final void mRULE_JRPARAM() throws RecognitionException {
        try {
            int _type = RULE_JRPARAM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:175:14: ( '$P{' ( options {greedy=false; } : . )* '}' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:175:16: '$P{' ( options {greedy=false; } : . )* '}'
            {
            match("$P{"); 

            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:175:22: ( options {greedy=false; } : . )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='}') ) {
                    alt1=2;
                }
                else if ( ((LA1_0>='\u0000' && LA1_0<='|')||(LA1_0>='~' && LA1_0<='\uFFFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:175:50: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_JRPARAM"

    // $ANTLR start "RULE_JRNPARAM"
    public final void mRULE_JRNPARAM() throws RecognitionException {
        try {
            int _type = RULE_JRNPARAM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:177:15: ( '$P!{' ( options {greedy=false; } : . )* '}' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:177:17: '$P!{' ( options {greedy=false; } : . )* '}'
            {
            match("$P!{"); 

            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:177:24: ( options {greedy=false; } : . )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='}') ) {
                    alt2=2;
                }
                else if ( ((LA2_0>='\u0000' && LA2_0<='|')||(LA2_0>='~' && LA2_0<='\uFFFF')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:177:52: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_JRNPARAM"

    // $ANTLR start "RULE_STAR"
    public final void mRULE_STAR() throws RecognitionException {
        try {
            int _type = RULE_STAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:179:11: ( '*' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:179:13: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_STAR"

    // $ANTLR start "RULE_INT"
    public final void mRULE_INT() throws RecognitionException {
        try {
            int _type = RULE_INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:181:10: ( ( '-' )? ( '0' .. '9' )+ )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:181:12: ( '-' )? ( '0' .. '9' )+
            {
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:181:12: ( '-' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='-') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:181:12: '-'
                    {
                    match('-'); 

                    }
                    break;

            }

            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:181:17: ( '0' .. '9' )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:181:18: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_INT"

    // $ANTLR start "RULE_TIMESTAMP"
    public final void mRULE_TIMESTAMP() throws RecognitionException {
        try {
            int _type = RULE_TIMESTAMP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:183:16: ( RULE_DATE ' ' RULE_TIME )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:183:18: RULE_DATE ' ' RULE_TIME
            {
            mRULE_DATE(); 
            match(' '); 
            mRULE_TIME(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_TIMESTAMP"

    // $ANTLR start "RULE_DATE"
    public final void mRULE_DATE() throws RecognitionException {
        try {
            int _type = RULE_DATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:185:11: ( '\\'' '0' .. '9' '0' .. '9' '0' .. '9' '0' .. '9' '-' '0' .. '1' '0' .. '9' '-' '0' .. '3' '0' .. '9' '\\'' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:185:13: '\\'' '0' .. '9' '0' .. '9' '0' .. '9' '0' .. '9' '-' '0' .. '1' '0' .. '9' '-' '0' .. '3' '0' .. '9' '\\''
            {
            match('\''); 
            matchRange('0','9'); 
            matchRange('0','9'); 
            matchRange('0','9'); 
            matchRange('0','9'); 
            match('-'); 
            matchRange('0','1'); 
            matchRange('0','9'); 
            match('-'); 
            matchRange('0','3'); 
            matchRange('0','9'); 
            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_DATE"

    // $ANTLR start "RULE_TIME"
    public final void mRULE_TIME() throws RecognitionException {
        try {
            int _type = RULE_TIME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:187:11: ( '\\'' '0' .. '9' '0' .. '9' ':' '0' .. '9' '0' .. '9' ':' '0' .. '1' '0' .. '9' '.' '0' .. '9' '0' .. '9' '0' .. '9' '\\'' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:187:13: '\\'' '0' .. '9' '0' .. '9' ':' '0' .. '9' '0' .. '9' ':' '0' .. '1' '0' .. '9' '.' '0' .. '9' '0' .. '9' '0' .. '9' '\\''
            {
            match('\''); 
            matchRange('0','9'); 
            matchRange('0','9'); 
            match(':'); 
            matchRange('0','9'); 
            matchRange('0','9'); 
            match(':'); 
            matchRange('0','1'); 
            matchRange('0','9'); 
            match('.'); 
            matchRange('0','9'); 
            matchRange('0','9'); 
            matchRange('0','9'); 
            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_TIME"

    // $ANTLR start "RULE_SIGNED_DOUBLE"
    public final void mRULE_SIGNED_DOUBLE() throws RecognitionException {
        try {
            int _type = RULE_SIGNED_DOUBLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:189:20: ( ( '-' )? ( '0' .. '9' )+ ( '.' ( '0' .. '9' )+ )? )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:189:22: ( '-' )? ( '0' .. '9' )+ ( '.' ( '0' .. '9' )+ )?
            {
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:189:22: ( '-' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='-') ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:189:22: '-'
                    {
                    match('-'); 

                    }
                    break;

            }

            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:189:27: ( '0' .. '9' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:189:28: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);

            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:189:39: ( '.' ( '0' .. '9' )+ )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='.') ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:189:40: '.' ( '0' .. '9' )+
                    {
                    match('.'); 
                    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:189:44: ( '0' .. '9' )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0>='0' && LA7_0<='9')) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:189:45: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt7 >= 1 ) break loop7;
                                EarlyExitException eee =
                                    new EarlyExitException(7, input);
                                throw eee;
                        }
                        cnt7++;
                    } while (true);


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_SIGNED_DOUBLE"

    // $ANTLR start "RULE_STRING_"
    public final void mRULE_STRING_() throws RecognitionException {
        try {
            int _type = RULE_STRING_;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:191:14: ( '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\'' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:191:16: '\\'' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )* '\\''
            {
            match('\''); 
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:191:21: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\\'' ) ) )*
            loop9:
            do {
                int alt9=3;
                int LA9_0 = input.LA(1);

                if ( (LA9_0=='\\') ) {
                    alt9=1;
                }
                else if ( ((LA9_0>='\u0000' && LA9_0<='&')||(LA9_0>='(' && LA9_0<='[')||(LA9_0>=']' && LA9_0<='\uFFFF')) ) {
                    alt9=2;
                }


                switch (alt9) {
            	case 1 :
            	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:191:22: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' )
            	    {
            	    match('\\'); 
            	    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||(input.LA(1)>='t' && input.LA(1)<='u') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;
            	case 2 :
            	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:191:67: ~ ( ( '\\\\' | '\\'' ) )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_STRING_"

    // $ANTLR start "RULE_STRING"
    public final void mRULE_STRING() throws RecognitionException {
        try {
            int _type = RULE_STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:193:13: ( '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:193:15: '\"' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )* '\"'
            {
            match('\"'); 
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:193:19: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '\"' ) ) )*
            loop10:
            do {
                int alt10=3;
                int LA10_0 = input.LA(1);

                if ( (LA10_0=='\\') ) {
                    alt10=1;
                }
                else if ( ((LA10_0>='\u0000' && LA10_0<='!')||(LA10_0>='#' && LA10_0<='[')||(LA10_0>=']' && LA10_0<='\uFFFF')) ) {
                    alt10=2;
                }


                switch (alt10) {
            	case 1 :
            	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:193:20: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' )
            	    {
            	    match('\\'); 
            	    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||(input.LA(1)>='t' && input.LA(1)<='u') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;
            	case 2 :
            	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:193:65: ~ ( ( '\\\\' | '\"' ) )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_STRING"

    // $ANTLR start "RULE_DBNAME"
    public final void mRULE_DBNAME() throws RecognitionException {
        try {
            int _type = RULE_DBNAME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:195:13: ( ( '`' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '`' ) ) )* '`' | '[' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | ']' ) ) )* ']' ) )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:195:15: ( '`' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '`' ) ) )* '`' | '[' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | ']' ) ) )* ']' )
            {
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:195:15: ( '`' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '`' ) ) )* '`' | '[' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | ']' ) ) )* ']' )
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0=='`') ) {
                alt13=1;
            }
            else if ( (LA13_0=='[') ) {
                alt13=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }
            switch (alt13) {
                case 1 :
                    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:195:16: '`' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '`' ) ) )* '`'
                    {
                    match('`'); 
                    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:195:20: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | '`' ) ) )*
                    loop11:
                    do {
                        int alt11=3;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0=='\\') ) {
                            alt11=1;
                        }
                        else if ( ((LA11_0>='\u0000' && LA11_0<='[')||(LA11_0>=']' && LA11_0<='_')||(LA11_0>='a' && LA11_0<='\uFFFF')) ) {
                            alt11=2;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:195:21: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' )
                    	    {
                    	    match('\\'); 
                    	    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||(input.LA(1)>='t' && input.LA(1)<='u') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;
                    	case 2 :
                    	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:195:66: ~ ( ( '\\\\' | '`' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='_')||(input.LA(1)>='a' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop11;
                        }
                    } while (true);

                    match('`'); 

                    }
                    break;
                case 2 :
                    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:195:86: '[' ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | ']' ) ) )* ']'
                    {
                    match('['); 
                    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:195:90: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' ) | ~ ( ( '\\\\' | ']' ) ) )*
                    loop12:
                    do {
                        int alt12=3;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0=='\\') ) {
                            alt12=1;
                        }
                        else if ( ((LA12_0>='\u0000' && LA12_0<='[')||(LA12_0>='^' && LA12_0<='\uFFFF')) ) {
                            alt12=2;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:195:91: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | 'u' | '\"' | '\\'' | '\\\\' )
                    	    {
                    	    match('\\'); 
                    	    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||(input.LA(1)>='t' && input.LA(1)<='u') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;
                    	case 2 :
                    	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:195:136: ~ ( ( '\\\\' | ']' ) )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='[')||(input.LA(1)>='^' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);

                    match(']'); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_DBNAME"

    // $ANTLR start "RULE_ID"
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:197:9: ( ( 'a' .. 'z' | 'A' .. 'Z' | '\\u00C0' .. '\\u00FF' | '\\u0100' .. '\\u017F' | '\\u0180' .. '\\u024F' | '\\u0410' .. '\\u044F' | '_' | '-' | '\\u3041' .. '\\u309F' | '\\u30A0' .. '\\u30FF' | '\\u31F0' .. '\\u31FF' | '\\u4E00' .. '\\u9FFF' | '\\u6B74' .. '\\u3059' | '\\u30A2' .. '\\u30F3' | '\\uF900' .. '\\uFAFF' | '\\u3400' .. '\\u4DBF' | '0' .. '9' )* )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:197:11: ( 'a' .. 'z' | 'A' .. 'Z' | '\\u00C0' .. '\\u00FF' | '\\u0100' .. '\\u017F' | '\\u0180' .. '\\u024F' | '\\u0410' .. '\\u044F' | '_' | '-' | '\\u3041' .. '\\u309F' | '\\u30A0' .. '\\u30FF' | '\\u31F0' .. '\\u31FF' | '\\u4E00' .. '\\u9FFF' | '\\u6B74' .. '\\u3059' | '\\u30A2' .. '\\u30F3' | '\\uF900' .. '\\uFAFF' | '\\u3400' .. '\\u4DBF' | '0' .. '9' )*
            {
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:197:11: ( 'a' .. 'z' | 'A' .. 'Z' | '\\u00C0' .. '\\u00FF' | '\\u0100' .. '\\u017F' | '\\u0180' .. '\\u024F' | '\\u0410' .. '\\u044F' | '_' | '-' | '\\u3041' .. '\\u309F' | '\\u30A0' .. '\\u30FF' | '\\u31F0' .. '\\u31FF' | '\\u4E00' .. '\\u9FFF' | '\\u6B74' .. '\\u3059' | '\\u30A2' .. '\\u30F3' | '\\uF900' .. '\\uFAFF' | '\\u3400' .. '\\u4DBF' | '0' .. '9' )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0=='-'||(LA14_0>='0' && LA14_0<='9')||(LA14_0>='A' && LA14_0<='Z')||LA14_0=='_'||(LA14_0>='a' && LA14_0<='z')||(LA14_0>='\u00C0' && LA14_0<='\u024F')||(LA14_0>='\u0410' && LA14_0<='\u044F')||(LA14_0>='\u3041' && LA14_0<='\u30FF')||(LA14_0>='\u31F0' && LA14_0<='\u31FF')||(LA14_0>='\u3400' && LA14_0<='\u4DBF')||(LA14_0>='\u4E00' && LA14_0<='\u9FFF')||(LA14_0>='\uF900' && LA14_0<='\uFAFF')) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u024F')||(input.LA(1)>='\u0410' && input.LA(1)<='\u044F')||(input.LA(1)>='\u3041' && input.LA(1)<='\u30FF')||(input.LA(1)>='\u31F0' && input.LA(1)<='\u31FF')||(input.LA(1)>='\u3400' && input.LA(1)<='\u4DBF')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FFF')||(input.LA(1)>='\uF900' && input.LA(1)<='\uFAFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ID"

    // $ANTLR start "RULE_SL_COMMENT"
    public final void mRULE_SL_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_SL_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:199:17: ( ( '--' | '#' | '//' ) (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:199:19: ( '--' | '#' | '//' ) (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:199:19: ( '--' | '#' | '//' )
            int alt15=3;
            switch ( input.LA(1) ) {
            case '-':
                {
                alt15=1;
                }
                break;
            case '#':
                {
                alt15=2;
                }
                break;
            case '/':
                {
                alt15=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;
            }

            switch (alt15) {
                case 1 :
                    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:199:20: '--'
                    {
                    match("--"); 


                    }
                    break;
                case 2 :
                    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:199:25: '#'
                    {
                    match('#'); 

                    }
                    break;
                case 3 :
                    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:199:29: '//'
                    {
                    match("//"); 


                    }
                    break;

            }

            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:199:35: (~ ( ( '\\n' | '\\r' ) ) )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( ((LA16_0>='\u0000' && LA16_0<='\t')||(LA16_0>='\u000B' && LA16_0<='\f')||(LA16_0>='\u000E' && LA16_0<='\uFFFF')) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:199:35: ~ ( ( '\\n' | '\\r' ) )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:199:51: ( ( '\\r' )? '\\n' )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0=='\n'||LA18_0=='\r') ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:199:52: ( '\\r' )? '\\n'
                    {
                    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:199:52: ( '\\r' )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0=='\r') ) {
                        alt17=1;
                    }
                    switch (alt17) {
                        case 1 :
                            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:199:52: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }

                    match('\n'); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_SL_COMMENT"

    // $ANTLR start "RULE_ML_COMMENT"
    public final void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:201:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:201:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:201:24: ( options {greedy=false; } : . )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0=='*') ) {
                    int LA19_1 = input.LA(2);

                    if ( (LA19_1=='/') ) {
                        alt19=2;
                    }
                    else if ( ((LA19_1>='\u0000' && LA19_1<='.')||(LA19_1>='0' && LA19_1<='\uFFFF')) ) {
                        alt19=1;
                    }


                }
                else if ( ((LA19_0>='\u0000' && LA19_0<=')')||(LA19_0>='+' && LA19_0<='\uFFFF')) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:201:52: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);

            match("*/"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ML_COMMENT"

    // $ANTLR start "RULE_WS"
    public final void mRULE_WS() throws RecognitionException {
        try {
            int _type = RULE_WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:203:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:203:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:203:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt20=0;
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( ((LA20_0>='\t' && LA20_0<='\n')||LA20_0=='\r'||LA20_0==' ') ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt20 >= 1 ) break loop20;
                        EarlyExitException eee =
                            new EarlyExitException(20, input);
                        throw eee;
                }
                cnt20++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_WS"

    // $ANTLR start "RULE_ANY_OTHER"
    public final void mRULE_ANY_OTHER() throws RecognitionException {
        try {
            int _type = RULE_ANY_OTHER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:205:16: ( . )
            // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:205:18: .
            {
            matchAny(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_ANY_OTHER"

    public void mTokens() throws RecognitionException {
        // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:8: ( KEYWORD_75 | KEYWORD_76 | KEYWORD_77 | KEYWORD_71 | KEYWORD_72 | KEYWORD_73 | KEYWORD_74 | KEYWORD_63 | KEYWORD_64 | KEYWORD_65 | KEYWORD_66 | KEYWORD_67 | KEYWORD_68 | KEYWORD_69 | KEYWORD_70 | KEYWORD_58 | KEYWORD_59 | KEYWORD_60 | KEYWORD_61 | KEYWORD_62 | KEYWORD_53 | KEYWORD_54 | KEYWORD_55 | KEYWORD_56 | KEYWORD_57 | KEYWORD_41 | KEYWORD_42 | KEYWORD_43 | KEYWORD_44 | KEYWORD_45 | KEYWORD_46 | KEYWORD_47 | KEYWORD_48 | KEYWORD_49 | KEYWORD_50 | KEYWORD_51 | KEYWORD_52 | KEYWORD_29 | KEYWORD_30 | KEYWORD_31 | KEYWORD_32 | KEYWORD_33 | KEYWORD_34 | KEYWORD_35 | KEYWORD_36 | KEYWORD_37 | KEYWORD_38 | KEYWORD_39 | KEYWORD_40 | KEYWORD_23 | KEYWORD_24 | KEYWORD_25 | KEYWORD_26 | KEYWORD_27 | KEYWORD_28 | KEYWORD_14 | KEYWORD_15 | KEYWORD_16 | KEYWORD_17 | KEYWORD_18 | KEYWORD_19 | KEYWORD_20 | KEYWORD_21 | KEYWORD_22 | KEYWORD_1 | KEYWORD_2 | KEYWORD_3 | KEYWORD_4 | KEYWORD_5 | KEYWORD_6 | KEYWORD_7 | KEYWORD_8 | KEYWORD_9 | KEYWORD_10 | KEYWORD_11 | KEYWORD_12 | KEYWORD_13 | RULE_JRPARAM | RULE_JRNPARAM | RULE_STAR | RULE_INT | RULE_TIMESTAMP | RULE_DATE | RULE_TIME | RULE_SIGNED_DOUBLE | RULE_STRING_ | RULE_STRING | RULE_DBNAME | RULE_ID | RULE_SL_COMMENT | RULE_ML_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt21=93;
        alt21 = dfa21.predict(input);
        switch (alt21) {
            case 1 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:10: KEYWORD_75
                {
                mKEYWORD_75(); 

                }
                break;
            case 2 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:21: KEYWORD_76
                {
                mKEYWORD_76(); 

                }
                break;
            case 3 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:32: KEYWORD_77
                {
                mKEYWORD_77(); 

                }
                break;
            case 4 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:43: KEYWORD_71
                {
                mKEYWORD_71(); 

                }
                break;
            case 5 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:54: KEYWORD_72
                {
                mKEYWORD_72(); 

                }
                break;
            case 6 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:65: KEYWORD_73
                {
                mKEYWORD_73(); 

                }
                break;
            case 7 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:76: KEYWORD_74
                {
                mKEYWORD_74(); 

                }
                break;
            case 8 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:87: KEYWORD_63
                {
                mKEYWORD_63(); 

                }
                break;
            case 9 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:98: KEYWORD_64
                {
                mKEYWORD_64(); 

                }
                break;
            case 10 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:109: KEYWORD_65
                {
                mKEYWORD_65(); 

                }
                break;
            case 11 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:120: KEYWORD_66
                {
                mKEYWORD_66(); 

                }
                break;
            case 12 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:131: KEYWORD_67
                {
                mKEYWORD_67(); 

                }
                break;
            case 13 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:142: KEYWORD_68
                {
                mKEYWORD_68(); 

                }
                break;
            case 14 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:153: KEYWORD_69
                {
                mKEYWORD_69(); 

                }
                break;
            case 15 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:164: KEYWORD_70
                {
                mKEYWORD_70(); 

                }
                break;
            case 16 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:175: KEYWORD_58
                {
                mKEYWORD_58(); 

                }
                break;
            case 17 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:186: KEYWORD_59
                {
                mKEYWORD_59(); 

                }
                break;
            case 18 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:197: KEYWORD_60
                {
                mKEYWORD_60(); 

                }
                break;
            case 19 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:208: KEYWORD_61
                {
                mKEYWORD_61(); 

                }
                break;
            case 20 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:219: KEYWORD_62
                {
                mKEYWORD_62(); 

                }
                break;
            case 21 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:230: KEYWORD_53
                {
                mKEYWORD_53(); 

                }
                break;
            case 22 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:241: KEYWORD_54
                {
                mKEYWORD_54(); 

                }
                break;
            case 23 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:252: KEYWORD_55
                {
                mKEYWORD_55(); 

                }
                break;
            case 24 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:263: KEYWORD_56
                {
                mKEYWORD_56(); 

                }
                break;
            case 25 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:274: KEYWORD_57
                {
                mKEYWORD_57(); 

                }
                break;
            case 26 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:285: KEYWORD_41
                {
                mKEYWORD_41(); 

                }
                break;
            case 27 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:296: KEYWORD_42
                {
                mKEYWORD_42(); 

                }
                break;
            case 28 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:307: KEYWORD_43
                {
                mKEYWORD_43(); 

                }
                break;
            case 29 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:318: KEYWORD_44
                {
                mKEYWORD_44(); 

                }
                break;
            case 30 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:329: KEYWORD_45
                {
                mKEYWORD_45(); 

                }
                break;
            case 31 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:340: KEYWORD_46
                {
                mKEYWORD_46(); 

                }
                break;
            case 32 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:351: KEYWORD_47
                {
                mKEYWORD_47(); 

                }
                break;
            case 33 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:362: KEYWORD_48
                {
                mKEYWORD_48(); 

                }
                break;
            case 34 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:373: KEYWORD_49
                {
                mKEYWORD_49(); 

                }
                break;
            case 35 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:384: KEYWORD_50
                {
                mKEYWORD_50(); 

                }
                break;
            case 36 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:395: KEYWORD_51
                {
                mKEYWORD_51(); 

                }
                break;
            case 37 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:406: KEYWORD_52
                {
                mKEYWORD_52(); 

                }
                break;
            case 38 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:417: KEYWORD_29
                {
                mKEYWORD_29(); 

                }
                break;
            case 39 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:428: KEYWORD_30
                {
                mKEYWORD_30(); 

                }
                break;
            case 40 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:439: KEYWORD_31
                {
                mKEYWORD_31(); 

                }
                break;
            case 41 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:450: KEYWORD_32
                {
                mKEYWORD_32(); 

                }
                break;
            case 42 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:461: KEYWORD_33
                {
                mKEYWORD_33(); 

                }
                break;
            case 43 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:472: KEYWORD_34
                {
                mKEYWORD_34(); 

                }
                break;
            case 44 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:483: KEYWORD_35
                {
                mKEYWORD_35(); 

                }
                break;
            case 45 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:494: KEYWORD_36
                {
                mKEYWORD_36(); 

                }
                break;
            case 46 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:505: KEYWORD_37
                {
                mKEYWORD_37(); 

                }
                break;
            case 47 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:516: KEYWORD_38
                {
                mKEYWORD_38(); 

                }
                break;
            case 48 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:527: KEYWORD_39
                {
                mKEYWORD_39(); 

                }
                break;
            case 49 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:538: KEYWORD_40
                {
                mKEYWORD_40(); 

                }
                break;
            case 50 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:549: KEYWORD_23
                {
                mKEYWORD_23(); 

                }
                break;
            case 51 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:560: KEYWORD_24
                {
                mKEYWORD_24(); 

                }
                break;
            case 52 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:571: KEYWORD_25
                {
                mKEYWORD_25(); 

                }
                break;
            case 53 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:582: KEYWORD_26
                {
                mKEYWORD_26(); 

                }
                break;
            case 54 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:593: KEYWORD_27
                {
                mKEYWORD_27(); 

                }
                break;
            case 55 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:604: KEYWORD_28
                {
                mKEYWORD_28(); 

                }
                break;
            case 56 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:615: KEYWORD_14
                {
                mKEYWORD_14(); 

                }
                break;
            case 57 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:626: KEYWORD_15
                {
                mKEYWORD_15(); 

                }
                break;
            case 58 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:637: KEYWORD_16
                {
                mKEYWORD_16(); 

                }
                break;
            case 59 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:648: KEYWORD_17
                {
                mKEYWORD_17(); 

                }
                break;
            case 60 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:659: KEYWORD_18
                {
                mKEYWORD_18(); 

                }
                break;
            case 61 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:670: KEYWORD_19
                {
                mKEYWORD_19(); 

                }
                break;
            case 62 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:681: KEYWORD_20
                {
                mKEYWORD_20(); 

                }
                break;
            case 63 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:692: KEYWORD_21
                {
                mKEYWORD_21(); 

                }
                break;
            case 64 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:703: KEYWORD_22
                {
                mKEYWORD_22(); 

                }
                break;
            case 65 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:714: KEYWORD_1
                {
                mKEYWORD_1(); 

                }
                break;
            case 66 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:724: KEYWORD_2
                {
                mKEYWORD_2(); 

                }
                break;
            case 67 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:734: KEYWORD_3
                {
                mKEYWORD_3(); 

                }
                break;
            case 68 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:744: KEYWORD_4
                {
                mKEYWORD_4(); 

                }
                break;
            case 69 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:754: KEYWORD_5
                {
                mKEYWORD_5(); 

                }
                break;
            case 70 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:764: KEYWORD_6
                {
                mKEYWORD_6(); 

                }
                break;
            case 71 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:774: KEYWORD_7
                {
                mKEYWORD_7(); 

                }
                break;
            case 72 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:784: KEYWORD_8
                {
                mKEYWORD_8(); 

                }
                break;
            case 73 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:794: KEYWORD_9
                {
                mKEYWORD_9(); 

                }
                break;
            case 74 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:804: KEYWORD_10
                {
                mKEYWORD_10(); 

                }
                break;
            case 75 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:815: KEYWORD_11
                {
                mKEYWORD_11(); 

                }
                break;
            case 76 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:826: KEYWORD_12
                {
                mKEYWORD_12(); 

                }
                break;
            case 77 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:837: KEYWORD_13
                {
                mKEYWORD_13(); 

                }
                break;
            case 78 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:848: RULE_JRPARAM
                {
                mRULE_JRPARAM(); 

                }
                break;
            case 79 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:861: RULE_JRNPARAM
                {
                mRULE_JRNPARAM(); 

                }
                break;
            case 80 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:875: RULE_STAR
                {
                mRULE_STAR(); 

                }
                break;
            case 81 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:885: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 82 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:894: RULE_TIMESTAMP
                {
                mRULE_TIMESTAMP(); 

                }
                break;
            case 83 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:909: RULE_DATE
                {
                mRULE_DATE(); 

                }
                break;
            case 84 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:919: RULE_TIME
                {
                mRULE_TIME(); 

                }
                break;
            case 85 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:929: RULE_SIGNED_DOUBLE
                {
                mRULE_SIGNED_DOUBLE(); 

                }
                break;
            case 86 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:948: RULE_STRING_
                {
                mRULE_STRING_(); 

                }
                break;
            case 87 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:961: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 88 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:973: RULE_DBNAME
                {
                mRULE_DBNAME(); 

                }
                break;
            case 89 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:985: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 90 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:993: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 91 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:1009: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 92 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:1025: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 93 :
                // ../com.jaspersoft.studio.data.sql/src-gen/com/jaspersoft/studio/data/parser/antlr/lexer/InternalSqlLexer.g:1:1033: RULE_ANY_OTHER
                {
                mRULE_ANY_OTHER(); 

                }
                break;

        }

    }


    protected DFA21 dfa21 = new DFA21(this);
    static final String DFA21_eotS =
        "\6\52\1\55\17\52\1\55\1\134\1\136\1\140\4\uffff\1\147\1\uffff\1"+
        "\153\4\uffff\1\161\3\55\5\uffff\4\52\1\174\6\52\3\uffff\4\52\1\u008b"+
        "\2\52\1\u008e\23\52\1\u00a6\15\uffff\1\52\1\161\17\uffff\3\52\1"+
        "\uffff\2\52\1\uffff\6\52\2\uffff\6\52\1\uffff\2\52\1\uffff\4\52"+
        "\1\u00c8\15\52\1\u00d7\1\u00d8\1\u00d9\1\u00da\1\u00db\3\uffff\1"+
        "\52\1\uffff\1\52\1\u00df\1\u00e0\1\uffff\2\52\1\uffff\7\52\1\u00ef"+
        "\2\uffff\2\52\1\u00f4\10\52\1\u00fd\1\uffff\2\52\1\u0100\1\52\1"+
        "\u0102\1\52\1\u0105\1\u0106\1\52\1\u0108\2\52\1\u010b\1\u010c\7"+
        "\uffff\1\52\4\uffff\1\52\1\u0111\3\uffff\1\52\1\u0113\1\52\1\uffff"+
        "\1\u0115\1\uffff\1\u0116\3\uffff\2\52\1\uffff\4\52\1\u011f\2\52"+
        "\1\u0122\1\uffff\2\52\3\uffff\1\u0125\3\uffff\1\u0126\1\uffff\1"+
        "\u0127\1\u0128\5\uffff\1\52\1\uffff\1\52\1\uffff\1\52\4\uffff\2"+
        "\52\1\uffff\1\52\1\uffff\1\u0133\1\uffff\1\52\1\u0135\1\uffff\1"+
        "\u0136\1\u0137\6\uffff\2\52\1\u013c\2\uffff\1\u0140\1\52\1\u0142"+
        "\1\uffff\1\u0143\5\uffff\1\52\1\u0147\1\uffff\1\u0149\1\u014a\2"+
        "\uffff\1\u014b\4\uffff\1\u014e\15\uffff\1\u0156\6\uffff";
    static final String DFA21_eofS =
        "\u015b\uffff";
    static final String DFA21_minS =
        "\1\0\1\105\1\116\1\101\1\111\1\110\1\0\2\105\1\122\1\106\1\105\1"+
        "\114\1\101\1\105\1\101\1\105\1\111\1\116\1\117\1\110\1\114\1\120"+
        "\2\75\1\174\4\uffff\1\55\1\uffff\1\52\4\uffff\1\55\3\0\5\uffff\1"+
        "\124\1\117\1\114\1\40\1\55\2\124\1\127\1\107\1\124\1\105\2\0\1\uffff"+
        "\1\124\2\123\1\105\1\55\1\106\1\124\1\55\1\122\1\103\1\125\1\123"+
        "\1\104\1\126\1\114\1\115\1\123\1\117\1\106\1\113\1\116\2\111\1\105"+
        "\1\120\1\114\1\104\1\55\1\uffff\1\41\13\uffff\1\0\1\55\13\uffff"+
        "\1\0\3\uffff\1\103\1\115\1\114\1\116\2\105\1\uffff\1\40\1\125\1"+
        "\123\2\110\1\116\2\0\1\127\1\124\1\103\1\125\1\101\1\105\1\uffff"+
        "\1\123\1\105\1\uffff\1\103\1\105\1\101\1\105\1\55\1\111\3\105\2"+
        "\123\1\124\1\111\1\105\1\125\1\117\2\116\5\55\3\uffff\2\0\1\110"+
        "\2\55\1\117\2\122\1\102\1\121\1\116\1\122\1\40\1\124\1\40\1\105"+
        "\1\55\2\0\1\105\1\111\1\55\1\120\1\124\1\122\1\105\1\122\1\105\1"+
        "\120\1\114\1\55\1\uffff\1\116\1\103\1\55\1\50\1\55\1\123\2\55\1"+
        "\124\1\55\1\123\1\116\2\55\5\uffff\2\0\1\40\4\uffff\1\123\1\55\3"+
        "\uffff\1\125\1\55\1\101\1\uffff\1\55\1\uffff\1\55\1\uffff\2\0\1"+
        "\105\1\116\1\uffff\1\40\1\105\1\40\1\124\1\55\1\116\1\124\1\55\1"+
        "\uffff\1\107\1\124\3\uffff\1\55\3\uffff\1\55\1\uffff\2\55\2\uffff"+
        "\2\0\1\uffff\1\105\1\uffff\1\101\1\uffff\1\114\2\uffff\2\0\1\116"+
        "\1\103\1\uffff\1\122\1\uffff\1\55\1\uffff\1\124\1\55\1\uffff\2\55"+
        "\4\uffff\2\0\1\103\1\114\1\55\2\0\1\55\1\124\1\55\1\uffff\1\55\3"+
        "\uffff\2\0\1\124\1\55\1\uffff\2\0\2\uffff\1\55\2\uffff\2\0\1\55"+
        "\5\uffff\2\0\2\uffff\4\0\1\40\1\0\2\uffff\1\0\2\uffff";
    static final String DFA21_maxS =
        "\1\uffff\1\165\1\163\2\157\1\151\1\uffff\1\145\1\151\1\162\1\165"+
        "\1\145\1\170\1\141\1\157\1\162\2\151\1\156\2\157\1\163\1\170\1\76"+
        "\1\75\1\174\4\uffff\1\ufaff\1\uffff\1\57\4\uffff\1\ufaff\3\uffff"+
        "\5\uffff\1\164\1\157\1\154\1\40\1\ufaff\2\164\1\167\1\147\1\164"+
        "\1\145\2\uffff\1\uffff\1\164\2\163\1\157\1\ufaff\1\146\1\164\1\ufaff"+
        "\1\162\1\143\1\165\1\163\1\144\1\166\1\154\1\155\1\163\1\157\1\163"+
        "\1\155\1\156\2\151\1\145\1\160\1\154\1\171\1\ufaff\1\uffff\1\173"+
        "\13\uffff\1\uffff\1\ufaff\13\uffff\1\uffff\3\uffff\1\143\1\155\1"+
        "\154\1\156\2\145\1\uffff\1\151\1\165\1\163\2\150\1\162\2\uffff\1"+
        "\167\1\164\1\143\1\165\1\141\1\145\1\uffff\1\163\1\145\1\uffff\1"+
        "\143\1\145\1\141\1\145\1\ufaff\1\151\2\145\1\164\2\163\1\164\1\151"+
        "\1\145\1\165\1\157\2\156\5\ufaff\3\uffff\2\uffff\1\150\2\ufaff\1"+
        "\165\2\162\1\154\1\161\1\156\1\162\1\40\1\164\1\40\1\145\1\ufaff"+
        "\2\uffff\1\145\1\151\1\ufaff\1\160\1\164\1\162\1\145\1\162\1\145"+
        "\1\160\1\154\1\ufaff\1\uffff\1\156\1\143\1\ufaff\1\50\1\ufaff\1"+
        "\163\2\ufaff\1\164\1\ufaff\1\163\1\156\2\ufaff\5\uffff\2\uffff\1"+
        "\40\4\uffff\1\163\1\ufaff\3\uffff\1\165\1\ufaff\1\141\1\uffff\1"+
        "\ufaff\1\uffff\1\ufaff\1\uffff\2\uffff\1\145\1\156\1\uffff\1\40"+
        "\1\145\1\40\1\164\1\ufaff\1\156\1\164\1\ufaff\1\uffff\1\147\1\164"+
        "\3\uffff\1\ufaff\3\uffff\1\ufaff\1\uffff\2\ufaff\2\uffff\2\uffff"+
        "\1\uffff\1\145\1\uffff\1\141\1\uffff\1\154\2\uffff\2\uffff\1\156"+
        "\1\143\1\uffff\1\162\1\uffff\1\ufaff\1\uffff\1\164\1\ufaff\1\uffff"+
        "\2\ufaff\4\uffff\2\uffff\1\143\1\154\1\ufaff\2\uffff\1\ufaff\1\164"+
        "\1\ufaff\1\uffff\1\ufaff\3\uffff\2\uffff\1\164\1\ufaff\1\uffff\2"+
        "\uffff\2\uffff\1\ufaff\2\uffff\2\uffff\1\ufaff\5\uffff\2\uffff\2"+
        "\uffff\4\uffff\1\40\1\uffff\2\uffff\1\uffff\2\uffff";
    static final String DFA21_acceptS =
        "\32\uffff\1\101\1\102\1\103\1\104\1\uffff\1\106\1\uffff\1\111\1"+
        "\113\1\115\1\120\4\uffff\2\131\1\132\1\134\1\135\15\uffff\1\130"+
        "\34\uffff\1\70\1\uffff\1\71\1\72\1\110\1\73\1\112\1\100\1\114\1"+
        "\101\1\102\1\103\1\104\2\uffff\1\105\1\106\1\132\1\133\1\107\1\111"+
        "\1\113\1\115\1\120\1\125\1\121\1\uffff\1\126\1\127\1\134\6\uffff"+
        "\1\75\16\uffff\1\77\2\uffff\1\76\27\uffff\1\74\1\116\1\117\37\uffff"+
        "\1\66\16\uffff\1\67\1\62\1\63\1\64\1\65\3\uffff\1\51\1\52\1\2\1"+
        "\22\2\uffff\1\3\1\13\1\27\3\uffff\1\5\1\uffff\1\6\1\uffff\1\61\4"+
        "\uffff\1\47\10\uffff\1\50\2\uffff\1\57\1\32\1\46\1\uffff\1\36\1"+
        "\55\1\54\1\uffff\1\56\2\uffff\1\53\1\60\2\uffff\1\1\1\uffff\1\35"+
        "\1\uffff\1\41\1\uffff\1\43\1\45\4\uffff\1\12\1\uffff\1\15\1\uffff"+
        "\1\42\2\uffff\1\34\2\uffff\1\33\1\37\1\40\1\44\12\uffff\1\30\1\uffff"+
        "\1\25\1\26\1\31\4\uffff\1\23\2\uffff\1\10\1\20\1\uffff\1\21\1\24"+
        "\3\uffff\1\14\1\7\1\16\1\17\1\11\2\uffff\1\4\1\7\6\uffff\1\123\1"+
        "\122\1\uffff\2\124";
    static final String DFA21_specialS =
        "\1\34\5\uffff\1\0\37\uffff\1\33\1\11\1\50\20\uffff\1\32\1\6\52\uffff"+
        "\1\10\14\uffff\1\24\20\uffff\1\31\1\5\44\uffff\1\25\1\26\17\uffff"+
        "\1\30\1\4\40\uffff\1\41\1\21\22\uffff\1\35\1\3\33\uffff\1\40\1\23"+
        "\10\uffff\1\36\1\2\20\uffff\1\43\1\22\3\uffff\1\37\1\1\10\uffff"+
        "\1\42\1\16\3\uffff\1\7\1\27\5\uffff\1\45\1\15\6\uffff\1\44\1\20"+
        "\2\uffff\1\47\1\17\1\46\1\13\1\uffff\1\12\2\uffff\1\14\2\uffff}>";
    static final String[] DFA21_transitionS = {
            "\11\55\2\54\2\55\1\54\22\55\1\54\1\55\1\47\1\53\1\26\2\55\1"+
            "\46\1\32\1\33\1\44\1\34\1\35\1\36\1\37\1\40\12\45\2\55\1\27"+
            "\1\41\1\30\2\55\1\25\1\7\1\17\1\10\1\14\1\1\1\11\1\15\1\2\1"+
            "\23\1\51\1\20\1\21\1\3\1\12\1\13\1\51\1\4\1\16\1\24\1\22\1\51"+
            "\1\5\3\51\1\6\3\55\1\51\1\50\1\25\1\7\1\17\1\10\1\14\1\1\1\11"+
            "\1\15\1\2\1\23\1\51\1\20\1\21\1\3\1\12\1\13\1\51\1\4\1\16\1"+
            "\24\1\22\1\51\1\5\3\51\1\42\1\31\1\43\102\55\u0190\51\u01c0"+
            "\55\100\51\u2bf1\55\u00bf\51\u00f0\55\20\51\u0200\55\u19c0\51"+
            "\100\55\u5200\51\u5900\55\u0200\51\u0500\55",
            "\1\56\14\uffff\1\57\2\uffff\1\60\17\uffff\1\56\14\uffff\1\57"+
            "\2\uffff\1\60",
            "\1\62\4\uffff\1\61\32\uffff\1\62\4\uffff\1\61",
            "\1\64\15\uffff\1\63\21\uffff\1\64\15\uffff\1\63",
            "\1\66\5\uffff\1\65\31\uffff\1\66\5\uffff\1\65",
            "\1\70\1\67\36\uffff\1\70\1\67",
            "\102\73\1\71\4\73\1\72\32\73\1\71\4\73\1\72\uff98\73",
            "\1\74\37\uffff\1\74",
            "\1\76\3\uffff\1\75\33\uffff\1\76\3\uffff\1\75",
            "\1\77\37\uffff\1\77",
            "\1\101\7\uffff\1\103\3\uffff\1\100\2\uffff\1\102\20\uffff\1"+
            "\101\7\uffff\1\103\3\uffff\1\100\2\uffff\1\102",
            "\1\104\37\uffff\1\104",
            "\1\107\1\uffff\1\110\2\uffff\1\106\6\uffff\1\105\23\uffff\1"+
            "\107\1\uffff\1\110\2\uffff\1\106\6\uffff\1\105",
            "\1\111\37\uffff\1\111",
            "\1\112\11\uffff\1\113\25\uffff\1\112\11\uffff\1\113",
            "\1\114\20\uffff\1\115\16\uffff\1\114\20\uffff\1\115",
            "\1\116\3\uffff\1\117\33\uffff\1\116\3\uffff\1\117",
            "\1\120\37\uffff\1\120",
            "\1\121\37\uffff\1\121",
            "\1\122\37\uffff\1\122",
            "\1\123\6\uffff\1\124\30\uffff\1\123\6\uffff\1\124",
            "\1\125\1\uffff\1\126\4\uffff\1\127\30\uffff\1\125\1\uffff\1"+
            "\126\4\uffff\1\127",
            "\1\131\7\uffff\1\130\37\uffff\1\130",
            "\1\132\1\133",
            "\1\135",
            "\1\137",
            "",
            "",
            "",
            "",
            "\1\145\2\uffff\12\146\7\uffff\32\52\4\uffff\1\52\1\uffff\32"+
            "\52\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf"+
            "\52\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52"+
            "\u5900\uffff\u0200\52",
            "",
            "\1\152\4\uffff\1\151",
            "",
            "",
            "",
            "",
            "\1\52\1\160\1\uffff\12\146\7\uffff\32\52\4\uffff\1\52\1\uffff"+
            "\32\52\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf"+
            "\52\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52"+
            "\u5900\uffff\u0200\52",
            "\60\163\12\162\uffc6\163",
            "\0\164",
            "\0\73",
            "",
            "",
            "",
            "",
            "",
            "\1\166\37\uffff\1\166",
            "\1\167\37\uffff\1\167",
            "\1\170\37\uffff\1\170",
            "\1\171",
            "\1\52\2\uffff\12\52\7\uffff\15\52\1\173\5\52\1\172\6\52\4\uffff"+
            "\1\52\1\uffff\15\52\1\173\5\52\1\172\6\52\105\uffff\u0190\52"+
            "\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52\u00f0\uffff\20\52\u0200"+
            "\uffff\u19c0\52\100\uffff\u5200\52\u5900\uffff\u0200\52",
            "\1\175\37\uffff\1\175",
            "\1\176\37\uffff\1\176",
            "\1\177\37\uffff\1\177",
            "\1\u0080\37\uffff\1\u0080",
            "\1\u0081\37\uffff\1\u0081",
            "\1\u0082\37\uffff\1\u0082",
            "\105\73\1\u0083\37\73\1\u0083\uff9a\73",
            "\122\73\1\u0084\37\73\1\u0084\uff8d\73",
            "",
            "\1\u0085\37\uffff\1\u0085",
            "\1\u0086\37\uffff\1\u0086",
            "\1\u0087\37\uffff\1\u0087",
            "\1\u0089\11\uffff\1\u0088\25\uffff\1\u0089\11\uffff\1\u0088",
            "\1\52\2\uffff\12\52\7\uffff\3\52\1\u008a\26\52\4\uffff\1\52"+
            "\1\uffff\3\52\1\u008a\26\52\105\uffff\u0190\52\u01c0\uffff\100"+
            "\52\u2bf1\uffff\u00bf\52\u00f0\uffff\20\52\u0200\uffff\u19c0"+
            "\52\100\uffff\u5200\52\u5900\uffff\u0200\52",
            "\1\u008c\37\uffff\1\u008c",
            "\1\u008d\37\uffff\1\u008d",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\u008f\37\uffff\1\u008f",
            "\1\u0090\37\uffff\1\u0090",
            "\1\u0091\37\uffff\1\u0091",
            "\1\u0092\37\uffff\1\u0092",
            "\1\u0093\37\uffff\1\u0093",
            "\1\u0094\37\uffff\1\u0094",
            "\1\u0095\37\uffff\1\u0095",
            "\1\u0096\37\uffff\1\u0096",
            "\1\u0097\37\uffff\1\u0097",
            "\1\u0098\37\uffff\1\u0098",
            "\1\u009a\14\uffff\1\u0099\22\uffff\1\u009a\14\uffff\1\u0099",
            "\1\u009c\1\uffff\1\u009b\35\uffff\1\u009c\1\uffff\1\u009b",
            "\1\u009d\37\uffff\1\u009d",
            "\1\u009e\37\uffff\1\u009e",
            "\1\u009f\37\uffff\1\u009f",
            "\1\u00a0\37\uffff\1\u00a0",
            "\1\u00a1\37\uffff\1\u00a1",
            "\1\u00a2\37\uffff\1\u00a2",
            "\1\u00a3\24\uffff\1\u00a4\12\uffff\1\u00a3\24\uffff\1\u00a4",
            "\1\52\2\uffff\12\52\7\uffff\2\52\1\u00a5\27\52\4\uffff\1\52"+
            "\1\uffff\2\52\1\u00a5\27\52\105\uffff\u0190\52\u01c0\uffff\100"+
            "\52\u2bf1\uffff\u00bf\52\u00f0\uffff\20\52\u0200\uffff\u19c0"+
            "\52\100\uffff\u5200\52\u5900\uffff\u0200\52",
            "",
            "\1\u00a8\131\uffff\1\u00a7",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\55\151\1\u00a9\2\151\12\u00a9\7\151\32\u00a9\4\151\1\u00a9"+
            "\1\151\32\u00a9\105\151\u0190\u00a9\u01c0\151\100\u00a9\u2bf1"+
            "\151\u00bf\u00a9\u00f0\151\20\u00a9\u0200\151\u19c0\u00a9\100"+
            "\151\u5200\u00a9\u5900\151\u0200\u00a9\u0500\151",
            "\1\52\1\160\1\uffff\12\146\7\uffff\32\52\4\uffff\1\52\1\uffff"+
            "\32\52\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf"+
            "\52\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52"+
            "\u5900\uffff\u0200\52",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\60\163\12\u00aa\uffc6\163",
            "",
            "",
            "",
            "\1\u00ab\37\uffff\1\u00ab",
            "\1\u00ac\37\uffff\1\u00ac",
            "\1\u00ad\37\uffff\1\u00ad",
            "\1\u00ae\37\uffff\1\u00ae",
            "\1\u00af\37\uffff\1\u00af",
            "\1\u00b0\37\uffff\1\u00b0",
            "",
            "\1\u00b1\44\uffff\1\u00b2\3\uffff\1\u00b3\33\uffff\1\u00b2"+
            "\3\uffff\1\u00b3",
            "\1\u00b4\37\uffff\1\u00b4",
            "\1\u00b5\37\uffff\1\u00b5",
            "\1\u00b6\37\uffff\1\u00b6",
            "\1\u00b7\37\uffff\1\u00b7",
            "\1\u00b9\3\uffff\1\u00b8\33\uffff\1\u00b9\3\uffff\1\u00b8",
            "\124\73\1\u00ba\37\73\1\u00ba\uff8b\73",
            "\105\73\1\u00bb\37\73\1\u00bb\uff9a\73",
            "\1\u00bc\37\uffff\1\u00bc",
            "\1\u00bd\37\uffff\1\u00bd",
            "\1\u00be\37\uffff\1\u00be",
            "\1\u00bf\37\uffff\1\u00bf",
            "\1\u00c0\37\uffff\1\u00c0",
            "\1\u00c1\37\uffff\1\u00c1",
            "",
            "\1\u00c2\37\uffff\1\u00c2",
            "\1\u00c3\37\uffff\1\u00c3",
            "",
            "\1\u00c4\37\uffff\1\u00c4",
            "\1\u00c5\37\uffff\1\u00c5",
            "\1\u00c6\37\uffff\1\u00c6",
            "\1\u00c7\37\uffff\1\u00c7",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\u00c9\37\uffff\1\u00c9",
            "\1\u00ca\37\uffff\1\u00ca",
            "\1\u00cb\37\uffff\1\u00cb",
            "\1\u00cd\16\uffff\1\u00cc\20\uffff\1\u00cd\16\uffff\1\u00cc",
            "\1\u00ce\37\uffff\1\u00ce",
            "\1\u00cf\37\uffff\1\u00cf",
            "\1\u00d0\37\uffff\1\u00d0",
            "\1\u00d1\37\uffff\1\u00d1",
            "\1\u00d2\37\uffff\1\u00d2",
            "\1\u00d3\37\uffff\1\u00d3",
            "\1\u00d4\37\uffff\1\u00d4",
            "\1\u00d5\37\uffff\1\u00d5",
            "\1\u00d6\37\uffff\1\u00d6",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "",
            "",
            "\55\151\1\u00a9\2\151\12\u00a9\7\151\32\u00a9\4\151\1\u00a9"+
            "\1\151\32\u00a9\105\151\u0190\u00a9\u01c0\151\100\u00a9\u2bf1"+
            "\151\u00bf\u00a9\u00f0\151\20\u00a9\u0200\151\u19c0\u00a9\100"+
            "\151\u5200\u00a9\u5900\151\u0200\u00a9\u0500\151",
            "\60\163\12\u00dc\1\u00dd\uffc5\163",
            "\1\u00de\37\uffff\1\u00de",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\u00e1\5\uffff\1\u00e2\31\uffff\1\u00e1\5\uffff\1\u00e2",
            "\1\u00e3\37\uffff\1\u00e3",
            "\1\u00e4\37\uffff\1\u00e4",
            "\1\u00e5\6\uffff\1\u00e7\2\uffff\1\u00e6\25\uffff\1\u00e5\6"+
            "\uffff\1\u00e7\2\uffff\1\u00e6",
            "\1\u00e8\37\uffff\1\u00e8",
            "\1\u00e9\37\uffff\1\u00e9",
            "\1\u00ea\37\uffff\1\u00ea",
            "\1\u00eb",
            "\1\u00ec\37\uffff\1\u00ec",
            "\1\u00ed",
            "\1\u00ee\37\uffff\1\u00ee",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\127\73\1\u00f0\37\73\1\u00f0\uff88\73",
            "\101\73\1\u00f1\37\73\1\u00f1\uff9e\73",
            "\1\u00f2\37\uffff\1\u00f2",
            "\1\u00f3\37\uffff\1\u00f3",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\u00f5\37\uffff\1\u00f5",
            "\1\u00f6\37\uffff\1\u00f6",
            "\1\u00f7\37\uffff\1\u00f7",
            "\1\u00f8\37\uffff\1\u00f8",
            "\1\u00f9\37\uffff\1\u00f9",
            "\1\u00fa\37\uffff\1\u00fa",
            "\1\u00fb\37\uffff\1\u00fb",
            "\1\u00fc\37\uffff\1\u00fc",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "\1\u00fe\37\uffff\1\u00fe",
            "\1\u00ff\37\uffff\1\u00ff",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\u0101",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\u0103\37\uffff\1\u0103",
            "\1\52\2\uffff\12\52\7\uffff\32\52\2\uffff\1\u0104\1\uffff\1"+
            "\52\1\uffff\32\52\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1"+
            "\uffff\u00bf\52\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff"+
            "\u5200\52\u5900\uffff\u0200\52",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\u0107\37\uffff\1\u0107",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\u0109\37\uffff\1\u0109",
            "\1\u010a\37\uffff\1\u010a",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "",
            "",
            "",
            "",
            "\60\163\12\u010d\uffc6\163",
            "\60\163\12\u010e\uffc6\163",
            "\1\u010f",
            "",
            "",
            "",
            "",
            "\1\u0110\37\uffff\1\u0110",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "",
            "",
            "\1\u0112\37\uffff\1\u0112",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\u0114\37\uffff\1\u0114",
            "",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "\105\73\1\u0117\37\73\1\u0117\uff9a\73",
            "\124\73\1\u0118\37\73\1\u0118\uff8b\73",
            "\1\u0119\37\uffff\1\u0119",
            "\1\u011a\37\uffff\1\u011a",
            "",
            "\1\u011b",
            "\1\u011c\37\uffff\1\u011c",
            "\1\u011d",
            "\1\u011e\37\uffff\1\u011e",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\u0120\37\uffff\1\u0120",
            "\1\u0121\37\uffff\1\u0121",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "\1\u0123\37\uffff\1\u0123",
            "\1\u0124\37\uffff\1\u0124",
            "",
            "",
            "",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "",
            "",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "",
            "\55\163\1\u0129\uffd2\163",
            "\60\163\12\u012a\uffc6\163",
            "",
            "\1\u012b\37\uffff\1\u012b",
            "",
            "\1\u012c\37\uffff\1\u012c",
            "",
            "\1\u012d\37\uffff\1\u012d",
            "",
            "",
            "\105\73\1\u012e\37\73\1\u012e\uff9a\73",
            "\105\73\1\u012f\37\73\1\u012f\uff9a\73",
            "\1\u0130\37\uffff\1\u0130",
            "\1\u0131\37\uffff\1\u0131",
            "",
            "\1\u0132\37\uffff\1\u0132",
            "",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "\1\u0134\37\uffff\1\u0134",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "",
            "",
            "",
            "\60\163\2\u0138\uffce\163",
            "\72\163\1\u0139\uffc5\163",
            "\1\u013a\37\uffff\1\u013a",
            "\1\u013b\37\uffff\1\u013b",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "\116\73\1\u013d\37\73\1\u013d\uff91\73",
            "\122\73\1\u013e\37\73\1\u013e\uff8d\73",
            "\1\52\2\uffff\12\52\7\uffff\32\52\2\uffff\1\u013f\1\uffff\1"+
            "\52\1\uffff\32\52\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1"+
            "\uffff\u00bf\52\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff"+
            "\u5200\52\u5900\uffff\u0200\52",
            "\1\u0141\37\uffff\1\u0141",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "",
            "",
            "\60\163\12\u0144\uffc6\163",
            "\60\163\2\u0145\uffce\163",
            "\1\u0146\37\uffff\1\u0146",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "\135\73\1\u0148\uffa2\73",
            "\0\73",
            "",
            "",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "",
            "\55\163\1\u014c\uffd2\163",
            "\60\163\12\u014d\uffc6\163",
            "\1\52\2\uffff\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52"+
            "\105\uffff\u0190\52\u01c0\uffff\100\52\u2bf1\uffff\u00bf\52"+
            "\u00f0\uffff\20\52\u0200\uffff\u19c0\52\100\uffff\u5200\52\u5900"+
            "\uffff\u0200\52",
            "",
            "",
            "",
            "",
            "",
            "\60\163\4\u0150\uffcc\163",
            "\56\163\1\u0151\uffd1\163",
            "",
            "",
            "\60\163\12\u0152\uffc6\163",
            "\60\163\12\u0153\uffc6\163",
            "\47\163\1\u0154\uffd8\163",
            "\60\163\12\u0155\uffc6\163",
            "\1\u0157",
            "\60\163\12\u0158\uffc6\163",
            "",
            "",
            "\47\163\1\u0159\uffd8\163",
            "",
            ""
    };

    static final short[] DFA21_eot = DFA.unpackEncodedString(DFA21_eotS);
    static final short[] DFA21_eof = DFA.unpackEncodedString(DFA21_eofS);
    static final char[] DFA21_min = DFA.unpackEncodedStringToUnsignedChars(DFA21_minS);
    static final char[] DFA21_max = DFA.unpackEncodedStringToUnsignedChars(DFA21_maxS);
    static final short[] DFA21_accept = DFA.unpackEncodedString(DFA21_acceptS);
    static final short[] DFA21_special = DFA.unpackEncodedString(DFA21_specialS);
    static final short[][] DFA21_transition;

    static {
        int numStates = DFA21_transitionS.length;
        DFA21_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA21_transition[i] = DFA.unpackEncodedString(DFA21_transitionS[i]);
        }
    }

    class DFA21 extends DFA {

        public DFA21(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 21;
            this.eot = DFA21_eot;
            this.eof = DFA21_eof;
            this.min = DFA21_min;
            this.max = DFA21_max;
            this.accept = DFA21_accept;
            this.special = DFA21_special;
            this.transition = DFA21_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( KEYWORD_75 | KEYWORD_76 | KEYWORD_77 | KEYWORD_71 | KEYWORD_72 | KEYWORD_73 | KEYWORD_74 | KEYWORD_63 | KEYWORD_64 | KEYWORD_65 | KEYWORD_66 | KEYWORD_67 | KEYWORD_68 | KEYWORD_69 | KEYWORD_70 | KEYWORD_58 | KEYWORD_59 | KEYWORD_60 | KEYWORD_61 | KEYWORD_62 | KEYWORD_53 | KEYWORD_54 | KEYWORD_55 | KEYWORD_56 | KEYWORD_57 | KEYWORD_41 | KEYWORD_42 | KEYWORD_43 | KEYWORD_44 | KEYWORD_45 | KEYWORD_46 | KEYWORD_47 | KEYWORD_48 | KEYWORD_49 | KEYWORD_50 | KEYWORD_51 | KEYWORD_52 | KEYWORD_29 | KEYWORD_30 | KEYWORD_31 | KEYWORD_32 | KEYWORD_33 | KEYWORD_34 | KEYWORD_35 | KEYWORD_36 | KEYWORD_37 | KEYWORD_38 | KEYWORD_39 | KEYWORD_40 | KEYWORD_23 | KEYWORD_24 | KEYWORD_25 | KEYWORD_26 | KEYWORD_27 | KEYWORD_28 | KEYWORD_14 | KEYWORD_15 | KEYWORD_16 | KEYWORD_17 | KEYWORD_18 | KEYWORD_19 | KEYWORD_20 | KEYWORD_21 | KEYWORD_22 | KEYWORD_1 | KEYWORD_2 | KEYWORD_3 | KEYWORD_4 | KEYWORD_5 | KEYWORD_6 | KEYWORD_7 | KEYWORD_8 | KEYWORD_9 | KEYWORD_10 | KEYWORD_11 | KEYWORD_12 | KEYWORD_13 | RULE_JRPARAM | RULE_JRNPARAM | RULE_STAR | RULE_INT | RULE_TIMESTAMP | RULE_DATE | RULE_TIME | RULE_SIGNED_DOUBLE | RULE_STRING_ | RULE_STRING | RULE_DBNAME | RULE_ID | RULE_SL_COMMENT | RULE_ML_COMMENT | RULE_WS | RULE_ANY_OTHER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA21_6 = input.LA(1);

                        s = -1;
                        if ( (LA21_6=='B'||LA21_6=='b') ) {s = 57;}

                        else if ( (LA21_6=='G'||LA21_6=='g') ) {s = 58;}

                        else if ( ((LA21_6>='\u0000' && LA21_6<='A')||(LA21_6>='C' && LA21_6<='F')||(LA21_6>='H' && LA21_6<='a')||(LA21_6>='c' && LA21_6<='f')||(LA21_6>='h' && LA21_6<='\uFFFF')) ) {s = 59;}

                        else s = 45;

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA21_303 = input.LA(1);

                        s = -1;
                        if ( (LA21_303=='R'||LA21_303=='r') ) {s = 318;}

                        else if ( ((LA21_303>='\u0000' && LA21_303<='Q')||(LA21_303>='S' && LA21_303<='q')||(LA21_303>='s' && LA21_303<='\uFFFF')) ) {s = 59;}

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA21_280 = input.LA(1);

                        s = -1;
                        if ( (LA21_280=='E'||LA21_280=='e') ) {s = 303;}

                        else if ( ((LA21_280>='\u0000' && LA21_280<='D')||(LA21_280>='F' && LA21_280<='d')||(LA21_280>='f' && LA21_280<='\uFFFF')) ) {s = 59;}

                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA21_241 = input.LA(1);

                        s = -1;
                        if ( (LA21_241=='T'||LA21_241=='t') ) {s = 280;}

                        else if ( ((LA21_241>='\u0000' && LA21_241<='S')||(LA21_241>='U' && LA21_241<='s')||(LA21_241>='u' && LA21_241<='\uFFFF')) ) {s = 59;}

                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA21_187 = input.LA(1);

                        s = -1;
                        if ( (LA21_187=='A'||LA21_187=='a') ) {s = 241;}

                        else if ( ((LA21_187>='\u0000' && LA21_187<='@')||(LA21_187>='B' && LA21_187<='`')||(LA21_187>='b' && LA21_187<='\uFFFF')) ) {s = 59;}

                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA21_132 = input.LA(1);

                        s = -1;
                        if ( (LA21_132=='E'||LA21_132=='e') ) {s = 187;}

                        else if ( ((LA21_132>='\u0000' && LA21_132<='D')||(LA21_132>='F' && LA21_132<='d')||(LA21_132>='f' && LA21_132<='\uFFFF')) ) {s = 59;}

                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA21_58 = input.LA(1);

                        s = -1;
                        if ( (LA21_58=='R'||LA21_58=='r') ) {s = 132;}

                        else if ( ((LA21_58>='\u0000' && LA21_58<='Q')||(LA21_58>='S' && LA21_58<='q')||(LA21_58>='s' && LA21_58<='\uFFFF')) ) {s = 59;}

                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA21_317 = input.LA(1);

                        s = -1;
                        if ( (LA21_317==']') ) {s = 328;}

                        else if ( ((LA21_317>='\u0000' && LA21_317<='\\')||(LA21_317>='^' && LA21_317<='\uFFFF')) ) {s = 59;}

                        else s = 329;

                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA21_101 = input.LA(1);

                        s = -1;
                        if ( (LA21_101=='-'||(LA21_101>='0' && LA21_101<='9')||(LA21_101>='A' && LA21_101<='Z')||LA21_101=='_'||(LA21_101>='a' && LA21_101<='z')||(LA21_101>='\u00C0' && LA21_101<='\u024F')||(LA21_101>='\u0410' && LA21_101<='\u044F')||(LA21_101>='\u3041' && LA21_101<='\u30FF')||(LA21_101>='\u31F0' && LA21_101<='\u31FF')||(LA21_101>='\u3400' && LA21_101<='\u4DBF')||(LA21_101>='\u4E00' && LA21_101<='\u9FFF')||(LA21_101>='\uF900' && LA21_101<='\uFAFF')) ) {s = 169;}

                        else if ( ((LA21_101>='\u0000' && LA21_101<=',')||(LA21_101>='.' && LA21_101<='/')||(LA21_101>=':' && LA21_101<='@')||(LA21_101>='[' && LA21_101<='^')||LA21_101=='`'||(LA21_101>='{' && LA21_101<='\u00BF')||(LA21_101>='\u0250' && LA21_101<='\u040F')||(LA21_101>='\u0450' && LA21_101<='\u3040')||(LA21_101>='\u3100' && LA21_101<='\u31EF')||(LA21_101>='\u3200' && LA21_101<='\u33FF')||(LA21_101>='\u4DC0' && LA21_101<='\u4DFF')||(LA21_101>='\uA000' && LA21_101<='\uF8FF')||(LA21_101>='\uFB00' && LA21_101<='\uFFFF')) ) {s = 105;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA21_39 = input.LA(1);

                        s = -1;
                        if ( ((LA21_39>='\u0000' && LA21_39<='\uFFFF')) ) {s = 116;}

                        else s = 45;

                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA21_341 = input.LA(1);

                        s = -1;
                        if ( ((LA21_341>='0' && LA21_341<='9')) ) {s = 344;}

                        else if ( ((LA21_341>='\u0000' && LA21_341<='/')||(LA21_341>=':' && LA21_341<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA21_339 = input.LA(1);

                        s = -1;
                        if ( ((LA21_339>='0' && LA21_339<='9')) ) {s = 341;}

                        else if ( ((LA21_339>='\u0000' && LA21_339<='/')||(LA21_339>=':' && LA21_339<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA21_344 = input.LA(1);

                        s = -1;
                        if ( (LA21_344=='\'') ) {s = 345;}

                        else if ( ((LA21_344>='\u0000' && LA21_344<='&')||(LA21_344>='(' && LA21_344<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA21_325 = input.LA(1);

                        s = -1;
                        if ( ((LA21_325>='0' && LA21_325<='9')) ) {s = 333;}

                        else if ( ((LA21_325>='\u0000' && LA21_325<='/')||(LA21_325>=':' && LA21_325<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA21_313 = input.LA(1);

                        s = -1;
                        if ( ((LA21_313>='0' && LA21_313<='1')) ) {s = 325;}

                        else if ( ((LA21_313>='\u0000' && LA21_313<='/')||(LA21_313>='2' && LA21_313<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA21_337 = input.LA(1);

                        s = -1;
                        if ( ((LA21_337>='0' && LA21_337<='9')) ) {s = 339;}

                        else if ( ((LA21_337>='\u0000' && LA21_337<='/')||(LA21_337>=':' && LA21_337<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA21_333 = input.LA(1);

                        s = -1;
                        if ( (LA21_333=='.') ) {s = 337;}

                        else if ( ((LA21_333>='\u0000' && LA21_333<='-')||(LA21_333>='/' && LA21_333<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA21_221 = input.LA(1);

                        s = -1;
                        if ( ((LA21_221>='0' && LA21_221<='9')) ) {s = 270;}

                        else if ( ((LA21_221>='\u0000' && LA21_221<='/')||(LA21_221>=':' && LA21_221<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA21_298 = input.LA(1);

                        s = -1;
                        if ( (LA21_298==':') ) {s = 313;}

                        else if ( ((LA21_298>='\u0000' && LA21_298<='9')||(LA21_298>=';' && LA21_298<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA21_270 = input.LA(1);

                        s = -1;
                        if ( ((LA21_270>='0' && LA21_270<='9')) ) {s = 298;}

                        else if ( ((LA21_270>='\u0000' && LA21_270<='/')||(LA21_270>=':' && LA21_270<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA21_114 = input.LA(1);

                        s = -1;
                        if ( ((LA21_114>='0' && LA21_114<='9')) ) {s = 170;}

                        else if ( ((LA21_114>='\u0000' && LA21_114<='/')||(LA21_114>=':' && LA21_114<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA21_169 = input.LA(1);

                        s = -1;
                        if ( (LA21_169=='-'||(LA21_169>='0' && LA21_169<='9')||(LA21_169>='A' && LA21_169<='Z')||LA21_169=='_'||(LA21_169>='a' && LA21_169<='z')||(LA21_169>='\u00C0' && LA21_169<='\u024F')||(LA21_169>='\u0410' && LA21_169<='\u044F')||(LA21_169>='\u3041' && LA21_169<='\u30FF')||(LA21_169>='\u31F0' && LA21_169<='\u31FF')||(LA21_169>='\u3400' && LA21_169<='\u4DBF')||(LA21_169>='\u4E00' && LA21_169<='\u9FFF')||(LA21_169>='\uF900' && LA21_169<='\uFAFF')) ) {s = 169;}

                        else if ( ((LA21_169>='\u0000' && LA21_169<=',')||(LA21_169>='.' && LA21_169<='/')||(LA21_169>=':' && LA21_169<='@')||(LA21_169>='[' && LA21_169<='^')||LA21_169=='`'||(LA21_169>='{' && LA21_169<='\u00BF')||(LA21_169>='\u0250' && LA21_169<='\u040F')||(LA21_169>='\u0450' && LA21_169<='\u3040')||(LA21_169>='\u3100' && LA21_169<='\u31EF')||(LA21_169>='\u3200' && LA21_169<='\u33FF')||(LA21_169>='\u4DC0' && LA21_169<='\u4DFF')||(LA21_169>='\uA000' && LA21_169<='\uF8FF')||(LA21_169>='\uFB00' && LA21_169<='\uFFFF')) ) {s = 105;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;
                    case 22 : 
                        int LA21_170 = input.LA(1);

                        s = -1;
                        if ( ((LA21_170>='0' && LA21_170<='9')) ) {s = 220;}

                        else if ( (LA21_170==':') ) {s = 221;}

                        else if ( ((LA21_170>='\u0000' && LA21_170<='/')||(LA21_170>=';' && LA21_170<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 23 : 
                        int LA21_318 = input.LA(1);

                        s = -1;
                        if ( ((LA21_318>='\u0000' && LA21_318<='\uFFFF')) ) {s = 59;}

                        else s = 330;

                        if ( s>=0 ) return s;
                        break;
                    case 24 : 
                        int LA21_186 = input.LA(1);

                        s = -1;
                        if ( (LA21_186=='W'||LA21_186=='w') ) {s = 240;}

                        else if ( ((LA21_186>='\u0000' && LA21_186<='V')||(LA21_186>='X' && LA21_186<='v')||(LA21_186>='x' && LA21_186<='\uFFFF')) ) {s = 59;}

                        if ( s>=0 ) return s;
                        break;
                    case 25 : 
                        int LA21_131 = input.LA(1);

                        s = -1;
                        if ( (LA21_131=='T'||LA21_131=='t') ) {s = 186;}

                        else if ( ((LA21_131>='\u0000' && LA21_131<='S')||(LA21_131>='U' && LA21_131<='s')||(LA21_131>='u' && LA21_131<='\uFFFF')) ) {s = 59;}

                        if ( s>=0 ) return s;
                        break;
                    case 26 : 
                        int LA21_57 = input.LA(1);

                        s = -1;
                        if ( (LA21_57=='E'||LA21_57=='e') ) {s = 131;}

                        else if ( ((LA21_57>='\u0000' && LA21_57<='D')||(LA21_57>='F' && LA21_57<='d')||(LA21_57>='f' && LA21_57<='\uFFFF')) ) {s = 59;}

                        if ( s>=0 ) return s;
                        break;
                    case 27 : 
                        int LA21_38 = input.LA(1);

                        s = -1;
                        if ( ((LA21_38>='0' && LA21_38<='9')) ) {s = 114;}

                        else if ( ((LA21_38>='\u0000' && LA21_38<='/')||(LA21_38>=':' && LA21_38<='\uFFFF')) ) {s = 115;}

                        else s = 45;

                        if ( s>=0 ) return s;
                        break;
                    case 28 : 
                        int LA21_0 = input.LA(1);

                        s = -1;
                        if ( (LA21_0=='F'||LA21_0=='f') ) {s = 1;}

                        else if ( (LA21_0=='I'||LA21_0=='i') ) {s = 2;}

                        else if ( (LA21_0=='N'||LA21_0=='n') ) {s = 3;}

                        else if ( (LA21_0=='R'||LA21_0=='r') ) {s = 4;}

                        else if ( (LA21_0=='W'||LA21_0=='w') ) {s = 5;}

                        else if ( (LA21_0=='[') ) {s = 6;}

                        else if ( (LA21_0=='B'||LA21_0=='b') ) {s = 7;}

                        else if ( (LA21_0=='D'||LA21_0=='d') ) {s = 8;}

                        else if ( (LA21_0=='G'||LA21_0=='g') ) {s = 9;}

                        else if ( (LA21_0=='O'||LA21_0=='o') ) {s = 10;}

                        else if ( (LA21_0=='P'||LA21_0=='p') ) {s = 11;}

                        else if ( (LA21_0=='E'||LA21_0=='e') ) {s = 12;}

                        else if ( (LA21_0=='H'||LA21_0=='h') ) {s = 13;}

                        else if ( (LA21_0=='S'||LA21_0=='s') ) {s = 14;}

                        else if ( (LA21_0=='C'||LA21_0=='c') ) {s = 15;}

                        else if ( (LA21_0=='L'||LA21_0=='l') ) {s = 16;}

                        else if ( (LA21_0=='M'||LA21_0=='m') ) {s = 17;}

                        else if ( (LA21_0=='U'||LA21_0=='u') ) {s = 18;}

                        else if ( (LA21_0=='J'||LA21_0=='j') ) {s = 19;}

                        else if ( (LA21_0=='T'||LA21_0=='t') ) {s = 20;}

                        else if ( (LA21_0=='A'||LA21_0=='a') ) {s = 21;}

                        else if ( (LA21_0=='$') ) {s = 22;}

                        else if ( (LA21_0=='<') ) {s = 23;}

                        else if ( (LA21_0=='>') ) {s = 24;}

                        else if ( (LA21_0=='|') ) {s = 25;}

                        else if ( (LA21_0=='(') ) {s = 26;}

                        else if ( (LA21_0==')') ) {s = 27;}

                        else if ( (LA21_0=='+') ) {s = 28;}

                        else if ( (LA21_0==',') ) {s = 29;}

                        else if ( (LA21_0=='-') ) {s = 30;}

                        else if ( (LA21_0=='.') ) {s = 31;}

                        else if ( (LA21_0=='/') ) {s = 32;}

                        else if ( (LA21_0=='=') ) {s = 33;}

                        else if ( (LA21_0=='{') ) {s = 34;}

                        else if ( (LA21_0=='}') ) {s = 35;}

                        else if ( (LA21_0=='*') ) {s = 36;}

                        else if ( ((LA21_0>='0' && LA21_0<='9')) ) {s = 37;}

                        else if ( (LA21_0=='\'') ) {s = 38;}

                        else if ( (LA21_0=='\"') ) {s = 39;}

                        else if ( (LA21_0=='`') ) {s = 40;}

                        else if ( (LA21_0=='K'||LA21_0=='Q'||LA21_0=='V'||(LA21_0>='X' && LA21_0<='Z')||LA21_0=='_'||LA21_0=='k'||LA21_0=='q'||LA21_0=='v'||(LA21_0>='x' && LA21_0<='z')||(LA21_0>='\u00C0' && LA21_0<='\u024F')||(LA21_0>='\u0410' && LA21_0<='\u044F')||(LA21_0>='\u3041' && LA21_0<='\u30FF')||(LA21_0>='\u31F0' && LA21_0<='\u31FF')||(LA21_0>='\u3400' && LA21_0<='\u4DBF')||(LA21_0>='\u4E00' && LA21_0<='\u9FFF')||(LA21_0>='\uF900' && LA21_0<='\uFAFF')) ) {s = 41;}

                        else if ( (LA21_0=='#') ) {s = 43;}

                        else if ( ((LA21_0>='\t' && LA21_0<='\n')||LA21_0=='\r'||LA21_0==' ') ) {s = 44;}

                        else if ( ((LA21_0>='\u0000' && LA21_0<='\b')||(LA21_0>='\u000B' && LA21_0<='\f')||(LA21_0>='\u000E' && LA21_0<='\u001F')||LA21_0=='!'||(LA21_0>='%' && LA21_0<='&')||(LA21_0>=':' && LA21_0<=';')||(LA21_0>='?' && LA21_0<='@')||(LA21_0>='\\' && LA21_0<='^')||(LA21_0>='~' && LA21_0<='\u00BF')||(LA21_0>='\u0250' && LA21_0<='\u040F')||(LA21_0>='\u0450' && LA21_0<='\u3040')||(LA21_0>='\u3100' && LA21_0<='\u31EF')||(LA21_0>='\u3200' && LA21_0<='\u33FF')||(LA21_0>='\u4DC0' && LA21_0<='\u4DFF')||(LA21_0>='\uA000' && LA21_0<='\uF8FF')||(LA21_0>='\uFB00' && LA21_0<='\uFFFF')) ) {s = 45;}

                        else s = 42;

                        if ( s>=0 ) return s;
                        break;
                    case 29 : 
                        int LA21_240 = input.LA(1);

                        s = -1;
                        if ( (LA21_240=='E'||LA21_240=='e') ) {s = 279;}

                        else if ( ((LA21_240>='\u0000' && LA21_240<='D')||(LA21_240>='F' && LA21_240<='d')||(LA21_240>='f' && LA21_240<='\uFFFF')) ) {s = 59;}

                        if ( s>=0 ) return s;
                        break;
                    case 30 : 
                        int LA21_279 = input.LA(1);

                        s = -1;
                        if ( (LA21_279=='E'||LA21_279=='e') ) {s = 302;}

                        else if ( ((LA21_279>='\u0000' && LA21_279<='D')||(LA21_279>='F' && LA21_279<='d')||(LA21_279>='f' && LA21_279<='\uFFFF')) ) {s = 59;}

                        if ( s>=0 ) return s;
                        break;
                    case 31 : 
                        int LA21_302 = input.LA(1);

                        s = -1;
                        if ( (LA21_302=='N'||LA21_302=='n') ) {s = 317;}

                        else if ( ((LA21_302>='\u0000' && LA21_302<='M')||(LA21_302>='O' && LA21_302<='m')||(LA21_302>='o' && LA21_302<='\uFFFF')) ) {s = 59;}

                        if ( s>=0 ) return s;
                        break;
                    case 32 : 
                        int LA21_269 = input.LA(1);

                        s = -1;
                        if ( (LA21_269=='-') ) {s = 297;}

                        else if ( ((LA21_269>='\u0000' && LA21_269<=',')||(LA21_269>='.' && LA21_269<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 33 : 
                        int LA21_220 = input.LA(1);

                        s = -1;
                        if ( ((LA21_220>='0' && LA21_220<='9')) ) {s = 269;}

                        else if ( ((LA21_220>='\u0000' && LA21_220<='/')||(LA21_220>=':' && LA21_220<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 34 : 
                        int LA21_312 = input.LA(1);

                        s = -1;
                        if ( ((LA21_312>='0' && LA21_312<='9')) ) {s = 324;}

                        else if ( ((LA21_312>='\u0000' && LA21_312<='/')||(LA21_312>=':' && LA21_312<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 35 : 
                        int LA21_297 = input.LA(1);

                        s = -1;
                        if ( ((LA21_297>='0' && LA21_297<='1')) ) {s = 312;}

                        else if ( ((LA21_297>='\u0000' && LA21_297<='/')||(LA21_297>='2' && LA21_297<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 36 : 
                        int LA21_332 = input.LA(1);

                        s = -1;
                        if ( ((LA21_332>='0' && LA21_332<='3')) ) {s = 336;}

                        else if ( ((LA21_332>='\u0000' && LA21_332<='/')||(LA21_332>='4' && LA21_332<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 37 : 
                        int LA21_324 = input.LA(1);

                        s = -1;
                        if ( (LA21_324=='-') ) {s = 332;}

                        else if ( ((LA21_324>='\u0000' && LA21_324<=',')||(LA21_324>='.' && LA21_324<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 38 : 
                        int LA21_338 = input.LA(1);

                        s = -1;
                        if ( (LA21_338=='\'') ) {s = 340;}

                        else if ( ((LA21_338>='\u0000' && LA21_338<='&')||(LA21_338>='(' && LA21_338<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 39 : 
                        int LA21_336 = input.LA(1);

                        s = -1;
                        if ( ((LA21_336>='0' && LA21_336<='9')) ) {s = 338;}

                        else if ( ((LA21_336>='\u0000' && LA21_336<='/')||(LA21_336>=':' && LA21_336<='\uFFFF')) ) {s = 115;}

                        if ( s>=0 ) return s;
                        break;
                    case 40 : 
                        int LA21_40 = input.LA(1);

                        s = -1;
                        if ( ((LA21_40>='\u0000' && LA21_40<='\uFFFF')) ) {s = 59;}

                        else s = 45;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 21, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}
