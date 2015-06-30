import groovy.transform.ToString

/**
 * Created by tejp on 29/06/15.
 */
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

    /**
     *
     * @return the set rules which has a start variable which can generate the given word.
     */
    Set<Rule> topRules() {
        table[table.length-1][0].rules
    }

    @Override
    String toString() {
        StringBuilder stringBuilder = new StringBuilder()
        table.each {
            it.each {
                stringBuilder.append("$it\t")
            }
            stringBuilder.append("\n")
        }
        stringBuilder.toString()
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
