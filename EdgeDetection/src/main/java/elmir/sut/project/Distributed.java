package elmir.sut.project;

import java.util.Arrays;
//import java.util.concurrent.CountDownLatch;

import mpi.Comm;
import mpi.MPI;


public class Distributed {
	public static int rootProcessorRank = RunEdgeDetection.rootProcessorRank;
	

	//need communicator to send messages between 
	private static Comm comunicator = RunEdgeDetection.comunicator;
	
	
	//needed to know how to slice our data 
	private static int numberOfProcessors = RunEdgeDetection.numberOfProcessors;
	
	private static int currentProcessorRank;
	
	//public static int slice = EdgeDetection.;
	
	
	//setters
	
	
	static double[][] input;
    static int width;
    static int height;
    static double[][] kernel;
    static int kernelWidth;
    static int kernelHeight;
    int smallWidth = width - kernelWidth + 1;//this is used so we do not come till edge of picture without sufficent pixels
    int smallHeight = height - kernelHeight + 1; 
    double[][] output;
    static double[] arrayImage;
    
    //setter
    Distributed(double[][] input,
            int width, int height,
            double[][] kernel,
            int kernelWidth,
            int kernelHeight) throws InterruptedException{
    	
		this.input=input;
		this.width=width;
		this.height=height;
		this.kernel=kernel;
		this.kernelWidth=kernelWidth;
		this.kernelHeight=kernelHeight;
		
		System.out.println("DISTRIBUTED:" + input.length + ", " + width+ ", " + height+ ", "+ ", " + kernelWidth+ ", " + kernelHeight);
		
		arrayImage = convertMatrixToArray(input); //WORKS
		
		System.out.println("ARRAY ELMIR:" + arrayImage.length + ", " + width+ ", " + height+ ", "+ ", " + kernelWidth+ ", " + kernelHeight);
		
}
    
    double[][] returnOutput() throws InterruptedException {
    	
    	return  convertArrayToMatrix(rootAction(arrayImage), height, width);
    }
	
    
	
	public static double[] rootAction(double[] A) throws InterruptedException {
		System.out.println("-------- ROOT  ROOT  ROOT  ROOT -----------");
		//currentProcessorRank = RunEdgeDetection.currentProcessorRank;   
//		 System.out.println("I need current rank:" + currentProcessorRank);
//		 System.out.println("Heigt/number of proccesors:" + height +"  Number of proccesors: " + numberOfProcessors);
		 
		System.out.println("HERE WE SEE VALUES arrayImage.length  "  + arrayImage.length);
		System.out.println("HERE WE SEE VALUES numberOfProcessors  " + numberOfProcessors);
		
       int initPartition = arrayImage.length/(numberOfProcessors-1); //need to get number of elements of array to be proccesed by single proccesor
       System.out.println("INITPARTITION UPPER:" + initPartition + " Width value" + arrayImage.length);
       
       int start = 0;
	   double[] arrayData = null;
	    //how much data is given to each non root processor
	   int partition = initPartition;
	    
	   
	   
	   for (int i = 1; i<numberOfProcessors; i++) {
	    	
	    	//I changed this because of possible implementations
	    	if(i==(numberOfProcessors-1)) { //this deals when it comes to an and
	    		System.out.println("FOR :" + A.length +", "   + ", "+ start + ", "+partition);
	    		arrayData = Arrays.copyOfRange(A, start, A.length);
       	}
       	else {
       		arrayData = Arrays.copyOfRange(A, start, partition);
       		System.out.println("FOR :" + A.length +", " +", "+ start + ", "+partition);
       	}
	    
	    double b [] = new double[1+1+1+1+1+(kernelWidth*kernelHeight)];
//       	double[] b = new double[1];//just to satisfy array data type
       	b[0] = initPartition;
       	b[1] =width;
       	b[2]=height;
       	b[3]=kernelWidth;
       	b[4]=kernelHeight;
       	
       	double[]kernelArray=convertMatrixToArray(kernel);
       	for(int j=0;j<kernelArray.length;j++) {
       		b[5+j]=kernelArray[j];
       	}
       	
       	
      System.out.println("B length:" + b[4] );

       	
       	//To send the size of array to be sent to the non root
       	//processors/cores to sort
       	//comm.Isend(data, start, size, type, to, flag
       	
      
       	comunicator.Isend(b, 0, 14, MPI.DOUBLE, i, 1);//use as a semaphore for the nonRootAction to wait
       	System.out.println("SENT b:" + b[0]);
       	
       	
       		//here we actually determine to which proccesor to be sent
          comunicator.Isend(arrayData, 0, initPartition, MPI.DOUBLE, i, 1);
          System.out.println("SENT PARTITION :" + partition);
           start = partition; 
           partition += initPartition;
       }
	   //until here works

	   

	   
	   double[] temp1 = new double[initPartition];
	   double[] temp2 = new double[initPartition];
	   double[] temp3 = new double[initPartition+1];
	   
//	    function nonRoot()
//	    comm.Recv(data, start, size, type, origin,flag)
//
	   System.out.println("BEFORE: " + temp1[5]);
	   //here is the actual problem!!! does not go forward from here
	   
	   /** PROBLEM PROBLEM PROBLEM PROBLEM PROBLEM  -> */
	   
	   comunicator.Recv(temp1, 0, initPartition, MPI.DOUBLE, 1, 1);
	   comunicator.Recv(temp2, 0, initPartition, MPI.DOUBLE, 2, 1);
	   comunicator.Recv(temp3, 0, initPartition, MPI.DOUBLE, 3, 1);
	   
	    System.out.println("AFTER: " +temp1[5]);
	   

	    double[] tempS = Arrays.copyOf(temp1, temp1.length + temp2.length);
		System.arraycopy(temp2, 0, tempS, temp1.length , temp2.length);
		
		double[] almostDone = Arrays.copyOf(tempS, tempS.length + temp3.length);
		
		System.arraycopy(temp3, 0, almostDone, tempS.length, temp3.length);
		
		
		return almostDone;
		
	}
	
	
	public static void nonRootAction() {
			
		
		System.out.println("--------NON ROOT NON ROOT NON ROOT NON ROOT -----------");
		
		double[] init = new double[14];
		comunicator.Recv(init, 0, 14, MPI.DOUBLE, rootProcessorRank, 1);
		System.out.println("INIT RECIEVED FROM ROOT " +  init[0]);
//		System.out.println("RECIEVED: height" +  height);
		int initialPart = (int)init[0];
		int width = (int)init[1];
		int height = (int)init[2];
		int kernelWidth = (int)init[3];
		int kernelHeight = (int)init[4];
		double[] kernel =  Arrays.copyOfRange(init, 5, init.length);
		
		

		
        double[]  arrayData = new double[initialPart];
        int end;
        
        currentProcessorRank = comunicator.Rank();
        System.out.println("I need current rank NONROOT:" + currentProcessorRank);
        
        comunicator.Recv(arrayData, 0, initialPart, MPI.DOUBLE, rootProcessorRank, 1);
       
        for(int i = 0 ; i<init.length; i++) {
        	System.out.print(init[i] + "~");
        }
        System.out.println();
        end = arrayData.length;
        //this prints out
        System.out.println("NONROOT END SWITCH: " + arrayData.length);
       // System.out.println("Check number of elements "+ Distributed.kernelWidth*Distributed.kernelHeight);
       
        //we need to convolute here
        arrayData = convolutionFinal(arrayData, width, height, kernel, kernelWidth, kernelHeight );
       
        
        switch(currentProcessorRank){
        case 1:
        	System.out.println("SENT================! CASE:1" +arrayData.length);
        	comunicator.Send(arrayData, 0, end, MPI.DOUBLE, 0, 1);
        	
        	break;
        case 2:
        	comunicator.Send(arrayData, 0, end, MPI.DOUBLE, 0, 1);
        	System.out.println("SENT! CASE:2" +arrayData.length);
        	break;
        case 3:
        	comunicator.Send(arrayData, 0, end, MPI.DOUBLE, 0, 1);
        	System.out.println("SENT! CASE:3" +arrayData.length);
        	break;
        }
        return;
	}
	
	
	
	
	
	
	
	
	//we will try to convert it to single array conversion
    public static double[] convolutionFinal(double[] input,
                                           int width, int height,
                                           double[] kernel,
                                           int kernelWidth,
                                           int kernelHeight) {	
    	 System.out.println("WIDTH HEIGHT " + width +" "+height);
        int smallWidth = width - kernelWidth + 1;//this is used so we do not come till edge of picture without sufficent pixels
        int smallHeight = height - kernelHeight + 1; //so we convolute last kernel sized matrix of the picture
          
        double[][] output = new double[width][height];//new matrix image accumulator        
        //now we need to create loop to accept array
        
        double[] outputArray = new double[width*height];
        double[] inputArray = input;
    //  matrix[i][j] = array [i*width + j]
        
       // fill2DMatrix(output, height, width); //put in zeros
      //  System.out.println("Matrix number " + input[50]);
        
//       
//        //here we need to fill in each cell
//        System.out.println("INPUT MATRIX: width/height   "  +width+"/"+height);
//        System.out.println("INPUT MATRIX: width/height   "  +input.length+"/"+input.length);
//        
       // System.out.println("LOOP:" + smallWidth + " : " + smallHeight +": TOTAL: " + smallWidth*smallHeight+"<"+outputArray.length );
        for (int i = 0; i < smallWidth; ++i) { //filling in the values starting from beginning
            for (int j = 0; j < smallHeight; ++j) {
//            	System.out.println("Matrix number " + input[i][j]);

//            	System.out.println("H number " + smallWidth);
//            	System.out.println("W number " + smallHeight);
                outputArray[(i+1)*height+(j+1)] =  singlePixelConvolutionArray(inputArray, i, j, kernel,
                        kernelWidth, kernelHeight, smallHeight);
                //System.out.println("Outputarray number " + outputArray[(i+1)*height+(j+1)]);
                //  matrix[i+1][j+1] = array [i*width + j]
      
            }
        }
        return outputArray;
    }
	
	
	

	public static void fill2DMatrix(double[][] matrix, int height, int width) {
		for (int i = 0; i < width; ++i) { // for each cell we will set value to be zero 0
			for (int j = 0; j < height; ++j) {
				matrix[i][j] = 0;
			}
		}

	}

	// method that calculates calue for each pixel using convolution formula
	public static double singlePixelConvolutionArray(double[] input, // part of the picture we select
			int x, int y, // to get current part of the picture to be convoluted
			double[] k, // actual kernel matrix
			int kernelWidth, // kernel size used in loop
			int kernelHeight, int height) {

		double output = 0; // accumulator
		for (int i = 0; i < kernelWidth; ++i) {
			for (int j = 0; j < kernelHeight; ++j) {
//ovdje nesto ne stima
//  matrix[i][j] = array [i*width + j]
// kernel[i][j] = k[i*kernelWidth + j]

//input[x + i][y + j] == array[(x]
				output = output + (input[(x + i) * height + (j + y)] * k[i*kernelWidth + j]); // we traverse through kernel and
																					// multiply
			}
		}
		return output;
	}
	
	
	
	//taken from my Matrix project
	static double[] convertMatrixToArray (double[][]matrix) {
		  double[] array = new double[matrix.length*matrix[0].length];
		  int m = matrix[0].length;
		  for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					array[ i*m + j ] =	matrix[i][j];
					
				}
				
			} 
		return array;
		  
	  }
	
	//here is the problem needed to fix!!!!!! 
	static double[][] convertArrayToMatrix(double[] array,  int height, int width) {
		  double[][] matrix = new double[height][width];
		  int m=width;
		  
		  if((height*width) == array.length) {
			  for (int i = 0; i < matrix.length; i++) {
					for (int j = 0; j < matrix[0].length; j++) {
							matrix[i][j] = array[ i*m + j ];
						
					}
					
				} 
		  }else System.out.println("ALL ZEROS IN MATRIX. ERROR!!");
		  
		  
		  return matrix;
	  }
	  	

}
