package rpise;

public class Program {

	public static void main(String[] args) {
		C1 obj1 = new C1();
		C2 obj2 = new C2();
		
		client(obj1);
		client(obj2);
	}
	
	static void client(C1 obj){
		obj.staticMethod();
	}
	
}

class C1{
	
	public static void staticMethod(){
		System.out.println("Static Method of C1");
	}

}

class C2 extends C1{
	
	public static void staticMethod(){
		System.out.println("Static Method of C2");
	}
}

