package elmir.sut.project.ui;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;

import static elmir.sut.project.EdgeDetection.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import elmir.sut.project.EdgeDetection;
//import elmir.sut.project.ui.ImagePanel;


@Slf4j
public class EdgeDetectionUI {

    private static final int FRAME_WIDTH = 1000;
    private static final int FRAME_HEIGHT = 600;
    private static final Font sansSerifBold = new Font("SansSerif", Font.BOLD, 22);
    private final ImagePanel sourceImage = new ImagePanel(500, 510);
    private final ImagePanel destImage = new ImagePanel(500, 510);
    private final JPanel mainPanel;
    public static JFrame mainFrame;
    
    private final EdgeDetection edgeDetection;
    BufferedImage imageBuffer;
    int availableThreads = Runtime.getRuntime().availableProcessors();
    File output;

    
    JTextField textDesc = new JTextField("Time taken milliseconds: ");
    JTextField sequentialText = new JTextField("Sequential");
    JTextField parallelText = new JTextField("Parallel");
    JTextField distributedText = new JTextField("Distributed");
    
    public EdgeDetectionUI() throws IOException {
    	
    	//here we create instance of Edge detection to get picture and to be able to convolute it
        edgeDetection = new EdgeDetection();
        
         mainFrame = createMainFrame();

        mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(sourceImage); //at the beginning will be same picture
        mainPanel.add(destImage); // 
       
        
        
        JPanel northPanel = fillNorthPanel();
        
        JPanel southPanel = fillSouthPanel();

        
        mainFrame.add(northPanel, BorderLayout.NORTH);
  
        mainFrame.add(mainPanel, BorderLayout.CENTER);
        
        mainFrame.add(southPanel, BorderLayout.SOUTH);
        mainFrame.setVisible(true);
        
    }
//___________________________________________________________came until here
    private JPanel fillSouthPanel() {
        JPanel southPanel = new JPanel();
        

        //SOUTH
        
        
        textDesc.setFont(sansSerifBold);
        textDesc.setEditable(false);
        
        sequentialText.setFont(sansSerifBold);
        parallelText.setFont(sansSerifBold);
        distributedText.setFont(sansSerifBold);
        
        sequentialText.setEditable(false);
        parallelText.setEditable(false);
        distributedText.setEditable(false);
        
        southPanel.add(textDesc);
        southPanel.add(sequentialText);
        southPanel.add(parallelText);
        southPanel.add(distributedText);
        
        return southPanel;

    }
    
    
    
    //part with buttons
    private JPanel fillNorthPanel() {
    	
    	
    	//button to select image to be convoluted
        JButton chooseButton = new JButton("Select Image");
        chooseButton.setFont(sansSerifBold);

        //part with buttons 
        JPanel northPanel = new JPanel();
        
        //dropdown list to select convolution type
        JComboBox filterType = new JComboBox();
        filterType.addItem(HORIZONTAL_FILTER);
        filterType.addItem(VERTICAL_FILTER);

        filterType.addItem(SOBEL_FILTER_VERTICAL);
        filterType.addItem(SOBEL_FILTER_HORIZONTAL);

        filterType.addItem(SCHARR_FILTER_VETICAL);
        filterType.addItem(SCHARR_FILTER_HORIZONTAL);
        filterType.setFont(sansSerifBold);
        //end of dropdown
        
        //button to apply convolution NORTH
        JButton detect = new JButton("Sequential");
        
        JButton detectP = new JButton("Parallel");
        
        JButton detectD = new JButton("Distributed");
        
        detect.setFont(sansSerifBold);
        
        detectP.setFont(sansSerifBold);
        detectD.setFont(sansSerifBold);
        
        
        
        //place where we add kernel selection, image selection and convolution application
        northPanel.add(filterType);
        northPanel.add(chooseButton);
        northPanel.add(detect);
        
        northPanel.add(detectP);
        northPanel.add(detectD);

        
        //function supporting image selection
        chooseButton.addActionListener(event -> {
        	//here we need get absolute path 
        	String basePath = new File("").getAbsolutePath();
        	
            
            
            //here we set to choose directly from our resources
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File(basePath+"/src/main/resources"));
            int action = chooser.showOpenDialog(null);
            if (action == JFileChooser.APPROVE_OPTION) {
                try {
                	//i have created this in order to trim file
                	output=new File(chooser.getSelectedFile().getAbsolutePath());
                	imageBuffer =ImageIO.read(new File( chooser.getSelectedFile().getAbsolutePath()));
                	
                	//rows must be divisible with thread number
                	
                	int moduleHeight = imageBuffer.getHeight()%availableThreads;
                	//replace fixed values with ones related to threads
                	// System.out.println("MODULE!!!!!!!MODULE" + moduleWidth);
                	 
                    imageBuffer=cropImage(imageBuffer,imageBuffer.getWidth(),imageBuffer.getHeight()-moduleHeight);
                    //System.out.println("CROP CROP AFTER :" + imageBuffer.getWidth());
                    moduleHeight = imageBuffer.getWidth()%availableThreads;
                    //System.out.println("MODULE!!!!!!!MODULE" + moduleWidth);
               	 
                    
                    //File outputFile = new File( basePath + "/edgesTmp.png");
                    //ImageIO.write(imageBuffer, "png", outputFile);
                    
                	ImageIO.write(imageBuffer,"png", output);
                	
                    sourceImage.setImage(chooser.getSelectedFile().getAbsolutePath());
                	
                	 //System.out.println("IMPORTANT!!!!!!!Name Image:" + sourceImage.getName());
                	 
                	// String basePathUI = new File("").getAbsolutePath();
                     //System.out.println(basePathUI);
                    
                   
                    //System.out.println("IMPORTANT!!!!!!!" + chooser.getSelectedFile().getAbsolutePath());
                    
                    mainPanel.updateUI();
                    //System.out.println("Something: " +  sourceImage);
                } catch (IOException e) {
                    //log.error("", e);
                    throw new RuntimeException(e);
                }
                
            }
        });

        //here is where whole program is activated
        detect.addActionListener(event -> {
            try {
            	//IMPORTANT
            	//here I need to chop picture into pieces and bring it back together
            	
            	//uploaded picture
                BufferedImage bufferedImage = ImageIO.read(new File(sourceImage.getFilePath()));
               
                //I need to chop it in an array and 
                
                File convolvedFile;
				try {
					convolvedFile = edgeDetection.detectEdges(bufferedImage, (String) filterType.getSelectedItem(), (String)"");
					destImage.setImage(convolvedFile.getAbsolutePath());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                //here I need to see whether it shows the picture, it does show.
            } catch (IOException e) {
                //log.error("", e);
                throw new RuntimeException(e);
            }sequentialText.setText( "Sequential: " + EdgeDetection.timeS);
        });
        
        detectP.addActionListener(event -> {
            try {
            	//IMPORTANT
            	//here I need to chop picture into pieces and bring it back together
            	
            	//uploaded picture
                BufferedImage bufferedImage = ImageIO.read(new File(sourceImage.getFilePath()));
                
                //I need to chop it in an array and 
                
                File convolvedFile;
				try {
					convolvedFile = edgeDetection.detectEdges(bufferedImage, (String) filterType.getSelectedItem(), (String)"P");
					destImage.setImage(convolvedFile.getAbsolutePath());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                //here I need to see whether it shows the picture, it does show.
            } catch (IOException e) {
                //log.error("", e);
                throw new RuntimeException(e);
            } parallelText.setText( "Parallel: " + EdgeDetection.timeP);
        });
        
        detectD.addActionListener(event -> {
            try {
            	//IMPORTANT
            	//here I need to chop picture into pieces and bring it back together
            	
            	//uploaded picture
                BufferedImage bufferedImage = ImageIO.read(new File(sourceImage.getFilePath()));
               
                //I need to chop it in an array and 
                
                File convolvedFile;
				try {
					convolvedFile = edgeDetection.detectEdges(bufferedImage, (String) filterType.getSelectedItem(), (String) "D");
					destImage.setImage(convolvedFile.getAbsolutePath());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                //here I need to see whether it shows the picture, it does show.
            } catch (IOException e) {
                //log.error("", e);
                throw new RuntimeException(e);
            }distributedText.setText( "Distributed: " + EdgeDetection.timeD);
        });
        return northPanel;
    }

    private JFrame createMainFrame() {
        JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        return mainFrame;
    }
    
    private BufferedImage cropImage(BufferedImage src, int width, int height) {
        BufferedImage dest = src.getSubimage(0, 0, width, height);
       System.out.println("CROP CROP CROP :" + dest.getWidth());
        return dest; 
     }
	

}
