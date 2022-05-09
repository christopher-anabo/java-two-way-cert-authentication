package com.partior.httpclient.dto;

import lombok.Data;

@Data
public class TransactionInfo {

    private String type;
    private String details;
    private String amount;
}
