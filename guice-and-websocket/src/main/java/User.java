/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class User {
    private volatile static int total = 0;
    private final int id = total++;

    @Override
    public String toString() {
        return "User<" + id + ">";
    }
}
