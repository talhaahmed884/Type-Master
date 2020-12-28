import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.Scanner;
import java.util.Random;


public class TypingMaster implements ActionListener{
    JPanel panel = new JPanel();        //for adding components in the window
    JFrame frame = new JFrame();        //for the new window creation
    public int activeLabel = 4;             //total number of words visible in the game
    JLabel wordLabel[] = new JLabel[activeLabel];           //for displaying each word in the game
    JLabel scoreLabel = new JLabel("Score: 0");                 //displays score
    JTextField userText;            //for getting input from the user in the game
    String color[] = new String[activeLabel];               //color code of each word in the game
    int score = 0;              //saves current score of the user
    String wordUsed[] = new String[activeLabel], wordSave[] = new String[activeLabel];          //words being displayed in the game
    boolean EndLabel[] = new boolean[activeLabel];              //when a word has been input by the user this is set to true, it makes the correctly entered word disappear from the game
    String labelText1 ="<html><FONT COLOR=GREEN>", labelText2 = "</FONT><FONT COLOR=BLUE>", labelText3 = "</FONT></html>";          //color labels for the words
    String charpress[] = new String[activeLabel];           //for storing words entered by the user in the game 
    int speed = 50;                 //speed of words going down in the game
    char CurrentEnteredChar;                //each character being entered by the user in the game
    public String removeFirstChar(String s){        //rmoves a character from the string given in the parameters and return it
        return s.substring(1);
     }
    public class KeyCatcher implements KeyListener {            //get user input given through keyboard, match it with all the words displayed on the screen and change the colors accordingly

        @Override
        public void keyTyped(KeyEvent e){}
        @Override
        public void keyPressed(KeyEvent e){}
        @Override
        public void keyReleased(KeyEvent e){
            for(int a=0; a<activeLabel; a++)            //running for all the words being actively displayed on the screen
            {
                CurrentEnteredChar = e.getKeyChar();            //gets the current character enetered by the user
                if( !wordUsed[a].isEmpty() ){
                    if(CurrentEnteredChar == wordUsed[a].charAt(0)){            //compares it with the words displayed on the display
                        charpress[a] += CurrentEnteredChar;             //if it matches, save it to the characters entered so far
                        wordUsed[a] = removeFirstChar(wordUsed[a]);             //remove the character from the word being displayed on the screen to change the color codes
    
                        setLabelText();
                    }
                }
            }
        }
    }


    public void setLabelText()          //set color code for the word on the display
    {
        for(int a=0; a<activeLabel; a++)        //for all the words being actively displayed on the screen
        {
            labelText2 = "</FONT><FONT COLOR=";     //HTML code for setting the color
            labelText2 += color[a];                     //color of the word at respective position in the screen
            labelText2 += ">";              //HTML code
            
            wordLabel[a].setText(labelText1 + charpress[a] + labelText2 + wordUsed[a] + labelText3);        //setting the custom color
        }

        return;
    }
    
    public TypingMaster() {         //default constructor
        
        DrawingBoard();             //set parameters for the window and add panel to the window for various componenets
        
        userText = new JTextField(20);          //initalizes the user input field
        userText.addActionListener(this);               //intialize the listner for actions on every key press
        userText.setBounds(200,325,100,25);                 //set the size and dimensions of input field
        userText.addKeyListener(new KeyCatcher());              //set the keyboard listener
        panel.add(userText);                    //add the input field to the panel to be displayed on the screen

        scoreLabel.setForeground(Color.black);              //set color for the score
        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 18));          //setting the font style and size for the score
        scoreLabel.setBounds(390,5,170,25);         //setting size and dimension for score
        panel.add(scoreLabel);                  //adding score label to the panel to be displayed on screen

        panel.setBackground(Color.white);           //setting the panels color
        frame.setVisible(true);                 //making window visible

        for(int a=0; a<activeLabel; a++)                //creating instances for the word label and storage containers
        {
            wordLabel[a] = new JLabel();
            EndLabel[a] = false;
            wordUsed[a] = new String();
            wordSave[a] = new String();
            charpress[a] = new String();
        }
    }


    public void DroppingWords(){            //mechanism for displaying words on the screen
        
        for(int a=0; a<activeLabel; a++)        //runs for all the words being displayed on the screen
        {
            wordUsed[a] = wordGrabber();            //grab the random word the word generator
            wordSave[a] = wordUsed[a];              //makes a copy of it
            wordLabel[a].setText(wordUsed[a]);              //save that word in the label to be displayed on the screen
            wordLabel[a].setForeground(Color.BLUE);         //sets the default color to blue
            wordLabel[a].setFont(new Font("Monospaced", Font.BOLD, 17));            //setting the font style and size for the word
            wordLabel[a].setBounds(10,20,170,25);
            
            panel.add(wordLabel[a]);            //adding the label to panel to be displayed
    
            if(EndLabel[a])         //if a word has been entered correctly, all of its data structures must be reset
            {
                EndLabel[a] = false;
                wordLabel[a].setVisible(true);
                charpress[a] = "";
            }

            panel.revalidate();         
            panel.repaint();        //repainting the window for all the updates to be displayed on the screen
        } 
        
           
        panel.add(scoreLabel);          //adding the score label to display score
    }

    
    @Override
    public void actionPerformed(ActionEvent e){         //action performed when enter key is pressed
        for(int a=0; a<activeLabel; a++)        //for all words being displayed on the screen
        {
            String input_word = userText.getText();         //get the input entered by the user
            if(input_word.equals(wordSave[a])){             //if user has entered the correct word
                wordLabel[a].setVisible(false);                 //disappear the word from display
                panel.remove(wordLabel[a]);             //remove it from the panel
                
                EndLabel[a] = true;
                userText.setText("");           //reset the user input field
                
                score++;            //increments the score
                scoreLabel.setText("Score: " + score);          //displays the updated score
            }
        }
    }


    public static void read(String [] words){       //reads the words from a file for the game
        try {
            String path = System.getProperty("user.dir");           //get the user directory for file reading
            
            File file = new File(path + "/words.txt");          //opens the file for reading
            Scanner scan = new Scanner(file);           //scanner for reading from file

            int count = 0;
            while(scan.hasNextLine()){      //reads untill file ends
                words[count] = scan.nextLine();         //scans the file and count total words in the file
                count++;
            }
        } catch (Exception e) {         //in case of error
          System.out.println(e.getClass());         //prints the error trail
        }
    }


    public static String wordGrabber(){         //grabs the wors from the file and store it inside a container and randomly return a word at each call
        String words[] = new String[500];       //container for storing words from file
        read(words);            //read words from file
        Random randNum = new Random();          //creates a random number generator object
        int index = randNum.nextInt(499);                   //generates the random number
        return (words[index]);          //returns the random word from the file
    }


    public void FireAnimation(){            //for fire animation being displayed at the end of the game
        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("fire.gif"));           //instances of fire gif
        ImageIcon imageIcon2 = new ImageIcon(this.getClass().getResource("fire.gif"));
        ImageIcon imageIcon3 = new ImageIcon(this.getClass().getResource("fire.gif"));
        ImageIcon imageIcon4 = new ImageIcon(this.getClass().getResource("fire.gif"));
        ImageIcon imageIcon5 = new ImageIcon(this.getClass().getResource("fire.gif"));

        JLabel fire = new JLabel(imageIcon);        //setting each instance of fir gif
        imageIcon.setImageObserver(fire);
        fire.setBounds(15, 320, 80, 50);
        panel.add(fire);
        JLabel fire2 = new JLabel(imageIcon2);
        imageIcon2.setImageObserver(fire2);
        fire2.setBounds(100, 320, 80, 50);
        panel.add(fire2);
        JLabel fire3 = new JLabel(imageIcon3);
        imageIcon3.setImageObserver(fire3);
        fire3.setBounds(200, 350, 80, 20);
        panel.add(fire3);
        JLabel fire4 = new JLabel(imageIcon4);
        imageIcon4.setImageObserver(fire4);
        fire4.setBounds(310, 320, 80, 50);
        panel.add(fire4);
        JLabel fire5 = new JLabel(imageIcon5);
        imageIcon5.setImageObserver(fire5);
        fire5.setBounds(400, 320, 80, 50);
        panel.add(fire5);
        
    }


    public void DrawingBoard(){ //for setting the dimensions of the window
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           //setting the operation when close window is pressed
        frame.add(panel);       //adding the panel in the window for all the componenets
        
        panel.setLayout(null);      //setting the layout to null
        
        FireAnimation();        //starting the fire animations on the display
    }


    public void moveLabel(){            //for moving the words on the display
        int b[] = new int[activeLabel];         //for x coordingates of all words on the display

        int col = 10;

        for(int a=0; a<activeLabel; a++)            //setting the x coordinated for all the words
        {
            b[a] = col;
            col += wordUsed[a].length()+100;        //setting columns for each word
        }

        Random rand = new Random();
        int d = rand.nextInt(20);           //randomly generating the rows for each word

        int allFalse = 1;

        for(int a=21; a<500; a++){      //runs to the end of the board
        
            int f = d;
            allFalse = 1;       //if all words are entered correctly by the user
            for(int c = 0; c<activeLabel; c++)          //runs for all the words on the display
            {
                if(!EndLabel[c])        //if is word hasnt entered by the user
                {                   
                    wordLabel[c].setBounds(b[c],a-f,170,25);        //sets the word dimensions
                    color[c] = "BLUE";      //sets the deauflt color
                    if(a>180){      //if words are lower than a range changes the color
                        wordLabel[c].setForeground(Color.orange);
                        color[c] = "ORANGE";        //sets the color to orange
                        setLabelText();         //updates the color of the word
                    }
                    if(a>250){          //if words are about to touch the fire
                        wordLabel[c].setForeground(Color.red);
                        color[c] = "RED";       //changes the color to red
                        setLabelText();         //updates the color
                    }
                    if(a>290){              //if any word touches the fire
                        if(EndLabel[c] == false){       //if that word was being displayed
                            System.out.println("Game_End");
                            System.exit(0);         //ends the game
                        }
                    }
                    
                    f+= 10;

                    allFalse = 0;
                }
            }
            
            if(allFalse == 0)       //if any word is present on the display
            {
                try{
                    Thread.sleep(speed);        //timer for updating the coordinates of the word
                }
                catch(InterruptedException e){
                    System.out.println(e);
                }
            }
            else
            {
                break;
            }
        }

        speed++;
    }


    public static void main(String[] args) {        //main for running the game
        TypingMaster game = new TypingMaster();         //creates the object for the game

        while(true)         //while the false condition is not reached
        {
            game.DroppingWords();       //creates words
            game.moveLabel();               //move labels

        }
    }

}