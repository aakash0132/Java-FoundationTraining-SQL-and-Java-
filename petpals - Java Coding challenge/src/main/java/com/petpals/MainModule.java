package com.petpals;

import com.petpals.dao.AdoptionEventDAO;
import com.petpals.dao.DonationDAO;
import com.petpals.dao.PetDAO;
import com.petpals.entity.*;
import com.petpals.exception.AdoptionException;
import com.petpals.exception.InsufficientFundsException;
import com.petpals.exception.InvalidPetAgeException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MainModule {
    private static final Scanner scanner = new Scanner(System.in);
    private static final PetDAO petDAO = new PetDAO();
    private static final DonationDAO donationDAO = new DonationDAO();
    private static final AdoptionEventDAO eventDAO = new AdoptionEventDAO();
    private static final PetShelter shelter = new PetShelter();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nPetPals Menu:");
            System.out.println("1. Add Pet");
            System.out.println("2. List Available Pets");
            System.out.println("3. Record Cash Donation");
            System.out.println("4. Record Item Donation");
            System.out.println("5. Host Adoption Event");
            System.out.println("6. Register for Adoption Event");
            System.out.println("7. Read Pets from File");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1 -> addPet();
                    case 2 -> listPets();
                    case 3 -> recordCashDonation();
                    case 4 -> recordItemDonation();
                    case 5 -> hostAdoptionEvent();
                    case 6 -> registerForEvent();
                    case 7 -> readPetsFromFile();
                    case 8 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void addPet() throws InvalidPetAgeException, SQLException, AdoptionException {
        System.out.print("Enter pet type (Dog/Cat): ");
        String type = scanner.nextLine();
        System.out.print("Enter pet name: ");
        String name = scanner.nextLine();
        System.out.print("Enter pet age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter pet breed: ");
        String breed = scanner.nextLine();

        if (age <= 0) {
            throw new InvalidPetAgeException("Pet age must be positive.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new AdoptionException("Pet name cannot be null or empty.");
        }

        Pet pet;
        if ("Dog".equalsIgnoreCase(type)) {
            System.out.print("Enter dog breed: ");
            String dogBreed = scanner.nextLine();
            pet = new Dog(name, age, breed, dogBreed);
        } else if ("Cat".equalsIgnoreCase(type)) {
            System.out.print("Enter cat color: ");
            String catColor = scanner.nextLine();
            pet = new Cat(name, age, breed, catColor);
        } else {
            throw new AdoptionException("Invalid pet type.");
        }

        petDAO.addPet(pet);
        shelter.addPet(pet);
        System.out.println("Pet added successfully.");
    }

    private static void listPets() throws SQLException {
        List<Pet> pets = petDAO.getAllPets();
        if (pets.isEmpty()) {
            System.out.println("No pets available.");
        } else {
            for (Pet pet : pets) {
                try {
                    System.out.println(pet.toString());
                } catch (NullPointerException e) {
                    System.out.println("Error: Pet information is missing.");
                }
            }
        }
    }

    private static void recordCashDonation() throws InsufficientFundsException, SQLException {
        System.out.print("Enter donor name: ");
        String donorName = scanner.nextLine();
        System.out.print("Enter donation amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount < 10) {
            throw new InsufficientFundsException("Donation amount must be at least $10.");
        }

        CashDonation donation = new CashDonation(donorName, amount, LocalDate.now());
        donationDAO.recordDonation(donation);
        donation.recordDonation();
    }

    private static void recordItemDonation() throws SQLException {
        System.out.print("Enter donor name: ");
        String donorName = scanner.nextLine();
        System.out.print("Enter donation value: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter item type (e.g., food, toys): ");
        String itemType = scanner.nextLine();

        ItemDonation donation = new ItemDonation(donorName, amount, itemType);
        donationDAO.recordDonation(donation);
        donation.recordDonation();
    }

    private static void hostAdoptionEvent() {
        AdoptionEvent event = new AdoptionEvent();
        event.registerParticipant(shelter);
        event.hostEvent();
    }

    private static void registerForEvent() throws SQLException {
        System.out.print("Enter event name: ");
        String eventName = scanner.nextLine();
        System.out.print("Enter event date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        System.out.print("Enter participant name: ");
        String participantName = scanner.nextLine();

        eventDAO.addEvent(eventName, java.sql.Date.valueOf(dateStr));
        eventDAO.registerParticipant(1, participantName); // Assuming event ID 1 for simplicity
        System.out.println("Registered for event successfully.");
    }

    private static void readPetsFromFile() {
        System.out.print("Enter file path: ");
        String filePath = scanner.nextLine();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Read pet: " + line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}