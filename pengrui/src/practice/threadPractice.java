package practice;

interface Source{void method();}
public class threadPractice implements Source{
	private Source source;
	public void decorate(){
		System.out.println("decorate");
	}
	@Override
	public void method() {
		// TODO Auto-generated method stub
		decorate();
		source.method();
	}
	public static void main(String args[]){
		threadPractice a=new threadPractice();
		a.method();
	}
}
