package com.example.animalcrash.model;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      Employee (admin) class methods
 *
 */

public class Medewerker {

    private int id;
    private String gebruikersnaam;
    private String geslacht;
    private String email;

    /**
     * Initialize medewerker
     */
    public Medewerker() {
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
     * Get email
     *
     * @return email     String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set email
     *
     * @param email String   Email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
