import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupportCounter {
	
	Map<String, Integer> allInfrequentCandidates = new HashMap<>();
	List<List<String>> dataset;
	int minSupport;
	long time, time_c, time_l;
	
	SupportCounter (List<List<String>> dataset, int minSupport) {
		this.dataset = dataset;
		this.minSupport = minSupport;
	}
	
	public Map<String, Integer> initialize(String ab, String folder) {
		long time_1 = System.currentTimeMillis();
		Map<String, Integer> frequentCandidates = new HashMap<>();
		for (List<String> list : dataset) {
			for (String key : list) {
				if (frequentCandidates.containsKey(key))
					frequentCandidates.put(key, frequentCandidates.get(key) + 1);
				else
					frequentCandidates.put(key, 1);
			}
		}
		long time_2 = System.currentTimeMillis();
		time_c = time_2 - time_1;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("./Output/" + ab + "/" + folder + "/C1.txt"));
			bw.write("The number of generated candidates: " + frequentCandidates.size() + "\n");
			bw.write("Computation time: " + time_c + "\n");
			bw.write("The list of all generated candidates:\n" + frequentCandidates.keySet());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<String> list = new ArrayList<>(frequentCandidates.keySet());
		for (String key : list) {
			if (frequentCandidates.get(key) < minSupport) {
				frequentCandidates.remove(key);
				if (allInfrequentCandidates.containsKey(key))
					allInfrequentCandidates.put(key, allInfrequentCandidates.get(key) + 1);
				else
					allInfrequentCandidates.put(key, 1);
			}
		}
		long time_3 = System.currentTimeMillis();
		time_l = time_3 - time_2;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("./Output/" + ab + "/" + folder + "/L1.txt"));
			bw.write("The number of frequent candidates: " + frequentCandidates.size() + "\n");
			bw.write("Computation time: " + time_l + "\n");
			bw.write("The list of all frequent candidates:\n" + frequentCandidates);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return frequentCandidates;
	}
	
	public Map<String, Integer> getFrequentCandidates(List<List<String>> candidates, String ab, String folder) {
		long time_1 = System.currentTimeMillis();
		Map<String, Integer> frequentCandidates = new HashMap<>();
		for (int i = 0; i < candidates.size(); i++) {
			for (int j = 0; j < dataset.size(); j++) {
				int counter = 0;
				for (int k = 0; k < candidates.get(0).size(); k++) {
					if (dataset.get(j).contains(candidates.get(i).get(k)))
						counter++;
					else
						break;
				}
				if (counter == candidates.get(0).size()) {
					if (frequentCandidates.containsKey(candidates.get(i).toString()))
						frequentCandidates.put(candidates.get(i).toString(), frequentCandidates.get(candidates.get(i).toString()) + 1);
					else
						frequentCandidates.put(candidates.get(i).toString(), 1);
				}
			}
		}
		List<String> list = new ArrayList<>(frequentCandidates.keySet());
		for (String key : list) {
			if (frequentCandidates.get(key) < minSupport) {
				frequentCandidates.remove(key);
				if (allInfrequentCandidates.containsKey(key))
					allInfrequentCandidates.put(key, allInfrequentCandidates.get(key) + 1);
				else
					allInfrequentCandidates.put(key, 1);
			}
		}
		long time_2 = System.currentTimeMillis();
		time = time_2 - time_1;
		if (candidates.isEmpty())
			return null;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("./Output/" + ab + "/" + folder + "/L" + candidates.get(0).size() + ".txt"));
			bw.write("The number of frequent candidates: " + frequentCandidates.size() + "\n");
			bw.write("Computation time: " + time + "\n");
			bw.write("The list of all frequent candidates:\n" + frequentCandidates);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return frequentCandidates;
	}
}
