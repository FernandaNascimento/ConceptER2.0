/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.concepter.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.apache.commons.lang3.StringUtils;

import com.mxgraph.model.mxCell;
import com.mxgraph.shape.mxITextShape;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxInteractiveCanvas;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

import br.com.concepter.model.beans.Agregacao;
import br.com.concepter.model.beans.Atributo;
import br.com.concepter.model.beans.Entidade;
import br.com.concepter.model.beans.Especializacao;
import br.com.concepter.model.beans.Relacao;
import br.com.concepter.model.beans.Relacionamento;
import br.com.concepter.model.enuns.TipoAtributoEnum;
import br.com.concepter.model.enuns.TipoEspecializacaoEnum;
import br.com.concepter.model.enuns.TipoObrigatoriedadeEnum;


public class AreaGrafica extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private static mxGraph grafico;
	private static mxGraphComponent areaGrafico;
	private static Integer cont_entidade = 0;
	private static Integer cont_relacionamento = 0;
	private static Integer cont_atributo = new Integer(0);

	public static Integer maxEntAtr = 20;

	private static HashMap<Integer, Entidade> mapaGraficoEntidades = new HashMap<Integer, Entidade>();
	private static HashMap<Integer, Atributo> mapaGraficoAtributos = new HashMap<Integer, Atributo>();
	private static HashMap<Integer, Relacionamento> mapaGraficoRelacionamentos = new HashMap<Integer, Relacionamento>();
	private static HashMap<Integer, Especializacao> mapaGraficoEspecializacao = new HashMap<Integer, Especializacao>();
	private static HashMap<Integer, Agregacao> mapaGraficoAgregacao = new HashMap<Integer, Agregacao>();

	private Object cell;
	private Entidade entidade_1 = null;
	private Entidade entidade_2 = null;
	private Entidade entidade_3 = null;
	private Entidade entidade_4 = null;
	private Relacionamento relacionamento = null;

	private JPopupMenu menuPopup;
	private JMenu menuCompletude;
	private JMenu menuCardinalidade;
	private JMenuItem menuItemDelete;
	private JMenuItem menuItemEspecializacao;
	private JMenu menuAgregacao;
	private JMenu menuTotalidade;
	private JMenu menuParcialidade;
	private JMenu menuNn;
	private JMenu menu1n;
	private static double px, py;
	mxUndoManager undoMgr;

	public AreaGrafica(){
		AreaGrafica.grafico = new mxGraph();
		undoMgr = new mxUndoManager();

		mxEventSource.mxIEventListener listener = new mxEventSource.mxIEventListener() {
			@Override
			public void invoke(Object sender, mxEventObject evt) {
				undoMgr.undoableEditHappened((mxUndoableEdit) evt.getProperty("edit"));
			}
		};


		AreaGrafica.areaGrafico = new mxGraphComponent(grafico) {
				/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				public mxInteractiveCanvas createCanvas()
	    {
	        return new mxInteractiveCanvas(){
	            @Override
	            public Object drawLabel(String text, mxCellState state, boolean html)
	            {
	                Map<String, Object> style = state.getStyle();
	                //mxIShape shapeL = getShape(style);
	                mxITextShape shape = getTextShape(style, html);
	                if (g != null && shape != null && drawLabels && text != null
	                        && text.length() > 0)
	                {
	                    // Creates a temporary graphics instance for drawing this shape
	                    float opacity = mxUtils.getFloat(style,
	                            mxConstants.STYLE_TEXT_OPACITY, 100);
	                    Graphics2D previousGraphics = g;
	                    if(((mxCell) state.getCell()).isVertex() || !text.equalsIgnoreCase("U")){
	                        g = createTemporaryGraphics(style, opacity, null);                                  
	                    }else{
	                        //quadrant will be true if the edge is NE or SW
	                        Object target = ((mxCell) state.getCell()).getTarget();
	                        Object source = ((mxCell) state.getCell()).getSource();
	                        boolean quadrant1 = false;
	                        boolean quadrant2 = false;
	                        boolean quadrant3 = false;
	                        boolean quadrant4 = false;

	                        if(((mxCell)target).getGeometry().getCenterX()<((mxCell)source).getGeometry().getCenterX()){
	                            if(((mxCell)target).getGeometry().getCenterY()>((mxCell)source).getGeometry().getCenterY()){
	                            	quadrant1 =  true;
	                            }
	                        }
	                        if(((mxCell)target).getGeometry().getCenterX()>((mxCell)source).getGeometry().getCenterX()){
	                            if(((mxCell)target).getGeometry().getCenterY()>((mxCell)source).getGeometry().getCenterY()){
	                              	
	                                quadrant2 =  true;
	                            }
	                        }
	                        if(((mxCell)target).getGeometry().getCenterX()>((mxCell)source).getGeometry().getCenterX()){
	                            if(((mxCell)target).getGeometry().getCenterY()<((mxCell)source).getGeometry().getCenterY()){
	                              	
	                                quadrant3 =  true;
	                            }
	                        }
	                        if(((mxCell)target).getGeometry().getCenterX()<((mxCell)source).getGeometry().getCenterX()){
	                            if(((mxCell)target).getGeometry().getCenterY()<((mxCell)source).getGeometry().getCenterY()){
	                              	
	                                quadrant4 =  true;
	                            }
	                        }
	                        g = createTemporaryGraphics(style, opacity, state, state.getLabelBounds(),quadrant1, quadrant2, quadrant3,quadrant4);
	                    }

	                    // Draws the label background and border
	                    Color bg = mxUtils.getColor(style,
	                            mxConstants.STYLE_LABEL_BACKGROUNDCOLOR);
	                    Color border = mxUtils.getColor(style,
	                            mxConstants.STYLE_LABEL_BORDERCOLOR);
	                    paintRectangle(state.getLabelBounds().getRectangle(), bg, border);

	                    // Paints the label and restores the graphics object
	                    shape.paintShape(this, text, state, style);
	                    g.dispose();
	                    g = previousGraphics;
	                }

	                return shape;
	            }
	            public Graphics2D createTemporaryGraphics(Map<String, Object> style,
	                    float opacity, mxRectangle bounds, mxRectangle labelbounds, boolean quad1, boolean quad2, boolean quad3, boolean quad4)
	            {
	                Graphics2D temporaryGraphics = (Graphics2D) g.create();

	                // Applies the default translate
	                temporaryGraphics.translate(translate.getX(), translate.getY());

	                // setup the rotation for the label
	                double angle = 0;
	                double rotation =0;
	                //System.out.println("Angulo "+angle);
	                //System.out.println("Quadrante "+quad);
	                if(quad1) {
	                	angle = java.lang.Math.atan(bounds.getHeight()/bounds.getWidth());
	                	rotation = Math.toDegrees(-angle)-90;
	                } else if (quad2) {
	                	angle = java.lang.Math.atan(bounds.getHeight()/bounds.getWidth());
	                	rotation = Math.toDegrees(angle)+90;
	                } else if (quad3) {
	                	angle = java.lang.Math.atan(bounds.getWidth()/bounds.getHeight());
	                	rotation = Math.toDegrees(angle);
	                } else if (quad4) {
	                	angle = java.lang.Math.atan(bounds.getWidth()/bounds.getHeight());
	                	rotation = Math.toDegrees(-angle);
	                }
	               
	                // Applies the rotation and translation on the graphics object
	                if (bounds != null)
	                {
	                    if (rotation != 0)
	                    {
	                        temporaryGraphics.rotate(Math.toRadians(rotation),
	                        		labelbounds.getCenterX(), labelbounds.getCenterY());
	                        
	                    }
	                }

	                // Applies the opacity to the graphics object
	                if (opacity != 100)
	                {
	                    temporaryGraphics.setComposite(AlphaComposite.getInstance(
	                            AlphaComposite.SRC_OVER, opacity / 100));
	                }

	                return temporaryGraphics;
	            }
	        };
	    }

	};

		//Quando um evento UNDO acontecer o mesmo será memorizado
		AreaGrafica.areaGrafico.getGraph().getModel().addListener(mxEvent.UNDO, listener);
		AreaGrafica.areaGrafico.getGraph().getView().addListener(mxEvent.UNDO, listener);
		
		AreaGrafica.grafico.setMinimumGraphSize(new mxRectangle(83,0,500,200));
		AreaGrafica.areaGrafico.setPreferredSize(new Dimension(500,200));
		AreaGrafica.areaGrafico.setLocation(83,0);
		AreaGrafica.areaGrafico.getGraphControl().addMouseListener(new ObjetoMer_Selecionado());
		AreaGrafica.areaGrafico.getGraphControl().addMouseListener(new BotaoEsquerdoCliqueGrafico());
		AreaGrafica.areaGrafico.getGraphControl().addMouseListener(new ObjetoMer_RigthClick());
				
		AreaGrafica.areaGrafico.putClientProperty("DropAllowed", Boolean.TRUE);

		AreaGrafica.areaGrafico.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if(e.getKeyCode()==KeyEvent.VK_DELETE) {
					deleteItem();
				}

			}


		});


		AreaGrafica.grafico.setEdgeLabelsMovable(false);   //Nao permite que a descricao da seta seja movida
		AreaGrafica.grafico.setCellsDisconnectable(false); //Nao Permite que as setas sejam desconectadas
		//this.grafico.setCellsResizable(false);      //Nao Permite que os objetos sejam redimensionados

		areaGrafico.setConnectable(false); // Nao permite que setas sejam criadas de dentro de um objeto
		areaGrafico.getGraphHandler().setRemoveCellsFromParent(false); //Nao permiti que os objetos sejam removidos do objetos pai

		AreaGrafica.grafico.setAllowDanglingEdges(false);
		AreaGrafica.grafico.setAllowLoops(false);
		AreaGrafica.grafico.setAllowNegativeCoordinates(false);

		AreaGrafica.grafico.setCellsBendable(false);
		AreaGrafica.grafico.setSplitEnabled(true);
		AreaGrafica.grafico.setKeepEdgesInForeground(false);
		AreaGrafica.grafico.setKeepEdgesInBackground(true);


		getConfig();

		this.add(areaGrafico);

		//Menu popup para Objetos Mer
		menuPopup = new JPopupMenu();

		menuCompletude = new JMenu("Completude");
		menuCardinalidade = new JMenu("Cardinalidade");

		//Item para o Menu popup de Objetos Mer
		menuItemDelete = new JMenuItem("Delete");
		menuItemEspecializacao = new JMenu();


		menuAgregacao = new JMenu("Agregação");

		menuParcialidade = new JMenu("Parcialidade");
		menuTotalidade = new JMenu("Totalidade");

		menuNn = new JMenu("N - N");
		menu1n = new JMenu("1 - N");

		menuPopup.add(menuItemDelete);

		menuItemDelete.addActionListener(new BotaoDeletarPopupMenu());
	}


	public mxGraph getGrafico(){
		return grafico;
	}

	public mxGraphComponent getAreaGrafico(){
		return areaGrafico;
	}

	public void setAreaGrafico(mxGraphComponent areaGrafico) {
		AreaGrafica.areaGrafico = areaGrafico;
	}

	public double getPx(){
		return px;
	}

	public double getPy(){
		return py;
	}

	public static Integer getCont_entidade() {
		return cont_entidade;
	}

	public Integer getCont_relacionamento() {
		return cont_relacionamento;
	}

	public static Integer getCont_atributo() {
		return cont_atributo;
	}

	public static void setCont_atributo() {
		cont_atributo++;
	}
	public static void setCont_atributo(Integer contAtributo) {
		cont_atributo = contAtributo;
	}

	public static void setCont_entidade(Integer cont_entidade) {
		AreaGrafica.cont_entidade = cont_entidade;
	}
	public static void setCont_entidade() {
		cont_entidade++;
	}

	public static void setCont_relacionamento(Integer cont_relacionamento) {
		AreaGrafica.cont_relacionamento = cont_relacionamento;
	}

	public static void setMapaGraficoEntidades(HashMap<Integer, Entidade> mapaGraficoEntidades) {
		AreaGrafica.mapaGraficoEntidades = mapaGraficoEntidades;
	}

	public static void setMapaGraficoAtributos(HashMap<Integer, Atributo> mapaGraficoAtributos) {
		AreaGrafica.mapaGraficoAtributos = mapaGraficoAtributos;
	}

	public static void setMapaGraficoRelacionamentos(HashMap<Integer, Relacionamento> mapaGraficoRelacionamentos) {
		AreaGrafica.mapaGraficoRelacionamentos = mapaGraficoRelacionamentos;
	}

	public static void setMapaGraficoEspecializacao(HashMap<Integer, Especializacao> mapaGraficoEspecializacao) {
		AreaGrafica.mapaGraficoEspecializacao = mapaGraficoEspecializacao;
	}

	public static void setMapaGraficoAgregacao(HashMap<Integer, Agregacao> mapaGraficoAgregacao) {
		AreaGrafica.mapaGraficoAgregacao = mapaGraficoAgregacao;
	}

	public static HashMap<Integer, Entidade> getMapaGraficoEntidades() {
		return mapaGraficoEntidades;
	}

	public static HashMap<Integer, Atributo> getMapaGraficoAtributos() {
		return mapaGraficoAtributos;
	}

	public static HashMap<Integer, Relacionamento> getMapaGraficoRelacionamentos() {
		return mapaGraficoRelacionamentos;
	}

	public static HashMap<Integer, Especializacao> getMapaGraficoEspecializacao() {
		return mapaGraficoEspecializacao;
	}

	public static HashMap<Integer, Agregacao> getMapaGraficoAgregacao() {
		return mapaGraficoAgregacao;
	}

	private void getConfig(){
		try{
			Properties properties = new Properties();
			properties.load(getClass().getResourceAsStream("/br/com/concepter/properties/config.properties"));
			properties.getProperty("grade");
		}catch(IOException e){
			JOptionPane.showMessageDialog(null, "Não foi possível ler ''config.properties''\n","Exception",JOptionPane.ERROR_MESSAGE);
		}
	}



	private void limparMenuPopup(){
		menuCompletude.removeAll();
		menuCardinalidade.removeAll();

		menuParcialidade.removeAll();
		menuTotalidade.removeAll();

		menu1n.removeAll();
		menuNn.removeAll();

		menuItemEspecializacao.removeAll();

		menuPopup.remove(menuCompletude);
		menuPopup.remove(menuCardinalidade);
		menuPopup.remove(menuAgregacao);
		menuPopup.remove(menuItemEspecializacao);
	}

	public static void preencherGrade(Long valor){
		if(valor == 0){
			grafico.setGridEnabled(false);
			areaGrafico.setGridVisible(false);
			areaGrafico.setGridStyle(3);
		}else{
			grafico.setGridEnabled(true);
			areaGrafico.setGridVisible(true);
			areaGrafico.setGridStyle(3);
		}

		areaGrafico.refresh();
	}

	public static Integer getMaxEntAtr() {
		return maxEntAtr;
	}

	public static void setMaxEntAtr(Integer maxEntAtr) {
		AreaGrafica.maxEntAtr = maxEntAtr;
	}

	public static boolean hasGrade() {
		if(grafico.isGridEnabled() && areaGrafico.isGridVisible()) {
			return true;
		} else {
			return false;
		}
	}

	public void deleteItem() {
		boolean isRelacionamento = false;
		boolean isEntidade = false;
		boolean isAtributo = false;
		boolean isAgregacao = false;
		boolean isEspecializaco = false;

		Entidade entidadeSelecionado = null;
		Relacionamento relacionamentoSelecionado = null;
		Atributo atributoSelecionado = null;
		Agregacao agregacaoSelecionado = null;
		Especializacao especializacaoSelecionado = null;
		grafico.getModel().beginUpdate();

		if(cell != null ){
			relacionamentoSelecionado = (Relacionamento) mapaGraficoRelacionamentos.get( Integer.parseInt( ( (mxCell) cell ).getId() ));
			entidadeSelecionado = (Entidade) mapaGraficoEntidades.get( Integer.parseInt( ( (mxCell) cell ).getId() ) );
			atributoSelecionado =  (Atributo) mapaGraficoAtributos.get( Integer.parseInt( ( (mxCell) cell ).getId() ) );
			agregacaoSelecionado =  (Agregacao) mapaGraficoAgregacao.get( Integer.parseInt( ( (mxCell) cell ).getId() ) );
			especializacaoSelecionado =  (Especializacao) mapaGraficoEspecializacao.get( Integer.parseInt( ( (mxCell) cell ).getId() ) );
		}

		if(relacionamentoSelecionado != null){
			isRelacionamento = true;
			if(!relacionamentoSelecionado.getAtributos().isEmpty()) {
				for (Atributo atributo : relacionamentoSelecionado.getAtributos()) {
					grafico.getModel().remove(atributo.getForma());
				}
			}
			if(relacionamentoSelecionado.isHasAgregacao()) {
				Iterator<Agregacao> i = mapaGraficoAgregacao.values().iterator();
				while(i.hasNext()) {
					Agregacao a =  i.next();
					if(a.getRelacionamentoAgregacao()==relacionamentoSelecionado || a.getRelacionamentoAgregado()==relacionamentoSelecionado) {
						Object[] ob = new Object[] {a.getRelacionamentoAgregacao().getCell()};
						grafico.removeCellsFromParent(ob);
						grafico.getModel().remove(a.getCell());
					}
				}

			}

			mapaGraficoRelacionamentos.remove(Integer.parseInt( ( (mxCell) cell ).getId() ));
		}

		if(entidadeSelecionado != null){
			isEntidade = true;

		}

		if(agregacaoSelecionado != null){
			isAgregacao = true;

			grafico.getModel().remove(agregacaoSelecionado.getRelacionamentoAgregacao().getCell());

			grafico.getModel().remove(agregacaoSelecionado.getRelacionamentoAgregado().getCell());
			if(!agregacaoSelecionado.getAtributos().isEmpty()) {
				for (Atributo atributo : agregacaoSelecionado.getAtributos()) {
					grafico.getModel().remove(atributo.getForma());
				}
			}

			mapaGraficoAgregacao.remove(Integer.parseInt( ( (mxCell) cell ).getId() ));
		}

		if(atributoSelecionado != null){
			isAtributo = true;
			mapaGraficoAtributos.remove(Integer.parseInt( ( (mxCell) cell ).getId() ));
		}

		if(especializacaoSelecionado != null){
			isEspecializaco = true;
			mapaGraficoEspecializacao.remove(Integer.parseInt( ( (mxCell) cell ).getId() ));
		}
		if(isEntidade && !entidadeSelecionado.getAtributos().isEmpty()) {
			for (Atributo atributo : entidadeSelecionado.getAtributos()) {
				if(!atributo.getTipoAtributo().equals(TipoAtributoEnum.SIMPLES)) {

				}
				grafico.getModel().remove(atributo.getForma());
			}
		} 
		if (isEntidade && entidadeSelecionado.hasRelacionamento()) {
			Collection<Relacionamento> relacionamentos = mapaGraficoRelacionamentos.values();
			Iterator<Relacionamento> i = relacionamentos.iterator();
			while(i.hasNext()) {
				Relacionamento relacionamento = i.next();
				if(relacionamento.getEntidades().containsKey(entidadeSelecionado)){
					grafico.getModel().remove(relacionamento.getCell());

				}
			}
		}
		if(isEntidade) {
			mapaGraficoEntidades.remove(Integer.parseInt( ( (mxCell) cell ).getId() ));
		}

		if(isRelacionamento || isAgregacao || isAtributo || isEntidade || isEspecializaco || isRelacionamento){
			grafico.getModel().remove(cell);

		}
		grafico.getModel().endUpdate();
		grafico.refresh();
	}

	public class BotaoDeletarPopupMenu implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			deleteItem();
		}
	}


	public class BotaoBinarioPopupMenu implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			TelaPrincipal.setBotao(7);
		}    
	}

	public class TrocaEspecializacao implements ActionListener {

		private Especializacao especializacao;

		public TrocaEspecializacao(Especializacao especializacao) {
			this.especializacao = especializacao;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			getAreaGrafico().getGraph().getModel().beginUpdate();
			if(especializacao.getTipoEspecializacao() == TipoEspecializacaoEnum.DISJUNCAO) {
				especializacao.getCell().setValue("o");
				especializacao.setTipoEspecializacao(TipoEspecializacaoEnum.SOBREPOSICAO);

			} else {
				especializacao.getCell().setValue("d");
				especializacao.setTipoEspecializacao(TipoEspecializacaoEnum.DISJUNCAO);
			}
			getAreaGrafico().getGraph().getModel().endUpdate();
			getAreaGrafico().refresh();
		}    
	}

	public class OnClickJMenuItemComplitude implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			mxCell relacionamento_cell = (mxCell)cell;

			JMenuItem menuItem = ((JMenuItem) e.getSource());
			JMenu parent = (JMenu) ((JPopupMenu) menuItem.getParent()).getInvoker();

			for (int i = 0; i < relacionamento_cell.getEdgeCount(); i++) {
				String entidade_str;
				Entidade entidade;
				Especializacao especializacao = mapaGraficoEspecializacao.get( Integer.parseInt( ( (mxCell) relacionamento_cell ).getId() ));
				if(especializacao!=null){
					entidade_str = ((mxCell)relacionamento_cell.getEdgeAt(i)).getTarget().getValue().toString();
					entidade = getMapaGraficoEntidades().get( Integer.valueOf( ((mxCell)relacionamento_cell.getEdgeAt(i)).getTarget().getId()));
					if( entidade_str.equals(parent.getText()) ){
						getAreaGrafico().getGraph().getModel().beginUpdate();
						if( "Total".equals(((JMenuItem)e.getSource()).getText()) ){
							relacionamento_cell.getEdgeAt(i).setStyle("startArrow=none;endArrow=none;fontSize=15;fontStyle=1;");
							especializacao.setTipoObrigatoriedade(TipoObrigatoriedadeEnum.TOTAL);
						}else{
							relacionamento_cell.getEdgeAt(i).setStyle("startArrow=none;endArrow=none;fontSize=15;fontStyle=1;dashed=1;");
							especializacao.setTipoObrigatoriedade(TipoObrigatoriedadeEnum.PARCIAL);
						}
						getAreaGrafico().getGraph().getModel().endUpdate();
						getAreaGrafico().refresh();
						return;
					}    

				} else {
					entidade_str = ((mxCell)relacionamento_cell.getEdgeAt(i)).getSource().getValue().toString();
					entidade = getMapaGraficoEntidades().get( Integer.valueOf( ((mxCell)relacionamento_cell.getEdgeAt(i)).getSource().getId()));
					if( entidade_str.equals(parent.getText()) ){
						getAreaGrafico().getGraph().getModel().beginUpdate();
						if( "Total".equals(((JMenuItem)e.getSource()).getText()) ){
							relacionamento_cell.getEdgeAt(i).setStyle("startArrow=none;endArrow=none;fontSize=15;fontStyle=1;");
							Relacionamento relacionamento = getMapaGraficoRelacionamentos().get(Integer.parseInt(relacionamento_cell.getId()));
							Relacao relacao = relacionamento.getEntidades().get(entidade);
							relacao.setObrigatoriedade(TipoObrigatoriedadeEnum.TOTAL);
						}else{
							relacionamento_cell.getEdgeAt(i).setStyle("startArrow=none;endArrow=none;fontSize=15;fontStyle=1;dashed=1;");
							Relacionamento relacionamento = getMapaGraficoRelacionamentos().get(Integer.parseInt(relacionamento_cell.getId()));
							Relacao relacao = relacionamento.getEntidades().get(entidade);
							relacao.setObrigatoriedade(TipoObrigatoriedadeEnum.PARCIAL);
						}
						getAreaGrafico().getGraph().getModel().endUpdate();
						getAreaGrafico().refresh();
						return;
					}    
				}

			}


		}
	}

	public class OnClickJMenuItemCardinalidade implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			mxCell relacionamento_cell = (mxCell)cell;

			JMenuItem menuItem = ((JMenuItem) e.getSource());
			JMenu parent = (JMenu) ((JPopupMenu) menuItem.getParent()).getInvoker();

			for (int i = 0; i < relacionamento_cell.getEdgeCount(); i++) {
				String entidade_str = ((mxCell)relacionamento_cell.getEdgeAt(i)).getSource().getValue().toString();
				Entidade entidade = getMapaGraficoEntidades().get( Integer.valueOf( ((mxCell)relacionamento_cell.getEdgeAt(i)).getSource().getId() ));

				if( entidade_str.equals(parent.getText()) ){

					Relacionamento relacionamento = getMapaGraficoRelacionamentos().get(Integer.parseInt(relacionamento_cell.getId()));
					Relacao relacao = relacionamento.getEntidades().get(entidade);
					relacao.setCardinalidade(new String(((JMenuItem)e.getSource()).getText()));

					relacionamento_cell.getEdgeAt(i).setValue(new String(((JMenuItem)e.getSource()).getText()));

					getAreaGrafico().refresh();
				}    
			}
		}
	}

	public class ObjetoMer_RigthClick extends MouseAdapter {	
		@Override
		public void mouseClicked(MouseEvent e){
			if(e.getButton() == 3){
				boolean isRelacionamento = false;

				Relacionamento relacionamentoSelecionado = null;
				Especializacao especializacaoSelecionado = null;

				limparMenuPopup();

				cell = areaGrafico.getCellAt(e.getX(), e.getY());

				if(cell != null ){
					relacionamentoSelecionado = (Relacionamento) mapaGraficoRelacionamentos.get( Integer.parseInt( ( (mxCell) areaGrafico.getCellAt(e.getX(), e.getY()) ).getId() ));
					especializacaoSelecionado =  (Especializacao) mapaGraficoEspecializacao.get( Integer.parseInt( ( (mxCell) areaGrafico.getCellAt(e.getX(), e.getY()) ).getId() ) );

					if(relacionamentoSelecionado != null){
						isRelacionamento = true;
					}

					if(isRelacionamento){
						menuPopup.add(menuCompletude);
						menuPopup.add(menuCardinalidade);
						Set<Entidade> entidades = relacionamentoSelecionado.getEntidades().keySet();
						for (Entidade entidade : entidades) {

							JMenu jmCardinalidade = new JMenu(entidade.getCell().getValue().toString());
							JMenuItem jmi1 = new JMenuItem("1");
							JMenuItem jmiN = new JMenuItem("N");
							jmCardinalidade.add(jmi1);
							jmCardinalidade.add(jmiN);
							jmi1.addActionListener(new OnClickJMenuItemCardinalidade());
							jmiN.addActionListener(new OnClickJMenuItemCardinalidade());

							JMenu jmComplitude = new JMenu(entidade.getCell().getValue().toString());
							JMenuItem jmiTotal = new JMenuItem("Total");
							JMenuItem jmiParcil = new JMenuItem("Parcial");
							jmComplitude.add(jmiParcil);
							jmComplitude.add(jmiTotal);
							jmiTotal.addActionListener(new OnClickJMenuItemComplitude());
							jmiParcil.addActionListener(new OnClickJMenuItemComplitude());

							menuCardinalidade.add(jmCardinalidade);
							menuCompletude.add(jmComplitude);
						}

					}
					if(especializacaoSelecionado!=null) {
						menuPopup.add(menuCompletude);
						if(especializacaoSelecionado.getTipoEspecializacao()==TipoEspecializacaoEnum.DISJUNCAO) {
							menuItemEspecializacao = new JMenuItem(TipoEspecializacaoEnum.SOBREPOSICAO.toString());
							menuItemEspecializacao.addActionListener(new TrocaEspecializacao(especializacaoSelecionado));
							menuPopup.add(menuItemEspecializacao);							
						} else if(especializacaoSelecionado.getTipoEspecializacao()==TipoEspecializacaoEnum.SOBREPOSICAO) {
							menuItemEspecializacao = new JMenuItem(TipoEspecializacaoEnum.DISJUNCAO.toString());
							menuItemEspecializacao.addActionListener(new TrocaEspecializacao(especializacaoSelecionado));
							menuPopup.add(menuItemEspecializacao);
						}

						JMenu jmComplitude = new JMenu(especializacaoSelecionado.getEntidadePrincipal().getCell().getValue().toString());
						JMenuItem jmiTotal = new JMenuItem("Total");
						JMenuItem jmiParcil = new JMenuItem("Parcial");
						jmComplitude.add(jmiParcil);
						jmComplitude.add(jmiTotal);
						jmiTotal.addActionListener(new OnClickJMenuItemComplitude());
						jmiParcil.addActionListener(new OnClickJMenuItemComplitude());

						menuCompletude.add(jmComplitude);

					}

					menuPopup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}
	}

	public class ObjetoMer_Selecionado extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e){
			cell = areaGrafico.getCellAt(e.getX(), e.getY());
		}


	}

	public class BotaoEsquerdoCliqueGrafico extends MouseAdapter{

		@Override
		public void mouseClicked(MouseEvent e){
			//System.out.println("Clicou! X- "+e.getX()+" Y- "+e.getY());
			boolean isRelacionamento = false;
			boolean isEntidade = false;
			boolean isAtributo = false;
			boolean isAgregacao = false;
			boolean isEspecializacao = false;

			Entidade entidadeSelecionado = null;
			Relacionamento relacionamentoSelecionado = null;
			Atributo atributoSelecionado = null;
			Agregacao agregacaoSelecionado = null;
			Especializacao especializacaoSelecionado = null;

			if(e.getButton() == MouseEvent.BUTTON1){

				if(px == 0 && py == 0){
					px = e.getX();
					py = e.getY();
				}
				if(e.getX()<=0 || e.getY()<=0) {
					JOptionPane.showMessageDialog(null, "Você não pode colocar aqui!");
				}

				if(areaGrafico.getCellAt(e.getX(), e.getY()) != null){
					relacionamentoSelecionado = (Relacionamento) mapaGraficoRelacionamentos.get( Integer.parseInt( ( (mxCell) areaGrafico.getCellAt(e.getX(), e.getY()) ).getId() ));
					entidadeSelecionado = (Entidade) mapaGraficoEntidades.get( Integer.parseInt( ( (mxCell) areaGrafico.getCellAt(e.getX(), e.getY()) ).getId() ) );
					atributoSelecionado =  (Atributo) mapaGraficoAtributos.get( Integer.parseInt( ( (mxCell) areaGrafico.getCellAt(e.getX(), e.getY()) ).getId() ) );
					agregacaoSelecionado =  (Agregacao) mapaGraficoAgregacao.get( Integer.parseInt( ( (mxCell) areaGrafico.getCellAt(e.getX(), e.getY()) ).getId() ) );
					especializacaoSelecionado =  (Especializacao) mapaGraficoEspecializacao.get( Integer.parseInt( ( (mxCell) areaGrafico.getCellAt(e.getX(), e.getY()) ).getId() ) );

					if(relacionamentoSelecionado != null){
						isRelacionamento = true;
					}

					if(entidadeSelecionado != null){
						isEntidade = true;
					}

					if(agregacaoSelecionado != null){
						isAgregacao = true;
					}

					if(atributoSelecionado != null){
						if(atributoSelecionado.getTipoAtributo().equals(TipoAtributoEnum.COMPOSTO) || atributoSelecionado.getTipoAtributo().equals(TipoAtributoEnum.MULTIVALORADO)){
							isAtributo = true;
						}
					}

					if(especializacaoSelecionado != null){
						isEspecializacao = true;
					}
				}

				//botao entidade
				if(TelaPrincipal.getBotao() == 1){
					cont_entidade++;
					if(isEspecializacao){
						Entidade entidade = new Entidade( grafico,
								"Entidade" + cont_entidade,
								px,
								py,
								TelaPrincipal.getTipoEntidade());
						entidade.add(especializacaoSelecionado);
					}
					else{
						Entidade entidade = new Entidade( grafico, 
								"Entidade" + cont_entidade,
								px,
								py,
								TelaPrincipal.getTipoEntidade());
						entidade.add();
					}
					TelaPrincipal.setTipoEntidade();
					TelaPrincipal.setBotao(0);
					TelaPrincipal.desclicarBotoes();
					px = 0;
					py = 0;
				}

				//botao atributo
				if(TelaPrincipal.getBotao() == 2){
					if (cell != null && (isRelacionamento || isEntidade || isAtributo || isAgregacao)){
						cont_atributo++;
						Atributo atributo = new Atributo( grafico,
								TelaPrincipal.getTipoAtributo(),
								"Atributo",
								((mxCell) cell).getGeometry().getX(),
								((mxCell) cell).getGeometry().getY());


						if(isEntidade){
							int cont = 0;
							if(TelaPrincipal.getTipoAtributo() == TipoAtributoEnum.CHAVE){
								for (Atributo atrib : entidadeSelecionado.getAtributos() ) {
									if(atrib.getTipoAtributo() == TipoAtributoEnum.CHAVE){
										cont++;
									}
									if(cont == 3){
										JOptionPane.showMessageDialog(null, "Número de atributos chave excedido!");
										TelaPrincipal.setBotao(0);
										TelaPrincipal.setTipoAtributo();
										TelaPrincipal.desclicarBotoes();
										cell = null;
										px = 0;
										py = 0;
										return;
									}
								}
							}

							if(entidadeSelecionado.getAtributos().size() < maxEntAtr){
								entidadeSelecionado.getAtributos().add(atributo);
							}else{
								JOptionPane.showMessageDialog(null, "Número de atributos excedido!");
								TelaPrincipal.setBotao(0);
								TelaPrincipal.setTipoAtributo();
								TelaPrincipal.desclicarBotoes();
								cell = null;
								px = 0;
								py = 0;
								return;
							}
						}

						if(isRelacionamento){
							int cont = 0;
							if(TelaPrincipal.getTipoAtributo() == TipoAtributoEnum.CHAVE){
								for (Atributo atrib : relacionamentoSelecionado.getAtributos() ) {
									if(atrib.getTipoAtributo() == TipoAtributoEnum.CHAVE){
										cont++;
									}
									if(cont == 3){
										JOptionPane.showMessageDialog(null, "Número de atributos chave excedido!");
										TelaPrincipal.setBotao(0);
										TelaPrincipal.setTipoAtributo();
										TelaPrincipal.desclicarBotoes();
										cell = null;
										px = 0;
										py = 0;
										return;
									}
								}
							}

							if(relacionamentoSelecionado.getAtributos().size() < maxEntAtr){
								relacionamentoSelecionado.getAtributos().add(atributo);
							}else{
								JOptionPane.showMessageDialog(null, "Número de atributos excedido!");
								TelaPrincipal.setBotao(0);
								TelaPrincipal.setTipoAtributo();
								TelaPrincipal.desclicarBotoes();
								cell = null;
								px = 0;
								py = 0;
								return;
							}
						}

						if(isAgregacao){
							int cont = 0;
							if(TelaPrincipal.getTipoAtributo() == TipoAtributoEnum.CHAVE){
								for (Atributo atrib : agregacaoSelecionado.getAtributos() ) {
									if(atrib.getTipoAtributo() == TipoAtributoEnum.CHAVE){
										cont++;
									}
									if(cont == 3){
										JOptionPane.showMessageDialog(null, "Número de atributos chave excedido!");
										TelaPrincipal.setBotao(0);
										TelaPrincipal.setTipoAtributo();
										TelaPrincipal.desclicarBotoes();
										cell = null;
										px = 0;
										py = 0;
										return;
									}
								}
							}

							if(agregacaoSelecionado.getAtributos().size() < maxEntAtr){
								agregacaoSelecionado.getAtributos().add(atributo);
							}else{
								JOptionPane.showMessageDialog(null, "Número de atributos excedido!");
								TelaPrincipal.setBotao(0);
								TelaPrincipal.setTipoAtributo();
								TelaPrincipal.desclicarBotoes();
								cell = null;
								px = 0;
								py = 0;
								return;
							}

						}

						if(isAtributo){
							if(atributoSelecionado.getAtributos().size() < maxEntAtr){
								//atributo.getAtributos().add(atributo);
							}else{
								JOptionPane.showMessageDialog(null, "Número de atributos excedido!");
								TelaPrincipal.setBotao(0);
								TelaPrincipal.setTipoAtributo();
								TelaPrincipal.desclicarBotoes();
								cell = null;
								px = 0;
								py = 0;
								return;
							}
						}

						atributo.add(cell);

						TelaPrincipal.setBotao(0);
						TelaPrincipal.setTipoAtributo();
						TelaPrincipal.desclicarBotoes();
						cell = null;
						px = 0;
						py = 0;
					}
				}

				//botao relacionamento
				if(TelaPrincipal.getBotao() == 3){
					if(entidade_1 != null && isEntidade){
						entidade_2 = entidadeSelecionado;
					}
					if(entidade_1 == null && isEntidade){
						entidade_1 = entidadeSelecionado;
					}

					if (entidade_1 != null && entidade_2 != null){
						cont_relacionamento++;

						Relacionamento relacionamento = new Relacionamento( grafico, 
								"Relacionamento" + cont_relacionamento, 
								px,
								py);

						relacionamento.add(entidade_1, entidade_2);
						TelaPrincipal.setBotao(0);
						TelaPrincipal.desclicarBotoes();
						entidade_1 = null;
						entidade_2 = null;
						px = 0;
						py = 0;

					}
				}
				//relacionamento ternario
				if(TelaPrincipal.getBotao() == 4){

					if(entidade_2 != null && entidade_1 != null && isEntidade){
						entidade_3 = entidadeSelecionado;
					}
					if(entidade_1 != null && entidade_3 == null && isEntidade){
						entidade_2 = entidadeSelecionado;
					}
					if(entidade_1 == null && isEntidade){
						entidade_1 = entidadeSelecionado;
					}

					if (entidade_1 != null && entidade_2 != null && entidade_3 != null){
						cont_relacionamento++;
						Relacionamento relacionamento = new Relacionamento( grafico,
								"Relacionamento" + cont_relacionamento,
								px,
								py);
						relacionamento.add(entidade_1, entidade_2, entidade_3);
						TelaPrincipal.setBotao(0);
						TelaPrincipal.desclicarBotoes();
						entidade_1 = null;
						entidade_2 = null;
						entidade_3 = null;
						px = 0;
						py = 0;
					}
				}

				if(TelaPrincipal.getBotao() == 5){

					if(entidade_1 != null && entidade_2 != null && entidade_3 != null && isEntidade){
						entidade_4 = entidadeSelecionado;
					}
					if(entidade_2 != null && entidade_1 != null && entidade_4 == null && isEntidade){
						entidade_3 = entidadeSelecionado;
					}
					if(entidade_1 != null && entidade_3 == null && entidade_4 == null && isEntidade){
						entidade_2 = entidadeSelecionado;
					}
					if(entidade_1 == null && isEntidade){
						entidade_1 = entidadeSelecionado;
					}

					if (entidade_1 != null && entidade_2 != null && entidade_3 != null && entidade_4 != null){
						cont_relacionamento++;
						Relacionamento relacionamento = new Relacionamento( grafico, 
								"Relacionamento" + cont_relacionamento,  
								px,
								py);
						relacionamento.add(entidade_1, entidade_2, entidade_3, entidade_4);
						TelaPrincipal.setBotao(0);
						TelaPrincipal.desclicarBotoes();
						entidade_1 = null;
						entidade_2 = null;
						entidade_3 = null;
						entidade_4 = null;
						px = 0;
						py = 0;
					}
				}
				//botao especializacao
				if(TelaPrincipal.getBotao() == 6){
					if(isEntidade){
						cont_entidade++;
						Especializacao disjuncao = new Especializacao(  grafico,
								px,
								py,
								TelaPrincipal.getTipoEspecializacao());
						disjuncao.add(entidadeSelecionado);
						TelaPrincipal.setBotao(0); 
						TelaPrincipal.desclicarBotoes();
					}
				}

				//botao agregacao
				if(TelaPrincipal.getBotao() == 7){
					int contCardinalidades = 0;
					if(entidade_1 == null && isEntidade){
						entidade_1 = entidadeSelecionado;
					}
					if(relacionamento == null && isRelacionamento){
						relacionamento = relacionamentoSelecionado;
					}

					if (relacionamento != null  && entidade_1 != null){
						Collection<Relacao> rel = relacionamento.getEntidades().values();
						Iterator<Relacao> i = rel.iterator();
						while(i.hasNext()) {
							Relacao relacao = i.next();
							if(StringUtils.isAlpha(relacao.getCardinalidade())){
								contCardinalidades++;
							}
						}
						if(contCardinalidades<2) {
							JOptionPane.showMessageDialog(null, "É necessário que as agregações tenham pelo menos 2 cardinalidades N-M!");
							return;
						}

						cont_entidade++;
						Agregacao agregacao = new Agregacao( grafico, 
								"Agregação", 
								px,
								py);
						agregacao.setRelacionamentoAgregacao(relacionamento);
						agregacao.add(relacionamento, entidade_1);
						TelaPrincipal.setBotao(0);
						TelaPrincipal.desclicarBotoes();
						px = 0;
						py = 0;
						entidade_1 = null;
						relacionamento = null;

					}
				} 


			}
		}
	}

}

