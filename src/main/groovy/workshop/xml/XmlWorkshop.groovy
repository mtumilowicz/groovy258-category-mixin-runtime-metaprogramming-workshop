package workshop.xml

import groovy.xml.MarkupBuilder

class XmlWorkshop {
    def static languages = ['C++': 'Bjarne Stroustrup', 'Java': 'James Gosling', 'Haskell': 'Simon Peyton Jones']

    static String toXml() {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        // create xml:
        // <languages>
        //  <language name: C++>
        //      <author>Bjarne Stroustrup

        writer.toString()
    }
}
