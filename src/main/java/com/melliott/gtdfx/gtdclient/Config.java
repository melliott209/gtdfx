package com.melliott.gtdfx.gtdclient;

public record Config(
        boolean remote,
        String serverIP,
        String serverPort
) {
    public static final Config DEFAULT = new Config(false, "127.0.0.1", "8080");
}
