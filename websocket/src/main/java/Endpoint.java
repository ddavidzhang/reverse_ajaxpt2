import org.codehaus.jettison.json.JSONArray;
import org.eclipse.jetty.websocket.WebSocket;

import java.io.IOException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Endpoint implements WebSocket {

    private static int clientCounter = 0;
    private Outbound outbound;
    private Endpoints endpoints;
    private int clientId = clientCounter++;

    Endpoint(Endpoints endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public void onConnect(Outbound outbound) {
        this.outbound = outbound;
        send(new JSONArray().put("ClientID = " + clientId).toString());
        endpoints.offer(this);
    }

    @Override
    public void onMessage(byte opcode, String data) {
        endpoints.broadcast(new JSONArray().put("From " + clientId + " : " + data).toString());
    }

    @Override
    public void onFragment(boolean more, byte opcode, byte[] data, int offset, int length) {
        // when a fragment is completed, onMessage is called
    }

    @Override
    public void onMessage(byte opcode, byte[] data, int offset, int length) {
        onMessage(opcode, new String(data, offset, length));
    }

    @Override
    public void onDisconnect() {
        endpoints.remove(this);
        outbound = null;
    }

    void send(String data) {
        try {
            if (outbound != null && outbound.isOpen()) {
                outbound.sendMessage(data);
            }
        } catch (IOException e) {
            outbound.disconnect();
        }
    }
}
