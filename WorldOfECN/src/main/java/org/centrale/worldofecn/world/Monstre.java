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
public abstract class Monstre extends Creature {
    
    private int pageEsq;
    /**
     * Premier constructeur de Monstre
     * 
     * @param pEsq
     * @param ptVie est le nombre de point de vie du Monstre
     * @param dAtt est le nombre de dégât d'attaque du Monstre
     * @param pageA est le pourcentage de chance qu'une attaque du Monstre soit réussie
     * @param p est la position du Monstre
     * @param jeu est la représentation matricielle de la carte
     * @param effets est une Collection List de Utilisable contenant les effets appliqués aux joueurs pendant le tour
     */
    
    public Monstre(int pEsq,int ptVie,int dAtt, int pageA, Point2D p,World jeu, List<Utilisable> effets){
        super(ptVie,dAtt,pageA,p,jeu, effets);
        pageEsq = pEsq;
    }
    
    /**
     * Deuxième constructeur de Monstre
     * @param m est un autre Monstre, à partir de laquel notre Monstre sera créé
     */
    
    public Monstre(Monstre m){
        super(m);
        this.pageEsq = m.pageEsq;
    }
    
    /**
     * Troisème contructeur de Monstre, permet d'initialiser tous les attributs avec leur valeur par défaut.
     * @param jeu
     */
    
    public Monstre(World jeu){
        super(jeu);
        this.pageEsq = 0;
    }
    
    public int getpageEsq(){
        return pageEsq;
    }
    
    public void setpageEsq(int e){
        pageEsq = e;
    }
    
    /** 
     * affiche permet d'afficher les informations du Monstre.
     */
    
    @Override
    public void affiche(){
        System.out.println("Votre monstre est un: "+this.getClass()+
                "a point de vie: "
                +this.getptVie()+" dégat d'attaque: "
                +this.getdegAtt()+" pageAtt: "
                +this.getpageAtt()+" pourcentage de parade: "
                +this.getpageEsq()+" et de position: ["
                + this.getposX()+";"+this.getposY()+"]");
    }
    
    /** 
     * affichePos permet d'afficher la position du Monstre.
     */
    
    public void affichePos(){
        System.out.println("Votre monstre est en position: ["+ this.getposX()+";"+this.getposY()+"]");
    }
    
}
