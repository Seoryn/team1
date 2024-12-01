package model;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontManager {
    private static Font customFont;

    static {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\main\\resources\\DungGeunMo.ttf")); // 폰트 파일 로드
        } catch (FontFormatException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }

    /**
     * 원하는 크기의 폰트를 반환
     * 
     * @param size 폰트 크기
     * @return 설정된 폰트
     */
    public static Font getFont(float size) {
        return customFont.deriveFont(size);
    }
}
