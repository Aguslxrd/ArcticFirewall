package com.damiansuffo.ArcticFirewall.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UfwRule {
    private int number;
    private String port;
    private String action;
    private String from;
    private boolean protectedRule;
}

