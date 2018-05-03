package tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import javax.swing.*;
/**
*加载现示BMP图象,需要JDK1.5或以上
*@author Eatsun
*/
@SuppressWarnings("serial")
public class test extends JFrame{
    public test(String bmpFile){
        super("test");
        Image image =null;
        try{
            image=ImageIO.read(new File(bmpFile));       //读取源图片
        }catch(IOException ex){
        }
       EdgeDetector edgeDetector=new EdgeDetector();   
        edgeDetector.setSourceImage(image);              //设置边缘处理的参数
        edgeDetector.setThreshold(128);
        edgeDetector.setWidGaussianKernel(5);
        try {
            edgeDetector.process();                       //进行边缘处理
        }
        catch(EdgeDetectorException e) {
            System.out.println(e.getMessage());
        }
        Image edgeImage=edgeDetector.getEdgeImage();      //得到边缘图片
      
    
        JLabel label =new JLabel(new ImageIcon(edgeImage)); //显示边缘图片
        add(label);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }
    public static void main(String[] args){
        final String fileName ="D:/TensorFlow/captcha/test/8017.jpg"; //把这个改成你自己的bmp图片的路径
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new test(fileName).setVisible(true);
            }
        });
    }
} 
