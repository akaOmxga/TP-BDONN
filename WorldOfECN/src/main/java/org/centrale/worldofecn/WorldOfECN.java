/* --------------------------------------------------------------------------------
 * ECN Tools
 * 
 * Ecole Centrale Nantes - Septembre 2022
 * Equipe pédagogique Informatique et Mathématiques
 * JY Martin
 * -------------------------------------------------------------------------------- */

package org.centrale.worldofecn;

import java.sql.SQLException;
import org.centrale.worldofecn.world.World;

/**
 *
 * @author ECN
 */
public class WorldOfECN {

    /**
     * main program
     * @param args
     */
    public static void main(String[] args) throws SQLException {
        World world = new World();
        world.setPlayer("Saegusa");
        
        // Test phase
        DatabaseTools database = new DatabaseTools();

        // Save world
        database.connect();
        Integer playerId = database.getPlayerID("test", "aze");
       // database.saveJustWorld(playerId,world);
         database.saveWorld(playerId,"Start", 5 , world);
        
        // Retreive World
        database.readWorld(playerId, "Start", world);
        
        
        // Delete World 
        //database.removeWorld(playerId, "Start");
        
        // The End
        database.disconnect();
    }
}
