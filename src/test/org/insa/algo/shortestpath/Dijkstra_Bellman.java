package org.insa.algo.shortestpath;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathSolution;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.RoadInformation;
import org.insa.graph.RoadInformation.RoadType;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;


public class Dijkstra_Bellman {
	  // Small graph use for tests
    private static Graph graph;

    // List of nodes
    private static Node[] nodes;

    private String mapsLocation = "/home/louis/Documents/INSA/map-BE_Graphes";
    
    @BeforeClass
    public static void initAll() throws IOException {

    	RoadInformation roadInformation = new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null);
    	
        // Create nodes
        nodes = new Node[6];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = new Node(i, null);
        }

        Node.linkNodes(nodes[0], nodes[1], 7, roadInformation, new ArrayList<>());
        Node.linkNodes(nodes[0], nodes[2], 8, roadInformation, new ArrayList<>());
        Node.linkNodes(nodes[1], nodes[3], 4, roadInformation, new ArrayList<>());
        Node.linkNodes(nodes[1], nodes[4], 1, roadInformation, new ArrayList<>());
        Node.linkNodes(nodes[1], nodes[5], 5, roadInformation, new ArrayList<>());
        Node.linkNodes(nodes[2], nodes[0], 7, roadInformation, new ArrayList<>());
        Node.linkNodes(nodes[2], nodes[1], 2, roadInformation, new ArrayList<>());
        Node.linkNodes(nodes[2], nodes[5], 2, roadInformation, new ArrayList<>());
        Node.linkNodes(nodes[4], nodes[2], 2, roadInformation, new ArrayList<>());
        Node.linkNodes(nodes[4], nodes[3], 2, roadInformation, new ArrayList<>());
        Node.linkNodes(nodes[4], nodes[5], 3, roadInformation, new ArrayList<>());
        Node.linkNodes(nodes[5], nodes[4], 3, roadInformation, new ArrayList<>());

        graph = new Graph("ID", "", Arrays.asList(nodes), null);

    }
    
    @Test
    public void testDoRun() {
    	for (Node origine : graph.getNodes()) {
    		System.out.print("x" + (origine.getId() + 1) + " : ");
    		for (Node destination : graph.getNodes()) {
    			//si l'origine et la destination sont les mêmes sommets 
    			if (origine.getId() == destination.getId()) {
    				System.out.print(" - ");
    			}else {
    				ArcInspector arcInspector = new ArcInspectorFactory().getAllFilters().get(0);
    				ShortestPathData data = new ShortestPathData(graph,origine,destination,arcInspector);
    				
    				BellmanFordAlgorithm bellmanAlgo = new BellmanFordAlgorithm(data);
    				DijkstraAlgorithm dijkstraAlgo = new DijkstraAlgorithm(data);
    				
    				ShortestPathSolution solutionBellman = bellmanAlgo.doRun();
    				ShortestPathSolution solutionDijkstra = dijkstraAlgo.doRun();
    				
    				//si le path de la solution n'existe pas alors il n'y pas de solution
    				if (solutionDijkstra.getPath() == null) {
    					assertEquals(solutionBellman.getPath(),solutionDijkstra.getPath());
    					System.out.print(" infini ");
    				}else {
    					//on vérifie que les couts sont egaux
    					float costBellman = solutionBellman.getPath().getLength();
    					float costDijkstra = solutionDijkstra.getPath().getLength();
    					assertEquals(costBellman,costDijkstra,0);
    					
    					//recupere le père du sommet destination
    					List <Arc> arcs = solutionDijkstra.getPath().getArcs();
    					Node successor = arcs.get(arcs.size() - 1).getOrigin();
    					
    					System.out.print(costDijkstra + ",(x" + (successor.getId() + 1) + ") ");
    					
    				}
    			}
    		}
    		System.out.println();
    	}
    	
    }
    
    //procedure generale permettant de tester differents scenarios
	public void testScenario(String mapName, int typeEvaluation, int origine, int destination) throws Exception{
		//typeEvaluation : 0 = temps, 1 = distance
		
        // Create a graph reader.
        GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        // Read the graph.
        Graph graph = reader.read();
        
        if ((typeEvaluation != 0 && typeEvaluation != 1) || origine < 0 || origine >= graph.size() || destination < 0 || destination >= graph.size()) {
        	System.out.println("Arguments non valides");
        	throw new Exception();
        }else {
        	System.out.println("Origine : " + origine);
        	System.out.println("Destination : " + destination);
        	//si l'origine et la destination sont les mêmes sommets 
			if (origine == destination) {
				System.out.print("Origine = Destination, cout nul");
			}else {
	        	ArcInspector arcInspector;
	        	//0 = temps, 1 = distance
	        	if (typeEvaluation == 0) {
	        		arcInspector = new ArcInspectorFactory().getAllFilters().get(2);
	        	}else {
	        		arcInspector = new ArcInspectorFactory().getAllFilters().get(0);
	        	}
				ShortestPathData data = new ShortestPathData(graph,graph.get(origine),graph.get(destination),arcInspector);
				
				BellmanFordAlgorithm bellmanAlgo = new BellmanFordAlgorithm(data);
				DijkstraAlgorithm dijkstraAlgo = new DijkstraAlgorithm(data);
				
				ShortestPathSolution solutionBellman = bellmanAlgo.doRun();
				ShortestPathSolution solutionDijkstra = dijkstraAlgo.doRun();
				
				//si le path de la solution n'existe pas alors il n'y pas de solution
				if (solutionDijkstra.getPath() == null) {
					assertEquals(solutionBellman.getPath(),solutionDijkstra.getPath());
					System.out.println("Pas de solution");
				}else {
					//on vérifie que les couts sont egaux
					double costBellman;
					double costDijkstra;
					if (typeEvaluation == 0) { //temps
						costBellman = solutionBellman.getPath().getMinimumTravelTime();
						costDijkstra = solutionDijkstra.getPath().getMinimumTravelTime();
					}else { //distance
						costBellman = solutionBellman.getPath().getLength();
						costDijkstra = solutionDijkstra.getPath().getLength();
					}
					assertEquals(costBellman,costDijkstra,0.001);
					System.out.println("Cout de la solution : " + costDijkstra);		
				}
			}
        }
        System.out.println();
	}
	
	@Test
	public void testTempsCarreDense() throws Exception{
		System.out.println("--Test--");
		System.out.println("--Map : Carre Dense--");
		System.out.println("--Type Evaluation : Temps--");
		
		String mapName = mapsLocation + "/carre-dense.mapgr";
		int origine = 0;
		int destination = 0;
		
		testScenario(mapName,0,origine,destination);
	}

}
