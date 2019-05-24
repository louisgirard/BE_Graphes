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

import org.insa.graph.Graph;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.junit.Test;
import java.io.FileNotFoundException;

public class TestPerformance {

    private String mapsLocation = "/home/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps";
    private String pathLocation = "/home/lgirard/BE_Graphes/tests_performance/";
    
	public void createFile(String filePath) throws IOException{
		File file = new File(filePath);
		
		if(file.createNewFile()){
            System.out.println("Fichier créé");
        }else{
        	System.out.println("Fichier existe déjà");
        }
	}
	
	public void generateFileTest1(String path, String map, int type, int nbPaires, int max, int min) throws IOException{
		int origine;
		int destination;
		createFile(path);
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
	    writer.write(map + "\n" + type + "\n" + nbPaires + "\n");
	    for (int i = 0; i < nbPaires; i++) {
			origine = (int)(Math.random() * (max - min));
			destination = (int)(Math.random() * (max - min));
			writer.write(origine + " " + destination + "\n");
	    }
	    writer.close();
	}
	
	public void generateFileTest2(String path) throws IOException{
		BufferedReader fileReader = new BufferedReader(new FileReader(new File(path)));
		String map;
		int type, nbPaires, origine, destination;
		String[] nodes;
		
		map = fileReader.readLine();
		type = Integer.parseInt(fileReader.readLine());
		nbPaires = Integer.parseInt(fileReader.readLine());
		
		for (int i = 0; i < nbPaires; i++) {
			nodes = fileReader.readLine().split(" ");
			origine = Integer.parseInt(nodes[0]);
			destination = Integer.parseInt(nodes[1]);
			
			// Create a graph reader.
	           GraphReader reader = new BinaryGraphReader(
	                   new DataInputStream(new BufferedInputStream(new FileInputStream(map + ".mapgr"))));
	           // Read the graph.
	           Graph graph = reader.read();
		}
		
	}
	
	@Test
	public void createFile() {
		String path = pathLocation + "toulouse_distance_50_data.txt";
		String map = "toulouse";
		try {
			generateFileTest1(path,map,0,50,35000,0);
			
		}catch(IOException e) {
			//do nothing
		}
		
	}
	
}
