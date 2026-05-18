package infra.config;

public class DBConfig {
    private final String url;
    private final String username;
    private final String password;
    private final int poolSize;

    public DBConfig(String url, String username, String password, int poolSize) {
        this.url = url; this.username = username;
        this.password = password; this.poolSize = poolSize;
    }

    public static DBConfig defaults() {
        return new DBConfig("jdbc:mysql://localhost:3306/insurance_db?serverTimezone=Asia/Seoul&characterEncoding=UTF-8", "root", "0000", 5);
    }

    public String getUrl()      { return url; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getPoolSize()    { return poolSize; }
}
