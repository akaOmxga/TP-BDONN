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
public class Lapin extends Monstre {
    
    /**
     * Premier constructeur de Lapin
     * 
     * @param pV est le nombre de point de vie du Lapin
     * @param dA est le nombre de dégât d'attaque du Lapin
     * @param ptPar est le nombre de dégat que le Lapin pare a chaque parade réussie
     * @param paAtt est le pourcentage de chance qu'une attaque du Lapin soit réussie
     * @param paPar est le pourcentage de chance qu'une parade du Lapin soit réussie
     * @param p est la position du Lapin
     * @param jeu est la représentation matricielle de la carte
     * @param effets est une Collection List de Utilisable contenant les effets appliqués aux joueurs pendant le tour 
     */
    
    public Lapin(int pV,int dA,int ptPar,int paAtt,int paPar,Point2D p,World jeu, List<Utilisable> effets){
        super(pV,dA,ptPar,paAtt,paPar,p,jeu, effets);
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
