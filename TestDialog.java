package test;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import canny.JpgImage;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JEditorPane;

@SuppressWarnings("serial")
public class TestDialog extends JDialog {
	public ImageIcon image;
	public JpgImage img;
	public JEditorPane editorPane;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TestDialog dialog = new TestDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TestDialog() {
		img = new JpgImage();
		setBounds(100, 100, 1669, 876);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				{
					editorPane = new JEditorPane();
					buttonPane.add(editorPane);
					editorPane.setBounds(0, 0, this.getWidth()-500, this.getHeight());
				}
			}
			JButton okButton = new JButton("加载图片");
			buttonPane.add(okButton);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					img.getImage("D:/TensorFlow/captcha/test/8015.jpg");
					drawImage();
				}
			});
			okButton.setActionCommand("加载图片");
			getRootPane().setDefaultButton(okButton);
			{
				JButton btnNewButton = new JButton("高斯平滑滤波");
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						img.setGaussianKernel(5, 5);
						img.doGaussianFilter();
						img.getGradient();
						img.SignalSuppression();
						img.thresholdImage();
						img.connectEdge(20,210);
						img.changeToBufferedImage();
						drawImage();
					}
				});
				buttonPane.add(btnNewButton);
			}
			{
				JButton cancelButton = new JButton("中值滤波");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						img.ConnectEdge();
						img.changeToBufferedImage();
						drawImage();
					}
				});
				cancelButton.setActionCommand("中值滤波");
			}
		}
	}
	public void drawImage() {
		this.getGraphics().clearRect(10, 100, img.imgGray.getWidth(null), img.imgGray.getHeight(null));
		this.getGraphics().drawImage(img.imgGray, 10, 100, img.imgGray.getWidth(null), img.imgGray.getHeight(null), null);
	}
}
