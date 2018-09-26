package se.vgregion.vardplatspusslet.db.migration.sql;

import java.util.List;

/**
 * Created by clalu4 on 2016-12-16.
 */
public interface ProducesSql {

    void toSql(StringBuilder sb, List values);

}
