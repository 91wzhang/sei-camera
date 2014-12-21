package ic.doc;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

public class ExampleJMock2Test {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void clientCallsServer() {

        final Server server = context.mock(Server.class);

        MyClient client = new MyClient(server);

        context.checking(new Expectations() {{
            exactly(1).of(server).doAServerThing();
        }});

        client.doAClientThing();

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