package practice;

public class sort {
	public static void quickSort(int[] a,int left,int right){
		if(left<right){
			int flag=a[left];
			int i=left;
			int j=right;
			while(i<j){
				while(j>i&&a[j]>=flag){
					j--;
				}
				if(i<j){
					a[i]=a[j];
					i++;
				}
				while(i<j&&a[i]<flag){
					i++;
				}
				if(i<j){
					a[j]=a[i];
					j--;
				}
			}
			a[i]=flag;
			quickSort(a,left,i-1);
			quickSort(a,i+1,right);
		}
	}
	public static void quickSort(int[] a){
		quickSort(a,0,a.length-1);
	}
	
	public static void swap(int[] a,int b,int c){
		int temp=a[b];
		a[b]=a[c];
		a[c]=temp;
	}
	public static void bubbleSort(int a[]){
		int flag=a.length;
		int j,k;
		while(flag>0){
			k=flag;
			flag=0;
			for(j=1;j<k;j++){
				if(a[j-1]>a[j]){
					swap(a,j-1,j);
					flag=j;
				}
			}
		}
	}
	
	public static void insertSort(int[] a){
		for(int i=1;i<a.length;i++){
			for(int j=i-1;j>=0&&a[j]>a[j+1];j--){
				swap(a,j,j+1);
			}
		}
	}
	
	public static void shellSort(int[] a){
		int n=a.length;
		int gap;
		for(gap=n/2;gap>0;gap/=2){
			for(int i=gap;i<n;i++){
				for(int j=i-gap;j>=0&&a[j]>a[j+gap];j-=gap){
					swap(a,j,j+gap);
				}
			}
		}
	}
	
	public static void selectSort(int[] a){
		int min;
		for(int i=0;i<a.length;i++){
			min=i;
			for(int j=i+1;j<a.length;j++){
				if(a[j]<a[min]){
					min=j;
				}
			}
			swap(a,i,min);
		}
	}
	public static void mergeArray(int[] a,int first,int mid,int last,int[] temp){
		int i=first;
		int k=0;
		int j=mid+1;
		while(i<=mid&&j<=last){
			if(a[i]<=a[j]){
				temp[k++]=a[i++];
			}else{
				temp[k++]=a[j++];
			}
		}
		while(i<=mid){
			temp[k++]=a[i++];
		}
		while(j<=last){
			temp[k++]=a[j++];
		}
		for(int l=0;l<k;l++){
			a[first+l]=temp[l];
		}
	}
	public static void mergeSort(int[] a,int first,int last,int[] temp){
		if(first<last){
			int mid=(first+last)/2;
			mergeSort(a,first,mid,temp);
			mergeSort(a,mid+1,last,temp);
			mergeArray(a,first,mid,last,temp);
		}
	}
	public static void mergeSort(int[] a){
		int[] p=new int[a.length];
		mergeSort(a,0,a.length-1,p);
		
	}
	public static void main(String[] args){
		int[] a={1,5,3,6,7,2,9};
		mergeSort(a);
		for(int i=0;i<a.length;i++){
			System.out.print(a[i]);
		}
	}
}
