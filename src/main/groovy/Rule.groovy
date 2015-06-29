import groovy.transform.ToString

/**
 * Created by tejp on 29/06/15.
 */
@ToString
class Rule {
    String name
    Set<String> values = new HashSet<>()
    boolean start
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
