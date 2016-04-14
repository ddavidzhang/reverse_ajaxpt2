import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class GuiceConfig extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(Stage.PRODUCTION, new WebModule());
    }
}
