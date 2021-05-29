import java.awt.*;
import javax.swing.*;


import java.io.*;
import java.lang.reflect.Array;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.*;


public class SlangGUI 
{
    //define constant variable
    final int _WINDOWN_WIDTH=800;
    final int _WINDOWN_HEIGHT=640;
    final int _BUTTON_WIDTH=200;
    final int _BUTTON_HEIGHT=30;
    
    Boolean _init=false;
    JFrame mainFrame;
    SlangData data;
    String[] key_data;


    private class ButtonSubMenuEvent implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String cmd=e.getActionCommand();
            if(cmd.equals("Back"))
            {
            
                setUpMainDisplay();
            }
        }
    }

    public SlangGUI() throws IOException
    {
        createMainInterface("Slang Word");
        initData();
    }
    public void initData() throws IOException
    {
        FileHandle fh=new FileHandle();
        data=new SlangData();
        String data_path="slang.txt";
        ArrayList<String> words=fh.getData(data_path);
        data.setData(words);

        ArrayList<String> keys=data.getKeyList();
        key_data=keys.toArray(new String[keys.size()]);
    }

    public void saveState() throws IOException
    {
        FileHandle fh=new FileHandle();
        String data_path="slang.txt";
        fh.writeData(data_path,data);
    }

    public JMenuBar setUpMenu()
    {
        JMenuBar menu=new JMenuBar();
        JMenu fileMenu=new JMenu("File");
        fileMenu.setForeground(Color.WHITE);

        fileMenu.add(new JMenuItem("Save ..."));
        fileMenu.add(new JMenuItem("Close Alt+F4"));
        
        JMenu editMenu=new JMenu("Edit");
        editMenu.setForeground(Color.WHITE);
        editMenu.add(new JMenuItem("Copy Ctrl + C"));
        editMenu.add(new JMenuItem("Paste ctrl + V"));

        menu.setBackground(Color.BLACK);

        menu.add(fileMenu,FlowLayout.LEFT);
        menu.add(editMenu);
        return menu;

    }
    public class EditDict
    {
        private JPanel center=new JPanel();
        private JPanel right=new JPanel(new BorderLayout());
        private JList<String> display_list=new JList<>(key_data);

        
        public void showFilter(String cur)
    {
        ArrayList<String> ret=new ArrayList<>();
        for(String temp:key_data)
        {
            if(temp.indexOf(cur,0)>=0)
            ret.add(temp);
        }
        String[] new_display=ret.toArray(new String[ret.size()]);
        display_list.setListData(new_display);
        display_list.setSelectedIndex(0);
        mainFrame.setVisible(true);
    }
        
        

        public void showEditWindows(String cur)
        {
            JFrame edit=new JFrame("Edit Word");
            edit.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            edit.setSize(400,150);
            
            
            JLabel define_label=new JLabel("Word: ");
            JLabel mean_label=new JLabel("Mean: ");
            JTextField define_text=new JTextField();
            define_text.setText(cur);
            JTextField mean_text=new JTextField();
            ArrayList<String> mean=(ArrayList<String>)data.getData().get(cur);
            String mean_display="";
            for (String temp:mean)
            {
                mean_display+=temp+",";
            }
            mean_display=mean_display.substring(0,mean_display.length()-1);
            
            JPanel process=new JPanel(new GridLayout(2,2,5,5));
            process.add(define_label);
            process.add(define_text);
            process.add(mean_label);
            process.add(mean_text);


            mean_text.setText(mean_display);
            JButton confirm=new JButton("Confirm");
            confirm.addMouseListener(new MouseAdapter ()
            {
                public void mouseClicked(MouseEvent e)
                {
                    int choice=JOptionPane.showConfirmDialog(null, "Do you want to change?",
                    "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
                    if(choice==JOptionPane.YES_OPTION)
                    {
                        String new_mean=mean_text.getText();
                        String[] edit_mean=new_mean.split(",");
                        data.editSlang(cur,new ArrayList<String>(Arrays.asList(edit_mean)));
                    }
                }
            });

            edit.add(process,BorderLayout.CENTER);
            edit.add(confirm,BorderLayout.EAST);

            edit.setVisible(true);

        }
        public void showAddWindows(String cur)
        {
            JFrame frame=new JFrame("Add Word");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400,150);
            
            
            JLabel define_label=new JLabel("Word: ");
            JLabel mean_label=new JLabel("Mean: ");
            JTextField define_text=new JTextField();
            
            JTextField mean_text=new JTextField();
            
            JPanel process=new JPanel(new GridLayout(2,2,5,5));
            process.add(define_label);
            process.add(define_text);
            process.add(mean_label);
            process.add(mean_text);

            JButton confirm=new JButton("Confirm");
            confirm.addMouseListener(new MouseAdapter ()
            {
                public void mouseClicked(MouseEvent e)
                {
                    String define=define_text.getText();
                    String mean=mean_text.getText();
                    if(define!=null&&mean!=null)
                    {
                        if(data.isExisted(define)==false)
                        {
                            data.addNewSlang(define,mean,'a');
                            //update keyList
                            ArrayList<String> new_keys=data.getKeyList();
                            key_data=(String[])new_keys.toArray(new String[new_keys.size()]);

                        }
                        else
                        {
                            int choice=JOptionPane.showConfirmDialog(null, "This word is existed! Do you want to overwrite?",
                            "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
                            if(choice==JOptionPane.YES_OPTION)
                            {
                                data.addNewSlang(mean,define,'w');
                            }
                            
                        }
                    }
                    
                }
            });

            frame.add(process,BorderLayout.CENTER);
            frame.add(confirm,BorderLayout.EAST);

            frame.setVisible(true);

        }

        public void  setupEdit()
        {
            //remove all old component
        mainFrame.getContentPane().removeAll();
        
        display_list.setSelectedIndex(0);
        
        
        right.setPreferredSize(new Dimension(200,600));

        JPanel buttonPane=new JPanel(new GridLayout(4,1,5,5));
        
        JButton add_button=new JButton("Add");
        add_button.setPreferredSize(new Dimension(150,50));
        JButton edit_button=new JButton("Edit");
        edit_button.setPreferredSize(new Dimension(150,50));
        JButton delete_button=new JButton("Delete");
        delete_button.setPreferredSize(new Dimension(150,50));
        JButton reset_button=new JButton("Reset");
        reset_button.setPreferredSize(new Dimension(150,50));

        buttonPane.add(add_button);
        buttonPane.add(edit_button);
        buttonPane.add(delete_button);
        buttonPane.add(reset_button);

        
        JButton back=new JButton("Back to Menu");
        back.setPreferredSize(new Dimension(150,50));
        back.addActionListener(new ButtonSubMenuEvent());
        back.setActionCommand("Back");

        

        right.add(buttonPane,BorderLayout.PAGE_START);
        right.add(back,BorderLayout.PAGE_END);

        JPanel input=new JPanel(new FlowLayout());
        input.setPreferredSize(new Dimension(600,100));
        JLabel label=new JLabel("Search: ");
        JTextField textField=new JTextField();
        textField.setPreferredSize(new Dimension(100,30));
        JButton search=new JButton("Search");
        input.add(label);
        input.add(textField);
        input.add(search);
        
        textField.addKeyListener(new KeyAdapter ()
        {
            public void keyReleased(KeyEvent ke)
            {
                String cur=textField.getText();
                showFilter(cur);
            }
        });

        JScrollPane scrollPane=new JScrollPane(display_list);
        display_list.setPreferredSize(new Dimension(400,600));

        scrollPane.setVerticalScrollBar(new JScrollBar());
        JPanel display=new JPanel();
        display.add(scrollPane);
        
        center.setLayout(new BorderLayout());
        center.add(input,BorderLayout.NORTH);
        center.add(display,BorderLayout.CENTER);


        //configure button
        add_button.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                String cur=(String)display_list.getSelectedValue();
                showAddWindows(cur);
            }
        });
        edit_button.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                String cur=(String)display_list.getSelectedValue();
                showEditWindows(cur);
            }
        });
        delete_button.addMouseListener(new MouseAdapter ()
        {
            
            public void mouseClicked(MouseEvent e)
            {
                String temp=(String)display_list.getSelectedValue();
                
                String message="Delete "+"\""+temp+"\""+" from list?";
                int choice =JOptionPane.showConfirmDialog(null,message , "Delete Message",JOptionPane.YES_NO_CANCEL_OPTION);
                if(choice<1)
                {
                    data.getData().remove(temp);
                    ArrayList<String> temp_list=data.getKeyList();
                    key_data=temp_list.toArray(new String[temp_list.size()]);
                    display_list.setListData(key_data);
                }
            }
        });

        reset_button.addMouseListener(new MouseAdapter ()
        {
            public void mouseClicked(MouseEvent e)
            {
                try
                {
                FileHandle fh=new FileHandle();
                String data_path="origin_slang.txt";
                
                data.setData(fh.getData(data_path));
                ArrayList<String> temp_list=data.getKeyList();
                key_data=temp_list.toArray(new String[temp_list.size()]);
                display_list.setListData(key_data);
                }
                catch (Exception ex)
                {
                    //donothing
                   System.out.println("Error load file!");
                }
                
            }
        });
        buttonPane.setBackground(new Color(166, 228, 218)); 
        center.setBackground(new Color(255, 188, 91));
        right.setBackground(new Color (166, 228, 218));
        input.setBackground(Color.BLUE);
        display.setBackground(new Color(255, 188, 91));
        mainFrame.add(center,BorderLayout.CENTER);
        mainFrame.add(right,BorderLayout.EAST);
        mainFrame.setVisible(true);

        }
    }

    
    public void createMainInterface(String name)
    {
        if (_init ==false)
        {
            mainFrame=new JFrame(name);
            _init=true;
        }

        //set up frame
        JFrame.setDefaultLookAndFeelDecorated(true);
        mainFrame.setSize(_WINDOWN_WIDTH,_WINDOWN_HEIGHT);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setBackground(Color.white);
    
        //set up main display
        setUpMainDisplay();


        mainFrame.setVisible(true);        
    }
    
    public void setUpMainDisplay()
    {
        //remove all old component
        mainFrame.getContentPane().removeAll();
        
        //create menubar to add to frame
        JMenuBar menu=setUpMenu();
        mainFrame.add(menu,BorderLayout.PAGE_START);
              
        //create new panel for display
        JPanel pane=new JPanel();
        pane.setPreferredSize(new Dimension(300,200));
        pane.setBackground(Color.WHITE);
        
        //set up label
        JLabel label=new JLabel();
        label.setIcon(new ImageIcon("Pics/russian-slang-cover-.jpg"));
        label.setHorizontalAlignment(JLabel.CENTER);
        //add label to panel
  
        pane.add(label,BorderLayout.PAGE_START);

         //create button
         JButton dictButton=new JButton("Search");
         JButton gameButton=new JButton("Game");
         JButton exitButton=new JButton("Quit");
         JButton editButton=new JButton("Edit");
         
         //set up button
         dictButton.setPreferredSize(new Dimension(_BUTTON_WIDTH,_BUTTON_HEIGHT));
         dictButton.setAlignmentX(Component.CENTER_ALIGNMENT);
         dictButton.setActionCommand("Search");
 
         editButton.setPreferredSize(new Dimension(_BUTTON_WIDTH,_BUTTON_HEIGHT));
         editButton.setAlignmentX(Component.CENTER_ALIGNMENT);
         editButton.setActionCommand("Edit");
 
         gameButton.setPreferredSize(new Dimension(_BUTTON_WIDTH,_BUTTON_HEIGHT));
         gameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
         gameButton.setActionCommand("Game");
 
         exitButton.setPreferredSize(new Dimension(_BUTTON_WIDTH,_BUTTON_HEIGHT));
         exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
         exitButton.setActionCommand("Quit");
         ButtonMainEvent be=new ButtonMainEvent();
         dictButton.addActionListener(be);
         gameButton.addActionListener(be);
         editButton.addActionListener(be);
         exitButton.addActionListener(be);
 
        //create a layout for button panel
        JPanel buttonPane=new JPanel();
        buttonPane.setLayout(new GridLayout(4,1,5,5));
        buttonPane.setPreferredSize(new Dimension(200,300));
        
        buttonPane.add(dictButton);
        buttonPane.add(editButton);
        buttonPane.add(gameButton);
        buttonPane.add(exitButton);
        
        JPanel center =new JPanel();
        center.setBackground(Color.WHITE);
        center.add(buttonPane);


        //add panel
    
        
 
        
        mainFrame.add(pane,BorderLayout.PAGE_START);    
        mainFrame.add(center,BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }
    
    public class Quiz implements ActionListener{
        final String[] _quests_string=
        {
            ">:O","BFF","CFC","FIFA","CTF",
            "LOL","LTNS","MSDN","WTK","YUP"
        };
        final String[] _answers_string=
        {
            "Angry","Best Friends Forever","Chlorofluorocarbon",
            "Federation International de Football Association",
            "Capture The Flag","Laughing Out Loud","Long Time No See",
            "Microsoft Developer Network","Want To Know","Yes"
        };
    
        public int[] generateFormat()
        {
            Random rd=new Random();
            int [] format=new int[5];
            for(int i=0;i<5;i++)
            {
                format[i]=rd.nextInt(10);
            }
            return format;
            
        }
        public String[] generateQuestions(String[] quests,int[]format)
        {
            String[] ret=new String[5];
            for (int i=0;i<5;i++)
            {
                ret[i]=quests[format[i]];
            }
            
            return ret;
        }
    
        public String[][] generateOptions(int[] format,String[]answers)
        {
            Random rd=new Random();
            String[][] ret=new String[5][4];
            for(int i=0;i<5;i++)
            {
                for(int j=0;j<4;j++)
                    ret[i][j]=answers[rd.nextInt(5)];
                //assggin answer
                ret[i][rd.nextInt(4)]=answers[format[i]];
               
                
            }
            return ret;
        }
    
        public char[] generateAnswer(String[]ans,String[][]ops,int[]format)
        {
            char[] ret=new char[5];
            for (int i=0;i<5;i++)
            {
                int temp=0;
                for(int j=0;j<4;j++)
                {
                    if(ans[format[i]].equals(ops[i][j]))
                        temp=j;
    
                }
              
                switch (temp)
                {
                case 0:
                ret[i]='A';
                break;
                case 1:
                ret[i]='B';
                break;
                case 2:
                ret[i]='C';
                break;
                case 3:
                ret[i]='D';
                break;
                
                }
            }
            return ret;
        }
        
        String[] questions;
        String[][] options ;
        char[] answers;
        char answer;
        int index;
        int correct_guesses =0;
        int total_questions = 0;
        int result;
        int seconds=10;
        
        JFrame frame = new JFrame();
        JTextField textfield = new JTextField();
        JTextArea textarea = new JTextArea();
        JButton buttonA = new JButton();
        JButton buttonB = new JButton();
        JButton buttonC = new JButton();
        JButton buttonD = new JButton();
        JButton back  = new JButton();
        JLabel answer_labelA = new JLabel();
        JLabel answer_labelB = new JLabel();
        JLabel answer_labelC = new JLabel();
        JLabel answer_labelD = new JLabel();
        JLabel time_label = new JLabel();
        JLabel seconds_left = new JLabel();
        JTextField number_right = new JTextField();
        JTextField percentage = new JTextField();
        
        Timer timer = new Timer(1000, new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds--;
                seconds_left.setText(String.valueOf(seconds));
                if(seconds<=0) {
                    displayAnswer();
                }
                }
            });
        
        public void generateEveryThing(char mode)
        {
            int [] format=generateFormat();
            if(mode=='d')
            {
            questions = generateQuestions(_quests_string, format);
            options = generateOptions(format, _answers_string);
            answers = generateAnswer(_answers_string, options,format);
            }
            if (mode=='m')
            {
                questions = generateQuestions(_answers_string, format);
            options = generateOptions(format, _quests_string);
            answers = generateAnswer(_quests_string, options,format);
            }
    
            total_questions=5;
            frame.setTitle("Slang MiniGame");
        }
        public Quiz(char mode) {
            generateEveryThing(mode);
            
            frame.setSize(650,650);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().setBackground(new Color(50,50,50));
            frame.setLayout(null);
            frame.setResizable(false);
            
            textfield.setBounds(0,0,650,50);
            textfield.setBackground(new Color(25,25,25));
            textfield.setForeground(new Color(25,255,0));
    
            textfield.setBorder(BorderFactory.createBevelBorder(1));
            textfield.setHorizontalAlignment(JTextField.CENTER);
            textfield.setEditable(false);
            
            textarea.setBounds(0,50,650,50);
            textarea.setLineWrap(true);
            textarea.setWrapStyleWord(true);
            textarea.setBackground(new Color(25,25,25));
            textarea.setForeground(new Color(25,255,0));
    
            textarea.setBorder(BorderFactory.createBevelBorder(1));
            textarea.setEditable(false);
            
            buttonA.setBounds(0,100,100,100);
    
            buttonA.setFocusable(false);
            buttonA.addActionListener(this);
            buttonA.setText("A");
            
            buttonB.setBounds(0,200,100,100);
    
            buttonB.setFocusable(false);
            buttonB.addActionListener(this);
            buttonB.setText("B");
            
            buttonC.setBounds(0,300,100,100);
    
            buttonC.setFocusable(false);
            buttonC.addActionListener(this);
            buttonC.setText("C");
            
            buttonD.setBounds(0,400,100,100);
    
            buttonD.setFocusable(false);
            buttonD.addActionListener(this);
            buttonD.setText("D");
            
            

            answer_labelA.setBounds(125,100,500,100);
            answer_labelA.setBackground(new Color(50,50,50));
            answer_labelA.setForeground(new Color(25,255,0));
    
            
            answer_labelB.setBounds(125,200,500,100);
            answer_labelB.setBackground(new Color(50,50,50));
            answer_labelB.setForeground(new Color(25,255,0));
    
            
            answer_labelC.setBounds(125,300,500,100);
            answer_labelC.setBackground(new Color(50,50,50));
            answer_labelC.setForeground(new Color(25,255,0));
    
    
            answer_labelD.setBounds(125,400,500,100);
            answer_labelD.setBackground(new Color(50,50,50));
            answer_labelD.setForeground(new Color(25,255,0));
    
            
            seconds_left.setBounds(535,510,100,100);
            seconds_left.setBackground(new Color(25,25,25));
            seconds_left.setForeground(new Color(255,0,0));
            seconds_left.setFont(new Font("Arial",Font.BOLD,60));
            seconds_left.setBorder(BorderFactory.createBevelBorder(1));
            seconds_left.setOpaque(true);
            seconds_left.setHorizontalAlignment(JTextField.CENTER);
            seconds_left.setText(String.valueOf(seconds));
            
            time_label.setBounds(535,475,100,25);
            time_label.setBackground(new Color(50,50,50));
            time_label.setForeground(new Color(255,0,0));
            time_label.setFont(new Font("Arial",Font.PLAIN,16));
            time_label.setHorizontalAlignment(JTextField.CENTER);
            time_label.setText("Timer");
            
            number_right.setBounds(225,225,200,100);
            number_right.setBackground(new Color(25,25,25));
            number_right.setForeground(new Color(25,255,0));
            number_right.setFont(new Font("Ink Free",Font.BOLD,50));
            number_right.setBorder(BorderFactory.createBevelBorder(1));
            number_right.setHorizontalAlignment(JTextField.CENTER);
            number_right.setEditable(false);
            
            percentage.setBounds(225,325,200,100);
            percentage.setBackground(new Color(25,25,25));
            percentage.setForeground(new Color(25,255,0));
            percentage.setFont(new Font("Ink Free",Font.BOLD,50));
            percentage.setBorder(BorderFactory.createBevelBorder(1));
            percentage.setHorizontalAlignment(JTextField.CENTER);
            percentage.setEditable(false);
            
            frame.add(time_label);
            frame.add(seconds_left);
            frame.add(answer_labelA);
            frame.add(answer_labelB);
            frame.add(answer_labelC);
            frame.add(answer_labelD);
            frame.add(buttonA);
            frame.add(buttonB);
            frame.add(buttonC);
            frame.add(buttonD);
   
            frame.add(textarea);
            frame.add(textfield);
            frame.setVisible(true);
            
            nextQuestion();
        }
        public void nextQuestion() {
            
            if(index>=total_questions) {
                results();
            }
            else {
                textfield.setText("Question "+(index+1));
                textarea.setText(questions[index]);
                answer_labelA.setText(options[index][0]);
                answer_labelB.setText(options[index][1]);
                answer_labelC.setText(options[index][2]);
                answer_labelD.setText(options[index][3]);
                timer.start();
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            
                buttonA.setEnabled(false);
                buttonB.setEnabled(false);
                buttonC.setEnabled(false);
                buttonD.setEnabled(false);

                
                if(e.getSource()==buttonA) {
                    answer= 'A';
                    if(answer == answers[index]) {
                        correct_guesses++;
                    }
                }
                if(e.getSource()==buttonB) {
                    answer= 'B';
                    if(answer == answers[index]) {
                        correct_guesses++;
                    }
                }
                if(e.getSource()==buttonC) {
                    answer= 'C';
                    if(answer == answers[index]) {
                        correct_guesses++;
                    }
                }
                if(e.getSource()==buttonD) {
                    answer= 'D';
                    if(answer == answers[index]) {
                        correct_guesses++;
                    }
                }
                displayAnswer();
        }
        public void displayAnswer() {
            
            timer.stop();
            
            buttonA.setEnabled(false);
            buttonB.setEnabled(false);
            buttonC.setEnabled(false);
            buttonD.setEnabled(false);
            
            if(answers[index] != 'A')
                answer_labelA.setForeground(new Color(255,0,0));
            if(answers[index] != 'B')
                answer_labelB.setForeground(new Color(255,0,0));
            if(answers[index] != 'C')
                answer_labelC.setForeground(new Color(255,0,0));
            if(answers[index] != 'D')
                answer_labelD.setForeground(new Color(255,0,0));
            
            Timer pause = new Timer(2000, new ActionListener() {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    answer_labelA.setForeground(new Color(25,255,0));
                    answer_labelB.setForeground(new Color(25,255,0));
                    answer_labelC.setForeground(new Color(25,255,0));
                    answer_labelD.setForeground(new Color(25,255,0));
                    
                    answer = ' ';
                    seconds=10;
                    seconds_left.setText(String.valueOf(seconds));
                    buttonA.setEnabled(true);
                    buttonB.setEnabled(true);
                    buttonC.setEnabled(true);
                    buttonD.setEnabled(true);
                    index++;
                    nextQuestion();
                }
            });
            pause.setRepeats(false);
            pause.start();
        }
        public void results(){
            
            buttonA.setEnabled(false);
            buttonB.setEnabled(false);
            buttonC.setEnabled(false);
            buttonD.setEnabled(false);
            
            result = (int)((correct_guesses/(double)total_questions)*100);
            
            textfield.setText("RESULTS!");
            textarea.setText("");
            answer_labelA.setText("");
            answer_labelB.setText("");
            answer_labelC.setText("");
            answer_labelD.setText("");
            
            number_right.setText("("+correct_guesses+"/"+total_questions+")");
            percentage.setText(result+"%");
            
            frame.add(number_right);
            frame.add(percentage);
            
        }

    }
    class gameListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String cmd=e.getActionCommand();
            if(cmd.equals("define"))
            {
                Quiz quiz=new Quiz('d');
            }
            else if(cmd.equals("mean"))
            {
                Quiz quiz=new Quiz('m');
            }

        }
    }

    public void setupGame()
    {
        mainFrame.getContentPane().removeAll();
        JPanel top=new JPanel();
        top.setPreferredSize(new Dimension(700,200));
        JLabel label =new JLabel("SLANG MINI GAME");
        label.setFont(new Font("Verdana", Font.BOLD, 28));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label.setAlignmentY(JLabel.CENTER_ALIGNMENT);
        top.add(label);


        JPanel center =new JPanel();
        JPanel buttonPane=new JPanel(new GridLayout(3,1,5,5));
        buttonPane.setPreferredSize(new Dimension(300,200));
        buttonPane.setAlignmentX(JPanel.BOTTOM_ALIGNMENT);

        JButton quiz_define=new JButton("Define Mode");
        quiz_define.addActionListener(new gameListener());
        quiz_define.setActionCommand("define");
        
        JButton quiz_means=new JButton("Meaning Mode");
        quiz_means.addActionListener(new gameListener());
        quiz_means.setActionCommand("mean");

        JButton back=new JButton("Back to menu");
        back.addActionListener(new ButtonSubMenuEvent());
        back.setActionCommand("Back");
       
        buttonPane.add(quiz_define);
        buttonPane.add(quiz_means); 
        buttonPane.add(back);


        center.add(buttonPane);


        top.setBackground(new Color(255, 220, 101));
        center.setBackground(new Color(255, 220, 101));
        buttonPane.setBackground(new Color(255, 220, 101));
        mainFrame.add(top,BorderLayout.NORTH);
        mainFrame.add(center,BorderLayout.CENTER);
       
        mainFrame.setVisible(true);

    }

    private class ButtonMainEvent implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String cmd=e.getActionCommand();
            if(cmd.equals("Search"))
            {
                Diction d=new Diction();
                d.setUpDict();
            }
            else if (cmd.equals("Edit"))
            {
                EditDict ed=new EditDict();
                ed.setupEdit();
                
            }
            else if(cmd.equals("Game"))
            {
                setupGame();
            }
            else if(cmd.equals("Quit"))
            {
                //remove all and close
                mainFrame.removeAll();
                mainFrame.dispose();
                System.out.println("End program!");
                System.exit(0);

            }

        }
    }
    class Diction
    {
    final String _FIND_KEY="Find by Key";
    final String _FIND_MEAN="Find by meaning";
    private JPanel top=new JPanel();
    private JPanel left=new JPanel();
    private JPanel center =new JPanel(new FlowLayout());
    JList<String> display_list=new JList<>(key_data);
    
    JPanel right =new JPanel();

    public void displayWord(String temp)
    {
        ArrayList<String> res=(ArrayList<String>)data.getData().get(temp);
        String ret="";
        for(String cur:res)
        {
            ret+=cur+"\n";
        }

        center.removeAll();
        JLabel define=new JLabel();
        define.setText("Slang word: "+temp);
        define.setFont(new Font("Arial",Font.BOLD,24));
        define.setPreferredSize(new Dimension(300,150));

        JTextArea mean=new JTextArea();
        mean.setText("Mean: "+ret);
        mean.setFont(new Font("Arial",Font.PLAIN,20));
        mean.setPreferredSize(new Dimension(300,150));
        mean.setEditable(false);
        center.add(define);
        center.add(mean);

        mainFrame.setVisible(true);

    }

    public void showFilter(String cur)
    {
        ArrayList<String> ret=new ArrayList<>();
        for(String temp:key_data)
        {
            if(temp.indexOf(cur,0)>=0)
            ret.add(temp);
        }
        String[] new_display=ret.toArray(new String[ret.size()]);
        display_list.setListData(new_display);
        display_list.setSelectedIndex(0);
        mainFrame.setVisible(true);
    }

    

    public void setUpDict()
    {
        //remove all old component
        mainFrame.getContentPane().removeAll();
        
        //configure top panel
        top.setPreferredSize(new Dimension(700,50));
        top.setLayout(new BorderLayout());
        top.setBackground(new Color(250,199,93));
        
        display_list.addKeyListener(new KeyAdapter ()
        {
            public void keyReleased(KeyEvent ke)
            {
                if(ke.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    String temp=(String)display_list.getSelectedValue();
                    displayWord(temp);
                }
            }
        });
       
        //add itemhandle
        
        JPanel input=new JPanel(new FlowLayout());
        JLabel label=new JLabel("Search: ");
        JTextField textField=new JTextField();
        textField.setPreferredSize(new Dimension(100,30));
        
    
        JButton search=new JButton("Search");

        
        top.add(input,BorderLayout.LINE_START);
        

        //configure left panel
     
        left.setPreferredSize(new Dimension(200,200));
        left.setBackground(Color.BLUE);
        
        //configure list
        
        JScrollPane scrollPane=new JScrollPane(display_list);
        scrollPane.setPreferredSize(new Dimension( 150,300));
        scrollPane.setVerticalScrollBar(new JScrollBar());
        left.add(scrollPane);
      
        right.setPreferredSize(new Dimension(200,200));
        
        right.setLayout(new BorderLayout());
        JButton backButton=new JButton("Back to Menu");
        backButton.setActionCommand("Back");
        backButton.addActionListener(new ButtonSubMenuEvent());
        
        JPanel history=new JPanel(new FlowLayout());
        JLabel history_label=new JLabel("History:");
        history_label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        
        ArrayList<String> history_data=new ArrayList<>();
        JList<String> history_list=new JList<>();

        history.add(history_label);
        JScrollPane his_scrollPane=new JScrollPane(history_list);
        his_scrollPane.setPreferredSize(new Dimension( 150,300));
        his_scrollPane.setVerticalScrollBar(new JScrollBar());
        history.add(his_scrollPane);

        right.add(history,BorderLayout.CENTER);
        right.add(backButton,BorderLayout.SOUTH);
        

        textField.addKeyListener(new KeyAdapter ()
        {
            public void keyReleased(KeyEvent ke)
            {
                String cur=textField.getText();
                showFilter(cur);
            }
        });
        textField.addKeyListener(new KeyAdapter ()
        {
            public void keyReleased(KeyEvent ke)
            {
                if(ke.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    String temp=(String)display_list.getSelectedValue();
                    
                    displayWord(temp);
                }
            }
        });
        search.addMouseListener(new MouseAdapter ()
        {
            
            public void mouseClicked(MouseEvent e)
            {
                String temp=(String)display_list.getSelectedValue();
                history_data.add(temp);
                String[] his_data=history_data.toArray(new String[history_data.size()]);
                history_list.setListData(his_data);
                displayWord(temp);
            }
        });

        display_list.addKeyListener(new KeyAdapter()
        {
            public void keyReleased(KeyEvent ke)
            {
                if(ke.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    String temp=(String)display_list.getSelectedValue();
                    history_data.add(temp);
                    String[] his_data=history_data.toArray(new String[history_data.size()]);
                    history_list.setListData(his_data);
                    
                }
            }
        });

        input.add(label);
        input.add(textField);
        input.add(search);
        center.setBackground(new Color(253, 254, 224));
        input.setBackground(new Color(250,199,93));

        right.setBackground(new Color(166, 228, 218));

        mainFrame.add(top,BorderLayout.NORTH);
        mainFrame.add(left,BorderLayout.WEST);
        mainFrame.add(center,BorderLayout.CENTER);
        mainFrame.add(right,BorderLayout.EAST);
        mainFrame.setVisible(true);
    }
    
   
    
    

    

    

    




  
   

}
    
}


