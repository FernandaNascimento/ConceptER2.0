package br.com.concepter.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import br.com.concepter.model.beans.SavedObject;
import br.com.concepter.utils.FileUtils;
import br.com.concepter.utils.XmlUtils;
import br.com.concepter.view.AreaGrafica;
import br.com.concepter.view.TelaPrincipal;

public class ConceptERController {
	
	public boolean save(AreaGrafica areaGrafica, String pathFile) {
		XmlUtils xmlUtils = new XmlUtils();		
		
		if(StringUtils.isNotEmpty(pathFile)) {
			
			SavedObject savedObject = new SavedObject(areaGrafica);
			savedObject.setGraph(FileUtils.saveGraph(areaGrafica.getAreaGrafico(), pathFile));
			
			try {
				FileOutputStream os = new FileOutputStream(pathFile.replaceAll(".er0","")+ ".er0");
				xmlUtils.save(savedObject, os);
				return true;
			} catch (FileNotFoundException ex) {
				Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
			}			
		}

		
		return false;
	}

}
