# CutImage  
验证码图像处理  
验证码图像处理流程：  
1.使用均值滤波过滤验证码图像中的噪点  
2.使用canny算子提取图像边缘  
3.使用像素直方图将验证码的4个数字分隔开，将一个含有4个数字的验证码图片变成4个只包含1个数字图片  
4.使用卷积神经网络(CNN)对每一个数字进行识别**(未完成)**  
