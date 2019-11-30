package elmir.sut.project;

/**
 * Handles the convolution operation with a filter
 */
public class Convolution {
	
/** 
 * Notice : I will use only grey scale photo, colors will be converted to white intensity number
 * 
 */

    /**
     * Takes an image (grey-levels) and a kernel and a position,
     * applies the convolution at that position and returns the
     * new pixel value.
     */
	
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

    /** 
     * input[x][y]  position of original pixel 
     * input[x + i][y + j] taking value of neighboring  pixels
     *  * k[i][j] multyplying with kernel position value
     * 
     * return output returns sum of all these multiplications 
     * 
     * */
    
    

   
    public static double[][] convolutionFinal(double[][] input,
                                           int width, int height,
                                           double[][] kernel,
                                           int kernelWidth,
                                           int kernelHeight) {
    	
        int smallWidth = width - kernelWidth + 1;//this is used so we do not come till edge of picture without sufficent pixels
        int smallHeight = height - kernelHeight + 1; //so we convolute last kernel sized matrix of the picture
        
        double[][] output = new double[width][height];//new matrix image accumulator
        
        for (int i = 0; i < width; ++i) {  //for each cell we will set value to be zero 0
            for (int j = 0; j < height; ++j) {
                output[i][j] = 0;
            }
        }
        //here we need to fill in each cell
        for (int i = 0; i < smallWidth; ++i) { //filling in the values starting from beginning
            for (int j = 0; j < smallHeight; ++j) {
                output[i+1][j+1] = singlePixelConvolution(input, i, j, kernel,
                        kernelWidth, kernelHeight); //calculating every single pixel and saving it
                
            }
        }
        System.out.println("Size of output: " + output.length +  "; "+output[0].length);
        
        return output;
    }

    

   
}