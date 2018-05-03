package canny;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class JpgImage {
	public BufferedImage imgGray;//灰度图像
	public Image img;//彩色图像
	public int[][] imgData;//图像矩阵
	public double[][] gaus;//高斯卷积核
	public int size;//卷积核大小
	public int HighThreshold;
	public int LowThreshold;
	public double DistanceThreshlod;
	public double AngleThreshold;
	public double[][] thegma;
	public double[][] G;
	public double[][] dataGradient;//梯度
	public double[][] magnitudes;//角度
	public void setThreshold(int highThreshold,int lowThreshold,double DistanceThreshlod,double AngleThreshold) {
		this.HighThreshold = highThreshold;
		this.LowThreshold = lowThreshold;
		this.DistanceThreshlod = DistanceThreshlod;
		this.AngleThreshold = AngleThreshold;
	}
	public void doSobel() {
		thegma = new double[imgGray.getWidth()][imgGray.getHeight()];
		G = new double[imgGray.getWidth()][imgGray.getHeight()];
		int[][] imgDataTmp = new int[imgGray.getWidth()][imgGray.getHeight()];
		for(int i=1;i<imgGray.getWidth()-1;i++) {
			for(int j=1;j<imgGray.getHeight()-1;j++) {
				int Sx = (imgData[i+1][j]-imgData[i-1][j])*2+(imgData[i+1][j-1]-imgData[i-1][j-1])+(imgData[i+1][j+1]-imgData[i-1][j+1]);
				int Sy = (imgData[i][j-1]-imgData[i][j+1])*2+(imgData[i-1][j-1]-imgData[i-1][j+1])+(imgData[i+1][j-1]-imgData[i+1][j+1]);
				G[i][j] = Math.sqrt(Sx*Sx+Sy*Sy);
				double c;
				if(Sy==0)
					c=255;
				else
					c = Sx/Sy;
				double Gp1 = (1-c)*imgData[i+1][j]+c*imgData[i+1][j-1];
				double Gp2 = (1-c)*imgData[i-1][j]+c*imgData[i-1][j+1];
				thegma[i][j] = c;
				if(imgData[i][j]>Gp1&&imgData[i][j]>Gp2)
					imgDataTmp[i][j] = imgData[i][j];
				else
					imgDataTmp[i][j] = 10;
			}
		}
		imgData = imgDataTmp;
	}
	public void thresholdImage() {
		int[][] imgDataTmp = new int[imgGray.getWidth()][imgGray.getHeight()];
		for(int i=1;i<imgGray.getWidth()-1;i++) {
			for(int j=1;j<imgGray.getHeight()-1;j++) {
				if(imgData[i][j]>=HighThreshold)
					imgDataTmp[i][j] = imgData[i][j];
				else if(imgData[i][j]>=LowThreshold) {
					for(int k=-5;k<6;k++)
						for(int l=-5;l<6;l++)
							if(imgData[i-k][j-l]!=0)
								imgDataTmp[i][j] = imgData[i-k][j-l];
				}
				else
					imgDataTmp[i][j] = 0;
			}
		}
		
	}
	public void showDoubleValue(){
		for(int i=0;i<imgGray.getWidth();i++){
			for(int j=0;j<imgGray.getHeight();j++)
				if(imgData[i][j]>this.LowThreshold)
					imgData[i][j]=255;
				else
					imgData[i][j]=0;
		}
	}
	public void getImage(String path){//获取指定URL的图片并转化为灰度图
		img = new ImageIcon(path).getImage();
		imgGray = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_BGR);
		imgGray.getGraphics().drawImage(img, 0, 0, null);
		imgData = new int[imgGray.getWidth()][imgGray.getHeight()];
		for(int i=0;i<imgGray.getWidth();i++) {
			for(int j=0;j<imgGray.getHeight();j++) {
				Color tmp = new Color(imgGray.getRGB(i, j));
				int colorTmp = (int)(tmp.getRed()*0.299+tmp.getGreen()*0.587+tmp.getBlue()*0.114);
				imgGray.setRGB(i, j, new Color(colorTmp,colorTmp,colorTmp).getRGB());
				imgData[i][j] = colorTmp;
			}
		}
		System.out.println("已打开"+path+"并转化为灰度图像");
	}
	public void changeToBufferedImage() {
		for(int i=0;i<imgGray.getWidth();i++) {
			for(int j=0;j<imgGray.getHeight();j++) {
				if(imgData[i][j]>255)
					imgData[i][j]=255;
				else if(imgData[i][j]<0)
					imgData[i][j]=0;
				imgGray.setRGB(i,j,new Color(imgData[i][j],imgData[i][j],imgData[i][j]).getRGB());
			}
		}
	}
	public void doMediaFilter() {//5*5中值滤波
		int[][] imgDataTmp = new int[imgGray.getWidth()][imgGray.getHeight()];
		for(int i=1;i<imgGray.getWidth()-1;i++) {
			for(int j=1;j<imgGray.getHeight()-1;j++) {
				int[] arr = new int[9];
				arr[0] = imgData[i][j];
				arr[1] = imgData[i-1][j];
				arr[2] = imgData[i+1][j];
				arr[3] = imgData[i][j-1];
				arr[4] = imgData[i][j+1];
				arr[5] = imgData[i-1][j-1];
				arr[6] = imgData[i+1][j+1];
				arr[7] = imgData[i-1][j+1];
				arr[8] = imgData[i+1][j-1];
				for(int m=0;m<arr.length;m++)
					for(int n=m+1;n<arr.length;n++) {
						if(arr[m]>arr[n]) {
							int tmp = arr[m];
							arr[n] = arr[m];
							arr[m] = tmp;
						}
					}
				imgDataTmp[i][j] = arr[4];
			}
		}
		imgData = imgDataTmp;
	}
	public void doGaussianFilter() {//使用高斯卷积核进行高斯平滑滤波
		int imgDataTmp[][] = new int[imgGray.getWidth()][imgGray.getHeight()];
		for(int i=2;i<imgGray.getWidth()-2;i++) {
			for(int j=2;j<imgGray.getHeight()-2;j++) {
				imgDataTmp[i][j] = 0;
				for(int m=0;m<this.size;m++) {
					for(int n=0;n<this.size;n++) {
						imgDataTmp[i][j] += imgData[i+m-size/2][j+n-size/2] * gaus[m][n];
					}
				}
				if(imgData[i][j]<0) {
					imgData[i][j]=0;
				}
				else if(imgData[i][j]>255) {
					imgData[i][j]=255;
				}
			}
		}
		imgData = imgDataTmp;
	}
	public void setGaussianKernel(int size,double sigma)  
	{
		this.size=size;
		gaus = new double[size][size];
	    int center=size/2;
	    double sum=0;
	    for(int i=0;i<size;i++)
	    {
	        for(int j=0;j<size;j++)
	        {
	            gaus[i][j]=(1/(2*Math.PI*sigma*sigma))*Math.exp(-((i-center)*(i-center)+(j-center)*(j-center))/(2*sigma*sigma)); 
	            sum+=gaus[i][j];
	        }
	    }
	    for(int i=0;i<size;i++)
	    {
	        for(int j=0;j<size;j++)
	        {
	            gaus[i][j]/=sum;
	        }
	    }
	}
	public void ConnectEdge() {
		int[][] imgDataTmp = new int[imgGray.getWidth()][imgGray.getHeight()];
		for(int i=0;i<imgGray.getWidth();i++) {
			for(int j=0;j<imgGray.getHeight();j++) {
				imgDataTmp[i][j]=imgData[i][j];
			}
		}
		for (int i = 0; i < imgGray.getWidth(); i++) {
			for (int j = 0; j < imgGray.getHeight(); j++) {
				if (this.imgData[i][j] == 255) {
					for (int k = -(int) this.DistanceThreshlod; k <= this.DistanceThreshlod && k != 0; k++) {
						for (int l = -(int) this.DistanceThreshlod; l <= this.DistanceThreshlod && l != 0; l++) {
							if (((i + k) < 0) || ((i + k) > (imgGray.getWidth() - 1)) || ((j + l) < 0)
									|| ((j + l) > (imgGray.getHeight() - 1)))
								continue;
							if (this.imgData[i + k][j + l] == 255) {
								if (Math.abs(this.G[i][j] - this.G[i + k][j + l]) <= this.DistanceThreshlod && Math
										.abs(this.thegma[i][j] - this.thegma[i + k][j + l]) <= this.AngleThreshold) {
									double angle = l / k;
									int lSig = l / Math.abs(l);
									int kSig = k / Math.abs(k);
									if (l > k) {
										for (int m = 0; m < l; m++)
											imgDataTmp[(int) (i + lSig * m / angle)][j + lSig * m] = 255;
									} else {
										for (int m = 0; m < l; m++)
											imgDataTmp[i + kSig * m][(int) (j + kSig * m * angle)] = 255;
									}
								}
							}
						}
					}
				}
			}
		}
		imgData = imgDataTmp;
	}
	public void getGradient() {
		// 计算梯度-gradient, X放与Y方向  
        dataGradient = new double[imgGray.getWidth()][imgGray.getHeight()];  
        magnitudes = new double[imgGray.getWidth()][imgGray.getHeight()];  
        for (int row = 0; row < imgGray.getHeight(); row++) {  
            for (int col = 0; col < imgGray.getWidth(); col++) {  
                // 计算X方向梯度  
                double xg = (getPixel(imgData, imgGray.getWidth(), imgGray.getHeight(), col, row+1) -   
                        getPixel(imgData, imgGray.getWidth(), imgGray.getHeight(), col, row) +   
                        getPixel(imgData, imgGray.getWidth(), imgGray.getHeight(), col+1, row+1) -  
                        getPixel(imgData, imgGray.getWidth(), imgGray.getHeight(), col+1, row))/2.0f;  
                double yg = (getPixel(imgData, imgGray.getWidth(), imgGray.getHeight(), col, row)-  
                        getPixel(imgData, imgGray.getWidth(), imgGray.getHeight(), col+1, row) +  
                        getPixel(imgData, imgGray.getWidth(), imgGray.getHeight(), col, row+1) -  
                        getPixel(imgData, imgGray.getWidth(), imgGray.getHeight(), col+1, row+1))/2.0f;  
                // 计算振幅与角度  
                dataGradient[col][row] = Math.hypot(xg, yg);  
                if(xg == 0)  
                {  
                    if(yg > 0)  
                    {  
                        magnitudes[col][row]=90;                         
                    }  
                    if(yg < 0)  
                    {  
                        magnitudes[col][row]=-90;  
                    }  
                }  
                else if(yg == 0)  
                {  
                    magnitudes[col][row]=0;  
                }  
                else  
                {  
                    magnitudes[col][row] = (float)((Math.atan(yg/xg) * 180)/Math.PI);                    
                }  
                // make it 0 ~ 180  
                magnitudes[col][row] += 90;  
            }  
        }
	}
	public void SignalSuppression() {
        for (int row = 0; row < imgGray.getHeight(); row++) {
            for (int col = 0; col < imgGray.getWidth(); col++) { 
                double angle = magnitudes[col][row];  
                double m0 = dataGradient[col][row];  
                magnitudes[col][row] = m0;  
                if(angle >=0 && angle < 22.5) // angle 0  
                {  
                	double m1 = getPixel(dataGradient, imgGray.getWidth(), imgGray.getHeight(), col-1, row);  
                	double m2 = getPixel(dataGradient, imgGray.getWidth(), imgGray.getHeight(), col+1, row);  
                    if(m0 < m1 || m0 < m2)  
                    {  
                        magnitudes[col][row] = 0;
                    }  
                }  
                else if(angle >= 22.5 && angle < 67.5) // angle +45  
                {  
                	double m1 = getPixel(dataGradient, imgGray.getWidth(), imgGray.getHeight(), col+1, row-1);  
                	double m2 = getPixel(dataGradient, imgGray.getWidth(), imgGray.getHeight(), col-1, row+1);  
                    if(m0 < m1 || m0 < m2)  
                    {  
                    	magnitudes[col][row] = 0;
                    }  
                }  
                else if(angle >= 67.5 && angle < 112.5) // angle 90  
                {  
                	double m1 = getPixel(dataGradient, imgGray.getWidth(), imgGray.getHeight(), col, row+1);  
                	double m2 = getPixel(dataGradient, imgGray.getWidth(), imgGray.getHeight(), col, row-1);  
                    if(m0 < m1 || m0 < m2)  
                    {  
                    	magnitudes[col][row] = 0;
                    }  
                }  
                else if(angle >=112.5 && angle < 157.5) // angle 135 / -45  
                {  
                	double m1 = getPixel(dataGradient, imgGray.getWidth(), imgGray.getHeight(), col-1, row-1);  
                	double m2 = getPixel(dataGradient, imgGray.getWidth(), imgGray.getHeight(), col+1, row+1);  
                    if(m0 < m1 || m0 < m2)  
                    {  
                    	magnitudes[col][row] = 0;
                    }  
                }  
                else if(angle >=157.5) // angle 0  
                {  
                	double m1 = getPixel(dataGradient, imgGray.getWidth(), imgGray.getHeight(), col, row+1);  
                	double m2 = getPixel(dataGradient, imgGray.getWidth(), imgGray.getHeight(), col, row-1);  
                    if(m0 < m1 || m0 < m2)  
                    {  
                    	magnitudes[col][row] = 0;
                    }  
                }  
            }  
        } 
	}
	public void connectEdge(double highThreshold,double lowThreshold) { 
        for (int row = 0; row < imgGray.getHeight(); row++) {
            for (int col = 0; col < imgGray.getWidth(); col++) {
                if(magnitudes[col][row] >= highThreshold && dataGradient[col][row] == 0)  
                {  
                    edgeLink(col, row, lowThreshold);  
                }
            }
        }
        for (int row = 0; row < imgGray.getHeight(); row++) {
            for (int col = 0; col < imgGray.getWidth(); col++) {
            	int gray = clamp((int)dataGradient[col][row]);  
            	imgData[col][row] = gray > 0 ? -1 : 0xff000000;       
            }
        }
	}
	private void edgeLink(int x1, int y1, double threshold) {  
        int x0 = (x1 == 0) ? x1 : x1 - 1;  
        int x2 = (x1 == imgGray.getWidth() - 1) ? x1 : x1 + 1;  
        int y0 = y1 == 0 ? y1 : y1 - 1;  
        int y2 = y1 == imgGray.getHeight() -1 ? y1 : y1 + 1;
        dataGradient[x1][y1] = magnitudes[x1][y1];
        for (int x = x0; x <= x2; x++) {
            for (int y = y0; y <= y2; y++) { 
                if ((y != y1 || x != x1)  
                    && dataGradient[x][y] == 0   
                    && magnitudes[x][y] >= threshold) {  
                    edgeLink(x, y, threshold);  
                    return;  
                }  
            }  
        }  
    }
	public double getPixel(double[][] input, int width, int height, int col, int row) {  
        if(col < 0 || col >= width)  
            col = 0;  
        if(row < 0 || row >= height)  
            row = 0;  
        return input[col][row];  
    }
	public double getPixel(int[][] input, int width, int height, int col, int row) {  
        if(col < 0 || col >= width)  
            col = 0;  
        if(row < 0 || row >= height)  
            row = 0;  
        return input[col][row];  
    }
	public int clamp(int value) {  
		return value > 255 ? 255 : (value < 0 ? 0 : value);  
	}
}
