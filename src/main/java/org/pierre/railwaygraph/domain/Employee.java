    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pierre.railwaygraph.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author Pierre
 */
@Entity
@Table(name = "EMPLOYEE")
@Inheritance(strategy = InheritanceType.JOINED)
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String socialSecurityNumber;
    private String lastname;
    private String firstname;
    private String phoneNumber;
    private Employee manager;
    private Date hireDate;
    private Set<Employee> workers = new HashSet<Employee>();
    private Address address;
    private Date created;
    private int version = 0;
    

    

    

    public Employee() {
        this.created = new Date();
    }

    public Employee(String socialSecurityNumber, String lastname, String firstname, String phoneNumber, Date hireDate, Address address) {
        this.created = new Date();
        this.socialSecurityNumber = socialSecurityNumber;
        this.lastname = lastname;
        this.firstname = firstname;
        this.phoneNumber = phoneNumber;
        this.hireDate = hireDate;
        this.address = address;
    }

    
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EMPLOYEE_ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    

    /**
     * Get the value of version
     *
     * @return the value of version
     */
    @Version
    @Column(name = "OBJ_VERSION")
    public int getVersion() {
        return version;
    }
    
    
    

    /**
     * Set the value of version
     *
     * @param version new value of version
     */
    public void setVersion(int version) {
        this.version = version;
    }

       

    /**
     * Get the value of SocialSecurityNumber
     *
     * @return the value of SocialSecurityNumber
     */
    @Column(name = "SS_NUMBER", length = 15, nullable = false, unique = true)
    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    /**
     * Set the value of SocialSecurityNumber
     *
     * @param SocialSecurityNumber new value of SocialSecurityNumber
     */
    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    

    /**
     * Get the value of lastname
     *
     * @return the value of lastname
     */
    @Column(name="FIRSTNAME", length = 255, nullable = false)
    public String getLastname() {
        return lastname;
    }

    /**
     * Set the value of lastname
     *
     * @param lastname new value of lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    

    /**
     * Get the value of firstname
     *
     * @return the value of firstname
     */
    @Column(name = "LASTNAME", length = 255, nullable = false)
    public String getFirstname() {
        return firstname;
    }

    /**
     * Set the value of firstname
     *
     * @param firstname new value of firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    

    /**
     * Get the value of phoneNumber
     *
     * @return the value of phoneNumber
     */
    @Column(name = "PHONE_NUMBER", length = 30, nullable = true)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the value of phoneNumber
     *
     * @param phoneNumber new value of phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    

    /**
     * Get the value of manager
     *
     * @return the value of manager
     */
        @ManyToOne
        @JoinColumn(name="FK_MANAGER_ID", referencedColumnName="EMPLOYEE_ID")
    public Employee getManager() {
        return manager;
    }

    /**
     * Set the value of manager
     *
     * @param manager new value of manager
     */
    public void setManager(Employee manager) {
        this.manager = manager;
    }

    

    /**
     * Get the value of hireDate
     *
     * @return the value of hireDate
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "HIRE_DATE", nullable = false, updatable = false)
    public Date getHireDate() {
        return hireDate;
    }

    /**
     * Set the value of hireDate
     *
     * @param hireDate new value of hireDate
     */
    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    

    /**
     * Get the value of workers
     *
     * @return the value of workers
     */
        @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
        @org.hibernate.annotations.Fetch(
            org.hibernate.annotations.FetchMode.SUBSELECT
        )
    public Set<Employee> getWorkers() {
        return workers;
    }

    /**
     * Set the value of workers
     *
     * @param workers new value of workers
     */
    public void setWorkers(Set<Employee> workers) {
        this.workers = workers;
    }
    
    
    
    public void addWorker(Employee worker) {
        if (worker == null) throw new IllegalArgumentException("worker is NULL!");
        if (worker.getManager() != null)
            worker.getManager().getWorkers().remove(worker);
        worker.setManager(this);
        getWorkers().add(worker);
    }
    
    public void deleteWorker(Employee worker) {
        if (worker == null) throw new IllegalArgumentException("worker is NULL!");
        worker.setManager(null);
        getWorkers().remove(worker);
    }
    
    
        
    /**
     * Get the value of address
     *
     * @return the value of address
     */
        @Embedded
    public Address getAddress() {
        return address;
    }

    /**
     * Set the value of address
     *
     * @param address new value of address
     */
    public void setAddress(Address address) {
        this.address = address;
    }
    

    /**
     * Get the value of created
     *
     * @return the value of created
     */
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name="CREATED", nullable = false, updatable = false)
    public Date getCreated() {
        return created;
    }
        
    
    /**
     * Set the value of created
     *
     * @param created new value of created
     */
    public void setCreated(Date created) {
        this.created = created;
    }



    @Override
    public int hashCode() {
        return (((getSocialSecurityNumber() != null) ? getSocialSecurityNumber().hashCode() : 0)
                ^ (37 * getCreated().hashCode()));
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }
        
        if (!(otherObj instanceof Employee)) {
            return false;
        }
        
        final Employee other = (Employee) otherObj;
        
        return (((getSocialSecurityNumber() == null) ? (other.getSocialSecurityNumber() == null)
                : getSocialSecurityNumber().equals(other.getSocialSecurityNumber()))
                && (getCreated().getTime() == other.getCreated().getTime()));
    }

    @Override
    public String toString() {
        return "Employee ('" + getId() + "'), Lastname: '" + getLastname() + 
        "', Firstname: '" + getFirstname() + "', Social security number : '" + 
        getSocialSecurityNumber() + "', Hire date: '" + getHireDate() + "'";
    }
    
}
