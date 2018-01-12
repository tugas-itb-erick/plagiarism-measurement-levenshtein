// HackerRank

import java.io.*;
import java.util.*;

public class Solution {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		Vector<Integer> a = new Vector<Integer>();

		int n = scan.nextInt();
		for(int i=0; i<n; i++){
			a.addElement(scan.nextInt());
		}
		
		int sum;
		int count = 0;
		int k=0;
		while(k < n){
			for(int j=0; j<n-k; ++j){
				sum = 0;
				for(int i=j; i<=j+k; i++){
					sum += a.get(i);
				}
				if (sum < 0)
					count++;
			}
			k++;
		}

		System.out.println(count);

		/* Print Vector
		for(Integer elem : a){
			System.out.println(elem);
		}*/
	}

}