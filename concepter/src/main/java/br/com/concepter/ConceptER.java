/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.concepter;

import br.com.concepter.view.TelaPrincipal;
import javax.swing.JFrame;


public class ConceptER {

    public static void main(String[] args) {
        // TODO code application logic here
        
        TelaPrincipal telaPrincipal = new TelaPrincipal();
        telaPrincipal.setVisible(true);
        telaPrincipal.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
}
