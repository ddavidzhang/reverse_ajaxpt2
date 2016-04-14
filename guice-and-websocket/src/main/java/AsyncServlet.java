import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class AsyncServlet extends HttpServlet {

    @Inject
    Provider<User> user;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // fetch the request-scoped user. It is not here before so it should be loaded from repsoitory and get an ID
        // the repository waits for 2 seconds.
        long time1 = System.currentTimeMillis();
        User user1 = user.get();
        time1 = System.currentTimeMillis() - time1;

        // load it again to verify it is cached
        long time2 = System.currentTimeMillis();
        User user2 = user.get();
        time2 = System.currentTimeMillis() - time2;

        resp.getWriter().write("[From HTTP Request] Loaded " + user1 + " in " + time1 + "ms and again " + user2 + " in " + time2 + "ms");
    }
}
