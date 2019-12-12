package elmir.sut.project;

import java.io.IOException;

import elmir.sut.project.ui.EdgeDetectionUI;
import mpi.MPI;


public class RunEdgeDetection {

    public static void main(String[] args) throws IOException {
    	MPI.Init(args);
        new EdgeDetectionUI();
    }
}
