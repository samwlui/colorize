import Jama.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Algorithm {
	
	double [][]i; 
	double [][][] info;
	Matrix A; 
	PadDraw pad;
	double [][] indexArray;
	int imgWidth;
	int imgHeight;
	Matrix yuvConv = new Matrix( new double[][]{{0.299, 0.587,0.114},
												{-0.14713, -0.28886, 0.436},
												{0.615, -0.51499, 0.10001}} );
	Matrix yiqConv = new Matrix( new double[][]{{0.299, 0.587,0.114},
												{0.595716, -0.274453, -0.321263},
												{0.211456, -0.522591, 0.311135}} );

	public Algorithm(PadDraw pad){
		this.pad = pad;
		this.imgWidth = pad.getImage().getWidth();
		this.imgHeight = pad.getImage().getHeight();
		this.indexArray = new double[this.imgWidth][this.imgHeight];
		this.info = new double[this.imgWidth][this.imgHeight][3];
	}
	
	public void runAlg(){

		exportImg(pad.getScribbled(),"scribbled.png");
		exportImg(pad.getOriginal(),"original.png");
		bldIndxArr(pad.getOriginal(),pad.getScribbled());
		info = buildInfo(pad.getOriginal(),pad.getScribbled());
		
		System.out.println("Y: " + info[0][0][0]);
		System.out.println("U: " + info[0][0][1]);
		System.out.println("V: " + info[0][0][2]);

		
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
	
	//builds the index array
	//denotes whether each pixel is a full constrained pixel or a pixel only with Y value.
	public void bldIndxArr(BufferedImage orig, BufferedImage scr){
		for (int x = 0; x < this.imgWidth; x++){
			for (int y = 0; y < this.imgHeight; y++){
				int[] original = getRGBatPxl(orig,x,y);
				int[] scribbled = getRGBatPxl(scr,x,y);
				for (int i = 0; i< original.length; i++){
					if(scribbled[i]-original[i] < 0.01){
						indexArray[x][y] = 0;
					}
					else{
						indexArray[x][y] = 1;
					}
				}
			
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
	
	public Matrix RGBtoYUV(Matrix rgb){
		return this.yiqConv.times(rgb);
		
	}
	
	public Matrix RGBtoMat(int[] src){
		double [][] array = {{src[0]}, {src[1]}, {src[2]}};
		return new Matrix(array);
	}
	
	public double[][][] buildInfo(BufferedImage orig, BufferedImage scr){
		
		double[][][] YUVmat = new double[orig.getWidth()][orig.getHeight()][3];
		
		for(int x=0; x<orig.getWidth(); x++){
			for(int y=0; y<orig.getHeight(); y++){
				
				int[] original = getRGBatPxl(orig,x,y);
				int[] scribbled = getRGBatPxl(scr,x,y);
				Matrix oRGB = RGBtoMat(original);
				Matrix sRGB = RGBtoMat(scribbled);
				
				Matrix oYUV = RGBtoYUV(oRGB);
				Matrix sYUV = RGBtoYUV(sRGB);
				
				YUVmat[x][y][0] = oYUV.get(0, 0)/255;
				YUVmat[x][y][1] = sYUV.get(1,0)/255;
				YUVmat[x][y][2] = sYUV.get(2, 0)/255;
				
			}
		}
		return YUVmat;
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
