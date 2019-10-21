package answers.category


import spock.lang.Specification

class IntegerUtilsAnswerTest extends Specification {
    def "even"() {
        expect:
        use(IntegerUtilsAnswer) {
            2.isEven()
        }
    }

    def "not even"() {
        expect:
        use(IntegerUtilsAnswer) {
            !1.isEven()
        }
    }
}
