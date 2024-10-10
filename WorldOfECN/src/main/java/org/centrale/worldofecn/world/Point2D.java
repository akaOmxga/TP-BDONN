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
 * @author ECN
 */
public class Point2D {
    
    /**
     * x est la coordonnée d'abscisse
     * y est la coordonnée d'ordonnée
     */

    private int x ;
    private int y ; 
    
    /**
     * Premier constructeur de Point2D
     * 
     * @param x est la coordonnée d'abscisse
     * @param y est la coordonnée d'ordonnée
     */
    
    public  Point2D(int x,int y) {
       this.x=x;
       this.y=y;
    }
    
    /**
     * Deuxième contructeur de Point2D, permet d'initialiser tous les attributs avec leur valeur par défaut.
     */
      
    public Point2D(){
        x=0;
        y=0;
    }
    
    /**
     * Troisème constructeur de Point2D
     * @param v est un autre Point2D, à partir de laquel notre Point2D sera créé
     */
    
    public Point2D(Point2D v){
        x=v.x;
        y=v.y;
    }
      
    public void setX(int x){
        this.x=x;
    }
    
    public int getX(){
        return x;
    }
    
    public void setY(int y){
        this.y=y;
    }
    
    public int getY(){
        return y;
    }
    
    public void setPosition(int x, int y){
        this.x=x;
        this.y=y;
    }
    
    /**
     * translate permet de translater notre Point2D par x en abscisse et y en ordonnée
     * @param x est la valeur de translation selon l'abscisse
     * @param y est la valeur de translation selon l'ordonnée
     */
            
    public void translate(int x, int y){
        this.x = this.x + x;
        this.y = this.y + y;
    }
    
    /**
     * affiche permet d'afficher les coordonées d'un Point2D
     */
    public void affiche(){
        System.out.println("[ "+x+" ; "+y+" ]");
    }
    
    /**
     * distance permet de calculer la distance entre deux Point2D (norme euclidienne de R²)
     * @param p est un Point2D avec lequel nous allons calculer la distance
     * @return 
     */
    
    public float distance (Point2D p){
        return (float) (Math.sqrt( (p.x - this.x)*(p.x - this.x) + (p.y - this.y)*(p.y - this.y) ));
    }
    
    /**
     * egalite renvoie un booléen traduissant l'égalité entre deux Point2D
     * @param p est le Point2D avec lequel nous vérifierons l'égalité
     * @return 
     */
    
    public boolean equals(Point2D p){
        return(this.x == p.x && this.y == p.y);
    }   
    
}
