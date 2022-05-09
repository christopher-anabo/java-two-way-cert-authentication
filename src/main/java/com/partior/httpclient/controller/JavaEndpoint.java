package com.partior.httpclient.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.partior.httpclient.client.CcfRestClient;
import com.partior.httpclient.dto.TransactionInfo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class JavaEndpoint {


    @Autowired
    CcfRestClient ccfRestClient;

    List<Map<String,String>> history = new ArrayList<>();

    @GetMapping("/api/deposit/{bank}/{amount}")
    @ResponseBody
    public ResponseEntity<String>  deposit(@PathVariable String bank, @PathVariable String amount) throws JsonProcessingException {

        String response = ccfRestClient.deposit(bank, amount);
        appendToHistory("DEPOSIT", "TO:" + bank, amount);
        return new ResponseEntity<String>(response, HttpStatus.OK);

    }

    @GetMapping("/api/balance/{bank}")
    @ResponseBody
    public ResponseEntity<String> balance(@PathVariable String bank) throws JsonProcessingException {
         String response = ccfRestClient.balance(bank);
        appendToHistory("BALANCE", "TO:" + bank, response);
         return new ResponseEntity<String>(response, HttpStatus.OK);

    }

    @GetMapping("/api/transfer/{accountFrom}/{accountTo}/{amount}")
    @ResponseBody
    public ResponseEntity<String>  transfer(@PathVariable String accountFrom, @PathVariable String accountTo, @PathVariable String amount) throws JsonProcessingException {
        String response = ccfRestClient.transfer(accountFrom, accountTo, amount);
        appendToHistory("TRANSFER", "FROM:" + accountFrom + " TO:" + accountTo, amount);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }


    @GetMapping("/api/latest")
    @ResponseBody
    //public ResponseEntity<Map<String,Object>>  latestTransaction() throws JsonProcessingException {
    public ResponseEntity<Map<String,Object>> latestTransaction() {

        Map<String,Object> dataTable = new HashMap<>();
        dataTable.put("draw",1);
        dataTable.put("recordsFiltered",history.size());
        dataTable.put("recordsTotal",history.size());
        dataTable.put("data",history);
        return new ResponseEntity<Map<String,Object>>(dataTable, HttpStatus.OK);
    }

    @GetMapping("/api/echo")
    @ResponseBody
    public ResponseEntity<String> echo() {
        return new ResponseEntity<String>("echo", HttpStatus.OK);

    }

    private void appendToHistory(String type, String details, String amount){

        Map<String,String> detail = new HashMap<>();
        detail.put("type", type);
        detail.put("details", details);
        detail.put("amount", amount);

        history.add(detail);

    }
}
