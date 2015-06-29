import org.junit.Test

/**
 * Created by tejp on 29/06/15.
 */
class GrammarTest extends GroovyTestCase {

    GrammarDelegate grammarBuilder

    @Override
    void setUp() {
        grammarBuilder = new GrammarDelegate()
    }

    @Test
    void testAddGrammar() {
        grammarBuilder.with {
            var 'S' becomes "AB"
            var 'A' becomes "BB" or "a"
            var 'B' becomes "AB" or "b"
        }


        assert grammarBuilder.grammar.rules.size() == 3

    }
}
