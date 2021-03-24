package com.codecool.database;


import java.sql.*;

public class RadioCharts {

    private final String url;
    private final String user;
    private final String password;

    public RadioCharts(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public String getMostPlayedSong() {
        String name = null;
        String query = "SELECT song FROM music_broadcast WHERE times_aired = (SELECT MAX(times_aired) FROM music_broadcast)";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) name = resultSet.getString("song");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (name == null) return "";
        return name;
    }

    public String getMostActiveArtist() {
        String name = null;
        String query = "SELECT TOP 1 artist FROM music_broadcast GROUP BY artist ORDER BY COUNT(song) DESC";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) name = resultSet.getString("artist");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (name == null) return "";
        return name;
    }

    private Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.err.println("Database unable to connect.");
        }
        return connection;
    }

}
