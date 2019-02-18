package com.example.idutil.distributedidgenerator.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : gaohui
 * @Date : 2019/2/18 11:09
 * @Description :
 */

@RestController
public class TestController {

    private int i;

    @RequestMapping("/hi")
    public String hi() {
        return "hello world " + i++;
    }
}
