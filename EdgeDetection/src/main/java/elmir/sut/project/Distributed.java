package elmir.sut.project;

import java.util.Arrays;
//import java.util.concurrent.CountDownLatch;

import mpi.Comm;
import mpi.MPI;


public class Distributed {
	public static int rootProcessorRank = EdgeDetection.rootProcessorRank;
	

	//need communicator to send messages between 
	private static Comm comunicator = EdgeDetection.comunicator;
	
	
	//needed to know how to slice our data 
	private static int numberOfProcessors = EdgeDetection.numberOfProcessors;
	
	private static int currentProcessorRank;
	
	//public static int slice = EdgeDetection.;
	
	
	//setters
	
	
	double[][] input;
    static int width;
    static int height;
    static double[][] kernel;
    static int kernelWidth;
    static int kernelHeight;
    int smallWidth = width - kernelWidth + 1;//this is used so we do not come till edge of picture without sufficent pixels
    int smallHeight = height - kernelHeight + 1; 
    double[][] output;
    
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
		
		System.out.println("DISTRIBUTED:" + input.length + ", " + width+ ", " + height+ ", "+ kernel+ ", " + kernelWidth+ ", " + kernelHeight);
		
		output = new double[width][height];
		
}
    
    double[][] returnOutput() throws InterruptedException {
    	return rootAction(input);
    }
	
	
	public static double[][] rootAction(double[][] A) throws InterruptedException {
		currentProcessorRank = EdgeDetection.currentProcessorRank;   
		 System.out.println("I need current rank:" + currentProcessorRank);
		 System.out.println("Heigt/number of proccesors:" + height +"  Number of proccesors: " + numberOfProcessors);
       int initPartition = width/(numberOfProcessors-1);
       System.out.println("INITPARTITION UPPER:" + initPartition);
       
       int start = 0;
	   double[][] arrayData = null;
	    //how much data is given to each non root processor
	   int partition = initPartition;
	     
	   
	   
	   for (int i = 1; i<numberOfProcessors; i++) {
	    	
	    	//I changed this because of possible implementations
	    	if(i==(numberOfProcessors-1)) { //this deals when it comes to an and
	    		System.out.println("FOR :" + A.length +", "  + A[0].length + ", "+ start + ", "+partition);
	    		arrayData = Arrays.copyOfRange(A, start, A.length);
       	}
       	else {
       		arrayData = Arrays.copyOfRange(A, start, partition);
       		System.out.println("TYPE:" + arrayData.getClass().getSimpleName());
       		System.out.println("TYPE:" + arrayData[0][1]);
       	}
       	double[] b = new double[1];//just to satisfy array data type
       	b[0] = initPartition;
       	
       	//To send the size of array to be sent to the non root
       	//processors/cores to sort
       	//comm.Isend(data, start, size, type, to, flag
       	
       	comunicator.Isend(b, 0, 1, MPI.DOUBLE, i, 1);//use as a semaphore for the nonRootAction to wait
       	System.out.println("SENT b:" + b[0]);
       	
       	//To send the actual array to be sorted by the non
       	//root processors/cores
       	
       		//here we actually determine to which proccesor to be sent
          comunicator.Isend(arrayData, 0, initPartition, MPI.DOUBLE, i, 1);
           start = partition; 
           partition += initPartition;
       }
	   

	   System.out.println("DISTRIBUTED TEMP: height" +  height);
	   System.out.println("DISTRIBUTED TEMP: initPartition" +  initPartition);

	   
	   double[][] temp1 = new double[initPartition][height];
	   double[][] temp2 = new double[initPartition][height];
	   double[][] temp3 = new double[initPartition+1][height];
	   
//	    function nonRoot()
//	    comm.Recv(data, start, size, type, origin,flag)
//
	   
	   comunicator.Recv(temp1, 0, initPartition, MPI.DOUBLE, 1, 1);
	   comunicator.Recv(temp2, 0, initPartition, MPI.DOUBLE, 2, 1);
	   comunicator.Recv(temp3, 0, initPartition, MPI.DOUBLE, 3, 1);
	   

	    double[][] tempS = Arrays.copyOf(temp1, temp1.length + temp2.length);
		System.arraycopy(temp2, 0, tempS, temp1.length , temp2.length);
		
		double[][] almostDone = Arrays.copyOf(tempS, tempS.length + temp3.length);
		
		System.arraycopy(temp3, 0, almostDone, tempS.length, temp3.length);
		
		
		return almostDone;
	}
	
	
	public static void nonRootAction() {
		double[][] init = new double[1][1];
		comunicator.Recv(init, 0, 1, MPI.DOUBLE, rootProcessorRank, 1);
		System.out.println("DISTRIBUTED: height" +  height);
		int initialPart = (int)init[0][0];
		
		System.out.println("INIT RECIEVED FROM ROOT" +  init[0]);
		
        double[][]  arrayData = new double[initialPart][height];
        int end;
        
        currentProcessorRank = comunicator.Rank();
        System.out.println("I need current rank NONROOT:" + currentProcessorRank);
        
        comunicator.Recv(arrayData, 0, initialPart, MPI.DOUBLE, rootProcessorRank, 1);
       
        end = arrayData.length;
        
        //we need to convolute here
        convolutionFinal(arrayData, width, height, kernel, kernelWidth, kernelHeight );
       
        
        switch(currentProcessorRank){
        case 1:
        	comunicator.Send(arrayData, 0, end, MPI.DOUBLE, 0, 1);
        	break;
        case 2:
        	comunicator.Send(arrayData, 0, end, MPI.DOUBLE, 0, 1);
        	break;
        case 3:
        	comunicator.Send(arrayData, 0, end, MPI.DOUBLE, 0, 1);
        	break;
        }
        return;
	}
	
	
	
	
	
	
	
	
	
	
	
	public static double[][] convolutionFinal(double[][] input, int width, int height, double[][] kernel,
			int kernelWidth, int kernelHeight) {
		long currentTime = System.currentTimeMillis();

		int smallWidth = width - kernelWidth + 1;// this is used so we do not come till edge of picture without
													// sufficent pixels
		int smallHeight = height - kernelHeight + 1; // so we convolute last kernel sized matrix of the picture

		double[][] output = new double[width][height];// new matrix image accumulator

		fill2DMatrix(output, height, width); // put in zeros

//here we need to fill in each cell
		for (int i = 0; i < smallWidth; ++i) { // filling in the values starting from beginning
			for (int j = 0; j < smallHeight; ++j) {
				output[i + 1][j + 1] = singlePixelConvolution(input, i, j, kernel, kernelWidth, kernelHeight); // calculating
																												// every
																												// single
																												// pixel
																												// and
																												// saving
																												// it

			}
		}

		

		System.out.println("Size of output: " + output.length + "; " + output[0].length);
//radi print2D(output);

		long endTime3 = System.currentTimeMillis();
		System.out.println("Time it took before finishing: " + (endTime3 - currentTime));

		return output;
	}

	public static void fill2DMatrix(double[][] matrix, int height, int width) {
		for (int i = 0; i < width; ++i) { // for each cell we will set value to be zero 0
			for (int j = 0; j < height; ++j) {
				matrix[i][j] = 0;
			}
		}

	}

	// method that calculates calue for each pixel using convolution formula
	public static double singlePixelConvolution(double[][] input, // part of the picture we select
			int x, int y, // to get current part of the picture to be convoluted
			double[][] k, // actual kernel matrix
			int kernelWidth, // kernel size used in loop
			int kernelHeight) {
		double output = 0; // accumulator
		for (int i = 0; i < kernelWidth; ++i) {
			for (int j = 0; j < kernelHeight; ++j) {
				output = output + (input[x + i][y + j] * k[i][j]); // we traverse through kernel and multiply
			}
		}
		return output;
	}
	
	

}
