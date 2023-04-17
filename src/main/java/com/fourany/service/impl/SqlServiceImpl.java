package com.fourany.service.impl;


import com.fourany.mapper.SqlMapper;
import com.fourany.service.ISqlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @author: zhangjh
 * @date: 2023年04月14日 10:52
 */
@Slf4j
@Service
public class SqlServiceImpl implements ISqlService {
    @Resource
    private SqlMapper sqlMapper;

    @Value("${monitor.publishlog.sql}")
    private String sqlPublishLog;

    @Value("${monitor.flowchart.sql}")
    private String sqlFlowChart;

    @Value("${monitor.formoney.sql}")
    private String sqlForMoney;

    @Override
    public List<Map<String, Object>> queryPublishLog() {
        return sqlMapper.queryPublishLog(sqlPublishLog);
    }

    @Override
    public List<Map<String, Object>> queryFlowChart() {
        return sqlMapper.queryFlowChart(sqlFlowChart);
    }

    @Override
    public List<Map<String, Object>> queryForMoney() {
        return sqlMapper.queryForMoney(sqlForMoney);
    }

}
