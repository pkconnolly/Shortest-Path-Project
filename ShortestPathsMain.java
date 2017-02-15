import java.util.*;

public class ShortestPathsMain
{
    public static void main(String[] args)
    {
	Scanner in = new Scanner(System.in);

	// read the first line with the dimensions of the grid
	int width = in.nextInt();
	int height = in.nextInt();
	int n = in.nextInt();
	List<String> cornerVerts = new ArrayList<String>();

	// THIS WILL MAKE A ARRAY (okay, a list of lists since Java won't allow
	// arrays of generics) OF GRAPHS FOR THE INDIVIDUAL CELLS --
	// g.get(r).get(c) IS THE GRAPH FOR THE CELL IN ROW r COLUMN c

	// make an empty graph for each cell
	List<List<WeightedGraph<String>>> g = new ArrayList<List<WeightedGraph<String>>>();
	for (int r = 0; r < height; r++)
	    {
		List<WeightedGraph<String>> row = new ArrayList<WeightedGraph<String>>();
		for (int c = 0; c < width; c++)
		    {
			// make the list of vertices in this cell starting
			// with the corners...
			List<String> verts = new ArrayList<String>();
			verts.add("g" + r + "." + c); // upper left
			verts.add("g" + (r + 1) + "." + c); // lower left
			verts.add("g" + r + "." + (c + 1)); // upper right
			verts.add("g" + (r + 1) + "." + (c + 1)); // lower right

			//...then the interior vertices
			for (int k = 0; k < n; k++)
			    {
				verts.add("v" + r + "." + c + "." + k);
			    }

			// add that graph!
			row.add(new WeightedGraph<String>(verts));
		    }
		g.add(row);
	    }

	// loop over edges to add
	String from;
	while (!(from = in.next()).equals("queries"))
	    {
		String to = in.next();
		int w = in.nextInt();
		
		// the to vertex is always in the interior of the cell
		assert to.charAt(0) == 'v';

		// figure out from the to vertex which cell we're in
		StringTokenizer tok = new StringTokenizer(to.substring(1), ".");	
		int r = Integer.parseInt(tok.nextToken());
		int c = Integer.parseInt(tok.nextToken());
		
		// add the edge to the correct cell
		g.get(r).get(c).addEdge(from, to, w);
	    }

	// MAKE YOUR CORNER GRAPH HERE (might want the ability to label edges with paths
	// they represent)
	for (int r = 0; r < height; r++){
		for (int c = 0; c < width; c++){
			cornerVerts.add("g" + r + "." + c); // upper left
			cornerVerts.add("g" + (r + 1) + "." + c); // lower left
			cornerVerts.add("g" + r + "." + (c + 1)); // upper right
			cornerVerts.add("g" + (r + 1) + "." + (c + 1)); // lower right
		}
	}

	WeightedGraph cornerGraph = new WeightedGraph<String>(cornerVerts);

	//cornerGraph.printEdges();

	for (int r = 0; r < height; r++){
		for (int c = 0; c < width; c++){

			WeightedGraph cell = g.get(r).get(c);
			Map<String, String> pred = new HashMap<String, String>();
			
			String[] corners = {"g" + r + "." + c,
					"g" + (r + 1) + "." + c,
					"g" + r + "." + (c + 1),
					"g" + (r + 1) + "." + (c + 1)};

			for(int i=0; i<4; i++){
				String s = corners[i];
				pred = cell.dijkstras(s);
				for(int j=0; j<4; j++){
					String e = corners[j];
					if(s != e) update(cell, cornerGraph, s, e, pred, true);
				}
			}	
		}
	}

	// process the queries
	while (in.hasNext())
	    {
		from = in.next();
		String to = in.next();

		// determine what cells we're in
		StringTokenizer tok = new StringTokenizer(from.substring(1), ".");
		int fromR = Integer.parseInt(tok.nextToken());
		int fromC = Integer.parseInt(tok.nextToken());

		tok = new StringTokenizer(to.substring(1), ".");
		int toR = Integer.parseInt(tok.nextToken());
		int toC = Integer.parseInt(tok.nextToken());
		
		String[] fromCorners = {"g" + fromR + "." + fromC,
					"g" + (fromR + 1) + "." + fromC,
					"g" + fromR + "." + (fromC + 1),
					"g" + (fromR + 1) + "." + (fromC + 1)};
		String[] toCorners = {"g" + toR + "." + toC,
				      "g" + (toR + 1) + "." + toC,
				      "g" + toR + "." + (toC + 1),
				      "g" + (toR + 1) + "." + (toC + 1)};

		// COMPUTE THE SHORTEST PATHS FROM from AND to TO THEIR CORNERS AND PERHAPS
		// UPDATE THE CORNER GRAPH ACCORDINGLY (existing Graph needs to know all
		// vertices when it is created; need some way to deal with that or an addVertex
		// method)

		cornerGraph.addVertex(from);
		cornerGraph.addVertex(to);

		WeightedGraph fromCell = g.get(fromR).get(fromC);
		WeightedGraph toCell = g.get(toR).get(toC);
		Map<String, String> pred = new HashMap<String, String>();
		pred = fromCell.dijkstras(from);  

		for(int i=0; i<4; i++){
			update(fromCell, cornerGraph, from, fromCorners[i], pred, false);
		}     

		pred = toCell.dijkstras(to);
		for(int i=0; i<4; i++){
			update(toCell, cornerGraph, to, toCorners[i], pred, true);
		}


		// RUN DIJKSTRA'S ON THE CORNER GRAPH

		pred = cornerGraph.dijkstras(from);


		// RECONSTRUCT COMPLETE PATH FROM THE PATH OF CORNERS

		ArrayList<String> rpath = new ArrayList<String>();
		ArrayList<String> path = new ArrayList<String>();
		int weight = 0;
		String v = pred.get(to);
		//rpath.add(to);
		String prev = to;
		while(v != null){
			weight += cornerGraph.getWeight(v, prev);
			ArrayList<String> p = cornerGraph.getEdge(v, prev).getPath();
			//System.out.print (prev + " to " + v + "(");
			for(String s : p){
				//System.out.print(s + "->");
			}
			//System.out.println(")");
			if(p == null){
				rpath.add(v);
			}else{
				for(String vert : p){
					rpath.add(vert);
				}
			}

			prev = v;
			v = pred.get(v);
		}
		for (int i = rpath.size()-1; i >=0; i--) {
			path.add(rpath.get(i));
		}
		System.out.print(weight + " ");
		System.out.println(path);


		// UNDO THE UPDATES (maybe add removeVertex and/or removeEdge to WeightedGraph)

		cornerGraph.removeVertex(from);
		cornerGraph.removeVertex(to);

	    }
    }

	private static void update(WeightedGraph cell, WeightedGraph cornerGraph, String s, String e, Map<String, String> pred, boolean r){
		ArrayList<String> rpath = new ArrayList<String>();
		ArrayList<String> path = new ArrayList<String>();
		int weight = 0;
		String v = pred.get(e);
		rpath.add(e);
		String prev = e;
		while(v != null){
			weight += cell.getWeight(v, prev);
			rpath.add(v);
			prev = v;
			v = pred.get(v);

		}
		//reverse path
		if(r){
			for (int i = rpath.size()-1; i >=1; i--) {
				path.add(rpath.get(i));
			}
		}else{
			path = rpath;
		}

		if(cornerGraph.hasEdge(s, e) || cornerGraph.getWeight(s, e) < weight){
			return;

		}
		cornerGraph.addEdge(s, e, weight, path);
	}
}