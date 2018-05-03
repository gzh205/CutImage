package tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import javax.swing.*;
/**
*������ʾBMPͼ��,��ҪJDK1.5������
*@author Eatsun
*/
@SuppressWarnings("serial")
public class test extends JFrame{
    public test(String bmpFile){
        super("test");
        Image image =null;
        try{
            image=ImageIO.read(new File(bmpFile));       //��ȡԴͼƬ
        }catch(IOException ex){
        }
       EdgeDetector edgeDetector=new EdgeDetector();   
        edgeDetector.setSourceImage(image);              //���ñ�Ե����Ĳ���
        edgeDetector.setThreshold(128);
        edgeDetector.setWidGaussianKernel(5);
        try {
            edgeDetector.process();                       //���б�Ե����
        }
        catch(EdgeDetectorException e) {
            System.out.println(e.getMessage());
        }
        Image edgeImage=edgeDetector.getEdgeImage();      //�õ���ԵͼƬ
      
    
        JLabel label =new JLabel(new ImageIcon(edgeImage)); //��ʾ��ԵͼƬ
        add(label);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }
    public static void main(String[] args){
        final String fileName ="D:/TensorFlow/captcha/test/8017.jpg"; //������ĳ����Լ���bmpͼƬ��·��
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new test(fileName).setVisible(true);
            }
        });
    }
} 
