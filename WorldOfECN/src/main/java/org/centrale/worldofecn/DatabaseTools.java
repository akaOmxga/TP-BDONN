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
import org.centrale.worldofecn.world.Archer;
import org.centrale.worldofecn.world.Creature;
import org.centrale.worldofecn.world.ElementDeJeu;
import org.centrale.worldofecn.world.Epee;
import org.centrale.worldofecn.world.Guerrier;
import org.centrale.worldofecn.world.Lapin;
import org.centrale.worldofecn.world.Loup;
import org.centrale.worldofecn.world.Paysan;
import org.centrale.worldofecn.world.Point2D;
import org.centrale.worldofecn.world.PotionSoin;
import org.centrale.worldofecn.world.Utilisable;


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
     * @param nomPartie
     * @param nomSauvegarde
     * @param monde
     */
    
    public void readWorld(Integer idJoueur, String nomPartie, String nomSauvegarde, World monde) {
        if (this.connection == null) {
            throw new IllegalStateException("La connexion à la base de données n'est pas établie");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String findSaveQuery;
        
        try {
            if (nomSauvegarde == null) { // Sauvegarde Rapide
                findSaveQuery = """
                    SELECT s.idSauvegarde, s.NbTour, m.Largeur, m.Hauteur
                    FROM Sauvegarde s
                    JOIN Monde m ON s.idMonde = m.idMonde
                    WHERE m.idJoueur = ? 
                    AND s.nom = NULL""";
                    }
            else { // Sauvegarde classique
                findSaveQuery = """
                    SELECT s.idSauvegarde, s.NbTour, m.Largeur, m.Hauteur
                    FROM Sauvegarde s
                    JOIN Monde m ON s.idMonde = m.idMonde
                    WHERE m.idJoueur = ? 
                    AND s.nom = ?""";
                  }
            pstmt = connection.prepareStatement(findSaveQuery);
            pstmt.setInt(1, idJoueur);
            pstmt.setString(2, nomSauvegarde);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                    throw new SQLException("Sauvegarde non trouvée");
                }

            int idSauvegarde = rs.getInt("idSauvegarde");
            monde.setNbTour(rs.getInt("NbTour"));
            monde.setWidth(rs.getInt("Largeur"));
            monde.setHeight(rs.getInt("Hauteur"));
            rs.close();
            pstmt.close();

            // Creature
            String creatureQuery = """
                SELECT c.*, h.*, r.*
                FROM Creature c
                LEFT JOIN Humanode h ON c.idCreature = h.idCreature
                LEFT JOIN Role r ON h.idHumanode = r.idHumanode
                WHERE c.idSauvegarde = ?""";

            pstmt = connection.prepareStatement(creatureQuery);
            pstmt.setInt(1, idSauvegarde);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ElementDeJeu element = createCreatureFromResultSet(rs, monde);
                if (element != null) {
                    monde.getlistElements().add(element);
                }
            }
            rs.close();
            pstmt.close();

            // Objet
            String objetQuery = """
                SELECT o.*, c.*
                FROM Objet o
                LEFT JOIN Categorie c ON o.idObjet = c.idObjet
                WHERE o.idSauvegarde = ?""";

            pstmt = connection.prepareStatement(objetQuery);
            pstmt.setInt(1, idSauvegarde);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ElementDeJeu element = createObjetFromResultSet(rs, monde);
                if (element != null) {
                    monde.getlistElements().add(element);
                }
            }
                
            }
        catch (SQLException ex) {
             System.out.println("Erreur" + ex);
            throw new RuntimeException("Erreur lors de la lecture du monde", ex);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                 System.out.println("Erreur" + ex);
            }
        }          
    }

    // Méthode utilitaire pour créer une créature à partir d'un ResultSet
    private ElementDeJeu createCreatureFromResultSet(ResultSet rs, World monde) throws SQLException { 
        // attributs des creatures                    
        int hp = rs.getInt("HP");                     
        int pagePar = rs.getInt("pagePar");    
        int ptPar = rs.getInt("ptPar");
        int x = rs.getInt("x");                               
        int y = rs.getInt("y");                             
        int degAtt = rs.getInt("DegAtt");   
        int pEsq = rs.getInt("pageEsq");

        String nom = rs.getString("nom");
        if (nom != null) {
            // C'est un humanoïde
            boolean estAgressif = rs.getBoolean("aggressif");
            int nbFleches = rs.getInt("NbFleche");
            if (nbFleches > 0) {                       
                return new Archer(nom, hp, degAtt, pEsq, pagePar, ptPar, 5, new Point2D(x,y), nbFleches, 0, monde, new ArrayList<>());
            } else if (estAgressif) {
                return new Guerrier(nom, hp, degAtt, pEsq, pagePar, ptPar, 1, new Point2D(x,y), 0, 0, 0, 0, 0, monde, new ArrayList<>());
            } else {
                return new Paysan(nom, hp, degAtt, pEsq, pagePar, ptPar, 1, new Point2D(x,y), 0, monde, new ArrayList<>());
            }
        } else {  
            // C'est un monstre
            boolean estAgressif = rs.getBoolean("aggressif");
            if (estAgressif) {
                return new Loup(pEsq, hp, degAtt, pagePar, new Point2D(x,y), monde, new ArrayList<>());
            } else {
                return new Lapin(pEsq, hp, degAtt, pagePar, new Point2D(x,y), monde, new ArrayList<>());
            }
        }
    }

    // Méthode utilitaire pour créer un objet à partir d'un ResultSet
    private ElementDeJeu createObjetFromResultSet(ResultSet rs, World monde) throws SQLException {
        //PotionSoin(int nbPVRendu, int place,int prix,Point2D p,World jeu){
        //Epee(int nbmain,int degEpee, int placeEpee,int prix, Point2D p,World jeu)
        int x = rs.getInt("x");
        int y = rs.getInt("y");
        int place = rs.getInt("place");
        int prix = rs.getInt("prix");

        // Vérifier la catégorie de l'objet
        int degBonus = rs.getInt("degBonus");
        int nbMain = rs.getInt("nbMain");
        Integer nbPvRendus = rs.getInt("nbPvRendus");

        // Créer l'objet 
        if (nbPvRendus != 0) { // Potion
            return new PotionSoin(nbPvRendus, place, prix, new Point2D(x,y), monde);
        } 
        else { // Epee
            return new Epee(nbMain, degBonus, place, prix, new Point2D(x,y), monde);
        }
    }

    /**
     * remove world from database
     * @param idJoueur
     * @param nomPartie
     * @param nomSauvegarde
     * @throws SQLException 
     */
    
    public void removeWorld(int idJoueur, String nomPartie, String nomSauvegarde) throws SQLException {
    if (nomPartie == null || nomSauvegarde == null) {
        throw new IllegalArgumentException("Le nom de la partie et le nom de la sauvegarde ne peuvent pas être null");
    }

    PreparedStatement pstmt = null;
    try {
        // Début de la transaction
        connection.setAutoCommit(false);

        // Id de la Sauvegarde
        String findSaveQuery = """
            SELECT s.idSauvegarde 
            FROM Sauvegarde s
            JOIN Monde m ON s.idMonde = m.idMonde
            WHERE m.idJoueur = ? 
            AND s.nom = ?""";
            
        pstmt = connection.prepareStatement(findSaveQuery);
        pstmt.setInt(1, idJoueur);
        pstmt.setString(2, nomSauvegarde);
        ResultSet rs = pstmt.executeQuery();
        
        if (!rs.next()) {
            throw new SQLException("Aucune sauvegarde trouvée avec ces critères");
        }
        int idSauvegarde = rs.getInt("idSauvegarde");
        rs.close();
        pstmt.close();

        // remove Categorie
        String deleteCategories = """
            DELETE FROM Categorie 
            WHERE idObjet IN (
                SELECT idObjet 
                FROM Objet 
                WHERE idSauvegarde = ?
            )""";
        pstmt = connection.prepareStatement(deleteCategories);
        pstmt.setInt(1, idSauvegarde);
        pstmt.executeUpdate();
        pstmt.close();

        // remove Personnage
        String deletePersonnages = """
            DELETE FROM Personnage 
            WHERE idRole IN (
                SELECT r.idRole 
                FROM Role r
                JOIN Humanode h ON r.idHumanode = h.idHumanode
                JOIN Creature c ON h.idCreature = c.idCreature
                WHERE c.idSauvegarde = ?
            )""";
        pstmt = connection.prepareStatement(deletePersonnages);
        pstmt.setInt(1, idSauvegarde);
        pstmt.executeUpdate();
        pstmt.close();

        // remove Role
        String deleteRoles = """
            DELETE FROM Role 
            WHERE idHumanode IN (
                SELECT h.idHumanode 
                FROM Humanode h
                JOIN Creature c ON h.idCreature = c.idCreature
                WHERE c.idSauvegarde = ?
            )""";
        pstmt = connection.prepareStatement(deleteRoles);
        pstmt.setInt(1, idSauvegarde);
        pstmt.executeUpdate();
        pstmt.close();

        // remove Humanode
        String deleteHumanodes = """
            DELETE FROM Humanode 
            WHERE idCreature IN (
                SELECT idCreature 
                FROM Creature 
                WHERE idSauvegarde = ?
            )""";
        pstmt = connection.prepareStatement(deleteHumanodes);
        pstmt.setInt(1, idSauvegarde);
        pstmt.executeUpdate();
        pstmt.close();

        // remove Creature
        String deleteCreatures = "DELETE FROM Creature WHERE idSauvegarde = ?";
        pstmt = connection.prepareStatement(deleteCreatures);
        pstmt.setInt(1, idSauvegarde);
        pstmt.executeUpdate();
        pstmt.close();

        // remove Object
        String deleteObjets = "DELETE FROM Objet WHERE idSauvegarde = ?";
        pstmt = connection.prepareStatement(deleteObjets);
        pstmt.setInt(1, idSauvegarde);
        pstmt.executeUpdate();
        pstmt.close();

        // remove Sauvegarde
        String deleteSauvegarde = "DELETE FROM Sauvegarde WHERE idSauvegarde = ?";
        pstmt = connection.prepareStatement(deleteSauvegarde);
        pstmt.setInt(1, idSauvegarde);
        pstmt.executeUpdate();

        // Valider la transaction
        connection.commit();

    } catch (SQLException e) {
        // En cas d'erreur, annuler toutes les modifications
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Erreur" + ex);
            }
        }
        throw e;
    } finally {
        // Restaurer l'auto-commit et fermer le PreparedStatement
        if (connection != null) {
            connection.setAutoCommit(true);
        }
        if (pstmt != null) {
            pstmt.close();
        }
    }
}
}

