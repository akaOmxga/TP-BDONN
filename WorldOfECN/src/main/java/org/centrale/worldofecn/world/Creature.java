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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ECN
 */
public abstract class Creature extends ElementDeJeu implements Deplacable{
    
   /**
     * ptVie est le nombre de point de vie de la Creature
     * degAtt est le nombre de dégats qu'inflige la Creature
     * ptPar est le nombre de dégat que la Creature pare a chaque parade réussie
     * pageAtt est le pourcentage de chance qu'une attaque de la Creature soit réussie
     * pagePar est le pourcentage de chance qu'une parade de la Creature soit réussie
     * pos est la position de la Creature
     * effets est une Collection d'Utilisable contenant les différents effets actifs sur le joueur pendant le tour
     */
     
    
    private int ptVie ; 
    private int degAtt;
    private int pageAtt;
    private List<Utilisable> effets;
    
    /**
     * Premier constructeur de Creature
     * @param ptVie est le nombre de point de vie de la Creature
     * @param dAtt est le nombre de dégats qu'inflige la Creature
     * @param ptP est le nombre de dégat que la Creature pare a chaque parade réussie
     * @param pageA est le pourcentage de chance qu'une attaque de la Creature soit réussie
     * @param pageP est le pourcentage de chance qu'une parade de la Creature soit réussie
     * @param p est la position de la Creature
     * @param jeu
     * @param effets est une Collection List de Utilisable contenant les effets appliqués aux joueurs pendant le tour
     */
    
    public Creature(int ptVie,int dAtt, int pageP,Point2D p,World jeu, List<Utilisable> effets){ 
        super(p,jeu);
        this.ptVie = ptVie; 
        degAtt = dAtt;
        this.effets = effets;
    }
    
    /**
     * Deuxième constructeur de Creature
     * @param c est une autre Creature, à partir de laquel notre Creature sera créée
     */
    
    public Creature(Creature c){
        super(c);
        ptVie = c.ptVie;
        degAtt = c.degAtt;
        pageAtt = c.pageAtt;
        effets = c.effets;
    }
    
    /**
     * Troisème contructeur de Creature, permet d'initialiser tous les attributs avec leur valeur par défaut.
     * @param jeu
     */
    
    public Creature(World jeu){
        this(0,0,0,new Point2D(),jeu, new ArrayList<>());
    }
    
    /**
     * getptVie renvoie le nombre de point de vie de la Creature
     * @return ptVie est le nombre de point de vie de la Creature 
     */
    
    
    public int getptVie(){
        return ptVie;
    }
    
    /**
     * getdegAtt renvoie le nombre de dégat d'attaque du personnage
     * @return degAtt est le nombre de dégats qu'inflige la Creature
     */

    public int getdegAtt(){
        return degAtt;
    } 
    

    
    /**
     * getpageAttt renvoie le pourcentage de chance qu'une attaque de la Creature soit réussie
     * @return pageAtt est le pourcentage de chance qu'une attaque de la Creature soit réussie
     */
    
    public int getpageAtt(){
        return pageAtt;
    }
    
    /**
     * setptVie permet d'attribuer la valeur ptVie aux point de Vie ptVie de la Creature
     * @param ptVie est un nombre de point de vie
     */
        
    public void setptVie(int ptVie){
        this.ptVie = ptVie;
    } 
    
    /**
     * setdegAtt permet d'attribuer la valeur dAtt aux dégât d'attaque degAtt de la Creature
     * @param dAtt est le nombre de dégats qu'inflige la Creature
     */
    
    public void setdegAtt(int dAtt){
        degAtt = dAtt;
    } 
    
    /**
     * setpageAtt permet d'attribuer la valeur pageA aux pourcentages d'attaque réussie pageAtt de la Creature
     * @param pageA est le pourcentage de chance qu'une attaque de la Creature soit réussie
     */
    
    public void setpageAtt(int pageA){
        pageAtt = pageA;
    }
    
    /**
     * deplace permet de déplacer la Creature aux coordonnées (x,y) désirées.
     * Simultanément, nous vérifions que lors du déplacement : 
     * 1) la Creature se déplace sur un Objet, si c'est le cas, l'Objet s'active
     * 2) la Creature ne se déplace pas sur une case déjà occuppée par une autre Creature
     * @param x est l'abscisse des coordonnées
     * @param y est l'ordonnée des coordonéees 
     */
    
    @Override
    public void deplace(int x , int y){
    }
    
    /**
     * deplace permet d'attribuer des valeurs de coordonnées aléatoires à la Creature
     */
    
    @Override
    public void deplace(){
        Random genAlé = new Random();
        int x = genAlé.nextInt(21)-10;
        int y = genAlé.nextInt(21)-10;
        deplace(x,y);
    }
    
    /**
     * coincide permet de vérifier si deux Creatures sont sur la même case
     * c'est-à-dire si elle possède les mêmes coordonnées
     * @param c est la Creature avec laquelle nous vérifions la position
     * @return reponse est un booléen traduissant si oui ou non les Creatures sont sur la même case
     */
    
    public boolean coincide(Creature c){
        boolean reponse = (this.getposX()== c.getposX() && this.getposY()== c.getposY());
        return(reponse);
    }
    
    public void affichePos(){
        System.out.println("Votre créature est un: "+this.getClass()+" et est en position: ["+ this.getposX()+";"+this.getposY()+"]");
    }
    
    public abstract void affiche();

}
