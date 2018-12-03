package practice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.io.UnsupportedEncodingException;
import java.util.*;
public class Main {

	public static void swap(int[] a,int i,int j){
		int t=a[i];
		a[i]=a[j];
		a[j]=t;
	}
	public static void quickSort(int[] a,int left,int right){
		if(left<right){
			int flag=a[left];
			int i=left+1;
			int j=right;
			while(i<=j){
				while(i<=j&&a[i]<flag){
					i++;
				}
				while(i<=j&&a[j]>=flag){
					j--;
				}
				swap(a,i,j);
			}
			swap(a,left,j);
			quickSort(a,left,i-1);
			quickSort(a,i+1,right);
		}
	}
	public static void main(String[] args){
		Scanner sc=new Scanner(System.in);
		int n=sc.nextInt();
		int[] a=new int[n];
		for(int i=0;i<n;i++){
			a[i]=sc.nextInt();
		}
		quickSort(a,0,n-1);
		for(int i=0;i<n;i++){
			System.out.print(a[i]+" ");
		}
	}
}