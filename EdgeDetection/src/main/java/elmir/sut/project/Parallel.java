package elmir.sut.project;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//Parallel implementation


	
	class Parallel  {
		
		Object a;
		CountDownLatch latch;
		double[][] input;
        int width;
        int height;
        double[][] kernel;
        int kernelWidth;
        int kernelHeight;
        int smallWidth = width - kernelWidth + 1;//this is used so we do not come till edge of picture without sufficent pixels
        int smallHeight = height - kernelHeight + 1; 
        double[][] output;
       int cores =Runtime.getRuntime().availableProcessors();
        //int cores=1;
        int slice ; //actually width
        
        int checker = 0;
        
        ExecutorService executor = Executors.newFixedThreadPool(cores);
        
        
      //NEED TO FIX DUPLICATIONS!!!
        
        double[][] returnOutput() {
        	
        	return output;
        }
        
        public  double singlePixelConvolution(double[][] input, // part of the picture we select
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
        
        Parallel(double[][] input,
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
        	this.latch=latch;
        	this.cores=cores;
        	output = new double[width][height];
        	
        	for (int i = 0; i < width; ++i) {  //for each cell we will set value to be zero 0
                for (int j = 0; j < height; ++j) {
                    output[i][j] = 0;
                }
            }
        	
    		smallWidth = width - kernelWidth + 1;
    		smallHeight = height - kernelHeight + 1;
    		
    		slice = height/cores;
            slice=slice-3;
           // System.out.println("Output : " + output[1][1]);
            
            for (int t=0; t<cores; t++) {
    			latch.countDown();
    			//System.out.println("Latch : " + latch);
    			
    			
    			executor.submit(new Multiply(t));
    			
    			
    			System.out.println("Thread : " + Thread.currentThread().getId());
    			
    			 
    			
            //************************PART TO EACH THREAD****************//
            }
            
            executor.shutdown(); 
	        
	        
            try {
				executor.awaitTermination(1, TimeUnit.DAYS);
				System.out.println(" FINISHED THREAD IN RUNNABLE" );
				checker+=100;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println("Checker : " + checker);
        }
        
        
        
        
	
	public void start() {
		
		
        

		
	}
	
	
	public class Multiply implements Runnable {
		
		int t;
//		int smallWidth;
//		int slice;
		
		
		//setter
		//we need to know loop integer at the moment(t), we need smallwidth, slice value
		Multiply(int t){
			this.t=t;
			
			
		}
		
		@Override
		public void run() {
			
			for (int i = 0; i < smallWidth; ++i) { //filling in the values starting from beginning
	        	
	        	//when t=3 it does not reach end
	            for (int j = (t*slice); j < ((t+1)*(slice)+2*t); ++j) {
	                output[i+1][j+1] = singlePixelConvolution(input, i, j, kernel,
	                        kernelWidth, kernelHeight); //calculating every single pixel and saving it
	                
	            }
	        }
			
		}
		
		
	}
	
	}


