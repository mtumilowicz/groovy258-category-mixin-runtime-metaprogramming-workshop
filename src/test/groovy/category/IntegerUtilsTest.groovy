package category

import spock.lang.Specification

class IntegerUtilsTest extends Specification {
    def "even"() {
        expect:
        use(IntegerUtils) {
            2.isEven()
        }
    }

    def "not even"() {
        expect:
        use(IntegerUtils) {
            !1.isEven()
        }
    }
}
