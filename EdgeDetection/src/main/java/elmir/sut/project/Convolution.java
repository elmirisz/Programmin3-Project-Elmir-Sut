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
     *
     * @param input        The 2D double array representing the image.
     * @param x            The x coordinate for the position of the convolution.
     * @param y            The y coordinate for the position of the convolution.
     * @param k            The 2D array representing the kernel.
     * @param kernelWidth  The width of the kernel.
     * @param kernelHeight The height of the kernel.
     * @return The new pixel value after the convolution.
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
     * Takes a 2D array of grey-levels and a kernel and applies the convolution
     * over the area of the image specified by width and height.
     *
     * @param input        the 2D double array representing the image
     * @param width        the width of the image
     * @param height       the height of the image
     * @param kernel       the 2D array representing the kernel
     * @param kernelWidth  the width of the kernel
     * @param kernelHeight the height of the kernel
     * @return the 2D array representing the new image
     */
    public static double[][] convolution2D(double[][] input,
                                           int width, int height,
                                           double[][] kernel,
                                           int kernelWidth,
                                           int kernelHeight) {
    	
        int smallWidth = width - kernelWidth + 1;
        int smallHeight = height - kernelHeight + 1;
        double[][] output = new double[smallWidth][smallHeight];//now result is 2d matrix
        
        for (int i = 0; i < smallWidth; ++i) { //instantiating values in output matrix
            for (int j = 0; j < smallHeight; ++j) {
                output[i][j] = 0;
            }
        }
        for (int i = 0; i < smallWidth; ++i) { //filling in the values
            for (int j = 0; j < smallHeight; ++j) {
                output[i][j] = singlePixelConvolution(input, i, j, kernel,
                        kernelWidth, kernelHeight); //calculating every single pixel and saving it
                
            }
        }
        return output;
    }

    
    /**
     * Takes a 2D array of grey-levels and a kernel, applies the convolution
     * over the area of the image specified by width and height and returns
     * a part of the final image.
     *
     * @param input        the 2D double array representing the image
     * @param width        the width of the image
     * @param height       the height of the image
     * @param kernel       the 2D array representing the kernel
     * @param kernelWidth  the width of the kernel
     * @param kernelHeight the height of the kernel
     * @return the 2D array representing the new image
     */
    public static double[][] convolution2DPadded(double[][] input,
                                                 int width, int height,
                                                 double[][] kernel,
                                                 int kernelWidth,
                                                 int kernelHeight) {
        int smallWidth = width - kernelWidth + 1;
        int smallHeight = height - kernelHeight + 1;
        int top = kernelHeight / 2;
        int left = kernelWidth / 2;

        double[][] small = convolution2D(input, width, height,
                kernel, kernelWidth, kernelHeight);
        double large[][] = new double[width][height];
        for (int j = 0; j < height; ++j) {
            for (int i = 0; i < width; ++i) {
                large[i][j] = 0;
            }
        }
        for (int j = 0; j < smallHeight; ++j) {
            for (int i = 0; i < smallWidth; ++i) {
                large[i + left][j + top] = small[i][j];
            }
        }
        return large;
    }


    /**
     * Applies the convolution2DPadded  algorithm to the input array as many as
     * iterations.
     *
     * @param input        the 2D double array representing the image
     * @param width        the width of the image
     * @param height       the height of the image
     * @param kernel       the 2D array representing the kernel
     * @param kernelWidth  the width of the kernel
     * @param kernelHeight the height of the kernel
     * @param iterations   the number of iterations to apply the convolution
     * @return the 2D array representing the new image
     */
    public double[][] convolutionType2(double[][] input,
                                       int width, int height,
                                       double[][] kernel,
                                       int kernelWidth, int kernelHeight,
                                       int iterations) {
        double[][] newInput = input.clone();
        double[][] output = input.clone();

        for (int i = 0; i < iterations; ++i) {
            output = convolution2DPadded(newInput, width, height,
                    kernel, kernelWidth, kernelHeight);
            newInput = output.clone();
        }
        return output;
    }
}
    