package com.fourany.controller;

import cn.hutool.json.JSONObject;
import com.fourany.common.Result;
import com.fourany.utils.CallUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: TODO
 * @author: zhangjh
 * @date: 2023年04月17日 9:41
 */
@Slf4j
@RestController
@RequestMapping("/callcenter")
public class CallController {

    @Value("${monitor.dial-filepath}")
    private String dialFilepath;

    @Value("${monitor.trunkName}")
    private String trunk;

    @PostMapping(value = "/callTrigger")
    public Result<Object> callTrigger(@RequestBody JSONObject jsonObject){
        log.info("收到管理端呼叫请求消息：{}",jsonObject);
        if(jsonObject == null){
            return Result.error("请传递消息体：{{\"telnumber\": \"13910101010,13800138000\",\"content\": \"monitor.wav\"}}");
        }

        String telNumber = jsonObject.getStr("telnumber");
        String wav = jsonObject.getStr("content");
        if(StringUtils.isEmpty(telNumber) || StringUtils.isEmpty(wav)){
            return Result.error("消息体缺少参数：{{\"telnumber\": \"13910101010,13800138000\",\"content\": \"monitor.wav\"}}");
        }

        CallUtils.makeCallFile("9", wav, dialFilepath, trunk, telNumber);
        return Result.OK();
    }
}
