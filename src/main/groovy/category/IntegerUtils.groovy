package category

@Category(Integer)
class IntegerUtils {
    boolean isEven() {
        this % 2 == 0
    }
}