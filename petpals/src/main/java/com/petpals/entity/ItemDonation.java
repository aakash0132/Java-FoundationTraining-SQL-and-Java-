package com.petpals.entity;

public class ItemDonation extends Donation {
    private String itemType;

    public ItemDonation(String donorName, double amount, String itemType) {
        super(donorName, amount);
        this.itemType = itemType;
    }

    @Override
    public void recordDonation() {
        System.out.println("Item donation recorded: Donor=" + getDonorName() + ", Amount=" + getAmount() + ", ItemType=" + itemType);
    }
}