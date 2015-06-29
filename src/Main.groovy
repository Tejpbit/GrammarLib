import groovy.transform.ToString

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

    CYKResult getCYKResult(String str) {
        new CYKResult(str, this)
    }

    Set<Rule> findRulesFor(String ruleResult) {
        rules.stream()
            .filter({it.values.contains(ruleResult)})
            .collect(Collectors.toSet())
    }
}

class CYKResult {

    CYKElement[][] table

    CYKResult(String str, Grammar grammar) {
        table = new CYKElement[str.length()][]

        for (int row = 0; row < str.length(); row++) {
            table[row] = new CYKElement[str.length() - row]
            for (int col = 0; col < table[row].length ; col++) {
                table[row][col] = new CYKElement(subWord: str.substring(col, col+row+1 ))

                table[row][col].findWays(row, col, grammar, table)
            }
        }

    }

    @ToString
    static class CYKElement {
        Set<Rule> rules = new HashSet<>()
        String subWord

        void findWays(int row, int col, Grammar grammar, CYKElement[][] table) {

            if (row == 0) {
                rules.addAll(grammar.findRulesFor(subWord))
            }

            for (int i = 1; i <= subWord.length() - 1 ; i++) {

                CYKElement left  = table[i-1][col]
                CYKElement right = table[row-i][col+i]

                left.rules.each { leftIT ->
                    right.rules.each { rightIT ->
                        String combined = leftIT.name + rightIT.name
                        rules.addAll(grammar.findRulesFor(combined))
                    }
                }
            }

        }
    }
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
    Set<String> values = new HashSet<>()
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
//    var 'S' becomes "AB"  or "C"
//    var 'A' becomes "aAb" or "ab"
//    var 'B' becomes "cBd" or "cd"
//    var 'C' becomes "aCd" or "aDd"
//    var 'D' becomes "bDc" or "bc"
    var 'S' becomes "AB"
    var 'A' becomes "BB" or "a"
    var 'B' becomes "AB" or "b"
}

//CYKResult table = g.getGrammar().getCYKResult("bbaab")
CYKResult table = g.getGrammar().getCYKResult("aabba")


table.table.each {
    it.each {
        print "$it\t"
    }
    println()
}