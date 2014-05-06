/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pv168.agencymanager.backend;

import com.sun.org.apache.xml.internal.serializer.ToStream;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author vlado
 */
public class Agent {
    
    private Long id;
    private int AgentNumber;
    private String name;
    private boolean isDead;
    private Date dateOfEnrollment;

    public Agent() {
    }

    public Agent(int AgentNumber, String name, boolean isDead, Date dateOfEnrollment) {
        this.AgentNumber = AgentNumber;
        this.name = name;
        this.isDead = isDead;
        this.dateOfEnrollment = dateOfEnrollment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAgentNumber() {
        return AgentNumber;
    }

    public void setAgentNumber(int AgentNumber) {
        this.AgentNumber = AgentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsDead() {
        return isDead;
    }

    public void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

    public Date getDateOfEnrollment() {
        return dateOfEnrollment;
    }

    public void setDateOfEnrollment(Date dateOfEnrollment) {
        this.dateOfEnrollment = dateOfEnrollment;
    }
   
    @Override
    public String toString() {
        return "Agent {" + "id=" + id + '}';
    }   

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Agent other = (Agent) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

    
}
