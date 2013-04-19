import Jama.*;


public class Algorithm {
	
	double [][]i; 
	Matrix A; 
	PadDraw pad;

	public Algorithm(PadDraw pad){
		this.pad = pad;
	}
	
	public void runAlg(){
		System.out.println("ran the algorithm");
	}
	
	public double[][] getWindow(Matrix A, int i, int j){
		double [][]window = new double[3][3];
		window[0][0] = A.get(i-1, j-1);
		window[0][1] = A.get(i-1, j);
		window[0][2] = A.get(i-1, j+1);
		
		window[1][0] = A.get(i, j-1);
		window[1][1] = A.get(i, j);
		window[1][2] = A.get(i, j+1);
		
		window[2][0] = A.get(i+1, j-1);
		window[2][1] = A.get(i+1, j);
		window[2][2] = A.get(i+1, j+1);
		
		
		return window;
	}
	
	
}
