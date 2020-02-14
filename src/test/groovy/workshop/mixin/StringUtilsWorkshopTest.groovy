package workshop.mixin


import spock.lang.Specification

class StringUtilsWorkshopTest extends Specification {

    def setupSpec() {
        String.mixin StringUtilsWorkshop
    }

    def 'string concat with comma'() {
        expect:
        'a'.concatWithComma('b') == 'a,b'
    }
}