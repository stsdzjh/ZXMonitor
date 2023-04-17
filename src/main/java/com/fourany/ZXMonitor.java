package com.fourany;

import cn.hutool.core.io.FileUtil;
import com.fourany.service.ISqlService;
import com.fourany.utils.CallUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import cn.hutool.core.io.file.FileAppender;
/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableScheduling
public class ZXMonitor
{
    @Resource
    private ISqlService sqlService;

    @Value("${monitor.mobile}")
    private String mobile;

    @Value("${monitor.dial-filepath}")
    private String dialFilepath;

    @Value("${monitor.trunkName}")
    private String trunk;

    @Value("${monitor.file.paths}")
    private String[] fileArray;

    @Value("${monitor.publishlog.wav}")
    private String wavPublishLog;

    @Value("${monitor.flowchart.wav}")
    private String wavFlowChart;

    @Value("${monitor.formoney.wav}")
    private String wavForMoney;

    @Value("${monitor.file.wav}")
    private String wavFile;

    @Value("${monitor.file.archiveCallFile}")
    private String archiveCallFile;

    public static void main( String[] args )
    {
        SpringApplication.run(ZXMonitor.class,args);
    }

    /**
     * Oracle publishLog Query type 1
     */
    @Scheduled(cron = "${monitor.publishlog.sche}")
    private void monitorPublishLog(){
        List list = sqlService.queryPublishLog();
        if(list.size() > 0){
            CallUtils.makeCallFile("1", wavPublishLog, dialFilepath, trunk, mobile, archiveCallFile);
        }
    }
    /**
     * Oracle FlowChart Query type 2
     */
    @Scheduled(cron = "${monitor.flowchart.sche}")
    private void monitorFlowChart(){
        List list = sqlService.queryFlowChart();
        if(list.size() > 0){
            CallUtils.makeCallFile("2", wavFlowChart, dialFilepath, trunk, mobile, archiveCallFile);
        }
    }

    /**
     * Oracle ForMoney Query type 3
     */
    @Scheduled(cron = "${monitor.formoney.sche}")
    private void monitorForMoney(){
        List list = sqlService.queryForMoney();
        if(list.size() > 0){
            CallUtils.makeCallFile("3", wavForMoney, dialFilepath, trunk, mobile, archiveCallFile);
        }
    }
    /**
     * File Exist Query type 0
     */
    @Scheduled(cron = "${monitor.file.sche}")
    private void monitorShareFile(){
        System.out.println(fileArray);
        for(int i=0; i< fileArray.length; i++){
            if(!FileUtil.exist(fileArray[i])){
                CallUtils.makeCallFile("0", wavFile, dialFilepath, trunk, mobile, archiveCallFile);
                break;
            }
        }

    }
}
