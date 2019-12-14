package matrix;

//The way most languages store multi-dimensional arrays is by doing a conversion like the following:
//
//If matrix has size, n by m [i.e. i goes from 0 to (n-1) and j from 0 to (m-1) ], then:
//
//matrix[ i ][ j ] = array[ i*m + j ].
//
//
//


public class Experiment {

	//practising to separate array in distributed 
	 public static void main(String[] args) {
		double[][] matrix = new double[9][6];
		double[][] kernel = new double[3][3];
		kernel[1][1]=1;
		//printMatrix(kernel);
		
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j]= i+j;
			}
		}
		
		printMatrix(matrix);
		
		double[] array = convertMatrixToArray(matrix);
		System.out.println("Matrix value: "+ matrix[3][5]);
		
//		printArray(array);
//		System.out.println();
//		System.out.println("Array value: "+ array[3*matrix[0].length+5]);
//		
		//CONCLUSION
		//  matrix[i][j] = array [i*width + j]
		//width = broj kolona
		
		
		double[][] convertedMatrix = convertArrayToMatrix(array,matrix.length,matrix[0].length);
//		System.out.println("convertedMatrix value: "+ convertedMatrix[5][5]);
//		System.out.println("Matrix value: "+ matrix[5][5]);
//		
		System.out.println("____________________________________");
		//printMatrix(convertedMatrix);
		
		//WORKS COMPLETELY
		
//		double[] arrayVal = new double[3];
//		System.out.println(arrayVal[0]);
//		
//		
//		double[][] arrayVal2 = new double[3][3];
//		System.out.println(arrayVal2[0][1]);
		
		//imamo dvije funkcije koje moramo upotrijebiti
		 /** 
		  * da pretvorimo matricu u array
		  * da taj array posaljemo 
		  * da taj array obradimo 
		  * array posaljemo nazad 
		  * da ga pretvorimo nazad u matricu
		  * da sve ih spojimo
		  * */
		
		//test arrayconvolution
		
		double[][] convoluted = convolutionFinalArray(matrix, matrix.length, matrix[0].length, kernel, 3, 3);
		printMatrix(convoluted);
		
		// ne valja algoritam, pogresni brojevi 
		double[][] normalConvoluted = convolutionFinal(matrix, matrix.length, matrix[0].length, kernel, 3, 3);
		System.out.println("___________________");
		printMatrix(normalConvoluted);
		
	}
	 
	 
	 
	 
	 
	 
	 
	  static void printMatrix(double[][] matrix) {
		  for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					
					System.out.print(matrix[i][j] + " ");
				}
				System.out.println();
			}
	}
	  
	  static void printArray(double[] array) {
		  for (int i = 0; i < array.length; i++) {
				
				System.out.print(array[i] + " ");
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
	  
	  /**  CONVOLUTION CONVOLUTION CONVOLUTION*/
	  
	  //method that calculates calue for each pixel using convolution formula
	    public static double singlePixelConvolutionArray(double[] input, //part of the picture we select
	                                                int x, int y, //to get current part of the picture to be convoluted
	                                                double[][] k, //actual kernel matrix
	                                                int kernelWidth, //kernel size used in loop
	                                                int kernelHeight, int height) {
	    	
	    	System.out.println();
	        double output = 0; //accumulator
	        for (int i = 0; i < kernelWidth; ++i) {
	            for (int j = 0; j < kernelHeight; ++j) {
	            	//x should be width or column number
	                output = output + (input[(x+i)*height+(j+y)] * k[i][j]); //we traverse through kernel and multiply              
	            }
	        }
	        return output;
	    }

	   // /we will try to convert it to single array conversion
	    public static double[][] convolutionFinalArray(double[][] input,
	                                           int width, int height,
	                                           double[][] kernel,
	                                           int kernelWidth,
	                                           int kernelHeight) {	
		
	        int smallWidth = width - kernelWidth + 1;//this is used so we do not come till edge of picture without sufficent pixels
	        int smallHeight = height - kernelHeight + 1; //so we convolute last kernel sized matrix of the picture
	               
	       
	        double[] outputArray = new double[width*height];
	        double[] inputArray = convertMatrixToArray(input);
	    //  matrix[i][j] = array [i*width + j]
	        System.out.println("INPUT MATRIX: width/height   "  +width+"/"+height);
	        System.out.println("INPUT MATRIX: width/height   "  +input.length+"/"+input[0].length);
	       // fill2DMatrix(output, height, width); //put in zeros
	        
	        //here we need to fill in each cell
	        for (int i = 0; i < smallWidth; ++i) { //filling in the values starting from beginning
	            for (int j = 0; j < smallHeight; ++j) {                
	                outputArray[(i+1)*height+(j+1)] =  singlePixelConvolutionArray(inputArray, i, j, kernel,
	                        kernelWidth, kernelHeight, height);
	      
	            }
	        }
	        
	        System.out.println("Matrix number of elements: " + (input.length*input[0].length));
	        System.out.println("Array number of elements: " + (outputArray.length));
	        return convertArrayToMatrix(outputArray, width, height);
	    }
	  
	  
	    
	    
	  //sada moramo implementirat convolution na nacin da je input i output array (1D)
	  
	  
	public static double[][] convolutionFinal(double[][] input, int width, int height, double[][] kernel,
			int kernelWidth, int kernelHeight) {
		

		int smallWidth = width - kernelWidth + 1;// this is used so we do not come till edge of picture without
													// sufficent pixels
		int smallHeight = height - kernelHeight + 1; // so we convolute last kernel sized matrix of the picture

		double[][] output = new double[width][height];// new matrix image accumulator

//fill2DMatrix(output, height, width); //put in zeros

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

		
		return output;
	}
	  
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
