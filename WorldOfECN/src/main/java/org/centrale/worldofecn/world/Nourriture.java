/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.centrale.worldofecn.world;

/**
 *
 * @author lazaregrail
 */
abstract public class Nourriture extends Objet implements Utilisable{ 
    /**
     * nbTour est la durée en tour de l'effet de la Nourriture, une fois consommée, sur la Creature
     * 1 signifie utilisation instantanée et unique (par exemple pour la PotionSoin)
     * 0 signifie que la Nourriture ne sera pas consommée
     */
    
    private int nbTour; 
    private Point2D pos;
    
    /**
     * Premier constructeur de Nourriture
     * @param nbTour est la durée en tour de l'effet de la Nourriture, une fois consommée, sur la Creature
     * @param pos est la position de la Nourriture
     */
    
    public Nourriture(int nbTour, Point2D pos,World jeu){
        super(0,0,pos,jeu);
        this.nbTour = nbTour; 
    }
    
    /**
     * Deuxième constructeur de Nourriture, par copie
     * @param n est une autre Nourriture, à partir de laquel notre Nourriture sera créée
     */
    
    public Nourriture(Nourriture n){
        super(n);
    }
    
    /**
     * Troisieme constructeur de Nourriture, par defautL. 
     */
    
    public Nourriture(World jeu){
        super(jeu);
        this.nbTour = 0;
    }
    
}
