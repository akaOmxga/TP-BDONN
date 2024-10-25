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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ECN
 */
public class Paysan extends Personnage {
    
    /**
     *
     * @param world
     */
    public Paysan(World world) {
        super(world);
    }
    
    public Paysan(String n,int pV,int dA,int ptPar,int paAtt,int paPar,int dMax,Point2D p,int argent,World jeu, List<Utilisable> effets){
        super(n,pV,dA,ptPar,paAtt,paPar,dMax,p,argent,jeu, effets);
    }
    
    @Override
    public void combattre(Creature c){
        System.out.println("Le paysan tremble devant l'adversité");
    }
    
    /**
     *
     * @param connection
     * @param idSauvegarde
     */
    @Override
    public void saveToDatabase(Connection connection, Integer idSauvegarde){
        try {
            String query = "INSERT INTO Creature (idsauvegarde,hp,pageatt,x,y,degatt)  VALUES ( ?, ? , ? , ? , ? , ? ) RETURNING idCreature";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt = connection.prepareStatement(query);
            stmt.setInt(1,idSauvegarde);
            stmt.setInt(2, this.getptVie());
            stmt.setInt(3, this.getpageAtt());
            stmt.setInt(4, this.getPosition().getX());
            stmt.setInt(5, this.getPosition().getY());
            stmt.setInt(6, this.getdegAtt());
            ResultSet id = stmt.executeQuery();
            id.next();
            int id2 = id.getInt("idCreature");
            query = "INSERT INTO Humanoide(idCreature,ptPar,pagePar,nom) VALUES ( ? , ? , ? , ?) RETURNING idhumanoide";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1,id2);
            stmt.setInt(2, this.getptPar());
            stmt.setInt(3, this.getpagePar());
            stmt.setString(4, this.getNom());
            id = stmt.executeQuery();
            id.next();
            id2 = id.getInt("idhumanoide");
            query = "INSERT INTO role(idhumanoide,aggressif) VALUES ( ? , ? )";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1,id2);
            stmt.setBoolean(2, false);
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
