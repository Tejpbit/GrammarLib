/**
 * Created by tejp on 29/06/15.
 */
class GrammarTest extends GroovyTestCase {

    GrammarDelegate grammarBuilder

    @Override
    void setUp() {
        grammarBuilder = new GrammarDelegate()
    }

    void testAddGrammar() {
        grammarBuilder.with {
            start 'S' becomes "AB"
            var 'A' becomes "BB" or "a"
            var 'B' becomes "AB" or "b"
        }


        assert grammarBuilder.grammar.rules.size() == 3
    }

    void testNoStartVariable() {
        grammarBuilder.with {
            var 'S' becomes "a"
        }

        shouldFail(RuntimeException){
            grammarBuilder.validate()
        }
    }

    void testAddStartVarGrammar() {

    }

    void testCanCreateWord() {
        grammarBuilder.with {
            start 'S' becomes "AB"
            var 'A' becomes "BB" or "a"
            var 'B' becomes "AB" or "b"
        }
        Grammar grammar = grammarBuilder.grammar
        assert grammar.canGenerateWord("bbaab")
    }
}
