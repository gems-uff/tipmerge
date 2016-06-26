/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class has information about committers, as: name, e-mai and number of
 * commits
 *
 * @author j2cf, Catarina
 */
public class Committer implements Comparable<Committer> {

    private final String name;
    private final String email;
    private Integer commits = 0;
    private double zScoreM;

    public Committer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public String getInitial() {
        return Arrays.asList(name.split(" ")).stream().map((str) -> ""+str.charAt(0)).collect(Collectors.joining());
    }

    public String getNameEmail() {
        return name + " <" + email + ">";
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the commits
     */
    public Integer getCommits() {
        return commits;
    }

    /**
     * @return the number of committer
     */
    public Integer getQuantity() {
        return 1;
    }

    /**
     * @param commits the commits to set
     */
    public void setCommits(Integer commits) {
        this.commits = commits;
    }

    @Override
    public String toString() {
        //return this.formatted(this.getName()) + " <" + this.formatted(this.getEmail()) + "> : " + this.getCommits();
        return this.formatted(this.getName());
    }
    //Sets a size for one string

    private String formatted(String string) {
        int tam = Math.min(string.length(), 20);
        if (string.length() >= tam) {
            return string.substring(0, tam);
        } else {
            return string.concat("                        ").substring(0, tam);
        }
    }

    @Override
    public int compareTo(Committer cmter) {
        return this.getName().compareTo(cmter.getName());
    }
    //this method compare committer email. It is same equals, but we can't use with OR, only AND. Is not possible to use email OR name.

    @Override
    public int hashCode() {
        int hash = 3;
        //hash = Math.max(67 * hash + Objects.hashCode(this.name), 67 * hash + Objects.hashCode(this.email));
        hash = 67 * hash + Objects.hashCode(this.name);
        //hash = 67 * hash + Objects.hashCode(this.email);
        return hash;
    }
    //this method compare committers name and email to avoid repetition

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Committer other = (Committer) obj;
        return (Objects.equals(this.name, other.name) || (Objects.equals(this.email, other.email)));
    }

    /**
     * @return the zScoreM
     */
    public double getzScoreM() {
        return zScoreM;
    }

    /**
     * @param zScoreM the zScoreM to set
     */
    public void setzScoreM(double zScoreM) {
        this.zScoreM = zScoreM;
    }

}
