
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

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
                    nuevo.setPista(ficheros[i]);
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
        int puntero1=4;
        short puntero2=4;
        short anio;
        short artista;
        int pista;
        int direccion=-1;
        int genero = 0;
        int disquera = 0;
        int album = 0;
        int enlaceA = 0;
        int enlaceD = 0;
        int enlaceO = 0;
        if(!archivo.exists()){  //Si es el primer registro
            RandomAccessFile almacen = new RandomAccessFile("Almacen.data","rw");
            RandomAccessFile indice = new RandomAccessFile("Indice.data","rw");
            RandomAccessFile informacion = new RandomAccessFile("Informacion.data","rw");
            RandomAccessFile letra = new RandomAccessFile("Letras.data","rw");
            /*Escribe el Almacen*/
            /*DIRECCION*/
            almacen.writeBytes("ALMA");
            almacen.writeInt(101008);   //Puntero al final de la seccion
            direccion=(int) almacen.length();
            almacen.writeByte(cancion.getDireccion().length());
            almacen.writeBytes(cancion.getDireccion());
            /*GÉNERO*/
            almacen.seek(101008);
            almacen.writeInt(122012);
            genero=(int) almacen.length();
            almacen.writeByte(cancion.getGenero().length());
            almacen.writeBytes(cancion.getGenero());
            /*DISQUERA*/
            almacen.seek(122012);
            almacen.writeInt(143016);
            disquera=(int) almacen.length();
            almacen.writeByte(cancion.getDisquera().length());
            almacen.writeBytes(cancion.getDisquera());
            /*ÁLBUM*/
            almacen.seek(143016);
            almacen.writeInt(164020);
            album=(int) almacen.length();
            almacen.writeByte(cancion.getAlbum().length());
            almacen.writeBytes(cancion.getAlbum());
            /*ENLACE DE ARTISTA*/
            almacen.seek(164020);
            almacen.writeInt(317036);
            almacen.writeInt(215028);
            enlaceA=(int) almacen.length();
            almacen.writeByte(cancion.getEnlaceArtista().length());
            almacen.writeBytes(cancion.getEnlaceArtista());
            /*ENLACE DE DISQUERA*/
            almacen.seek(215028);
            almacen.writeInt(266032);
            enlaceD=(int) almacen.length();
            almacen.writeByte(cancion.getEnlaceDisquera().length());
            almacen.writeBytes(cancion.getEnlaceDisquera());
            /*ENLACE DE OTROS*/
            almacen.seek(266032);
            almacen.writeInt(317036);
            enlaceO=(int) almacen.length();
            almacen.writeByte(cancion.getEnlaceOtros().length());
            almacen.writeBytes(cancion.getEnlaceOtros());
            /*INDICE*/
            indice.writeBytes("INDX");
            /*Años*/
            indice.writeShort(14);    //Puntero al final del Bloque
            indice.writeShort(1);   //Cantidad de registros
            anio=(short) indice.length();
            indice.writeShort(cancion.getAnio());
            indice.writeInt(30012); //Puntero a la pista
            /*Artistas*/
            indice.seek(8006);  //Va al siguiente bloque
            indice.writeShort(8008+7+cancion.getArtista().length());   //Puntero al final del Bloque
            indice.writeShort(1);   //Cantidad de registros
            artista=(short) indice.length();    //Puntero al artista para el registro
            indice.writeByte(cancion.getArtista().length());    //Longitud de la cadena
            indice.writeBytes(cancion.getArtista());    //Artista
            indice.writeInt(30012); //Puntero a la pista
            /*Pistas*/
            indice.seek(30008);
            indice.writeInt(30012+1+cancion.getPista().length()+4);
            pista = (int) indice.length();
            indice.writeByte(cancion.getPista().length());
            indice.writeBytes(cancion.getPista());
            indice.writeInt(317040+4);   //Guarda el puntero al inicio del registro
            /*REGISTRO*/
            almacen.seek(317036);
            almacen.writeInt(317036+52);
            almacen.writeInt(317036+52);
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
            /*INFORMACIÓN Y LETRAS*/
            informacion.writeBytes("INFO");
            almacen.writeInt((int) informacion.length());
            informacion.writeInt(cancion.getInfo().length());
            informacion.writeBytes(cancion.getInfo());
            informacion.writeBytes("LETR");
            almacen.writeInt((int) letra.length());
            almacen.writeInt(0);
            letra.writeInt(cancion.getLetra().length());
            letra.writeBytes(cancion.getLetra());
        }
        else{
            //////////////////////////////////////////////////////////////////////////////////
            RandomAccessFile almacen = new RandomAccessFile("Almacen.data","rw");
            RandomAccessFile indice = new RandomAccessFile("Indice.data","rw");
            RandomAccessFile informacion = new RandomAccessFile("Informacion.data","rw");
            RandomAccessFile letra = new RandomAccessFile("Letras.data","rw");
            /*Escribe el Almacen*/
            /*DIRECCION*/
            boolean existe=false;
            int posicion=8;
            almacen.seek(posicion);
            String dato="";
            byte tamanio=almacen.readByte();
            while(tamanio!=0){
                for(int i=0; i<tamanio; i++){
                    dato=dato+(char) almacen.readByte();
                }
                if(cancion.getDireccion().equals(dato)){
                    existe=true;
                    direccion=posicion;
                }
                else{
                    dato="";
                    posicion=posicion+1+tamanio;
                    tamanio=almacen.readByte();
                }
            }
            if(!existe){
                almacen.seek(posicion);
                direccion=posicion;
                almacen.writeByte(cancion.getDireccion().length());
                almacen.writeBytes(cancion.getDireccion());
            }
            /*Género*/
            existe=false;
            posicion=101012;
            almacen.seek(posicion);
            dato="";
            tamanio=almacen.readByte();
            while(tamanio!=0){
                for(int i=0; i<tamanio; i++){
                    dato=dato+(char) almacen.readByte();
                }
                if(cancion.getGenero().equals(dato)){
                    dato="";
                    existe=true;
                    genero=posicion;
                }
                else{
                    posicion=posicion+1+tamanio;
                    tamanio=almacen.readByte();
                }
            }
            if(!existe){
                almacen.seek(posicion);
                genero=posicion;
                almacen.writeByte(cancion.getGenero().length());
                almacen.writeBytes(cancion.getGenero());
            }
            /*Disquera*/
            existe=false;
            posicion=122016;
            almacen.seek(posicion);
            dato="";
            tamanio=almacen.readByte();
            while(tamanio!=0){
                for(int i=0; i<tamanio; i++){
                    dato=dato+(char) almacen.readByte();
                }
                if(cancion.getDisquera().equals(dato)){
                    dato="";
                    existe=true;
                    disquera=posicion;
                }
                else{
                    posicion=posicion+1+tamanio;
                    tamanio=almacen.readByte();
                }
            }
            if(!existe){
                almacen.seek(posicion);
                disquera=posicion;
                almacen.writeByte(cancion.getDisquera().length());
                almacen.writeBytes(cancion.getDisquera());
            }
            /*Álbum*/
            existe=false;
            posicion=143020;
            almacen.seek(posicion);
            dato="";
            tamanio=almacen.readByte();
            while(tamanio!=0){
                for(int i=0; i<tamanio; i++){
                    dato=dato+(char) almacen.readByte();
                }
                if(cancion.getAlbum().equals(dato)){
                    dato="";
                    existe=true;
                    album=posicion;
                }
                else{
                    posicion=posicion+1+tamanio;
                    tamanio=almacen.readByte();
                }
            }
            if(!existe){
                almacen.seek(posicion);
                album=posicion;
                almacen.writeByte(cancion.getAlbum().length());
                almacen.writeBytes(cancion.getAlbum());
            }
            /*EnlaceA*/
            existe=false;
            posicion=164028;
            almacen.seek(posicion);
            dato="";
            tamanio=almacen.readByte();
            while(tamanio!=0){
                for(int i=0; i<tamanio; i++){
                    dato=dato+(char) almacen.readByte();
                }
                if(cancion.getEnlaceArtista().equals(dato)){
                    dato="";
                    existe=true;
                    enlaceA=posicion;
                }
                else{
                    posicion=posicion+1+tamanio;
                    tamanio=almacen.readByte();
                }
            }
            if(!existe){
                almacen.seek(posicion);
                enlaceA=posicion;
                almacen.writeByte(cancion.getEnlaceArtista().length());
                almacen.writeBytes(cancion.getEnlaceArtista());
            }
            /*EnlaceD*/
            existe=false;
            posicion=215032;
            almacen.seek(posicion);
            dato="";
            tamanio=almacen.readByte();
            while(tamanio!=0){
                for(int i=0; i<tamanio; i++){
                    dato=dato+(char) almacen.readByte();
                }
                if(cancion.getEnlaceDisquera().equals(dato)){
                    dato="";
                    existe=true;
                    enlaceD=posicion;
                }
                else{
                    posicion=posicion+1+tamanio;
                    tamanio=almacen.readByte();
                }
            }
            if(!existe){
                almacen.seek(posicion);
                enlaceD=posicion;
                almacen.writeByte(cancion.getEnlaceDisquera().length());
                almacen.writeBytes(cancion.getEnlaceDisquera());
            }
            /*EnlaceO*/
            existe=false;
            posicion=266036;
            almacen.seek(posicion);
            dato="";
            tamanio=almacen.readByte();
            while(tamanio!=0){
                for(int i=0; i<tamanio; i++){
                    dato=dato+(char) almacen.readByte();
                }
                if(cancion.getEnlaceOtros().equals(dato)){
                    dato="";
                    existe=true;
                    enlaceO=posicion;
                }
                else{
                    posicion=posicion+1+tamanio;
                    tamanio=almacen.readByte();
                }
            }
            if(!existe){
                almacen.seek(posicion);
                enlaceO=posicion;
                almacen.writeByte(cancion.getEnlaceOtros().length());
                almacen.writeBytes(cancion.getEnlaceOtros());
            }
            /*INDICE*/
            /*AÑOS*/
            puntero2=4;
            indice.seek(puntero2);
            short tamanioReg=indice.readShort();
            indice.seek(puntero2);
            puntero2=indice.readShort();
            indice.seek(puntero2);
            indice.writeShort(1);
            anio=(short) (puntero2+1);
            indice.writeShort(cancion.getAnio());
            indice.writeInt((int) indice.length());
            indice.seek(4);
            indice.writeShort(tamanioReg+8);
            /*Artistas*/
            puntero2=8006;
            indice.seek(puntero2);
            tamanioReg=indice.readShort();
            indice.seek(puntero2);
            puntero2=indice.readShort();
            indice.seek(puntero2);
            indice.writeShort(1);
            artista=(short) (puntero2+1);
            indice.writeByte(cancion.getArtista().length());
            indice.writeBytes(cancion.getArtista());
            indice.writeInt((int) indice.length());
            indice.seek(8006);
            indice.writeShort(tamanioReg+cancion.getArtista().length()+7);
            /*Pistas*/
            puntero1=30008;
            pista=(int) indice.length();
            indice.seek(puntero1);
            int tamanioPistas=indice.readInt();
            indice.seek(puntero1);
            puntero1=indice.readInt();
            indice.seek(puntero1);
            indice.writeByte(cancion.getPista().length());
            indice.writeBytes(cancion.getPista());
            indice.writeInt((int) almacen.length());
            almacen.seek(almacen.length()-4);
            almacen.writeInt((int) (almacen.length()-4+52));
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
            /*INFORMACIÓN Y LETRAS*/
            informacion.seek(informacion.length());
            almacen.writeInt((int) informacion.length());
            informacion.writeInt(cancion.getInfo().length());
            informacion.writeBytes(cancion.getInfo());
            almacen.writeInt((int) letra.length());
            almacen.writeInt(0);
            letra.writeInt(cancion.getLetra().length());
            letra.writeBytes(cancion.getLetra());
            indice.seek(30008);
            indice.writeInt((int) indice.length());
        }
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
                if(!etiqueta.equals("-")){
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
                Cancion aux=new Cancion();
                aux.setPista("pista 1");
                aux.setDisquera("disquera 1");
                aux.setArtista("artista 1");
                aux.setAlbum("album 1");
                aux.setAnio((short)2000);
                aux.setGenero("genero 1");
                aux.setDireccion("direccion 1");
                aux.setDuracion((short)240);
                aux.setLetra("letra 1");
                aux.setEnlaceArtista("artista1.com");
                aux.setEnlaceDisquera("disquera1.com");
                aux.setEnlaceOtros("otros.com");
                aux.setInfo("info 1");
                Escribir(aux);
//                for(int i=0; i<canciones.size();i++){
//                    Escribir(canciones.get(i));
//              }
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
