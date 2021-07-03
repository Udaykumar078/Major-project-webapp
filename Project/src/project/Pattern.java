
package project;

import java.util.*;

public class Pattern {
    
        public static int freq=0;//temporary variable to find the no.of times the pattern is repeating in win_str
	public static int threshold=5;//min no.of times for the pattern to appear
	public static int size_win_str;//size of win_str
	public static int[] win_str;//array to store winner's traderid of all auctions w.r.t time
	public static HashSet<List<Integer>> twod_set;//hashset to store the list of cartels

	public static int CheckPatternUtil(int[] a,int start){
		int p=start;
		int count=0;
		for(int i=0;i<a.length;i++){
			while(p<size_win_str&&a[i]!=win_str[p]){
				p++;
			}
			if(p<size_win_str&&a[i]==win_str[p]){
				count++;
				p++;
			}else
				break;
		}
		if(count==a.length)
			freq++;
		if(freq>=threshold){
		    //System.out.print("Cartel :");
		   List<Integer> l1=new ArrayList<Integer>();
			for(int i=0;i<a.length;i++){
				//System.out.print(a[i]+" ");
				l1.add(a[i]);
			}
                        Collections.sort(l1);
			twod_set.add(l1);
			p=-1;
		}			
		return p;
	}
	
        public static void CheckPattern(int[] a,int n){
		int start=0;
		while(start+n-1<size_win_str){
	            	start=CheckPatternUtil(a,start);
		if(start==-1)
			return;
		}
		
	}
	
        public static void printArr(int a[], int n)
	{
		int[] temp=new int[n];
		for (int i = 0; i < n; i++){
			temp[i]=a[i];
		}
		freq=0;
		CheckPattern(temp,n);
	}

	// Generating permutation using Heap Algorithm
	public static void heapPermutation(int a[], int size, int n)
	{
		// if size becomes 1 then prints the obtained permutation
		if (size == 1)
			printArr(a, n);

		for (int i = 0; i < size; i++) {
			heapPermutation(a, size - 1, n);

			// if size is odd, swap 0th i.e (first) and
			// (size-1)th i.e (last) element
			if (size % 2 == 1) {
				int temp = a[0];
				a[0] = a[size - 1];
				a[size - 1] = temp;
			}

			// If size is even, swap ith 
			// and (size-1)th i.e last element
			else {
				int temp = a[i];
				a[i] = a[size - 1];
				a[size - 1] = temp;
			}
		}
	}
        
        public static HashSet<List<Integer>> finPattern(int[] arr,int[] prof_list){
               // System.out.println("************************************************");
		//System.out.println("Program to demonstrate pattern Matching");
                //arr or win_str is array of trader id's who won the contracts
                //prof_list is the array of trader id's who have approximately equal income
                //in this program we generate each permutation of prof_list and check if the current
                //permutation appers in arr more than threshold times. if appears we consider them as 
                //csrtel and store them in twod_set 
		twod_set=new HashSet<List<Integer>>();
		win_str=new int[arr.length];
                //copy all contents of arr to win_str
		for(int i=0;i<arr.length;i++){
			win_str[i]=arr[i];
		}
		
		size_win_str=win_str.length;
                if(prof_list.length==2){
                    printArr(prof_list,2);
                    int temp=prof_list[0];
                    prof_list[0]=prof_list[1];
                    prof_list[1]=temp;
                    printArr(prof_list,2);
                }else{
		int min_trad=3;
		int max_trad=prof_list.length;
		int size_of_prof_list=max_trad;
		for(int i=min_trad;i<=max_trad;i++){
			heapPermutation(prof_list,size_of_prof_list,i);
		}
                }
		System.out.println("List of cartels :");
		System.out.println(twod_set);
                return twod_set;
        
        }
	
	public static void main(String[] args) 
	{
		
	}
    
}
