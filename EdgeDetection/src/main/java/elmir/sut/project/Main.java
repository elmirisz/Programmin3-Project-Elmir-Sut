package elmir.sut.project;

import mpi.Comm;
import mpi.MPI;

public class Main {
	public static int rootProcessorRank = 0;
	public static int currentProcessorRank;
	public static int numberOfProcessors;
	
	public static Comm comunicator;

	public static void main(String[] args) {
		
		if  (args.length>0) {
//			MPI.Init(args);
//			comunicator = MPI.COMM_WORLD;
//			currentProcessorRank = comunicator.Rank();
//			numberOfProcessors = comunicator.Size();
			System.out.println("I am here to print stuff as I want :D "+ args.length);

		}
		
		System.out.println("I am here to print stuff as I want "+ args.length);

		
	}

}
