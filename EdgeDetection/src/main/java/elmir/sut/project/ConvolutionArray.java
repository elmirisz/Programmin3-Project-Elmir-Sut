package elmir.sut.project;

import java.util.Arrays;



// DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS 
public class ConvolutionArray {
	

	//method which fills matrix with instance values 
	public static void fill2DMatrix (double[][] matrix, int height, int width) {
		for (int i = 0; i < width; ++i) {  //for each cell we will set value to be zero 0
            for (int j = 0; j < height; ++j) {
                matrix[i][j] = 0;
            }
        }
		
	}
	
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
	  
	
	//method that calculates calue for each pixel using convolution formula
    public static double singlePixelConvolution(double[][] input, //part of the picture we select
                                                int x, int y, //to get current part of the picture to be convoluted
                                                double[][] k, //actual kernel matrix
                                                int kernelWidth, //kernel size used in loop
                                                int kernelHeight) {
        double output = 0; //accumulator
        for (int i = 0; i < kernelWidth; ++i) {
            for (int j = 0; j < kernelHeight; ++j) {
                output = output + (input[x + i][y + j] * k[i][j]); //we traverse through kernel and multiply
            }
        }
        return output;
    }

  //method that calculates calue for each pixel using convolution formula
    public static double singlePixelConvolutionArray(double[] input, //part of the picture we select
                                                int x, int y, //to get current part of the picture to be convoluted
                                                double[][] k, //actual kernel matrix
                                                int kernelWidth, //kernel size used in loop
                                                int kernelHeight, int height) {
    	
        double output = 0; //accumulator
        for (int i = 0; i < kernelWidth; ++i) {
            for (int j = 0; j < kernelHeight; ++j) {
            	//ovdje nesto ne stima
            //  matrix[i][j] = array [i*width + j]
            	
            //input[x + i][y + j] == array[(x]
                output = output + (input[(x+i)*height+(j+y)] * k[i][j]); //we traverse through kernel and multiply              
            }
        }
        return output;
    }

    
    
    
    
    /** 
     * input[x][y]  position of original pixel 
     * input[x + i][y + j] taking value of neighboring  pixels
     *  * k[i][j] multyplying with kernel position value
     * 
     * return output returns sum of all these multiplications 
     * 
     * */
    
    

   //we will try to convert it to single array conversion
    public static double[][] convolutionFinal(double[][] input,
                                           int width, int height,
                                           double[][] kernel,
                                           int kernelWidth,
                                           int kernelHeight) {	
	
        int smallWidth = width - kernelWidth + 1;//this is used so we do not come till edge of picture without sufficent pixels
        int smallHeight = height - kernelHeight + 1; //so we convolute last kernel sized matrix of the picture
               
        double[][] output = new double[width][height];//new matrix image accumulator        
        //now we need to create loop to accept array
        
        double[] outputArray = new double[width*height];
        double[] inputArray = convertMatrixToArray(input);
    //  matrix[i][j] = array [i*width + j]
        
       // fill2DMatrix(output, height, width); //put in zeros
        System.out.println("Matrix number " + input[50][50]);
        
        System.out.println("Array number " + inputArray[50*height+50]);
        //here we need to fill in each cell
        System.out.println("INPUT MATRIX: width/height   "  +width+"/"+height);
        System.out.println("INPUT MATRIX: width/height   "  +input.length+"/"+input[0].length);
        
        for (int i = 0; i < smallWidth; ++i) { //filling in the values starting from beginning
            for (int j = 0; j < smallHeight; ++j) {
//            	System.out.println("Matrix number " + input[i][j]);
//                
//                System.out.println("Array number " + inputArray[i*height+j]);
                outputArray[(i+1)*height+(j+1)] =  singlePixelConvolutionArray(inputArray, i, j, kernel,
                        kernelWidth, kernelHeight, height);
                
                //  matrix[i+1][j+1] = array [i*width + j]
      
            }
        }
        return convertArrayToMatrix(outputArray, width, height);
    }

    
//    public static void print2D(double mat[][]) 
//    { 
//        // Loop through all rows 
//        for (double[] row : mat) 
//  
//            // converting each row as string 
//            // and then printing in a separate line 
//            System.out.println(Arrays.toString(row)); 
//    } 

   
}


//DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS DONE WORKS 
