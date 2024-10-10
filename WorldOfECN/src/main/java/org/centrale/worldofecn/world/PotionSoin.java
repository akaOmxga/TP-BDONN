/* --------------------------------------------------------------------------------
 * WoE
 * 
 * Ecole Centrale Nantes - Septembre 2022
 * Equipe pédagogique Informatique et Mathématiques
 * JY Martin
 * -------------------------------------------------------------------------------- */

package org.centrale.worldofecn.world;

import java.sql.Connection;

/**
 *
 * @author lazaregrail & victorsimon
 */
public class PotionSoin extends Nourriture {
    
    /**
     * nbPVRendu est le nombre de point de vie que la PotionSoin peut rendre à une Creature
     */
    private int nbPVRendu;
    
    /**
     * Premier constructeur de PotionSoin
     * 
     * @param nbPVRendu est le nombre de point de vie que la PotionSoin peut rendre à une Creature
     * @param place est le nombre de place que l'objet occupe dans l'inventaire d'un Personnage
     * @param prix est la quantité d'argent que vaut l'Objet
     * @param p est la position de l'Objet
     */
    
    public PotionSoin(int nbPVRendu, int place,int prix,Point2D p,World jeu){
        super(1,p,jeu);
        this.nbPVRendu = nbPVRendu;
    }
    
    /**
     * crée une potion de soin
     * @param nbPVRendu 
     */
    
    public PotionSoin(int nbPVRendu,World jeu){
        super(jeu);
        this.nbPVRendu = nbPVRendu;
    }
    
    /**
     * Deuxième constructeur d'Objet, permet d'initialiser tous les attributs avec leur valeur par défaut.
     */
    
    public PotionSoin(World jeu){
        super(jeu);
        nbPVRendu = 0;
    }
    
    /**
     * on crée une potion de soin a partir d'une autre potion de soin
     * @param p 
     */
    
    public PotionSoin(PotionSoin p){
        super(p);
    }
    
    /**
     * activation permet d'activer la PotionSoin sur la Creature c.
     * la potion permet de rendre nbPVRendu aux points de vie de la Creature c
     * @param c est la Creature sur laquelle la PotionSoin sera activée
     */
    
    
    @Override
    public void activation(Creature c) {
        c.setptVie(c.getptVie() + nbPVRendu);
        System.out.println("Vous avez regagné des points de vie");
    }
    
    
    public int getnbPVRendu(){
        return nbPVRendu;
    }
    
    public void setnbPVRendu(int nb){
        nbPVRendu = nb;
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
