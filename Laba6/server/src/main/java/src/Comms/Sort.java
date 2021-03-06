package src.Comms;
import src.GivenClasses.Worker;

import java.util.*;

public class Sort{
	/** 
	 *Sorts the collection
	 *@author AP
	*/
	
	public TreeSet<Worker> sort(DAO<Worker> dao){
		LinkedHashSet<Worker> bd = new LinkedHashSet<Worker>(dao.getAll());
		TreeSet<Worker> srt = new TreeSet<Worker>();
		for(Worker w : bd) {
			srt.add(w);
		}
		return srt;
	}
}
