package com.phonepe.cabs.data.repository;

import com.phonepe.cabs.data.dao.Booking;
import com.phonepe.cabs.data.dao.Cab;
import com.phonepe.cabs.data.dao.TravelLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class CabRepository {

    @Autowired
    NamedParameterJdbcTemplate cabManagementNamedParameterJdbcTemplate;

    @Transactional
    public void registerCab(Cab cab){
        String registerCabSql = "insert into cabs(registration_no, registration_year, city_id) values(:registrationNo, :registrationYear, :cityId)";
        Map<String, Object> params = new HashMap<>(3);
        params.put("registrationNo", cab.getRegistrationNo());
        params.put("registrationYear", cab.getRegistrationYear());
        params.put("cityId", cab.getCityId());
        cabManagementNamedParameterJdbcTemplate.update(registerCabSql, params);

        String defineCabState = "insert into cab_travel_log(registration_no, state_id, state_start_time) values(:registrationNo, 'I', current_timestamp)";
        params = new HashMap<>(1);
        params.put("registrationNo", cab.getRegistrationNo());
        cabManagementNamedParameterJdbcTemplate.update(defineCabState, params);
    }

    public void updateCabCity(String registrationNo, Integer cityId){
        String updateCabCitySql = "update cabs set city_id=:cityId where registration_no=:registrationNo";
        Map<String, Object> params = new HashMap<>(2);
        params.put("registrationNo", registrationNo);
        params.put("cityId", cityId);
        cabManagementNamedParameterJdbcTemplate.update(updateCabCitySql, params);
    }

    @Transactional
    public void updateCabState(String registrationNo, String updateToStateId){

        String currentStateId = getCurrentCabState(registrationNo);

        if(currentStateId.equals(updateToStateId)) return;

        String updateEndCabStateSql="";
        String insertStartCabStateSql="";
        switch (updateToStateId){
            case "I":
                updateEndCabStateSql = "update cab_travel_log set state_end_time=current_timestamp where registration_no=:registrationNo and state_end_time is null and state_id='R' ";
                insertStartCabStateSql = "insert into cab_travel_log(registration_no, state_id, state_start_time) values(:registrationNo, 'I', current_timestamp)";
                break;
            case "R":
                updateEndCabStateSql = "update cab_travel_log set state_end_time=current_timestamp where registration_no=:registrationNo and state_end_time is null and state_id='I'";
                insertStartCabStateSql = "insert into cab_travel_log(registration_no, state_id, state_start_time) values(:registrationNo, 'R', current_timestamp)";
                break;
            default:
                throw new RuntimeException("Unrecognised State Id: " + updateToStateId);
        }

        Map<String, Object> params = new HashMap<>(1);
        params.put("registrationNo", registrationNo);
        int rowsUpdated = cabManagementNamedParameterJdbcTemplate.update(updateEndCabStateSql, params);
        if(rowsUpdated!=1) throw new RuntimeException("RegistrationNo: " + registrationNo + " has invalid state.");
        cabManagementNamedParameterJdbcTemplate.update(insertStartCabStateSql, params);
    }

    public String getCurrentCabState(String registrationNo){

        String getCurrentCabStateSql = "select state_id from cab_travel_log where registration_no=:registrationNo and state_end_time is null";

        Map<String, Object> params = new HashMap<>(1);
        params.put("registrationNo", registrationNo);
        String currentStateId = cabManagementNamedParameterJdbcTemplate.query(getCurrentCabStateSql, params, rs -> {
            if(!rs.isBeforeFirst() || !rs.next()){
                throw new RuntimeException("RegistrationNo: " + registrationNo + " has no initial state defined.");
            }
            return rs.getString("state_id");
        });

        return currentStateId;
    }

    @Transactional
    public Booking bookCab(Integer cityId){
        String bookingCabRegNo = getMostIdleCab(cityId);
        createBooking(bookingCabRegNo, cityId);
        updateCabState(bookingCabRegNo, "R");
        return getCurrentBooking(bookingCabRegNo, cityId);
    }

    public String getMostIdleCab(Integer cityId){
        String getMostIdleCabInCitySql = "select t.registration_no from cab_travel_log t " +
                "inner join cabs c on t.registration_no=c.registration_no " +
                "where " +
                "state_end_time is null " +
                "and state_id='I' " +
                "and city_id=:cityId " +
                "order by state_start_time " +
                "fetch first 1 rows only";
        Map<String, Object> params = new HashMap<>(2);
        params.put("cityId", cityId);
        String availableCab = cabManagementNamedParameterJdbcTemplate.query(getMostIdleCabInCitySql, params, rs -> {
            if(!rs.isBeforeFirst() || !rs.next()){
                throw new RuntimeException("No cabs available.");
            }
            return rs.getString("registration_no");
        });
        return availableCab;
    }

    public void createBooking(String registrationNo, Integer cityId){
        String createBookingSql = "insert into bookings(registration_no, city_id) values(:registrationNo, :cityId)";
        Map<String, Object> params = new HashMap<>(2);
        params.put("cityId", cityId);
        params.put("registrationNo", registrationNo);
        cabManagementNamedParameterJdbcTemplate.update(createBookingSql, params);
    }

    public Booking getCurrentBooking(String registrationNo, Integer cityId){
        String getCurrentBookingSql = "select * from bookings where booking_status_id='A' and registration_no=:registrationNo and city_id=:cityId";

        Map<String, Object> params = new HashMap<>(2);
        params.put("cityId", cityId);
        params.put("registrationNo", registrationNo);
        Booking booking = cabManagementNamedParameterJdbcTemplate.queryForObject(getCurrentBookingSql, params, (rs, rowNum) -> {
           Booking bookingData = new Booking() ;
           bookingData.setBookingId(rs.getLong("booking_id"));
           bookingData.setCityId(rs.getInt("city_id"));
           bookingData.setRegistrationNo(rs.getString("registration_no"));
           bookingData.setBookingStatus(rs.getString("booking_status_id"));
           return bookingData;
        });
        return booking;
    }

    public List<TravelLog> getCabTravelHistory(String registrationNo){
        String getCabTravelHistorySql = "select * from cab_travel_log where registration_no=:registrationNo";
        Map<String, String> params = new HashMap<>(1);
        params.put("registrationNo", registrationNo);
        List<TravelLog> travelLogs = cabManagementNamedParameterJdbcTemplate.query(getCabTravelHistorySql, params, (rs, rowNum) -> {
            TravelLog travelLog=new TravelLog();
            travelLog.setRegistrationNo(rs.getString("registration_no"));
            travelLog.setStateStartTime(rs.getString("state_start_time"));
            travelLog.setStateEndTime(rs.getString("state_end_time"));
            travelLog.setStateId(rs.getString("state_id"));
            return travelLog;
        });
        return travelLogs;
    }
}
