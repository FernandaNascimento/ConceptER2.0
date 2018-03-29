package br.com.concepter.model.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;

import br.com.concepter.model.enuns.TipoObrigatoriedadeEnum;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Relacionamento implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
    private String nome;
    private List<Atributo> atributos = new ArrayList<>();
    
    private HashMap<Entidade, Relacao> entidades = new HashMap<Entidade, Relacao>();
    
    private Agregacao agregacao = null;
    
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
    

    public Relacionamento() {
    }

    public Relacionamento(mxGraph grafico, HashMap<Integer, Relacionamento> mapaGraficoRelacionamentos, String nome, double pX, double pY){
            this.nome = nome;
            this.grafico = grafico;
            this.mapaGraficoRelacionamentos= mapaGraficoRelacionamentos;
            this.tamanhoLargura = 100;
            this.tamanhoAltura = 50;
            this.pX = pX;
            this.pY = pY;
    }
	
    public void add(Object entidade_agregacao, Entidade entidade_2){
            this.grafico.getModel().beginUpdate();
            Object relacionamento = null;
            Object parent = this.grafico.getDefaultParent();
            double posx = 0;
            double posy = 0;
            
            this.entidades.clear();
                    
            this.entidades.put(entidade_2, new Relacao("N", TipoObrigatoriedadeEnum.PARCIAL));
                    
                    
            //this.entidades.clear();
                    
            
            try{	
                //Object[] cells = this.grafico.getChildVertices(parent);
                
//                for (Object cell : cells) {
//                    if( ((mxCell)cell).getId() ){
//                        System.out.println("ok");
//                    } 
//                }
                
                if("br.com.concepter.model.beans.Entidade".equals(entidade_agregacao.getClass().getName())){
                	this.entidades.put((Entidade)entidade_agregacao, new Relacao("N", TipoObrigatoriedadeEnum.PARCIAL));
                	//((Entidade)entidade_agregacao).getRelacionamentos().add(this);
                    posx = ((Entidade)entidade_agregacao).getCell().getGeometry().getX()+160;
                    posy = ((Entidade)entidade_agregacao).getCell().getGeometry().getY();
                    
                    entidade_agregacao = ((Entidade)entidade_agregacao).getCell();      
                }else{
                    posx = ((mxCell)entidade_agregacao).getGeometry().getX();
                    posy = ((mxCell)entidade_agregacao).getGeometry().getY()+200;
                   
                    //this.agregacao = (Relacionamento)entidade_agregacao;
                    
                    entidade_agregacao = ((mxCell)entidade_agregacao);
                }

                relacionamento =  this.grafico.insertVertex(parent, null, this.nome, posx , posy, this.tamanhoLargura, this.tamanhoAltura, "fillColor=white;shape=rhombus;");
                                
                mxCell e1 = (mxCell) this.grafico.insertEdge(parent, null, "N", entidade_agregacao, relacionamento,"startArrow=none;endArrow=none;fontSize=15;fontStyle=1;dashed=1;");
                mxCell e2 = (mxCell)this.grafico.insertEdge(parent, null, "M", entidade_2.getCell(), relacionamento,"startArrow=none;endArrow=none;fontSize=15;fontStyle=1;dashed=1;");

                //mxPoint mx = new mxPoint();
                //mx.setX(((mxCell)relacionamento).getGeometry().getX()+((mxCell)relacionamento).getGeometry().getWidth()/2+20);
                //mx.setY(((mxCell)entidade_agregacao).getGeometry().getY()+((mxCell)entidade_agregacao).getGeometry().getHeight()*4/5);
                //List<mxPoint> list = new ArrayList<mxPoint>();
                //list.add(mx);
                //e1.getGeometry().setPoints(list);
                
                if(entidade_agregacao == entidade_2.getCell()) {
                	mxPoint mx2 = new mxPoint();
                	mx2.setX(((mxCell)relacionamento).getGeometry().getX()-((mxCell)relacionamento).getGeometry().getWidth()/2+20);
                	mx2.setY(((mxCell)entidade_2.getCell()).getGeometry().getY()-((mxCell)entidade_2.getCell()).getGeometry().getHeight()*4/5);
                	List<mxPoint> list2 = new ArrayList<mxPoint>();
                	list2.add(mx2);
                	e2.getGeometry().setPoints(list2);
                }

            }
            finally{
                
                this.cell = (mxCell)relacionamento;
                
                this.id = Integer.parseInt(((mxCell)relacionamento).getId());
                
                //entidade_2.getRelacionamentos().add(this);
                
                this.mapaGraficoRelacionamentos.put( Integer.valueOf( ((mxCell) relacionamento).getId() ), this);

                this.grafico.getModel().endUpdate();
                this.grafico.refresh();
            }
    }

    public void add(Object entidade_agregacao, Entidade entidade_2, Entidade entidade_3){
            this.grafico.getModel().beginUpdate();
            Object relacionamento = null;
            Object parent = this.grafico.getDefaultParent();
            double posx = 0;
            double posy = 0;
            
            try{	
                if("br.com.concepter.model.beans.Entidade".equals(entidade_agregacao.getClass().getName())){
                    posx = ((Entidade)entidade_agregacao).getCell().getGeometry().getX();
                    posy = ((Entidade)entidade_agregacao).getCell().getGeometry().getY();
                    
                    this.entidades.put(((Entidade)entidade_agregacao), new Relacao("N", TipoObrigatoriedadeEnum.PARCIAL));
                    
                    entidade_agregacao = ((Entidade)entidade_agregacao).getCell();      
                }else{
                    posx = ((Agregacao)entidade_agregacao).getCell().getGeometry().getX();
                    posy = ((Agregacao)entidade_agregacao).getCell().getGeometry().getY();
                   
                    this.agregacao = (Agregacao)entidade_agregacao;
                    
                    entidade_agregacao = ((Agregacao)entidade_agregacao).getCell();
                }

                relacionamento =  this.grafico.insertVertex(parent, null, this.nome, posx + 160, posy, this.tamanhoLargura, this.tamanhoAltura, "fillColor=white;shape=rhombus;");

                this.grafico.insertEdge(parent, null, "N", entidade_agregacao, relacionamento,"startArrow=none;endArrow=none;fontSize=15;fontStyle=1;dashed=1;");
                this.grafico.insertEdge(parent, null, "M", entidade_2.getCell(), relacionamento,"startArrow=none;endArrow=none;fontSize=15;fontStyle=1;dashed=1;");
                this.grafico.insertEdge(parent, null, "O", entidade_3.getCell(), relacionamento,"startArrow=none;endArrow=none;fontSize=15;fontStyle=1;dashed=1;");

            }
            finally{
                this.entidades.put(entidade_2, new Relacao("N", TipoObrigatoriedadeEnum.PARCIAL));
                this.entidades.put(entidade_3, new Relacao("M", TipoObrigatoriedadeEnum.PARCIAL));
                
                this.cell = (mxCell)relacionamento;
                
                this.id = Integer.parseInt(((mxCell)relacionamento).getId());

                this.mapaGraficoRelacionamentos.put( Integer.valueOf( ((mxCell) relacionamento).getId() ), this);

                this.grafico.getModel().endUpdate();
                this.grafico.refresh();
            }
    }

    public void add(Object entidade_agregacao, Entidade entidade_2, Entidade entidade_3, Entidade entidade_4){
            this.grafico.getModel().beginUpdate();
            Object relacionamento = null;
            Object parent = this.grafico.getDefaultParent();

            double posx = 0;
            double posy = 0;
            try{	
                if("br.com.concepter.model.beans.Entidade".equals(entidade_agregacao.getClass().getName())){
                    posx = ((Entidade)entidade_agregacao).getCell().getGeometry().getX();
                    posy = ((Entidade)entidade_agregacao).getCell().getGeometry().getY();
                    
                    this.entidades.put(((Entidade)entidade_agregacao), new Relacao("N", TipoObrigatoriedadeEnum.PARCIAL));
                    
                    entidade_agregacao = ((Entidade)entidade_agregacao).getCell();      
                }else{
                    posx = ((Agregacao)entidade_agregacao).getCell().getGeometry().getX();
                    posy = ((Agregacao)entidade_agregacao).getCell().getGeometry().getY();
                   
                    this.agregacao = (Agregacao)entidade_agregacao;
                    
                    entidade_agregacao = ((Agregacao)entidade_agregacao).getCell();
                }

                relacionamento =  this.grafico.insertVertex(parent, null, this.nome, posx + 160, posy, this.tamanhoLargura, this.tamanhoAltura, "fillColor=white;shape=rhombus;");

                this.grafico.insertEdge(parent, null, "N", entidade_agregacao, relacionamento,"startArrow=none;endArrow=none;fontSize=15;fontStyle=1;dashed=1;");
                this.grafico.insertEdge(parent, null, "M", entidade_2.getCell(), relacionamento,"startArrow=none;endArrow=none;fontSize=15;fontStyle=1;dashed=1;");
                this.grafico.insertEdge(parent, null, "O", entidade_3.getCell(), relacionamento,"startArrow=none;endArrow=none;fontSize=15;fontStyle=1;dashed=1;");
                this.grafico.insertEdge(parent, null, "P", entidade_4.getCell(), relacionamento,"startArrow=none;endArrow=none;fontSize=15;fontStyle=1;dashed=1;");
            }
            finally{
                this.entidades.put(entidade_2, new Relacao("N", TipoObrigatoriedadeEnum.PARCIAL));
                this.entidades.put(entidade_3, new Relacao("N", TipoObrigatoriedadeEnum.PARCIAL));
                this.entidades.put(entidade_4, new Relacao("N", TipoObrigatoriedadeEnum.PARCIAL));
                
                this.cell = (mxCell)relacionamento;
               
                this.id = Integer.parseInt(((mxCell)relacionamento).getId());
                
                this.mapaGraficoRelacionamentos.put( Integer.valueOf( ((mxCell) relacionamento).getId() ), this);

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

    public List<Atributo> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<Atributo> atributos) {
        this.atributos = atributos;
    }
  
    public HashMap<Entidade, Relacao> getEntidades() {
        return entidades;
    }
    
    public void setEntidades(HashMap<Entidade, Relacao> entidades) {
        this.entidades = entidades;
    }

    public Agregacao getAgregacao() {
        return agregacao;
    }

    public void setAgregacao(Agregacao agregacao) {
        this.agregacao = agregacao;
    }
    
    public mxGraph getGrafico() {
        return grafico;
    }

    public void setGrafico(mxGraph grafico) {
        this.grafico = grafico;
    }
 
    public mxCell getCell() {
        return cell;
    }

    public void setCell(mxCell cell) {
        this.cell = cell;
    }
   
    public double getpY() {
        return pY;
    }

    public void setpY(double pY) {
        this.pY = pY;
    }
    
    public double getpX() {
        return pX;
    }

    public void setpX(double pX) {
        this.pX = pX;
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
 
    public HashMap getMapaGraficoRelacionamentos() {
        return mapaGraficoRelacionamentos;
    }

    public void setMapaGraficoRelacionamentos(HashMap mapaGraficoRelacionamentos) {
        this.mapaGraficoRelacionamentos = mapaGraficoRelacionamentos;
    }

 

    
    
}