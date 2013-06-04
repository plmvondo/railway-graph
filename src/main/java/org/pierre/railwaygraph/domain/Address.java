    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Pierre
 */
@Embeddable
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String streetName;
    private String addressSupplement;
    private String zipCode;
    private String city;
    private String country;


    public Address() {
    }
       

    public Address(String streetName, String addressSupplement, String zipCode, String city, String country) {
        this.streetName = streetName;
        this.addressSupplement = addressSupplement;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
    }

        
        

    /**
     * Get the value of streetName
     *
     * @return the value of streetName
     */
        @Column(name = "STREET_NAME", length = 255, nullable = false)
    public String getStreetName() {
        return streetName;
    }

    /**
     * Set the value of streetName
     *
     * @param streetName new value of streetName
     */
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }


    /**
     * Get the value of addressSupplement
     *
     * @return the value of addressSupplement
     */
        @Column(name = "ADDRESS_SUPPLEMENT", length = 255, nullable = true)
    public String getAddressSupplement() {
        return addressSupplement;
    }

    /**
     * Set the value of addressSupplement
     *
     * @param addressSupplement new value of addressSupplement
     */
    public void setAddressSupplement(String addressSupplement) {
        this.addressSupplement = addressSupplement;
    }


    /**
     * Get the value of zipCode
     *
     * @return the value of zipCode
     */
        @Column(name = "ZIP_CODE", length = 5, nullable = false)
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Set the value of zipCode
     *
     * @param zipCode new value of zipCode
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


    /**
     * Get the value of city
     *
     * @return the value of city
     */
        @Column(name = "CITY", length = 255, nullable = false)
    public String getCity() {
        return city;
    }

    /**
     * Set the value of city
     *
     * @param city new value of city
     */
    public void setCity(String city) {
        this.city = city;
    }

    
        

    /**
     * Get the value of country
     *
     * @return the value of country
     */
        @Column(name = "COUNTRY", length = 255, nullable = false)
    public String getCountry() {
        return country;
    }

    /**
     * Set the value of country
     *
     * @param country new value of country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    

    @Override
    public int hashCode() {
        int hash = (getStreetName() != null ? getStreetName().hashCode() : 0);
        hash = hash ^ (29 * (getZipCode() != null ? getZipCode().hashCode() : 0));
        hash = hash ^ (29 * (getCity() != null ? getCity().hashCode() : 0));
        return hash;
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }
        
        if (!(otherObj instanceof Address)) {
            return false;
        }
        
        Address other = (Address) otherObj;
        
        return ((this.getStreetName() != null) ? this.getStreetName().equals(other.getStreetName()) : other.getStreetName() == null)
            && ((this.getZipCode() != null) ? this.getZipCode().equals(other.getZipCode()) : other.getZipCode() == null)
            && ((this.getCity() != null) ? this.getCity().equals(other.getCity()) : other.getCity() == null);
    }

    @Override
    public String toString() {
        return "Street name: '" + getStreetName() + "', Address supplement: '" + getAddressSupplement() + 
        "', Zip code: '" + getZipCode() + "', City: '" + getCity() + "', Country: '" +
        getCountry() + "'";
    }
    
}
