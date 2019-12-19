package elmir.sut.project;

import java.io.IOException;

import javax.swing.JFrame;

import elmir.sut.project.ui.EdgeDetectionUI;
import mpi.Comm;
import mpi.MPI;


public class RunEdgeDetection {
	
	
	public static int rootProcessorRank = 0;
	public static int currentProcessorRank;
	public static int numberOfProcessors;
	
	public static Comm comunicator;
	static int counter=0;
	static String[] argsMain;
	
	

    public static void main(String[] args) throws IOException {
    	/**Initialize the MPI execution environment */
    	argsMain=args;
    	MPI.Init(argsMain);
//    	System.out.println(args[0]);
    	/** 
    	 * When a program is ran with MPI
    	 * all the processes are grouped in
    	 * what we call a communicator.
    	 *   
    	 *   */
//    	String[] nesto = new String[3];
//    	nesto[0]="";
//    	nesto[1]="";
//    	nesto[2]="";
//    	String[] prazan=null;
//    	
//    	Main.main(nesto);
    	for(int i = 0; i<args.length;i++) {
    		System.out.print(args[i]+"___ ");
    	}
    	System.out.println();
    	//box grouping processes together
    	
		comunicator = MPI.COMM_WORLD;
		
		//Every process is connected and can communicate inside this communicator.
		currentProcessorRank = comunicator.Rank();
		numberOfProcessors = comunicator.Size();
		//System.out.println("NUM OF PROC"+numberOfProcessors);

		/**  The number in a communicator does not change once it is created. 
		 * That number is called the size of the communicator.
		 *  At the same time, each process inside a communicator has a unique number to identify it.
		 *   This number is called the rank of the process.
		 *    In the previous example, the size of MPI_COMM_WORLD is 5.
		 *     The rank of each process is the number inside each circle. 
		 *     The rank of a process always ranges from 0 to size−1. */
		
		
		
		//System.out.println("RUN EDGE DETECTION: "+currentProcessorRank +" == "+ rootProcessorRank);
		if (currentProcessorRank == rootProcessorRank) {
			EdgeDetectionUI ui = new EdgeDetectionUI();
			//ui.mainFrame.setVisible(true);
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
//			ui.mainFrame.setVisible(true);
//			ui.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			System.out.println(currentProcessorRank +" == "+ rootProcessorRank);
			
		}
		else {
			//System.out.println("RUN EDGE DETECTION NON ROOT : "+currentProcessorRank +" == "+ rootProcessorRank);
			//this part will be called by changing current processor rank
			System.out.println("HELLO COUNTER "+ counter);
			counter++;
			DistributedMPI.nonRootAction();
		}
		
		MPI.Finalize();
		//System.out.println("COOOOOONGRAAAAAAATSSSSS" + counter);
		return;
    	
    	
    	
    	
    	
       
       
    }
}
