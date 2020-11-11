
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
public class Mostrar extends javax.swing.JFrame {
    ArrayList<Cancion> canciones=new ArrayList();
    DefaultTableModel tabla = new DefaultTableModel();
    DefaultListModel lista = new DefaultListModel();
    /**
     * Creates new form Mostrar
     */
    public Mostrar() {
        tabla.addColumn("Artista");
        tabla.addColumn("Album");
        tabla.addColumn("Pista");
        initComponents();
        agregar();
    }
    public void agregar(){
        leer();
        for(int i=0; i<canciones.size(); i++){
            tabla.addRow(new Object[]{canciones.get(i).getArtista(),canciones.get(i).getAlbum(), canciones.get(i).getPista()});
        }
        jTable1.setModel(tabla);
    }

    
    public void leer(){
        try {
            RandomAccessFile almacen = new RandomAccessFile("Almacen.data","rw");
            RandomAccessFile indice = new RandomAccessFile("Indice.data","rw");
            RandomAccessFile informacion = new RandomAccessFile("Informacion.data","rw");
            RandomAccessFile letra = new RandomAccessFile("Letras.data","rw");
            almacen.seek(397032);
            int posicion=397036;
            int fin=almacen.readInt();
            int puntero;
            byte lon;
            String dato="";
            while(posicion!=fin){
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
                canciones.add(nuevo);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Mostrar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Mostrar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setTitle("Almacén de Música");
        setAlwaysOnTop(true);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Artista", "Álbum", "Pista"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jButton1.setText("Seleccionar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Cancion s =canciones.get(jTable1.getSelectedRow());
        lista.clear();
        lista.addElement("Pista:    "+s.getPista());
        lista.addElement("Duración: "+s.getDuracion());
        lista.addElement("Artista:  "+s.getArtista());
        lista.addElement("Álbum:    "+s.getAlbum());
        lista.addElement("Disquera: "+s.getDisquera());
        lista.addElement("Año:  "+s.getAnio());
        lista.addElement("Genero:   "+s.getGenero());
        lista.addElement("Dirección:    "+s.getDireccion()+s.getPista()+".mp3");
        lista.addElement("Enlace del Artista:   "+s.getEnlaceArtista());
        lista.addElement("Enlace de la Disquera:   "+s.getEnlaceDisquera());
        lista.addElement("Otros Enlaces:   "+s.getEnlaceOtros());
        lista.addElement("Información:  "+s.getInfo());
        lista.addElement("Letra:    "+s.getLetra());
        jList1.setModel(lista);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
    }//GEN-LAST:event_formKeyPressed

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
            java.util.logging.Logger.getLogger(Mostrar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Mostrar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Mostrar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Mostrar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Mostrar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
