/**
 * Configuration for SMTP-based email notifications.
 */
public class SmtpConfig {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String fromAddress;
    private final boolean useTls;

    public SmtpConfig(String host, int port, String username, String password,
                      String fromAddress, boolean useTls) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.fromAddress = fromAddress;
        this.useTls = useTls;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public boolean isUseTls() {
        return useTls;
    }
}
