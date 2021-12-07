package com.cs4400.service_backend.controller;

import com.cs4400.service_backend.entity.Account;
import com.cs4400.service_backend.entity.Property;
import com.cs4400.service_backend.service.Login;
import com.cs4400.service_backend.service.RegisterUser;
import com.cs4400.service_backend.service.PropertyProcess;
import com.cs4400.service_backend.vo.LoginInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@Api(tags = "Property Controller")
@RestController
public class PropertyController {

    @Autowired
    private RegisterUser registerUser;

    @Autowired
    private Login login;

    @Autowired
    private PropertyProcess propertyProcess;

    /**
     *
     * @param high h
     * @param low l
     * @return
     */
    @GetMapping(value = "/properties")
    @ApiOperation(value = "view Properties", notes = "view properties.")
    public List<Property> properties(@RequestParam Integer high,@RequestParam Integer low) {
        List<Property> result = propertyProcess.viewProperties(high, low);
        System.out.println(result);
        return result;
    }



}

