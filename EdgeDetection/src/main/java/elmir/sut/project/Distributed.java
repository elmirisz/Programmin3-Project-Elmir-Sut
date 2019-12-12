package elmir.sut.project;

import java.util.concurrent.CountDownLatch;

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
    int width;
    static int height;
    double[][] kernel;
    int kernelWidth;
    int kernelHeight;
    int smallWidth = width - kernelWidth + 1;//this is used so we do not come till edge of picture without sufficent pixels
    int smallHeight = height - kernelHeight + 1; 
    double[][] output;
    
    Distributed(double[][] input,
            int width, int height,
            double[][] kernel,
            int kernelWidth,
            int kernelHeight, CountDownLatch latch, int cores){
    	
		this.input=input;
		this.width=width;
		this.height=height;
		this.kernel=kernel;
		this.kernelWidth=kernelWidth;
		this.kernelHeight=kernelHeight;
		
		output = new double[width][height];
}
    
	
	
	public static double[][] distribute(double[][] A) throws InterruptedException {
		currentProcessorRank = EdgeDetection.currentProcessorRank;   
		 System.out.println("I need current rank:" + currentProcessorRank);
       int initPartition = height/(numberOfProcessors-1);
       System.out.println("I need length size in root:" + height);
		
		
		return null;
	}
	
	

}
