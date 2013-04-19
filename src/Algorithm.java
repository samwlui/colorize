import Jama.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Algorithm {
	
	double [][]i; 
	Matrix A; 
	PadDraw pad;
	double [][] indexArray;
	int imgWidth;
	int imgHeight;

	public Algorithm(PadDraw pad){
		this.pad = pad;
		this.imgWidth = pad.getImage().getWidth();
		this.imgHeight = pad.getImage().getHeight();
		indexArray = new double[this.imgWidth][this.imgHeight];
	}
	
	public void runAlg(){
		System.out.println("ran the algorithm");
		exportImg(pad.getScribbled(),"scribbled.png");
		exportImg(pad.getOriginal(),"original.png");
	}
	
	public void exportImg(BufferedImage img, String name){
		try {
		    File outputfile = new File(name);
		    ImageIO.write(img, "png", outputfile);
		    System.out.println("saved!");
		} catch (IOException e) {
			System.out.println("error");
		}
	}
	
	public void bldIndxArr(BufferedImage orig, BufferedImage scr){
		for (int x = 0; x < this.imgWidth; x++){
			for (int y = 0; y < this.imgHeight; y++){
				
			}
		}
	}
	
	public int[] getRGBatPxl(BufferedImage img, int x, int y){
		final int clr = img.getRGB(x, y);
        final int red = (clr & 0x00ff0000) >> 16;
        final int green = (clr & 0x0000ff00) >> 8;
        final int blue = clr & 0x000000ff;  
        
        return new int[]{red,green,blue};
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
