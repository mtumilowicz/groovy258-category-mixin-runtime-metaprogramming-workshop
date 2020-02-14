package workshop.category


import spock.lang.Specification

class IntegerUtilsWorkshopTest extends Specification {
    def 'even'() {
        expect:
        use(IntegerUtilsWorkshop) {
            2.isEven()
        }
    }

    def 'not even'() {
        expect:
        use(IntegerUtilsWorkshop) {
            !1.isEven()
        }
    }
}