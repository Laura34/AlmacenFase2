
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Usuario 1
 */
public class Lector extends javax.swing.JFrame {

    ArrayList<Cancion> canciones=new ArrayList();
    int punteroA;
    int punteroAux;
    DefaultListModel lista = new DefaultListModel();
    /**
     * Creates new form Lector1
     */
    public Lector() {
        initComponents();
        File p= new File("Punteros.data");
        if(p.exists()){
            try {
                RandomAccessFile puntero = new RandomAccessFile("Punteros.data", "r");
                puntero.seek(0);
                punteroA=puntero.readInt();
                punteroAux=puntero.readInt();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            punteroA=4;
            punteroAux=0;
        }
    }
    
    public void Ingresar(String ruta){
        File seleccion = jFileChooser1.getSelectedFile();
        if(seleccion.isDirectory()){    //Verifica si el usuario selecciono una carpeta
            String[] ficheros = seleccion.list();   //Obtiene los ficheros
            if(ficheros!=null){ //Verifica que no sea una carpeta vacía
                for(int i=0; i<ficheros.length; i++){
                    String subruta = ruta+"\\"+ficheros[i];
                    File dir2 = new File(subruta);
                    Cancion nuevo = new Cancion();
                    String nombre=ficheros[i];
                    String corte="";
                    for(int x=0; x<(nombre.length()-4);x++){
                        corte=corte+nombre.charAt(x);
                    }
                    nuevo.setPista(corte);
                    corte="";
                    nuevo.setDireccion(ruta);
                    Leer(subruta, nuevo);
                    if(dir2.isDirectory()){ //Lee las carpetas dentro de la carpeta
                        Ingresar(subruta);  //Recursividad
                    }
                }
            }
        }
        else{
            Cancion nuevo = new Cancion();
            nuevo.setPista(jFileChooser1.getSelectedFile().getName());
            String aux="";
            nuevo.setDireccion(jFileChooser1.getSelectedFile().getAbsolutePath());
            for(int i=0;i<(nuevo.getDireccion().length()-nuevo.getPista().length());i++){
                aux=aux+nuevo.getDireccion().charAt(i);
            }
            nuevo.setDireccion(aux);
            Leer(jFileChooser1.getSelectedFile().getPath(),nuevo);
        }
    }
    
    public String BuscarInfo(short tamanioA, RandomAccessFile a, String tipo){
        String etiqueta="";
        boolean info=false;
        try {
            a.seek(10);     //Omite el encabezado del archivo
            for(int i = 0; i<tamanioA; i++){    //Busca el dato en la etiqueta
                if(!etiqueta.contains(tipo)){
                    etiqueta = etiqueta + (char)a.readByte();
                }
                else{
                    info=true;
                    break;
                }
            }
            if(info){   //Si la informacion si existe en el archivo, procederá a leerla
                int tamanioDato=0;
                for(int i=0; i<4;i++){  //Lee la informacion para saber el tamaño del dato
                    tamanioDato=tamanioDato + a.readByte();
                }
                a.skipBytes(2);
                etiqueta="";
                if(tipo.equals("COMM")){
                    a.skipBytes(5);     //Al buscar este dato, debe omitir 5 bytes
                    for(int i=0; i<tamanioDato-5;i++){
                        etiqueta=etiqueta + (char)a.readByte();     //Encuentra la información
                    }
                }
                else{
                    for(int i=0; i<tamanioDato;i++){
                        etiqueta=etiqueta + (char)a.readByte(); //Para los demas datos solo debe leerlos sin saltar bytes
                    }
                }
            }
            else{   //Si no encuentra la informacion solo mostrará un guión
                etiqueta="No Disponible";
            }
        } catch (IOException ex) {
            Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return etiqueta;
    }
    
    public void Escribir(Cancion cancion) throws IOException{
        File archivo = new File("Almacen.data");
        short anio=0;
        short artista=0;
        int pista = 0;
        int direccion=0;
        int genero = 0;
        int disquera = 0;
        int album = 0;
        int enlaceA = 0;
        int enlaceD = 0;
        int enlaceO = 0;
        int infor=0;
        int let=0;
        if(!archivo.exists()){  //Si es el primer registro
            RandomAccessFile almacen = new RandomAccessFile("Almacen.data","rw");
            RandomAccessFile indice = new RandomAccessFile("Indice.data","rw");
            RandomAccessFile informacion = new RandomAccessFile("Informacion.data","rw");
            RandomAccessFile letra = new RandomAccessFile("Letras.data","rw");
            almacen.writeBytes("ALMA");
            indice.writeBytes("INDX");
            informacion.writeBytes("INFO");
            letra.writeBytes("LTRA");
            indice.seek(4);
            indice.writeShort(6);
            indice.seek(6006);
            indice.writeShort(6008);
            indice.seek(26008);
            indice.writeInt(26012);
            almacen.seek(4);
            almacen.writeInt(8);
            almacen.seek(101008);
            almacen.writeInt(101012);
            almacen.seek(202012);
            almacen.writeInt(202016);
            almacen.seek(223016);
            almacen.writeInt(223020);
            almacen.seek(244020);
            almacen.writeInt(244024);
            almacen.seek(295024);
            almacen.writeInt(295028);
            almacen.seek(346028);
            almacen.writeInt(346032);
            almacen.seek(397032);
            almacen.writeInt(397036);
            informacion.seek(4);
            informacion.writeInt(8);
            letra.seek(4);
            letra.writeInt(8);
        }
        RandomAccessFile almacen = new RandomAccessFile("Almacen.data","rw");
        RandomAccessFile indice = new RandomAccessFile("Indice.data","rw");
        RandomAccessFile informacion = new RandomAccessFile("Informacion.data","rw");
        RandomAccessFile letra = new RandomAccessFile("Letras.data","rw");
        int tamanio1;
        short tamanio2;
        /*Año*/
        indice.seek(4);
        tamanio2=indice.readShort();
        anio=tamanio2;
        indice.seek(tamanio2);
        indice.writeShort(cancion.getAnio());
        indice.writeInt((int) indice.length());
        indice.seek(4);
        indice.writeShort(tamanio2+6);
            
        /*Artista*/
        indice.seek(6006);
        tamanio2=indice.readShort();
        artista=tamanio2;
        indice.seek(tamanio2);
        indice.writeByte(cancion.getArtista().length());
        indice.writeBytes(cancion.getArtista());
        indice.writeInt((int) indice.length());
        indice.seek(6006);
        indice.writeShort(tamanio2+5+cancion.getArtista().length());
            
        /*Pista*/
        indice.seek(26008);
        tamanio1=indice.readInt();
        pista=tamanio1;
        indice.seek(tamanio1);
        indice.writeByte(cancion.getPista().length());
        indice.writeBytes(cancion.getPista());
        indice.writeInt((int) almacen.length());
        indice.seek(26008);
        indice.writeInt(tamanio1+5+cancion.getPista().length());
            
        /*Ruta*/
        almacen.seek(4);
        tamanio1=almacen.readInt();
        direccion=tamanio1;
        almacen.seek(tamanio1);
        almacen.writeByte(cancion.getDireccion().length());
        almacen.writeBytes(cancion.getDireccion());
        almacen.seek(4);
        almacen.writeInt(tamanio1+1+cancion.getDireccion().length());
                
        /*Genero*/
        almacen.seek(101008);
        tamanio1=almacen.readInt();
        genero=tamanio1;
        almacen.seek(tamanio1);
        almacen.writeByte(cancion.getGenero().length());
        almacen.writeBytes(cancion.getGenero());
        almacen.seek(101008);
        almacen.writeInt(tamanio1+1+cancion.getGenero().length());

        /*Disquera*/
        almacen.seek(202012);
        tamanio1=almacen.readInt();
        disquera=tamanio1;
        almacen.seek(tamanio1);
        almacen.writeByte(cancion.getDisquera().length());
        almacen.writeBytes(cancion.getDisquera());
        almacen.seek(202012);
        almacen.writeInt(tamanio1+1+cancion.getDisquera().length());
                
        /*Album*/
        almacen.seek(223016);
        tamanio1=almacen.readInt();
        album=tamanio1;
        almacen.seek(tamanio1);
        almacen.writeByte(cancion.getAlbum().length());
        almacen.writeBytes(cancion.getAlbum());
        almacen.seek(223016);
        almacen.writeInt(tamanio1+1+cancion.getAlbum().length());
        
        /*Enlace Artista*/
        almacen.seek(244020);
        tamanio1=almacen.readInt();
        enlaceA=tamanio1;
        almacen.seek(tamanio1);
        almacen.writeByte(cancion.getEnlaceArtista().length());
        almacen.writeBytes(cancion.getEnlaceArtista());
        almacen.seek(244020);
        almacen.writeInt(tamanio1+1+cancion.getEnlaceArtista().length());
        
        /*Enlace Disquera*/
        almacen.seek(295024);
        tamanio1=almacen.readInt();
        enlaceD=tamanio1;
        almacen.seek(tamanio1);
        almacen.writeByte(cancion.getEnlaceDisquera().length());
        almacen.writeBytes(cancion.getEnlaceDisquera());
        almacen.seek(295024);
        almacen.writeInt(tamanio1+1+cancion.getEnlaceDisquera().length());
        
        /*Enlace Otros*/
        almacen.seek(346028);
        tamanio1=almacen.readInt();
        enlaceO=tamanio1;
        almacen.seek(tamanio1);
        almacen.writeByte(cancion.getEnlaceOtros().length());
        almacen.writeBytes(cancion.getEnlaceOtros());
        almacen.seek(346028);
        almacen.writeInt(tamanio1+1+cancion.getEnlaceOtros().length());
        
        /*Informacion*/
        informacion.seek(4);
        tamanio1=informacion.readInt();
        infor=tamanio1;
        informacion.seek(tamanio1);
        informacion.writeInt(cancion.getInfo().length());
        informacion.writeBytes(cancion.getInfo());
        informacion.seek(4);
        informacion.writeInt(tamanio1+4+cancion.getInfo().length());
        
        /*Letra*/
        letra.seek(4);
        tamanio1=letra.readInt();
        let=tamanio1;
        letra.seek(tamanio1);
        letra.writeInt(cancion.getLetra().length());
        letra.writeBytes(cancion.getLetra());
        letra.seek(4);
        letra.writeInt(tamanio1+4+cancion.getLetra().length());
        
        /*Registro*/
        almacen.seek(397032);
        tamanio1=almacen.readInt();
        almacen.seek(tamanio1);
        almacen.writeShort(pista);
        almacen.writeInt(disquera);
        almacen.writeShort(artista);
        almacen.writeInt(album);
        almacen.writeShort(anio);
        almacen.writeInt(genero);
        almacen.writeInt(direccion);
        almacen.writeShort(cancion.getDuracion());
        almacen.writeInt(enlaceA);
        almacen.writeInt(enlaceD);
        almacen.writeInt(enlaceO);
        almacen.writeInt(infor);
        almacen.writeInt(let);
        almacen.seek(397032);
        almacen.writeInt(tamanio1+44);
        
        
    }
    
    public void Leer(String ruta,Cancion nuevo){
        FileInputStream archivo = null;
        try {
            archivo = new FileInputStream(ruta); //Obtiene el archivo
            RandomAccessFile a = new RandomAccessFile("auxiliar.data", "rw");
            /*VERIFICA QUE SEA UN ARCHIVO MP3*/
            for(int i=0;i<3;i++){
                a.write(archivo.read());
            }
            a.seek(0);
            String etiqueta="";
            for(int i=0; i<3;i++){
                etiqueta=etiqueta+(char)a.readByte();
            }
            if(etiqueta.equals("ID3")){
                //Copia el archivo a un archivo auxiliar para facilitar la lectura
                a.write(archivo.readAllBytes());
                etiqueta="";
                a.seek(6);
                etiqueta="";
                ////////////////////TAMAÑO DEL ARCHIVO////////////////////
                long aux;
                for(int i=0;i<4;i++){   //Decodifica los bytes convirtiendolos a binario
                    aux = a.readByte();
                    String binario = Long.toBinaryString(aux);
                    while(binario.length()!=7){     //Se agregan caracteres
                        binario="0"+binario;
                    }
                    etiqueta=etiqueta+binario;  //Se juntan los bytes
                }
                short tamanioA=0;
                for(int i=0;i<8;i++){   //Se convierte el binario en decimal
                    if(etiqueta.charAt(etiqueta.length()-1-i)=='1'){
                        tamanioA = (short) (tamanioA + Math.pow(2, i)); //Se obtiene el tamaño de la etiqueta
                    }
                }
                /*EL ARCHIVO FUE LEIDO*/
                /*Escritura*/
                etiqueta=BuscarInfo(tamanioA, a, "TIT2");
                if(!etiqueta.equals("No Disponible")){
                    nuevo.setPista(etiqueta);
                }
                nuevo.setDisquera(BuscarInfo(tamanioA, a, "TPUB"));
                nuevo.setArtista(BuscarInfo(tamanioA, a, "TPE1"));
                nuevo.setAlbum(BuscarInfo(tamanioA, a, "TALB"));
                etiqueta=BuscarInfo(tamanioA, a, "TYER");
                if(etiqueta.equals("-")){
                    etiqueta=BuscarInfo(tamanioA, a, "TDRC");
                }
                if(etiqueta.equals("-") || etiqueta.trim().length()>4){
                    nuevo.setAnio(Short.parseShort("0"));
                }
                else{
                    nuevo.setAnio(Short.parseShort(etiqueta.trim()));
                }
                nuevo.setGenero(BuscarInfo(tamanioA, a, "TMOO"));
                nuevo.setLetra(BuscarInfo(tamanioA, a, "SYLT"));
                nuevo.setEnlaceArtista(BuscarInfo(tamanioA, a, "WOAR"));
                nuevo.setEnlaceDisquera(BuscarInfo(tamanioA, a, "WPUB"));
                nuevo.setEnlaceOtros(BuscarInfo(tamanioA, a, "WOAS"));
                nuevo.setInfo(BuscarInfo(tamanioA, a, "COMM")+"\n"+BuscarInfo(tamanioA, a, "TXXX"));
                lista.addElement(nuevo.getPista());
                jList1.setModel(lista);
                canciones.add(nuevo);
            }
            archivo.close();
            a.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
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

        jFileChooser1 = new javax.swing.JFileChooser();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();

        setTitle("Selector de Archivos");
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jFileChooser1.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);
        jFileChooser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooser1ActionPerformed(evt);
            }
        });

        jButton1.setText("Finalizar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {};
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        jLabel1.setText("Canciones Seleccionadas");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFileChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 94, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jFileChooser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileChooser1ActionPerformed
        String ruta=jFileChooser1.getCurrentDirectory().getAbsolutePath()+"\\"+jFileChooser1.getSelectedFile().getName();
        this.Ingresar(ruta);
    }//GEN-LAST:event_jFileChooser1ActionPerformed
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setVisible(false);
        if(!canciones.isEmpty()){
            try {
                for(int i=0; i<canciones.size();i++){
                    Escribir(canciones.get(i));
              }
            } catch (IOException ex) {
                Logger.getLogger(Lector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(Lector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Lector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Lector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Lector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Lector().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
