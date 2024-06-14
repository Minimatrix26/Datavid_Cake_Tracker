package org.example.datavid_cake_tracker.Repo;

import org.example.datavid_cake_tracker.Model.Member;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import org.sqlite.SQLiteDataSource;
public class Repo {
    private String JDBC_URL = "jdbc:sqlite:members.db";

    private Connection connection;

    public Repo() throws SQLException {
        openConnection();
        createTable();
    }

    private void openConnection() {
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl(JDBC_URL);

        try {
            if (connection == null || connection.isClosed()) {
                connection = ds.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if(connection != null){
            try{
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void createTable() throws SQLException {
        try (final Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS members (id int, firstName varchar(30), lastName varchar(30), birthDate varchar(15), country varchar(30), city varchar(30));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Member> getMembers() {
        ArrayList<Member> members = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM members;")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String birthDate = rs.getString("birthDate");
                String country = rs.getString("country");
                String city = rs.getString("city");

                 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                 LocalDate parsedBirthDate = LocalDate.parse(birthDate, formatter);
                 Member member = new Member(id, firstName, lastName, parsedBirthDate, country, city);

                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

    public void addMember(Member member) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO members VALUES (?, ?, ?, ?, ?, ?);")) {
            stmt.setInt(1, getMembers().size() + 1);
            stmt.setString(2, member.getFirstName());
            stmt.setString(3, member.getLastName());
            stmt.setString(4, member.getBirthDate().toString());
            stmt.setString(5, member.getCountry());
            stmt.setString(6, member.getCity());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMember(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM members WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) throws SQLException {
//        Repo repo = new Repo();
//
//        //Member member2 = new Member(2, "Jake", "Clarke", LocalDate.of(2004, 1, 10), "USA", "Boston");
//        //repo.addMember(member2);
//        //repo.deleteMember(2);
//
//        ArrayList<Member> members = repo.getMembers();
//        for (var member : members) {
//            System.out.println(member);
//        }
//    }

}
