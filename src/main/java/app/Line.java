package app;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.RRect;
import io.github.humbleui.skija.Rect;
import misc.CoordinateSystem2i;
import misc.Misc;
import misc.Vector2d;
import misc.Vector2i;

public class Line {
    public Vector2d a;
    public Vector2d b;

    Task task;

    public Line(Vector2d a, Vector2d b, Task task) {
        this.a = a;
        this.b = b;
        this.task = task;
    }


    void renderLine(Canvas canvas, CoordinateSystem2i windowCS) {
        // опорные точки линии
        Vector2i pointA = windowCS.getCoords(a, task.getOwnCS());
        Vector2i pointB = windowCS.getCoords(b, task.getOwnCS());
        // вектор, ведущий из точки A в точку B
        Vector2i delta = Vector2i.subtract(pointA, pointB);
        // получаем максимальную длину отрезка на экране, как длину диагонали экрана
        int maxDistance = (int) windowCS.getSize().length();
        // получаем новые точки для рисования, которые гарантируют, что линия
        // будет нарисована до границ экрана
        Vector2i renderPointA = Vector2i.sum(pointA, Vector2i.mult(delta, maxDistance));
        Vector2i renderPointB = Vector2i.sum(pointA, Vector2i.mult(delta, -maxDistance));
        // рисуем линию
        try (Paint p = new Paint()) {
            p.setColor(Misc.getColor(100, 200, 100, 50));
            canvas.drawLine(renderPointA.x, renderPointA.y, renderPointB.x, renderPointB.y, p);
        }
        try (Paint p = new Paint()) {
            p.setColor(Misc.getColor(200, 200, 100, 50));
            canvas.drawRect(Rect.makeXYWH(pointA.x - 3, pointA.y - 3, 6, 6), p);
            canvas.drawRect(Rect.makeXYWH(pointB.x - 3, pointB.y - 3, 6, 6), p);
        }

    }
}




