import groovy.transform.ToString
import org.codehaus.groovy.control.CompilerConfiguration

import java.util.stream.Collectors

/**
 * Created by tejp on 29/06/15.
 */
class Grammar {
    List<Rule> rules = new ArrayList<>()

    void addRule(Rule rule) {
        rules << rule
    }

    void toChomskyNormalForm() {

    }

   boolean canGenerateWord(String word) {
       CYKResult result = getCYKResult(word)
       result.topRules().stream().anyMatch({it.start})
   }

    CYKResult getCYKResult(String str) {
        new CYKResult(str, this)
    }

    Set<Rule> findRulesFor(String ruleResult) {
        rules.stream()
                .filter({it.values.contains(ruleResult)})
                .collect(Collectors.toSet())
    }
}

@ToString
class GrammarDelegate {
    Grammar grammar = new Grammar()

    RuleBuilder start(String varName) {
        Rule rule = new Rule(name:varName, start: true)
        RuleBuilder ruleBuilder = new RuleBuilder(rule:rule)
        grammar.addRule rule
        ruleBuilder
    }

    RuleBuilder var(String varName) {
        Rule rule = new Rule(name:varName)
        RuleBuilder ruleBuilder = new RuleBuilder(rule:rule)
        grammar.addRule rule
        ruleBuilder
    }

    void validate() {
        if (! this.grammar.rules.stream().anyMatch({it.start})) {
            throw new RuntimeException("No start variable declared")
        }
    }

    static GrammarDelegate load(File file) {

        GrammarDelegate grammarDelegate = new GrammarDelegate()

        CompilerConfiguration cc = new CompilerConfiguration()
        cc.setScriptBaseClass(DelegatingScript.class.getName())
        GroovyShell sh = new GroovyShell(cc)
        DelegatingScript script = (DelegatingScript) sh.parse(file)
        script.setDelegate(grammarDelegate)
        script.run()

        grammarDelegate.validate()
        grammarDelegate
    }
}