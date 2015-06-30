import groovy.transform.ToString

/**
 * Created by tejp on 29/06/15.
 */
@ToString
class Rule {
    String name
    Set<RuleTarget> values = new HashSet<>()
    boolean start
}

class RuleBuilder {
    Rule rule

    RuleBuilder becomes(String s) {
        rule.values << new RuleTarget(value: s)
        this
    }

    RuleBuilder or(String s) {
        becomes(s)
        this
    }
}

@ToString
class RuleTarget {
    String value
}