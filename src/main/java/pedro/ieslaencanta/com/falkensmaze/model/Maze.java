/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.falkensmaze.model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import pedro.ieslaencanta.com.falkensmaze.Size;

/**
 *
 * @author Pedro
 */
@XmlRootElement
public class Maze implements Serializable {

    private Size size;
    private Block[][] blocks;
    private double time;
    private String sound;

    public Maze() {
    }

    public void init() {
        this.setBlocks(new Block[this.getSize().getHeight()][this.getSize().getWidth()]);
        for (int i = 0; i < this.getBlocks().length; i++) {
            for (int j = 0; j < this.getBlocks()[i].length; j++) {
                this.getBlocks()[i][j] = new Block();

            }
        }
    }

    public void reset() {
        for (int i = 0; i < this.getBlocks().length; i++) {
            for (int j = 0; j < this.getBlocks()[i].length; j++) {
                this.getBlocks()[i][j] = null;

            }
        }
        this.setBlocks(null);
    }

    public void reset(Size newsize) {
        this.reset();;
        this.setSize(newsize);
        this.init();
    }

    public void setBlockValue(String value, int row, int col) {
        this.getBlocks()[col][row].setValue(value);
    }

    public String getBlockValue(int row, int col) {
        return this.getBlocks()[row][col].getValue();
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block[][] blocks) {
        this.blocks = blocks;
    }

    public static Maze load(File file) throws IOException, FileNotFoundException, ClassNotFoundException, Exception  {
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        Maze m = null;
        if(extension.toLowerCase().equals("bin")){
         //para cargar, es simplemente loadBin, o load la extension deseada.
            m = Maze.loadBin(file);            
        } else if (extension.toLowerCase().equals("json")){
            m = Maze.loadJSON(file);
        } else if (extension.toLowerCase().equals("xml")){
            m = Maze.loadXML(file);
        } else {
            throw new Exception("No se puede cargar esa extension");
        }
        return m;

    }

    public static void save(Maze maze, File file) throws IOException, FileNotFoundException, ClassNotFoundException, Exception {
        if (maze.sound == null || maze.sound.equals("")) {
            String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
       //     throw new Exception("Es necesario indicar el sonido del laberinto");
       if(extension.toLowerCase().equals("bin")){
           //si el final del objeto o sea la extension, condcide con bin, se almacena
            saveBin(maze, file);
       } else if(extension.toLowerCase().equals("json")){
           //si el final del objeto o sea la extension, condcide con json, se almacena
            saveJSON(maze, file);
        } else if(extension.toLowerCase().equals("xml")){
            //si el final del objeto o sea la extension, condcide con xml, se almacena
            saveXML(maze, file);
        } else {
             throw new Exception("No se almacena con esa extensión");
        }
       
        }
    }
    private static Maze loadJSON (File file) throws IOException, FileNotFoundException, ClassNotFoundException, JAXBException  {
       Gson gson = new Gson();
       String json = gson.toJson(file);
       Maze m =gson.fromJson(json, Maze.class);
       return m;
    }

    private static Maze loadXML(File file) throws FileNotFoundException, IOException, ClassNotFoundException, JAXBException  {
      JAXBContext context = JAXBContext.newInstance( Maze.class );
      Unmarshaller unmarshaller = context.createUnmarshaller();
      Maze m = (Maze)unmarshaller.unmarshal(file);
      return m;
    }

    public static Maze loadBin(File file) throws FileNotFoundException, IOException, ClassNotFoundException  {
      FileInputStream os = new FileInputStream(file);
      ObjectInputStream oss = new ObjectInputStream(os);
      Maze m = (Maze) oss.readObject();
      oss.close();
      os.close();
       return m; 
    }

    private static void saveJSON(Maze maze, File file) throws IOException, FileNotFoundException, ClassNotFoundException, JAXBException  {
       Gson gson = new Gson();
       String json = gson.toJson(maze);
       FileWriter fw = new FileWriter(file);
       fw.write(json);
       fw.close();
       /*
       Maze m = gson.fromJson(json,Maze.class);    
       oss.writeObject(m);*/
    }

    private static void saveXML(Maze maze, File file) throws FileNotFoundException, IOException, JAXBException  {
      FileOutputStream os = new FileOutputStream(file);    
      JAXBContext contexto = JAXBContext.newInstance(maze.getClass());
      Marshaller marshaller = contexto.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
      marshaller.marshal(maze, os);
      os.close();
      

    }

    public static void saveBin(Maze maze, File file) throws FileNotFoundException, IOException  {
      FileOutputStream os = new FileOutputStream(file);
      ObjectOutputStream oss= new ObjectOutputStream(os);
      oss.writeObject(maze);
      os.close();
      oss.close();                    
    }

}
