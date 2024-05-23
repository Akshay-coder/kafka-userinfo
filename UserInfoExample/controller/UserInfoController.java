package com.kafka.UserInfoExample.controller;

import com.kafka.UserInfoExample.service.UserInfoService;
import com.kafka.UserInfoExample.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/produce/user-info")
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;


    @PostMapping
    public ResponseEntity<String> produceUserInfo(@RequestBody List<UserInfo> userInfoList){
        userInfoService.produceUserInfoData(userInfoList);
        return new ResponseEntity<>("Users Produces successfully", HttpStatus.ACCEPTED);
    }


}
