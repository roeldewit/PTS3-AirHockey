package Airhockey.Renderer;

import Airhockey.Elements.TriangleLine;

/**
 *
 * @author Sam
 */
public class RendererUtilities {

    private final int bottomLeftX;
    private final int bottomRightX;

    private final int centerTopY;
    private final int centerBottomY;

    private final int centerLineHeight;
    private final int baseLineWidth;

    public RendererUtilities(TriangleLine triangleLine) {
        centerTopY = triangleLine.getCenterTopY();
        centerBottomY = triangleLine.getBottomLeftY();

        bottomLeftX = triangleLine.getBottomLeftX();
        bottomRightX = triangleLine.getBottomRightX();

        centerLineHeight = centerBottomY - centerTopY;
        baseLineWidth = bottomRightX - bottomLeftX;
    }

    protected int batPositionSideToBottom(int positionY) {
        System.out.println("POS: " + positionY);

        double i = (100.0 / (double) centerLineHeight) * (double) (positionY - centerTopY);
        double l = (((double) baseLineWidth / 100.0) * i) + bottomLeftX;

        System.out.println("I: " + i);
        System.out.println("L: " + l);
        return (int) Math.floor(l);
    }
}
