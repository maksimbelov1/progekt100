package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.humbleui.jwm.MouseButton;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Rect;
import lombok.Getter;
import misc.*;
import panels.PanelLog;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static app.Colors.CROSSED_COLOR;
import static app.Colors.SUBTRACTED_COLOR;
import static java.lang.reflect.Array.getLength;

/**
 * Класс задачи
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class Task {
    /**
     * Текст задачи
     */
    public static final String TASK_TEXT = """
            ПОСТАНОВКА ЗАДАЧИ:
            На плоскости задано множество точек.
            Найти среди них такие две пары, что
            точка пересечения прямых,
            проведенных через эти пары точек,
            находится ближе всего к началу
            координат.""";

    /**
     * Вещественная система координат задачи
     */
    @Getter
    private final CoordinateSystem2d ownCS;
    /**
     * Список точек
     */
    @Getter
    private final ArrayList<Point> points;

    /**
     * Список точек в пересечении
     */
    @Getter
    @JsonIgnore
    private final ArrayList<Point> crossed;
    /**
     * Список точек в разности
     */
    @Getter
    @JsonIgnore
    private final ArrayList<Point> single;

    Line line;

    @Getter
    Segment segment;

    /**
     * Задача
     *
     * @param ownCS  СК задачи
     * @param points массив точек
     */
    @JsonCreator
    public Task(
            @JsonProperty("ownCS") CoordinateSystem2d ownCS,
            @JsonProperty("points") ArrayList<Point> points
    ) {
        this.ownCS = ownCS;
        this.points = points;

        this.crossed = new ArrayList<>();
        this.single = new ArrayList<>();
        line = new Line(new Vector2d(1, 2), new Vector2d(2, 1), this);


    }


    /**
     * Размер точки
     */
    private static final int POINT_SIZE = 3;
    /**
     * последнее движение мыши
     */
    protected Vector2i lastMove = new Vector2i(0, 0);
    /**
     * было ли оно внутри панели
     */
    protected boolean lastInside = false;

    /**
     * Очистить задачу
     */
    public void clear() {
        points.clear();
        solved = false;
    }


    /**
     * Флаг, решена ли задача
     */
    private boolean solved;


    /**
     * Решить задачу
     */
    public void solve() {
        // очищаем списки
        crossed.clear();
        single.clear();
        Point px = null;
        Point pmin = null;
        double l = Double.MAX_VALUE;
        double x = 0;
        // перебираем пары точек
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                for (int w = j + 1; w < points.size(); w++) {
                    for (int v = w + 1; v < points.size(); v++) {
                        // сохраняем точки
                        Point a = points.get(i);
                        Point b = points.get(j);
                        Point c = points.get(w);
                        Point d = points.get(v);
                        Line l1 = new Line(new Vector2d(a.pos.x, a.pos.y), new Vector2d(b.pos.x, b.pos.y), this);
                        Line l2 = new Line(new Vector2d(c.pos.x, c.pos.y), new Vector2d(d.pos.x, d.pos.y), this);
                        px = l1.cross(l2);
                        x = Math.sqrt(Math.pow((px.pos.x), 2) + Math.pow((px.pos.y), 2));
                        // если растояние между прямыми меньше x
                        if (x < l) {
                            l = x;
                            pmin = l1.cross(l2);

                        }
                    }
                }
            }

        }

        // задача решена
        solved = true;


        segment = new Segment(new Vector2d(pmin.pos.x, pmin.pos.y));

    }

    /**
     * Отмена решения задачи
     */
    public void cancel() {
        solved = false;
    }


    /**
     * проверка, решена ли задача
     *
     * @return флаг
     */
    public boolean isSolved() {
        return solved;
    }


    /**
     * Добавить случайные точки
     *
     * @param cnt кол-во случайных точек
     */
    public void addRandomPoints(int cnt) {
        // если создавать точки с полностью случайными координатами,
        // то вероятность того, что они совпадут крайне мала
        // поэтому нужно создать вспомогательную малую целочисленную ОСК
        // для получения случайной точки мы будем запрашивать случайную
        // координату этой решётки (их всего 30х30=900).
        // после нам останется только перевести координаты на решётке
        // в координаты СК задачи
        CoordinateSystem2i addGrid = new CoordinateSystem2i(30, 30);

        // повторяем заданное количество раз
        for (int i = 0; i < cnt; i++) {
            // получаем случайные координаты на решётке
            Vector2i gridPos = addGrid.getRandomCoords();
            // получаем координаты в СК задачи
            Vector2d pos = ownCS.getCoords(gridPos, addGrid);
            // сработает примерно в половине случаев
            if (ThreadLocalRandom.current().nextBoolean())
                addPoint(pos);

        }
    }


    /**
     * последняя СК окна
     */
    protected CoordinateSystem2i lastWindowCS;

    /**
     * Клик мыши по пространству задачи
     *
     * @param pos         положение мыши
     * @param mouseButton кнопка мыши
     */
    public void click(Vector2i pos, MouseButton mouseButton) {


        // задам pos1 для segment
        Vector2i pos1 = new Vector2i(0, 0);


        if (lastWindowCS == null) return;
        // получаем положение на экране
        Vector2d taskPos = ownCS.getCoords(pos, lastWindowCS);
        // если левая кнопка мыши, добавляем в первое множество
        if (mouseButton.equals(MouseButton.PRIMARY)) {
            addPoint(taskPos);
        }
    }


    /**
     * Добавить точку
     *
     * @param pos положение
     */
    public void addPoint(Vector2d pos) {
        solved = false;
        Point newPoint = new Point(pos);
        points.add(newPoint);
        PanelLog.info("точка " + newPoint + " добавлена ");
    }


    /**
     * Рисование задачи
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    public void paint(Canvas canvas, CoordinateSystem2i windowCS) {
        // Сохраняем последнюю СК
        lastWindowCS = windowCS;

        canvas.save();
        // создаём перо
        try (var paint = new Paint()) {
            if (segment != null)
                segment.paint(canvas, windowCS, ownCS);
            for (Point p : points) {

                if (!solved) {
                    paint.setColor(p.getColor());
                } else {
                    if (crossed.contains(p))
                        paint.setColor(CROSSED_COLOR);
                    else
                        paint.setColor(SUBTRACTED_COLOR);
                }
                // y-координату разворачиваем, потому что у СК окна ось y направлена вниз,
                // а в классическом представлении - вверх
                Vector2i windowPos = windowCS.getCoords(p.pos.x, p.pos.y, ownCS);
                // рисуем точку
                canvas.drawRect(Rect.makeXYWH(windowPos.x - POINT_SIZE, windowPos.y - POINT_SIZE, POINT_SIZE * 2, POINT_SIZE * 2), paint);
            }

        }

        line.renderLine(canvas, windowCS);

        canvas.restore();
    }


}