package org.demo.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootApplication
public class BackendApplication {


    public static void main(String[] args) throws IOException {
        SpringApplication.run(BackendApplication.class, args);


        ServerSocket serverSocket = new ServerSocket(15151);
        while (true) {
            Socket socket = serverSocket.accept();
            socket.close();
        }
    }

}
