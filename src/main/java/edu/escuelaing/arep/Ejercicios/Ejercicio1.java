package edu.escuelaing.arep.Ejercicios;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ejercicio1 {

    public  static void main(String[] args){
        analisisURL("https://campusvirtual.escuelaing.edu.co/moodle/mod/assign/view.php?id=74411");
        analisisURL("https://www.youtube.com/watch?v=G4opLHyXp-c&list=RDG4opLHyXp-c&start_radio=1");
        analisisURL("https://campusvirtual.escuelaing.edu.co/moodle/mod/assign/view.php?id=34731");
    }

    public static void analisisURL(String lineURL){
        try{
            URL siteUrl = new URL(lineURL);
            System.out.println(siteUrl);
            System.out.println("Protocol: "+siteUrl.getProtocol());
            System.out.println("Authority: "+siteUrl.getAuthority());
            System.out.println("Host: "+siteUrl.getHost());
            System.out.println("Port: "+siteUrl.getPort());
            System.out.println("Path: "+siteUrl.getPath());
            System.out.println("Query: "+siteUrl.getQuery());
            System.out.println("File: "+siteUrl.getFile());
            System.out.println("Ref: "+siteUrl.getRef());
            System.out.println("-----------------------------");
        }catch (MalformedURLException ex){
            Logger.getLogger(Ejercicio1.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
}
