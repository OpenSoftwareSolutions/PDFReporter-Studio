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
package com.jaspersoft.studio.editor.jrexpressions.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.eclipse.xtext.parser.antlr.Lexer;

@SuppressWarnings("all")
public class InternalJavaJRExpressionLexer extends Lexer {
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int RULE_ID=11;
    public static final int T__66=66;
    public static final int T__67=67;
    public static final int T__64=64;
    public static final int T__29=29;
    public static final int T__65=65;
    public static final int T__28=28;
    public static final int T__62=62;
    public static final int T__27=27;
    public static final int T__63=63;
    public static final int T__26=26;
    public static final int RULE_ANY_OTHER=25;
    public static final int RULE_HEXDIGIT=18;
    public static final int T__61=61;
    public static final int EOF=-1;
    public static final int T__60=60;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int RULE_NONINTEGERNUMBER=14;
    public static final int T__59=59;
    public static final int RULE_FLOATSUFFIX=15;
    public static final int RULE_INT=5;
    public static final int RULE_CHAR=9;
    public static final int RULE_LONGSUFFIX=13;
    public static final int T__50=50;
    public static final int T__42=42;
    public static final int RULE_ESCAPESEQUENCE=17;
    public static final int T__43=43;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int RULE_EXPOBJIDENTIFIER=21;
    public static final int T__48=48;
    public static final int RULE_LONG=6;
    public static final int T__49=49;
    public static final int RULE_HEXPREFIX=19;
    public static final int RULE_BRACED_IDENTIFIER=4;
    public static final int RULE_FLOAT=7;
    public static final int RULE_SL_COMMENT=23;
    public static final int RULE_DOUBLE=8;
    public static final int RULE_ML_COMMENT=22;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int RULE_DOUBLESUFFIX=16;
    public static final int T__32=32;
    public static final int RULE_STRING=10;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int RULE_EXPONENT=20;
    public static final int RULE_INTEGERNUMBER=12;
    public static final int RULE_WS=24;

    // delegates
    // delegators

    public InternalJavaJRExpressionLexer() {;} 
    public InternalJavaJRExpressionLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public InternalJavaJRExpressionLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g"; }

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:11:7: ( '?' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:11:9: '?'
            {
            match('?'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:12:7: ( ':' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:12:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:13:7: ( '||' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:13:9: '||'
            {
            match("||"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:14:7: ( '&&' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:14:9: '&&'
            {
            match("&&"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:15:7: ( '==' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:15:9: '=='
            {
            match("=="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:16:7: ( '!=' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:16:9: '!='
            {
            match("!="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:17:7: ( 'instanceof' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:17:9: 'instanceof'
            {
            match("instanceof"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:18:7: ( '+' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:18:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "T__34"
    public final void mT__34() throws RecognitionException {
        try {
            int _type = T__34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:19:7: ( '-' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:19:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__34"

    // $ANTLR start "T__35"
    public final void mT__35() throws RecognitionException {
        try {
            int _type = T__35;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:20:7: ( '*' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:20:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__35"

    // $ANTLR start "T__36"
    public final void mT__36() throws RecognitionException {
        try {
            int _type = T__36;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:21:7: ( '/' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:21:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__36"

    // $ANTLR start "T__37"
    public final void mT__37() throws RecognitionException {
        try {
            int _type = T__37;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:22:7: ( '%' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:22:9: '%'
            {
            match('%'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__37"

    // $ANTLR start "T__38"
    public final void mT__38() throws RecognitionException {
        try {
            int _type = T__38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:23:7: ( '~' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:23:9: '~'
            {
            match('~'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__38"

    // $ANTLR start "T__39"
    public final void mT__39() throws RecognitionException {
        try {
            int _type = T__39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:24:7: ( '!' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:24:9: '!'
            {
            match('!'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__39"

    // $ANTLR start "T__40"
    public final void mT__40() throws RecognitionException {
        try {
            int _type = T__40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:25:7: ( '.' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:25:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__40"

    // $ANTLR start "T__41"
    public final void mT__41() throws RecognitionException {
        try {
            int _type = T__41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:26:7: ( '$F' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:26:9: '$F'
            {
            match("$F"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__41"

    // $ANTLR start "T__42"
    public final void mT__42() throws RecognitionException {
        try {
            int _type = T__42;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:27:7: ( '$P' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:27:9: '$P'
            {
            match("$P"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__42"

    // $ANTLR start "T__43"
    public final void mT__43() throws RecognitionException {
        try {
            int _type = T__43;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:28:7: ( '$V' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:28:9: '$V'
            {
            match("$V"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__43"

    // $ANTLR start "T__44"
    public final void mT__44() throws RecognitionException {
        try {
            int _type = T__44;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:29:7: ( '$R' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:29:9: '$R'
            {
            match("$R"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__44"

    // $ANTLR start "T__45"
    public final void mT__45() throws RecognitionException {
        try {
            int _type = T__45;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:30:7: ( 'new' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:30:9: 'new'
            {
            match("new"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__45"

    // $ANTLR start "T__46"
    public final void mT__46() throws RecognitionException {
        try {
            int _type = T__46;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:31:7: ( '[' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:31:9: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__46"

    // $ANTLR start "T__47"
    public final void mT__47() throws RecognitionException {
        try {
            int _type = T__47;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:32:7: ( ']' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:32:9: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__47"

    // $ANTLR start "T__48"
    public final void mT__48() throws RecognitionException {
        try {
            int _type = T__48;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:33:7: ( 'false' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:33:9: 'false'
            {
            match("false"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__48"

    // $ANTLR start "T__49"
    public final void mT__49() throws RecognitionException {
        try {
            int _type = T__49;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:34:7: ( 'true' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:34:9: 'true'
            {
            match("true"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__49"

    // $ANTLR start "T__50"
    public final void mT__50() throws RecognitionException {
        try {
            int _type = T__50;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:35:7: ( 'null' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:35:9: 'null'
            {
            match("null"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__50"

    // $ANTLR start "T__51"
    public final void mT__51() throws RecognitionException {
        try {
            int _type = T__51;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:36:7: ( '(' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:36:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__51"

    // $ANTLR start "T__52"
    public final void mT__52() throws RecognitionException {
        try {
            int _type = T__52;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:37:7: ( ')' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:37:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__52"

    // $ANTLR start "T__53"
    public final void mT__53() throws RecognitionException {
        try {
            int _type = T__53;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:38:7: ( '{' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:38:9: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__53"

    // $ANTLR start "T__54"
    public final void mT__54() throws RecognitionException {
        try {
            int _type = T__54;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:39:7: ( '}' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:39:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__54"

    // $ANTLR start "T__55"
    public final void mT__55() throws RecognitionException {
        try {
            int _type = T__55;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:40:7: ( ',' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:40:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__55"

    // $ANTLR start "T__56"
    public final void mT__56() throws RecognitionException {
        try {
            int _type = T__56;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:41:7: ( '<' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:41:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__56"

    // $ANTLR start "T__57"
    public final void mT__57() throws RecognitionException {
        try {
            int _type = T__57;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:42:7: ( '>' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:42:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__57"

    // $ANTLR start "T__58"
    public final void mT__58() throws RecognitionException {
        try {
            int _type = T__58;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:43:7: ( 'extends' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:43:9: 'extends'
            {
            match("extends"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__58"

    // $ANTLR start "T__59"
    public final void mT__59() throws RecognitionException {
        try {
            int _type = T__59;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:44:7: ( 'super' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:44:9: 'super'
            {
            match("super"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__59"

    // $ANTLR start "T__60"
    public final void mT__60() throws RecognitionException {
        try {
            int _type = T__60;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:45:7: ( 'boolean' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:45:9: 'boolean'
            {
            match("boolean"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__60"

    // $ANTLR start "T__61"
    public final void mT__61() throws RecognitionException {
        try {
            int _type = T__61;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:46:7: ( 'char' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:46:9: 'char'
            {
            match("char"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__61"

    // $ANTLR start "T__62"
    public final void mT__62() throws RecognitionException {
        try {
            int _type = T__62;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:47:7: ( 'byte' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:47:9: 'byte'
            {
            match("byte"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__62"

    // $ANTLR start "T__63"
    public final void mT__63() throws RecognitionException {
        try {
            int _type = T__63;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:48:7: ( 'short' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:48:9: 'short'
            {
            match("short"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__63"

    // $ANTLR start "T__64"
    public final void mT__64() throws RecognitionException {
        try {
            int _type = T__64;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:49:7: ( 'int' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:49:9: 'int'
            {
            match("int"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__64"

    // $ANTLR start "T__65"
    public final void mT__65() throws RecognitionException {
        try {
            int _type = T__65;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:50:7: ( 'long' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:50:9: 'long'
            {
            match("long"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__65"

    // $ANTLR start "T__66"
    public final void mT__66() throws RecognitionException {
        try {
            int _type = T__66;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:51:7: ( 'float' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:51:9: 'float'
            {
            match("float"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__66"

    // $ANTLR start "T__67"
    public final void mT__67() throws RecognitionException {
        try {
            int _type = T__67;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:52:7: ( 'double' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:52:9: 'double'
            {
            match("double"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__67"

    // $ANTLR start "T__68"
    public final void mT__68() throws RecognitionException {
        try {
            int _type = T__68;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:53:7: ( '<=' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:53:9: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__68"

    // $ANTLR start "T__69"
    public final void mT__69() throws RecognitionException {
        try {
            int _type = T__69;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:54:7: ( '>=' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:54:9: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__69"

    // $ANTLR start "RULE_LONG"
    public final void mRULE_LONG() throws RecognitionException {
        try {
            int _type = RULE_LONG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3098:11: ( RULE_INTEGERNUMBER RULE_LONGSUFFIX )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3098:13: RULE_INTEGERNUMBER RULE_LONGSUFFIX
            {
            mRULE_INTEGERNUMBER(); 
            mRULE_LONGSUFFIX(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_LONG"

    // $ANTLR start "RULE_INT"
    public final void mRULE_INT() throws RecognitionException {
        try {
            int _type = RULE_INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3100:10: ( RULE_INTEGERNUMBER )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3100:12: RULE_INTEGERNUMBER
            {
            mRULE_INTEGERNUMBER(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_INT"

    // $ANTLR start "RULE_FLOAT"
    public final void mRULE_FLOAT() throws RecognitionException {
        try {
            int _type = RULE_FLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3102:12: ( RULE_NONINTEGERNUMBER RULE_FLOATSUFFIX )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3102:14: RULE_NONINTEGERNUMBER RULE_FLOATSUFFIX
            {
            mRULE_NONINTEGERNUMBER(); 
            mRULE_FLOATSUFFIX(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_FLOAT"

    // $ANTLR start "RULE_DOUBLE"
    public final void mRULE_DOUBLE() throws RecognitionException {
        try {
            int _type = RULE_DOUBLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3104:13: ( RULE_NONINTEGERNUMBER ( RULE_DOUBLESUFFIX )? )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3104:15: RULE_NONINTEGERNUMBER ( RULE_DOUBLESUFFIX )?
            {
            mRULE_NONINTEGERNUMBER(); 
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3104:37: ( RULE_DOUBLESUFFIX )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='D'||LA1_0=='d') ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3104:37: RULE_DOUBLESUFFIX
                    {
                    mRULE_DOUBLESUFFIX(); 

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
    // $ANTLR end "RULE_DOUBLE"

    // $ANTLR start "RULE_CHAR"
    public final void mRULE_CHAR() throws RecognitionException {
        try {
            int _type = RULE_CHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3106:11: ( '\\'' ( RULE_ESCAPESEQUENCE | ~ ( ( '\\'' | '\\\\' | '\\r' | '\\n' ) ) ) '\\'' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3106:13: '\\'' ( RULE_ESCAPESEQUENCE | ~ ( ( '\\'' | '\\\\' | '\\r' | '\\n' ) ) ) '\\''
            {
            match('\''); 
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3106:18: ( RULE_ESCAPESEQUENCE | ~ ( ( '\\'' | '\\\\' | '\\r' | '\\n' ) ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='\\') ) {
                alt2=1;
            }
            else if ( ((LA2_0>='\u0000' && LA2_0<='\t')||(LA2_0>='\u000B' && LA2_0<='\f')||(LA2_0>='\u000E' && LA2_0<='&')||(LA2_0>='(' && LA2_0<='[')||(LA2_0>=']' && LA2_0<='\uFFFF')) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3106:19: RULE_ESCAPESEQUENCE
                    {
                    mRULE_ESCAPESEQUENCE(); 

                    }
                    break;
                case 2 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3106:39: ~ ( ( '\\'' | '\\\\' | '\\r' | '\\n' ) )
                    {
                    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;

            }

            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_CHAR"

    // $ANTLR start "RULE_STRING"
    public final void mRULE_STRING() throws RecognitionException {
        try {
            int _type = RULE_STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3108:13: ( '\"' ( RULE_ESCAPESEQUENCE | ~ ( ( '\\\\' | '\"' | '\\r' | '\\n' ) ) )* '\"' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3108:15: '\"' ( RULE_ESCAPESEQUENCE | ~ ( ( '\\\\' | '\"' | '\\r' | '\\n' ) ) )* '\"'
            {
            match('\"'); 
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3108:19: ( RULE_ESCAPESEQUENCE | ~ ( ( '\\\\' | '\"' | '\\r' | '\\n' ) ) )*
            loop3:
            do {
                int alt3=3;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='\\') ) {
                    alt3=1;
                }
                else if ( ((LA3_0>='\u0000' && LA3_0<='\t')||(LA3_0>='\u000B' && LA3_0<='\f')||(LA3_0>='\u000E' && LA3_0<='!')||(LA3_0>='#' && LA3_0<='[')||(LA3_0>=']' && LA3_0<='\uFFFF')) ) {
                    alt3=2;
                }


                switch (alt3) {
            	case 1 :
            	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3108:20: RULE_ESCAPESEQUENCE
            	    {
            	    mRULE_ESCAPESEQUENCE(); 

            	    }
            	    break;
            	case 2 :
            	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3108:40: ~ ( ( '\\\\' | '\"' | '\\r' | '\\n' ) )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop3;
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

    // $ANTLR start "RULE_ESCAPESEQUENCE"
    public final void mRULE_ESCAPESEQUENCE() throws RecognitionException {
        try {
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3110:30: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' | '0' .. '3' '0' .. '7' '0' .. '7' | '0' .. '7' '0' .. '7' | '0' .. '7' | 'u' RULE_HEXDIGIT RULE_HEXDIGIT RULE_HEXDIGIT RULE_HEXDIGIT ) )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3110:32: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' | '0' .. '3' '0' .. '7' '0' .. '7' | '0' .. '7' '0' .. '7' | '0' .. '7' | 'u' RULE_HEXDIGIT RULE_HEXDIGIT RULE_HEXDIGIT RULE_HEXDIGIT )
            {
            match('\\'); 
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3110:37: ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' | '0' .. '3' '0' .. '7' '0' .. '7' | '0' .. '7' '0' .. '7' | '0' .. '7' | 'u' RULE_HEXDIGIT RULE_HEXDIGIT RULE_HEXDIGIT RULE_HEXDIGIT )
            int alt4=12;
            alt4 = dfa4.predict(input);
            switch (alt4) {
                case 1 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3110:38: 'b'
                    {
                    match('b'); 

                    }
                    break;
                case 2 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3110:42: 't'
                    {
                    match('t'); 

                    }
                    break;
                case 3 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3110:46: 'n'
                    {
                    match('n'); 

                    }
                    break;
                case 4 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3110:50: 'f'
                    {
                    match('f'); 

                    }
                    break;
                case 5 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3110:54: 'r'
                    {
                    match('r'); 

                    }
                    break;
                case 6 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3110:58: '\"'
                    {
                    match('\"'); 

                    }
                    break;
                case 7 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3110:62: '\\''
                    {
                    match('\''); 

                    }
                    break;
                case 8 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3110:67: '\\\\'
                    {
                    match('\\'); 

                    }
                    break;
                case 9 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3110:72: '0' .. '3' '0' .. '7' '0' .. '7'
                    {
                    matchRange('0','3'); 
                    matchRange('0','7'); 
                    matchRange('0','7'); 

                    }
                    break;
                case 10 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3110:99: '0' .. '7' '0' .. '7'
                    {
                    matchRange('0','7'); 
                    matchRange('0','7'); 

                    }
                    break;
                case 11 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3110:117: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }
                    break;
                case 12 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3110:126: 'u' RULE_HEXDIGIT RULE_HEXDIGIT RULE_HEXDIGIT RULE_HEXDIGIT
                    {
                    match('u'); 
                    mRULE_HEXDIGIT(); 
                    mRULE_HEXDIGIT(); 
                    mRULE_HEXDIGIT(); 
                    mRULE_HEXDIGIT(); 

                    }
                    break;

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "RULE_ESCAPESEQUENCE"

    // $ANTLR start "RULE_INTEGERNUMBER"
    public final void mRULE_INTEGERNUMBER() throws RecognitionException {
        try {
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3112:29: ( ( '0' | '1' .. '9' ( '0' .. '9' )* | '0' ( '0' .. '7' )+ | RULE_HEXPREFIX ( RULE_HEXDIGIT )+ ) )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3112:31: ( '0' | '1' .. '9' ( '0' .. '9' )* | '0' ( '0' .. '7' )+ | RULE_HEXPREFIX ( RULE_HEXDIGIT )+ )
            {
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3112:31: ( '0' | '1' .. '9' ( '0' .. '9' )* | '0' ( '0' .. '7' )+ | RULE_HEXPREFIX ( RULE_HEXDIGIT )+ )
            int alt8=4;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='0') ) {
                switch ( input.LA(2) ) {
                case 'X':
                case 'x':
                    {
                    alt8=4;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    {
                    alt8=3;
                    }
                    break;
                default:
                    alt8=1;}

            }
            else if ( ((LA8_0>='1' && LA8_0<='9')) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3112:32: '0'
                    {
                    match('0'); 

                    }
                    break;
                case 2 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3112:36: '1' .. '9' ( '0' .. '9' )*
                    {
                    matchRange('1','9'); 
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3112:45: ( '0' .. '9' )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3112:46: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);


                    }
                    break;
                case 3 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3112:57: '0' ( '0' .. '7' )+
                    {
                    match('0'); 
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3112:61: ( '0' .. '7' )+
                    int cnt6=0;
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( ((LA6_0>='0' && LA6_0<='7')) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3112:62: '0' .. '7'
                    	    {
                    	    matchRange('0','7'); 

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


                    }
                    break;
                case 4 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3112:73: RULE_HEXPREFIX ( RULE_HEXDIGIT )+
                    {
                    mRULE_HEXPREFIX(); 
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3112:88: ( RULE_HEXDIGIT )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( ((LA7_0>='0' && LA7_0<='9')||(LA7_0>='A' && LA7_0<='F')||(LA7_0>='a' && LA7_0<='f')) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3112:88: RULE_HEXDIGIT
                    	    {
                    	    mRULE_HEXDIGIT(); 

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

        }
        finally {
        }
    }
    // $ANTLR end "RULE_INTEGERNUMBER"

    // $ANTLR start "RULE_HEXPREFIX"
    public final void mRULE_HEXPREFIX() throws RecognitionException {
        try {
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3114:25: ( ( '0x' | '0X' ) )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3114:27: ( '0x' | '0X' )
            {
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3114:27: ( '0x' | '0X' )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='0') ) {
                int LA9_1 = input.LA(2);

                if ( (LA9_1=='x') ) {
                    alt9=1;
                }
                else if ( (LA9_1=='X') ) {
                    alt9=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3114:28: '0x'
                    {
                    match("0x"); 


                    }
                    break;
                case 2 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3114:33: '0X'
                    {
                    match("0X"); 


                    }
                    break;

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "RULE_HEXPREFIX"

    // $ANTLR start "RULE_HEXDIGIT"
    public final void mRULE_HEXDIGIT() throws RecognitionException {
        try {
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3116:24: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3116:26: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='F')||(input.LA(1)>='a' && input.LA(1)<='f') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "RULE_HEXDIGIT"

    // $ANTLR start "RULE_LONGSUFFIX"
    public final void mRULE_LONGSUFFIX() throws RecognitionException {
        try {
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3118:26: ( ( 'l' | 'L' ) )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3118:28: ( 'l' | 'L' )
            {
            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "RULE_LONGSUFFIX"

    // $ANTLR start "RULE_NONINTEGERNUMBER"
    public final void mRULE_NONINTEGERNUMBER() throws RecognitionException {
        try {
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:32: ( ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( RULE_EXPONENT )? | '.' ( '0' .. '9' )+ ( RULE_EXPONENT )? | ( '0' .. '9' )+ RULE_EXPONENT | ( '0' .. '9' )+ | RULE_HEXPREFIX ( RULE_HEXDIGIT )* '.' ( RULE_HEXDIGIT )* ( 'p' | 'P' ) ( '+' | '-' )? ( '0' .. '9' )+ ) )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:34: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( RULE_EXPONENT )? | '.' ( '0' .. '9' )+ ( RULE_EXPONENT )? | ( '0' .. '9' )+ RULE_EXPONENT | ( '0' .. '9' )+ | RULE_HEXPREFIX ( RULE_HEXDIGIT )* '.' ( RULE_HEXDIGIT )* ( 'p' | 'P' ) ( '+' | '-' )? ( '0' .. '9' )+ )
            {
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:34: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( RULE_EXPONENT )? | '.' ( '0' .. '9' )+ ( RULE_EXPONENT )? | ( '0' .. '9' )+ RULE_EXPONENT | ( '0' .. '9' )+ | RULE_HEXPREFIX ( RULE_HEXDIGIT )* '.' ( RULE_HEXDIGIT )* ( 'p' | 'P' ) ( '+' | '-' )? ( '0' .. '9' )+ )
            int alt21=5;
            alt21 = dfa21.predict(input);
            switch (alt21) {
                case 1 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:35: ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( RULE_EXPONENT )?
                    {
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:35: ( '0' .. '9' )+
                    int cnt10=0;
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( ((LA10_0>='0' && LA10_0<='9')) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:36: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt10 >= 1 ) break loop10;
                                EarlyExitException eee =
                                    new EarlyExitException(10, input);
                                throw eee;
                        }
                        cnt10++;
                    } while (true);

                    match('.'); 
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:51: ( '0' .. '9' )*
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( ((LA11_0>='0' && LA11_0<='9')) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:52: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop11;
                        }
                    } while (true);

                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:63: ( RULE_EXPONENT )?
                    int alt12=2;
                    int LA12_0 = input.LA(1);

                    if ( (LA12_0=='E'||LA12_0=='e') ) {
                        alt12=1;
                    }
                    switch (alt12) {
                        case 1 :
                            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:63: RULE_EXPONENT
                            {
                            mRULE_EXPONENT(); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:78: '.' ( '0' .. '9' )+ ( RULE_EXPONENT )?
                    {
                    match('.'); 
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:82: ( '0' .. '9' )+
                    int cnt13=0;
                    loop13:
                    do {
                        int alt13=2;
                        int LA13_0 = input.LA(1);

                        if ( ((LA13_0>='0' && LA13_0<='9')) ) {
                            alt13=1;
                        }


                        switch (alt13) {
                    	case 1 :
                    	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:83: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt13 >= 1 ) break loop13;
                                EarlyExitException eee =
                                    new EarlyExitException(13, input);
                                throw eee;
                        }
                        cnt13++;
                    } while (true);

                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:94: ( RULE_EXPONENT )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0=='E'||LA14_0=='e') ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:94: RULE_EXPONENT
                            {
                            mRULE_EXPONENT(); 

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:109: ( '0' .. '9' )+ RULE_EXPONENT
                    {
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:109: ( '0' .. '9' )+
                    int cnt15=0;
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( ((LA15_0>='0' && LA15_0<='9')) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:110: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt15 >= 1 ) break loop15;
                                EarlyExitException eee =
                                    new EarlyExitException(15, input);
                                throw eee;
                        }
                        cnt15++;
                    } while (true);

                    mRULE_EXPONENT(); 

                    }
                    break;
                case 4 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:135: ( '0' .. '9' )+
                    {
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:135: ( '0' .. '9' )+
                    int cnt16=0;
                    loop16:
                    do {
                        int alt16=2;
                        int LA16_0 = input.LA(1);

                        if ( ((LA16_0>='0' && LA16_0<='9')) ) {
                            alt16=1;
                        }


                        switch (alt16) {
                    	case 1 :
                    	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:136: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt16 >= 1 ) break loop16;
                                EarlyExitException eee =
                                    new EarlyExitException(16, input);
                                throw eee;
                        }
                        cnt16++;
                    } while (true);


                    }
                    break;
                case 5 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:147: RULE_HEXPREFIX ( RULE_HEXDIGIT )* '.' ( RULE_HEXDIGIT )* ( 'p' | 'P' ) ( '+' | '-' )? ( '0' .. '9' )+
                    {
                    mRULE_HEXPREFIX(); 
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:162: ( RULE_HEXDIGIT )*
                    loop17:
                    do {
                        int alt17=2;
                        int LA17_0 = input.LA(1);

                        if ( ((LA17_0>='0' && LA17_0<='9')||(LA17_0>='A' && LA17_0<='F')||(LA17_0>='a' && LA17_0<='f')) ) {
                            alt17=1;
                        }


                        switch (alt17) {
                    	case 1 :
                    	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:162: RULE_HEXDIGIT
                    	    {
                    	    mRULE_HEXDIGIT(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop17;
                        }
                    } while (true);

                    match('.'); 
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:181: ( RULE_HEXDIGIT )*
                    loop18:
                    do {
                        int alt18=2;
                        int LA18_0 = input.LA(1);

                        if ( ((LA18_0>='0' && LA18_0<='9')||(LA18_0>='A' && LA18_0<='F')||(LA18_0>='a' && LA18_0<='f')) ) {
                            alt18=1;
                        }


                        switch (alt18) {
                    	case 1 :
                    	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:181: RULE_HEXDIGIT
                    	    {
                    	    mRULE_HEXDIGIT(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop18;
                        }
                    } while (true);

                    if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}

                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:206: ( '+' | '-' )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0=='+'||LA19_0=='-') ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:
                            {
                            if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                                input.consume();

                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                recover(mse);
                                throw mse;}


                            }
                            break;

                    }

                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:217: ( '0' .. '9' )+
                    int cnt20=0;
                    loop20:
                    do {
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( ((LA20_0>='0' && LA20_0<='9')) ) {
                            alt20=1;
                        }


                        switch (alt20) {
                    	case 1 :
                    	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3120:218: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

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
                    break;

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "RULE_NONINTEGERNUMBER"

    // $ANTLR start "RULE_EXPONENT"
    public final void mRULE_EXPONENT() throws RecognitionException {
        try {
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3122:24: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3122:26: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3122:36: ( '+' | '-' )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0=='+'||LA22_0=='-') ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;

            }

            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3122:47: ( '0' .. '9' )+
            int cnt23=0;
            loop23:
            do {
                int alt23=2;
                int LA23_0 = input.LA(1);

                if ( ((LA23_0>='0' && LA23_0<='9')) ) {
                    alt23=1;
                }


                switch (alt23) {
            	case 1 :
            	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3122:48: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt23 >= 1 ) break loop23;
                        EarlyExitException eee =
                            new EarlyExitException(23, input);
                        throw eee;
                }
                cnt23++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "RULE_EXPONENT"

    // $ANTLR start "RULE_FLOATSUFFIX"
    public final void mRULE_FLOATSUFFIX() throws RecognitionException {
        try {
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3124:27: ( ( 'f' | 'F' ) )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3124:29: ( 'f' | 'F' )
            {
            if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "RULE_FLOATSUFFIX"

    // $ANTLR start "RULE_DOUBLESUFFIX"
    public final void mRULE_DOUBLESUFFIX() throws RecognitionException {
        try {
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3126:28: ( ( 'd' | 'D' ) )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3126:30: ( 'd' | 'D' )
            {
            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "RULE_DOUBLESUFFIX"

    // $ANTLR start "RULE_BRACED_IDENTIFIER"
    public final void mRULE_BRACED_IDENTIFIER() throws RecognitionException {
        try {
            int _type = RULE_BRACED_IDENTIFIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3128:24: ( '{' RULE_EXPOBJIDENTIFIER '}' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3128:26: '{' RULE_EXPOBJIDENTIFIER '}'
            {
            match('{'); 
            mRULE_EXPOBJIDENTIFIER(); 
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RULE_BRACED_IDENTIFIER"

    // $ANTLR start "RULE_EXPOBJIDENTIFIER"
    public final void mRULE_EXPOBJIDENTIFIER() throws RecognitionException {
        try {
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3130:32: ( (~ ( ( '\\r' | '\\n' | '}' ) ) )+ )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3130:34: (~ ( ( '\\r' | '\\n' | '}' ) ) )+
            {
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3130:34: (~ ( ( '\\r' | '\\n' | '}' ) ) )+
            int cnt24=0;
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( ((LA24_0>='\u0000' && LA24_0<='\t')||(LA24_0>='\u000B' && LA24_0<='\f')||(LA24_0>='\u000E' && LA24_0<='|')||(LA24_0>='~' && LA24_0<='\uFFFF')) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3130:34: ~ ( ( '\\r' | '\\n' | '}' ) )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='|')||(input.LA(1)>='~' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt24 >= 1 ) break loop24;
                        EarlyExitException eee =
                            new EarlyExitException(24, input);
                        throw eee;
                }
                cnt24++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "RULE_EXPOBJIDENTIFIER"

    // $ANTLR start "RULE_ID"
    public final void mRULE_ID() throws RecognitionException {
        try {
            int _type = RULE_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3132:9: ( ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3132:11: ( '^' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            {
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3132:11: ( '^' )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0=='^') ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3132:11: '^'
                    {
                    match('^'); 

                    }
                    break;

            }

            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3132:40: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( ((LA26_0>='0' && LA26_0<='9')||(LA26_0>='A' && LA26_0<='Z')||LA26_0=='_'||(LA26_0>='a' && LA26_0<='z')) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop26;
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

    // $ANTLR start "RULE_ML_COMMENT"
    public final void mRULE_ML_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_ML_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3134:17: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3134:19: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3134:24: ( options {greedy=false; } : . )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0=='*') ) {
                    int LA27_1 = input.LA(2);

                    if ( (LA27_1=='/') ) {
                        alt27=2;
                    }
                    else if ( ((LA27_1>='\u0000' && LA27_1<='.')||(LA27_1>='0' && LA27_1<='\uFFFF')) ) {
                        alt27=1;
                    }


                }
                else if ( ((LA27_0>='\u0000' && LA27_0<=')')||(LA27_0>='+' && LA27_0<='\uFFFF')) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3134:52: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop27;
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

    // $ANTLR start "RULE_SL_COMMENT"
    public final void mRULE_SL_COMMENT() throws RecognitionException {
        try {
            int _type = RULE_SL_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3136:17: ( '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )? )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3136:19: '//' (~ ( ( '\\n' | '\\r' ) ) )* ( ( '\\r' )? '\\n' )?
            {
            match("//"); 

            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3136:24: (~ ( ( '\\n' | '\\r' ) ) )*
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( ((LA28_0>='\u0000' && LA28_0<='\t')||(LA28_0>='\u000B' && LA28_0<='\f')||(LA28_0>='\u000E' && LA28_0<='\uFFFF')) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3136:24: ~ ( ( '\\n' | '\\r' ) )
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
            	    break loop28;
                }
            } while (true);

            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3136:40: ( ( '\\r' )? '\\n' )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0=='\n'||LA30_0=='\r') ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3136:41: ( '\\r' )? '\\n'
                    {
                    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3136:41: ( '\\r' )?
                    int alt29=2;
                    int LA29_0 = input.LA(1);

                    if ( (LA29_0=='\r') ) {
                        alt29=1;
                    }
                    switch (alt29) {
                        case 1 :
                            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3136:41: '\\r'
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

    // $ANTLR start "RULE_WS"
    public final void mRULE_WS() throws RecognitionException {
        try {
            int _type = RULE_WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3138:9: ( ( ' ' | '\\t' | '\\r' | '\\n' )+ )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3138:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            {
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3138:11: ( ' ' | '\\t' | '\\r' | '\\n' )+
            int cnt31=0;
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( ((LA31_0>='\t' && LA31_0<='\n')||LA31_0=='\r'||LA31_0==' ') ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:
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
            	    if ( cnt31 >= 1 ) break loop31;
                        EarlyExitException eee =
                            new EarlyExitException(31, input);
                        throw eee;
                }
                cnt31++;
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
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3140:16: ( . )
            // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:3140:18: .
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
        // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:8: ( T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | RULE_LONG | RULE_INT | RULE_FLOAT | RULE_DOUBLE | RULE_CHAR | RULE_STRING | RULE_BRACED_IDENTIFIER | RULE_ID | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER )
        int alt32=56;
        alt32 = dfa32.predict(input);
        switch (alt32) {
            case 1 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:10: T__26
                {
                mT__26(); 

                }
                break;
            case 2 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:16: T__27
                {
                mT__27(); 

                }
                break;
            case 3 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:22: T__28
                {
                mT__28(); 

                }
                break;
            case 4 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:28: T__29
                {
                mT__29(); 

                }
                break;
            case 5 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:34: T__30
                {
                mT__30(); 

                }
                break;
            case 6 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:40: T__31
                {
                mT__31(); 

                }
                break;
            case 7 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:46: T__32
                {
                mT__32(); 

                }
                break;
            case 8 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:52: T__33
                {
                mT__33(); 

                }
                break;
            case 9 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:58: T__34
                {
                mT__34(); 

                }
                break;
            case 10 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:64: T__35
                {
                mT__35(); 

                }
                break;
            case 11 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:70: T__36
                {
                mT__36(); 

                }
                break;
            case 12 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:76: T__37
                {
                mT__37(); 

                }
                break;
            case 13 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:82: T__38
                {
                mT__38(); 

                }
                break;
            case 14 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:88: T__39
                {
                mT__39(); 

                }
                break;
            case 15 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:94: T__40
                {
                mT__40(); 

                }
                break;
            case 16 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:100: T__41
                {
                mT__41(); 

                }
                break;
            case 17 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:106: T__42
                {
                mT__42(); 

                }
                break;
            case 18 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:112: T__43
                {
                mT__43(); 

                }
                break;
            case 19 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:118: T__44
                {
                mT__44(); 

                }
                break;
            case 20 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:124: T__45
                {
                mT__45(); 

                }
                break;
            case 21 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:130: T__46
                {
                mT__46(); 

                }
                break;
            case 22 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:136: T__47
                {
                mT__47(); 

                }
                break;
            case 23 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:142: T__48
                {
                mT__48(); 

                }
                break;
            case 24 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:148: T__49
                {
                mT__49(); 

                }
                break;
            case 25 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:154: T__50
                {
                mT__50(); 

                }
                break;
            case 26 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:160: T__51
                {
                mT__51(); 

                }
                break;
            case 27 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:166: T__52
                {
                mT__52(); 

                }
                break;
            case 28 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:172: T__53
                {
                mT__53(); 

                }
                break;
            case 29 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:178: T__54
                {
                mT__54(); 

                }
                break;
            case 30 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:184: T__55
                {
                mT__55(); 

                }
                break;
            case 31 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:190: T__56
                {
                mT__56(); 

                }
                break;
            case 32 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:196: T__57
                {
                mT__57(); 

                }
                break;
            case 33 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:202: T__58
                {
                mT__58(); 

                }
                break;
            case 34 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:208: T__59
                {
                mT__59(); 

                }
                break;
            case 35 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:214: T__60
                {
                mT__60(); 

                }
                break;
            case 36 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:220: T__61
                {
                mT__61(); 

                }
                break;
            case 37 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:226: T__62
                {
                mT__62(); 

                }
                break;
            case 38 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:232: T__63
                {
                mT__63(); 

                }
                break;
            case 39 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:238: T__64
                {
                mT__64(); 

                }
                break;
            case 40 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:244: T__65
                {
                mT__65(); 

                }
                break;
            case 41 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:250: T__66
                {
                mT__66(); 

                }
                break;
            case 42 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:256: T__67
                {
                mT__67(); 

                }
                break;
            case 43 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:262: T__68
                {
                mT__68(); 

                }
                break;
            case 44 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:268: T__69
                {
                mT__69(); 

                }
                break;
            case 45 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:274: RULE_LONG
                {
                mRULE_LONG(); 

                }
                break;
            case 46 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:284: RULE_INT
                {
                mRULE_INT(); 

                }
                break;
            case 47 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:293: RULE_FLOAT
                {
                mRULE_FLOAT(); 

                }
                break;
            case 48 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:304: RULE_DOUBLE
                {
                mRULE_DOUBLE(); 

                }
                break;
            case 49 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:316: RULE_CHAR
                {
                mRULE_CHAR(); 

                }
                break;
            case 50 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:326: RULE_STRING
                {
                mRULE_STRING(); 

                }
                break;
            case 51 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:338: RULE_BRACED_IDENTIFIER
                {
                mRULE_BRACED_IDENTIFIER(); 

                }
                break;
            case 52 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:361: RULE_ID
                {
                mRULE_ID(); 

                }
                break;
            case 53 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:369: RULE_ML_COMMENT
                {
                mRULE_ML_COMMENT(); 

                }
                break;
            case 54 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:385: RULE_SL_COMMENT
                {
                mRULE_SL_COMMENT(); 

                }
                break;
            case 55 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:401: RULE_WS
                {
                mRULE_WS(); 

                }
                break;
            case 56 :
                // ../com.jaspersoft.studio.editor.jrexpressions/src-gen/com/jaspersoft/studio/editor/jrexpressions/parser/antlr/internal/InternalJavaJRExpression.g:1:409: RULE_ANY_OTHER
                {
                mRULE_ANY_OTHER(); 

                }
                break;

        }

    }


    protected DFA4 dfa4 = new DFA4(this);
    protected DFA21 dfa21 = new DFA21(this);
    protected DFA32 dfa32 = new DFA32(this);
    static final String DFA4_eotS =
        "\11\uffff\2\15\1\uffff\1\16\3\uffff";
    static final String DFA4_eofS =
        "\20\uffff";
    static final String DFA4_minS =
        "\1\42\10\uffff\2\60\1\uffff\1\60\3\uffff";
    static final String DFA4_maxS =
        "\1\165\10\uffff\2\67\1\uffff\1\67\3\uffff";
    static final String DFA4_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\2\uffff\1\14\1\uffff\1"+
        "\13\1\12\1\11";
    static final String DFA4_specialS =
        "\20\uffff}>";
    static final String[] DFA4_transitionS = {
            "\1\6\4\uffff\1\7\10\uffff\4\11\4\12\44\uffff\1\10\5\uffff\1"+
            "\1\3\uffff\1\4\7\uffff\1\3\3\uffff\1\5\1\uffff\1\2\1\13",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\10\14",
            "\10\16",
            "",
            "\10\17",
            "",
            "",
            ""
    };

    static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);
    static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);
    static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);
    static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);
    static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);
    static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);
    static final short[][] DFA4_transition;

    static {
        int numStates = DFA4_transitionS.length;
        DFA4_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
        }
    }

    class DFA4 extends DFA {

        public DFA4(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 4;
            this.eot = DFA4_eot;
            this.eof = DFA4_eof;
            this.min = DFA4_min;
            this.max = DFA4_max;
            this.accept = DFA4_accept;
            this.special = DFA4_special;
            this.transition = DFA4_transition;
        }
        public String getDescription() {
            return "3110:37: ( 'b' | 't' | 'n' | 'f' | 'r' | '\"' | '\\'' | '\\\\' | '0' .. '3' '0' .. '7' '0' .. '7' | '0' .. '7' '0' .. '7' | '0' .. '7' | 'u' RULE_HEXDIGIT RULE_HEXDIGIT RULE_HEXDIGIT RULE_HEXDIGIT )";
        }
    }
    static final String DFA21_eotS =
        "\1\uffff\1\5\1\uffff\1\5\4\uffff";
    static final String DFA21_eofS =
        "\10\uffff";
    static final String DFA21_minS =
        "\2\56\1\uffff\1\56\4\uffff";
    static final String DFA21_maxS =
        "\1\71\1\170\1\uffff\1\145\4\uffff";
    static final String DFA21_acceptS =
        "\2\uffff\1\2\1\uffff\1\5\1\4\1\1\1\3";
    static final String DFA21_specialS =
        "\10\uffff}>";
    static final String[] DFA21_transitionS = {
            "\1\2\1\uffff\1\1\11\3",
            "\1\6\1\uffff\12\3\13\uffff\1\7\22\uffff\1\4\14\uffff\1\7\22"+
            "\uffff\1\4",
            "",
            "\1\6\1\uffff\12\3\13\uffff\1\7\37\uffff\1\7",
            "",
            "",
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
            return "3120:34: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( RULE_EXPONENT )? | '.' ( '0' .. '9' )+ ( RULE_EXPONENT )? | ( '0' .. '9' )+ RULE_EXPONENT | ( '0' .. '9' )+ | RULE_HEXPREFIX ( RULE_HEXDIGIT )* '.' ( RULE_HEXDIGIT )* ( 'p' | 'P' ) ( '+' | '-' )? ( '0' .. '9' )+ )";
        }
    }
    static final String DFA32_eotS =
        "\3\uffff\3\51\1\60\1\62\3\uffff\1\70\2\uffff\1\74\1\51\1\62\2\uffff"+
        "\2\62\2\uffff\1\113\2\uffff\1\117\1\121\6\62\2\137\3\51\12\uffff"+
        "\1\62\11\uffff\1\142\5\uffff\2\62\2\uffff\3\62\12\uffff\10\62\2"+
        "\uffff\1\142\1\137\1\142\5\uffff\1\137\3\uffff\1\62\1\177\1\uffff"+
        "\1\u0082\14\62\1\137\1\uffff\1\142\2\uffff\1\142\1\62\2\uffff\1"+
        "\142\1\uffff\1\u0094\2\62\1\u0097\4\62\1\u009c\1\u009d\1\u009e\1"+
        "\62\3\uffff\1\142\1\62\1\uffff\1\u00a3\1\u00a4\1\uffff\1\62\1\u00a6"+
        "\1\u00a7\1\62\3\uffff\1\62\1\uffff\1\142\1\62\2\uffff\1\62\2\uffff"+
        "\1\62\1\u00ad\1\62\1\u00af\1\u00b0\1\uffff\1\62\2\uffff\1\62\1\u00b3"+
        "\1\uffff";
    static final String DFA32_eofS =
        "\u00b4\uffff";
    static final String DFA32_minS =
        "\1\0\2\uffff\1\174\1\46\2\75\1\156\3\uffff\1\52\2\uffff\1\60\1\106"+
        "\1\145\2\uffff\1\141\1\162\2\uffff\1\0\2\uffff\2\75\1\170\1\150"+
        "\1\157\1\150\2\157\2\56\2\0\1\101\12\uffff\1\163\11\uffff\1\60\5"+
        "\uffff\1\167\1\154\2\uffff\1\154\1\157\1\165\12\uffff\1\164\1\160"+
        "\2\157\1\164\1\141\1\156\1\165\2\56\1\60\2\56\1\uffff\1\53\3\uffff"+
        "\1\56\3\uffff\1\164\1\60\1\53\1\60\1\154\1\163\1\141\3\145\1\162"+
        "\1\154\1\145\1\162\1\147\1\142\1\56\2\60\1\53\2\60\1\141\1\uffff"+
        "\2\60\1\uffff\1\60\1\145\1\164\1\60\1\156\1\162\1\164\1\145\3\60"+
        "\1\154\1\60\1\53\2\60\1\156\1\uffff\2\60\1\uffff\1\144\2\60\1\141"+
        "\3\uffff\1\145\2\60\1\143\2\uffff\1\163\2\uffff\1\156\1\60\1\145"+
        "\2\60\1\uffff\1\157\2\uffff\1\146\1\60\1\uffff";
    static final String DFA32_maxS =
        "\1\uffff\2\uffff\1\174\1\46\2\75\1\156\3\uffff\1\57\2\uffff\1\71"+
        "\1\126\1\165\2\uffff\1\154\1\162\2\uffff\1\uffff\2\uffff\2\75\1"+
        "\170\1\165\1\171\1\150\2\157\1\170\1\154\2\uffff\1\172\12\uffff"+
        "\1\164\11\uffff\1\146\5\uffff\1\167\1\154\2\uffff\1\154\1\157\1"+
        "\165\12\uffff\1\164\1\160\2\157\1\164\1\141\1\156\1\165\3\146\1"+
        "\154\1\146\1\uffff\1\71\3\uffff\1\154\3\uffff\1\164\1\172\1\71\1"+
        "\172\1\154\1\163\1\141\3\145\1\162\1\154\1\145\1\162\1\147\1\142"+
        "\1\154\1\160\1\146\2\71\1\146\1\141\1\uffff\1\71\1\146\1\uffff\1"+
        "\172\1\145\1\164\1\172\1\156\1\162\1\164\1\145\3\172\1\154\1\160"+
        "\2\71\1\146\1\156\1\uffff\2\172\1\uffff\1\144\2\172\1\141\3\uffff"+
        "\1\145\1\71\1\146\1\143\2\uffff\1\163\2\uffff\1\156\1\172\1\145"+
        "\2\172\1\uffff\1\157\2\uffff\1\146\1\172\1\uffff";
    static final String DFA32_acceptS =
        "\1\uffff\1\1\1\2\5\uffff\1\10\1\11\1\12\1\uffff\1\14\1\15\3\uffff"+
        "\1\25\1\26\2\uffff\1\32\1\33\1\uffff\1\35\1\36\15\uffff\1\64\1\67"+
        "\1\70\1\1\1\2\1\3\1\4\1\5\1\6\1\16\1\uffff\1\64\1\10\1\11\1\12\1"+
        "\65\1\66\1\13\1\14\1\15\1\uffff\1\17\1\20\1\21\1\22\1\23\2\uffff"+
        "\1\25\1\26\3\uffff\1\32\1\33\1\63\1\34\1\35\1\36\1\53\1\37\1\54"+
        "\1\40\15\uffff\1\56\1\uffff\1\57\1\60\1\55\1\uffff\1\61\1\62\1\67"+
        "\27\uffff\1\47\2\uffff\1\24\21\uffff\1\31\2\uffff\1\30\4\uffff\1"+
        "\45\1\44\1\50\4\uffff\1\27\1\51\1\uffff\1\42\1\46\5\uffff\1\52\1"+
        "\uffff\1\41\1\43\2\uffff\1\7";
    static final String DFA32_specialS =
        "\1\3\26\uffff\1\0\14\uffff\1\1\1\2\u008e\uffff}>";
    static final String[] DFA32_transitionS = {
            "\11\51\2\50\2\51\1\50\22\51\1\50\1\6\1\45\1\51\1\17\1\14\1\4"+
            "\1\44\1\25\1\26\1\12\1\10\1\31\1\11\1\16\1\13\1\42\11\43\1\2"+
            "\1\51\1\32\1\5\1\33\1\1\1\51\32\47\1\21\1\51\1\22\1\46\1\47"+
            "\1\51\1\47\1\36\1\37\1\41\1\34\1\23\2\47\1\7\2\47\1\40\1\47"+
            "\1\20\4\47\1\35\1\24\6\47\1\27\1\3\1\30\1\15\uff81\51",
            "",
            "",
            "\1\54",
            "\1\55",
            "\1\56",
            "\1\57",
            "\1\61",
            "",
            "",
            "",
            "\1\66\4\uffff\1\67",
            "",
            "",
            "\12\73",
            "\1\75\11\uffff\1\76\1\uffff\1\100\3\uffff\1\77",
            "\1\101\17\uffff\1\102",
            "",
            "",
            "\1\105\12\uffff\1\106",
            "\1\107",
            "",
            "",
            "\12\112\1\uffff\2\112\1\uffff\157\112\1\uffff\uff82\112",
            "",
            "",
            "\1\116",
            "\1\120",
            "\1\122",
            "\1\124\14\uffff\1\123",
            "\1\125\11\uffff\1\126",
            "\1\127",
            "\1\130",
            "\1\131",
            "\1\134\1\uffff\10\135\2\136\12\uffff\1\142\1\140\1\141\5\uffff"+
            "\1\143\13\uffff\1\133\13\uffff\1\142\1\140\1\141\5\uffff\1\143"+
            "\13\uffff\1\132",
            "\1\134\1\uffff\12\144\12\uffff\1\142\1\140\1\141\5\uffff\1"+
            "\143\27\uffff\1\142\1\140\1\141\5\uffff\1\143",
            "\12\145\1\uffff\2\145\1\uffff\31\145\1\uffff\uffd8\145",
            "\12\146\1\uffff\2\146\1\uffff\ufff2\146",
            "\32\62\4\uffff\1\62\1\uffff\32\62",
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
            "\1\150\1\151",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\12\73\13\uffff\1\152\1\141\36\uffff\1\152\1\141",
            "",
            "",
            "",
            "",
            "",
            "\1\153",
            "\1\154",
            "",
            "",
            "\1\155",
            "\1\156",
            "\1\157",
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
            "\1\160",
            "\1\161",
            "\1\162",
            "\1\163",
            "\1\164",
            "\1\165",
            "\1\166",
            "\1\167",
            "\1\171\1\uffff\12\170\7\uffff\6\170\32\uffff\6\170",
            "\1\171\1\uffff\12\170\7\uffff\6\170\32\uffff\6\170",
            "\12\172\13\uffff\1\173\1\141\36\uffff\1\173\1\141",
            "\1\134\1\uffff\10\135\2\136\12\uffff\1\142\1\140\1\141\5\uffff"+
            "\1\143\27\uffff\1\142\1\140\1\141\5\uffff\1\143",
            "\1\134\1\uffff\12\136\13\uffff\1\140\1\141\36\uffff\1\140\1"+
            "\141",
            "",
            "\1\174\1\uffff\1\174\2\uffff\12\175",
            "",
            "",
            "",
            "\1\134\1\uffff\12\144\12\uffff\1\142\1\140\1\141\5\uffff\1"+
            "\143\27\uffff\1\142\1\140\1\141\5\uffff\1\143",
            "",
            "",
            "",
            "\1\176",
            "\12\62\7\uffff\32\62\4\uffff\1\62\1\uffff\32\62",
            "\1\u0080\1\uffff\1\u0080\2\uffff\12\u0081",
            "\12\62\7\uffff\32\62\4\uffff\1\62\1\uffff\32\62",
            "\1\u0083",
            "\1\u0084",
            "\1\u0085",
            "\1\u0086",
            "\1\u0087",
            "\1\u0088",
            "\1\u0089",
            "\1\u008a",
            "\1\u008b",
            "\1\u008c",
            "\1\u008d",
            "\1\u008e",
            "\1\171\1\uffff\12\170\7\uffff\6\170\5\uffff\1\143\24\uffff"+
            "\6\170\5\uffff\1\143",
            "\12\u008f\7\uffff\6\u008f\11\uffff\1\u0090\20\uffff\6\u008f"+
            "\11\uffff\1\u0090",
            "\12\172\13\uffff\1\173\1\141\36\uffff\1\173\1\141",
            "\1\u0091\1\uffff\1\u0091\2\uffff\12\u0092",
            "\12\175",
            "\12\175\14\uffff\1\141\37\uffff\1\141",
            "\1\u0093",
            "",
            "\12\u0081",
            "\12\u0081\14\uffff\1\141\37\uffff\1\141",
            "",
            "\12\62\7\uffff\32\62\4\uffff\1\62\1\uffff\32\62",
            "\1\u0095",
            "\1\u0096",
            "\12\62\7\uffff\32\62\4\uffff\1\62\1\uffff\32\62",
            "\1\u0098",
            "\1\u0099",
            "\1\u009a",
            "\1\u009b",
            "\12\62\7\uffff\32\62\4\uffff\1\62\1\uffff\32\62",
            "\12\62\7\uffff\32\62\4\uffff\1\62\1\uffff\32\62",
            "\12\62\7\uffff\32\62\4\uffff\1\62\1\uffff\32\62",
            "\1\u009f",
            "\12\u008f\7\uffff\6\u008f\11\uffff\1\u0090\20\uffff\6\u008f"+
            "\11\uffff\1\u0090",
            "\1\u00a0\1\uffff\1\u00a0\2\uffff\12\u00a1",
            "\12\u0092",
            "\12\u0092\14\uffff\1\141\37\uffff\1\141",
            "\1\u00a2",
            "",
            "\12\62\7\uffff\32\62\4\uffff\1\62\1\uffff\32\62",
            "\12\62\7\uffff\32\62\4\uffff\1\62\1\uffff\32\62",
            "",
            "\1\u00a5",
            "\12\62\7\uffff\32\62\4\uffff\1\62\1\uffff\32\62",
            "\12\62\7\uffff\32\62\4\uffff\1\62\1\uffff\32\62",
            "\1\u00a8",
            "",
            "",
            "",
            "\1\u00a9",
            "\12\u00a1",
            "\12\u00a1\14\uffff\1\141\37\uffff\1\141",
            "\1\u00aa",
            "",
            "",
            "\1\u00ab",
            "",
            "",
            "\1\u00ac",
            "\12\62\7\uffff\32\62\4\uffff\1\62\1\uffff\32\62",
            "\1\u00ae",
            "\12\62\7\uffff\32\62\4\uffff\1\62\1\uffff\32\62",
            "\12\62\7\uffff\32\62\4\uffff\1\62\1\uffff\32\62",
            "",
            "\1\u00b1",
            "",
            "",
            "\1\u00b2",
            "\12\62\7\uffff\32\62\4\uffff\1\62\1\uffff\32\62",
            ""
    };

    static final short[] DFA32_eot = DFA.unpackEncodedString(DFA32_eotS);
    static final short[] DFA32_eof = DFA.unpackEncodedString(DFA32_eofS);
    static final char[] DFA32_min = DFA.unpackEncodedStringToUnsignedChars(DFA32_minS);
    static final char[] DFA32_max = DFA.unpackEncodedStringToUnsignedChars(DFA32_maxS);
    static final short[] DFA32_accept = DFA.unpackEncodedString(DFA32_acceptS);
    static final short[] DFA32_special = DFA.unpackEncodedString(DFA32_specialS);
    static final short[][] DFA32_transition;

    static {
        int numStates = DFA32_transitionS.length;
        DFA32_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA32_transition[i] = DFA.unpackEncodedString(DFA32_transitionS[i]);
        }
    }

    class DFA32 extends DFA {

        public DFA32(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 32;
            this.eot = DFA32_eot;
            this.eof = DFA32_eof;
            this.min = DFA32_min;
            this.max = DFA32_max;
            this.accept = DFA32_accept;
            this.special = DFA32_special;
            this.transition = DFA32_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | RULE_LONG | RULE_INT | RULE_FLOAT | RULE_DOUBLE | RULE_CHAR | RULE_STRING | RULE_BRACED_IDENTIFIER | RULE_ID | RULE_ML_COMMENT | RULE_SL_COMMENT | RULE_WS | RULE_ANY_OTHER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA32_23 = input.LA(1);

                        s = -1;
                        if ( ((LA32_23>='\u0000' && LA32_23<='\t')||(LA32_23>='\u000B' && LA32_23<='\f')||(LA32_23>='\u000E' && LA32_23<='|')||(LA32_23>='~' && LA32_23<='\uFFFF')) ) {s = 74;}

                        else s = 75;

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA32_36 = input.LA(1);

                        s = -1;
                        if ( ((LA32_36>='\u0000' && LA32_36<='\t')||(LA32_36>='\u000B' && LA32_36<='\f')||(LA32_36>='\u000E' && LA32_36<='&')||(LA32_36>='(' && LA32_36<='\uFFFF')) ) {s = 101;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA32_37 = input.LA(1);

                        s = -1;
                        if ( ((LA32_37>='\u0000' && LA32_37<='\t')||(LA32_37>='\u000B' && LA32_37<='\f')||(LA32_37>='\u000E' && LA32_37<='\uFFFF')) ) {s = 102;}

                        else s = 41;

                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA32_0 = input.LA(1);

                        s = -1;
                        if ( (LA32_0=='?') ) {s = 1;}

                        else if ( (LA32_0==':') ) {s = 2;}

                        else if ( (LA32_0=='|') ) {s = 3;}

                        else if ( (LA32_0=='&') ) {s = 4;}

                        else if ( (LA32_0=='=') ) {s = 5;}

                        else if ( (LA32_0=='!') ) {s = 6;}

                        else if ( (LA32_0=='i') ) {s = 7;}

                        else if ( (LA32_0=='+') ) {s = 8;}

                        else if ( (LA32_0=='-') ) {s = 9;}

                        else if ( (LA32_0=='*') ) {s = 10;}

                        else if ( (LA32_0=='/') ) {s = 11;}

                        else if ( (LA32_0=='%') ) {s = 12;}

                        else if ( (LA32_0=='~') ) {s = 13;}

                        else if ( (LA32_0=='.') ) {s = 14;}

                        else if ( (LA32_0=='$') ) {s = 15;}

                        else if ( (LA32_0=='n') ) {s = 16;}

                        else if ( (LA32_0=='[') ) {s = 17;}

                        else if ( (LA32_0==']') ) {s = 18;}

                        else if ( (LA32_0=='f') ) {s = 19;}

                        else if ( (LA32_0=='t') ) {s = 20;}

                        else if ( (LA32_0=='(') ) {s = 21;}

                        else if ( (LA32_0==')') ) {s = 22;}

                        else if ( (LA32_0=='{') ) {s = 23;}

                        else if ( (LA32_0=='}') ) {s = 24;}

                        else if ( (LA32_0==',') ) {s = 25;}

                        else if ( (LA32_0=='<') ) {s = 26;}

                        else if ( (LA32_0=='>') ) {s = 27;}

                        else if ( (LA32_0=='e') ) {s = 28;}

                        else if ( (LA32_0=='s') ) {s = 29;}

                        else if ( (LA32_0=='b') ) {s = 30;}

                        else if ( (LA32_0=='c') ) {s = 31;}

                        else if ( (LA32_0=='l') ) {s = 32;}

                        else if ( (LA32_0=='d') ) {s = 33;}

                        else if ( (LA32_0=='0') ) {s = 34;}

                        else if ( ((LA32_0>='1' && LA32_0<='9')) ) {s = 35;}

                        else if ( (LA32_0=='\'') ) {s = 36;}

                        else if ( (LA32_0=='\"') ) {s = 37;}

                        else if ( (LA32_0=='^') ) {s = 38;}

                        else if ( ((LA32_0>='A' && LA32_0<='Z')||LA32_0=='_'||LA32_0=='a'||(LA32_0>='g' && LA32_0<='h')||(LA32_0>='j' && LA32_0<='k')||LA32_0=='m'||(LA32_0>='o' && LA32_0<='r')||(LA32_0>='u' && LA32_0<='z')) ) {s = 39;}

                        else if ( ((LA32_0>='\t' && LA32_0<='\n')||LA32_0=='\r'||LA32_0==' ') ) {s = 40;}

                        else if ( ((LA32_0>='\u0000' && LA32_0<='\b')||(LA32_0>='\u000B' && LA32_0<='\f')||(LA32_0>='\u000E' && LA32_0<='\u001F')||LA32_0=='#'||LA32_0==';'||LA32_0=='@'||LA32_0=='\\'||LA32_0=='`'||(LA32_0>='\u007F' && LA32_0<='\uFFFF')) ) {s = 41;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 32, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}
