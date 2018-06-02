package br.com.concepter.model.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

import br.com.concepter.view.AreaGrafica;

@XmlRootElement()
public class Agregacao {
    private Long id;
    private String nome;
    
    private List<Atributo> atributos = new ArrayList<>();
    private Relacionamento relacionamentoAgregacao;
    private Relacionamento relacionamentoAgregado;
    
    @XmlTransient
    private mxGraph grafico;
    @XmlTransient
    private mxCell cell;
    
    private double pY;
    private double pX;
    private int tamanhoLargura;
    private int tamanhoAltura;
        
    public Agregacao() {
	}

	public Agregacao(mxGraph grafico,  String nome, double pX, double pY){
            this.nome = nome;
            this.grafico = grafico;
            this.tamanhoLargura = 125;
            this.tamanhoAltura = 70;
            this.pX = pX;
            this.pY = pY;
    }
        	
    public void add(Object relacionamento, Entidade entidade){
            this.grafico.getModel().beginUpdate();
            
            Object parent = this.grafico.getDefaultParent();
            
            double posx = 0;
            double posy = 0;
            Object relacionamento2 = null;
            
            try{	
                
                if (relacionamento instanceof Relacionamento){
                    posx =  ((Relacionamento)relacionamento).getCell().getGeometry().getX() ;
                    posy =  ((Relacionamento)relacionamento).getCell().getGeometry().getY() ;
                    relacionamento2 = this.grafico.insertVertex(parent, null, null, posx, posy, this.tamanhoLargura, tamanhoAltura, "verticalAlign=top;fillColor=none;shape=rectangle;");
                }

                 this.grafico.addCell(((Relacionamento)relacionamento).getCell(), (mxCell)relacionamento2);
                 
                ((mxCell)relacionamento2).getGeometry().setRect(posx, posy, 125, 70);
                ((Relacionamento)relacionamento).getCell().getGeometry().setX(10);
                ((Relacionamento)relacionamento).getCell().getGeometry().setY(10);
                this.cell = ((mxCell)relacionamento2);
                                
                Relacionamento relacionamentoAgregado = new Relacionamento(grafico,  "Relacionamento", posx, posy);

                relacionamentoAgregado.add(this,(Entidade) entidade);
                this.setRelacionamentoAgregado(relacionamentoAgregado);
                this.setRelacionamentoAgregacao((Relacionamento) relacionamento);
                
            }
            finally{
                AreaGrafica.getMapaGraficoAgregacao().put( Integer.valueOf(((mxCell) relacionamento2).getId()), this);
                
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
    
    public Relacionamento getRelacionamentoAgregacao() {
		return relacionamentoAgregacao;
	}

	public void setRelacionamentoAgregacao(Relacionamento relacionamentoAgregacao) {
		this.relacionamentoAgregacao = relacionamentoAgregacao;
	}

	public Relacionamento getRelacionamentoAgregado() {
		return relacionamentoAgregado;
	}

	public void setRelacionamentoAgregado(Relacionamento relacionamentoAgregado) {
		this.relacionamentoAgregado = relacionamentoAgregado;
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
    
    
    
    
}