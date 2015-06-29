import groovy.transform.ToString

/**
 * Created by tejp on 29/06/15.
 */

class Grammar {
    List<Rule> rules

    void addRule(Rule rule) {
        rules << rule
    }

    void toChomskyNormalForm() {

    }

    
}

class CYKResult {

}

@ToString
class GrammarDelegate {
    Grammar grammar = new Grammar()

    RuleBuilder var(String c) {
        Rule rule = new Rule(name:c)
        RuleBuilder ruleBuilder = new RuleBuilder(rule:rule)
        grammar.addRule rule
        ruleBuilder
    }
}

@ToString
class Rule {
    String name
    List<String> values = new ArrayList<>()
}

class RuleBuilder {
    Rule rule

    RuleBuilder becomes(String s) {
        rule.values << s
        this
    }

    RuleBuilder or(String s) {
        rule.values << s
        this
    }
}

GrammarDelegate g = new GrammarDelegate();

g.with {
    var 'S' becomes "AB"  or "C"
    var 'A' becomes "aAb" or "ab"
    var 'B' becomes "cBd" or "cd"
    var 'C' becomes "aCd" or "aDd"
    var 'D' becomes "bDc" or "bc"
}

println g