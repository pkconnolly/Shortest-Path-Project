import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;

/**
 * An undirected, unweighted, simple graph.  The vertex set is static; edges
 * may be added but not removed.
 *
 * @param K the type of the vertices
 * @author Jim Glenn
 * @version 0.1 2015-10-27
 */

public class WeightedGraph<K> implements Graph<K>
{
    /**
     * This graph's vertex set.
     */
    private Set<K> verts;

    /**
     * This graph's adjacency lists.
     */
    private Map<K, Map<K, Edge>> adjMaps;

    /**
     * Creates a graph with the given vertex set and no edges.
     *
     * @param v a collection of vertices
     */
    public WeightedGraph(Collection<K> v)
    {
	    // make ourselves a private copy of the vertex set
        verts = new HashSet<K>(v);

	    // set up empty adjacency lists for each vertex
        adjMaps = new HashMap<K, Map<K, Edge>>();
        for (K src : verts)
        {
          Map<K, Edge> adjMap = new HashMap<K, Edge>();
          adjMaps.put(src, adjMap);
        }
    }

    public Map<K, K> dijkstras(K s){
        Map<K, K> pred = new HashMap<K, K>();
        for(K v : verts){
            pred.put(v, null);
        }

        Map<K, Integer> d = new HashMap<K, Integer>();
        PriorityQueue<K, Integer> q = new PriorityQueue<K, Integer>();

        for(K v : verts){
            d.put(v, 9999999);
            q.addItem(v, 9999999);
        }
        
        q.changePriority(s, 0);
        d.put(s, 0);

        while(q.getSize()>0){
            K u = q.removeItem();
            Iterable<K> n = neighbors(u);
            for(K v : n){
                int i = d.get(u)+(getWeight(u, v));
                if(q.containsKey(v) && i < d.get(v)){
                    d.put(v, i);
                    pred.put(v, u);
                    q.changePriority(v, d.get(v));
                }
            }
        }
        return pred;

    }

    public void addVertex(K v){
        verts.add(v);
        adjMaps.put(v, new HashMap<K, Edge>());
    }

    public void removeVertex(K v){
        removeEdges(v);
        verts.remove(v);
        adjMaps.remove(v);
    }

    public void removeEdges(K v){
        Iterable<K> neighbors = neighbors(v);
        for(K n : neighbors){
            removeEdge(n, v);
        }
    }

    public void removeEdge(K v, K u){
        adjMaps.get(v).remove(u);
    }

    /**
     * Adds the given edge to this graph if it does not already exist.
     *
     * @param u a vertex in this graph
     * @param v a vertex in this graph
     */
    public void addEdge(K u, K v, int weight)
    {
       if (u.equals(v))
       {
          throw new IllegalArgumentException("adding self loop");
      }

	// get u's adjacency list
      Map<K, Edge> adj = adjMaps.get(u);

	// check for edge already being there
      if (!adj.containsKey(v))
      {
		// edge is not already there -- add to both adjacency lists
        Edge e = new Edge(weight);
          adj.put(v, e);
          adjMaps.get(v).put(u, e);
      }
  }

  public void addEdge(K u, K v, int weight, ArrayList<String> path){
        if (u.equals(v))
        {
            throw new IllegalArgumentException("adding self loop");
        }

        // get u's adjacency list
        Map<K, Edge> adj = adjMaps.get(u);

        // check for edge already being there
        if (!adj.containsKey(v))
        {
            // edge is not already there -- add to both adjacency lists
            Edge e = new Edge(weight, path);
            adj.put(v, e);
            adjMaps.get(v).put(u, e);
        }
    }

    public void printEdges(){
        System.out.println("size:" + verts.size());
        for(K v : verts){
            System.out.print(m + " HIT2");
            for(Set<K> m : adjMaps.get(v).keySet()){
                System.out.print(m.getKey());
                System.out.println(m.getValue().getWeight());
            }
        }
    }

    public Edge getEdge(K u, K v){
        return adjMaps.get(u).get(v);
    }

    /**
     * Determines if the given edge is present in this graph.
     *
     * @param u a vertex in this graph
     * @param v a vertex in this graph
     * @return true if and only if the edge (u, v) is in this graph
     */
    public boolean hasEdge(K u, K v)
    {
       return adjMaps.get(u).containsKey(v);
   }

    public int getWeight(K u, K v)
    {
        if(hasEdge(u, v)){
            //System.out.println(u + "->" + v + " w:" + adjMaps.get(u).get(v).getWeight());
            return(adjMaps.get(u).get(v).getWeight());
        }else{
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Returns an iterator over the vertices in this graph.
     *
     * @return an iterator over the vertices in this graph
     */
    public Iterator<K> iterator()
    {
        return (new HashSet<K>(verts)).iterator();
    }

    /**
     * Returns an iterator over the neighbors of the vertices in this graph.
     *
     * @param v a vertex in this graph
     * @return an iterator over the vertices in this graph
     */
    public Iterable<K> neighbors(K v)
    {
        
        return adjMaps.get(v).keySet();
    }

    /**
     * Returns a printable represenation of this graph.
     *
     * @return a printable representation of this graph
     */
    public String toString()
    {
        String ret = "";
        String s = adjMaps.toString();
        String[] parts = s.split("},");
        for(int i=0; i< parts.length; i++){
            ret += parts[i] + "}\n";
        }
        return ret;
   }
}