package com.wei.omini.controller;

import com.wei.omini.model.RequestContext;
import com.wei.omini.request.RemoteRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-21 17:30
 */
@RestController
public class DemoController {
    @Resource
    private RemoteRequest request;

    @GetMapping("/test")
    public String test() {
        RequestContext context = new RequestContext();
        context.setCmd("friend");
        context.setSub("add");
        context.setContent(System.currentTimeMillis());
        request.request("test", context);
        return "hello word";
    }
}
