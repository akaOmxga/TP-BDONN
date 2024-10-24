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
import org.centrale.worldofecn.world.Creature;
import org.centrale.worldofecn.world.ElementDeJeu;
import org.centrale.worldofecn.world.Guerrier;


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
                boolean exist = rs.next();
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

    public void saveWorld(Integer idJoueur, String nomSauvegarde,int idMonde, World mondee) {
        if (this.connection != null) {
            if (nomSauvegarde == null){
                try {
                    //mondee.saveToDatabase(connection, idMonde, idJoueur);
                    String query = "UPDATE Sauvegarde SET nbTour = ? WHERE nom = NULL AND idMonde = ? AND Monde.idJoueur = ? INNER JOIN Monde ON Sauvegarde.idMonde = Monde.idMonde RETURNING idSauvegarde";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setInt(1, mondee.getNBTour());
                    stmt.setInt(2, idMonde);
                    stmt.setInt(3, idJoueur);
                    ResultSet id = stmt.executeQuery() ;
                    id.next();
                    int id2 = id.getInt("idSauvegarde");
                    for (ElementDeJeu e: mondee.getlistElements()){
                        e.saveToDatabase(connection,id2);
                    } 
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
                try {
                    //mondee.saveToDatabase(connection, idMonde, idJoueur);
                    String query = "INSERT INTO Sauvegarde (idMonde,nbtour,nom) VALUES ( ?, ?, ? ) RETURNING idSauvegarde";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setInt(1, idMonde);
                    stmt.setInt(2, mondee.getNBTour());
                    stmt.setString(3, nomSauvegarde);
                    ResultSet id = stmt.executeQuery() ;
                    id.next();
                    int id2 = id.getInt("idSauvegarde");
                    for (ElementDeJeu e: mondee.getlistElements()){
                        e.saveToDatabase(connection,id2);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * read database to world
     * @param idJoueur
     * @param idMonde
     * @param nomSauvegarde
     * @param monde
     */
    /*
    public void readWorld(Integer idJoueur, Integer idMonde, String nomSauvegarde, World monde) {
        if (this.connection != null) {
            if (nomSauvegarde == null){ // Pour une Sauvegarde Rapide
                try {
                    // Creation du Monde 
                    String query1 = "INSERT INTO Monde(idJoueur, Largeur, Hauteur) values (?,?,?)";
                    PreparedStatement stmt1 = connection.prepareStatement(query1);
                    stmt1.setString(1, String.valueOf(idJoueur));
                    stmt1.setString(2, String.valueOf(monde.getWidth()));
                    stmt1.setString(3, String.valueOf(monde.getHeight()));
                    stmt1.executeUpdate();
                    // Charger le World monde : Creature 
                    String query2 = "SELECT * FROM Creature INNER JOIN Sauvegarde ON Sauvegarde.idSauvegarde = Creature.idSauvegarde";
                    PreparedStatement stmt2 = connection.prepareStatement(query2);
                    stmt2.executeQuery();
                    ResultSet rs2 = stmt2.executeQuery();
                    // Creation des Creatures
                    while (rs2.next()) {
                        // Récupération des attributs de la créature depuis le ResultSet
                        ArrayList statCreature = Creature.getStat(rs2);
                       
                        // Récupération de l'Humanoide associé 
                        String query2_1 = "SELECT * FROM Humanoide INNER JOIN Creature ON Creature.idCreature = Humanoide.idCreature WHERE Humanoide.idCreature = ?";
                        stmt1.setString(1, String.valueOf(idJoueur));
                        PreparedStatement stmt2_1 = connection.prepareStatement(query2_1);
                        stmt2_1.executeQuery();
                        ResultSet rs2_1 = stmt2_1.executeQuery();
                        
                        // filtrage du type de la Creature et Creation de l'objet o : 
                        
                        if (rs2_1.getString('nom') == null){ // il s'agit d'une Creature 
                            if (rs2_1.getInt('NbFleche') != null){ // il s'agit d'un Archer
                            
                                }
                            else if (rs2_1.getBoolean('aggressif')){ // il s'agit d'un Guerrier
                            
                                }
                            else { // il s'agit d'un Paysan
                                    
                                }
                            }
                        else { // il s'agit d'un monstre
                            if (rs2_1.getBoolean('aggressif')){ // il s'agit d'un Loup
                                
                                }
                            else { // il s'agit d'un lapin
                                
                                }
                            }
                        
                        }
                        // Créer l'ElementDeJeu à partir de la Creature o et ajout au tableau
                        monde.listElements.add(ElementDeJeu e);
                    
                    // Charger le World monde : Objet 
                    String query3 = "SELECT * FROM Objet INNER JOIN Sauvegarde ON Sauvegarde.idSauvegarde = Objet.idSauvegarde";
                    PreparedStatement stmt3 = connection.prepareStatement(query3);
                    stmt3.executeQuery();
                    ResultSet rs3 = stmt3.executeQuery(); 
                    }
                catch (SQLException ex) {
                    Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
                try {
                    String query = "UPDATE Sauvegarde SET tableauMonde = (SELECT tableauMonde FROM Sauvegarde INNER JOIN Monde ON Sauvegarde.idMonde = Monde.idMonde WHERE idMonde = ?), nbTour = ?, nom = ?  WHERE nom = ? AND idMonde = ? AND Monde.idJoueur = ? INNER JOIN Monde ON Sauvegarde.idMonde = Monde.idMonde";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setString(5, String.valueOf(idMonde));
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
     * @param idMonde
     * @param nomSauvegarde
     * @param monde
     */
    
    public void removeWorld(Integer idJoueur, int idMonde, String nomSauvegarde, World monde) {
        if (this.connection != null) {
            if (nomSauvegarde == null){
                try {
                    String query = "DELETE FROM Sauvegarde INNER JOIN Monde ON Sauvegarde.idMonde = Monde.idMonde WHERE "
                        + "idSauvegarde = (SELECT idSauvegarde FROM Sauvegarde WHERE idMonde = ? AND idJoueur = ?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, monde.getlistElements().toString());
                stmt.setString(2, String.valueOf(monde.getNBTour()));
                stmt.setString(3, String.valueOf(idMonde));
                stmt.setString(4, String.valueOf(idJoueur));
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
                }     
            }
        }
    }
}
