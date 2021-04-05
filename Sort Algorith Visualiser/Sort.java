import java.util.ArrayList;

import javax.swing.SwingUtilities;

public class Sort{

    private ArrayList<Integer> sortlist; 
    private Frame frame;
    private boolean running; 
    private int passes = 0; 
    private int n;
    private String timeComplexity = ""; 

    public Sort(Frame frame){
        this.frame = frame; 
    }

    public void start( ArrayList<Integer> sortList, String sortType){
        this.sortlist = sortList;      
        running = true; 
    
        if (sortType == "bubbleSort"){
            bubbleSort();
        }
        else if (sortType == "quickSort"){
        }
    }


    void bubbleSort(){
        new Thread( new Runnable(){
            public void run(){
                boolean swapped;
                for (int i = 0; i < sortlist.size() - 1; i++){
                    swapped = false;
                    passes ++; 
                    for (int j = 0; i < sortlist.size() - j - 1; j++){ 
                        if (sortlist.get(j) > sortlist.get(j+1)){
                            int index = j; 
                            swap(j, j+1);
                            swapped = true;
                            n++;

                            SwingUtilities.invokeLater(new Runnable(){
                               public void run() {
                                   frame.updateSortList(sortlist);
                                   frame.currentIndex(index);
                                   frame.repaint();
                               }
                            });

                            try {
                                java.lang.Thread.sleep(1);
                            }
                            catch(Exception e) { }
                        }
                    }
                    if (swapped == false){
                        running = false; 
                        frame.currentIndex(-1);
                        n = 0; 
                        break;
                    }
                }  
            }
        }).start();

        frame.repaint();
    } 

    //void quickSort(ArrayList<Integer> list, int lo, int hi){}

    //private ArrayList<Integer> partition(){}

    private void swap(int index1, int index2){
        int tempVal = sortlist.get(index1);
        sortlist.set(index1, sortlist.get(index2)); 
        sortlist.set(index2, tempVal);
    }

    public boolean isRunning(){
        return running; 
    }

    public int getPasses(){
        return passes; 
    }
}
