import java.awt.Color;


public class SeamCarver {

	private Picture picture;
	private final int BORDER_ENERGY = 3 * 255 * 255;
	private double[][] energyGrid;
	private boolean transposed = false;
	
	public SeamCarver(Picture picture) {
		this.picture = picture;
		//energyGrid = new double[width()][height()];
		computeEnergyGrid();
	}
   
	public Picture picture() {                      // current picture
		return this.picture;
	}
	
	public int width() {                        // width of current picture
		return picture.width();
	}
   
	public int height() {                        // height of current picture
		return picture.height();
	}
	
	private int widthT() {                        // width of current picture
		if(transposed)
			return picture.height();
		return picture.width();
	}
   
	private int heightT() {                        // height of current picture
		if(transposed)
			return picture.width();
		return picture.height();
	}
   
	private void computeEnergyGrid() {
		energyGrid = new double[width()][height()];
		for (int i = 0; i < width(); i++)
			for (int j = 0; j < height(); j++)
				energyGrid[i][j] = cumputeEnergy(i, j);
	}
	
	
	private double cumputeEnergy(int x, int y) {
		if ( x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
			return BORDER_ENERGY;
		
		Color[] xyNearColor = {picture.get(x-1, y), picture.get(x + 1, y), 
				picture.get(x, y - 1), picture.get(x, y + 1)};
		
		double xGradSquare = computeSquareDiff(xyNearColor[0], xyNearColor[1]);
		double yGradSquare = computeSquareDiff(xyNearColor[2], xyNearColor[3]);
		
		return xGradSquare + yGradSquare;
	}
	
	private double getFromEnergyGrid(int x, int y) {
		if (transposed)
			return energyGrid[y][x];
		else
			return energyGrid[x][y];
	}
	
	public double energy(int x, int y) {           // energy of pixel at column x and row y
		if ( x < 0 || x > width() - 1 || y < 0 || y > height() - 1)
		throw new java.lang.IndexOutOfBoundsException();
		
		return getFromEnergyGrid(x, y);
		
		
	}
   
	private double computeSquareDiff(Color c1, Color c2) {
		double rDiff = c1.getRed() - c2.getRed();
		double gDiff = c1.getGreen() - c2.getGreen();
		double bDiff = c1.getBlue() - c2.getBlue();
		return rDiff*rDiff + gDiff*gDiff + bDiff*bDiff; 
	}
	
	private int getMinIndexOfThree(double v1, double v2, double v3) {
		int minInd = 1;
		double minVal = v3;
		if (v2 < minVal) {
			minInd = 0;
			minVal = v2;
		}
		if (v1 < minVal) {
			minInd = -1;
			minVal = v1;
		}
		return minInd;
		
	}
	
	public int[] findHorizontalSeam() {           // sequence of indices for horizontal seam
	  //Picture oldPic = picture;
		/*
	  tranposePic();
	  int[] ret = findVerticalSeam();
	  tranposePic();
	  return ret;
	  */
	  double[] prevRow = new double[height()];
	  double[] newRow = new double[height()];
	  int[] ret = new int[width()];
				
	  int pathInd =  -1;
	  double minLastRowEnergy = BORDER_ENERGY * width();
	  int minIndex = 0;
	  int[][] pathInfo = new int[height()][width()];
	  
	  for (int j = 1; j <= width() - 1; j++) {
		  prevRow[0] = prevRow[height() - 1] = BORDER_ENERGY * j;
		  for(int k = 1; k < height() - 1; k++) {
			  prevRow[k] = newRow[k];
		  }
		  
		  for (int i = 1; i < height() - 1; i++) {
			  pathInd = getMinIndexOfThree(prevRow[i - 1], 
					  prevRow[i], 
					  prevRow[i + 1]);
			  //System.out.printf("i= %d , j = %d, picI = %d, picJ = %d, picI_t = %d, picJ_t = %d\n", 
			  //	i, j, width(), height(),widthT() , heightT());
			  newRow[i] = getFromEnergyGrid(j, i) + prevRow[i + pathInd];
			  pathInfo[i][j] = pathInd;
		  }
	  }
	  
	  //get min last row
	  for (int k = 1; k < height() - 1; k++) {
		  if (newRow[k] < minLastRowEnergy) { minIndex = k; minLastRowEnergy =  newRow[k];}
	  }
	  int currInd = minIndex;
	  for(int k = width() - 1; k > 0; k--) {
		  ret[k] = currInd;
		  currInd = currInd + pathInfo[currInd][k];
	  }
	  ret[0] = currInd;
	  return ret;
	  
	}
   
	public int[] findVerticalSeam() {              // sequence of indices for vertical seam
		//double[][] energyTable = new double[width()][height()];
		
		double[] prevRow = new double[width()];
		double[] newRow = new double[width()];
		int[] ret = new int[height()];
				
		int pathInd =  -1;
		double minLastRowEnergy = BORDER_ENERGY * height();
		int minIndex = 0;
		int[][] pathInfo = new int[width()][height()];
		
		for (int j = 1; j <= height() - 1; j++) {
			prevRow[0] = prevRow[width() - 1] = BORDER_ENERGY * j;
			for(int k = 1; k < width() - 1; k++) {
				prevRow[k] = newRow[k];
			}
			for (int i = 1; i < width() - 1; i++) {
				pathInd = getMinIndexOfThree(prevRow[i - 1], 
						prevRow[i], 
						prevRow[i + 1]);
				//System.out.printf("i= %d , j = %d, picI = %d, picJ = %d, picI_t = %d, picJ_t = %d\n", 
				//		i, j, width(), height(),widthT() , heightT());
				newRow[i] = getFromEnergyGrid(i, j) + prevRow[i + pathInd];
				pathInfo[i][j] = pathInd;
			}
		}
		/*
		System.out.println("newRow");
		for (int i = 0; i < newRow.length; i++)
            System.out.printf("%9.0f ", newRow[i]);
		System.out.println("\npathInfo:\n");
		for (int j = 0; j < height(); j++)
        {
            for (int i = 0; i < width(); i++)
                System.out.printf("%d ", pathInfo[i][j]);

            System.out.println();
        }
		*/
		
		//get min last row
		for (int k = 1; k < width() - 1; k++) {
			if (newRow[k] < minLastRowEnergy) { minIndex = k; minLastRowEnergy =  newRow[k];}
		}
		int currInd = minIndex;
		for(int k = height() - 1; k > 0; k--) {
			ret[k] = currInd;
			currInd = currInd + pathInfo[currInd][k];
		}
		ret[0] = currInd;
		
		
		return ret;
	}
	
	
	private Color getColor(int x, int y) {
		if (transposed)
			return picture.get(y, x);
		return picture.get(x, y);
	}
	
	private void tranposePic() {
		/*
		Picture transPic = new Picture(height(), width());
		//System.out.printf("image is %d columns by %d rows before transpose\n", picture.width(), picture.height());
		for (int i = 0; i < width(); i++) {
			for(int j = 0; j < height(); j++) {
				transPic.set(j, i, picture.get(i, j));
			}
		}
		picture = transPic;
		*/
		transposed = !transposed;
		//computeEnergyGrid();
		//System.out.printf("image is %d columns by %d rows after transpose\n", picture.width(), picture.height());
	}
   
	public void removeHorizontalSeam(int[] a) {  // remove horizontal seam from picture
		/*
		System.out.printf("w = %d a.l = %d \n", width(), a.length);
		if (width() <= 1 || a.length != width())
			throw new java.lang.IllegalArgumentException();
		tranposePic();
		removeVerticalSeam(a);
		tranposePic();
		*/
		
		// ------
		
		if (width() <= 1 || a.length != width())
			throw new java.lang.IllegalArgumentException();
		
		for (int i = 1; i < a.length; i++)
			if (Math.abs(a[i - 1] - a[i]) > 1)
				throw new java.lang.IllegalArgumentException();
		
		Picture nPic = new Picture(width(), height() - 1);
		int newH = 0;
		double[][] newEnergyGrid = new double[width()][height() - 1];
		
		for(int oldW = 0; oldW < width(); oldW++) {
			newH = 0;
			for(int oldH = 0; oldH < height(); oldH++) {
				if (oldH == a[oldW]) // skip
					continue;
				//nPic.set(newW, oldH, picture.get(oldW, oldH));
				nPic.set(oldW, newH, getColor(oldW, oldH));
				//newEnergyGrid[newW][oldH] = getFromEnergyGrid(newW, oldH);//   energyGrid[newW][oldH];
				newH++;
			}
		}
		picture = nPic;
		computeEnergyGrid();
	}
   
	public void removeVerticalSeam(int[] a) {    // remove vertical seam from picture
		// |
		// |
		if (height() <= 1 || a.length != height())
			throw new java.lang.IllegalArgumentException();
		
		for (int i = 1; i < a.length; i++)
			if (Math.abs(a[i - 1] - a[i]) > 1)
				throw new java.lang.IllegalArgumentException();
		
		Picture nPic = new Picture(width() - 1, height());
		int newW = 0;
		double[][] newEnergyGrid = new double[width() - 1][height()];
		
		for(int oldH = 0; oldH < height(); oldH++) {
			newW = 0;
			for(int oldW = 0; oldW < width(); oldW++) {
				if (oldW == a[oldH]) // skip
					continue;
				//nPic.set(newW, oldH, picture.get(oldW, oldH));
				nPic.set(newW, oldH, getColor(oldW, oldH));
				//newEnergyGrid[newW][oldH] = getFromEnergyGrid(newW, oldH);//   energyGrid[newW][oldH];
				newW++;
			}
		}
		picture = nPic;
		//energyGrid = newEnergyGrid;
		computeEnergyGrid();
	}
	
}
