package com.petpals.entity;

import com.petpals.dao.IAdoptable;

import java.util.ArrayList;
import java.util.List;

public class PetShelter implements IAdoptable {
    private List<Pet> availablePets;

    public PetShelter() {
        this.availablePets = new ArrayList<>();
    }

    public void addPet(Pet pet) {
        availablePets.add(pet);
    }

    public void removePet(Pet pet) {
        availablePets.remove(pet);
    }

    public List<Pet> listAvailablePets() {
        return new ArrayList<>(availablePets);
    }

    @Override
    public void adopt() {
        System.out.println("Adoption process initiated for shelter pets.");
    }
}