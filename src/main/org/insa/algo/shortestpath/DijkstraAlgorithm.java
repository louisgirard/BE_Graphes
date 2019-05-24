package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.algo.utils.*;
import org.insa.graph.*;
import org.insa.algo.AbstractSolution.Status;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	private long cpuTime = 0;
	private int nbExplores = 0;	
	private int nbMarques = 0;	
	private int maxTas = 0;
	
    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
    	long cpuTime = 0;
    	int maxTas = 0;
    	
        ShortestPathData data = getInputData();
        
        ShortestPathSolution solution = null;
        
        BinaryHeap<Label> labelHeap = new BinaryHeap<Label>();
        ArrayList<Label> tabLabel = new ArrayList<Label>();
        
        Label labelCourant = newLabel();
        Label labelFils = newLabel();
        
        //initialisation
        for (int i = 0; i < data.getGraph().size(); i++) {
        	tabLabel.add(newLabel(data.getGraph().get(i),data));
        }
        tabLabel.get(data.getOrigin().getId()).setCost(0);
        labelCourant = tabLabel.get(data.getOrigin().getId()); //recupere le sommet
        labelHeap.insert(labelCourant);
        
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());
        
        cpuTime = System.nanoTime();
        
        //tant qu'il existe des sommets non marqués, tant que le tas n'est pas vide
        while(!labelHeap.isEmpty() && !labelCourant.getCurrentNode().equals(data.getDestination())) {
        	maxTas = labelHeap.size();
        	if (this.maxTas < maxTas) {
        		this.maxTas = maxTas;
        	}
        	labelCourant = labelHeap.deleteMin();
        	this.nbExplores++;
        	//marque le sommet courant à TRUE
        	tabLabel.get(labelCourant.getCurrentNode().getId()).setMark();
        	this.nbMarques++;
        	//on regarde tous les successeurs du sommet/label courant
        	for (Arc a : labelCourant.getCurrentNode().getSuccessors()) {
        		labelFils = tabLabel.get(a.getDestination().getId());
            	this.nbExplores++;

                // Small test to check allowed roads...
                if (!data.isAllowed(a)) {
                    continue;
                }
                
        		//si la marque du premier successeur du sommet courant est false
        		if (!labelFils.getMark()) {
        			notifyNodeReached(a.getDestination());
        			if (labelFils.getCost() > (labelCourant.getCost() + data.getCost(a))) {
        				labelFils.setCost(labelCourant.getCost() + data.getCost(a));
        				labelHeap.insert(labelFils);
        				labelFils.setFather(a);
        				//mettre dans le tableau de label
        				tabLabel.set(a.getDestination().getId(), labelFils);
        			}
        		}
        	}
        }
        
        this.cpuTime = System.nanoTime() - cpuTime;
        
        // Destination has no predecessor, the solution is infeasible...
        if (tabLabel.get(data.getDestination().getId()).getFather() == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {
            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());
            
            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = tabLabel.get(data.getDestination().getId()).getFather();
            while (arc != null) {
                arcs.add(arc);
                arc = tabLabel.get(arc.getOrigin().getId()).getFather();
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(data.getGraph(), arcs));
            
        }
        
        
        return solution;
    }
    
    protected Label newLabel(Node node, ShortestPathData data) {
		return new Label(node);
	}
    
    protected Label newLabel() {
		return new Label();
	}
    
    public long getCpuTime() {
    	return cpuTime;
    }
    
    public int getNbExplores() {
    	return nbExplores;
    }
    
    public int getNbMarques() {
    	return nbMarques;
    }
    
    public int getMaxTas() {
    	return maxTas;
    }

}
