package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Rect;
import misc.*;

import java.util.Objects;

import static app.Colors.CROSSED_COLOR;

public class Segment {
    /**
     * Координаты конца 1
     */

    public Vector2d pos1;

    /**
     * Координаты конца 2
     */
    public Vector2d pos2;

    /**
     * Конструктор отрезка
     *
     * @param pos1 конец 1
     * @param pos2 конец 2
     */
    @JsonCreator
    public Segment(@JsonProperty("pos1") Vector2d pos1, @JsonProperty("pos2") Vector2d pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }


    /**
     * Конструктор отрезка
     *
     * @param pos2 конец 2
     */
    public Segment(Vector2d pos2) {
        this(new Vector2d(0, 0), pos2);
    }


    /**
     * Получить конец 1
     *
     * @return конец 1
     */
    public Vector2d getPos1() {
        return pos1;
    }


    /**
     * Получить конец 2
     *
     * @return конец 2
     */
    public Vector2d getPos2() {
        return pos2;
    }


    /**
     * Получить длину отрезка
     *
     * @return положение
     */
    public double getLength() {
        Vector2d tmp = Vector2d.subtract(pos1, pos2);
        return tmp.length();
    }


    /**
     * Получить хэш-код объекта
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(pos1, pos2);
    }

    public void paint(Canvas canvas, CoordinateSystem2i windowCS, CoordinateSystem2d ownCS) {
        try (var p = new Paint()) {
            p.setColor(CROSSED_COLOR);
            Vector2i posA = windowCS.getCoords(pos1, ownCS);
            Vector2i posB = windowCS.getCoords(pos2, ownCS);  // рисуем линию
            canvas.drawLine(posA.x, posA.y, posB.x, posB.y, p);
            p.setColor(Misc.getColor(200, 200, 100, 50));
            canvas.drawRect(Rect.makeXYWH(posA.x - 3, posA.y - 3, 6, 6), p);
            canvas.drawRect(Rect.makeXYWH(posB.x - 3, posB.y - 3, 6, 6), p);

        }
    }
}
