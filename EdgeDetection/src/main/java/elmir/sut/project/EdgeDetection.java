package elmir.sut.project;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
//IMPORTANT!!!
//OUTLINE
//need to chop every picture in small pieces 
//and to convolute each separately
//have them saved in file array
//bring file array back in one piece
//save that image and show it
import mpi.Comm;
import mpi.MPI;

public class EdgeDetection {
	int availableThreads = Runtime.getRuntime().availableProcessors();
	//int availableThreads = 2;
	
	public static int rootProcessorRank = 0;
	public static int currentProcessorRank;
	public static int numberOfProcessors;
	
	public static Comm comunicator;
	
	
	
    public static final String HORIZONTAL_FILTER = "Horizontal Filter";
    public static final String VERTICAL_FILTER = "Vertical Filter";

    public static final String SOBEL_FILTER_VERTICAL = "Sobel Vertical Filter";
    public static final String SOBEL_FILTER_HORIZONTAL = "Sobel Horizontal Filter";

    public static final String SCHARR_FILTER_VETICAL = "Scharr Vertical Filter";
    public static final String SCHARR_FILTER_HORIZONTAL = "Scharr Horizontal Filter";

    private static final double[][] FILTER_VERTICAL = {{1, 0, -1}, {1, 0, -1}, {1, 0, -1}};
    private static final double[][] FILTER_HORIZONTAL = {{1, 1, 1}, {0, 0, 0}, {-1, -1, -1}};

    private static final double[][] FILTER_SOBEL_V = {{1, 0, -1}, {2, 0, -2}, {1, 0, -1}};
    private static final double[][] FILTER_SOBEL_H = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};

    private static final double[][] FILTER_SCHARR_V = {{3, 0, -3}, {10, 0, -10}, {3, 0, -3}};
    private static final double[][] FILTER_SCHARR_H = {{3, 10, 3}, {0, 0, 0}, {-3, -10, -3}};

    private final HashMap<String, double[][]> filterMap;

    public EdgeDetection() {
        filterMap = buildFilterMap();

    }

    
    public File detectEdges(BufferedImage bufferedImage, String selectedFilter) throws IOException, InterruptedException {
    	//place where we got read original picture, not converted to array
        double[][][] image = transformImageToArray(bufferedImage); //conversion to array
        
        double[][] filter = filterMap.get(selectedFilter);//kernel
        
       // numberOfProcessors = comunicator.Size();
        
        double[][] convolvedPixels = applyConvolutionDistributed(bufferedImage.getWidth(),
                bufferedImage.getHeight(), image, filter); //to start process
        
        return createImageFromConvolutionMatrix(bufferedImage, convolvedPixels); 
        
        
    }
    

    private double[][][] transformImageToArray(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        double[][][] image = new double[3][height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = new Color(bufferedImage.getRGB(j, i));
                image[0][i][j] = color.getRed();
                image[1][i][j] = color.getGreen();
                image[2][i][j] = color.getBlue();
            }
        }
        return image;
    }

    private double[][] applyConvolution(int width, int height, double[][][] image, double[][] filter) {
    long current = System.currentTimeMillis();
        Convolution convolution = new Convolution();
        double[][] redConv = convolution.convolutionFinal(image[0], height, width, filter, 3, 3);
        double[][] greenConv = convolution.convolutionFinal(image[1], height, width, filter, 3, 3);
        double[][] blueConv = convolution.convolutionFinal(image[2], height, width, filter, 3, 3);
        double[][] finalConv = new double[redConv.length][redConv[0].length];
        //here we create gray version of convolution
        
        for (int i = 0; i < redConv.length; i++) {
            for (int j = 0; j < redConv[i].length; j++) {
                finalConv[i][j] = redConv[i][j] + greenConv[i][j] + blueConv[i][j];
            }
        }
        //System.out.println("Available threads ..." + availableThreads);
    long end = System.currentTimeMillis();
    System.out.println("TIME TAKEN: " + (end-current) + " Milliseconds");
        return finalConv;
    }
    
    /** TEST TEST TEST TESTTEST TEST */
    private double[][] applyConvolutionArray(int width, int height, double[][][] image, double[][] filter) {
        long current = System.currentTimeMillis();
            ConvolutionArray convolution = new ConvolutionArray();
            double[][] redConv = convolution.convolutionFinal(image[0], height, width, filter, 3, 3);
            double[][] greenConv = convolution.convolutionFinal(image[1], height, width, filter, 3, 3);
            double[][] blueConv = convolution.convolutionFinal(image[2], height, width, filter, 3, 3);
            double[][] finalConv = new double[redConv.length][redConv[0].length];
            //here we create gray version of convolution
            
            for (int i = 0; i < redConv.length; i++) {
                for (int j = 0; j < redConv[i].length; j++) {
                    finalConv[i][j] = redConv[i][j] + greenConv[i][j] + blueConv[i][j];
                }
            }
            //System.out.println("Available threads ..." + availableThreads);
        long end = System.currentTimeMillis();
        System.out.println("TIME TAKEN: " + (end-current) + " Milliseconds");
            return finalConv;
        }
    
    
    
    
    /** TEST TEST TEST TESTTEST TEST */
    
    
    
    
    private double[][] applyConvolutionParallel(int width, int height, double[][][] image, double[][] filter) {
    	long current = System.currentTimeMillis();
    	CountDownLatch latch = new CountDownLatch(availableThreads);
//    	ExecutorService executor = Executors.newFixedThreadPool(availableThreads);
   	 
       
        //RED PARALLEL 
        Parallel parallel = new Parallel(image[0], height, width, filter, 3, 3, latch, availableThreads); 
        double[][] redConv =parallel.returnOutput();
        
       
      //GREEN PARALLEL 
         parallel = new Parallel(image[1], height, width, filter, 3, 3, latch, availableThreads);    
        double[][] greenConv =parallel.returnOutput();
        

//      //BLUE PARALLEL 
        parallel = new Parallel(image[2], height, width, filter, 3, 3, latch, availableThreads);
//       
//         
        
       double[][] blueConv =parallel.returnOutput();
        
        
        
        double[][] finalConv = new double[redConv.length][redConv[0].length];
        //here we create gray version of convolution
        
        for (int i = 0; i < redConv.length; i++) {
            for (int j = 0; j < redConv[i].length; j++) {
                finalConv[i][j] = redConv[i][j] + greenConv[i][j] + blueConv[i][j];
            }
        }
        //System.out.println("Available threads ..." + availableThreads);
        long end = System.currentTimeMillis();
        System.out.println("TIME TAKEN: " + (end-current) + " Milliseconds");
        
        return finalConv;
    }
    
    /***
     * 
     * 
     * ?????????PROBA??????
     * @throws InterruptedException 
     * 
     * */
    
//    private double[][] applyConvolution(int width, int height, double[][][] image, double[][] filter) {
//    	ArrayList<ArrayList<ArrayList<Integer>>> imageArrayList = new ArrayList <ArrayList<ArrayList<Integer>>>(Arrays.asList(image));
//    	System.out.println(Arrays.toString(image));
//    	
//
//
////        ConvolutionParallel convolution = new ConvolutionParallel();
////        double[][] redConv = convolution.sliceImage(image[0], height, width, filter, 3, 3);
////        double[][] greenConv = convolution.sliceImage(image[1], height, width, filter, 3, 3);
////        double[][] blueConv = convolution.sliceImage(image[2], height, width, filter, 3, 3);
////        double[][] finalConv = new double[redConv.length][redConv[0].length];
////        //here we create gray version of convolution
////        
////        for (int i = 0; i < redConv.length; i++) {
////            for (int j = 0; j < redConv[i].length; j++) {
////                finalConv[i][j] = redConv[i][j] + greenConv[i][j] + blueConv[i][j];
////            }
////        }
////        return finalConv;
//    	return null;
//    }
//    
//    
    
    private double[][] applyConvolutionDistributed(int width, int height, double[][][] image, double[][] filter) throws InterruptedException {
    	long current = System.currentTimeMillis();
    	CountDownLatch latch = new CountDownLatch(availableThreads);
//    	ExecutorService executor = Executors.newFixedThreadPool(availableThreads);
   	 
       
        //RED PARALLEL 
        Distributed distributed = new Distributed(image[0], height, width, filter, 3, 3); 
        double[][] redConv =distributed.returnOutput();
        
       
      //GREEN PARALLEL 
        distributed = new Distributed(image[1], height, width, filter, 3, 3);    
        double[][] greenConv =distributed.returnOutput();
        

//      //BLUE PARALLEL 
        distributed = new Distributed(image[2], height, width, filter, 3, 3);
//       
//         
        
       double[][] blueConv =distributed.returnOutput();
        
        
        
        double[][] finalConv = new double[redConv.length][redConv[0].length];
        //here we create gray version of convolution
        
        for (int i = 0; i < redConv.length; i++) {
            for (int j = 0; j < redConv[i].length; j++) {
                finalConv[i][j] = redConv[i][j] + greenConv[i][j] + blueConv[i][j];
            }
        }
        //System.out.println("Available threads ..." + availableThreads);
        long end = System.currentTimeMillis();
        //System.out.println("TIME TAKEN: " + (end-current) + " Milliseconds");
        
        return finalConv;
    }
    
    
    
    
    /*************** */
    private File createImageFromConvolutionMatrix(BufferedImage originalImage, double[][] imageRGB) throws IOException {
    	//here we just get width height in order to be same size, this is empty picture with proportions as original
        BufferedImage writeBackImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        for (int i = 0; i < imageRGB.length; i++) {
            for (int j = 0; j < imageRGB[i].length; j++) {
                Color color = new Color(fixOutOfRangeRGBValues(imageRGB[i][j]),
                        fixOutOfRangeRGBValues(imageRGB[i][j]),//just to fix if bigger or smaller
                        fixOutOfRangeRGBValues(imageRGB[i][j]));//same, will be grey but converting it for computer
                writeBackImage.setRGB(j, i, color.getRGB()); //we have image created back
            }
        } 
        //my experimenting part
        
        String basePath = new File("").getAbsolutePath();
       // System.out.println(basePath);
       

//        String path = new File("src/main/resources/conf.properties")
//                                                               .getAbsolutePath();
       // System.out.println(basePath);
        
        
        
        File outputFile = new File( basePath + "/edgesTmp.png");
        ImageIO.write(writeBackImage, "png", outputFile); //here we save image back
        return outputFile;
    }

    private int fixOutOfRangeRGBValues(double value) {
        if (value < 0.0) {
            value = -value;
        }
        if (value > 255) {
            return 255;
        } else {
            return (int) value;
        }
    }

    private HashMap<String, double[][]> buildFilterMap() {
        HashMap<String, double[][]> filterMap;
        filterMap = new HashMap<>();
        filterMap.put(VERTICAL_FILTER, FILTER_VERTICAL);
        filterMap.put(HORIZONTAL_FILTER, FILTER_HORIZONTAL);

        filterMap.put(SOBEL_FILTER_VERTICAL, FILTER_SOBEL_V);
        filterMap.put(SOBEL_FILTER_HORIZONTAL, FILTER_SOBEL_H);

        filterMap.put(SCHARR_FILTER_VETICAL, FILTER_SCHARR_V);
        filterMap.put(SCHARR_FILTER_HORIZONTAL, FILTER_SCHARR_H);
        return filterMap;
    }


}
