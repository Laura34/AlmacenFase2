/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Usuario 1
 */
public class Cancion {
    private String pista;   //v+1
    private String disquera;    //v+1
    private String artista; //v+1
    private String album;   //v+1
    private short anio; //2
    private String genero;  //v+1
    private String direccion;   //v+1
    private short duracion; //2
    private String letra;   //v+4
    private String enlaceArtista;   //v+1
    private String enlaceDisquera;  //v+1
    private String enlaceOtros; //v+1
    private String info;    //v+4

    public Cancion() {
        this.pista = "";
        this.disquera = "";
        this.artista = "";
        this.album = "";
        this.anio = 0;
        this.genero = "";
        this.direccion = "";
        this.duracion = 0;
        this.letra = "";
        this.enlaceArtista = "";
        this.enlaceDisquera = "";
        this.enlaceOtros = "";
        this.info = "";
    }

    public String getPista() {
        return pista;
    }

    public void setPista(String pista) {
        this.pista = pista;
    }

    public String getDisquera() {
        return disquera;
    }

    public void setDisquera(String disquera) {
        this.disquera = disquera;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public short getAnio() {
        return anio;
    }

    public void setAnio(short anio) {
        this.anio = anio;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public short getDuracion() {
        return duracion;
    }

    public void setDuracion(short duracion) {
        this.duracion = duracion;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public String getEnlaceArtista() {
        return enlaceArtista;
    }

    public void setEnlaceArtista(String enlaceArtista) {
        this.enlaceArtista = enlaceArtista;
    }

    public String getEnlaceDisquera() {
        return enlaceDisquera;
    }

    public void setEnlaceDisquera(String enlaceDisquera) {
        this.enlaceDisquera = enlaceDisquera;
    }

    public String getEnlaceOtros() {
        return enlaceOtros;
    }

    public void setEnlaceOtros(String enlaceOtros) {
        this.enlaceOtros = enlaceOtros;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
