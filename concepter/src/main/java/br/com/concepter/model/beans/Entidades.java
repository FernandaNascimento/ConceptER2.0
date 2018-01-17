/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.concepter.model.beans;

import com.sun.xml.internal.bind.CycleRecoverable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author AllanMagnum
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Entidades implements Serializable, CycleRecoverable{
    
    @XmlElement(name="entidade", type=Entidade.class)
    private List<Entidade> entidades = new ArrayList<Entidade>();

    public Entidades() {
    }

    public Entidades(List<Entidade> entidades) {
        this.entidades = entidades;
    }

    public List<Entidade> getEntidades() {
        return entidades;
    }

    public void setEntidades(List<Entidade> entidades) {
        this.entidades = entidades;
    }

    @Override
    public Object onCycleDetected(Context cntxt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
