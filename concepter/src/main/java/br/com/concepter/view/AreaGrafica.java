/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.concepter.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxGraphTransferHandler;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;

import br.com.concepter.model.beans.Agregacao;
import br.com.concepter.model.beans.Atributo;
import br.com.concepter.model.beans.Entidade;
import br.com.concepter.model.beans.Especializacao;
import br.com.concepter.model.beans.Relacao;
import br.com.concepter.model.beans.Relacionamento;
import br.com.concepter.model.enuns.TipoAtributoEnum;
import br.com.concepter.model.enuns.TipoObrigatoriedadeEnum;


public class AreaGrafica extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private final static String DROPALLOWED_KEY = "DropAllowed";

	
	private static mxGraph grafico;
	private static mxGraphComponent areaGrafico;
	private static Integer cont_entidade = 0;
	private Integer cont_relacionamento = 0;
	private Integer cont_atributo = new Integer(0);
	
	public static Integer maxEntAtr = 20;

	private static HashMap<Integer, Entidade> mapaGraficoEntidades = new HashMap<Integer, Entidade>();
	private HashMap<Integer, Atributo> mapaGraficoAtributos = new HashMap<Integer, Atributo>();
	private HashMap<Integer, Relacionamento> mapaGraficoRelacionamentos = new HashMap<Integer, Relacionamento>();
	private HashMap<Integer, Especializacao> mapaGraficoEspecializacao = new HashMap<Integer, Especializacao>();
	private HashMap<Integer, Agregacao> mapaGraficoAgregacao = new HashMap<Integer, Agregacao>();

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
	private JMenuItem menuItemBinario;
	private JMenuItem menuItemTernario;
	private JMenuItem menuItemQuaternario;
	private JMenu menuAgregacao;
	private JMenu menuTotalidade;
	private JMenu menuParcialidade;
	private JMenu menuNn;
	private JMenu menu1n;
	private static double px, py;

	public AreaGrafica(){
		this.grafico = new mxGraph();
		this.grafico.setMinimumGraphSize(new mxRectangle(83,0,500,200));

		this.areaGrafico = new mxGraphComponent(grafico){

            protected TransferHandler createTransferHandler()
            {
                return new mxGraphTransferHandler() {
                	
                    public boolean importData(JComponent c, Transferable t)
                    {
                        if( c instanceof mxGraphComponent) {
                        	
                        	
                            Object obj = ((mxGraphComponent) c).getClientProperty(DROPALLOWED_KEY);
                            boolean allowDrop = obj != null && ((Boolean) obj) == true;

                            if( !allowDrop) {
                                System.out.println( "Drop not allowed here!");
                                return false;
                            }

                        }

                        return super.importData(c, t);

                    }

                };
            }

        };
        
		this.areaGrafico.setPreferredSize(new Dimension(500,200));
		this.areaGrafico.setLocation(83,0);
		this.areaGrafico.getGraphControl().addMouseListener(new ObjetoMer_Selecionado());
		this.areaGrafico.getGraphControl().addMouseListener(new ObjetoMer_RigthClick());
		this.areaGrafico.getGraphControl().addMouseListener(new BotaoEsquerdoCliqueGrafico());
		
		this.areaGrafico.putClientProperty("DropAllowed", Boolean.TRUE);
		this.areaGrafico.setBorder( BorderFactory.createLineBorder(Color.red));
		
		/*this.areaGrafico.getGraphControl().addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				System.out.println("Moved X= "+ e.getX()+" Y= "+ e.getY());
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				System.out.println("Dragged X= "+ e.getX()+" Y= "+ e.getY());				
			}
		});

		this.areaGrafico.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if(e.getKeyCode()==KeyEvent.VK_DELETE) {
					deleteItem();
				}

			}
		});*/


		this.grafico.setEdgeLabelsMovable(false);   //Nao permite que a descricao da seta seja movida
		this.grafico.setCellsDisconnectable(false); //Nao Permite que as setas sejam desconectadas
		//this.grafico.setCellsResizable(false);      //Nao Permite que os objetos sejam redimensionados

		areaGrafico.setConnectable(false); // Nao permite que setas sejam criadas de dentro de um objeto
		areaGrafico.getGraphHandler().setRemoveCellsFromParent(false); //Nao permiti que os objetos sejam removidos do objetos pai

		this.grafico.setAllowDanglingEdges(false);
		this.grafico.setAllowLoops(false);
		this.grafico.setAllowNegativeCoordinates(false);

		this.grafico.setCellsBendable(false);
		this.grafico.setSplitEnabled(true);
		this.grafico.setKeepEdgesInForeground(false);
		this.grafico.setKeepEdgesInBackground(true);

		getConfig();

		this.add(areaGrafico);

		//Menu popup para Objetos Mer
		menuPopup = new JPopupMenu();

		menuCompletude = new JMenu("Completude");
		menuCardinalidade = new JMenu("Cardinalidade");

		//Item para o Menu popup de Objetos Mer
		menuItemDelete = new JMenuItem("Delete");

		menuAgregacao = new JMenu("Agregação");
		menuItemBinario = new JMenuItem("Binário");
		menuItemTernario = new JMenuItem("Ternário");
		menuItemQuaternario = new JMenuItem("Quaternário");

		menuParcialidade = new JMenu("Parcialidade");
		menuTotalidade = new JMenu("Totalidade");

		menuNn = new JMenu("N - N");
		menu1n = new JMenu("1 - N");

		menuPopup.add(menuItemDelete);

		menuItemDelete.addActionListener(new BotaoDeletarPopupMenu());
	}

	public mxGraph getGrafico(){
		return this.grafico;
	}

	public mxGraphComponent getAreaGrafico(){
		return this.areaGrafico;
	}

	public void setAreaGrafico(mxGraphComponent areaGrafico) {
		this.areaGrafico = areaGrafico;
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

	public Integer getCont_atributo() {
		return cont_atributo;
	}

	public void setCont_atributo(Integer cont_atributo) {
		this.cont_atributo = cont_atributo;
	}
	
	public void setCont_entidade(Integer cont_entidade) {
		this.cont_entidade = cont_entidade;
	}

	public void setCont_relacionamento(Integer cont_relacionamento) {
		this.cont_relacionamento = cont_relacionamento;
	}

	public void setMapaGraficoEntidades(HashMap<Integer, Entidade> mapaGraficoEntidades) {
		this.mapaGraficoEntidades = mapaGraficoEntidades;
	}

	public void setMapaGraficoAtributos(HashMap<Integer, Atributo> mapaGraficoAtributos) {
		this.mapaGraficoAtributos = mapaGraficoAtributos;
	}

	public void setMapaGraficoRelacionamentos(HashMap<Integer, Relacionamento> mapaGraficoRelacionamentos) {
		this.mapaGraficoRelacionamentos = mapaGraficoRelacionamentos;
	}

	public void setMapaGraficoEspecializacao(HashMap<Integer, Especializacao> mapaGraficoEspecializacao) {
		this.mapaGraficoEspecializacao = mapaGraficoEspecializacao;
	}

	public void setMapaGraficoAgregacao(HashMap<Integer, Agregacao> mapaGraficoAgregacao) {
		this.mapaGraficoAgregacao = mapaGraficoAgregacao;
	}

	public static HashMap<Integer, Entidade> getMapaGraficoEntidades() {
		return mapaGraficoEntidades;
	}

	public HashMap<Integer, Atributo> getMapaGraficoAtributos() {
		return mapaGraficoAtributos;
	}

	public HashMap<Integer, Relacionamento> getMapaGraficoRelacionamentos() {
		return mapaGraficoRelacionamentos;
	}

	public HashMap<Integer, Especializacao> getMapaGraficoEspecializacao() {
		return mapaGraficoEspecializacao;
	}

	public HashMap<Integer, Agregacao> getMapaGraficoAgregacao() {
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

		menuPopup.remove(menuCompletude);
		menuPopup.remove(menuCardinalidade);
		menuPopup.remove(menuAgregacao);
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

		if(cell != null ){
			relacionamentoSelecionado = (Relacionamento) mapaGraficoRelacionamentos.get( Integer.parseInt( ( (mxCell) cell ).getId() ));
			entidadeSelecionado = (Entidade) mapaGraficoEntidades.get( Integer.parseInt( ( (mxCell) cell ).getId() ) );
			atributoSelecionado =  (Atributo) mapaGraficoAtributos.get( Integer.parseInt( ( (mxCell) cell ).getId() ) );
			agregacaoSelecionado =  (Agregacao) mapaGraficoAgregacao.get( Integer.parseInt( ( (mxCell) cell ).getId() ) );
			especializacaoSelecionado =  (Especializacao) mapaGraficoEspecializacao.get( Integer.parseInt( ( (mxCell) cell ).getId() ) );
		}

		if(relacionamentoSelecionado != null){
			isRelacionamento = true;
			mapaGraficoRelacionamentos.remove(Integer.parseInt( ( (mxCell) cell ).getId() ));
		}

		if(entidadeSelecionado != null){
			isEntidade = true;
			
		}

		if(agregacaoSelecionado != null){
			isAgregacao = true;
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
					for (Atributo atributo1 : atributo.getAtributos()) {
						grafico.getModel().remove(atributo1.getForma());
					}
				}
				grafico.getModel().remove(atributo.getForma());
			}
		} 
		/*if (isEntidade && !entidadeSelecionado.getRelacionamentos().isEmpty()) {
			for (Relacionamento relacionamento : entidadeSelecionado.getRelacionamentos()) {
				grafico.getModel().remove(relacionamento.getCell());
			}
		}*/
		if(isEntidade) {
			mapaGraficoEntidades.remove(Integer.parseInt( ( (mxCell) cell ).getId() ));
		}

		if(isRelacionamento || isAgregacao || isAtributo || isEntidade || isEspecializaco || isRelacionamento){
			grafico.getModel().remove(cell);
			
		}
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

	public class OnClickJMenuItemComplitude implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			mxCell relacionamento_cell = (mxCell)cell;

			JMenuItem menuItem = ((JMenuItem) e.getSource());
			JMenu parent = (JMenu) ((JPopupMenu) menuItem.getParent()).getInvoker();

			for (int i = 0; i < relacionamento_cell.getEdgeCount(); i++) {
				String entidade_str = ((mxCell)relacionamento_cell.getEdgeAt(i)).getSource().getValue().toString();
				Entidade entidade = getMapaGraficoEntidades().get( Integer.valueOf( ((mxCell)relacionamento_cell.getEdgeAt(i)).getSource().getId()));

				if( entidade_str.equals(parent.getText()) ){

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

					getAreaGrafico().refresh();
					return;
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
				boolean isEntidade = false;
				boolean isAtributo = false;
				boolean isAgregacao = false;
				boolean isEspecializaco = false;

				Entidade entidadeSelecionado = null;
				Relacionamento relacionamentoSelecionado = null;
				Atributo atributoSelecionado = null;
				Agregacao agregacaoSelecionado = null;
				Especializacao especializacaoSelecionado = null;

				limparMenuPopup();
				
				cell = areaGrafico.getCellAt(e.getX(), e.getY());

				if(cell != null ){
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
								mapaGraficoEntidades,
								"Entidade" + cont_entidade,
								px,
								py,
								TelaPrincipal.getTipoEntidade());
						entidade.add(especializacaoSelecionado);
					}
					else{
						Entidade entidade = new Entidade( grafico, 
								mapaGraficoEntidades,
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
								mapaGraficoAtributos,
								TelaPrincipal.getTipoAtributo(),
								"Atributo",
								((mxCell) cell).getGeometry().getX(),
								((mxCell) cell).getGeometry().getY(),
								cont_atributo);
						atributo.setIsRelacionamento(isRelacionamento);

						if(isEntidade){
							int cont = 0;
							if(TelaPrincipal.getTipoAtributo() == TipoAtributoEnum.CHAVE){
								for (Atributo atrib : entidadeSelecionado.getAtributos() ) {
									if(atrib.getTipoAtributo() == TipoAtributoEnum.CHAVE){
										cont++;
									}
									if(cont == 3){
										JOptionPane.showMessageDialog(null, "Número de atributos chave excedido!");
										return;
									}
								}
							}

							if(entidadeSelecionado.getAtributos().size() < maxEntAtr){
								atributo.setEntidade( entidadeSelecionado );
							}else{
								JOptionPane.showMessageDialog(null, "Número de atributos excedido!");
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
										return;
									}
								}
							}

							if(relacionamentoSelecionado.getAtributos().size() < maxEntAtr){
								atributo.setRelacionamento(relacionamentoSelecionado );
							}else{
								JOptionPane.showMessageDialog(null, "Número de atributos excedido!");
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
										return;
									}
								}
							}

							if(agregacaoSelecionado.getAtributos().size() < maxEntAtr){
								atributo.setAgregacao(agregacaoSelecionado);
							}else{
								JOptionPane.showMessageDialog(null, "Número de atributos excedido!");
								return;
							}

						}

						if(isAtributo){
							if(atributoSelecionado.getAtributos().size() < maxEntAtr){
								atributo.setAtributo(atributoSelecionado);
							}else{
								JOptionPane.showMessageDialog(null, "Número de atributos excedido!");
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
								mapaGraficoRelacionamentos,
								"Relacionamento" + cont_relacionamento, 
								px,
								py);
						System.out.println(entidade_1);
						System.out.println(entidade_2);
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
								mapaGraficoRelacionamentos,
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
								mapaGraficoRelacionamentos,
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

				if(TelaPrincipal.getBotao() == 6){

					if(isEntidade){
						Especializacao disjuncao = new Especializacao(  grafico, 
								mapaGraficoEspecializacao, 
								px,
								py,
								TelaPrincipal.getTipoEspecializacao());
						disjuncao.add(entidadeSelecionado, mapaGraficoEntidades, ++cont_entidade);
						TelaPrincipal.setBotao(0); 
						TelaPrincipal.desclicarBotoes();
					}
				}

				//botao agregacao binaria
				if(TelaPrincipal.getBotao() == 7){

					/*if(entidade_1 == null  && isEntidade){
						entidade_1 = entidadeSelecionado;
					}*/
					if(relacionamento == null && isRelacionamento){
						relacionamento = relacionamentoSelecionado;
					}

					if (relacionamento != null ){

						Agregacao agregacao = new Agregacao( grafico, 
								mapaGraficoAgregacao,
								mapaGraficoRelacionamentos,
								"Agregação", 
								px,
								py);
						agregacao.add(relacionamento, entidade_1);
						TelaPrincipal.setBotao(0);
						TelaPrincipal.desclicarBotoes();
						entidade_1 = null;
						entidade_2 = null;
						px = 0;
						py = 0;

					}
				} 
				//Botao agregacao ternaria
				if(TelaPrincipal.getBotao() == 8){
					if(entidade_1 != null && relacionamento != null  && isEntidade){
						entidade_2 = entidadeSelecionado;
					}
					if(relacionamento != null && entidade_2 == null  && isEntidade){
						entidade_1 = entidadeSelecionado;
					}
					if(relacionamento == null && isRelacionamento){
						relacionamento = relacionamentoSelecionado;
					}

					if (relacionamento != null && entidade_1 != null && entidade_2 != null){

						Agregacao agregacao = new Agregacao( grafico, 
								mapaGraficoAgregacao, 
								mapaGraficoRelacionamentos,
								"Agregação", 
								px,
								py);
						agregacao.add(relacionamento, entidade_1, entidade_2);
						TelaPrincipal.setBotao(0);
						TelaPrincipal.desclicarBotoes();
						relacionamento = null;
						entidade_1 = null;
						entidade_2 = null;
						px = 0;
						py = 0;

					}
				}

				//botao agregacao quaternaria
				if(TelaPrincipal.getBotao() == 9){
					if(relacionamento != null && entidade_1 != null && entidade_2 != null  && isEntidade){
						entidade_3 = entidadeSelecionado;
					}
					if(entidade_1 != null && relacionamento != null && entidade_3 == null  && isEntidade){
						entidade_2 = entidadeSelecionado;
					}
					if(relacionamento != null && entidade_2 == null && entidade_3 == null  && isEntidade){
						entidade_1 = entidadeSelecionado;
					}
					if(relacionamento == null && isRelacionamento){
						relacionamento = relacionamentoSelecionado;
					}

					if (relacionamento != null && entidade_1 != null && entidade_2 != null && entidade_3 != null){

						Agregacao agregacao = new Agregacao( grafico, 
								mapaGraficoAgregacao, 
								mapaGraficoRelacionamentos,
								"Agregação", 
								px,
								py);
						agregacao.add(relacionamento, entidade_1, entidade_2, entidade_3);
						TelaPrincipal.setBotao(0);
						TelaPrincipal.desclicarBotoes();
						relacionamento = null;
						entidade_1 = null;
						entidade_2 = null;
						entidade_3 = null;
						px = 0;
						py = 0;

					}
				}

			}
		}
	}

}

