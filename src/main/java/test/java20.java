package test;

class Parent{
	int x = 100;
	Parent(){
		this(500);
		System.out.println("1");
	}
	Parent(int x){
		this.x = x;
		System.out.println("2");
	}
	int getX() {
		return x;
	}
}

class Child extends Parent{
	int x = 4000;
	Child(){
//		super();
		this(5000);
		System.out.println("3");
	}
	Child(int x){
		this.x = x;
		System.out.println("4");
	}
//	@Override
//	int getX() {
//		return x;
//	}
}
public class java20 {

	public static void main(String[] args) {
//		Child obj = new Child();
//		System.out.println(obj.getX());
		
		//23-2
		String s1 = "java";
		String s2 = " java ";
		String s3 = new String(" java ");

		System.out.println(s1 == s2);        // true (문자열 상수 풀에서 동일한 객체 사용)
		System.out.println(s1 == s3);        // false (new로 생성한 객체는 다른 주소)

		System.out.println(s1.equals(s2));   // true (내용 비교)
		System.out.println(s1.equals(s3));   // true (내용이 같음)

	}

}
