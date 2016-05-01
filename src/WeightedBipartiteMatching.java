import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class WeightedBipartiteMatching {
	private static int msize = 0;
	public static ArrayList<Edge> matchingEdges = new ArrayList<Edge>();

	public static int matching(Graph g) {
		msize = 0;
		labelVerticesAsInnerOrOuter(g);
		assignLabelWeightsToNodes(g);
		return msize;
	}
	
	private static void assignLabelWeightsToNodes(Graph g) {
		for (Vertex v : g) {
			if(v.type == 'o'){
				v.L = 0;
				for (Edge e : v.Adj) {
					if(e.Weight > v.L)
						v.L = e.Weight;
				}
			}
			else{
				v.L = 0;
			}
		}
	}

	private static void labelVerticesAsInnerOrOuter(Graph g) {
		Vertex first = g.verts.get(1);
		first.type = 'o';
		Queue<Vertex> queue = new LinkedList<Vertex>();
		queue.add(first);
		while(!queue.isEmpty()){
			Vertex v = queue.poll();
			for (Edge e : v.Adj) {
				Vertex u = e.otherEnd(v);
				if(u.type != 'u')//already set. Not unknown.
					continue;
				if(v.type == 'i')
					u.type = 'o';
				else if(v.type == 'o')
					u.type = 'i';
				queue.add(u);
			}
		}
	}
}
