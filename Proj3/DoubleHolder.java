package Proj3;
public class DoubleHolder{
	private double[][] array; 
	private double[][] array2;
	private double det;
	public DoubleHolder() {
		
	}
	public DoubleHolder(double[][] array, double[][] array2, double det) {
		this.array =array;
		this.array2 =array2;
		this.det = det;
	}
	
	public double[][] getArray1() {
		return this.array;
	}
	public double[][] getArray2() {
		return this.array2;
	}
	public double getDet() {
		return this.det;
	}
	public void setArrays(DoubleHolder array) {
		this.array = array.getArray1();
		this.array2 =array.getArray2();
		this.det = array.det;
	}
	
}