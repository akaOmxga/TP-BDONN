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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ECN
 */
public class World {

    private static final int MAXPEOPLE = 20;
    private static final int MAXMONSTERS = 10;
    private static final int MAXOBJECTS = 20;
    
    private int[][] map;

    private Integer width;
    private Integer height;
    private Random genAlé = new Random();
    private List<ElementDeJeu> listElements;
    private HashMap<Integer, ElementDeJeu> dicoPerso = new HashMap<>();
    private Joueur player;
    
    // identifiant 
    // configuration des id : 
    private int indiceGuerrier = 100; 
    private int indiceArcher = 200;
    private int indicePaysan = 300;
    private int indiceLoup = 400;
    private int indiceLapin = 500;
    private int indiceEpee = 1100;
    private int indicePotion = 1000;
    private int indiceChampi = 1200;
    private int indiceEpinard = 1300;
    private int indiceNuage = 1400;

    private int nbTour;

    /**
     * Default constructor
     */
    public World() {
        this(20, 20);
        map = new int [20][20];
    }

    /**
     * Constructor for specific world size
     *ç
     * @param width : world width
     * @param height : world height
     */
    public World(int width, int height) {
        this.setHeightWidth(height, width);
        map = new int [width][height];
        init();
        generate();
        
    }

    /**
     * Initialize elements
     */
    private void init() {
        this.listElements = new LinkedList();
        this.player = new Joueur(this);
    }
    
    public void creerMondeAlea(){
        
        Random genAlé = new Random();
        Set<Integer> list = dicoPerso.keySet();
        for(int ind : list){
            int x=-1;
            int y=-1;
            while (dicoPerso.get(ind).getposX()!= x || dicoPerso.get(ind).getposY()!=y){
                x=genAlé.nextInt(width);
                y=genAlé.nextInt(height);
                if (map[x][y] == 0){
                    dicoPerso.get(ind).setpos(x,y);
                    map[x][y] = ind;
                }
            }
        }
    }
    
    /**
     * 
     * @return List
     */
    
    public List<ElementDeJeu> getlistElements(){
        return listElements;
    }
    
    public int getNBTour(){
        return nbTour;
    }
    /**
     *
     * @return
     */
    public Integer getWidth() {
        return width;
    }

    /**
     *
     * @param width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     *
     * @return
     */
    public Integer getHeight() {
        return height;
    }

    /**
     *
     * @param height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     *
     * @param height
     * @param width
     */
    public final void setHeightWidth(Integer height, Integer width) {
        this.setHeight(height);
        this.setWidth(width);
    }

    /**
     * Check element can be created
     *
     * @param element
     * @return
     */
    private ElementDeJeu check(ElementDeJeu element) {
        return element;
    }

    /**
     * Generate personnages
     */
    private void generatePersonnages(int nbElements) {
        Random rand = new Random();
        int ptVie = genAlé.nextInt(21)+190;
        int DA = genAlé.nextInt(11)+55;
        int ptPar = genAlé.nextInt(11)+30;
        int paAtt = genAlé.nextInt(11)+90;
        int paPar = genAlé.nextInt(11)+45;
        int dMax = 1;
        Point2D pos = new Point2D(0,0);
        int argent = genAlé.nextInt(501);
        int nbMain = 0;
        int degEpee = 0;
        int prix = 0;
        int place = 0;
        int nbF = genAlé.nextInt(21)+20;
        for (int i = 0; i < nbElements; i++) {
            int itemType = rand.nextInt(3);
            Creature item = null;
            while (item == null) {
                switch (itemType) {
                    case 0: // Guerrier
                        int indiceGuerrier = 100 + i;
                        item = new Guerrier("guerrier",ptVie,DA,ptPar,paAtt,paPar,dMax,pos,place,nbMain,degEpee,prix,argent,this, new ArrayList<Utilisable>());
                        dicoPerso.put(indiceGuerrier,item);
                        break;
                    case 1: // Archer
                        int indiceArcher = 200 + i;
                        item = new Archer("archer",ptVie,DA,ptPar,paAtt,paPar,dMax,pos,nbF,argent,this,new ArrayList<Utilisable>());
                        dicoPerso.put(indiceArcher,item);
                        break;
                    case 2: // Paysan
                        int indicePaysan = 300 + i;
                        item = new Paysan(this);
                        dicoPerso.put(indicePaysan,item);
                        break;
                }
                item = (Personnage) check(item);
            }
            // Add to list
            this.listElements.add(item);
        }
    }

    /**
     * Generate Monsters
     */
    private void generateMonsters(int nbElements) {
        Random rand = new Random();

        // Generate monsters
        for (int i = 0; i < nbElements; i++) {
            int itemType = rand.nextInt(2);
            Monstre item = null;
            while (item == null) {
                switch (itemType) {
                    case 0: // Lapin
                        indiceLapin = 500+i;
                        item = new Lapin(this);
                        dicoPerso.put(indiceLapin, item);
                        break;
                    case 1: // Loup
                        indiceLoup = 400 + i;
                        item = new Loup(this);
                        dicoPerso.put(indiceLoup,item);
                        break;
                }
                item = (Monstre) check(item);
            }
            // Add to list
            this.listElements.add(item);
        }
    }

    /**
     * Generate Objects
     */
    private void generateObjects(int nbElements) {
        Random rand = new Random();

        // Generate objects
        for (int i = 0; i < nbElements; i++) {
            int itemType = rand.nextInt(2);
            Objet item = null;
            while (item == null) {
                switch (itemType) {
                    case 0: // Potion de soin
                        int indicePotion = 1000 + i;
                        item = new PotionSoin(genAlé.nextInt(20)+20,this);
                        dicoPerso.put(indicePotion,item);
                        break;
                    case 1: // Arme
                        int indiceEpee = 1100 + i;
                        item = new Epee(genAlé.nextInt(20)+20,this);
                        dicoPerso.put(indiceEpee,item); 
                        break;
                }
                item = (Objet) check(item);
            }
            // Add to list
            this.listElements.add(item);
        }
    }

    /**
     * Generate Player
     */
    private void generatePlayer(int itemType) {
        Personnage item = null;
        int indiceJ = 1;
        Random rand = new Random();
        int ptVie = genAlé.nextInt(21)+190;
        int DA = genAlé.nextInt(11)+55;
        int ptPar = genAlé.nextInt(11)+30;
        int paAtt = genAlé.nextInt(11)+90;
        int paPar = genAlé.nextInt(11)+45;
        int dMax = 1;
        Point2D pos = new Point2D(0,0);
        int argent = genAlé.nextInt(501);
        int nbMain = 0;
        int degEpee = 0;
        int prix = 0;
        int place = 0;
        int nbF = genAlé.nextInt(21)+20;
        while (item == null) {
            switch (itemType) {
                case 0: // Guerrier
                    item = new Guerrier("Joueur",ptVie,DA,ptPar,paAtt,paPar,dMax,pos,place,nbMain,degEpee,prix,argent,this, new ArrayList<Utilisable>());
                    dicoPerso.put(indiceJ,item);
                    break;
                case 1: // Archer
                    item = new Archer("Joueur",ptVie,DA,ptPar,paAtt,paPar,dMax,pos,nbF,argent,this,new ArrayList<Utilisable>());
                    dicoPerso.put(indiceJ,item);
                    break;
                case 2: // Paysan
                    item = new Paysan(this);
                    dicoPerso.put(indiceJ,item);
                    break;
            }
            item = (Personnage) check(item);
        }
        // Add to list
        this.listElements.add(item);
    }

    /**
     * Generate elements randomly
     */
    private void generate() {
        Random rand = new Random();


        generatePersonnages(rand.nextInt(MAXPEOPLE));
        generateMonsters(rand.nextInt(MAXMONSTERS));
        generateObjects(rand.nextInt(MAXOBJECTS));
    }
    
    /**
     * Save world to database
     *
     * @param connection
     * @param gameName
     * @param saveName
     */
    public void getToDatabase(Connection connection, String gameName, String saveName) {
        if (connection != null) {
            // Get Player ID
            this.setHeightWidth(0, 0);
            init();
            // Save world for Player ID
        }
    }

    /**
     * Get world from database
     *
     * @param connection
     * @param idJoueur
     */
    public void saveToDatabase(Connection connection , int  idJoueur) {
        if (connection != null) {
            try {
                String query = "INSERT INTO Monde (idJoueur,largeur,hauteur) VALUES ( ?, ?, ? ) returning idMonde";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, idJoueur);
                stmt.setInt(2, this.width);
                stmt.setInt(3, this.height);
                ResultSet id = stmt.executeQuery();
                id.next();
                int id2 = id.getInt("idMonde");
                query = "INSERT INTO Sauvegarde (idMonde,nbtour) VALUES ( ?, ?) RETURNING idSauvegarde";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, id2);
                stmt.setInt(2, nbTour);
                id = stmt.executeQuery() ;
                id.next();
                id2 = id.getInt("idSauvegarde");
                for (ElementDeJeu e: getlistElements()){
                    e.saveToDatabase(connection,id2);
                }
            } catch (SQLException ex) {
                Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void setNbTour(int nbTour){
        this.nbTour = nbTour;
    }
    
    public void setPlayer(String name) {
        this.player.getPersonnage().setNom(name);
    }
    
    public void afficheJeu(){ 
        for (int i = 0;i <  map.length; i++ ) {
            for (int j = 0; j < map.length; j++) {
                if (map[j][i] == 0) {
                    System.out.print("_ ");
                } else if (map[j][i] == 1){
                    System.out.print("J ");
                } else if (map[j][i] >= 100 && map[j][i] < 200){
                    System.out.print("G ");
                } else if (map[j][i] >= 200 && map[j][i] < 300){
                    System.out.print("A ");
                } else if (map[j][i] >= 300 && map[j][i] < 400){
                    System.out.print("P ");
                } else if (map[j][i] >= 400 && map[j][i] < 500){
                    System.out.print("W ");
                } else if (map[j][i] >= 500 && map[j][i] < 600){
                    System.out.print("L ");
                } else if (map[j][i] >= 1000 && map[j][i] < 1100){
                    System.out.print("P ");
                } else if (map[j][i] >= 1100 && map[j][i] < 1200){
                    System.out.print("S ");
                } else if (map[j][i] >= 1200 && map[j][i] < 1300){
                    System.out.print("C ");
                } else if (map[j][i] >= 1300 && map[j][i] < 1400){
                    System.out.print("E ");
                } else if (map[j][i] >= 1400 && map[j][i] < 1500){
                    System.out.print("N ");
                }
            }
            System.out.println("");
        }  
    }
    
    public void setDicoPerso(int index, ElementDeJeu e){
        dicoPerso.put(index, e);
    }
    
    public void setMap(int x, int y, int index){
        map[x][y] = index;
    }
}
