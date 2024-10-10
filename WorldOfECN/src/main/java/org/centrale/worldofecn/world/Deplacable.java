/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.worldofecn.world;

/**
 *
 * @author lazaregrail & victorsimon
 * 
 */

public interface Deplacable {
    
    /**
     * Permet de déplacer les Joueurs et PNJ.
     * @param x est l'abscisse du déplacement
     * @param y est l'ordonnée du déplacement
     */
    
    void deplace(int x , int y);
    
    /**
     * Permet de déplacer les Créatures ou le joueur vers une direction aléatoire
     */
    
    void deplace();
}

