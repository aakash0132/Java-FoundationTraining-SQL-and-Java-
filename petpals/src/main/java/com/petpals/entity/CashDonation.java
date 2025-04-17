package com.petpals.entity;

import java.time.LocalDate;

public class CashDonation extends Donation {
    private LocalDate donationDate;

    public CashDonation(String donorName, double amount, LocalDate donationDate) {
        super(donorName, amount);
        this.donationDate = donationDate;
    }

    public LocalDate getDonationDate() { return donationDate; }

    @Override
    public void recordDonation() {
        System.out.println("Cash donation recorded: Donor=" + getDonorName() + ", Amount=" + getAmount() + ", Date=" + donationDate);
    }
}