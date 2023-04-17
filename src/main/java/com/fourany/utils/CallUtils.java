package com.fourany.utils;

import cn.hutool.core.io.file.FileAppender;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: TODO
 * @author: zhangjh
 * @date: 2023年04月16日 8:47
 */
@Slf4j
public class CallUtils {
    /**
     * 生成呼叫任务
     * @param callType  0：File 1：publishlog 2:flowchart  3:formoney  9:webRequest
     * @param dialFilepath
     * @param trunk
     * @param dialNumber
     */
    public static void makeCallFile(String callType, String wavFileName, String dialFilepath, String trunk, String dialNumber, String archive){
        String[] telArray = dialNumber.split(",");
        for(int i=0; i< telArray.length; i++){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = simpleDateFormat.format(new Date()) + "-" + callType + "-" + telArray[i] + ".call";
            File file = new File(dialFilepath  + fileName);
            if(file.exists()){
                return;
            }
            FileAppender appender = new FileAppender(file, 2, true);
            appender.append("Channel: SIP/" + trunk + "/" + telArray[i]);
            appender.append("MaxRetries: 0");
            appender.append("WaitTime: 60");
            appender.append("Context: zxjt-monitor");
            appender.append("Extension: s");
            appender.append("Priority: 1");
            appender.append("Archive: " + archive);
            appender.append("Set: TASKFILE=" + wavFileName); //语音文件名根据类型命名
            appender.flush();
            log.info(String.format("外呼任务已生成{type:%s, fileName: %s, tel: %s}", callType, fileName, telArray[i]));
        }

    }
}
