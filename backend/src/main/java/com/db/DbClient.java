package com.db;

import java.sql.*;

public class DbClient {
    public static void insertUser(String name, String email) throws SQLException {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "CALL ad_user_insert(?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.execute();
            }
        }
    }

    public static void insertChatCompletion(String request, String response) throws SQLException {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "CALL ch_completion_insert(?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, request);
                stmt.setString(2, response);
                stmt.execute();
            }
        }
    }
}
