
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LP5Driver {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;
        boolean VERBOSE = true;

//        if (args.length > 0) {
            File inputFile = new File("test/simple.txt");
            in = new Scanner(inputFile);
//        } else { 
//            in = new Scanner(System.in);
//        }
//		if (args.length > 1) {
//		    VERBOSE = true;
//		}
	    Graph g = Graph.readGraph(in, false);   // read undirected graph from stream "in"
		int result = Matching.matching(g);
		if(result == -1)
			return;
		System.out.println(result);
		if (VERBOSE) {
	//	    Output the edges of M.
		}
		if(in!=null)
			in.close();
    }
}
