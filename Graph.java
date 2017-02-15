import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * An unweighted, simple graph.
 *
 * @param K the type of the vertices
 * @author Jim Glenn
 * @version 0.1 2015-10-27
 */

public interface Graph<K> extends Iterable<K>
{
    /**
     * Adds the given edge to this graph if it does not already exist.
     *
     * @param u a vertex in this graph
     * @param v a vertex in this graph
     */
    public void addEdge(K u, K v, int weight);

    /**
     * Determines if the given edge is present in this graph.
     *
     * @param u a vertex in this graph
     * @param v a vertex in this graph
     * @return true if and only if the edge (u, v) is in this graph
     */
    public boolean hasEdge(K u, K v);

    /**
     * Returns an iterator over the vertices in this graph.
     *
     * @return an iterator over the vertices in this graph
     */
    public Iterator<K> iterator();

    /**
     * Returns an iterator over the neighbors of the vertices in this graph.
     *
     * @param v a vertex in this graph
     * @return an iterator over the vertices in this graph
     */
    public Iterable<K> neighbors(K v);
}