import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CandidateGen {
	
	long time;
	
	public List<List<String>> getCandidates(Map<String, Integer> frequentCandidates, String ab, String folder) {
		long time_1 = System.currentTimeMillis();
		List<List<String>> candidates_old = new ArrayList<>();
		List<List<String>> candidates_new = new ArrayList<>();
		for (String key : frequentCandidates.keySet()) {
			key = key.replaceAll("[\\[\\]\\{\\}]", "");
			candidates_old.add(Arrays.asList(key.split(", ")));
		}
		for (int i = 0; i < candidates_old.size(); i++) {
			for (int j = i + 1; j < candidates_old.size(); j++) {
				int counter = 0;
				String item = null;
				for (int k = 0; k < candidates_old.get(0).size(); k++) {
					if (candidates_old.get(i).contains(candidates_old.get(j).get(k)))
						counter ++;
					else
						item = candidates_old.get(j).get(k);
				}
				if (counter == (candidates_old.get(0).size() - 1)) {
					candidates_new.add(new ArrayList<>(candidates_old.get(i)));
					candidates_new.get(candidates_new.size() - 1).add(item);
				}
			}
		}
		for (int i = 0; i < candidates_new.size(); i++) {
			for (int j = i + 1; j < candidates_new.size(); j++) {
				int counter = 0;
				for (int k = 0; k < candidates_new.get(0).size(); k++) {
					if (candidates_new.get(i).contains(candidates_new.get(j).get(k)))
						counter++;
				}
				if (counter == candidates_new.get(0).size())
					candidates_new.remove(j--);
			}
		}
		for (int i = 0; i < candidates_new.size(); i++) {
			int counter = 0;
			for (int k = 0; k < candidates_new.get(0).size(); k++) {
				List<String> list = new ArrayList<>(candidates_new.get(i));
				list.remove(k);
				for (int j = 0; j < candidates_old.size(); j++) {
					if (candidates_old.get(j).containsAll(list)) {
						counter++;
						break;
					}
				}
			}
			if (counter != candidates_new.get(0).size())
				candidates_new.remove(i--);
		}
		long time_2 = System.currentTimeMillis();
		time = time_2 - time_1;
		if (candidates_new.isEmpty())
			return null;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("./Output/" + ab + "/" + folder + "/C" + candidates_new.get(0).size() + ".txt"));
			bw.write("The number of generated candidates: " + candidates_new.size() + "\n");
			bw.write("Computation time: " + time + "\n");
			bw.write("The list of all generated candidates:\n" + candidates_new);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return candidates_new;
	}
}
