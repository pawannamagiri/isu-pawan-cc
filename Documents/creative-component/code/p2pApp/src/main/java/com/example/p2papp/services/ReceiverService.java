package com.example.p2papp.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Author @ Pawan Namagiri
 **/

@Service
public class ReceiverService {

    @Value("${SERVER_ADDRESS}")
    private String server;

    public String[] findFileOwner(String fileName) {


        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);



        String url = server+"/find?fileName="+fileName;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,null ,String.class);

        System.out.println("Response status code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
//        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONArray jsonArray = new JSONArray(response.getBody());
        String[] listOfOwners = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            listOfOwners[i] = jsonObject.getString("ip");
        }

        return listOfOwners;
    }
}
