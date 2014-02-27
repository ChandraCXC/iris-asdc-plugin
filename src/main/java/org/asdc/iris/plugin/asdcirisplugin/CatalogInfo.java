/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asdc.iris.plugin;

import java.io.Serializable;

/**
 *
 * @author Roberto
 */
public class CatalogInfo implements Serializable{
    private int catalogId ;
    private String name ;
    private double radius;
    private static final long serialVersionUID = 140605814607823206L; 

    
    public CatalogInfo() {
    
    }
    
    public CatalogInfo (int catalogId, String name, double radius ) {
        this.catalogId = catalogId;
        this.name = name;
        this.radius = radius;
    }
    
    public String toString() {
        return name;
    }
    
    public String getName() {
        return name;
    }
    public double getRadius() {
        return radius;
    }
    public int getCatalogId() {
        return catalogId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
    
    
        
}
