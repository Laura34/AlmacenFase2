
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Usuario 1
 */
public class Buscar extends javax.swing.JFrame {

    ArrayList<Indices> indices=new ArrayList();
    ArrayList<Indices> aux=new ArrayList();
    ArrayList<Cancion> canciones=new ArrayList();
    DefaultListModel lista = new DefaultListModel();
    DefaultListModel lista2 = new DefaultListModel();
    /**
     * Creates new form Buscar
     */
    public Buscar() {
        initComponents();
        leerIndice();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Ingrese el año, artista o nombre de la canción");

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Salir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList2);

        jButton3.setText("Seleccionar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3))
                            .addComponent(jLabel1))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void leerIndice(){
        try {
            // TODO add your handling code here:
            Indices indx;
            RandomAccessFile indice = new RandomAccessFile("Indice.data","rw");
            indice.seek(4);
            int posicion=6;
            int longitud=indice.readShort();
            while(posicion!=longitud){
                indx=new Indices(false);
                indx.setNombre(indice.readShort()+"");
                indx.setPuntero(indice.readInt());
                indices.add(indx);
                posicion+=6;
            }
            posicion=6008;
            indice.seek(6006);
            longitud=indice.readShort();
            byte tamanio;
            String dato="";
            while(posicion!=longitud){
                indx=new Indices(false);
                tamanio=indice.readByte();
                for(int i=0; i<tamanio;i++){
                    dato=dato+(char)indice.readByte();
                }
                indx.setNombre(dato);
                indx.setPuntero(indice.readInt());
                indices.add(indx);
                posicion=posicion+5+dato.length();
                dato="";
            }
            posicion=26012;
            indice.seek(26008);
            longitud=indice.readInt();
            while(posicion!=longitud){
                indx=new Indices(true);
                tamanio=indice.readByte();
                for(int i=0; i<tamanio;i++){
                    dato=dato+(char)indice.readByte();
                }
                indx.setNombre(dato);
                indx.setPuntero(indice.readInt());
                indices.add(indx);
                posicion=posicion+5+dato.length();
                dato="";
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Buscar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Buscar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void leerRegistro(int ubicacion){
        try {
            RandomAccessFile almacen = new RandomAccessFile("Almacen.data","rw");
            RandomAccessFile indice = new RandomAccessFile("Indice.data","rw");
            RandomAccessFile informacion = new RandomAccessFile("Informacion.data","rw");
            RandomAccessFile letra = new RandomAccessFile("Letras.data","rw");
            almacen.seek(ubicacion);
            int posicion=ubicacion;
            int puntero;
            byte lon;
            String dato="";
            Cancion nuevo =new Cancion();
            /*Pista*/
            almacen.seek(posicion);
            puntero=almacen.readShort();
            posicion+=2;
            indice.seek(puntero);
                lon=indice.readByte();
                for(int i=0; i<lon; i++){
                    dato=dato+(char)indice.readByte();
                }
                nuevo.setPista(dato);
                dato="";
                
                /*Disquera*/
                almacen.seek(posicion);
                puntero=almacen.readInt();
                posicion+=4;
                almacen.seek(puntero);
                lon=almacen.readByte();
                for(int i=0; i<lon; i++){
                    dato=dato+(char)almacen.readByte();
                }
                nuevo.setDisquera(dato);
                dato="";
                
                /*Artista*/
                almacen.seek(posicion);
                puntero=almacen.readShort();
                posicion+=2;
                indice.seek(puntero);
                lon=indice.readByte();
                for(int i=0; i<lon; i++){
                    dato=dato+(char)indice.readByte();
                }
                nuevo.setArtista(dato);
                dato="";
                
                /*Album*/
                almacen.seek(posicion);
                puntero=almacen.readInt();
                posicion+=4;
                almacen.seek(puntero);
                lon=almacen.readByte();
                for(int i=0; i<lon; i++){
                    dato=dato+(char)almacen.readByte();
                }
                nuevo.setAlbum(dato);
                dato="";
                
                /*Año*/
                almacen.seek(posicion);
                puntero=almacen.readShort();
                posicion+=2;
                indice.seek(puntero);
                nuevo.setAnio(indice.readShort());
                
                /*Genero*/
                almacen.seek(posicion);
                puntero=almacen.readInt();
                posicion+=4;
                almacen.seek(puntero);
                lon=almacen.readByte();
                for(int i=0; i<lon; i++){
                    dato=dato+(char)almacen.readByte();
                }
                nuevo.setGenero(dato);
                dato="";
                
                /*Ruta*/
                almacen.seek(posicion);
                puntero=almacen.readInt();
                posicion+=4;
                almacen.seek(puntero);
                lon=almacen.readByte();
                for(int i=0; i<lon; i++){
                    dato=dato+(char)almacen.readByte();
                }
                nuevo.setDireccion(dato);
                dato="";
                
                /*Duracion*/
                almacen.seek(posicion);
                nuevo.setDuracion(almacen.readShort());
                posicion+=2;
                
                /*Enlace Artista*/
                almacen.seek(posicion);
                puntero=almacen.readInt();
                posicion+=4;
                almacen.seek(puntero);
                lon=almacen.readByte();
                for(int i=0; i<lon; i++){
                    dato=dato+(char)almacen.readByte();
                }
                nuevo.setEnlaceArtista(dato);
                dato="";
                
                /*Enlace Disquera*/
                almacen.seek(posicion);
                puntero=almacen.readInt();
                posicion+=4;
                almacen.seek(puntero);
                lon=almacen.readByte();
                for(int i=0; i<lon; i++){
                    dato=dato+(char)almacen.readByte();
                }
                nuevo.setEnlaceDisquera(dato);
                dato="";
                
                /*Enlace Otros*/
                almacen.seek(posicion);
                puntero=almacen.readInt();
                posicion+=4;
                almacen.seek(puntero);
                lon=almacen.readByte();
                for(int i=0; i<lon; i++){
                    dato=dato+(char)almacen.readByte();
                }
                nuevo.setEnlaceOtros(dato);
                dato="";
                
                /*Info*/
                int leer;
                almacen.seek(posicion);
                puntero=almacen.readInt();
                posicion+=4;
                informacion.seek(puntero);
                leer=informacion.readInt();
                for(int i=0; i<leer; i++){
                    dato=dato+(char)informacion.readByte();
                }
                nuevo.setInfo(dato);
                dato="";
                
                /*Letra*/
                almacen.seek(posicion);
                puntero=almacen.readInt();
                posicion+=4;
                letra.seek(puntero);
                leer=letra.readInt();
                for(int i=0; i<leer; i++){
                    dato=dato+(char)letra.readByte();
                }
                nuevo.setLetra(dato);
                dato="";
                lista2.clear();
        lista2.addElement("Pista:    "+nuevo.getPista());
        lista2.addElement("Duración: "+nuevo.getDuracion());
        lista2.addElement("Artista:  "+nuevo.getArtista());
        lista2.addElement("Álbum:    "+nuevo.getAlbum());
        lista2.addElement("Disquera: "+nuevo.getDisquera());
        lista2.addElement("Año:  "+nuevo.getAnio());
        lista2.addElement("Genero:   "+nuevo.getGenero());
        lista2.addElement("Dirección:    "+nuevo.getDireccion()+nuevo.getPista());
        lista2.addElement("Enlace del Artista:   "+nuevo.getEnlaceArtista());
        lista2.addElement("Enlace de la Disquera:   "+nuevo.getEnlaceDisquera());
        lista2.addElement("Otros Enlaces:   "+nuevo.getEnlaceOtros());
        lista2.addElement("Información:  "+nuevo.getInfo());
        lista2.addElement("Letra:    "+nuevo.getLetra());
        jList1.setModel(lista2);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Mostrar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Mostrar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        lista.clear();
        lista2.clear();
        aux.clear();
        String nombre=jTextField1.getText();
        boolean encontro=false;
        for(int i=0; i<indices.size();i++){
            if(indices.get(i).getNombre().equalsIgnoreCase(nombre)
                    ||indices.get(i).getNombre().toLowerCase().contains(nombre.toLowerCase())
                    ||indices.get(i).getNombre().contains(nombre)){
                encontro=true;
                if(indices.get(i).isFin()){
                   lista.addElement(indices.get(i).getNombre());
                   aux.add(indices.get(i));
                }
                else{
                    try {
                        RandomAccessFile indice = new RandomAccessFile("Indice.data","rw");
                        int posicion=indices.get(i).getPuntero();
                        indice.seek(posicion);
                        Indices indx=new Indices(true);
                        String dato="";
                        byte tamanio=indice.readByte();
                        for(int j=0; j< tamanio;j++){
                            dato=dato+(char)indice.readByte();
                        }
                        indx.setNombre(dato);
                        indx.setPuntero(indice.readInt());
                        lista.addElement(indx.getNombre());
                        aux.add(indx);
                        dato="";
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Buscar.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Buscar.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        if(!encontro){
            JOptionPane.showMessageDialog(null, "No se encontró ninguna pista con esa información");
        }
        jList2.setModel(lista);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Indices i=aux.get(jList2.getSelectedIndex());
        leerRegistro(i.getPuntero());
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Buscar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Buscar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Buscar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Buscar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Buscar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
