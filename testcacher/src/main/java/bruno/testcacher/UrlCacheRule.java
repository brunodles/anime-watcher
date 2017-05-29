package bruno.testcacher;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class UrlCacheRule implements TestRule {

    private final int httpPort;
    private final int httpsPort;

    public UrlCacheRule(int httpPort, int httpsPort) {
        this.httpPort = httpPort;
        this.httpsPort = httpsPort;
    }

    @Override public Statement apply(final Statement base, Description description) {
        return new CacheStatement(base, httpPort, httpsPort);
    }

    private static final class CacheStatement extends Statement {

        static final String HTTP_PROXY_HOST = "http.proxyHost";
        static final String HTTP_PROXY_PORT = "http.proxyPort";
        static final String HTTPS_PROXY_HOST = "https.proxyHost";
        static final String HTTPS_PROXY_PORT = "http.proxyPort";

        private final Statement inner;
        private final int httpPort;
        private final int httpsPort;

//        private Thread httpAceptThread = new ThreadProxy()

        CacheStatement(Statement inner, int httpPort, int httpsPort) {
            this.inner = inner;
            this.httpPort = httpPort;
            this.httpsPort = httpsPort;
        }

        @Override public void evaluate() throws Throwable {
            Throwable throwable = null;
            setup();
            try {
                inner.evaluate();
            } catch (Throwable t) {
                throwable = t;
            }
            teardown();
            if (throwable != null)
                throw throwable;
        }

        private void setup() {
            System.setProperty(HTTP_PROXY_HOST, "localhost");
            System.setProperty(HTTP_PROXY_PORT, String.valueOf(httpPort));
            System.setProperty(HTTPS_PROXY_HOST, "localhost");
            System.setProperty(HTTPS_PROXY_PORT, String.valueOf(httpsPort));
            Proxy.INSTANCE.start(httpPort);
        }

        private void teardown() {
            Proxy.INSTANCE.stop();
            System.clearProperty(HTTP_PROXY_HOST);
            System.clearProperty(HTTP_PROXY_PORT);
            System.clearProperty(HTTPS_PROXY_HOST);
            System.clearProperty(HTTPS_PROXY_PORT);
        }
    }
}
