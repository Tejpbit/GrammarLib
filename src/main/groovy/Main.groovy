import groovy.transform.ToString
import org.codehaus.groovy.control.CompilerConfiguration

import java.util.stream.Collectors

/**
 * Created by tejp on 29/06/15.
 */



//CYKResult table = g.getGrammar().getCYKResult("bbaab")
CYKResult table = g.getGrammar().getCYKResult("aabba")


table.table.each {
    it.each {
        print "$it\t"
    }
    println()
}