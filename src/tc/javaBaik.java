// HackerRank

import java.io.Scanner;

class Solution {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		List<Integer> myList = new ArrayList<Integer>();

		// Input
		while (scanner.hasNext()){
			myList.add(scan.nextInt())
		}

		// Brute Force
		int count = brute(myList);
		System.out.println(count);

		// Print List Content
		System.out.println(myList.toString());
	}

	public static int brute(List<Integer> myList){
		int sum = 0, count = 0, k = 0;
		while(k < myList.size()){

			for(int j=0; j<n-myList.size(); ++j){

				sum = 0;

				for(int i=j; i<=j+myList.size(); i++){
					sum += myList.get(i);
				}

				if (sum < 0)
					count++;
			}
			
			k++;
		}

		return count;
	}

}