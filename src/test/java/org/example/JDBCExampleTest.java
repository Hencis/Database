package org.example;

import org.junit.AfterClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class JDBCExampleTest {

    @AfterClass
    public static void closeDbConnection() {
        JDBCExample.closeConnection();
    }

    @Test
    public void testInsertAccommodation() {
        List<Accommodation> accommodationList = JDBCExample.selectAccommodations();
        int idBeforeInsert = accommodationList.get(accommodationList.size() - 1).getId();

        JDBCExample.insertAccommodation("Hotel", "Queen Bed", 2, "Description");
        accommodationList = JDBCExample.selectAccommodations();
        int idAfterInsert = accommodationList.get(accommodationList.size() - 1).getId();
        assertEquals(idBeforeInsert + 1, idAfterInsert);
        assertEquals(String.valueOf(idAfterInsert), String.valueOf(accommodationList.get(accommodationList.size() - 1).getId()));
        assertEquals("Hotel", accommodationList.get(accommodationList.size() - 1).getType());
        assertEquals("Queen Bed", accommodationList.get(accommodationList.size() - 1).getBedType());
        assertEquals("2", String.valueOf(accommodationList.get(accommodationList.size() - 1).getMaxGuests()));
        assertEquals("Description", accommodationList.get(accommodationList.size() - 1).getDescription());
    }

    @Test
    public void testInsertRoomFair() {
        List<RoomFair> roomFairList = JDBCExample.selectRoomFair();
        int idBeforeInsert = roomFairList.get(roomFairList.size() - 1).getId();

        JDBCExample.insertRoomFair(100.000, "Summer");
        roomFairList = JDBCExample.selectRoomFair();
        int idAfterInsert = roomFairList.get(roomFairList.size() - 1).getId();
        assertEquals(idBeforeInsert + 1, idAfterInsert);
        assertEquals(String.valueOf(idAfterInsert), String.valueOf(roomFairList.get(roomFairList.size() - 1).getId()));
        assertEquals("Summer", roomFairList.get(roomFairList.size() - 1).getSeason());
        assertEquals(100.000, roomFairList.get(roomFairList.size() - 1).getValue(), 1e-15);
    }

    @Test
    public void testInsertAccommodationRoomFairRelation() {
        List<AccommodationRoomFairRelation> accommodationRoomFairRelationList = JDBCExample.selectAccommodationRoomFairRelation();
        int idBeforeInsert = accommodationRoomFairRelationList.get(accommodationRoomFairRelationList.size() - 1).getId();
        int accommodationId = JDBCExample.insertAccommodation("Hotel", "Queen Bed", 2, "Description");
        int roomFairId = JDBCExample.insertRoomFair(100.000, "Summer");
        JDBCExample.linkAccommodationWithRoomFair(accommodationId, roomFairId);
        accommodationRoomFairRelationList = JDBCExample.selectAccommodationRoomFairRelation();
        int idAfterInsert = accommodationRoomFairRelationList.get(accommodationRoomFairRelationList.size() - 1).getId();
        assertEquals(idBeforeInsert + 1, idAfterInsert);
    }

    @Test
    public void testPricePerRoom() {
        JDBCExample.printRoomPrices();
    }
}