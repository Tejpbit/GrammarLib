import groovy.transform.ToString
import org.codehaus.groovy.control.CompilerConfiguration

import java.util.stream.Collectors

/**
 * Created by tejp on 29/06/15.
 */



//CYKResult table = g.getGrammar().getCYKResult("bbaab")
//CYKResult table = g.getGrammar().getCYKResult("aabba")




GrammarDelegate grammarDelegate = GrammarDelegate.load(getClass().classLoader.getResource("MyGrammar.groovy").toURI())
CYKResult result = grammarDelegate.grammar.getCYKResult("bbaab")

println result