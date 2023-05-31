package org.example;

public class AccommodationRoomFairRelation {

    int id;
    int accommodationId;
    int roomFairId;

    public AccommodationRoomFairRelation(int id, int accommodationId, int roomFairId) {
        this.id = id;
        this.accommodationId = accommodationId;
        this.roomFairId = roomFairId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(int accommodationId) {
        this.accommodationId = accommodationId;
    }

    public int getRoomFairId() {
        return roomFairId;
    }

    public void setRoomFairId(int roomFairId) {
        this.roomFairId = roomFairId;
    }
}
