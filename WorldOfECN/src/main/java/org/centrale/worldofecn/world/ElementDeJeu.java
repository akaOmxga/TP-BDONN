/* --------------------------------------------------------------------------------
 * WoE
 * 
 * Ecole Centrale Nantes - Septembre 2022
 * Equipe pédagogique Informatique et Mathématiques
 * JY Martin
 * -------------------------------------------------------------------------------- */

package org.centrale.worldofecn.world;

import java.sql.Connection;
import java.util.Random;

/**
 *
 * @author ECN
 */
public abstract class ElementDeJeu {
    private Point2D position;
    
    /**
     * pos est la position de l'ElementDeJeu
     */
    
    private Point2D pos;
    private World jeu;
    
    /** 
     * Premier constructeur de ElementDeJeu
     *@param p est la position de l'ElementDeJeu
     */
    public ElementDeJeu(Point2D p,World jeu){
        this.pos = p;
        this.jeu = jeu;
    }
    
    /**
     * Deuxième constructeur de Monstre
     * @param e est un autre ElementDeJeu, à partir de laquel notre ElementDeJeu sera créé
     */
    
    public ElementDeJeu(ElementDeJeu e){
        this.pos = e.pos;
        this.jeu = e.jeu;
    }
    
    /**
     * Troisème contructeur de Monstre, permet d'initialiser tous les attributs avec leur valeur par défaut.
     */
    
    public ElementDeJeu(){
        this.pos = new Point2D();
        jeu = null;
    }
    
    /**
     * isObjet permet de savoir, grâce à un booléen, si l'ElementDeJeu est un Objet.
     */
    
    public boolean isObjet(){
        return (this instanceof Objet);
    }
    
    /**
     * isCreature permet de savoir, grâce à un booléen, si l'ElementDeJeu est une Creature.
     */
    
    public boolean isCreature(){
        return (this instanceof Creature);
    }
    
    public World getjeu(){
        return jeu;
    }
    
    
    public void setpos(int x, int y){
        pos.setPosition(x,y);
    }
    
    public int getposX(){
        return pos.getX();
    }
    
    public int getposY(){
        return pos.getY();
    }
    
    /**
     * distance permet de calculer la distance entre deux Creatures
     * @param c est la Creature avec laquelle nous calculons la distance
     * @return 
     */
    
    
    public float distance (ElementDeJeu c){
        return this.pos.distance(c.pos);
    }
    
    /**
     *
     * @param connection
     */
    public abstract void saveToDatabase(Connection connection);
    
    /**
     *
     * @param connection
     * @param id
     */
    public abstract void getFromDatabase(Connection connection, Integer id);
}
