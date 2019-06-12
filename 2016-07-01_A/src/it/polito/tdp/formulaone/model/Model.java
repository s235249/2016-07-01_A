package it.polito.tdp.formulaone.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {

	private FormulaOneDAO dao= new FormulaOneDAO();
	private List<Integer> stagioni= new ArrayList<Integer>();
	private SimpleDirectedWeightedGraph<Driver,DefaultWeightedEdge> graph;

	public Model() {
		super();
	}
	
	public List<Integer> getStagioni(){
		
		stagioni.addAll(dao.getAllYearsOfRace());
		return stagioni;
		
	}

	public void creaGrafo(Integer year) {
		
		List<Integer> races = new ArrayList<Integer>(dao.getAllRacesFromYear(year));
		
		dao.getAllDrivers();
		
		List<Driver> drivers= new ArrayList<Driver>();
		
		
		for(Integer r: races)
		{
			
			List<Driver> daControllare= dao.getAllDriversFromRace(r);
			
			for ( Driver d: daControllare)
			{
				if (!drivers.contains(d))
				{
					drivers.add(d);
				}
				
			}
			
		}
		
		graph= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.graph, drivers);
		
		for (Driver d1 : drivers)
		{
			for (Driver d2: drivers)
			{
				if(d1!=d2)
				{
					DefaultWeightedEdge e= this.graph.addEdge(d1, d2);
					this.graph.setEdgeWeight(e, dao.getWeight(d1.getDriverId(), d2.getDriverId()));
					
				}
				
			}
			
		}
		
	
	}
	
	public Driver migliorPilota()
	{
		int migliore=0;
		Driver best = null;
		
		for (Driver d: this.graph.vertexSet())
		{
			Set<DefaultWeightedEdge> in= graph.incomingEdgesOf(d);
			Set<DefaultWeightedEdge> out= graph.outgoingEdgesOf(d);
			int inc=0;
			int outg=0;
			int punteggio=0;
			
			for(DefaultWeightedEdge e: in)
			{
				inc+= graph.getEdgeWeight(e);
			}
			
			for(DefaultWeightedEdge e: out)
			{
				outg+= graph.getEdgeWeight(e);
			}
			
			punteggio= outg-inc;
			
			if (punteggio>migliore)
			{
				migliore=punteggio;
				best=d;
			}
		}
		
		return best;
		
	}

}
