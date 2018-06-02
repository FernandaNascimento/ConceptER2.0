package br.com.concepter.model.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import br.com.concepter.model.enuns.TipoEntidadeEnum;
import br.com.concepter.view.AreaGrafica;


public class Entidade implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
    private String nome;
    private TipoEntidadeEnum tipo;
    
    //private Especializacao especializacao;
    
    
    private List<Atributo> atributos = new ArrayList<>();
    
    @XmlTransient
    private mxGraph grafico;
    
    @XmlTransient
    private mxCell cell;
    
    private double pX;
    
    private double pY;
    
    private int tamanhoLargura;
    
    private int tamanhoAltura;
    
    private boolean hasRelacionamento;

    public Entidade() {
    }
    
    public Entidade(mxGraph grafico,  String nome, double pX, double pY, TipoEntidadeEnum tipo){
            this.nome = nome;
            this.tipo = tipo;
            this.grafico = grafico;
            this.tamanhoLargura = 100;
            this.tamanhoAltura = 50;
            this.pX = pX;
            this.pY = pY;
    }
	
    public mxCell add(){

            this.grafico.getModel().beginUpdate();
            Object entidade = null;
            Object parent = this.grafico.getDefaultParent();
            String caracteristicas = "";
            if(tipo == TipoEntidadeEnum.FORTE){
                caracteristicas = "fillColor=white;shape=rectangle;";
            }
            if(tipo == TipoEntidadeEnum.FRACA){
                caracteristicas = "fillColor=white;shape=doubleRectangle;";
            }
            try{		
                entidade = this.grafico.insertVertex(parent, 
                                                     null, 
                                                     this.nome, 
                                                     this.pX, 
                                                     this.pY, 
                                                     this.tamanhoLargura, 
                                                     this.tamanhoAltura, 
                                                     caracteristicas);
            }
            finally{
                this.id = Integer.parseInt(((mxCell) entidade).getId());
                this.cell = (mxCell)entidade;
                
                AreaGrafica.getMapaGraficoEntidades().put( Integer.valueOf( ((mxCell) entidade).getId() ), this);
                this.grafico.getModel().endUpdate();
                this.grafico.refresh();
            }
            return (mxCell)entidade;
    }
    
    public void add(Especializacao especializacao){
            this.grafico.getModel().beginUpdate();
            Object parent = this.grafico.getDefaultParent();
            
            String caracteristicas = "";
            if(tipo == TipoEntidadeEnum.FORTE){
                caracteristicas = "fillColor=white;shape=rectangle;";
            }
            if(tipo == TipoEntidadeEnum.FRACA){
                caracteristicas = "fillColor=white;shape=doubleRectangle;";
            }
            try{	
                double posx = especializacao.getCell().getGeometry().getX();
                double posy = especializacao.getCell().getGeometry().getY();
                
                this.cell = (mxCell) this.grafico.insertVertex(parent, 
                                                            null, 
                                                            this.nome, 
                                                            posx - 40, 
                                                            posy + 70, 
                                                            this.tamanhoLargura, 
                                                            this.tamanhoAltura, 
                                                            caracteristicas);

                this.grafico.insertEdge(parent, null, "U", especializacao.getCell(), this.cell, "startArrow=none;endArrow=none;");
            }
            finally{
                    //this.especializacao = especializacao;
                    especializacao.getEntidades().add(this);
                    AreaGrafica.getMapaGraficoEntidades().put( Integer.valueOf( cell.getId() ), this);
                    this.grafico.getModel().endUpdate();
                    this.grafico.refresh();
            }
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public TipoEntidadeEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoEntidadeEnum tipo) {
        this.tipo = tipo;
    }
   
   /* public Especializacao getEspecializacao() {
        return especializacao;
    }

    public void setEspecializacao(Especializacao especializacao) {
        this.especializacao = especializacao;
    }*/
    
   
 
    public List<Atributo> getAtributos() {
        return atributos;
    }
    
    public void setAtributos(List<Atributo> atributos) {
        this.atributos = atributos;
    }
    @XmlTransient
    public mxGraph getGrafico() {
        return grafico;
    }

    public void setGrafico(mxGraph grafico) {
        this.grafico = grafico;
    }
    @XmlTransient
    public mxCell getCell() {
        return cell;
    }

    public void setCell(mxCell cell) {
        this.cell = cell;
    }
    
    public double getpX() {
        return pX;
    }
    
    public void setpX(double pX) {
        this.pX = pX;
    }
    
    public double getpY() {
        return pY;
    }

    public void setpY(double pY) {
        this.pY = pY;
    }
    
    public int getTamanhoLargura() {
        return tamanhoLargura;
    }

    public void setTamanhoLargura(int tamanhoLargura) {
        this.tamanhoLargura = tamanhoLargura;
    }
    
    public int getTamanhoAltura() {
        return tamanhoAltura;
    }

    public void setTamanhoAltura(int tamanhoAltura) {
        this.tamanhoAltura = tamanhoAltura;
    }

	public boolean hasRelacionamento() {
		return hasRelacionamento;
	}

	public void setHasRelacionamento(boolean hasRelacionamento) {
		this.hasRelacionamento = hasRelacionamento;
	}
   
}
