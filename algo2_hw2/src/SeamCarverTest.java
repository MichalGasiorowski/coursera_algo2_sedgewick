import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class SeamCarverTest {

@Test
public void testConstructor() {
	Picture pic = new Picture("data/seamCarving/10x12.png");
	int width = pic.width();
	int height = pic.height();
	
	SeamCarver seamCarver = new SeamCarver(pic);
	
	assertEquals(12, seamCarver.height());
	assertEquals(10, seamCarver.width());
	
	assertEquals(height, seamCarver.height());
	assertEquals(width, seamCarver.width());
	
	assertEquals(pic, seamCarver.picture());
}


@Test
public void testEnergySum() {
	Picture pic = new Picture("data/seamCarving/10x12.png");
	
	for (int x = 0; x < 100000; x++) {
	System.out.println(x);
	SeamCarver seamCarver = new SeamCarver(pic);
	int[] actual = seamCarver.findHorizontalSeam();
	double sum = 0;
	for (int i = 0; i < seamCarver.width(); i++) {
	sum += seamCarver.energy(i, actual[i]);
	}
	assertEquals(644531.0, sum, 0.0001);
	}

}




@Test
public void testSeamRemovalHJOceanHorizontalTiming() {
	Picture pic = new Picture("data/seamCarving/HJocean.png");
	SeamCarver seamCarver = new SeamCarver(pic);
	
	long startTotal = System.currentTimeMillis();
	long start = 0;
	long end = 0;
	long runningTime = 0;
	for (int i = 0; i < 100; i++) {
	System.out.println("Running "+ i);
	start = System.currentTimeMillis();
	int[] seam = seamCarver.findHorizontalSeam();
	end = System.currentTimeMillis();
	runningTime = end - start;
	System.out.println("took: "+runningTime+" ms - findHorizontalSeam");
	start = System.currentTimeMillis();
	seamCarver.removeHorizontalSeam(seam);
	end = System.currentTimeMillis();
	runningTime = end - start;
	System.out.println("took: "+runningTime+" ms - removeHorizontalSeam");
	start = System.currentTimeMillis();
	int[] seamForVertical = seamCarver.findVerticalSeam();
	end = System.currentTimeMillis();
	runningTime = end - start;
	System.out.println("took: "+runningTime+" ms - findVerticalSeam");
	start = System.currentTimeMillis();
	seamCarver.removeVerticalSeam(seamForVertical);
	end = System.currentTimeMillis();
	runningTime = end - start;
	System.out.println("took: "+runningTime+" ms - removeVerticalSeam");
	}
	runningTime = end - startTotal;
	System.out.println("took: "+runningTime+" ms");
	assertTrue(runningTime < 20000);
}

@Test
public void testSeamRemoval6x5Horizontal() {
	Picture pic = new Picture("data/seamCarving/6x5.png");
	
	SeamCarver seamCarver = new SeamCarver(pic);
	int[] seam = seamCarver.findHorizontalSeam();
	seamCarver.removeHorizontalSeam(seam);
	
	printEnergies(seamCarver);
	
	Picture picOutput = seamCarver.picture();
	
	printColors(picOutput);
	
	assertEquals(4, seamCarver.height());
	assertEquals(6, seamCarver.width());
	
	assertEquals(4, picOutput.height());
	assertEquals(6, picOutput.width());
}

@Test
public void testSeamRemoval6x5Vertical() {
	Picture pic = new Picture("data/seamCarving/6x5.png");
	
	SeamCarver seamCarver = new SeamCarver(pic);
	printEnergies(seamCarver);
	
	int[] seam = seamCarver.findVerticalSeam();
	
	PrintSeams.printVerticalSeam(seamCarver);
	
	seamCarver.removeVerticalSeam(seam);
	
	printEnergies(seamCarver);
	
	Picture picOutput = seamCarver.picture();
	
	// printColors(pic, picOutput);
	
	assertEquals(5, seamCarver.height());
	assertEquals(5, seamCarver.width());
	
	assertEquals(5, picOutput.height());
	assertEquals(5, picOutput.width());

}

@Test
public void testSeamRemoval4x6Verticatl() {
Picture pic = new Picture("data/seamCarving/4x6.png");

SeamCarver seamCarver = new SeamCarver(pic);
printEnergies(seamCarver);

int[] seam = seamCarver.findVerticalSeam();

PrintSeams.printVerticalSeam(seamCarver);

seamCarver.removeVerticalSeam(seam);

printEnergies(seamCarver);

Picture picOutput = seamCarver.picture();

// printColors(pic, picOutput);

assertEquals(6, seamCarver.height());
assertEquals(3, seamCarver.width());

assertEquals(6, picOutput.height());
assertEquals(3, picOutput.width());

}

private void printColors(Picture pic) {
for (int y = 0; y < pic.height(); y++) {
System.out.print("{");
for (int x = 0; x < pic.width(); x++) {
Color color = pic.get(x, y);
System.out.print("new Color(" +color.getRed()+ ","+color.getBlue()+","+color.getGreen()+"), ");
}
System.out.println("}, ");
}
}

@Test
public void testEnergy() {
Picture pic = get3x4Picture();
SeamCarver seamCarver = new SeamCarver(pic);

assertEquals(195075, seamCarver.energy(0, 0), 0.0001);
assertEquals(195075, seamCarver.energy(0, 1), 0.0001);
assertEquals(195075, seamCarver.energy(0, 2), 0.0001);
assertEquals(195075, seamCarver.energy(1, 0), 0.0001);
assertEquals(52225, seamCarver.energy(1, 1), 0.0001);
assertEquals(52024, seamCarver.energy(1, 2), 0.0001);
assertEquals(195075, seamCarver.energy(2, 0), 0.0001);
assertEquals(195075, seamCarver.energy(2, 1), 0.0001);
assertEquals(195075, seamCarver.energy(2, 2), 0.0001);

}

@Test
public void testHorizontalSeam() {
	Picture pic = get6x5Picture();
	SeamCarver seamCarver = new SeamCarver(pic);
	
	printEnergies(seamCarver);
	int[] expected = new int[] { 4, 3, 3, 3, 2, 3 };
	int[] actual = seamCarver.findHorizontalSeam();
	for (int i = 0; i < expected.length; i++) {
		System.out.printf("expected: %d , actual: %d", expected[i], actual[i]);
		System.out.println();
		//assertEquals(expected[i], actual[i]);
	}

}

private void printEnergies(SeamCarver seamCarver) {
	int counter = 0;
	for (int j = 0; j < seamCarver.height(); j++) {
		for (int i = 0; i < seamCarver.width(); i++) {
			System.out.printf(i + ":" + j + "(" + (counter++) + ")=>"
					+ "%9.0f ", seamCarver.energy(i, j));
		}
		System.out.println();
	}
}

@Test
public void testVerticalSeam() {
	Picture pic = get6x5Picture();
	SeamCarver seamCarver = new SeamCarver(pic);
	printEnergies(seamCarver);
	int[] expected = new int[] { 4, 3, 3, 3, 4 };
	int[] actual = seamCarver.findVerticalSeam();
	for (int i = 0; i < expected.length; i++) {
	assertEquals(expected[i], actual[i]);
	}

}

@Test
public void testEnergyTable() {
	Picture pic = get6x5Picture();
	SeamCarver seamCarver = new SeamCarver(pic);
	
	printEnergies(seamCarver);
	
	assertEquals(195075.0, seamCarver.energy(0, 0), 0.0001);
	assertEquals(195075.0, seamCarver.energy(1, 0), 0.0001);
	assertEquals(195075.0, seamCarver.energy(2, 0), 0.0001);
	assertEquals(195075.0, seamCarver.energy(3, 0), 0.0001);
	assertEquals(195075.0, seamCarver.energy(4, 0), 0.0001);
	assertEquals(195075.0, seamCarver.energy(5, 0), 0.0001);
	assertEquals(195075.0, seamCarver.energy(0, 1), 0.0001);
	assertEquals(23346.0, seamCarver.energy(1, 1), 0.0001);
	assertEquals(51304.0, seamCarver.energy(2, 1), 0.0001);
	assertEquals(31519.0, seamCarver.energy(3, 1), 0.0001);
	assertEquals(55112.0, seamCarver.energy(4, 1), 0.0001);
	assertEquals(195075.0, seamCarver.energy(5, 1), 0.0001);
	assertEquals(195075.0, seamCarver.energy(0, 2), 0.0001);
	assertEquals(47908.0, seamCarver.energy(1, 2), 0.0001);
	assertEquals(61346.0, seamCarver.energy(2, 2), 0.0001);
	assertEquals(35919.0, seamCarver.energy(3, 2), 0.0001);
	assertEquals(38887.0, seamCarver.energy(4, 2), 0.0001);
	assertEquals(195075.0, seamCarver.energy(5, 2), 0.0001);
	assertEquals(195075.0, seamCarver.energy(0, 3), 0.0001);
	assertEquals(31400.0, seamCarver.energy(1, 3), 0.0001);
	assertEquals(37927.0, seamCarver.energy(2, 3), 0.0001);
	assertEquals(14437.0, seamCarver.energy(3, 3), 0.0001);
	assertEquals(63076.0, seamCarver.energy(4, 3), 0.0001);
	assertEquals(195075.0, seamCarver.energy(5, 3), 0.0001);
	assertEquals(195075.0, seamCarver.energy(0, 4), 0.0001);
	assertEquals(195075.0, seamCarver.energy(1, 4), 0.0001);
	assertEquals(195075.0, seamCarver.energy(2, 4), 0.0001);
	assertEquals(195075.0, seamCarver.energy(3, 4), 0.0001);
	assertEquals(195075.0, seamCarver.energy(4, 4), 0.0001);
	assertEquals(195075.0, seamCarver.energy(5, 4), 0.0001);

}

@Test(expected = IndexOutOfBoundsException.class)
public void testOutOfBounds1() throws Exception {
	Picture pic = get3x4Picture();
	SeamCarver seamCarver = new SeamCarver(pic);
	seamCarver.energy(-1, 0);
}

@Test(expected = IndexOutOfBoundsException.class)
public void testOutOfBounds2() throws Exception {
	Picture pic = get3x4Picture();
	SeamCarver seamCarver = new SeamCarver(pic);
	seamCarver.energy(0, -1);
}

@Test(expected = IndexOutOfBoundsException.class)
public void testOutOfBounds3() throws Exception {
Picture pic = get3x4Picture();
SeamCarver seamCarver = new SeamCarver(pic);
seamCarver.energy(0, 4);
}

@Test(expected = IndexOutOfBoundsException.class)
public void testOutOfBounds4() throws Exception {
Picture pic = get3x4Picture();
SeamCarver seamCarver = new SeamCarver(pic);
seamCarver.energy(3, 0);
}

@Test(expected = IllegalArgumentException.class)
public void testWrongLength() throws Exception {
Picture pic = get3x4Picture();
SeamCarver seamCarver = new SeamCarver(pic);
seamCarver.removeHorizontalSeam(new int[] { 1, 1, 1, 1 });
}

@Test(expected = IllegalArgumentException.class)
public void testWrongLength2() throws Exception {
Picture pic = get3x4Picture();
SeamCarver seamCarver = new SeamCarver(pic);
seamCarver.removeHorizontalSeam(new int[] { 1, 1 });
}

@Test(expected = IllegalArgumentException.class)
public void testWrongLength3() throws Exception {
Picture pic = get3x4Picture();
SeamCarver seamCarver = new SeamCarver(pic);
seamCarver.removeVerticalSeam(new int[] { 1, 1 });
}

@Test(expected = IllegalArgumentException.class)
public void testWrongLength4() throws Exception {
Picture pic = get3x4Picture();
SeamCarver seamCarver = new SeamCarver(pic);
seamCarver.removeVerticalSeam(new int[] { 1, 1, 1, 1, 1 });
}

@Test
public void testSeamRemoval3x4Vertical() {
Picture expectedPicture = get3x4ExpectedPictureAfterVerticalSeamRemoval();
System.out.println("====Expected: ========");
printColors(expectedPicture);

Picture pic = get3x4Picture();

SeamCarver seamCarver = new SeamCarver(pic);
int[] seam = seamCarver.findVerticalSeam();
seamCarver.removeVerticalSeam(seam);

Picture actualPicture = seamCarver.picture();
System.out.println("====Actual: ========");
printColors(actualPicture);

assertEquals(4, seamCarver.height());
assertEquals(2, seamCarver.width());

assertEquals(4, actualPicture.height());
assertEquals(2, actualPicture.width());

assertEquals(expectedPicture, actualPicture);

}

@Test
public void testSeamRemoval3x4Horizontal() {
Picture expectedPicture = get3x4PictureAfterHorizontalSeamRemoval();
System.out.println("====Expected: ========");
printColors(expectedPicture);

Picture pic = get3x4Picture();

SeamCarver seamCarver = new SeamCarver(pic);
int[] seam = seamCarver.findHorizontalSeam();
seamCarver.removeHorizontalSeam(seam);

Picture actualPicture = seamCarver.picture();
System.out.println("====Actual: ========");
printColors(actualPicture);

assertEquals(3, seamCarver.height());
assertEquals(3, seamCarver.width());

assertEquals(3, actualPicture.height());
assertEquals(3, actualPicture.width());

assertEquals(expectedPicture, actualPicture);

}


@Test
public void testSeamRemovalRandomFromHorizontal() {
Picture pic = getWxHPicture(10, 10);
printColors(pic);

SeamCarver seamCarver = new SeamCarver(pic);

for(int i=0;i<5;i++){
int[] seam = seamCarver.findHorizontalSeam();
seamCarver.removeHorizontalSeam(seam);
}

}

@Test
public void testSeamRemoval10x10FromHorizontal() {
Picture pic = get10x10Picture();
printColors(pic);

SeamCarver seamCarver = new SeamCarver(pic);

for(int i=0;i<5;i++){
System.out.println("================== "+i + " h: " + seamCarver.height()+" w: " + seamCarver.width());
int[] seam = seamCarver.findHorizontalSeam();
seamCarver.removeHorizontalSeam(seam);
}

}

private Picture getWxHPicture(int w, int h) {
Picture pic = new Picture(w, h);
for (int y = 0; y < pic.height(); y++) {
for (int x = 0; x < pic.width(); x++) {
Color color = new Color(StdRandom.uniform(0, 256), StdRandom.uniform(0, 256), StdRandom.uniform(0, 256));
pic.set(x, y, color);
}
}
return pic;
}

private Picture get3x4Picture() {
Picture pic = new Picture(3, 4);
Color[][] colors = new Color[][] {
{ new Color(255, 101, 51), new Color(255, 101, 153),new Color(255, 101, 255) },
{ new Color(255, 153, 51), new Color(255, 153, 153),new Color(255, 153, 255) },
{ new Color(255, 203, 51), new Color(255, 204, 153),new Color(255, 205, 255) },
{ new Color(255, 255, 51), new Color(255, 255, 153),new Color(255, 255, 255) } };
for (int y = 0; y < pic.height(); y++) {
for (int x = 0; x < pic.width(); x++) {
pic.set(x, y, colors[y][x]);
}
}
return pic;
}

private Picture get3x4PictureAfterHorizontalSeamRemoval() {
Picture pic = new Picture(3, 3);
Color[][] colors = new Color[][] {
{ new Color(255, 101, 51), new Color(255, 101, 153),new Color(255, 101, 255) },
{ new Color(255, 153, 51), new Color(255, 153, 153),new Color(255, 153, 255) },
{ new Color(255, 203, 51), new Color(255, 255, 153),new Color(255, 205, 255) }, };
for (int y = 0; y < pic.height(); y++) {
for (int x = 0; x < pic.width(); x++) {
pic.set(x, y, colors[y][x]);
}
}
return pic;
}

private Picture get3x4ExpectedPictureAfterVerticalSeamRemoval() {
Picture pic = new Picture(2, 4);
Color[][] colors = new Color[][] {
{ new Color(255, 101, 51), new Color(255, 101, 153), },
{ new Color(255, 153, 51), new Color(255, 153, 255) },
{ new Color(255, 203, 51), new Color(255, 205, 255) },
{ new Color(255, 255, 51),new Color(255, 255, 153) , } };

for (int y = 0; y < pic.height(); y++) {
for (int x = 0; x < pic.width(); x++) {
pic.set(x, y, colors[y][x]);
}
}
return pic;
}

private Picture get6x5Picture() {
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


private Picture get10x10Picture() {
Picture pic = new Picture(10, 10);
Color[][] colors = new Color[][] {

{new Color(182,199,117), new Color(246,204,215), new Color(244,101,99), new Color(249,107,157), new Color(101,41,104), new Color(99,174,90), new Color(143,165,45), new Color(95,76,123), new Color(32,197,175), new Color(125,11,235), },
{new Color(178,52,4), new Color(0,84,215), new Color(114,212,71), new Color(128,248,141), new Color(68,248,237), new Color(145,194,228), new Color(174,176,86), new Color(204,146,200), new Color(190,255,191), new Color(45,87,200), },
{new Color(213,227,252), new Color(129,69,103), new Color(172,142,226), new Color(60,219,93), new Color(253,232,9), new Color(165,123,108), new Color(89,11,132), new Color(94,82,179), new Color(110,251,220), new Color(135,30,143), },
{new Color(224,37,213), new Color(183,219,233), new Color(186,37,227), new Color(78,170,20), new Color(149,42,112), new Color(29,152,164), new Color(200,194,75), new Color(15,23,182), new Color(108,47,105), new Color(207,93,236), },
{new Color(42,136,196), new Color(188,7,92), new Color(49,104,130), new Color(141,42,105), new Color(54,27,77), new Color(108,122,234), new Color(29,37,224), new Color(212,10,63), new Color(235,101,38), new Color(49,237,69), },
{new Color(218,233,150), new Color(235,226,245), new Color(70,198,96), new Color(207,169,175), new Color(110,167,51), new Color(253,26,253), new Color(145,92,44), new Color(243,229,168), new Color(125,229,111), new Color(44,37,73), },
{new Color(219,215,204), new Color(132,57,145), new Color(97,27,92), new Color(179,229,242), new Color(195,99,169), new Color(114,48,30), new Color(126,217,92), new Color(213,78,155), new Color(234,107,77), new Color(212,122,215), },
{new Color(107,224,11), new Color(138,187,49), new Color(166,141,30), new Color(81,65,70), new Color(41,242,11), new Color(173,163,122), new Color(152,76,6), new Color(82,98,55), new Color(161,68,29), new Color(20,124,131), },
{new Color(97,244,67), new Color(118,24,227), new Color(142,18,168), new Color(245,7,10), new Color(90,206,220), new Color(3,237,135), new Color(145,236,102), new Color(111,142,167), new Color(246,23,46), new Color(66,106,138), },
{new Color(32,0,153), new Color(212,78,43), new Color(122,64,218), new Color(16,190,224), new Color(190,177,146), new Color(32,229,64), new Color(231,66,103), new Color(200,229,40), new Color(0,185,113), new Color(0,79,98), },

};

for (int y = 0; y < pic.height(); y++) {
for (int x = 0; x < pic.width(); x++) {
pic.set(x, y, colors[y][x]);
}
}
return pic;
}

}