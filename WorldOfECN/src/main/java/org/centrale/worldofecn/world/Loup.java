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
import java.util.Random;

/**
 *
 * @author ECN
 */
public class Loup extends Monstre implements Combattant{
    
    /**
     * Premier constructeur de Loup
     * 
     * @param pV est le nombre de point de vie du Loup
     * @param dA est le nombre de dégât d'attaque du Loup
     * @param ptPar est le nombre de dégat que le Loup pare a chaque parade réussie
     * @param paAtt est le pourcentage de chance qu'une attaque du Loup soit réussie
     * @param paPar est le pourcentage de chance qu'une parade du Loup soit réussie
     * @param p est la position du Loup
     * @param jeu est la représentation matricielle de la carte
     * @param effets est une Collection List de Utilisable contenant les effets appliqués aux joueurs pendant le tour 
     */
    
    public Loup(int pV,int dA,int ptPar,int paAtt,int paPar,Point2D p,World jeu, List<Utilisable> effets){
      super(pV,dA,ptPar,paAtt,paPar,p,jeu, effets);
    }
    
    /**
     * Deuxième constructeur de Loup
     * @param l est un autre Loup, à partir de laquel notre Loup sera créé
     */

    public Loup(Loup l){
      super(l);
    }
    
    /**
     * Troisème contructeur de Loup, permet d'initialiser tous les attributs avec leur valeur par défaut.
     */

    public Loup(World jeu){
      super(jeu);
    }
    
    /**
     * combattre permet d'effectuer une attaque du Loup sur la Creature c.
     * le combat est régit par les pourcentage d'attaque et de parade des deux protagonistes
     * @param c est la Creature que notre Loup va attaquer
     */
    
    @Override
    public void combattre(Creature c){
        if (this.distance(c)<=1){
            Random genAlé = new Random();
            int pourcAtt = genAlé.nextInt(100);
            int pourcPar = genAlé.nextInt(100);
            if (pourcAtt <= this.getpageAtt() && pourcPar>c.getpagePar()){
                c.setptVie(c.getptVie() - this.getdegAtt());
            }
            else if (pourcAtt <= this.getpageAtt() && pourcPar<=c.getpagePar() && this.getdegAtt() - c.getptPar() >0){
                c.setptVie(c.getptVie() - this.getdegAtt() + c.getptPar());
            }
      }
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
