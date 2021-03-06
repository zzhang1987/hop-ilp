// Generated from /Users/ndt/Development/fyp/hop-ilp/res/spudd.g4 by ANTLR 4.5.3
package com.ndt.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class spuddLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, ID=14, INT=15, FLOAT=16, WS=17, 
		COMMENT=18;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "ID", "INT", "FLOAT", "DIGIT", "WS", 
		"COMMENT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'init'", "'[*'", "']'", "'(variables'", "')'", "'('", "'action'", 
		"'endaction'", "'cost'", "'[+'", "'reward'", "'discount'", "'horizon'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, "ID", "INT", "FLOAT", "WS", "COMMENT"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public spuddLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "spudd.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\24\u00a3\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n"+
		"\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\6\17"+
		"v\n\17\r\17\16\17w\3\20\5\20{\n\20\3\20\6\20~\n\20\r\20\16\20\177\3\21"+
		"\5\21\u0083\n\21\3\21\6\21\u0086\n\21\r\21\16\21\u0087\3\21\3\21\6\21"+
		"\u008c\n\21\r\21\16\21\u008d\3\22\3\22\3\23\6\23\u0093\n\23\r\23\16\23"+
		"\u0094\3\23\3\23\3\24\3\24\3\24\3\24\7\24\u009d\n\24\f\24\16\24\u00a0"+
		"\13\24\3\24\3\24\2\2\25\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f"+
		"\27\r\31\16\33\17\35\20\37\21!\22#\2%\23\'\24\3\2\7\3\2c|\7\2))\62;C\\"+
		"aac|\4\2--//\5\2\13\f\17\17\"\"\4\2\f\f\17\17\u00a9\2\3\3\2\2\2\2\5\3"+
		"\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2"+
		"\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3"+
		"\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\3)"+
		"\3\2\2\2\5.\3\2\2\2\7\61\3\2\2\2\t\63\3\2\2\2\13>\3\2\2\2\r@\3\2\2\2\17"+
		"B\3\2\2\2\21I\3\2\2\2\23S\3\2\2\2\25X\3\2\2\2\27[\3\2\2\2\31b\3\2\2\2"+
		"\33k\3\2\2\2\35s\3\2\2\2\37z\3\2\2\2!\u0082\3\2\2\2#\u008f\3\2\2\2%\u0092"+
		"\3\2\2\2\'\u0098\3\2\2\2)*\7k\2\2*+\7p\2\2+,\7k\2\2,-\7v\2\2-\4\3\2\2"+
		"\2./\7]\2\2/\60\7,\2\2\60\6\3\2\2\2\61\62\7_\2\2\62\b\3\2\2\2\63\64\7"+
		"*\2\2\64\65\7x\2\2\65\66\7c\2\2\66\67\7t\2\2\678\7k\2\289\7c\2\29:\7d"+
		"\2\2:;\7n\2\2;<\7g\2\2<=\7u\2\2=\n\3\2\2\2>?\7+\2\2?\f\3\2\2\2@A\7*\2"+
		"\2A\16\3\2\2\2BC\7c\2\2CD\7e\2\2DE\7v\2\2EF\7k\2\2FG\7q\2\2GH\7p\2\2H"+
		"\20\3\2\2\2IJ\7g\2\2JK\7p\2\2KL\7f\2\2LM\7c\2\2MN\7e\2\2NO\7v\2\2OP\7"+
		"k\2\2PQ\7q\2\2QR\7p\2\2R\22\3\2\2\2ST\7e\2\2TU\7q\2\2UV\7u\2\2VW\7v\2"+
		"\2W\24\3\2\2\2XY\7]\2\2YZ\7-\2\2Z\26\3\2\2\2[\\\7t\2\2\\]\7g\2\2]^\7y"+
		"\2\2^_\7c\2\2_`\7t\2\2`a\7f\2\2a\30\3\2\2\2bc\7f\2\2cd\7k\2\2de\7u\2\2"+
		"ef\7e\2\2fg\7q\2\2gh\7w\2\2hi\7p\2\2ij\7v\2\2j\32\3\2\2\2kl\7j\2\2lm\7"+
		"q\2\2mn\7t\2\2no\7k\2\2op\7|\2\2pq\7q\2\2qr\7p\2\2r\34\3\2\2\2su\t\2\2"+
		"\2tv\t\3\2\2ut\3\2\2\2vw\3\2\2\2wu\3\2\2\2wx\3\2\2\2x\36\3\2\2\2y{\t\4"+
		"\2\2zy\3\2\2\2z{\3\2\2\2{}\3\2\2\2|~\5#\22\2}|\3\2\2\2~\177\3\2\2\2\177"+
		"}\3\2\2\2\177\u0080\3\2\2\2\u0080 \3\2\2\2\u0081\u0083\t\4\2\2\u0082\u0081"+
		"\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0085\3\2\2\2\u0084\u0086\5#\22\2\u0085"+
		"\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0085\3\2\2\2\u0087\u0088\3\2"+
		"\2\2\u0088\u0089\3\2\2\2\u0089\u008b\7\60\2\2\u008a\u008c\5#\22\2\u008b"+
		"\u008a\3\2\2\2\u008c\u008d\3\2\2\2\u008d\u008b\3\2\2\2\u008d\u008e\3\2"+
		"\2\2\u008e\"\3\2\2\2\u008f\u0090\4\62;\2\u0090$\3\2\2\2\u0091\u0093\t"+
		"\5\2\2\u0092\u0091\3\2\2\2\u0093\u0094\3\2\2\2\u0094\u0092\3\2\2\2\u0094"+
		"\u0095\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0097\b\23\2\2\u0097&\3\2\2\2"+
		"\u0098\u0099\7\61\2\2\u0099\u009a\7\61\2\2\u009a\u009e\3\2\2\2\u009b\u009d"+
		"\n\6\2\2\u009c\u009b\3\2\2\2\u009d\u00a0\3\2\2\2\u009e\u009c\3\2\2\2\u009e"+
		"\u009f\3\2\2\2\u009f\u00a1\3\2\2\2\u00a0\u009e\3\2\2\2\u00a1\u00a2\b\24"+
		"\2\2\u00a2(\3\2\2\2\13\2wz\177\u0082\u0087\u008d\u0094\u009e\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}