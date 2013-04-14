import java.awt.Color;

/*************************************************************************
 *  Compilation:  javac PrintSeams.java
 *  Execution:    java PrintSeams input.png
 *  Dependencies: SeamCarver.java SCUtility.java Picture.java StdDraw.java
 *                
 *
 *  Read image from file specified as command line argument. Print energy
 *  of each image, as well as both seams, and the total energy of each seam.
 *
 *************************************************************************/

public class PrintSeams {

    private static void printHorizontalSeam(SeamCarver sc)
    {        
        double totalSeamEnergy = 0;

        int[] horizontalSeam = sc.findHorizontalSeam();
        for (int j = 0; j < sc.height(); j++)
        {
            for (int i = 0; i < sc.width(); i++)            
            {
                char lMarker = ' ';
                char rMarker = ' ';
                if (j == horizontalSeam[i])
                {
                    lMarker = '[';
                    rMarker = ']';
                    totalSeamEnergy += sc.energy(i, j);
                }

                System.out.printf("%c%6.0f%c ", lMarker, sc.energy(i, j), rMarker);
            }
            System.out.println();
        }                
        
        System.out.printf("\nTotal energy: %.0f\n\n", totalSeamEnergy);
    }


    public static void printVerticalSeam(SeamCarver sc)
    {
        double totalSeamEnergy = 0;

        int[] verticalSeam = sc.findVerticalSeam();
        for (int j = 0; j < sc.height(); j++)
        {
            for (int i = 0; i < sc.width(); i++)            
            {
                char lMarker = ' ';
                char rMarker = ' ';
                if (i == verticalSeam[j])
                {
                    lMarker = '[';
                    rMarker = ']';
                    totalSeamEnergy += sc.energy(i, j);
                }

                System.out.printf("%c%6.0f%c ", lMarker, sc.energy(i, j), rMarker);
            }

            System.out.println();
        }                
        
        System.out.printf("\nTotal energy: %.0f\n\n", totalSeamEnergy);
    }
    
    private static Picture get6x5Picture() {
    	Picture pic = new Picture(6, 5);
    	Color[][] colors = new Color[][] {
    	{ new Color(97, 82, 107), new Color(220, 172, 141),
    	new Color(243, 71, 205), new Color(129, 173, 222),
    	new Color(225, 40, 209), new Color(66, 109, 219) },
    	{ new Color(181, 78, 68), new Color(15, 28, 216),
    	new Color(245, 150, 150), new Color(177, 100, 167),
    	new Color(205, 205, 177), new Color(147, 58, 99) },
    	{ new Color(196, 224, 21), new Color(166, 217, 190),
    	new Color(128, 120, 162), new Color(104, 59, 110),
    	new Color(49, 148, 137), new Color(192, 101, 89) },
    	{ new Color(83, 143, 103), new Color(110, 79, 247),
    	new Color(106, 71, 174), new Color(92, 240, 205),
    	new Color(129, 56, 146), new Color(121, 111, 147) },
    	{ new Color(82, 157, 137), new Color(92, 110, 129),
    	new Color(183, 107, 80), new Color(89, 24, 217),
    	new Color(207, 69, 32), new Color(156, 112, 31) } };
    	
    	for (int y = 0; y < pic.height(); y++) {
    	for (int x = 0; x < pic.width(); x++) {
    	pic.set(x, y, colors[y][x]);
    	}
    	}
    	return pic;
    }

    public static void main(String[] args)
    {
        //Picture inputImg = new Picture(args[0]);
    	//Picture inputImg = new Picture("data/SeamCarving/3x4.png");
    	Picture inputImg = get6x5Picture();
        System.out.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
        //inputImg.show();        
        SeamCarver sc = new SeamCarver(inputImg);
        
        System.out.printf("Displaying horizontal seam calculated.\n");
        printHorizontalSeam(sc);

        System.out.printf("Displaying vertical seam calculated.\n");
        printVerticalSeam(sc);


    }

}