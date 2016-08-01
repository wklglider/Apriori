import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	
	public static void main(String[] args) {
		List<List<String>> dataset1 = new ArrayList<>();
		List<List<String>> dataset2 = new ArrayList<>();
		List<List<String>> dataset3 = new ArrayList<>();
		int minSupport_a = 6;
		int minSupport_b1 = 3;
		int minSupport_b2 = 4;
		int minSupport_b3 = 5;
		try {
			BufferedReader br_1 = new BufferedReader(new FileReader("./Datasets/dataset1.txt"));
			BufferedReader br_2 = new BufferedReader(new FileReader("./Datasets/dataset2.txt"));
			BufferedReader br_3 = new BufferedReader(new FileReader("./Datasets/dataset3.txt"));
			String temp;
			while ((temp = br_1.readLine()) != null)
				dataset1.add(Arrays.asList(temp.split(" ")));
			while ((temp = br_2.readLine()) != null)
				dataset2.add(Arrays.asList(temp.split(" ")));
			while ((temp = br_3.readLine()) != null)
				dataset3.add(Arrays.asList(temp.split(" ")));
			br_1.close();
			br_2.close();
			br_3.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		go(dataset1, minSupport_a, "A", "dataset1");
		go(dataset2, minSupport_a, "A", "dataset2");
		go(dataset3, minSupport_a, "A", "dataset3");
		go(dataset2, minSupport_b1, "B", "minSup3");
		go(dataset2, minSupport_b2, "B", "minSup4");
		go(dataset2, minSupport_b3, "B", "minSup5");
	}
	
	public static void go(List<List<String>> dataset, int minSupport, String ab, String folder) {
		File file = new File("./Output/" + ab + "/" + folder);
		if (!file.exists())
			file.mkdirs();
		SupportCounter sc = new SupportCounter(dataset, minSupport);
		CandidateGen cg = new CandidateGen();
		Map<String, Integer> allFrequentItemsets = new HashMap<>();
		Map<String, Integer> allInfrequentItemsets = new HashMap<>();
		long time_c, time_l;
		Map<String, Integer> lk = sc.initialize(ab, folder);
		time_c = sc.time_c;
		time_l = sc.time_l;
		allFrequentItemsets.putAll(lk);
		allInfrequentItemsets.putAll(sc.allInfrequentCandidates);
		List<List<String>> ck = cg.getCandidates(lk, ab, folder);
		time_c += cg.time;
		while ((lk != null) && (ck != null)) {
			lk = sc.getFrequentCandidates(ck, ab, folder);
			time_l += sc.time;
			allFrequentItemsets.putAll(lk);
			allInfrequentItemsets.putAll(sc.allInfrequentCandidates);
			ck = cg.getCandidates(lk, ab, folder);
			time_c += cg.time;
		}
		try {
			BufferedWriter summary_txt = new BufferedWriter(new FileWriter("./Output/" + ab + "/" + folder + "/Summary.txt"));
			summary_txt.write("The minSupport value: " + sc.minSupport + "\n");
			summary_txt.write("Total computation time for the CandidateGenerator: " + time_c + "\n");
			summary_txt.write("Total computation time for the SupportCoutner: " + time_l + "\n");
			summary_txt.write("Total computation time: " + (time_c + time_l) + "\n");
			summary_txt.write("Total number of frequent itemsets: " + allFrequentItemsets.size() + "\n");
			summary_txt.write("Total number of infrequent itemsets: " + allInfrequentItemsets.size() + "\n");
			summary_txt.close();
			BufferedWriter frequent_txt = new BufferedWriter(new FileWriter("./Output/" + ab + "/" + folder + "/Frequent.txt"));
			frequent_txt.write(allFrequentItemsets.toString());
			frequent_txt.close();
			BufferedWriter infrequent_txt = new BufferedWriter(new FileWriter("./Output/" + ab + "/" + folder + "/Infrequent.txt"));
			infrequent_txt.write(allInfrequentItemsets.toString());
			infrequent_txt.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
