import java.io.*;

class PlagiarismDetector {

	private String s1, s2;
	private int levenshteinDist;
	private double similarity;

	/** 
	  * Constructor.
	  * Initialize string, count LDist and Similarity.
	  * @param s1 first string
	  * @param s2 second string
	  */
	public PlagiarismDetector(String s1, String s2){
		this.s1 = s1;
		this.s2 = s2;

		initializeLevenshteinDist();
		initializeSimilarity();
	}

	/** 
	  * Count Levenshtein Distance with Dynamic Programming.
	  */
	private void initializeLevenshteinDist(){
		int lengthA = s1.length() + 1;
		int lengthB = s2.length() + 1;
		int[][] dp = new int[lengthA][lengthB];

		// Initialize DP base value
		for(int i=0; i<lengthA; i++){
			dp[i][0] = i;
		}
		for(int j=0; j<lengthB; j++){
			dp[0][j] = j;
		}

		// Turn strings to be 1-based
		String DUMMY = new String("-");
		String stringA = DUMMY + s1;
		String stringB = DUMMY + s2;

		// build table values
		for(int i=1; i<lengthA; i++){
			for(int j=1; j<lengthB; j++){
				if (stringA.charAt(i) == stringB.charAt(j)){
					dp[i][j] = dp[i-1][j-1];
				}
				else{
					dp[i][j] = min(
						dp[i-1][j] + 1, // deletion
						dp[i][j-1] + 1, // insertion
						dp[i-1][j-1] + 1 // substitution
					);
				}
			}
		}

		// print table
		//for(int i=0; i<lengthA; i++){
		//	for(int j=0; j<lengthB; j++){
		//		System.out.print(dp[i][j]);
		//		if (dp[i][j] / 10 == 0)
		//			System.out.print("  ");
		//		else
		//			System.out.print(" ");
		//	}
		//	System.out.println();
		//}

		levenshteinDist = dp[lengthA-1][lengthB-1];
	}

	/** 
	  * Count Similarity.
	  * Initial State: levenshteinDist has been initalized.
	  */
	private void initializeSimilarity(){
		similarity = 1.0 - (double) levenshteinDist / (max(s1.length(), s2.length()));
	}

	private int max(int a, int b){
		return (a > b)? a : b;
	}

	private int max(int a, int b, int c){
		return (max(a, b) > c)? max(a, b) : c;
	}

	private int min(int a, int b){
		return (a < b)? a : b;
	}

	private int min(int a, int b, int c){
		return (min(a, b) < c)? min(a, b) : c;
	}

	/**
	  * Getter first string
	  */
	public String getFirst(){
		return s1;
	}

	/**
	  * Getter second string
	  */
	public String getSecond(){
		return s2;
	}

	/**
	  * Get Levenshtein Distance value
	  */
	public int getLevenshteinDist(){
		return levenshteinDist;
	}

	/**
	  * Get Similarity value
	  */
	public double getSimilarity(){
		return similarity;
	}

	/**
	  * Return maximum length from s1 and s2
	  */
	public int getMaximumDist(){
		return (s1.length() > s2.length()) ? s1.length() : s2.length();
	}


	public static void main(String[] args) {
		// Preprocessing
		StringBuilder a = new StringBuilder();
		StringBuilder b = new StringBuilder();
		try{
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			String line;
			while ((line = br.readLine()) != null) {
			    a.append(line);
			}
			
			BufferedReader br1 = new BufferedReader(new FileReader(args[1]));
			while ((line = br1.readLine()) != null) {
			    b.append(line);
			}
		}
		catch (Exception e){

		}
		

		PlagiarismDetector pd = new PlagiarismDetector(a.toString(), b.toString());
		//PlagiarismDetector pd = new PlagiarismDetector("timoy", "askamda");

		System.out.println("Panjang String Maksimum: " + pd.getMaximumDist());
		System.out.println("Jarak Levenshtein: " + pd.getLevenshteinDist());
		System.out.printf ("Similarity(%%): %.2f\n", pd.getSimilarity()*100);
	}

}