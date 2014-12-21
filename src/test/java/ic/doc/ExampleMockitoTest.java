package ic.doc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExampleMockitoTest {

    @Test
    public void clientCallsServer() {

        Server server = mock(Server.class);

        MyClient client = new MyClient(server);

        client.doAClientThing();

        verify(server).doAServerThing();

    }

}

// These classes are the implementation - here they are in the same file just for convenience
// Put your implementation code under src/main/java

class MyClient {

    private final Server server;

    public MyClient(Server server) {
        this.server = server;
    }

    public void doAClientThing() {
        server.doAServerThing();
    }
}

interface Server {
    void doAServerThing();
}