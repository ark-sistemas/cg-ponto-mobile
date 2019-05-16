package senai.fatesg.com.cgponto.email;

public enum Config {

    EMAIL("EMAIL"), PASSWORD("PASSWORD");

    private String config;

    Config(String config){
        this.config = config;
    }

    public String getConfig() {
        return config;
    }
}
