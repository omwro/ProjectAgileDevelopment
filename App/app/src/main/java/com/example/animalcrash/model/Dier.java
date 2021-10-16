package com.example.animalcrash.model;

import com.example.animalcrash.controller.SpelerController;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Map class methods
 *
 */

public class Dier {

    private int id;
    private String naam;
    private String afbeelding;
    private String afbeeldingLocked;
    private String beschrijving;

    /**
     * Initialize dier
     */
    public Dier() {
    }

    /**
     * Get id
     *
     * @return id       int
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
     * Get name
     *
     * @return naam     String
     */
    public String getNaam() {
        return naam;
    }

    /**
     * Set name
     *
     * @param naam String   Name of animal
     */
    public void setNaam(String naam) {
        this.naam = naam;
    }

    /**
     * Get image
     *
     * @return afbeelding     String
     */
    public String getAfbeelding() {
        return afbeelding;
    }

    /**
     * Set image
     *
     * @param afbeelding String   Image of animal
     */
    public void setAfbeelding(String afbeelding) {
        this.afbeelding = afbeelding;
    }

    /**
     * Get image when locked
     *
     * @return afbeeldingLocked     String
     */
    public String getAfbeeldingLocked() {
        return afbeeldingLocked;
    }

    /**
     * Set image locked
     *
     * @param afbeeldingLocked String   Image of animal when locked
     */
    public void setAfbeeldingLocked(String afbeeldingLocked) {
        this.afbeeldingLocked = afbeeldingLocked;
    }

    /**
     * Get animal description
     *
     * @return beschrijving    String
     */
    public String getBeschrijving() {
        return beschrijving;
    }

    /**
     * Set animal description
     *
     * @param beschrijving String  Description of animal
     */
    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    /**
     * Get animal caught count
     *
     * @param dierId int    Animal id
     * @return count int
     */
    public int getAantalKeerGevangen(int dierId) {
        SpelerController spelerController = new SpelerController();

        int count, leeuwCount, vosCount, paardCount, rodepandaCount, neushoornCount;
        count = leeuwCount = vosCount = paardCount = rodepandaCount = neushoornCount = 0;

        // Retrieve user's animals
        for (Speler speler : spelerController.getSpelers()) {
            for (Dier dier : speler.getDieren()) {
                if (dier.getId() == dierId) {
                    switch (dierId) {
                        case 1:
                            leeuwCount++;
                            break;
                        case 2:
                            vosCount++;
                            break;
                        case 3:
                            paardCount++;
                            break;
                        case 4:
                            rodepandaCount++;
                            break;
                        case 5:
                            neushoornCount++;
                            break;
                    }
                }
            }
        }

        switch (dierId) {
            case 1:
                count = leeuwCount;
                break;
            case 2:
                count = vosCount;
                break;
            case 3:
                count = paardCount;
                break;
            case 4:
                count = rodepandaCount;
                break;
            case 5:
                count = neushoornCount;
                break;
        }

        return count;
    }

    /**
     * Get relevant data of animal
     *
     * @return string String
     */
    public String getData() {
        StringBuilder string = new StringBuilder();

        string.append("BESCHRIJVING: " + getBeschrijving());
        string.append("\nAANTAL KEER GEVANGEN: " + getAantalKeerGevangen(getId()));

        return string.toString();
    }

    @Override
    public String toString() {
        return "Dier{" +
                "id=" + id +
                ", naam='" + naam + '\'' +
                ", afbeelding(locked)='" + afbeeldingLocked + '\'' +
                ", afbeelding='" + afbeelding + '\'' +
                ", beschrijving='" + beschrijving + '\'' +
                '}';
    }
}
