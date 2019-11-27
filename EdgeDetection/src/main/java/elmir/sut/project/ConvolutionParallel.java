package elmir.sut.project;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Handles the convolution operation with a filter
 */

//Here we need to implement parallel version of convolution
public class ConvolutionParallel  extends Thread{
	
	private int row;
	   private int column;
	   private AtomicIntegerArray matrixA;
	   private AtomicIntegerArray[] matrixB;
	   private AtomicIntegerArray[] product;

	   public  ConvolutionParallel(final int row,
			   					   final int column,
			   					   final AtomicIntegerArray matrixA, 
			   					   final AtomicIntegerArray[] matrixB,
			   					   final AtomicIntegerArray[] product) {
	      this.row = row;
	      this.column = column;
	      this.matrixA = matrixA;
	      this.matrixB = matrixB;
	      this.product = product;
	   }

	   public void run() {
	      int value = 0;
	      for (int i = 0; i < matrixA.length(); i++) {
	         value = value + (matrixA.get(i) * matrixB[i].get(column));
	      }
	      product[row].set(column, value);
	   }
	
	
}
    