package edu.escuelaing.arep.httpserver;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {
    private boolean running;
    private int port;

    public HttpServer(){
        port=4500;
        running = false;
    }

    public void start() throws IOException {
        ServerSocket serverSocket=  null;
        try{
            serverSocket = new ServerSocket(getPort());
            running =true;
        }catch (IOException e){
            System.out.println("Could no listen on port: 4500");
            System.exit(1);
        }

        while(running){
            Socket clientSocket = null;

            try{
                System.out.println("Ready to recive...");
                clientSocket = serverSocket.accept();
            }catch (IOException e){
                System.out.println("Accept Failed");
                System.exit(1);
            }
            PrintWriter out= new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outpurLine;

            Map< String, String> request = new HashMap<>();
            boolean rLineR = false;

            while ((inputLine = in.readLine())!= null){
                System.out.println("Recive: "+ inputLine);
                if(!rLineR){
                    request.put("Solicitud Line", inputLine);
                    rLineR = true;
                }else {
                    String[] data = creatData(inputLine);
                    if(data.length>1){
                        request.put(data[0],data[1]);
                    }
                }
                if (!in.ready()){
                    break;
                }
            }
            System.out.println("linea pedida"+ request.get("lineaSolicitud"));

            createResponse(clientSocket.getOutputStream(), request.get("lineaSolicitud"));
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    private String[] creatData(String cEntrada){
        return cEntrada.split(":");
    }

    private static int getPort() {
        if (System.getenv("PORT") != null){
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4500;
    }
    private void createResponse(OutputStream out, String request) {
        PrintWriter printWriter = new PrintWriter(out);
        String[] resource = response(request);
        String value = "";
        String type = "";


        if (resource != null) {
            value = resource[0];
            type = resource[1];

        }
        String html = "";
        if(type.equals("txt")){
            html = mostrarTexto(value);
        }
        else if(type.equals("jpg") ){
            getImagen("src/main/resource/imagenes/"+value+".jpg",out);
        }

        else if (type.equals("html")) {
            html = getPage(value,type);
        } else {
            html = "HTTP/1.1 404 Not Found\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n"
                    + "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "<head>\n"
                    + "<meta charset=\"UTF-8\">\n"
                    + "<title>Web Server</title>\n"
                    + "</head>\n"
                    + "<body>\n"
                    + "<h1>El recurso "+value +" no fue encontrado</h1>\n"
                    + "</body>\n"
                    + "</html>\n";
        }

        printWriter.println(html);
        printWriter.close();
    }

    private String getPage(String value,String type) {
        String pag = "";
        try {
            pag = getPageCode("src/main/resource/"+value+"."+type,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pag;

    }

    private String getPageCode(String path, String type) {
        StringBuilder data = new StringBuilder();
        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new FileReader(path));
            data.append("HTTP/1.1 200 OK\r\n");
            if(type.equals("html"))
                data.append("Content-Type: text/html\r\n");
            else
                data.append("Content-Type: application/javascript\r\n");

            data.append("\r\n");
            String line = buffer.readLine();
            while (line != null) {
                data.append(line + "\n");
                line = buffer.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }



    private void getImagen(String path, OutputStream out) {
        BufferedImage imagen;
        try{
            imagen = ImageIO.read(new File(path));
            ByteArrayOutputStream ImageBytes = new ByteArrayOutputStream();
            DataOutputStream mostrar = new DataOutputStream(out);
            ImageIO.write(imagen, "JPG", ImageBytes);
            mostrar.writeBytes("HTTP/1.1 200 OK\r\n"
                    + "Content-Type: image/jpeg\r\n"
                    + "\r\n");
            mostrar.write(ImageBytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String  mostrarTexto(String value) {
        String mostrar = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Mini Spark</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "<h1>Bienvenido a tu Servidor Web</h1>\n"
                + "<h2>La palabra que ud ingreso es: </h1> \n"
                + "<h2>" + value + "</h2>\n"
                + "</body>\n"
                + "</html>\n";

        return  mostrar;

    }

    private String[] response(String request) {
        String[] info;
        info = null;
        if (request != null) {
            info =new String[2];
            String[] values = request.split(" ");
            String resource = values[1].replace("/", "");
            if (resource.contains(".")) {
                String[] data = resource.split("\\.");
                info[0] = data[0];
                info[1] = data[1];
            } else {
                info[0] = resource;
                info[1] = "text";
            }
        }
        return info;
    }



}
