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
            String query = "INSERT INTO Creature VALUES ( ?, ? , ? , ? , ? , ? ) RETURNING idCreature";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt = connection.prepareStatement(query);
            stmt.setString(1,String.valueOf(idSauvegarde));
            stmt.setString(2, String.valueOf(this.getptVie()));
            stmt.setString(3, String.valueOf(this.getpageAtt()));
            stmt.setString(4, String.valueOf(this.getPosition().getX()));
            stmt.setString(5, String.valueOf(this.getPosition().getY()));
            stmt.setString(6, String.valueOf(this.getdegAtt()));
            ResultSet id = stmt.executeQuery();
            int id2 = id.getInt("idCreature");
            query = "INSERT INTO Humanoide(idCreature,ptPar,agressif,pagePar,nom) VALUES ( ? , ? , ? , ? , ? ) RETURNING idCreature";
            stmt = connection.prepareStatement(query);
            stmt = connection.prepareStatement(query);
            stmt.setString(1,String.valueOf(id2));
            stmt.setString(2, String.valueOf(this.getptPar()));
            stmt.setString(3, String.valueOf(false));
            stmt.setString(4, String.valueOf(this.getpagePar()));
            stmt.setString(5, this.getNom());
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
