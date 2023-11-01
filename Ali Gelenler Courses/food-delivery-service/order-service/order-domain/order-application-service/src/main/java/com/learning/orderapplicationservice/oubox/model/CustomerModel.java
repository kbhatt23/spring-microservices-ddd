package com.learning.orderapplicationservice.oubox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CustomerModel {
    private String id;
    
    private String userName;
    
    private String firstName;
    
    private String lastName;
}