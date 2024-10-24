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
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ECN
 */
public class Archer extends Personnage {
    
    /**
     * nbFleches est le Nombre de Flèches que l'Archer possède
     */
    private int nbFleches;
    
    /**
     * Premier constructeur d'Archer
     * 
     * @param n est le nom de l'Archer
     * @param pV est le nombre de point de vie de l'Archer
     * @param dA est le nombre de dégât d'attaque de l'Archer
     * @param ptPar est le nombre de dégat que l'Archer pare a chaque parade réussie
     * @param paAtt est le pourcentage de chance qu'une attaque de l'Archer soit réussie
     * @param paPar est le pourcentage de chance qu'une parade de l'Archer soit réussie
     * @param dMax est la distance maximun à laquelle l'Archer peut attaquer 
     * @param p est la position de l'Archer
     * @param nbFleches est le Nombre de Flèches que l'Archer possède
     * @param argent est la somme d'argent que l'Archer possède
     * @param jeu est une représentation matricielle de la carte 
     * @param effets est une Collection List de Utilisable contenant les effets appliqués aux joueurs pendant le tour 
     */
    
    public Archer(String n,int pV,int dA,int ptPar,int paAtt,int paPar,int dMax,Point2D p,int nbFleches,int argent,World jeu, List<Utilisable> effets){
        super(n,pV,dA,ptPar,paAtt,paPar,dMax,p,argent,jeu, effets);
        this.nbFleches = nbFleches; 
    }
    
    /**
     * Deuxième constructeur d'Archer
     * @param a est un autre Archer, à partir de laquel notre Archer sera créé
     */
    
    public Archer(Archer a){
        super(a);
        this.nbFleches = a.nbFleches; 
    }
    
    /**
     * Troisème contructeur d'Archer, permet d'initialiser tous les attributs avec leur valeur par défaut.
     */
    
    public Archer(World jeu){
        super(jeu);
        nbFleches = 0;
    }
    
    /**
     * combattre permet d'effectuer une attaque d'Archer sur la Creature c.
     * le combat est régit par les pourcentage d'attaque et de parade des deux protagonistes
     * il est notable que l'Archer perd une flèche lorsqu'il attaque
     * @param c est la Creature que notre Archer va attaquer
     */
    
    @Override
    public void combattre(Creature c){
        if (this.distance(c)>1 && this.distance(c)<= this.getdistM() && nbFleches > 0){
            Random genAlé = new Random();
            int pourcAtt = genAlé.nextInt(100);
            nbFleches = nbFleches - 1;
            if (pourcAtt <= this.getpageAtt() ){
                c.setptVie(c.getptVie() - this.getdegAtt());
            }
        }
    } 
    
    public int getnbFleches(){
        return nbFleches;
    }
    
    public void setnbFleches(int nb){
        nbFleches = nb;
    }
    
    /**
     *
     * @param connection
     */
    @Override
    public void saveToDatabase(Connection connection,Integer idSauvegarde) {
        try {
            String query = "INSERT INTO Creature VALUES ( ?, ? , ? , ? , ? , ? ) RETURNING idCreature";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt = connection.prepareStatement(query);
            stmt.setInt(1,idSauvegarde);
            stmt.setInt(2, this.getptVie());
            stmt.setInt(3, this.getpageAtt());
            stmt.setInt(4, this.getPosition().getX());
            stmt.setInt(5, this.getPosition().getY());
            stmt.setInt(6, this.getdegAtt());
            ResultSet id = stmt.executeQuery();
            id.next();
            int id2 = id.getInt("idCreature");
            query = "INSERT INTO Humanoide(idCreature,ptPar,NbFleche,agressif,pagePar,nom) VALUES ( ? , ? , ? , ? , ? , ? )";
            stmt = connection.prepareStatement(query);
            stmt = connection.prepareStatement(query);
            stmt.setInt(1,id2);
            stmt.setInt(2, this.getptPar());
            stmt.setInt(3, nbFleches);
            stmt.setBoolean(4, true);
            stmt.setInt(5, this.getpagePar());
            stmt.setString(6, this.getNom());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Creature.class.getName()).log(Level.SEVERE, null, ex);
        } 
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
