package workshop.xml


import org.xmlunit.builder.DiffBuilder
import org.xmlunit.builder.Input
import org.xmlunit.diff.Diff
import spock.lang.Specification

class XmlWorkshoKpTest extends Specification {
    def 'xmls should have no differences'() {
        given:
        def test = XmlWorkshop.toXml()

        def control = '''
                            <languages>
                              <language name='C++'>
                                <author>Bjarne Stroustrup</author>
                              </language>
                              <language name='Java'>
                                <author>James Gosling</author>
                              </language>
                              <language name='Haskell'>
                                <author>Simon Peyton Jones</author>
                              </language>
                            </languages>
                       '''

        when:
        Diff d = DiffBuilder.compare(Input.fromString(control))
                .withTest(Input.fromString(test))
                .ignoreWhitespace()
                .ignoreComments()
                .normalizeWhitespace()
                .build()

        then:
        !d.hasDifferences()
    }
}
