import com.google.inject.Key;
import com.google.inject.servlet.ServletScopes;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class WSServletFixed extends WebSocketServlet {

    @Inject
    Provider<User> user;

    @Override
    protected WebSocket doWebSocketConnect(final HttpServletRequest request, String protocol) {
        return new WebSocket() {
            Outbound outbound;

            @Override
            public void onConnect(Outbound outbound) {
                this.outbound = outbound;
            }

            @Override
            public void onMessage(byte opcode, String data) {
                Map<Key<?>, Object> bindings = new HashMap<Key<?>, Object>();
                bindings.put(Key.get(HttpServletRequest.class), request);
                try {
                    ServletScopes.scopeRequest(new Callable<Object>() {
                            @Override
                            public Object call() throws Exception {
                                // fetch the request-scoped user. It is not here before so it should be loaded from repsoitory and get an ID
                                // the repository waits for 2 seconds.
                                long time1 = System.currentTimeMillis();
                                User user1 = user.get();
                                time1 = System.currentTimeMillis() - time1;

                                // load it again to verify it is cached
                                long time2 = System.currentTimeMillis();
                                User user2 = user.get();
                                time2 = System.currentTimeMillis() - time2;

                                outbound.sendMessage("[From FIXED WebSocket onMessage] Loaded " + user1 + " in " + time1 + "ms and again " + user2 + " in " + time2 + "ms");
                                return null;
                            }
                        }, bindings).call();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
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
