package com.petpals.dao;

import com.petpals.util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdoptionEventDAO {
    public void addEvent(String eventName, java.sql.Date eventDate) throws SQLException {
        String sql = "INSERT INTO adoption_events (event_name, event_date) VALUES (?, ?)";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventName);
            stmt.setDate(2, eventDate);
            stmt.executeUpdate();
        }
    }

    public List<String> getAllEvents() throws SQLException {
        List<String> events = new ArrayList<>();
        String sql = "SELECT event_name, event_date FROM adoption_events";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                events.add(rs.getString("event_name") + " on " + rs.getDate("event_date"));
            }
        }
        return events;
    }

    public void registerParticipant(int eventId, String participantName) throws SQLException {
        String sql = "INSERT INTO participants (event_id, participant_name) VALUES (?, ?)";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            stmt.setString(2, participantName);
            stmt.executeUpdate();
        }
    }
}