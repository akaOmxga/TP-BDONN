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
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ECN
 */
public class Joueur implements Deplacable{
    
    private String login;
    private String password;
    private Personnage role;
    private int priorité;
    private Scanner scanner = new Scanner(System.in);
    private World jeu;
    private ArrayList<Objet> inventaire;
    private int tailleInventaire;
    
    /**
     *
     */
    public Joueur(World jeu) {
        this(null, null,jeu);
    }

    /**
     *
     * @param login
     * @param password
     * @param jeu
     */

    public Joueur( String login, String password, World jeu) {
        this.jeu = jeu;
        System.out.print("Entrez votre rôle entre Archer(1) et Guerrier(2): ");
        String classe = scanner.nextLine();
        System.out.print("Entrez votre nom: ");
        String nom = scanner.nextLine();
        Random genAlé = new Random();
        if (classe.equals("1")){
            
            int ptVie = genAlé.nextInt(21)+90;
            int DA = genAlé.nextInt(11)+25;
            int ptPar = genAlé.nextInt(10)+1;
            int paAtt = genAlé.nextInt(11)+75;
            int paPar = genAlé.nextInt(11)+15;
            int dMax = genAlé.nextInt(1)+5;
            Point2D pos = new Point2D(0,0);
            int nbF = genAlé.nextInt(21)+20;
            int argent = genAlé.nextInt(501);
            role = new Archer(nom,ptVie,DA,ptPar,paAtt,paPar,dMax,pos,nbF,argent,jeu,new ArrayList<Utilisable>());
        }
        else if (classe.equals("2")){
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
            role = new Guerrier(nom,ptVie,DA,ptPar,paAtt,paPar,dMax,pos,place,nbMain,degEpee,prix,argent,jeu, new ArrayList<Utilisable>());
        }
        this.login = "test";
        this.password = "aze";
    }

    /**
     *
     * @return
     */
    public String getLogin() {
        return login;
    }

    /**
     *
     * @param login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public Personnage getPersonnage() {
        return role;
    }

    /**
     *
     * @param personnage
     */
    public void setPersonnage(Personnage personnage) {
        this.role = personnage;
    }
    
    @Override
    public void deplace(){
        System.out.println("Le joueur se déplace");
        this.role.deplace();
    }
    
    /**
     * premet de déplacer le joueur à la case de coordonée x y 
     * @param x
     * @param y 
     */
    
    @Override
    public void deplace(int x,int y){
        System.out.println("Le joueur se déplace");
        this.role.deplace( x ,  y);
    }
    
    public void saveToDatabase(Connection connection,Integer idSauvegarde) {
        try {
            String query = "INSERT INTO Creature VALUES ( ?, ? , ? , ? , ? , ? ) RETURNING idCreature";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt = connection.prepareStatement(query);
            stmt.setInt(1,idSauvegarde);
            stmt.setInt(2, this.role.getptVie());
            stmt.setInt(3, this.role.getpageAtt());
            stmt.setInt(4, this.role.getPosition().getX());
            stmt.setInt(5, this.role.getPosition().getY());
            stmt.setInt(6, this.role.getdegAtt());
            ResultSet id = stmt.executeQuery();
            int id2 = id.getInt("idCreature");
            if (this.role instanceof Guerrier g){
                query = "INSERT INTO Humanoide(idCreature,ptPar,agressif,pagePar,nom) VALUES ( ? , ? , ? , ? , ? ) RETURNING idHumanoide";
                stmt = connection.prepareStatement(query);
                stmt = connection.prepareStatement(query);
                stmt.setInt(1,id2);
                stmt.setInt(2, g.getptPar());
                stmt.setBoolean(3, true);
                stmt.setInt(4, g.getpagePar());
                stmt.setString(5, this.role.getNom());
                stmt.executeUpdate();
                if(g.getarme()!=null){
                        g.getarme().saveToDatabase(connection, idSauvegarde);
                }
            } else if (this.role instanceof Archer a){
                query = "INSERT INTO Humanoide(idCreature,ptPar,agressif,pagePar,nom) VALUES ( ? , ? , ? , ? , ? ) RETURNING idHumanoide";
                stmt = connection.prepareStatement(query);
                stmt = connection.prepareStatement(query);
                stmt.setInt(1,id2);
                stmt.setInt(2, a.getptPar());
                stmt.setInt(3, a.getnbFleches());
                stmt.setBoolean(4, true);
                stmt.setInt(5, a.getpagePar());
                stmt.setString(6, a.getNom());
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Creature.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
