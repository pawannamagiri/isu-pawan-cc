package com.example.p2papp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

/**
 * Author @ Pawan Namagiri
 **/

@Service
public class RegisterService {

    //get SERVER value from config file using annotation
    @Value("${SERVER_ADDRESS}")
    private String server;

    @Value("${SERVER_PORT}")
    private String serverPort;


    public String registerWithIndexServer() {


        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String localIp = inetAddress.getHostAddress();

        System.out.println("Local IP address: " + localIp);

        String listOfFiles= getListOfFiles();



        String url = server+"/register";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = "{\"ip\":\""+localIp+"\",\"files\":"+listOfFiles+"}";
        System.out.println(requestJson);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        System.out.println("Response status code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());


        return "pass";
    }

    private String getListOfFiles(){



        String folderPath = System.getProperty("user.home")+"/Desktop/p2p-files";
        File folder = new File(folderPath);
        String[] listOfFiles = folder.list();

        ObjectMapper mapper = new ObjectMapper();
        String jsonArray = null;
        try {
            jsonArray = mapper.writeValueAsString(listOfFiles);
            System.out.println(jsonArray);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return jsonArray;
    }



}
