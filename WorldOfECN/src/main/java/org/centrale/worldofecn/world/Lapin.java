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
public class Lapin extends Monstre {
    
    /**
     * Premier constructeur de Lapin
     * 
     * @param pageEsq
     * @param pV est le nombre de point de vie du Lapin
     * @param dA est le nombre de dégât d'attaque du Lapin
     * @param paAtt est le pourcentage de chance qu'une attaque du Lapin soit réussie
     * @param p est la position du Lapin
     * @param jeu est la représentation matricielle de la carte
     * @param effets est une Collection List de Utilisable contenant les effets appliqués aux joueurs pendant le tour 
     */
    
    public Lapin(int pageEsq,int pV,int dA,int paAtt,Point2D p,World jeu, List<Utilisable> effets){
        super(pageEsq,pV,dA,paAtt,p,jeu, effets);
    }
    
    /**
     * Deuxième constructeur de Lapin
     * @param l est un autre Lapin, à partir de laquel notre Lapin sera créé
     */
    
    public Lapin(Lapin l){
        super(l);
    }
    
    /**
     * Troisème contructeur de Lapin, permet d'initialiser tous les attributs avec leur valeur par défaut.
     * @param jeu
     */
    
    public Lapin(World jeu){
        super(jeu);
    }
    
    /**
     *
     * @param connection
     * @param idSauvegarde
     */
    @Override
    public void saveToDatabase(Connection connection,Integer idSauvegarde) {
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
            query = "INSERT INTO Humanoide(idCreature,pageEsq,agressif) VALUES ( ? , ? , ? )";
            stmt = connection.prepareStatement(query);
            stmt = connection.prepareStatement(query);
            stmt.setString(1,String.valueOf(id2));
            stmt.setString(2, String.valueOf(this.getpageEsq()));
            stmt.setString(3, String.valueOf(false));
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
