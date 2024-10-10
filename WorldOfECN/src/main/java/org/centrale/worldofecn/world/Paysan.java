/* --------------------------------------------------------------------------------
 * WoE
 * 
 * Ecole Centrale Nantes - Septembre 2022
 * Equipe pédagogique Informatique et Mathématiques
 * JY Martin
 * -------------------------------------------------------------------------------- */
package org.centrale.worldofecn.world;

import java.sql.Connection;
import java.util.List;

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
     */
    @Override
    public void saveToDatabase(Connection connection) {
        
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
