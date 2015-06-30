/**
 * Created by tejp on 29/06/15.
 */
class GrammarTest extends GroovyTestCase {

    GrammarDelegate grammarBuilder

    @Override
    void setUp() {
        grammarBuilder = new GrammarDelegate()
    }

    void mySetUp() {
        grammarBuilder.with {
            start 'S' becomes "AB"
            var 'A' becomes "BB" or "a"
            var 'B' becomes "AB" or "b"
        }
    }

    void testAddGrammar() {
        mySetUp()

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

    void testCanCreateWord() {
        mySetUp()
        Grammar grammar = grammarBuilder.grammar
        assert grammar.canGenerateWord("bbaab")
    }

    void testNewVariable() {
        mySetUp()
        Grammar grammar = grammarBuilder.grammar
        Rule rule = grammar.newVariable()
        assert grammar.rules.stream().allMatch({ ! it.name.equals(rule.name)})
    }

    void testToChomskyNormalForm() {
        grammarBuilder.with {
            start 'S' becomes "aBc"
            var 'B' becomes "b"
            var 'C' becomes "c" or "SB"
        }
        Grammar grammar = grammarBuilder.grammar

        grammar.toChomskyNormalForm()

        println grammar
        assert grammar.rules.stream()
                    .allMatch({it.values.stream()
                            .allMatch({
                                it.value.matches('^[A-Z][A-Z]$|^[a-z]$')
                            })
                    })

    }
}
