package com.db;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.Record;
import java.sql.Connection;
import java.sql.SQLException;
import static com.generated_jooq.tables.OdUser.OD_USER;

public class SecretKeyValidator {

    public static boolean validateSecretKey(String secretKey, Connection connection) throws SQLException {
        DSLContext dslContext = DSL.using(connection);

        // Check if the secretKey exists in the database
        Record record = dslContext.select()
                                  .from(OD_USER)
                                  .where(OD_USER.SECRET_KEY.eq(secretKey))
                                  .fetchOne();

        if (record != null) {
            // Decrement remaining_requests by 1
            dslContext.update(OD_USER)
                      .set(OD_USER.REMAINING_REQUESTS, OD_USER.REMAINING_REQUESTS.minus(1))
                      .where(OD_USER.SECRET_KEY.eq(secretKey))
                      .execute();
            return true;
        }

        return false;
    }
}