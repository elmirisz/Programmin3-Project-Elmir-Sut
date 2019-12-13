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
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j]= i+j;
			}
		}
		
		printMatrix(matrix);
		
		double[] array = convertMatrixToArray(matrix);
		System.out.println("Matrix value: "+ matrix[3][5]);
		
		printArray(array);
		System.out.println();
		System.out.println("Array value: "+ array[3*matrix[0].length+5]);
		
		//CONCLUSION
		//  matrix[i][j] = array [i*width + j]
		//width = broj kolona
		
		
		double[][] convertedMatrix = convertArrayToMatrix(array,matrix.length,matrix[0].length);
		System.out.println("convertedMatrix value: "+ convertedMatrix[5][5]);
		System.out.println("Matrix value: "+ matrix[5][5]);
		
		printMatrix(convertedMatrix);
		
//		double[] arrayVal = new double[3];
//		System.out.println(arrayVal[0]);
//		
//		
//		double[][] arrayVal2 = new double[3][3];
//		System.out.println(arrayVal2[0][1]);
		
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
	  
	  //NO NEED
	  
//	  static double[][] convertMatrixToArraySliced (double[][]matrix, int slices) {
//		  double[][] array = new double[slices][(matrix.length*matrix[0].length)/3];
//		  int m = matrix[0].length;
//		  for (int i = 0; i < matrix.length; i++) {
//				for (int j = 0; j < matrix[0].length; j++) {
//					array[ i*m + j ] =	matrix[i][j];
//					
//				}
//				
//			}
//		  
//		  
//		  
//		  
//		  
//		return array;
//		  
//	  }
	  
	
}
