package Proj3;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

public class BilinearInterpolationParallel {
    /* gets the 'n'th byte of a 4-byte integer */
    private static int get(int self, int n) {
        return (self >> (n * 8)) & 0xFF;
    }

    private static float lerp(float s, float e, float t) {
        return s + (e - s) * t;
    }

    private static float blerp(final Float c00, float c10, float c01, float c11, float tx, float ty) {
        return lerp(lerp(c00, c10, tx), lerp(c01, c11, tx), ty);
    }

    private static BufferedImage scale(BufferedImage self, float scaleX, float scaleY) {
        int newWidth = (int) (self.getWidth() * scaleX);
        int newHeight = (int) (self.getHeight() * scaleY);
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, self.getType());

        IntStream.range(0, newWidth).parallel().forEach(x -> {

            IntStream.range(0, newHeight).parallel().forEach(y -> {

                float gx = ((float) x) / newWidth * (self.getWidth() - 1);
                float gy = ((float) y) / newHeight * (self.getHeight() - 1);
                int gxi = (int) gx;
                int gyi = (int) gy;
                int c00 = self.getRGB(gxi, gyi);
                int c10 = self.getRGB(gxi + 1, gyi);
                int c01 = self.getRGB(gxi, gyi + 1);
                int c11 = self.getRGB(gxi + 1, gyi + 1);

                int rgb = IntStream.rangeClosed(0, 2).map(i -> {
                    float b00 = get(c00, i);
                    float b10 = get(c10, i);
                    float b01 = get(c01, i);
                    float b11 = get(c11, i);
                    int ble = ((int) blerp(b00, b10, b01, b11, gx - gxi, gy - gyi)) << (8 * i);
                    return ble;
                }).reduce(0, (a, b) -> a | b);

                newImage.setRGB(x, y, rgb);
            });
        });

        return newImage;
    }

    public static void main(String[] args) throws IOException {
        
        File img = new File("a.jpg");
        BufferedImage image = ImageIO.read(img);



        long startTime = System.nanoTime();
        BufferedImage image2 = scale(image, 1.6f, 1.6f);

        long endTime = System.nanoTime();

        long duration = (endTime - startTime);

        File img2 = new File("a_larger.jpg");
        ImageIO.write(image2, "jpg", img2);

      

        System.out.println(duration + " milliseconds (parallel)");
    }
}
