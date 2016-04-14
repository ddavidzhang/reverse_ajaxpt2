import org.codehaus.jettison.json.JSONArray;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Random;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ReverseAjaxServlet extends WebSocketServlet {

    private final Endpoints endpoints = new Endpoints();
    private final Random random = new Random();
    private final Thread generator = new Thread("Event generator") {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(random.nextInt(5000));
                    endpoints.broadcast(new JSONArray().put("At " + new Date()).toString());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    };

    @Override
    public void init() throws ServletException {
        super.init();
        generator.start();
    }

    @Override
    public void destroy() {
        generator.interrupt();
        super.destroy();
    }

    @Override
    protected WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        return endpoints.newEndpoint();
    }


}
