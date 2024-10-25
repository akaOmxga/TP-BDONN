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
public class Loup extends Monstre implements Combattant{
    
    /**
     * Premier constructeur de Loup
     * 
     * @param pEsq
     * @param pV est le nombre de point de vie du Loup
     * @param dA est le nombre de dégât d'attaque du Loup
     * @param paAtt est le pourcentage de chance qu'une attaque du Loup soit réussie
     * @param p est la position du Loup
     * @param jeu est la représentation matricielle de la carte
     * @param effets est une Collection List de Utilisable contenant les effets appliqués aux joueurs pendant le tour 
     */
    
    public Loup(int pEsq, int pV,int dA,int paAtt,Point2D p,World jeu, List<Utilisable> effets){
      super(pEsq,pV,dA,paAtt,p,jeu, effets);
    }
    
    /**
     * Deuxième constructeur de Loup
     * @param l est un autre Loup, à partir de laquel notre Loup sera créé
     */

    public Loup(Loup l){
      super(l);
    }
    
    /**
     * Troisème contructeur de Loup, permet d'initialiser tous les attributs avec leur valeur par défaut.
     * @param jeu
     */

    public Loup(World jeu){
      super(jeu);
    }
    
    /**
     * combattre permet d'effectuer une attaque du Loup sur la Creature c.
     * le combat est régit par les pourcentage d'attaque et de parade des deux protagonistes
     * @param c est la Creature que notre Loup va attaquer
     */
    
    @Override
    public void combattre(Creature c){
      if (this.distance(c)<=1){
          Random genAlé = new Random();
          int pourcAtt = genAlé.nextInt(100);
          int pourcPar = genAlé.nextInt(100);
          if (c instanceof Personnage p){
            if (pourcAtt <= this.getpageAtt() && pourcPar>p.getpagePar()){
                p.setptVie(p.getptVie() - this.getdegAtt());
                System.out.println("l'attaque du Guerrier est un succès");
            }
            else if (pourcAtt <= this.getpageAtt() && pourcPar<=p.getpagePar() && this.getdegAtt() - p.getptPar() >0){
                p.setptVie(p.getptVie() - this.getdegAtt() + p.getptPar());
                System.out.println("l'attaque du Guerrier est contrée");
            }
          }
          if (c instanceof Monstre m){
            if (pourcAtt <= this.getpageAtt() && pourcPar>m.getpageEsq()){
                m.setptVie(m.getptVie() - this.getdegAtt());
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
            String query = "INSERT INTO Creature (idsauvegarde,hp,pageatt,x,y,degatt) VALUES ( ?, ? , ? , ? , ? , ? ) RETURNING idCreature";
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
            query = "INSERT INTO Humanoide(idCreature,pageEsq) VALUES ( ? , ? ) returning idhumanoide";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1,id2);
            stmt.setInt(2, this.getpageEsq());
            id = stmt.executeQuery();
            id.next();
            id2 = id.getInt("idhumanoide");
            query = "INSERT INTO role(idhumanoide,aggressif) VALUES ( ? , ? )";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1,id2);
            stmt.setBoolean(2, true);
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
