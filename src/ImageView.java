import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.SwingConstants;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


public class ImageView extends JFrame {

	private JPanel contentPane;
	boolean ImageSelected=false;
	boolean ObjectorSegment=true;
	String imgpath=null;
	MainClass mImage;//= new MainClass();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageView frame = new ImageView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ImageView() {
		setResizable(false);
		setOpacity(1.0f);
		
		
		

		
		setTitle("Image Segmenter");
		setForeground(new Color(0, 0, 0));
		setBackground(new Color(0, 0, 0));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 615, 360);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(0);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setForeground(new Color(0, 0, 0));
		splitPane.setBackground(Color.BLACK);
		contentPane.add(splitPane);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setDividerSize(0);

		splitPane_1.setBackground(new Color(0, 0, 0));
		splitPane.setRightComponent(splitPane_1);
		
		final JTextArea txtrImageInformation = new JTextArea();
		txtrImageInformation.setEditable(false);
		txtrImageInformation.setLineWrap(true);
		txtrImageInformation.setForeground(Color.BLACK);
		txtrImageInformation.setText("Image Info:");
		splitPane_1.setLeftComponent(txtrImageInformation);
		
		final JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		splitPane_1.setRightComponent(lblNewLabel);
		
		
		JSplitPane splitPane_2 = new JSplitPane();
		splitPane_2.setDividerSize(0);

		splitPane_2.setForeground(new Color(0, 0, 0));
		splitPane_2.setResizeWeight(0.5);
		splitPane.setLeftComponent(splitPane_2);
		
		JSplitPane splitPane_3 = new JSplitPane();
		splitPane_3.setDividerSize(0);

		splitPane_3.setBackground(new Color(0, 0, 0));
		splitPane_3.setResizeWeight(0.5);
		splitPane_2.setRightComponent(splitPane_3);
		
		Button button_2 = new Button("Select Background");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!ImageSelected){
					lblNewLabel.setText("Please select an Image");
					lblNewLabel.setForeground(Color.RED);
				}
				else
				{
					ObjectorSegment=false;
				}
			}
		});
		button_2.setForeground(new Color(0, 0, 0));
		button_2.setBackground(new Color(65, 105, 225));
		splitPane_3.setLeftComponent(button_2);
		
		Button button_3 = new Button("Segment !");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(ImageSelected)
				{
					//mImage.bgsub();
				mImage.printImage();
					//mImage.bgcalc();
					//mImage=new MainClass(imgpath);
				//	try {
				//		Thread.sleep(10000);
				//	} catch (InterruptedException e) {
						// TODO Auto-generated catch block
				//		e.printStackTrace();
				//	}
			//	lblNewLabel.setIcon( new ImageIcon("test.jpg"));
				//	mImage.bgsub();
				lblNewLabel.setIcon( new ImageIcon("test.jpg"));
				}
			}
		});
		button_3.setBackground(new Color(65, 105, 225));
		splitPane_3.setRightComponent(button_3);
		
		JSplitPane splitPane_4 = new JSplitPane();
		splitPane_4.setBackground(Color.BLACK);
		splitPane_4.setDividerSize(0);

		splitPane_4.setResizeWeight(0.5);
		splitPane_2.setLeftComponent(splitPane_4);
		
		Button button = new Button("Select Object");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!ImageSelected){
					lblNewLabel.setText("Please select an Image");
					lblNewLabel.setForeground(Color.RED);
				}
				else
				{
					ObjectorSegment=true;
				}
			}
			
		});
		button.setForeground(new Color(0, 0, 0));
		button.setBackground(new Color(65, 105, 225));
		splitPane_4.setRightComponent(button);
		
		Button button_1 = new Button("Select Image");
		button_1.setForeground(new Color(0, 0, 0));
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblNewLabel.setText("");
				ImageSelected=true;
				lblNewLabel.setForeground(Color.BLACK);
				 JFileChooser chooser = new JFileChooser();
				    FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "JPG Images", "jpg");
				    chooser.setFileFilter(filter);
				    int returnVal = chooser.showOpenDialog(getParent());
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				     //  System.out.println("You chose to open this file: " +chooser.getSelectedFile().getName());
				       
				       ImageIcon iconLogo = new ImageIcon(chooser.getSelectedFile().getAbsolutePath());
				       
				       imgpath=chooser.getSelectedFile().getAbsolutePath();
				       mImage=new MainClass(chooser.getSelectedFile().getAbsolutePath());
				       
				       
				       txtrImageInformation.append("\n"+chooser.getSelectedFile().getName());
				       lblNewLabel.setIcon(iconLogo);
				       lblNewLabel.addMouseListener(new MouseListener() {
					        public void mouseClicked(MouseEvent e) {
					          //  JOptionPane.showMessageDialog(lblNewLabel, );
					        	//txtrImageInformation.append("\nSelected Object @"+e.getX()+","+e.getY());
					        
				            	//JOptionPane.showMessageDialog(lblNewLabel, mImage.getCol()+" "+mImage.getRow());
					        	int c=e.getX();
					        	int r=e.getY();
					          if(c<=mImage.getCol()&& r <=mImage.getRow())
					            {
					            	int val= ObjectorSegment?1:2;
				    	   			mImage.setPoint(e.getX(), e.getY(), val);
				    	   			if(ObjectorSegment)
				    	   			{
				    	   				txtrImageInformation.append("\nSelected Object @"+e.getX()+","+e.getY());
				    	   				mImage.arr[c-1][r]=1;
				    	   				mImage.arr[c][r-1]=1;
				    	   				mImage.arr[c-1][r-1]=1;
				    	   				mImage.arr[c+1][r+1]=1;
				    	   				mImage.arr[c+1][r]=1;
				    	   				mImage.arr[c][r+1]=1;
				    	   				mImage.arr[c+1][r-1]=1;
				    	   				mImage.arr[c-1][r+1]=1;
				    	   				
				    	   				mImage.arr[c][r]=1;
				    	   				
				    	   			
				    	   			}
				    	   			else
				    	   			{
				    	   				txtrImageInformation.append("\nSelected Background @"+e.getX()+","+e.getY());
				    	   				mImage.arr[c-1][r]=2;
				    	   				mImage.arr[c][r-1]=2;
				    	   				mImage.arr[c-1][r-1]=2;
				    	   				mImage.arr[c+1][r+1]=2;
				    	   				mImage.arr[c+1][r]=2;
				    	   				mImage.arr[c][r+1]=2;
				    	   				mImage.arr[c+1][r-1]=2;
				    	   				mImage.arr[c-1][r+1]=2;
				    	   				
				    	   				mImage.arr[c][r]=2;
				    	   				
				    	   				
				    	   			 
				    	   			}
					            }
					            else
					            {
					            	JOptionPane.showMessageDialog(lblNewLabel, "Please select points within the image");
					            }
					            
					        }

							public void mouseEntered(MouseEvent arg0) {
								// TODO Auto-generated method stub
								
							}

							public void mouseExited(MouseEvent arg0) {
								
							}

							public void mousePressed(MouseEvent arg0) {
								
							}

							public void mouseReleased(MouseEvent arg0) {
								
							}
					    });
				    }
			}
		});
		button_1.setBackground(new Color(65, 105, 225));
		splitPane_4.setLeftComponent(button_1);
	}

}
