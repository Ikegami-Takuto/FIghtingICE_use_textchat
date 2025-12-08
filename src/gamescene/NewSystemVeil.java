package gamescene;

//import org.lwjgl.util.Color;

import image.Image;
import loader.ResourceLoader;
import manager.GraphicManager;
import setting.GameSetting;
import java.util.*;

import fighting.Character;
import image.Image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Area;

import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class NewSystemVeil {
    public static volatile float x = 0.2f;//次に明るくするy座標
    public static volatile float y = 0.7f;//次に明るくするx座標
    private Area drawArea = new Area(); //描画するエリア（明るくなっているエリア）
    private int diameter = 200;

    private List<Float> xList = new ArrayList<>();
    private List<Float> yList = new ArrayList<>();
    private final int SIZE = 500;

    public static void setCoordinates(float x1, float y1) {
        x = x1;
        y = y1;
    }

    public void setArea(){
        // String[] axis = command.split(",");
        // axis[0] = axis[0].split("=")[1];
        // axis[1] = axis[1].split("=")[1];
        // x = Integer.parseInt(axis[0]);
        // y = Integer.parseInt(axis[1]);
        int diameter = 100;

        // xList.add(x);
        // yList.add(y);
        // if (xList.size() > SIZE) {
        //     xList.remove(0);
        //     yList.remove(0);
        // }
        // int area_x = (int)(calcAverage(xList)*GameSetting.STAGE_WIDTH);
        // int area_y = (int)(calcAverage(yList)*GameSetting.STAGE_WIDTH);
        this.drawArea.add(new Area(new Ellipse2D.Float(GameSetting.STAGE_WIDTH*x, GameSetting.STAGE_HEIGHT*y, diameter, diameter)));
    }

    private float calcAverage(List<Float> list) {
        float result = 0;
        for (float l : list) {
            result += l;
        }

        return (result/list.size());
    }

    public Area initLightArea(int time, Character characters) {
        Area area = new Area();

        int maxDiameter = 300;
		int interval = 500;
        if (time > 60000 - maxDiameter - interval){
			this.diameter = (int)(time - 60000 + maxDiameter + interval);
			if (this.diameter > maxDiameter) { 
				this.diameter = maxDiameter;
			}
		}

        int cx = (characters.getHitAreaCenterX())-this.diameter/2;
		int cy = (characters.getHitAreaCenterY())-this.diameter/2;

        area.add(new Area(new Ellipse2D.Float(cx, cy, this.diameter, this.diameter)));

        return area;
    }

    public Image drawVeilImage(int time, BufferedImage backImage, boolean shouldRender) {
        BufferedImage image = new BufferedImage(GameSetting.STAGE_WIDTH, GameSetting.STAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();

		g2d.setClip(this.drawArea);
		g2d.drawImage(backImage, 0, 0, null);

		Image veilImage = ResourceLoader.getInstance().loadTextureFromBufferedImage(image);
		//GraphicManager.getInstance().drawImage(veilImage, 0, 0, Image.DIRECTION_RIGHT, shouldRender);

        return veilImage;

	}
}
