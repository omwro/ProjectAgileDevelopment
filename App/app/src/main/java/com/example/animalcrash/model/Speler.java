package com.example.animalcrash.model;

import com.example.animalcrash.controller.DierController;
import com.example.animalcrash.util.DateTimeParser;
import java.util.ArrayList;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Player class methods
 *
 */

public class Speler {

    private int id;
    private String gebruikersnaam;
    private String geslacht;
    private int geld;
    private int speeltijd;
    private String avatar;
    private int online;
    private String laatstGezien;
    private int actief;
    private int XP;
    private ArrayList<Dier> dieren;
    private ArrayList<Integer> gevangenIndexes;
    private ArrayList<Dier> uniekeDieren;

    /**
     * Initialize speler
     */
    public Speler() {
        this.dieren = new ArrayList<>();
        this.gevangenIndexes = new ArrayList<>();
    }

    /**
     * Get id
     *
     * @return id int
     */
    public int getId() {
        return id;
    }

    /**
     * Set id
     *
     * @param id int    Id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get username
     *
     * @return gebruikersnaam   String
     */
    public String getGebruikersnaam() {
        return gebruikersnaam;
    }

    /**
     * Set username
     *
     * @param gebruikersnaam String     Username
     */
    public void setGebruikersnaam(String gebruikersnaam) {
        this.gebruikersnaam = gebruikersnaam;
    }

    /**
     * Get gender
     *
     * @return geslacht     String
     */
    public String getGeslacht() {
        return geslacht;
    }

    /**
     * Set gender
     *
     * @param geslacht String   Gender
     */
    public void setGeslacht(String geslacht) {
        this.geslacht = geslacht;
    }

    /**
     * Get currency
     *
     * @return geld     int
     */
    public int getGeld() {
        return geld;
    }

    /**
     * Add currency
     *
     * @param geld int      Amount of currency
     */
    public void addGeld(int geld){
        this.geld += geld;
    }

    /**
     * Set currency
     *
     * @param geld String   Amount of currency
     */
    public void setGeld(int geld) {
        this.geld = geld;
    }

    /**
     * Get play time
     *
     * @return speeltijd    int
     */
    public int getSpeeltijd() {
        return speeltijd;
    }

    /**
     * Set play time
     *
     * @param speeltijd int     Amount of play time
     */
    public void setSpeeltijd(int speeltijd) {
        this.speeltijd = speeltijd;
    }

    /**
     * Retrieve avatar
     *
     * @return avatarURL String
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Set avatar
     *
     * @param avatar String     Avatar URL
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * Get online status
     *
     * @return online   int
     */
    public int isOnline() {
        return online;
    }

    /**
     * Set online status
     *
     * @param online int    Online status
     */
    public void setOnline(int online) {
        this.online = online;
    }

    /**
     * Get last seen date
     *
     * @return laatstGezien     String
     */
    public String getLaatstGezien() {
        return laatstGezien;
    }

    /**
     * Set last seen date
     *
     * @param laatstGezien String   Date of last seen
     */
    public void setLaatstGezien(String laatstGezien) {
        this.laatstGezien = new DateTimeParser(laatstGezien).dateTimeParser(false);
    }

    /**
     * Check status, whether blocked or not
     *
     * @return actief   int
     */
    public int isActief() {
        return actief;
    }

    /**
     * Set status, (un)block
     *
     * @param actief int    Status
     */
    public void setActief(int actief) {
        this.actief = actief;
    }

    /**
     * Get amount of XP
     *
     * @return XP int
     */
    public int getXP() {
        return XP;
    }

    /**
     * Add amount of XP
     *
     * @param XP int    Amount of xp
     */
    public void addXP(int XP){
        this.XP += XP;
    }

    /**
     * Set amount of XP
     *
     * @param XP int    Amount of XP
     */
    public void setXP(int XP) {
        this.XP = XP;
    }

    /**
     * Get animals of user
     *
     * @return dieren   ArrayList<Dier>
     */
    public ArrayList<Dier> getDieren() {
        return dieren;
    }

    /**
     * Add animal of user
     *
     * @param dier Dier     The animal object
     */
    public void addDieren(Dier dier){
        this.dieren.add(dier);
    }

    /**
     * Set animals of user
     *
     * @param dieren ArrayList<Dier>    Animals
     */
    public void setDieren(ArrayList<Dier> dieren) {
        this.dieren = dieren;
    }

    public ArrayList<Integer> getGevangenIndexes() {
        return gevangenIndexes;
    }

    public void addGevangenIndexes(Integer index) {
        this.gevangenIndexes.add(index);
    }

    public void setGevangenIndexes(ArrayList<Integer> gevangenIndexes) {
        this.gevangenIndexes = gevangenIndexes;
    }

    /**
     * Get unique amount of animals count
     *
     * @return uniekeDieren     int
     */
    public ArrayList<Dier> getUniekeDieren() {
        return uniekeDieren;
    }

    /**
     * Set unique amount of animals count
     *
     * @param uniekeDieren int  Unique animal count
     */
    public void setUniekeDieren(ArrayList<Dier> uniekeDieren) {
        this.uniekeDieren = uniekeDieren;
    }

    /**
     * Get relevant data and score of user
     *
     * @return string String
     */
    public String getDataAndScoreToString() {
        StringBuilder string = new StringBuilder();
        SpelerLevel spelerLevel = new SpelerLevel(getXP());

        string.append("GEVANGEN DIEREN: " + getDieren().size());
        string.append("\nDIEREN ONTDEKT: " + getUniekeDieren().size() + "/" + new DierController().getDieren().size());
        string.append("\nLEVEL: " + spelerLevel.getLevel() + " (" + spelerLevel.getXpLeft() + "/" + spelerLevel.getXpNeeded() + " XP)");
        string.append("\nGELD: " + getGeld());

        return string.toString();
    }

    @Override
    public String toString() {
        return "Speler{" +
                "id=" + id +
                ", gebruikersnaam='" + gebruikersnaam + '\'' +
                ", geslacht='" + geslacht + '\'' +
                ", geld=" + geld +
                ", speeltijd=" + speeltijd +
                ", avatar='" + avatar + '\'' +
                ", online=" + online +
                ", laatstGezien='" + laatstGezien + '\'' +
                ", actief=" + actief +
                ", XP=" + XP +
                ", dieren=" + dieren +
                '}';
    }
}
