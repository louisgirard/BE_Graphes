package org.insa.algo.shortestpath;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;

import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.graph.Graph;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.junit.Test;
import java.io.FileNotFoundException;

public class TestPerformance {

    private String mapsLocation = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/";
    private String pathLocation = "/home/lgirard/BE_Graphes/tests_performance/";
    
    //créer un fichier avec le bon nom
	public String createFile(String filePath, String map, int type, int nbPaires, int dataType) throws IOException{
		//dataType : 0 -> data, 1 -> dijkstra, 2 -> astar
		String name;
		name = map + "_";
		if (type == 0) {
			name += "distance_";
		}else {
			name += "temps_";			
		}
		name += nbPaires + "_";
		
		switch(dataType) {
			case 0:
				name += "data.txt";
				break;
			case 1:
				name += "dijkstra.txt";
				break;
			case 2:
				name += "astar.txt";
				break;
			default:
		}
		
		File file = new File(filePath + name);
		file.createNewFile();
		
        System.out.println("Fichier créé");
        
        return name;
	}
	
	//créer le fichier data.txt
	public String generateFileTestData(String map, int type, int nbPaires, int max, int min) throws IOException{
		int origine;
		int destination;
		String fileName;
		fileName = createFile(pathLocation,map,type,nbPaires,0);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(pathLocation + fileName));
	    writer.write(map + "\n" + type + "\n" + nbPaires + "\n");
	    for (int i = 0; i < nbPaires; i++) {
			origine = (int)(Math.random() * (max - min));
			destination = (int)(Math.random() * (max - min));
			writer.write(origine + " " + destination + "\n");
	    }
	    writer.close();
	    
	    return fileName;
	}
	
	//créer les fichiers dijkstra.txt et astar.txt sans les origines et destinations
	public String initializeFileTestAlgo(String map, int type, int nbPaires, int dataType) throws IOException{
		String fileName;
		fileName = createFile(pathLocation,map,type,nbPaires,dataType);
		String dataToWrite;
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(pathLocation + fileName));
		dataToWrite = map + "\n" + type + "\n" + nbPaires + "\n";
	    if (dataType == 1) {
	    	dataToWrite += "dijkstra\n";
	    }else {
	    	dataToWrite += "astar\n";
	    }
	    writer.write(dataToWrite);
	    
	    writer.close();
	    
	    return fileName;
	    
	}
	
	public void writeFileTestAlgo(String fileName, int origine, int destination, double solution, long cpuTime, int nbExplores, int nbMarques, int maxTas) 
			throws IOException {
		String dataToWrite;
		FileWriter writer = new FileWriter(pathLocation + fileName, true);
		dataToWrite = origine + " " + destination + " " + solution + " " + cpuTime + " " + nbExplores + " " + nbMarques + " " + maxTas + "\n";
	    writer.write(dataToWrite);
	    
		writer.close();
	}
	
	//génère les fichiers dijkstra.txt et astar.txt en fonction de data.txt
	public void generateFileTest(String fileNameData) throws IOException{
		//fileNameData = nom du fichier data.txt
		String fileNameDijkstra, fileNameAStar;
		BufferedReader fileReader = new BufferedReader(new FileReader(new File(pathLocation + fileNameData)));
		String map;
		int type, nbPaires, origine, destination;
		double costDijkstra, costAStar;
		String[] nodes;

		map = fileReader.readLine();
		type = Integer.parseInt(fileReader.readLine());
		nbPaires = Integer.parseInt(fileReader.readLine());
		
		//création des fichiers pour dijkstra et astar
		fileNameDijkstra = initializeFileTestAlgo(map,type,nbPaires,1);
		fileNameAStar = initializeFileTestAlgo(map,type,nbPaires,2);
		
		for (int i = 0; i < nbPaires; i++) {
			nodes = fileReader.readLine().split(" ");
			origine = Integer.parseInt(nodes[0]);
			destination = Integer.parseInt(nodes[1]);
			
			// Create a graph reader.
           GraphReader reader = new BinaryGraphReader(
                   new DataInputStream(new BufferedInputStream(new FileInputStream(mapsLocation + map + ".mapgr"))));
           // Read the graph.
           Graph graph = reader.read();

	       	ArcInspector arcInspector;
	       	//0 = distance, 1 = temps
	       	if (type == 1) {
	       		arcInspector = new ArcInspectorFactory().getAllFilters().get(2);
	       	}else {
	       		arcInspector = new ArcInspectorFactory().getAllFilters().get(0);
	       	}
			ShortestPathData data = new ShortestPathData(graph,graph.get(origine),graph.get(destination),arcInspector);
			
			DijkstraAlgorithm dijkstraAlgo = new DijkstraAlgorithm(data);
			AStarAlgorithm aStarAlgo = new AStarAlgorithm(data);
			
			System.out.println("Lancement de Dijkstra et Astar");
			
			ShortestPathSolution solutionDijkstra = dijkstraAlgo.doRun();
			ShortestPathSolution solutionAStar = aStarAlgo.doRun();
			
			if (solutionDijkstra.getPath() == null) {
				costDijkstra = Double.POSITIVE_INFINITY;
			}
			else {
				if (type == 1) { //temps
					costDijkstra = solutionDijkstra.getPath().getMinimumTravelTime();
				}else { //distance
					costDijkstra = solutionDijkstra.getPath().getLength();
				}
			}
			
			if (solutionAStar.getPath() == null) {
				costAStar = Double.POSITIVE_INFINITY;
			}
			else {
				if (type == 1) { //temps
					costAStar = solutionAStar.getPath().getMinimumTravelTime();
				}else { //distance
					costAStar = solutionAStar.getPath().getLength();
				}
			}
			
			writeFileTestAlgo(fileNameDijkstra, origine, destination, costDijkstra, dijkstraAlgo.getCpuTime(), 
					dijkstraAlgo.getNbExplores(), dijkstraAlgo.getNbMarques(), dijkstraAlgo.getMaxTas());
			writeFileTestAlgo(fileNameAStar, origine, destination, costAStar, aStarAlgo.getCpuTime(), 
					aStarAlgo.getNbExplores(), aStarAlgo.getNbMarques(), aStarAlgo.getMaxTas());
			
		}
		
		fileReader.close();
		
	}
	
	@Test
	public void createFile() {
		String map = "toulouse";
		String fileNameData;
		try {
			fileNameData = generateFileTestData(map,0,10,35000,0);
			generateFileTest(fileNameData);
		}catch(IOException e) {
			//do nothing
		}
		
	}
	
}
