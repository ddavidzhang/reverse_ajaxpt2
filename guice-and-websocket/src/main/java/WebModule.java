import com.google.inject.Provides;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class WebModule extends ServletModule {
    @Override
    protected void configureServlets() {
        bind(UserRepository.class).in(Singleton.class);

        // bind our websocket servlet to test request-scoped object
        bind(WSServlet.class).in(Singleton.class);
        serve("/ws").with(WSServlet.class);

        // bind our websocket servlet to test request-scoped object with the fix for scoping
        bind(WSServletFixed.class).in(Singleton.class);
        serve("/ws-fixed").with(WSServletFixed.class);

        // bind our a standard http servlet to test request-scoped object
        bind(AsyncServlet.class).in(Singleton.class);
        serve("/async").with(AsyncServlet.class);
    }

    @Provides
    @RequestScoped
    User fetchUser(HttpServletRequest request, UserRepository repository) {
        String userId = (String) request.getSession().getAttribute("user");
        return repository.load(userId);
    }
}
