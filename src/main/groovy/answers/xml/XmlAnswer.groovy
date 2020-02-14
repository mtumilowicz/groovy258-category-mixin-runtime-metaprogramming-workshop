package answers.xml

import groovy.xml.MarkupBuilder

class XmlAnswer {
    def static languagesMap = ['C++': 'Bjarne Stroustrup', 'Java': 'James Gosling', 'Haskell': 'Simon Peyton Jones']

    static String toXml() {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.languages {
            languagesMap.each { k, v ->
                language(name: k) { author(v) }
            }
        }

        writer.toString()
    }
}
