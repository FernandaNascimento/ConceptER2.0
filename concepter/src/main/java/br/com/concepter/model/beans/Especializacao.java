/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.concepter.model.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import br.com.concepter.model.enuns.TipoEntidadeEnum;
import br.com.concepter.model.enuns.TipoEspecializacaoEnum;
import br.com.concepter.model.enuns.TipoObrigatoriedadeEnum;
import br.com.concepter.view.AreaGrafica;


public class Especializacao {
    
    private Long id;
    private String nome;
    private TipoEspecializacaoEnum tipoEspecializacao;
    private TipoObrigatoriedadeEnum tipoObrigatoriedade;
    
    private List<Entidade> entidades = new ArrayList<Entidade>();
    
    private Entidade entidadePrincipal;
    
    @XmlTransient
    private mxGraph grafico;
    @XmlTransient
    private mxCell cell;
    
    private double pX;
    private double pY;
    private int tamanhoLargura;
    private int tamanhoAltura;

    public Especializacao() {
    }
    
    public Especializacao(mxGraph grafico,  double pX, double pY, TipoEspecializacaoEnum tipoEspecializacao){
            this.grafico = grafico;
            this.tamanhoLargura = 20;
            this.tamanhoAltura = 20;
            this.pX = pX;
            this.pY = pY + 50;
            this.tipoEspecializacao = tipoEspecializacao;
    }

    public void add(Entidade entidade){
        this.grafico.getModel().beginUpdate();
        Object parent = this.grafico.getDefaultParent();
        
        if(tipoEspecializacao == TipoEspecializacaoEnum.ESPECIALIZACAO){
            nome = "c";
        }
        
        if(tipoEspecializacao == TipoEspecializacaoEnum.DISJUNCAO){
            nome = "d";
        }
        if(tipoEspecializacao == TipoEspecializacaoEnum.SOBREPOSICAO){
            nome = "o";
        }
        
        Double posx = entidade.getCell().getGeometry().getX();
        Double posy = entidade.getCell().getGeometry().getY();

        try{	
            Entidade entidade_1 = new Entidade(this.grafico,  "Entidade" + AreaGrafica.getCont_entidade(), posx - 125, posy + 170, TipoEntidadeEnum.FORTE);
            entidade_1.add();
            this.entidades.add(entidade_1);
            
            if(tipoEspecializacao != TipoEspecializacaoEnum.ESPECIALIZACAO){
            	AreaGrafica.setCont_entidade();
            	this.cell = (mxCell) this.grafico.insertVertex(parent, null, this.nome, posx+40, posy+100, this.tamanhoLargura, this.tamanhoAltura, "fillColor=white;shape=ellipse;");
                Entidade entidade_2 = new Entidade(this.grafico, "Entidade" + AreaGrafica.getCont_entidade(), posx + 125, posy + 170, TipoEntidadeEnum.FORTE);
                entidade_2.add();
                this.entidades.add(entidade_2);
                this.grafico.insertEdge(parent, null, "U", entidade_2.getCell(), this.cell,"startArrow=none;endArrow=none;");
                this.grafico.insertEdge(parent, null, "", this.cell, entidade.getCell(),"startArrow=none;endArrow=none;dashed=1;");
            } else {
            	this.cell = entidade.getCell();            	
            }
            this.entidadePrincipal = entidade;
            this.tipoObrigatoriedade = TipoObrigatoriedadeEnum.PARCIAL;
            this.entidades.add(entidade);
                        
            mxStylesheet stylesheet = this.grafico.getStylesheet();
            
            String myStyleName = "myImageStyle";
            
            // define image style           
            Hashtable<String, Object> style = new Hashtable<String, Object>();
            style.put( mxConstants.STYLE_SHAPE, mxConstants.SHAPE_IMAGE);
            style.put( mxConstants.STYLE_IMAGE, "file:/c:/images/estarContido.png");
            style.put( mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);

            stylesheet.putCellStyle( myStyleName, style);
            
            this.grafico.insertEdge(parent, null, "U", entidade_1.getCell(), this.cell,"startArrow=none;endArrow=none;");
        } catch(Exception e) {
        	e.printStackTrace();
        } finally{
        	AreaGrafica.getMapaGraficoEspecializacao().put( Integer.valueOf( this.cell.getId() ), this);
            this.grafico.getModel().endUpdate();
            this.grafico.refresh();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoEspecializacaoEnum getTipoEspecializacao() {
        return tipoEspecializacao;
    }

    public void setTipoEspecializacao(TipoEspecializacaoEnum tipoEspecializacao) {
        this.tipoEspecializacao = tipoEspecializacao;
    }

    public List<Entidade> getEntidades() {
        return entidades;
    }

    public void setEntidades(List<Entidade> entidades) {
        this.entidades = entidades;
    }

    @XmlTransient
    public mxGraph getGrafico() {
        return grafico;
    }

    public void setGrafico(mxGraph grafico) {
        this.grafico = grafico;
    }
    @XmlTransient
    public double getpX() {
        return pX;
    }

    public void setpX(double pX) {
        this.pX = pX;
    }
    @XmlTransient
    public double getpY() {
        return pY;
    }

    public void setpY(double pY) {
        this.pY = pY;
    }
    @XmlTransient
    public int getTamanhoLargura() {
        return tamanhoLargura;
    }

    public void setTamanhoLargura(int tamanhoLargura) {
        this.tamanhoLargura = tamanhoLargura;
    }
    @XmlTransient
    public int getTamanhoAltura() {
        return tamanhoAltura;
    }

    public void setTamanhoAltura(int tamanhoAltura) {
        this.tamanhoAltura = tamanhoAltura;
    }
    @XmlTransient
    public mxCell getCell() {
        return cell;
    }

    public void setCell(mxCell cell) {
        this.cell = cell;
    }

	public Entidade getEntidadePrincipal() {
		return entidadePrincipal;
	}

	public void setEntidadePrincipal(Entidade entidadePrincipal) {
		this.entidadePrincipal = entidadePrincipal;
	}

	public TipoObrigatoriedadeEnum getTipoObrigatoriedade() {
		return tipoObrigatoriedade;
	}

	public void setTipoObrigatoriedade(TipoObrigatoriedadeEnum tipoObrigatoriedade) {
		this.tipoObrigatoriedade = tipoObrigatoriedade;
	}
        
        
}
