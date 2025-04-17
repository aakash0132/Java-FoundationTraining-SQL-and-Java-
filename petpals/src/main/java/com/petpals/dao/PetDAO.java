package com.petpals.dao;

import com.petpals.entity.Cat;
import com.petpals.entity.Dog;
import com.petpals.entity.Pet;
import com.petpals.util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PetDAO {
    public void addPet(Pet pet) throws SQLException {
        String sql = "INSERT INTO pets (name, age, breed, type, dog_breed, cat_color) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pet.getName());
            stmt.setInt(2, pet.getAge());
            stmt.setString(3, pet.getBreed());
            if (pet instanceof Dog) {
                stmt.setString(4, "Dog");
                stmt.setString(5, ((Dog) pet).getDogBreed());
                stmt.setString(6, null);
            } else if (pet instanceof Cat) {
                stmt.setString(4, "Cat");
                stmt.setString(5, null);
                stmt.setString(6, ((Cat) pet).getCatColor());
            }
            stmt.executeUpdate();
        }
    }

    public List<Pet> getAllPets() throws SQLException {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pets";
        try (Connection conn = DBConnUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Pet pet;
                String type = rs.getString("type");
                if ("Dog".equals(type)) {
                    pet = new Dog(rs.getString("name"), rs.getInt("age"), rs.getString("breed"), rs.getString("dog_breed"));
                } else {
                    pet = new Cat(rs.getString("name"), rs.getInt("age"), rs.getString("breed"), rs.getString("cat_color"));
                }
                pets.add(pet);
            }
        }
        return pets;
    }
}