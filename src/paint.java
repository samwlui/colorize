/* 
 * Code by NomNom /*
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import Jama.*;


public class paint{
	public static void main(String[] args){
		Icon iconB = new ImageIcon("blue.gif");
		//the blue image icon
		Icon iconM = new ImageIcon("magenta.gif");
		//magenta image icon
		Icon iconR = new ImageIcon("red.gif");
		//red image icon
		Icon iconBl = new ImageIcon("black.gif");
		//black image icon
		Icon iconG = new ImageIcon("green.gif");
		//finally the green image icon
		//These will be the images for our colors.
		
		JFrame frame = new JFrame("Paint It");
		//Creates a frame with a title of "Paint it"
		
		Container content = frame.getContentPane();
		//Creates a new container
		content.setLayout(new BorderLayout());
		//sets the layout
		
		final PadDraw drawPad = new PadDraw();
		//creates a new padDraw, which is pretty much the paint program
		
		content.add(drawPad, BorderLayout.CENTER);
		//sets the padDraw in the center
		
		JPanel panel = new JPanel();
		//creates a JPanel
		panel.setPreferredSize(new Dimension(32, 68));
		panel.setMinimumSize(new Dimension(32, 68));
		panel.setMaximumSize(new Dimension(32, 68));
		//This sets the size of the panel
		
		JButton clearButton = new JButton("Clear");
		//creates the clear button and sets the text as "Clear"
		clearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPad.clear();
			}
		});
		
		
		JButton runButton = new JButton("Colorize");
		runButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				drawPad.setScribbled(drawPad.getCurrentScreen());
				Algorithm alg = new Algorithm(drawPad);
				alg.runAlg();

			}
		});
		//this is the clear button, which clears the screen.  This pretty
		//much attaches an action listener to the button and when the
		//button is pressed it calls the clear() method
		
		JButton redButton = new JButton(iconR);
		//creates the red button and sets the icon we created for red
		redButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPad.red();
			}

		});
		//when pressed it will call the red() method.  So on and so on =]
		
		JButton blackButton = new JButton(iconBl);
		//same thing except this is the black button
		blackButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPad.black();
			}
		});
		
		JButton magentaButton = new JButton(iconM);
		//magenta button
		magentaButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPad.magenta();
			}
		});
		
		JButton blueButton = new JButton(iconB);
		//blue button
		blueButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPad.blue();
			}
		});
		
		JButton greenButton = new JButton(iconG);
		//green button
		greenButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPad.green();
			}
		});
		blackButton.setPreferredSize(new Dimension(16, 16));
		magentaButton.setPreferredSize(new Dimension(16, 16));
		redButton.setPreferredSize(new Dimension(16, 16));
		blueButton.setPreferredSize(new Dimension(16, 16));
		greenButton.setPreferredSize(new Dimension(16,16));
		//sets the sizes of the buttons
		
		panel.add(greenButton);
		panel.add(blueButton);
		panel.add(magentaButton);
		panel.add(blackButton);
		panel.add(redButton);
		panel.add(clearButton);
		panel.add(runButton);
		//adds the buttons to the panel
		
		content.add(panel, BorderLayout.WEST);
		//sets the panel to the left
		
		frame.setSize(400, 400);
		//sets the size of the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//makes it so you can close
		frame.setVisible(true);
		//makes it so you can see it
	}
}


class PadDraw extends JComponent{
	
    BufferedImage image;
    BufferedImage original;
    BufferedImage scribbled;

	Graphics2D graphics2D;
	//this is what we'll be using to draw on
	  int currentX, currentY, oldX, oldY;
	//these are gonna hold our mouse coordinates

	//Now for the constructors
	public PadDraw(){
		



	setPreferredSize(new Dimension(300, 300));
	setMinimumSize(new Dimension(300, 300));
	setMaximumSize(new Dimension(300, 300));
		
		setDoubleBuffered(false);
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				oldX = e.getX();
				oldY = e.getY();
			}
		});
		//if the mouse is pressed it sets the oldX & oldY
		//coordinates as the mouses x & y coordinates
		addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e){
				currentX = e.getX();
				currentY = e.getY();
				if(graphics2D != null)
				//System.out.println(graphics2D.getPaint());
			    graphics2D.drawLine(oldX, oldY, currentX, currentY);
				repaint();
				oldX = currentX;
				oldY = currentY;
			}

		});
		//while the mouse is dragged it sets currentX & currentY as the mouses x and y
		//then it draws a line at the coordinates
		//it repaints it and sets oldX and oldY as currentX and currentY
	}
	
	public BufferedImage makeGray(BufferedImage img){
		
		for(int x=0; x<img.getWidth(); x++){
			for(int y=0; y<img.getHeight(); y++){
				final int clr = img.getRGB(x, y);
                final int red = (clr & 0x00ff0000) >> 16;
                final int green = (clr & 0x0000ff00) >> 8;
                final int blue = clr & 0x000000ff;                
                int gry = (red + green + blue)/3;
                int pixel = (gry << 16) | (gry << 8) | gry;
				img.setRGB(x, y, pixel);
			}
		}
		
		return img;
	}

	public void paintComponent(Graphics g){
		if(image == null){
			//image = createImage(getSize().width, getSize().height);
			
			try{
					String imgPath = "images/test.JPG";
					image = ImageIO.read(getClass().getResourceAsStream(imgPath));
					//image =  ImageIO.read(new File("images/test.JPG"));
					//image = loadImage("images/test.JPG");
					image = makeGray(image);
					original = deepCopy(image);
					double[][]arr = new double[image.getWidth()][image.getHeight()];
					for(int i=0; i<image.getWidth();i++){
						for(int j=0; j<image.getHeight();j++){
						    arr[i][j] = image.getRGB(i,j);
						  }
				  }
					Matrix A = new Matrix(arr);
					int pix = image.getRGB(161,120);
					System.out.println(pix);
					
					System.out.println(A.get(161,120));
				}
				catch(IOException ex){
					System.out.println("you fail");
				}
			graphics2D = (Graphics2D)image.getGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			//clear();
		//System.out.println("image is null");
		}
		
		//graphics2D = (Graphics2D)image.getGraphics();
		//graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//clear(); 
		g.drawImage(image, 0, 0, null);
	}
	//this is the painting bit
	//if it has nothing on it then
	//it creates an image the size of the window
	//sets the value of Graphics as the image
	//sets the rendering
	//runs the clear() method
	//then it draws the image


	public void clear(){
		graphics2D.setPaint(Color.white);
		graphics2D.fillRect(0, 0, getSize().width, getSize().height);
		graphics2D.setPaint(Color.black);
		repaint();
	}
	
	public BufferedImage getCurrentScreen(){
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(),BufferedImage.TYPE_INT_RGB);
		this.paint(img.getGraphics());
		return img;
	}
	
	//this is the clear
	//it sets the colors as white
	//then it fills the window with white
	//thin it sets the color back to black
	public void red(){
		graphics2D.setPaint(Color.red);
		
		//System.out.println(graphics2D.getPaint());
		repaint();
	}
	//this is the red paint
	public void black(){
		graphics2D.setPaint(Color.black);
		repaint();
	}
	//black paint
	public void magenta(){
		graphics2D.setPaint(Color.magenta);
		repaint();
	}
	//magenta paint
	public void blue(){
		graphics2D.setPaint(Color.blue);
		repaint();
	}
	//blue paint
	public void green(){
		graphics2D.setPaint(Color.green);
		repaint();
	}
	//green paint
	
	public void setScribbled(BufferedImage img){
		this.scribbled = img;
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	public BufferedImage getScribbled(){
		return scribbled;
	}
	
	public BufferedImage getOriginal(){
		return original;
	}
	
	static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

}