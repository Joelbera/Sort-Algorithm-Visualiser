import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction; 
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.Timer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.function.ToLongBiFunction;
import java.util.Scanner;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*; 
import java.awt.BasicStroke;
import java.awt.Font;

import java.nio.charset.StandardCharsets;
import java.io.IOException;



public class Frame extends JPanel implements ActionListener, KeyListener{
    
    ArrayList<Integer> sortList; 
    
    JFrame window; 
    JMenuBar menuBar;
    JMenu fileMenu ,algorithmMenu; 
    JMenuItem start, upload, exit, bubbleSortMenu; 
    JTextField addValue ; 
    StringBuilder str = new StringBuilder();
    int largest, currentIndex; 
    File file; 
    String sortType; 
    Sort sortHandler;
    
    Timer timer = new Timer(100, this);

    Font f = new Font("Dialog", Font.PLAIN, 15);

    public Frame(){
        sortList = new ArrayList(); 
        sortType = "bubbleSort";   
        
        sortHandler = new Sort(this); 
        currentIndex = 0; 

        addKeyListener(this);
        setFocusable(true);

        window = new JFrame("Sort Visualiser"); 
        window.setContentPane(this);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setPreferredSize(new Dimension(700, 500));
        window.getContentPane().setBackground(Color.decode("#1e1e1e"));

        UIManager.put("PopupMenu.border", new LineBorder(null));
        UIManager.put("MenuItem.background", Color.black); 
        UIManager.put("MenuItem.foreground", Color.decode("#BDBDBD"));
        UIManager.put("MenuItem.border", new LineBorder(Color.decode("#9C87F0")));

        menuBar = new JMenuBar(); 
        menuBar.setBackground(Color.black);
        window.setJMenuBar(menuBar);

        fileMenu = new JMenu("File"); 
        fileMenu.setForeground(Color.white);
        fileMenu.setBorder(null);
        menuBar.add(fileMenu);

        start = new JMenuItem("Start"); 
        start.addActionListener(e -> {
            sortHandler.start(sortList, sortType);
        });
        fileMenu.add(start);

        upload = new JMenuItem("Upload");
        upload.addActionListener(e -> {
            selectFile();
        });
        fileMenu.add(upload);
        
        
        exit = new JMenuItem("Exit");
        exit.addActionListener(e -> {
            exitApplication();
        });
        fileMenu.add(exit); 

        algorithmMenu = new JMenu("Algoritms"); 
        algorithmMenu.setForeground(Color.white);
        algorithmMenu.setBorder(null);
        menuBar.add(algorithmMenu); 

        bubbleSortMenu = new JMenuItem("Bubble Sort");
        algorithmMenu.add(bubbleSortMenu); 

        window.pack();
        window.setVisible(true);
        
        this.revalidate();
        this.repaint();
       
    }
    
    public void paint(Graphics g){
        super.paintComponent(g);

        int height = this.getHeight(); 
        int width = this.getWidth();  
        double largestHeight =  height * 0.90;
        double rectHeight;

        g.setColor(Color.white);
        g.setFont(f);
        g.drawString("Algorithm: " + sortType, (int) (width * 0.1), (int) (height * 0.05));
        g.drawString("Passes: " + Integer.toString(sortHandler.getPasses()), (int) (width * 0.4), (int) (height * 0.05));
        g.drawString("Running: " + Boolean.toString(sortHandler.isRunning()), (int) (width * 0.6), (int) (height * 0.05));
        
        
        if (sortList.size() != 0){
            int objectWidth = width / sortList.size();
            for(int i = 0; i < sortList.size(); i++){  
                rectHeight = (sortList.get(i) * largestHeight)/largest;
                if (i == currentIndex) {
                    g.setColor(Color.decode("#E0C4F2"));     
                }
                else {
                    g.setColor(Color.decode("#9C87F0"));
                }
                
                g.fillRect(objectWidth*i, height - (int) rectHeight, objectWidth, (int) rectHeight);
                g.setColor(Color.decode("#14111F"));
                g.drawRect(objectWidth*i, height - (int) rectHeight, objectWidth, (int) rectHeight);
            }
        }

        repaint();
        revalidate();
        
    }

    public static void main(String[] args) {
        new Frame(); 
    }

    public void selectFile() { 
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            String fileName = file.getName(); 
            if (fileName.substring(fileName.lastIndexOf("."), fileName.length()).toLowerCase().equals(".txt")){
                readFile(file); 
            }
        } else {
        }
    }

    public void readFile(File file){ 
        try {
            Scanner fileReader = new Scanner(new FileReader(file));
            
            String str = fileReader.nextLine(); 
            StringBuilder tempString = new StringBuilder(); 
            for(int i = 0; i < str.length(); i++){
                if(Character.isDigit(str.charAt(i))){
                     tempString.append(str.charAt(i));
                }
                else if(str.charAt(i) == ','){
                    if (sortList.size() == 0){
                        largest = Integer.parseInt(tempString.toString());
                    }
                    else if (Integer.parseInt(tempString.toString()) > largest){
                        largest = Integer.parseInt(tempString.toString()); 
                    }
                    sortList.add(Integer.parseInt(tempString.toString()));
                    tempString.setLength(0);
                }
            }
        }
        catch (FileNotFoundException e){
            return; 
        }
        catch (IOException e){
            return;
        }
    }
    

    public void mapKeys(KeyEvent e){  
        if (Character.isDigit(e.getKeyChar())){
            str.append(e.getKeyChar());
        } 
        else if ( e.getKeyCode() == KeyEvent.VK_ENTER && str.length() > 0) {
            int intVal = Integer.parseInt(str.toString()); 
            
            if (sortList.size() == 0){
                largest = intVal;
            }
            else if (intVal > largest){
                largest = intVal; 
            }

            sortList.add(intVal);
            str.setLength(0);
            System.out.println("Enter"); 
            repaint();
        }
    }

    @Override 
    public void keyPressed(KeyEvent e){  
        mapKeys(e); 
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
    
    public void exitApplication(){
        System.exit(0);
    }

    public void updateSortList(ArrayList<Integer> list){
        sortList = list; 
    }

    public void currentIndex(int index){
        currentIndex = index; 
    }
}