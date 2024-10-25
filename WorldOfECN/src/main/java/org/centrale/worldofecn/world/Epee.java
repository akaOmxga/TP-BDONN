/* --------------------------------------------------------------------------------
 * WoE
 * 
 * Ecole Centrale Nantes - Septembre 2022
 * Equipe pédagogique Informatique et Mathématiques
 * JY Martin
 * -------------------------------------------------------------------------------- */

package org.centrale.worldofecn.world;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kwyhr
 */
public class Epee extends Objet {
    
    /**
     * nbMain est le nombre de Main nécessaire pour manier l'Epee
     * degAtt est le bonus de dégât d'attaque engendré par l'Epee
     */
    private int nbMain;
    private int degAtt;
    
    /**
     * Premier constructeur d'Epee
     * 
     * @param nbmain
     * @param degEpee est le bonus de dégât d'attaque engendré par l'Epee
     * @param placeEpee place prise par l'epee
     * @param prix pric de l'epee
     * @param p est la position de l'Epee
     */
    
    public Epee(int nbmain,int degEpee, int placeEpee,int prix, Point2D p,World jeu){
        super(placeEpee,prix, p,jeu);
        this.nbMain = nbmain;
        this.degAtt = degEpee;
    }
    
    /**
     * crée une épee
     * @param degAtt 
     */
    
    public Epee(int degAtt,World jeu){
        super(jeu);
        this.nbMain = 0;
        this.degAtt = degAtt;
    }
    
    /**
     * Deuxième constructeur d'Epee
     * @param e est une autre Epee, à partir de laquel notre Epee sera créée
     */
    
    public Epee(Epee e){
        super(e);
        this.nbMain = e.getPlace();
        this.degAtt = e.getdegAtt();
    }
    
    /**
     * Troisème contructeur d'Epee, permet d'initialiser tous les attributs avec leur valeur par défaut.
     */
    
    public Epee(World jeu){
        super(jeu);
        nbMain = 0;
        degAtt = 0;
    }
    
    public int getnbMain(){
        return nbMain;
    }
    
    public int getdegAtt(){
        return degAtt;
    }
    
    public void setnbMain(int nbMain){
        this.nbMain = nbMain;
    }
    public void setdegAtt(int degAtt){
        this.degAtt = degAtt;
    }
    
    /**
     * activation permet d'activer l'Epee sur la Creature c.
     * l'Epee vient augmenter de degAtt les dégâts d'attaque de la Creature c
     * et nécessite une taille d'inventaire suffisante
     * @param c est la Creature sur laquelle l'Epee sera activée
     */
    
    @Override
    public void activation(Creature c) {
        System.out.println("L'épée est activée, elle augmente votre force.");
    }

    /**
     *
     * @param connection
     * @param idSauvegarde
     */
    @Override
    public void saveToDatabase(Connection connection,Integer idSauvegarde) {
        try {
            String query = "INSERT INTO Objet (idsauvegarde,place,prix,x,y) VALUES ( ? , ? , ? , ? , ? ) RETURNING idobjet";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt = connection.prepareStatement(query);
            stmt.setInt(1,idSauvegarde);
            stmt.setInt(2, this.getPlace());
            stmt.setInt(3, this.getPrix());
            stmt.setInt(4, this.getPosition().getX());
            stmt.setInt(5, this.getPosition().getY());
            ResultSet id = stmt.executeQuery();
            id.next();
            int id2 = id.getInt("idobjet");
            query = "INSERT INTO Categorie(idObjet,degBonus,nbMain) VALUES ( ? , ? , ?)";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1,id2);
            stmt.setInt(2, this.degAtt);
            stmt.setInt(3, this.nbMain);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Creature.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    /**
     *
     * @param connection
     * @param id
     */
    @Override
    public void getFromDatabase(Connection connection, Integer id) {

    }
    
}
