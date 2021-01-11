/* ShortestPaths.java
   CSC 226 - Fall 2018
   
   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
   java ShortestPaths
   
   To conveniently test the algorithm with a large input, create a text file
   containing one or more test graphs (in the format described below) and run
   the program with
   java ShortestPaths file.txt
   where file.txt is replaced by the name of the text file.
   
   The input consists of a series of graphs in the following format:
   
   <number of vertices>
   <adjacency matrix row 1>
   ...
   <adjacency matrix row n>
   
   Entry A[i][j] of the adjacency matrix gives the weight of the edge from 
   vertex i to vertex j (if A[i][j] is 0, then the edge does not exist).
   Note that since the graph is undirected, it is assumed that A[i][j]
   is always equal to A[j][i].
   
   An input file can contain an unlimited number of graphs; each will be processed separately.
   
   NOTE: For the purpose of marking, we consider the runtime (time complexity)
         of your implementation to be based only on the work done starting from
	 the ShortestPaths() method. That is, do not not be concerned with the fact that
	 the current main method reads in a file that encodes graphs via an
	 adjacency matrix (which takes time O(n^2) for a graph of n vertices).
   
   
   (originally from B. Bird - 08/02/2014)
   (revised by N. Mehta - 10/24/2018)
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Iterator;
import edu.princeton.cs.algs4.*;
import java.util.*;


//Do not change the name of the ShortestPaths class
public class ShortestPaths{

    //TODO: Your code here
    public static int n; 
    public static int[] d;
    public static int[] p;
    
    
    
    static void ShortestPaths(int[][][] adj, int source){
	n = adj.length;
	d = new int[n];
	p = new int[n];
	IndexMinPQ<Integer> Q = new  IndexMinPQ<Integer>(n);
	
	
	EdgeWeightedDigraph G = new EdgeWeightedDigraph(n);
	for (int i = 0; i<n; i++) 
	{
		for (int j = 0; j<adj[i].length; j++) 
		{
			DirectedEdge e = new DirectedEdge(i, adj[i][j][0], adj[i][j][1]);
			G.addEdge(e);	} }
	
	
	for (int v = 0; v < n; v++) {
        d[v] = Integer.MAX_VALUE; 
        }
    d[source] = 0;
    
    Q.insert(source, d[source]);
    
    while(!Q.isEmpty()) {
    	int Top_of_pq = Q.delMin();
    	for(DirectedEdge e : G.adj(Top_of_pq)) {
    		int Original_vertex = e.from(); 
    		int Destination_vertex = e.to();
    		int Test_path = (int) (d[Original_vertex] + e.weight());
    		if(Test_path < d[Destination_vertex])//if we found a path less than the original path, make it the official path
    		{
    			
    			d[Destination_vertex] = Test_path;//now the test path is the official shortest path for the vertex
    			p[Destination_vertex] = Original_vertex;
    			
    			
    			if(!Q.contains(Destination_vertex))//insert the destination vertex to the queue if it is not in the queue
    			{
    				Q.insert(Destination_vertex, d[Destination_vertex]);
    			}else //decrease key otherwise
    			{
    				Q.decreaseKey(Destination_vertex, d[Destination_vertex]);
    			}
    		}
    	}
    }
    
 }
 
   
  
    
    
    static void PrintPaths(int source){
    	for(int i = 0; i < n; i++) {
    			System.out.print("The path from "+source+" to "+i+ " is: ");
    			System.out.print(source+" --> "+p[i]);
    			System.out.print(" and the total distance is : "+d[i]);
    			System.out.println();
    			
    			
    		
    	}
    	
    }
    
    
    /* main()
       Contains code to test the ShortestPaths function. You may modify the
       testing code if needed, but nothing in this function will be considered
       during marking, and the testing process used for marking will not
       execute any of the code below.
    */
    public static void main(String[] args) throws FileNotFoundException{
	Scanner s;
	if (args.length > 0){
	    //If a file argument was provided on the command line, read from the file
	    try{
		s = new Scanner(new File(args[0]));
	    } catch(java.io.FileNotFoundException e){
		System.out.printf("Unable to open %s\n",args[0]);
		return;
	    }
	    System.out.printf("Reading input values from %s.\n",args[0]);
	}
	else{
	    //Otherwise, read from standard input
	    s = new Scanner(System.in);
	    System.out.printf("Reading input values from stdin.\n");
	}
	
	int graphNum = 0;
	double totalTimeSeconds = 0;
	
	//Read graphs until EOF is encountered (or an error occurs)
	while(true){
	    graphNum++;
	    if(graphNum != 1 && !s.hasNextInt())
		break;
	    System.out.printf("Reading graph %d\n",graphNum);
	    int n = s.nextInt();
	    int[][][] adj = new int[n][][];
	    
	    int valuesRead = 0;
	    for (int i = 0; i < n && s.hasNextInt(); i++){
		LinkedList<int[]> edgeList = new LinkedList<int[]>(); 
		for (int j = 0; j < n && s.hasNextInt(); j++){
		    int weight = s.nextInt();
		    if(weight > 0) {
			edgeList.add(new int[]{j, weight});
		    }
		    valuesRead++;
		}
		adj[i] = new int[edgeList.size()][2];
		Iterator it = edgeList.iterator();
		for(int k = 0; k < edgeList.size(); k++) {
		    adj[i][k] = (int[]) it.next();
		}
	    }
	    if (valuesRead < n * n){
		System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
		break;
	    }
	    
	    // output the adjacency list representation of the graph
	    /*
	    for(int i = 0; i < n; i++) {
	    	System.out.print(i + ": ");
	    	for(int j = 0; j < adj[i].length; j++) {
	    	    System.out.print("(" + adj[i][j][0] + ", " + adj[i][j][1] + ") ");
	    	}
	    	System.out.print("\n");
	    }
	    */
	    long startTime = System.currentTimeMillis();
	    
	    ShortestPaths(adj, 0);
	    PrintPaths(0);
	    long endTime = System.currentTimeMillis();
	    totalTimeSeconds += (endTime-startTime)/1000.0;
	    
	    
	    //System.out.printf("Graph %d: Minimum weight of a 0-1 path is %d\n",graphNum,totalWeight);
	}
	graphNum--;
	System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
    }
}