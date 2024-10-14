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
public class Guerrier extends Personnage implements Combattant {
    
   /**
     * arme est l'Epee maniée par le Guerrier
     */
    private Epee arme;
    
    /**
     * Premier constructeur de Personnage
     * 
     * @param n est le nom du Personnage
     * @param pV est le nombre de point de vie du Guerrier
     * @param dA est le nombre de dégât d'attaque du Guerrier
     * @param ptPar est le nombre de dégat que le Guerrier pare a chaque parade réussie
     * @param paAtt est le pourcentage de chance qu'une attaque du Guerrier soit réussie
     * @param paPar est le pourcentage de chance qu'une parade du Guerrier soit réussie
     * @param dMax est la distance maximun à laquelle le Guerrier peut attaquer 
     * @param p est la position du Guerrier
     * @param placeEpee est la place que prend l'epee dans l'inventaire du Guerrier
     * @param nbmain est le nombre de main du Guerrier occupées à manier une Epee
     * @param degEpee est la caractéristique de dégât bonus de l'Epee du Guerrier
     * @param prix est la caractéristique de prix de l'Epee du Guerrier
     * @param argent est la somme d'argent que le Guerrier possède
     * @param jeu est une représentation matricielle de la carte
     * @param effets est une Collection List de Utilisable contenant les effets appliqués aux joueurs pendant le tour
     */
    
    public Guerrier(String n,int pV,int dA,int ptPar,int paAtt,int paPar,int dMax,Point2D p,
            int placeEpee,int nbmain,int degEpee,int prix,int argent, World jeu, List<Utilisable> effets){
        super(n,pV,dA,ptPar,paAtt,paPar,dMax,p,argent,jeu, effets);
        arme = new Epee(nbmain,degEpee,placeEpee,prix,p,jeu);
    }
    
    /**
     * Deuxième constructeur de Guerrier
     * @param a est un autre Guerrier, à partir de laquel notre Guerrier sera créé
     */
    
    public Guerrier(Guerrier a){
        super(a);
        arme = new Epee(a.arme);
    }
    
    /**
     * Troisème contructeur de Guerrier, permet d'initialiser tous les attributs avec leur valeur par défaut.
     */
    
    public Guerrier(World jeu){
        super(jeu);
        arme = new Epee(jeu);
    }
    
    public Epee getarme(){
        return arme;
    }
    /**
     * combattre permet d'effectuer une attaque du Guerrier sur la Creature c.
     * le combat est régit par les pourcentage d'attaque et de parade des deux protagonistes
     * @param c est la Creature que notre Guerrier va attaquer
     */
    
    @Override
    public void combattre(Creature c){
      if (this.distance(c)<=1){
          Random genAlé = new Random();
          int pourcAtt = genAlé.nextInt(100);
          int pourcPar = genAlé.nextInt(100);
          if (c instanceof Personnage p){
            if (pourcAtt <= this.getpageAtt() && pourcPar>p.getpagePar()){
                p.setptVie(p.getptVie() - this.getdegAtt() - this.arme.getdegAtt());
                System.out.println("l'attaque du Guerrier est un succès");
            }
            else if (pourcAtt <= this.getpageAtt() && pourcPar<=p.getpagePar() && this.getdegAtt() - p.getptPar() >0){
                p.setptVie(p.getptVie() - this.getdegAtt() + p.getptPar());
                System.out.println("l'attaque du Guerrier est contrée");
            }
          }
          if (c instanceof Monstre m){
            if (pourcAtt <= this.getpageAtt() && pourcPar>m.getpageEsq()){
                m.setptVie(m.getptVie() - this.getdegAtt() - this.arme.getdegAtt());
                System.out.println("l'attaque du Guerrier est un succès");
            }
          }
    }
  }
    
    /**
     *
     * @param connection
     * @param idSauvegarde
     */
    @Override
    public void saveToDatabase(Connection connection,Integer idSauvegarde) {
        try {
            String query = "INSERT INTO Creature VALUES ( ?, ? , ? , ? , ? , ? ) RETURNING idCreature";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt = connection.prepareStatement(query);
            stmt.setString(1,String.valueOf(idSauvegarde));
            stmt.setString(2, String.valueOf(this.getptVie()));
            stmt.setString(3, String.valueOf(this.getpageAtt()));
            stmt.setString(4, String.valueOf(this.getPosition().getX()));
            stmt.setString(5, String.valueOf(this.getPosition().getY()));
            stmt.setString(6, String.valueOf(this.getdegAtt()));
            ResultSet id = stmt.executeQuery();
            int id2 = id.getInt("idCreature");
            query = "INSERT INTO Humanoide(idCreature,ptPar,agressif,pagePar,nom) VALUES ( ? , ? , ? , ? , ? )";
            stmt = connection.prepareStatement(query);
            stmt = connection.prepareStatement(query);
            stmt.setString(1,String.valueOf(id2));
            stmt.setString(2, String.valueOf(this.getptPar()));
            stmt.setString(3, String.valueOf(true));
            stmt.setString(4, String.valueOf(this.getpagePar()));
            stmt.setString(5, this.getNom());
            stmt.executeUpdate();
            if(arme!=null){
                    arme.saveToDatabase(connection, idSauvegarde);
            }
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
