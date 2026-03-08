package image;
import java.awt.*;


/**
 * A utility class for image processing operations on ASCII art generation.
 * This class handles image padding to power-of-two dimensions, splitting into sub-images,
 * and calculating brightness values for ASCII art conversion.
 * @author Tomer Kadosh
 * @see Image
 */
public class ImagePrepration {
    /** The source image to be processed */
    Image image;

    /** The number of columns to split the image into */
    int resolution;

    /**
     * Constructs an ImagePrepration object with the specified image and resolution.
     *
     * @param image The source image to be processed
     * @param resolution The number of columns to split the image into
     */
    public ImagePrepration(Image image, int resolution){
        this.image = image;
        this.resolution = resolution;
    }

    /**
     * Pads the image to dimensions that are powers of two by adding white space.
     * The original image is centered within the padded dimensions.
     */
    private void imagePadding(){
        int orignalWidth = image.getWidth();
        int orignalHigt = image.getHeight();
        int newWidth = orignalWidth;
        int newHight = orignalHigt;

        // Adjust dimensions to nearest power of two if needed
        if(!isPowerOfTwo(orignalWidth)){
            newWidth = getPowerOfTwo(orignalWidth);
        }
        if (!isPowerOfTwo(orignalWidth)){
            newHight = getPowerOfTwo(orignalHigt);
        }

        int widthDiff = newWidth - orignalWidth;
        int hightDiff = newHight - orignalHigt;

        // Initialize padded image with white background
        Color[][] paddedImage = new Color[newHight][newWidth];
        for (int x = 0; x < newHight; x++) {
            for (int y = 0; y < newWidth; y++) {
                paddedImage[x][y] = Color.WHITE;
            }
        }

        // Copy original image centered in padded area
        for (int i = 0; i < orignalHigt; i++) {
            for (int j = 0; j < orignalWidth; j++) {
                paddedImage[i + hightDiff/2][j + widthDiff/2] = image.getPixel(i, j);
            }
        }

        image = new Image(paddedImage, newWidth, newHight);
    }

    /**
     * Checks if a number is a power of two using bitwise operations.
     *
     * @param n Number to check
     * @return true if n is a power of two, false otherwise
     */
    private boolean isPowerOfTwo(int n){
        return n > 0 && (n & (n - 1)) == 0;
    }

    /**
     * Calculates the next power of two greater than or equal to the input number.
     *
     * @param n Input number
     * @return The next power of two
     */
    private int getPowerOfTwo(int n){
        int exp = (int) Math.ceil(Math.log(n) / Math.log(2));
        return (int) Math.pow(2, exp);
    }

    /**
     * Splits the image into a grid of equal-sized square sub-images.
     * The image width and height must be evenly divisible by the block size.
     *
     * @return 2D array of sub-images
     * @throws IllegalArgumentException if image dimensions are not divisible by the block size
     */
    private Image[][] splitToSquareSubImages() {
        int imageW = image.getWidth();
        int imageH = image.getHeight();
        int blockSize = imageW / resolution;

        if (imageW % blockSize != 0 || imageH % blockSize != 0) {
            throw new IllegalArgumentException("Image dimensions must be divisible by block size");
        }

        int rows = imageH / blockSize;
        int cols = resolution;
        Image[][] subImages = new Image[rows][cols];

        // Create sub-images by copying pixel blocks
        for (int blockRow = 0; blockRow < rows; blockRow++) {
            for (int blockCol = 0; blockCol < cols; blockCol++) {
                Color[][] blockPixels = new Color[blockSize][blockSize];
                for (int i = 0; i < blockSize; i++) {
                    for (int j = 0; j < blockSize; j++) {
                        int srcRow = blockRow * blockSize + i;
                        int srcCol = blockCol * blockSize + j;
                        blockPixels[i][j] = image.getPixel(srcRow, srcCol);
                    }
                }
                subImages[blockRow][blockCol] = new Image(blockPixels, blockSize, blockSize);
            }
        }
        return subImages;
    }

    /**
     * Calculates the average brightness of an image using the luminance formula:
     * Y = 0.2126R + 0.7152G + 0.0722B
     *
     * @param image The image to analyze
     * @return Normalized brightness value between 0 and 1
     */
    private double calculateImageBrightness(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        double totalBrightness = 0;

        // Sum weighted RGB values for each pixel
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color pixelColor = image.getPixel(x, y);
                double greyPixel = (pixelColor.getRed()*0.2126 +
                        pixelColor.getGreen()*0.7152 +
                        pixelColor.getBlue()*0.0722);
                totalBrightness += greyPixel;
            }
        }

        // Normalize to [0,1] range
        return (totalBrightness / (width * height)) / 255.0;
    }

    /**
     * Main processing method that calculates brightness values for all sub-images.
     * The process includes padding the image, splitting it into sub-images, and
     * calculating brightness for each sub-image.
     *
     * @param image The image to analyze
     * @param resolution Number of columns to split the image into
     * @return 2D array of brightness values for each sub-image
     */
    public double[][] calculateBrightnessForALL(Image image, int resolution){
        imagePadding();
        Image[][] subImages = splitToSquareSubImages();
        double[][] brightnessValues = new double[subImages.length][subImages[0].length];

        // Calculate brightness for each sub-image
        for (int blockRow = 0; blockRow < subImages.length; blockRow++) {
            for (int blockCol = 0; blockCol < subImages[0].length; blockCol++) {
                brightnessValues[blockRow][blockCol] =
                        calculateImageBrightness(subImages[blockRow][blockCol]);
            }
        }
        return brightnessValues;
    }
}