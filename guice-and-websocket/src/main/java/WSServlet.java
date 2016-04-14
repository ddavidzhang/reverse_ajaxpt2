import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class WSServlet extends WebSocketServlet {

    @Inject
    Provider<User> user;

    @Override
    protected WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        return new WebSocket() {
            Outbound outbound;

            @Override
            public void onConnect(Outbound outbound) {
                this.outbound = outbound;
            }

            @Override
            public void onMessage(byte opcode, String data) {
                try {
                    // fetch the request-scoped user. It is not here before so it should be loaded from repsoitory and get an ID
                    // the repository waits for 2 seconds.
                    long time1 = System.currentTimeMillis();
                    User user1 = user.get();
                    time1 = System.currentTimeMillis() - time1;

                    // load it again to verify it is cached
                    long time2 = System.currentTimeMillis();
                    User user2 = user.get();
                    time2 = System.currentTimeMillis() - time2;

                    outbound.sendMessage("[From WebSocket onMessage] Loaded " + user1 + " in " + time1 + "ms and again " + user2 + " in " + time2 + "ms");
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        outbound.sendMessage("ERROR: " + e.getMessage());
                    } catch (IOException e1) {
                        throw new RuntimeException(e1.getMessage(), e1);
                    }
                }
            }

            @Override
            public void onFragment(boolean more, byte opcode, byte[] data, int offset, int length) {
            }

            @Override
            public void onMessage(byte opcode, byte[] data, int offset, int length) {
            }

            @Override
            public void onDisconnect() {
            }
        };
    }

}
