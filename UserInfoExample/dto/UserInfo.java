package com.kafka.UserInfoExample.dto;

import lombok.Data;

@Data
public class UserInfo {

    private String user_id;

    private String name;

    private byte age;

    private String destination;

}
