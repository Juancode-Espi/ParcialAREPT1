package edu.escuelaing.arep;
import java.net.*;
import java.nio.charset.Charset;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpServer {

    private static final String WHEATER_QUERY = "https://api.openweathermap.org/data/2.5/weather?q=country&appid=d1bcfbab47d918a819df1b59af4eee93";

    private static final HttpServer _instance = new HttpServer();

    public static HttpServer getInstance(){
        return _instance;
    }

    private HttpServer(){
    }

    public String start() throws IOException{
        ServerSocket serverSocket = null;
        String jsonString="VACIOS";
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while(running){
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            try {
                jsonString=serverConnection(clientSocket);
            } catch (URISyntaxException e) {
                System.err.println("URI incorrect.");
                System.exit(1);
            }
        }
        serverSocket.close();
        return jsonString;
    }

    public String serverConnection(Socket clientSocket) throws IOException, URISyntaxException {
        OutputStream outStream=clientSocket.getOutputStream();
        PrintWriter out = new PrintWriter(outStream, true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
        System.out.println("in "+in);
        String inputLine, outputLine;
        ArrayList<String> request = new ArrayList<>();
        String sv="";

        //System.out.println("in.readLine() "+in.readLine());
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Response: "+inputLine);
            request.add(inputLine);
            if (!in.ready()) {
                break;
            }
        }
        System.out.println("request "+request.get(0).split(" ")[1]);
        String uriContentType="";

        try {

            uriContentType=request.get(0).split(" ")[1];
            //URI resource = new URI(uriContentType);


        }catch(Exception e){
            System.out.println(e);
        }
        outputLine = getResource( uriContentType, outStream);
        //out.println(outputLine);
        out.close();
        in.close();
        clientSocket.close();

        return outputLine;
    }





    public String computeDefaultResponse(){
        String outputLine =
                "HTTP/1.1 200 OK\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<meta charset=\"UTF-8\">"
                        + "<title>Title of the document</title>\n"
                        + "</head>"
                        + "<body>"
                        + "My Web Site"
                        + "</body>"
                        + "</html>";
        return outputLine;
    }
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000; //returns default port if heroku-port isn't set (i.e. on localhost)
    }





    public static void main(String[] args) throws IOException {
        HttpServer.getInstance().start();
    }
}
