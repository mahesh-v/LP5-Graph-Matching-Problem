
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LP5Driver {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;
        boolean VERBOSE = false;

//        if (args.length > 0) {
            File inputFile = new File("lp5-data/bip4.txt");
            in = new Scanner(inputFile);
//        } else { 
//            in = new Scanner(System.in);
//        }
//		if (args.length > 1) {
//		    VERBOSE = true;
//		}
	    Graph g = Graph.readGraph(in, false);   // read undirected graph from stream "in"
	    Timer t = new Timer();
		int result = BipartiteMatching.matching(g);
		t.end();
		if(result == -1)
			return;
		System.out.println(result);
		if (VERBOSE) {
			for (Edge e : BipartiteMatching.matchingEdges) {
				System.out.println(e.From+" "+e.To+" "+e.Weight);
			}
	//	    Output the edges of M.
		}
		System.out.println(t);
		if(in!=null)
			in.close();
    }
}
