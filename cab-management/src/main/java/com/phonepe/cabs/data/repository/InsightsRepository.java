package com.phonepe.cabs.data.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InsightsRepository {

    @Autowired
    NamedParameterJdbcTemplate cabManagementNamedParameterJdbcTemplate;

    public Long getIdleTimeInSeconds(String registrationNo, String startTime, String endTime){
        String getIdleTimeSql = "select sum(idleTime) total_idle_time from (\n" +
                "select registration_no, extract (epoch from  state_end_time - state_start_time) idleTime\n" +
                "from cabmanagement.cab_travel_log \n" +
                "where state_id='I' \n" +
                "and state_end_time is not null\n" +
                "and registration_no=:registrationNo\n" +
                "and state_start_time>=TO_TIMESTAMP(:startTime, 'DD-MM-YYYY HH24:MI:SS')\n" +
                "and state_end_time<=TO_TIMESTAMP(:endTime, 'DD-MM-YYYY HH24:MI:SS')\n" +
                ") as i group by registration_no";
        Map<String, String> params=new HashMap<>(3);
        params.put("registrationNo", registrationNo);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        Long idleTimeInSecs = cabManagementNamedParameterJdbcTemplate.query(getIdleTimeSql, params, rs -> {
            if(!rs.isBeforeFirst() || !rs.next()){
                return 0L;
            }
            return rs.getLong("total_idle_time");
        });
        return idleTimeInSecs;
    }
}
