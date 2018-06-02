/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.concepter.model.beans;

import java.io.Serializable;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.internal.bind.CycleRecoverable;

import br.com.concepter.view.AreaGrafica;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class SavedObject implements Serializable, CycleRecoverable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	private Integer cont_entidade;
	private Integer cont_relacionamento;
	private Integer cont_atributo;
	
	private String graph;
	
	private HashMap<Integer, Entidade> mapaGraficoEntidades;
	private HashMap<Integer, Atributo> mapaGraficoAtributos;
	private HashMap<Integer, Relacionamento> mapaGraficoRelacionamentos;
	private HashMap<Integer, Especializacao> mapaGraficoEspecializacao;
	private HashMap<Integer, Agregacao> mapaGraficoAgregacao;

    public SavedObject() {
		// TODO Auto-generated constructor stub
	}
	
    public SavedObject(AreaGrafica areaGrafica) {
		this.cont_atributo = AreaGrafica.getCont_atributo();
		this.cont_relacionamento = areaGrafica.getCont_relacionamento();
		this.cont_entidade = AreaGrafica.getCont_entidade();
		
		this.mapaGraficoEntidades = AreaGrafica.getMapaGraficoEntidades();
		this.mapaGraficoAtributos = AreaGrafica.getMapaGraficoAtributos();
		this.mapaGraficoRelacionamentos = AreaGrafica.getMapaGraficoRelacionamentos();
		this.mapaGraficoEspecializacao = AreaGrafica.getMapaGraficoEspecializacao();
		this.mapaGraficoAgregacao = AreaGrafica.getMapaGraficoAgregacao();
	}

    public Integer getCont_entidade() {
		return cont_entidade;
	}

	public void setCont_entidade(Integer cont_entidade) {
		this.cont_entidade = cont_entidade;
	}

	public Integer getCont_relacionamento() {
		return cont_relacionamento;
	}

	public void setCont_relacionamento(Integer cont_relacionamento) {
		this.cont_relacionamento = cont_relacionamento;
	}

	public Integer getCont_atributo() {
		return cont_atributo;
	}

	public void setCont_atributo(Integer cont_atributo) {
		this.cont_atributo = cont_atributo;
	}

	public String getGraph() {
		return graph;
	}

	public void setGraph(String graph) {
		this.graph = graph;
	}

	public HashMap<Integer, Entidade> getMapaGraficoEntidades() {
		return mapaGraficoEntidades;
	}

	public void setMapaGraficoEntidades(HashMap<Integer, Entidade> mapaGraficoEntidades) {
		this.mapaGraficoEntidades = mapaGraficoEntidades;
	}

	public HashMap<Integer, Atributo> getMapaGraficoAtributos() {
		return mapaGraficoAtributos;
	}

	public void setMapaGraficoAtributos(HashMap<Integer, Atributo> mapaGraficoAtributos) {
		this.mapaGraficoAtributos = mapaGraficoAtributos;
	}

	public HashMap<Integer, Relacionamento> getMapaGraficoRelacionamentos() {
		return mapaGraficoRelacionamentos;
	}

	public void setMapaGraficoRelacionamentos(HashMap<Integer, Relacionamento> mapaGraficoRelacionamentos) {
		this.mapaGraficoRelacionamentos = mapaGraficoRelacionamentos;
	}

	public HashMap<Integer, Especializacao> getMapaGraficoEspecializacao() {
		return mapaGraficoEspecializacao;
	}

	public void setMapaGraficoEspecializacao(HashMap<Integer, Especializacao> mapaGraficoEspecializacao) {
		this.mapaGraficoEspecializacao = mapaGraficoEspecializacao;
	}

	public HashMap<Integer, Agregacao> getMapaGraficoAgregacao() {
		return mapaGraficoAgregacao;
	}

	public void setMapaGraficoAgregacao(HashMap<Integer, Agregacao> mapaGraficoAgregacao) {
		this.mapaGraficoAgregacao = mapaGraficoAgregacao;
	}

	@Override
    public Object onCycleDetected(Context cntxt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
