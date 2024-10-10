/* --------------------------------------------------------------------------------
 * WoE
 * 
 * Ecole Centrale Nantes - Septembre 2022
 * Equipe pédagogique Informatique et Mathématiques
 * JY Martin
 * -------------------------------------------------------------------------------- */

package org.centrale.worldofecn.world;

/**
 *
 * @author kwyhr
 */
public abstract class Objet extends ElementDeJeu{
    
    private int place;
    private int prix;
    
    public Objet(int place,int prix, Point2D p,World jeu){
        super(p,jeu);
        this.place = place;
        this.prix = prix;
    }
    
    public Objet(Objet o){
        super(o);
        this.place = o.place;
        this.prix = o.prix;
    }
    
    public Objet(World jeu){
        this(0,0,new Point2D(),jeu);
    }
    
    public int getPlace(){
        return place;
    }
    
    public int getPrix(){
        return prix;
    }
    
    public void setPrix(int pris){
        prix = pris;
    }   
    
    /**
     * activation permet d'activer l'Objet sur la Creature c.
     * il est important de noter que chaque Objet possède sa propre activation
     * @param c est la Creature sur laquelle l'Objet sera activé
     */
    
    public abstract void activation(Creature c);
    
}
