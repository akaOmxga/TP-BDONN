/* --------------------------------------------------------------------------------
 * WoE Tools
 * 
 * Ecole Centrale Nantes - Septembre 2022
 * Equipe pédagogique Informatique et Mathématiques
 * JY Martin
 * -------------------------------------------------------------------------------- */
package org.centrale.worldofecn;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.centrale.worldofecn.world.World;

/**
 *
 * @author ECN
 */
public class DatabaseTools {

    private String login;
    private String password;
    private String url;
    private Connection connection;

    /**
     * Load infos
     */
    public DatabaseTools() {
        try {
            // Get Properties file
            ResourceBundle properties = ResourceBundle.getBundle(DatabaseTools.class.getPackage().getName() + ".database");

            // USE config parameters
            login = properties.getString("login");
            password = properties.getString("password");
            String server = properties.getString("server");
            String database = properties.getString("database");
            url = "jdbc:postgresql://" + server + "/" + database;

            // Mount driver
            Driver driver = DriverManager.getDriver(url);
            if (driver == null) {
                Class.forName("org.postgresql.Driver");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.connection = null;
    }

    /**
     * Get connection to the database
     */
    public void connect() {
        if (this.connection == null) {
            try {
                this.connection = DriverManager.getConnection(url, login, password);
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Disconnect from database
     */
    public void disconnect() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * get Player ID
     * @param nomJoueur
     * @param password
     * @return
     */
    
    public Integer getPlayerID(String nomJoueur, String password) {
        if (this.connection!=null){
            try {
                String query = "SELECT IDJoueur FROM Joueur Where Login=? AND Password=?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1,nomJoueur);
                stmt.setString(2,password);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                return rs.getInt("IDJoueur");
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            return null;
    }

    /**
     * save world to database
     * @param idJoueur
     * @param idMonde
     * @param nomSauvegarde
     * @param monde
     */

    public void saveWorld(Integer idJoueur, Integer idMonde, String nomSauvegarde, World monde) {
        if (this.connection != null) {
            if (nomSauvegarde == null){
                try {
                    String query = "UPDATE Sauvegarde SET tableauMonde = ? , nbTour = ? WHERE nom = NULL AND idMonde = ? AND Monde.idJoueur = ? INNER JOIN Monde ON Sauvegarde.idMonde = Monde.idMonde";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setString(1, monde.getlistElements().toString());
                    stmt.setString(2, String.valueOf(monde.getNBTour()));
                    stmt.setString(3, String.valueOf(idMonde));
                    stmt.setString(4, String.valueOf(idJoueur));
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
                try {
                    String query = "UPDATE Sauvegarde SET tableauMonde = ? , nbTour = ?, nom = ? WHERE nom = ? AND idMonde = ? AND Monde.idJoueur = ? INNER JOIN Monde ON Sauvegarde.idMonde = Monde.idMonde";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setString(1, monde.getlistElements().toString());
                    stmt.setString(2, String.valueOf(monde.getNBTour()));
                    stmt.setString(3, nomSauvegarde);
                    stmt.setString(4, nomSauvegarde);
                    stmt.setString(5, String.valueOf(idMonde));
                    stmt.setString(6, String.valueOf(idJoueur));
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * get world from database
     * @param idJoueur
     * @param nomPartie
     * @param nomSauvegarde
     * @param monde
     */
    public void readWorld(Integer idJoueur, String nomPartie, String nomSauvegarde, World monde) {

    }
}
