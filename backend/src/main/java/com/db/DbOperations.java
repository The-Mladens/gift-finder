package com.db;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import java.sql.Connection;
import java.sql.SQLException;

import com.generated_jooq.Tables;

public class DbOperations {

    public static void insertUser(String name, String email, String secretKey, int remainingRequests) {
        try (Connection conn = PgDataSource.getDataSource().getConnection()) {
            DSLContext dsl = DSL.using(conn);
            var OD_USER = Tables.OD_USER;
            dsl.insertInto(OD_USER)
                .set(OD_USER.NAME, name)
                .set(OD_USER.EMAIL, email)
                .set(OD_USER.SECRET_KEY, secretKey)
                .set(OD_USER.REMAINING_REQUESTS, remainingRequests)
                .execute();
        } catch (SQLException e) {
            throw new RuntimeException("Database operation error: " + e.getMessage(), e);
        }
    }

}