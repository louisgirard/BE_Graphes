package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.algo.utils.*;
import org.insa.graph.*;
import org.insa.algo.AbstractInputData.Mode;
import org.insa.algo.AbstractSolution.Status;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
    	int nbIterations = 0;
    	
        ShortestPathData data = getInputData();
        
        ShortestPathSolution solution = null;
        
        BinaryHeap<Label> labelHeap = new BinaryHeap<Label>();
        ArrayList<Label> tabLabel = new ArrayList<Label>();
        
        Label labelCourant = new Label();
        Label labelFils = new Label();
        
        //initialisation
        for (int i = 0; i < data.getGraph().size(); i++) {
        	tabLabel.add(new Label(data.getGraph().get(i)));
        }
        tabLabel.get(data.getOrigin().getId()).setCost(0);
        labelCourant = tabLabel.get(data.getOrigin().getId()); //recupere le sommet
        labelHeap.insert(labelCourant);
        
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());
        
        //tant qu'il existe des sommets non marqués, tant que le tas n'est pas vide
        while(!labelHeap.isEmpty() && !labelCourant.getCurrentNode().equals(data.getDestination())) {
        	nbIterations++;
        	labelCourant = labelHeap.deleteMin();
        	//marque le sommet courant à TRUE
        	tabLabel.get(labelCourant.getCurrentNode().getId()).setMark();
        	//on regarde tous les successeurs du sommet/label courant
        	for (Arc a : labelCourant.getCurrentNode().getSuccessors()) {
        		labelFils = tabLabel.get(a.getDestination().getId());

                // Small test to check allowed roads...
                if (!data.isAllowed(a)) {
                    continue;
                }
                
        		//si la marque du premier successeur du sommet courant est false
        		if (!labelFils.getMark()) {
        			notifyNodeReached(a.getDestination());
        			if (labelFils.getCost() > (labelCourant.getCost() + Cost(a, data))) {
        				labelFils.setCost(labelCourant.getCost() + Cost(a, data));
        				labelHeap.insert(labelFils);
        				labelFils.setFather(a);
        				//mettre dans le tableau de label
        				tabLabel.set(a.getDestination().getId(), labelFils);
        			}
        		}
        	}
        }
        
        
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
            
            //System.out.println("Nb arcs : " + arcs.size() + ", Nb iterations : " + nbIterations);
        }
        
        
        return solution;
    }
    
    protected double Cost(Arc a, ShortestPathData data) {
    	if (data.getMode() == Mode.TIME) {
    		return a.getMinimumTravelTime();
    	}else { //Mode.LENGTH
    		return a.getLength();
    	}
    }

}
