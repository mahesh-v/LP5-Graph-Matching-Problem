import java.util.LinkedList;


public class Matching {
//	private static int msize = 0;

	public static int matching(Graph g) {
		if(!labelVerticesAsInnerOrOuter(g)){
			System.out.println("G is not bipartite");
			return -1;
		}
		initialGreedyMatch(g);
		for (Vertex v : g) {
			System.out.println(v.name +" - "+v.level + " - "+v.mate);
		}
		return 0;
	}

	private static void initialGreedyMatch(Graph g) {
		for (Vertex v : g) {
			for (Edge e : v.Adj) {
				Vertex u = e.otherEnd(v);
				if(u.mate == null && v.mate == null){
					u.mate = v;
					v.mate = u;
					break;
				}
			}
		}
		
	}

	private static boolean labelVerticesAsInnerOrOuter(Graph g) {
		Vertex first = g.verts.get(1);
		first.level = 'o';
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		queue.add(first);
		while(!queue.isEmpty()){
			Vertex v = queue.removeFirst();
			for (Edge e : v.Adj) {
				Vertex u = e.otherEnd(v);
				if(u.level == 'u') {//unseen
					if(v.level == 'i')
						u.level = 'o';
					else if(v.level == 'o')
						u.level = 'i';
					queue.add(u);
				}
				else if((v.level == 'o' && u.level == 'o') || (v.level == 'i' && u.level == 'i'))//not bipartite
					return false;
			}
		}
		return true;
	}

}
