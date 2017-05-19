// HackerRank

import java.io.Scanner;

public class Solution {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		List<Integer> a = new ArrayList<Integer>();

		int n = scanner.nextInt();
		for(int i=0; i<n; i++){
			a.add(scanner.nextInt());
		}
		
		int sum;
		int counter = 0;
		int k=0;

		while(k < n){
			for(int j=0; j<n-k; ++j){
				sum = 0;
				for(int i=j; i<=j+k; i++){
					sum += a.get(i);
				}
				if (sum < 0){
					counter++;
				}
			}
			k = k + 1;
		}

		System.out.println(counter);
	}

}