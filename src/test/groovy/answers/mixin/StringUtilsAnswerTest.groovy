package answers.mixin


import spock.lang.Specification

class StringUtilsAnswerTest extends Specification {

    def setupSpec() {
        String.mixin StringUtilsAnswer
    }

    def 'string concat with comma'() {
        expect:
        'a'.concatWithComma('b') == 'a,b'
    }
}