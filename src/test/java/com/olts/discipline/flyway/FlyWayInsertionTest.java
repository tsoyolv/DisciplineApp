package com.olts.discipline.flyway;

import com.olts.discipline.StaticAppContext;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.simple.AbstractJdbcCall;

import java.util.List;
import java.util.Map;

/**
 * OLTS on 29.04.2017.
 */
public class FlyWayInsertionTest {

    @Test
    public void multiplicationOfZeroIntegersShouldReturnZero() {
        String selectQuery = "SELECT * FROM `disciplinedb`.`user`";
        AbstractJdbcCall jdbcCall = StaticAppContext.<AbstractJdbcCall>getBean("jdbcCall");
        List<Map<String, Object>> queryForList = jdbcCall.getJdbcTemplate().queryForList(selectQuery);
        Map<String, Object> obj = queryForList.iterator().next();
        Assert.assertEquals(obj.get("login"), "olts");
    }
}
