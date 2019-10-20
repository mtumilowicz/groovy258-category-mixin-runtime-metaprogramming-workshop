import mixin.StringUtils
import spock.lang.Specification

class StringUtilsTest extends Specification {

    def setupSpec() {
        String.mixin StringUtils
    }

    def "string concat with comma"() {
        expect:
        "a".concatWithComma("b") == "a,b"
    }
}