package elmir.sut.project;

import java.util.concurrent.CountDownLatch;

//Parallel implementation


	
	class Parallel implements Runnable{
		
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
        int cores ;
        int slice ; //actually width
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
        	
        	
        }
        
        
        
        
	@Override
	public void run() {
		
		for (int t=0; t<cores; t++) {
			latch.countDown();
		//************************PART TO EACH THREAD****************//
        for (int i = 0; i < smallWidth; ++i) { //filling in the values starting from beginning
        	
        	
            for (int j = (t*slice); j < ((t+1)*slice+((cores+kernelWidth)%(t+1)+1))- kernelWidth + 1 ; ++j) {
                output[i+1][j+1] += singlePixelConvolution(input, i, j, kernel,
                        kernelWidth, kernelHeight); //calculating every single pixel and saving it
                
            }
        }
        //************************PART TO EACH THREAD****************//
        }
        

		
	}
}

