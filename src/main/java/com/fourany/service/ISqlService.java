package com.fourany.service;

import java.util.List;
import java.util.Map;

public interface ISqlService {
    List<Map<String, Object>> queryPublishLog();
    List<Map<String, Object>> queryFlowChart();
    List<Map<String, Object>> queryForMoney();
}
