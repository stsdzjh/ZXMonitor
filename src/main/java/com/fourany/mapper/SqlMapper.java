package com.fourany.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @author: zhangjh
 * @date: 2023年04月14日 10:48
 */
@Mapper
public interface SqlMapper extends BaseMapper {

    @Select(value = "${sqlStr}")
    List<Map<String, Object>> queryPublishLog(@Param(value = "sqlStr") String sqlStr);

    @Select(value = "${sqlStr}")
    List<Map<String, Object>> queryFlowChart(@Param(value = "sqlStr") String sqlStr);

    @Select(value = "${sqlStr}")
    List<Map<String, Object>> queryForMoney(@Param(value = "sqlStr") String sqlStr);
}
