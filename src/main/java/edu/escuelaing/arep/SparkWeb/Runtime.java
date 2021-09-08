package edu.escuelaing.arep.SparkWeb;
import edu.escuelaing.arep.httpserver.HttpServer;



import java.io.IOException;


public class Runtime {
    public static void  main(String[] args) throws IOException{
        HttpServer server = new HttpServer();
        server.start();
    }
}
