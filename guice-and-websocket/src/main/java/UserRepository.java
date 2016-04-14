/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class UserRepository {
    User load(String id) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e.getMessage(), e);
        }
        return new User();
    }
}
