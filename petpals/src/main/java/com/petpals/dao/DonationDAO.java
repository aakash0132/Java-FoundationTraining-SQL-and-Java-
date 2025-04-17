package com.petpals.dao;

import com.petpals.entity.CashDonation;
import com.petpals.entity.Donation;
import com.petpals.entity.ItemDonation;
import com.petpals.util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DonationDAO {
    public void recordDonation(Donation donation) throws SQLException {
        String sql = "INSERT INTO donations (donor_name, amount, donation_date, item_type, donation_type) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, donation.getDonorName());
            stmt.setDouble(2, donation.getAmount());
            if (donation instanceof CashDonation) {
                stmt.setDate(3, java.sql.Date.valueOf(((CashDonation) donation).getDonationDate()));
                stmt.setString(4, null);
                stmt.setString(5, "Cash");
            } else if (donation instanceof ItemDonation) {
                stmt.setDate(3, null);
                stmt.setString(4, ((ItemDonation) donation).getItemType());
                stmt.setString(5, "Item");
            }
            stmt.executeUpdate();
        }
    }
}