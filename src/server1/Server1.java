/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server1;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Server1 extends JFrame implements Runnable,ActionListener{
    Box boxV1,boxV2,boxV3,boxV4,boxBase;
    Panel panel;
    
    // PoliceText sendListener;
    JScrollPane scroll;
    JButton startButton,sendButton;
    JLabel biaoqian1,biaoqian2,biaoqian3,say;
    JTextField send,portAddress;
    JTextArea receive;
    
    DataInputStream in=null;
    DataOutputStream out=null;
    ServerSocket server = null;
    Socket socket = null;
    Thread threadContect;
    ThreadListen threadListen;
    
    int[] threadCount = new int[1];
    int[] runLeft = new int[1];
    int temp;
    public Server1(){
        super("服务器");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        init();
        FlowLayout flo = new java.awt.FlowLayout();
        setLayout(flo);
       // flo.setAlignment(FlowLayout.LEFT);
    }
    void init(){
        boxV1 = Box.createHorizontalBox();
        biaoqian1=new JLabel("服务器设置:");
        panel=new Panel();     
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(biaoqian1);
        boxV1.add(panel);       
        boxV2 = Box.createHorizontalBox();
        biaoqian2=new JLabel("Port:");
        boxV2.add(biaoqian2);
        boxV2.add(Box.createHorizontalStrut(8));
        portAddress=new JTextField(20);
        portAddress.setText("input the port");
        boxV2. add(portAddress);
        boxV2.add(Box.createHorizontalStrut(8));
        startButton=new JButton("Start");
        boxV2.add(startButton);
        boxV3 = Box.createHorizontalBox();
        receive= new JTextArea(24,63);
        scroll=new JScrollPane(receive);
        boxV3.add(scroll);
        boxV4 = Box.createHorizontalBox();
        say=new JLabel("Say:");
        boxV4.add(say);
        boxV4.add(Box.createHorizontalStrut(8));
        send=new JTextField(20);
        boxV4.add(send);
        boxV4.add(Box.createHorizontalStrut(8));
        sendButton = new JButton("Send");
        boxV4.add(sendButton);
        boxBase = Box.createVerticalBox();
        boxBase.add(boxV1);
        boxBase.add(Box.createVerticalStrut(10));
        boxBase.add(boxV2);
        boxBase.add(Box.createVerticalStrut(10));
        boxBase.add(boxV3);
        boxBase.add(Box.createVerticalStrut(10));
        boxBase.add(boxV4);
        add(boxBase);
        sendButton.setEnabled(false);
        startButton.addActionListener(this);
        //sendButton.addActionListener(this);
        //threadListen = new Thread(this);
        threadContect = new Thread(this);
        threadCount[0]=0;
    }
     
    public static void main(String[] args){
        Server1 mf1=new Server1();
        mf1.setBounds(260,100,800,580);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==startButton){
            receive.append("Server starting...");
            if(!(threadContect.isAlive())) threadContect.start();
            /*if(socket!=null){
                socket = new 
            }*/
        }
       /* if(e.getSource()==sendButton){
            String message = send.getText();
            try{
                out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(message);
                receive.append('\n' + "帅的人(我): " +  message);
                send.setText(null);
            }
            catch(IOException e1){
                receive.append('\n' + "Send Error");
            }
        }*/
    }
    public void listenContect(){
         while(true){
            try{            
                server = new ServerSocket(Integer.parseInt(portAddress.getText()));
                
            }
            catch(IOException e1){
                
            }
            try{
            socket = server.accept();
            receive.append('\n' + "Client connected");
            sendButton.setEnabled(true);
            
            }
            catch(IOException e2){
                
            }
            if(socket!=null){
                threadCount[0] = threadCount[0]+1;
                runLeft[0]=threadCount[0];
                new ThreadListen(socket, receive, out,send,threadCount,runLeft).start();
                sendButton.addActionListener(new ThreadListen(socket, receive, out,send,threadCount,runLeft));
            }
        }
    }
    public void listenInput(){
       
    }
    @Override
    public void run(){
        listenContect();
        
    }
}
class ThreadListen extends Thread implements ActionListener{
    DataInputStream in=null;
    DataOutputStream out=null;
    Socket socket = null;
    JTextArea receive;
    JTextField send;
    int[] threadCount;
    int []runLeft;
    ThreadListen(Socket soc,JTextArea receiveText, DataOutputStream o, JTextField sender,int count[],int[] left){
        socket = soc;
        receive = receiveText;
        out = o;
        send = sender;
        threadCount = count;
        runLeft = left;
        try{
            in = new DataInputStream(soc.getInputStream());
           // out = new DataOutputStream(socket.getOutputStream());
            
        }
        catch(IOException e){
            
        }
    }
    public void run(){
        String message = null;
        while(true){
            try{
                message = in.readUTF();
                receive.append("\n");
                receive.append('\n' + "丑的人: " + message);
            }
            catch(IOException e){
                threadCount[0] = threadCount[0] - 1;
                receive.append('\n' + "Contection Break");
                return;
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e){
         String message = send.getText();
            try{
                out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(message);
                    System.out.println("count"+threadCount[0] +"left"+runLeft[0]);
                if(threadCount[0]==runLeft[0]){
                    receive.append("\n");
                    receive.append('\n' +  "帅的人(我): " +  message);
                }
                runLeft[0]=runLeft[0]-1;                
                if(runLeft[0]==0){                    
                     send.setText(null);  
                     runLeft[0]=threadCount[0];
                }
            }
            catch(IOException e1){
                receive.append('\n' + "Send Error");
            }
    }
}
