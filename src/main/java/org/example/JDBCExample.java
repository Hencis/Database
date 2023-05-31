package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCExample {

    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/mydb";
    private static final String USER = "postgres";
    private static final String PASS = "root";

    private static Connection connection = null;


    static {
        System.out.println("Initializing the Connection object...");
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            System.out.println("Error in the Connection object initialization... " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        insertData();
        printAllAccommodations();
        closeConnection();
    }

    //closing connection
    public static void closeConnection() {
        System.out.println("Closing the DataBase connection...");
        try {
            if (connection != null) connection.close();
        } catch (SQLException se) {
            System.out.println("Error in closing the Connection object... " + se.getMessage());
            se.printStackTrace();
        }
    }

    //This method returns the accommodation table's content
    public static List<Accommodation> selectAccommodations() {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String sql = "SELECT id, type, bed_type, max_guests, description FROM accommodation;";

            ResultSet resultSet = stmt.executeQuery(sql);

            List<Accommodation> accommodations = new ArrayList<>();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String type = resultSet.getString("type");
                String bed_type = resultSet.getString("bed_type");
                Integer max_guests = resultSet.getInt("max_guests");
                String description = resultSet.getString("description");
                Accommodation accommodation = new Accommodation(id, type, bed_type, max_guests, description);
                accommodations.add(accommodation);
            }

            resultSet.close();
            stmt.close();
            return accommodations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
    }

    //This method returns the Room Fair table's content
    public static List<RoomFair> selectRoomFair() {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String sql = "SELECT id, value, season FROM room_fair;";

            ResultSet resultSet = stmt.executeQuery(sql);

            List<RoomFair> roomFair = new ArrayList<>();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Double value = resultSet.getDouble("value");
                String season = resultSet.getString("season");
                RoomFair roomFair1 = new RoomFair(id, value, season);
                roomFair.add(roomFair1);
            }

            resultSet.close();
            stmt.close();
            return roomFair;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
    }

    //This method returns the relation table's content
    public static List<AccommodationRoomFairRelation> selectAccommodationRoomFairRelation() {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            String sql = "SELECT id, accommodation_id, room_fair_id FROM accommodation_room_fair_relation;";

            ResultSet resultSet = stmt.executeQuery(sql);

            List<AccommodationRoomFairRelation> accommodationRoomFairRelations = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int accommodationId = resultSet.getInt("accommodation_id");
                int roomFairId = resultSet.getInt("room_fair_id");
                AccommodationRoomFairRelation accommodationRoomFairRelation = new AccommodationRoomFairRelation(id, accommodationId, roomFairId);
                accommodationRoomFairRelations.add(accommodationRoomFairRelation);
            }

            resultSet.close();
            stmt.close();
            return accommodationRoomFairRelations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
    }

    public static void printAllAccommodations() {
        System.out.println("Writing out the context of the DataBase...");
        List<Accommodation> accommodations = selectAccommodations();
        for (Accommodation accommodation : accommodations) {
            System.out.println("ID: " + accommodation.getId() + ", Type: " + accommodation.getType() + ", BedType: " + accommodation.getBedType() + ", Max Guests: " + accommodation.getMaxGuests() + ", Description: " + accommodation.getDescription());
        }
    }

    static void insertData() throws SQLException {
        int accommodationId = insertAccommodation("Hotel", "Queen", 2, "Standard Room");
        int roomFairId = insertRoomFair(100.00, "High Season");
        linkAccommodationWithRoomFair(accommodationId, roomFairId);
    }

    //This method inserts into the accommodation table and returns the last id added to the table
    public static int insertAccommodation(String type, String bedType, int maxGuests, String description) {
        String sql = "INSERT INTO accommodation (type, bed_type, max_guests, description) " + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, type);
            statement.setString(2, bedType);
            statement.setInt(3, maxGuests);
            statement.setString(4, description);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            return Integer.parseInt(resultSet.getString(1));
        } catch (SQLException e) {
            System.out.println("Error while inserting new accommodation into the DataBase... " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //This method inserts into the room fair table and returns the last id added to the table
    public static int insertRoomFair(double value, String season) {
        String sql = "INSERT INTO room_fair (value, season) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setDouble(1, value);
            statement.setString(2, season);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            return Integer.parseInt(resultSet.getString(1));
        } catch (SQLException e) {
            System.out.println("Error while inserting new room fair into the DataBase... " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //This method inserts into the relation table based on the accommodation and room fair table's last id
    public static void linkAccommodationWithRoomFair(int accommodationId, int roomFairId) {

        String insertQuery = "INSERT INTO accommodation_room_fair_relation (room_fair_id, accommodation_id) VALUES (?, ?)";

        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setInt(1, roomFairId);
            insertStatement.setInt(2, accommodationId);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while inserting new relation record into the DataBase... " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /*Here I created two separate sql queries, because I couldn't figure out which one is needed. The first one prints all the prices, while
    the second one calculates a summary based on the common accommodation and room id's. */
    public static void printRoomPrices() {
        String sql = "SELECT accommodation.type, accommodation.bed_type, room_fair.value " + "FROM accommodation " +
                "JOIN accommodation_room_fair_relation ON accommodation.id = accommodation_room_fair_relation.accommodation_id " +
                "JOIN room_fair ON accommodation_room_fair_relation.room_fair_id = room_fair.id";

        String sql2 = "SELECT accommodation.id AS accommodation_id, room_fair.id AS room_id, SUM(room_fair.value) AS room_fair_total_value\n" +
                "FROM accommodation_room_fair_relation\n" +
                "JOIN accommodation ON accommodation_room_fair_relation.accommodation_id = accommodation.id\n" +
                "JOIN room_fair ON accommodation_room_fair_relation.room_fair_id = room_fair.id\n" +
                "GROUP BY (accommodation.id, room_fair.id)";

        try (PreparedStatement statement = connection.prepareStatement(sql2)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String accommodationId = resultSet.getString("accommodation_id");
                String roomFairId = resultSet.getString("room_id");
                double roomFairValue = resultSet.getDouble("room_fair_total_value");

                System.out.println("Accommodation Id: " + accommodationId + ", Room Fair Id: " + roomFairId + ", Room Fair Value: " + roomFairValue);
            }
        } catch (SQLException e) {
            System.out.println("Error while executing query with join on Room table to obtain the prices per rooms... " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}


