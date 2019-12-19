package elmir.sut.project;

import java.util.Arrays;
//import java.util.concurrent.CountDownLatch;

import mpi.Comm;
import mpi.MPI;


public class DistributedMPI {
	public static int rootProcessorRank = RunEdgeDetection.rootProcessorRank;
	

	//need communicator to send messages between 
	private static Comm comunicator = RunEdgeDetection.comunicator;
	
	
	//needed to know how to slice our data 
	//private static int numberOfProcessors = RunEdgeDetection.numberOfProcessors;
	
	private static int currentProcessorRank;
	
	//public static int slice = EdgeDetection.;
	
	
	//setters
	
	
//	static double[][] input1;
//	static double[][] input2;
//	static double[][] input3;
//    static int width;
//    static int height;
//    static double[][] kernel;
//    static int kernelWidth;
//    static int kernelHeight;
//    int smallWidth = width - kernelWidth + 1;//this is used so we do not come till edge of picture without sufficent pixels
//    int smallHeight = height - kernelHeight + 1; 
//    double[][] output;
    static double[] arrayImage1;
    static double[] arrayImage2;
    static double[] arrayImage3;
//    //static double[] arrayImage;
//    static double[]kernelArray;
    
    //setter
    DistributedMPI(double[][] A1, double[][] A2, double[][] A3,
            int width, int height,
            double[][] kernel,
            int kernelWidth,
            int kernelHeight) throws InterruptedException{
    	
////		this.input1=A1;
////		this.input2=A2;
////		this.input3=A3;
//		this.width=width;
//		this.height=height;
//		this.kernel=kernel;
//		this.kernelWidth=kernelWidth;
//		this.kernelHeight=kernelHeight;
//		
//		
//		arrayImage1 = convertMatrixToArray(A1); //WORKS
//		arrayImage2 = convertMatrixToArray(A2); //WORKS
//		arrayImage3 = convertMatrixToArray(A3); //WORKS
//		
		
		
}
    
//    double[][] returnOutput() throws InterruptedException {
//    	
//    	
//    	return  convertArrayToMatrix(rootAction(arrayImage1,arrayImage2,arrayImage3), width, height);
//    }
//	
    
	
	public static double[][] rootAction(double[][] R, double[][] G, double[][] B, int height, int width,double[][] kernel, int kernelWidth, int kernelHeight) throws InterruptedException {  
		double[] kernelArray=convertMatrixToArray(kernel);
		double[] A1 = convertMatrixToArray(R); //WORKS
		double[] A2 = convertMatrixToArray(G); //WORKS
		double[] A3 = convertMatrixToArray(B); //WORKS
	int numberOfProcessors = RunEdgeDetection.numberOfProcessors;
	
	   for (int i = 1; i<numberOfProcessors; i++) {
	   
	    double b [] = new double[1+1+1+1+(kernelWidth*kernelHeight)];
//       	double[] b = new double[1];//just to satisfy array data type
       	
       	b[0] =width;
       	b[1]=height;
       	b[2]=kernelWidth;
       	b[3]=kernelHeight;
     	
       	//solved
       	
       	for(int j=0;j<kernelArray.length;j++) {
       		b[4+j]=kernelArray[j];
       	}
       	
       	
      System.out.println("B length:" + b[4] );

       	
       	//To send the size of array to be sent to the non root
       	//processors/cores to sort
       	//comm.Isend(data, start, size, type, to, flag
//       	double[] arr = new double[(int) Integer.MAX_VALUE];
//       	System.out.println("ARR length" + arr.length);
      	
       	comunicator.Isend(b, 0, (1+1+1+1+(kernelWidth*kernelHeight)), MPI.DOUBLE, i, 1);//use as a semaphore for the nonRootAction to wait
       	System.out.println("A1 length:" + A1.length);
       	  
       	
       if(i==1) {
    	   comunicator.Isend(A1, 0, A1.length, MPI.DOUBLE, i, 1);
       }
       if(i==2) {
    	   comunicator.Isend(A2, 0, width*height, MPI.DOUBLE, i, 1);
       }
       if(i==3) {
    	   comunicator.Isend(A3, 0, width*height, MPI.DOUBLE, i, 1);
       }
       		//here we actually determine to which proccesor to be sent
          
          
          
           
       }
	   
	   //until here we send

	   

	    double[] temp1 = new  double[A1.length];
	   double[] temp2 = new double[A1.length];
	   double[] temp3 = new double[A1.length+1];
	   
//	    function nonRoot()
//	    comm.Recv(data, start, size, type, origin,flag)
//
	   System.out.println("BEFORE: " + temp1[5]);
	   //here is the actual problem!!! does not go forward from here
	   
	   /** PROBLEM PROBLEM PROBLEM PROBLEM PROBLEM  -> */
	   
	   
	   comunicator.Recv(temp1, 0, A1.length, MPI.DOUBLE, 1, 1);
	   System.out.println("AFTER: ################################# " );
	   
	   comunicator.Recv(temp2, 0, A1.length, MPI.DOUBLE, 2, 1);
	   System.out.println("AFTER: ################################# " );
	   comunicator.Recv(temp3, 0, A1.length, MPI.DOUBLE, 3, 1);
	   System.out.println("AFTER: ################################# " );
	  
	    System.out.println("AFTER: " +temp1[5]);
//		
	   

	   
		
		System.out.println("<<<<<<<<<<<<<<<<<<"+">>>>>>>>>>>>>>>");
		System.out.println("<<<<<<<<<<<<<<<<<||||||||<"+">>>||||||>>>>>>>>>>>>");
		//MPI.Finalize();
		System.out.println("RETURNED!");
		double[] arrayImage=new double[temp1.length];
		
		
		System.out.println();
		for(int i = 0; i < temp1.length;i++) {
			arrayImage[i]=temp1[i]+temp2[i]+temp3[i];
		}
		//return matrix immidately
		
		
		 return convertArrayToMatrix(arrayImage, width, height);
		
	}
	
	
	public static void nonRootAction() {
			
		
		
		
		double[] init = new double[13];
		comunicator.Recv(init, 0, 15, MPI.DOUBLE, rootProcessorRank, 1);
		System.out.println("INIT RECIEVED FROM ROOT " +  init[0]);
//		System.out.println("RECIEVED: height" +  height);
		
		int width = (int)init[0];
		int height = (int)init[1];
		int kernelWidth = (int)init[2];
		int kernelHeight = (int)init[3];
		
		double[] kernel =  Arrays.copyOfRange(init, 4, init.length);
		
//		for (int i = 0; i < init.length; i++) {
//			
//			System.out.print(init[i] + "|__"+i+"__|");
//		}
//		System.out.println();
		
        double[]  arrayData = new double[height*width];
       
        
        currentProcessorRank = comunicator.Rank();
        System.out.println("I need current rank NONROOT:" + currentProcessorRank);
        
        
        comunicator.Recv(arrayData, 0, height*width, MPI.DOUBLE, rootProcessorRank, 1);
        
        
        
        
        int size = height*width;
       
      
        
//        System.out.println("NONROOT END SWITCH: " + arrayData.length);
       // System.out.println("Check number of elements "+ Distributed.kernelWidth*Distributed.kernelHeight);
       
        //we need to convolute here
//        System.out.println("WIDTH: "+width+" HEIGHT:"+ height);
//        System.out.println("WHICH DATA WE NEED" +arrayData.length/3);
//        
        arrayData = convolutionFinalArray(arrayData, width, height, kernel, kernelWidth, kernelHeight );
       
        
        switch(currentProcessorRank){
        case 1:
        	System.out.println("SENT================! CASE:1" +arrayData.length);
        	comunicator.Send(arrayData, 0, size, MPI.DOUBLE, 0, 1);
        	
        	break;
        case 2:
        	comunicator.Send(arrayData, 0, size, MPI.DOUBLE, 0, 1);
        	System.out.println("SENT! CASE:2 " +arrayData.length);
        	break;
        case 3:
        	comunicator.Send(arrayData, 0, size, MPI.DOUBLE, 0, 1);
        	System.out.println("SENT! CASE:3 " +arrayData.length);
        	break;
        }
        return;
	}
	
	
	
	
	
	
	
	
	public static double[] convolutionFinalArray(double[] input, int width, int height, double[] kernel,
			int kernelWidth, int kernelHeight) {

		int smallWidth = width - kernelWidth + 1;// this is used so we do not come till edge of picture without
													// sufficent pixels
		int smallHeight = height - kernelHeight + 1; // so we convolute last kernel sized matrix of the picture

		double[] outputArray = new double[width * height];
		double[] inputArray = input;
//  matrix[i][j] = array [i*width + j]
		System.out.println("INPUT MATRIX: width/height   " + width + "/" + height);
// System.out.println("INPUT MATRIX: width/height   "  +input.length+"/"+input[0].length);
// fill2DMatrix(output, height, width); //put in zeros

//here we need to fill in each cell
		for (int i = 0; i < smallWidth; ++i) { // filling in the values starting from beginning
			for (int j = 0; j < smallHeight; ++j) {
				outputArray[(i + 1) * height + (j + 1)] = singlePixelConvolutionArray(inputArray, i, j, kernel,
						kernelWidth, kernelHeight, height);

			}
		}

// System.out.println("Matrix number of elements: " + (input.length*input[0].length));
		System.out.println("Array number of elements: " + (outputArray.length));
		return outputArray;
	}
	

	public static void fill2DMatrix(double[][] matrix, int height, int width) {
		for (int i = 0; i < width; ++i) { // for each cell we will set value to be zero 0
			for (int j = 0; j < height; ++j) {
				matrix[i][j] = 0;
			}
		}

	}

	  //method that calculates calue for each pixel using convolution formula
    public static double singlePixelConvolutionArray(double[] input, //part of the picture we select
                                                int x, int y, //to get current part of the picture to be convoluted
                                                double[] k, //actual kernel matrix
                                                int kernelWidth, //kernel size used in loop
                                                int kernelHeight, int height) {
    	
    	System.out.println();
        double output = 0; //accumulator
        for (int i = 0; i < kernelWidth; ++i) {
            for (int j = 0; j < kernelHeight; ++j) {
            	//x should be width or column number
                output = output + (input[(x+i)*height+(j+y)] * k[i*kernelWidth +j]); //we traverse through kernel and multiply              
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
		  }else System.out.println("ALL ZEROS IN MATRIX. ERROR!!" + height*width+ "==" +array.length);
		  
		  
		  return matrix;
	  }

	
	  	

}
