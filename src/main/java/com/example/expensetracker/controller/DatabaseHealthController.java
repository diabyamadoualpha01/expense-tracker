package com.example.expensetracker.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseHealthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/api/health/db")
    public String checkDbConnection() {
        try {
            jdbcTemplate.execute("SELECT 1");
            return "Database connection: UP";
        } catch (Exception e) {
            return "Database connection: DOWN - " + e.getMessage();
        }
    }
}