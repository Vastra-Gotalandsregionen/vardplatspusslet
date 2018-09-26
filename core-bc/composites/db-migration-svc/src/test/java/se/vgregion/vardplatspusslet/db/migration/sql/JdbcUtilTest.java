package se.vgregion.vardplatspusslet.db.migration.sql;

import org.junit.Test;

import static org.junit.Assert.*;

public class JdbcUtilTest {

  @Test
  public void toCamelCase() {
    String r = JdbcUtil.toCamelCase("TABLE_CAT");
    assertEquals("tableCat", r);
  }

}
