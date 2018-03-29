package br.com.concepter.model.beans;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.view.mxGraph;

import br.com.concepter.model.enuns.TipoEntidadeEnum;
import br.com.concepter.view.AreaGrafica;
import br.com.concepter.view.TelaPrincipal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class Agregacao {
    private Long id;
    private String nome;
    @XmlTransient
    private List<Atributo> atributos = new ArrayList<>();
    private Relacionamento relacionamento;
    
    @XmlTransient
    private mxGraph grafico;
    @XmlTransient
    private mxCell cell;
    
    private double pY;
    private double pX;
    private int tamanhoLargura;
    private int tamanhoAltura;
    
    @XmlTransient
    private HashMap<Integer, Relacionamento> mapaGraficoRelacionamentos;
    
    @XmlTransient
    private HashMap<Integer, Agregacao> mapaGraficoAgregacao;

    public Agregacao() {
    }
    
    public Agregacao(mxGraph grafico, HashMap<Integer, Agregacao> mapaGraficoAgregacao, HashMap<Integer, Relacionamento> mapaGraficoRelacionamentos, String nome, double pX, double pY){
            this.nome = nome;
            this.grafico = grafico;
            this.mapaGraficoAgregacao= mapaGraficoAgregacao;
            this.mapaGraficoRelacionamentos= mapaGraficoRelacionamentos;
            this.tamanhoLargura = 125;
            this.tamanhoAltura = 70;
            this.pX = pX;
            this.pY = pY;
    }
        	
    public void add(Object relacionamento, Object entidade){
            this.grafico.getModel().beginUpdate();
            
            Object parent = this.grafico.getDefaultParent();
            
            double posx = 0;
            double posy = 0;
            Object relacionamento2 = null;
            Object mxcell = null;
            
            try{	
                
                if (relacionamento instanceof Relacionamento){
                    posx =  ((Relacionamento)relacionamento).getCell().getGeometry().getX() ;
                    posy =  ((Relacionamento)relacionamento).getCell().getGeometry().getY() ;
                        
                    relacionamento2 = this.grafico.insertVertex(parent, null, null, posx, posy, this.tamanhoLargura, tamanhoAltura, "verticalAlign=top;fillColor=none;shape=rectangle;");
                    
                }else{
                }
                /*
                if (entidade instanceof Relacionamento){
                    posx =  ((Relacionamento)entidade).getCell().getGeometry().getX() -20;
                    posy = ((Relacionamento)entidade).getCell().getGeometry().getY() -20;
                        
                    entidade = this.grafico.insertVertex(parent, null, null, posx, posy, this.tamanhoLargura, tamanhoAltura, "verticalAlign=top;fillColor=none;shape=rectangle;");
                }else{
                	//((Entidade)entidade).getCell().getGeometry().setRect(20, 20, 100, 50);
                }
                */

                mxcell = this.grafico.addCell(((Relacionamento)relacionamento).getCell(), (mxCell)relacionamento2);
                ((mxCell)relacionamento2).getGeometry().setRect(posx, posy, 125, 70);
                ((mxCell)mxcell).setConnectable(false);
                ((Relacionamento)relacionamento).getCell().getGeometry().setX(10);
                ((Relacionamento)relacionamento).getCell().getGeometry().setY(10);
                //((mxCell)mxcell).getGeometry().setX(posx);
                //((mxCell)mxcell).getGeometry().setY(posy);
                
                //((Relacionamento)relacionamento).getCell().getGeometry().setRect(cell.getGeometry().getX(), cell.getGeometry().getY(), 100, 50);
                
                entidade = new Entidade( grafico, AreaGrafica.getMapaGraficoEntidades(), "Entidade" + AreaGrafica.getCont_entidade(), posx,posy+300, TipoEntidadeEnum.FORTE);
               ((Entidade) entidade).add();
                Relacionamento relacionamento_agregacao = new Relacionamento(grafico, mapaGraficoRelacionamentos, "Relacionamento", posx, posy);

                relacionamento_agregacao.add(((mxCell)relacionamento2),(Entidade) entidade);
            }
            finally{
                //this.mapaGraficoAgregacao.put( Integer.valueOf(((mxCell) mxcell).getId()), mxcell );
                
                this.grafico.getModel().endUpdate();
                this.grafico.refresh();
            }
    }

    public void add(Object relacionamento, Entidade entidade_1, Entidade entidade_2){
            this.grafico.getModel().beginUpdate();
            Object agregacao = null;
            Object parent = this.grafico.getDefaultParent();

            try{	
                double posx = ((mxCell) relacionamento).getGeometry().getX() -20;
                double posy = ((mxCell) relacionamento).getGeometry().getY() -20;

                agregacao = this.grafico.insertVertex(parent, null, null, posx, posy, this.tamanhoLargura, tamanhoAltura, "verticalAlign=top;fillColor=none;shape=rectangle;rounded=true;");

                ((mxCell) relacionamento).getGeometry().setRect(20, 30, 100, 50);

                this.grafico.addCell(relacionamento, agregacao);
                
                Relacionamento relacionamento_agregacao = new Relacionamento(grafico, mapaGraficoRelacionamentos, "Relacionamento", posx + 220, posy + 20);

                relacionamento_agregacao.add(this, entidade_1, entidade_2);

            }
            finally{
                this.mapaGraficoAgregacao.put( Integer.valueOf( ((mxCell) agregacao).getId() ), this ) ;
                
                this.grafico.getModel().endUpdate();
                this.grafico.refresh();
                
            }
    }

    public void add(Object relacionamento, Object entidade_1, Object entidade_2, Object entidade_3){
            this.grafico.getModel().beginUpdate();
            Object agregacao = null;
            Object parent = this.grafico.getDefaultParent();

            try{	
                double posx = ((mxCell) relacionamento).getGeometry().getX() -20;
                double posy = ((mxCell) relacionamento).getGeometry().getY() -20;

                agregacao = this.grafico.insertVertex(parent, null, null, posx, posy, this.tamanhoLargura, tamanhoAltura, "verticalAlign=top;fillColor=none;shape=rectangle;");

                ((mxCell) relacionamento).getGeometry().setRect(20, 30, 100, 50);

                this.grafico.addCell(relacionamento, agregacao);

                relacionamento =  this.grafico.insertVertex(parent, null, "Relacionamento", posx + 220, posy + 20, 100, 50, "shape=rhombus;");

                this.grafico.insertEdge(parent, null, "N-M", agregacao, relacionamento,"startArrow=none;endArrow=none;strokeColor=red;");
                this.grafico.insertEdge(parent, null, "N-M", entidade_1, relacionamento,"startArrow=none;endArrow=none;strokeColor=red;");
                this.grafico.insertEdge(parent, null, "N-M", entidade_2, relacionamento,"startArrow=none;endArrow=none;strokeColor=red;");
                this.grafico.insertEdge(parent, null, "N-M", entidade_3, relacionamento,"startArrow=none;endArrow=none;strokeColor=red;");
            }
            finally{
                this.mapaGraficoAgregacao.put( Integer.valueOf( ((mxCell) agregacao).getId() ), this ) ;
         
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

    public List<Atributo> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<Atributo> atributos) {
        this.atributos = atributos;
    }

    public Relacionamento getRelacionamento() {
        return relacionamento;
    }

    public void setRelacionamento(Relacionamento relacionamento) {
        this.relacionamento = relacionamento;
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
    @XmlTransient
    public double getpY() {
        return pY;
    }

    public void setpY(double pY) {
        this.pY = pY;
    }
    @XmlTransient
    public double getpX() {
        return pX;
    }

    public void setpX(double pX) {
        this.pX = pX;
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
    public HashMap<Integer, Relacionamento> getMapaGraficoRelacionamentos() {
        return mapaGraficoRelacionamentos;
    }

    public void setMapaGraficoRelacionamentos(HashMap<Integer, Relacionamento> mapaGraficoRelacionamentos) {
        this.mapaGraficoRelacionamentos = mapaGraficoRelacionamentos;
    }
    @XmlTransient
    public HashMap<Integer, Agregacao> getMapaGraficoAgregacao() {
        return mapaGraficoAgregacao;
    }

    public void setMapaGraficoAgregacao(HashMap<Integer, Agregacao> mapaGraficoAgregacao) {
        this.mapaGraficoAgregacao = mapaGraficoAgregacao;
    }
    
    
    
    
}