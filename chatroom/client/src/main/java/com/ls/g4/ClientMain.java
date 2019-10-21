package com.ls.g4;

import com.ls.g4.client.Client;
import com.ls.g4.config.ClientProperties;

public class ClientMain {
  public static void main(String[] args) {
    Client client = new Client(ClientProperties.host, ClientProperties.port);
    client.start();
  }
}
