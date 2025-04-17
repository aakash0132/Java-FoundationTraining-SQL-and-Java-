package com.petpals.entity;

import com.petpals.dao.IAdoptable;

import java.util.ArrayList;
import java.util.List;

public class AdoptionEvent {
    private List<IAdoptable> participants;

    public AdoptionEvent() {
        this.participants = new ArrayList<>();
    }

    public void hostEvent() {
        System.out.println("Hosting adoption event with " + participants.size() + " participants.");
        for (IAdoptable participant : participants) {
            participant.adopt();
        }
    }

    public void registerParticipant(IAdoptable participant) {
        participants.add(participant);
        System.out.println("Participant registered for adoption event.");
    }
}