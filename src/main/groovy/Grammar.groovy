import groovy.transform.ToString
import org.codehaus.groovy.control.CompilerConfiguration

import java.util.regex.Pattern
import java.util.stream.Collectors

/**
 * Created by tejp on 29/06/15.
 */
class Grammar {
    Set<Rule> rules = new HashSet<>()

    void addRule(Rule rule) {
        rules << rule
    }

    void toChomskyNormalForm() {
        List<String> terminals = new ArrayList<>()
        rules.each { rule ->
            rule.values.each { replacement ->
                replacement.value.each { target ->
                    if (target.matches("[a-z]")) {
                        terminals.add(target)
                    }
                }
            }
        }

        terminals.each { terminal ->
            if (rules.stream().noneMatch({ it.values.value.contains(terminal) && it.values.size() == 1 })) {
                Rule newRule = newVariable()
                newRule.values << new RuleTarget(value: terminal)
                rules << newRule
            }
        }

        rules.each { rule ->
            rule.values.each {
                if (Pattern.compile("[a-z]").matcher(it.value).find() && it.value.length() > 1) {
                    StringBuilder strBuilder = new StringBuilder()
                    for (int i = 0; i < it.value.length(); i++) {
                        char ch = it.value.charAt(i)
                        if (ch.isLowerCase()) {
                            strBuilder.append(
                                rules.stream()
                                        .filter({ it.values.value.contains(String.valueOf(ch)) && it.values.size() == 1})
                                        .findAny()
                                        .orElseThrow({new NoSuchElementException("Missing terminal " + ch + "\n" + this)})
                                        .name
                            )
                        } else {
                            strBuilder.append(ch)
                        }
                    }
                    it.value = strBuilder.toString()
                }
            }
        }

        new HashSet<Rule>(rules).each {rule ->
            rule.values.each {ruleTarget ->
                while (ruleTarget.value.length() > 2) {
                    Rule newRule = newVariable()
                    newRule.values << new RuleTarget(value: ruleTarget.value.substring(0, 2))
                    rules << newRule

                    ruleTarget.value = newRule.name + ruleTarget.value.substring(2)
                }
            }
        }


    }

    /**
     *
     * @return a Rule with a unused name and no connections yet made
     */
    Rule newVariable() {
        char ruleName = 'A'

        rules.stream().sorted(new Comparator<Rule>() {
            @Override
            int compare(Rule o1, Rule o2) {
                return o1.name.compareTo(o2.name)
            }
        }).each {
            if (it.name.equals(ruleName.toString())) {
                ruleName+=1
            }
        }

        new Rule(name: ruleName)
    }

   boolean canGenerateWord(String word) {
       CYKResult result = getCYKResult(word)
       result.topRules().stream().anyMatch({it.start})
   }

    CYKResult getCYKResult(String str) {
        new CYKResult(str, this)
    }

    /**
     *
     * @param ruleResult: what you want the rules to be able to result in
     * @return all rules which result in the String ruleResult
     */
    Set<Rule> findRulesFor(String ruleResult) {
        rules.stream()
                .filter({it.values.value.contains(ruleResult)})
                .collect(Collectors.toSet())
    }


    @Override
    public String toString() {
        return "Grammar{" +
                "rules=" + rules.stream().map({it.toString()}).collect(Collectors.joining("\n")) +
                '}';
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


    static GrammarDelegate load(URI uri) {

        GrammarDelegate grammarDelegate = new GrammarDelegate()

        CompilerConfiguration cc = new CompilerConfiguration()
        cc.setScriptBaseClass(DelegatingScript.class.getName())
        GroovyShell sh = new GroovyShell(cc)
        DelegatingScript script = (DelegatingScript) sh.parse(uri)
        script.setDelegate(grammarDelegate)
        script.run()

        grammarDelegate.validate()
        grammarDelegate
    }
}