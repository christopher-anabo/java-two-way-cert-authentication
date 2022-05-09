package com.partior.httpclient.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Component
public class CcfRestClient {

    @Autowired
    private RestTemplate restTemplate;

    public String balance(String bank) throws JsonProcessingException {

        final String url = "https://192.168.1.49:8000/app/payment/balance";



        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> bodyParamMap = new HashMap<String, Object>();
        bodyParamMap.put("account", bank);

        String reqBodyData = new ObjectMapper().writeValueAsString(bodyParamMap);

        HttpEntity<String> requestEntity = new HttpEntity<>(reqBodyData, headers);


        ResponseEntity<HashMap> result = restTemplate.postForEntity(url, requestEntity, HashMap.class);

        return result.getBody().get("amount").toString();


    }


        public String deposit(String bank, String amount) throws JsonProcessingException {

            final String url = "https://192.168.1.49:8000/app/payment/deposit";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> bodyParamMap = new HashMap<String, Object>();
            bodyParamMap.put("account", bank);
            bodyParamMap.put("amount", Integer.valueOf(amount));

            String reqBodyData = new ObjectMapper().writeValueAsString(bodyParamMap);

            HttpEntity<String> requestEntity = new HttpEntity<>(reqBodyData, headers);


            ResponseEntity<HashMap> result = restTemplate.postForEntity(url, requestEntity, HashMap.class);


            return result.getBody().get("msg").toString();
        }

    public String transfer(String from, String to, String amount) throws JsonProcessingException {

        final String url = "https://192.168.1.49:8000/app/payment/transfer";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> bodyParamMap = new HashMap<String, Object>();

        bodyParamMap.put("accountFrom", from);
        bodyParamMap.put("accountTo", to);
        bodyParamMap.put("amount",  Integer.valueOf(amount));
        String reqBodyData = new ObjectMapper().writeValueAsString(bodyParamMap);

        HttpEntity<String> requestEntity = new HttpEntity<>(reqBodyData, headers);


        ResponseEntity<HashMap> result = restTemplate.postForEntity(url, requestEntity, HashMap.class);


        return result.getBody().get("msg").toString();

        }




}
