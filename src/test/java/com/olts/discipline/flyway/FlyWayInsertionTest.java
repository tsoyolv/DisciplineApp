package com.olts.discipline.flyway;

import org.junit.Test;

/**
 * OLTS on 29.04.2017.
 */
public class FlyWayInsertionTest {

    @Test
    public void testDefaultUser() {
        String selectQuery = "SELECT * FROM `disciplinedb`.`user`";
        //AbstractJdbcCall jdbcCall = StaticAppContext.getBean("jdbcCall");
       //List<Map<String, Object>> queryForList = jdbcCall.getJdbcTemplate().queryForList(selectQuery);
       // Map<String, Object> obj = queryForList.iterator().next();
       // Assert.assertEquals(obj.get("login"), "olts");
    }
}
