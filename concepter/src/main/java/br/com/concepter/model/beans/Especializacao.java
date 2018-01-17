/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.concepter.model.beans;

import br.com.concepter.model.enuns.TipoEntidadeEnum;
import br.com.concepter.model.enuns.TipoEspecializacaoEnum;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author AllanMagnum
 */
@XmlRootElement
public class Especializacao {
    
    private Long id;
    private String nome;
    private TipoEspecializacaoEnum tipoEspecializacao; 
    private List<Entidade> entidades = new ArrayList<Entidade>();

    private HashMap<Integer, Especializacao> mapaGraficoEspecializacao = new HashMap<Integer, Especializacao>();

    private mxGraph grafico;
    private double pX;
    private double pY;
    private int tamanhoLargura;
    private int tamanhoAltura;
    private mxCell cell;

    public Especializacao() {
    }
    
    public Especializacao(mxGraph grafico, HashMap<Integer, Especializacao> mapaGraficoEspecializacao, double pX, double pY, TipoEspecializacaoEnum tipoEspecializacao){
            this.grafico = grafico;
            this.mapaGraficoEspecializacao = mapaGraficoEspecializacao;
            this.tamanhoLargura = 20;
            this.tamanhoAltura = 20;
            this.pX = pX;
            this.pY = pY + 50;
            this.tipoEspecializacao = tipoEspecializacao;
    }

    public void add(Entidade entidade, HashMap<Integer, Entidade> mapaGraficoEntidades, Integer contEntidade){
        this.grafico.getModel().beginUpdate();
        Object parent = this.grafico.getDefaultParent();
        
        if(tipoEspecializacao == TipoEspecializacaoEnum.DISJUNCAO){
            nome = "d";
        }
        if(tipoEspecializacao == TipoEspecializacaoEnum.SOBREPOSICAO){
            nome = "o";
        }
        
        Double posx = entidade.getCell().getGeometry().getX();
        Double posy = entidade.getCell().getGeometry().getY();

        try{	
            this.cell = (mxCell) this.grafico.insertVertex(parent, null, this.nome, posx+40, posy+100, this.tamanhoLargura, this.tamanhoAltura, "fillColor=white;shape=ellipse;");
            
            Entidade entidade_1 = new Entidade(this.grafico, mapaGraficoEntidades, "Entidade" + contEntidade, posx - 125, posy + 170, TipoEntidadeEnum.FORTE);
            entidade_1.add();
            
            Entidade entidade_2 = new Entidade(this.grafico, mapaGraficoEntidades, "Entidade" + ++contEntidade, posx + 125, posy + 170, TipoEntidadeEnum.FORTE);
            entidade_2.add();
            
            this.entidades.add(entidade_1);
            this.entidades.add(entidade_2);
            
            entidade.setEspecializacao(this);
            
            this.grafico.insertEdge(parent, null, "", this.cell, entidade.getCell(),"startArrow=none;endArrow=none;dashed=1;");
            
            mxStylesheet stylesheet = this.grafico.getStylesheet();
            
            String myStyleName = "myImageStyle";

            
            // define image style           
            Hashtable<String, Object> style = new Hashtable<String, Object>();
            style.put( mxConstants.STYLE_SHAPE, mxConstants.SHAPE_IMAGE);
            style.put( mxConstants.STYLE_IMAGE, "file:/c:/images/estarContido.png");
            style.put( mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);

            stylesheet.putCellStyle( myStyleName, style);
            
            this.grafico.insertEdge(parent, null, "U", entidade_1.getCell(), this.cell,"startArrow=none;endArrow=none;");
            this.grafico.insertEdge(parent, null, "U", entidade_2.getCell(), this.cell,"startArrow=none;endArrow=none;");
        }

        finally{
            this.mapaGraficoEspecializacao.put( Integer.valueOf( this.cell.getId() ), this);
            this.grafico.getModel().endUpdate();
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

    public HashMap<Integer, Especializacao> getMapaGraficoEspecializacao() {
        return mapaGraficoEspecializacao;
    }

    public void setMapaGraficoEspecializacao(HashMap<Integer, Especializacao> mapaGraficoEspecializacao) {
        this.mapaGraficoEspecializacao = mapaGraficoEspecializacao;
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
        
        
}
