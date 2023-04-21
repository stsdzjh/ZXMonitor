package com.fourany;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fourany.service.ISqlService;
import com.fourany.utils.CallUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Hello world!
 *
 */
@Slf4j
@SpringBootApplication
@EnableScheduling
public class ZXMonitor
{
    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private ISqlService sqlService;

    @Value("${monitor.mobile}")
    private String mobile;

    @Value("${monitor.dial-filepath-tmp}")
    private String dialFilepathTmp;

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

    @Value("${monitor.archiveCallFile}")
    private String archiveCallFile;

    @Value("${monitor.get-dial-info-url}")
    private String dialInfoUrl;

    @Value("${monitor.obcid}")
    private String obcid;

    @Value("${monitor.max-retries}")
    private String maxRetries;

    @Value("${monitor.retry-time}")
    private String retryTime;

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
            String url = dialInfoUrl + "?monitorType=01";
            JSONObject getResult = restTemplate.getForObject(url, JSONObject.class);
            if(getResult.getString("code").equals("200")){
                JSONArray data = getResult.getJSONArray("data");
                if(data == null ){
                    return;
                }
                for(int i=0; i< data.size(); i++){
                    JSONObject item = data.getJSONObject(i);
                    String telNumber = item.getString("telnumber");
                    String wav = item.getString("content");
                    String logId = item.getString("logId");
                    CallUtils.makeCallFile("1", wav, dialFilepathTmp, dialFilepath, trunk, obcid, telNumber, archiveCallFile, logId, maxRetries, retryTime);
                }

            }else{
                log.error("请求失败");
            }

        }
    }
    /**
     * Oracle FlowChart Query type 2
     */
    @Scheduled(cron = "${monitor.flowchart.sche}")
    private void monitorFlowChart(){
        List list = sqlService.queryFlowChart();
        if(list.size() > 0){
            if(list.size() > 0){
                String url = dialInfoUrl + "?monitorType=02";
                JSONObject getResult = restTemplate.getForObject(url, JSONObject.class);
                if(getResult.getString("code").equals("200")){
                    JSONArray data = getResult.getJSONArray("data");
                    if(data == null ){
                        return;
                    }
                    for(int i=0; i< data.size(); i++){
                        JSONObject item = data.getJSONObject(i);
                        String telNumber = item.getString("telnumber");
                        String wav = item.getString("content");
                        String logId = item.getString("logId");
                        CallUtils.makeCallFile("2", wav, dialFilepathTmp, dialFilepath, trunk, obcid, telNumber, archiveCallFile,logId,  maxRetries, retryTime);
                    }

                }else{
                    log.error("请求失败");
                }

            }

        }
    }

    /**
     * Oracle ForMoney Query type 3
     */
    @Scheduled(cron = "${monitor.formoney.sche}")
    private void monitorForMoney(){
        List list = sqlService.queryForMoney();
        if(list.size() > 0){
            String url = dialInfoUrl + "?monitorType=03";
            JSONObject getResult = restTemplate.getForObject(url, JSONObject.class);
            if(getResult.getString("code").equals("200")){
                JSONArray data = getResult.getJSONArray("data");
                if(data == null ){
                    return;
                }
                for(int i=0; i< data.size(); i++){
                    JSONObject item = data.getJSONObject(i);
                    String telNumber = item.getString("telnumber");
                    String wav = item.getString("content");
                    String logId = item.getString("logId");
                    CallUtils.makeCallFile("3", wav, dialFilepathTmp, dialFilepath, trunk, obcid, telNumber, archiveCallFile,logId, maxRetries, retryTime);
                }

            }else{
                log.error("请求失败");
            }

        }
    }
    /**
     * File Exist Query type 0
     */
    @Scheduled(cron = "${monitor.file.sche}")
    private void monitorShareFile(){
        log.info("fileArray: {}", fileArray);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String nowDate = simpleDateFormat.format(new Date());

        for(int i=0; i< fileArray.length; i++){
            String fileName = fileArray[i].replace("#DATE#", nowDate);
            if(!FileUtil.exist(fileName)){
                String url = dialInfoUrl + "?monitorType=00";
                JSONObject getResult = restTemplate.getForObject(url, JSONObject.class);
                if(getResult.getString("code").equals("200")){
                    JSONArray data = getResult.getJSONArray("data");
                    if(data == null ){
                        break;
                    }
                    for(int j=0; j< data.size(); j++){
                        JSONObject item = data.getJSONObject(j);
                        String telNumber = item.getString("telnumber");
                        String wav = item.getString("content");
                        String logId = item.getString("logId");
                        CallUtils.makeCallFile("0", wav, dialFilepathTmp, dialFilepath, trunk, obcid, telNumber, archiveCallFile,logId, maxRetries, retryTime);
                    }

                }else{
                    log.error("请求失败");
                }
                break;
            }
        }

    }
}
