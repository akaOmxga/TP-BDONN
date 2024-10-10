/* --------------------------------------------------------------------------------
 * WoE
 * 
 * Ecole Centrale Nantes - Septembre 2022
 * Equipe pédagogique Informatique et Mathématiques
 * JY Martin
 * -------------------------------------------------------------------------------- */
package org.centrale.worldofecn.world;

import java.lang.Integer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.centrale.worldofecn.DatabaseTools;

/**
 *
 * @author ECN
 */
public abstract class Creature extends ElementDeJeu {
    
    /**
     *
     * @param world
     */
    public Creature(World world) {
        super(world);
    }
    
    static public ArrayList<Integer> getStat(ResultSet rs){
        try{
            // Récupération des attributs de la créature depuis le ResultSet
            int id = rs.getInt("idCreature");
            int hp = rs.getInt("HP");
            int pageAtt = rs.getInt("pageAtt");
            int x = rs.getInt("x");
            int y = rs.getInt("y");
            int DegAtt = rs.getInt("DegAtt");
            ArrayList<Integer> result = new ArrayList<>();
            result.add(id);
            result.add(hp);
            result.add(pageAtt);
            result.add(y);
            result.add(x);
            result.add(DegAtt);
            return result;
        }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
            return null;
            }
    }

}
