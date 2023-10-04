package com.jonesclass.chirica.tenniscampattendance;

import java.io.Serializable;

/**
 *
 */
public class Member implements Serializable {
    //Fields.
    private String camp;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private int id;
    private boolean isActive;

    /**
     *
     * @param camp = String
     * @param username = String
     * @param password = String
     * @param firstName = String
     * @param lastName = String
     * @param id = int
     * @param isActive = boolean
     */
    public Member(String camp, String username, String password, String firstName, String lastName, int id, boolean isActive) {
        this.camp = camp;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.isActive = isActive;
    }

    /**
     * One of three parameterized constructors for a member.
     * @param firstName = String
     * @param lastName = String
     * @param id = int
     * @param isActive = boolean
     * @param camp = String
     */
    public Member(String firstName, String lastName, int id, boolean isActive, String camp) {
        username = "USER MEMBER ENTRY";
        password = "USER PASSWORD ENTRY";
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.isActive = isActive;
        this.camp = camp;
    }

    /**
     *
     * @param username
     * @param password
     */
    public Member(String username, String password) {
        camp = "";
        this.username = username;
        this.password = password;
        firstName = "";
        lastName = "";
        id = -5;
        isActive = true;
    }

    /*public Member() {
        firstName = "Stefan";
        lastName = "Chirica";
        id = -1;
        isActive = true;
    }*/

    /**
     * Allows user to get a member's camp.
     * @return String camp.
     */
    public String getCamp() {
        return camp;
    }

    /**
     * Allows user to set the camp of a member.
     * @param camp = String
     */
    public void setCamp(String camp) {
        this.camp = camp;
    }

    /**
     * Allows user to get a member's first name.
     * @return String first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Allows user to set the first name of a member.
     * @param firstName = String
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Allows user to get a member's last name.
     * @return String last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Allows user to set the last name of a member.
     * @param lastName = String
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Allows user to get a member's ID.
     * @return int id.
     */
    public int getId() {
        return id;
    }

    /**
     * Allows user to set the ID of a member.
     * @param id = int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Allows user to get a member's active status.
     * @return boolean isActive.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Allows user to set the active status of a member.
     * @param active = String
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    /**
     * Puts member's information into a string format.
     */
    public String toString() {
        return "Member{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id=" + id +
                ", isActive=" + isActive +
                ", camp=" + camp +
                '}';
    }
}
