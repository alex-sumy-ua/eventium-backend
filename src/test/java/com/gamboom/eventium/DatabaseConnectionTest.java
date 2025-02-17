package com.gamboom.eventium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("For testing Neon DB Connection")
    void testDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
            System.out.println("Connection successful: " + connection.getCatalog());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed.");
        }
    }

}
