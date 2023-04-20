import app.Line;
import app.Point;
import app.Segment;
import app.Task;
import misc.CoordinateSystem2d;
import misc.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class Test {

    @org.junit.Test
    public void test1() {
        Task t = new Task(new CoordinateSystem2d(-10, -10, 20, 20), new ArrayList<>());

        Line l1 = new Line(
                new Vector2d(4, 4),
                new Vector2d(0, 0),
                t
        );

        Line l2 = new Line(
                new Vector2d(0, 4),
                new Vector2d(4, 0),
                t
        );

        Point p = l1.cross(l2);

        Point target = new Point(new Vector2d(2, 2));

        System.out.printf("%.3f %.3f", p.pos.x, p.pos.y);

        assert Math.abs(p.pos.x - target.pos.x) < 0.001 && Math.abs(p.pos.y - target.pos.y) < 0.001;

    }

    @org.junit.Test
    public void test2() {
        Task t = new Task(new CoordinateSystem2d(-10, -10, 20, 20), new ArrayList<>());

        Line l1 = new Line(
                new Vector2d(0, 0),
                new Vector2d(1, 0),
                t
        );

        Line l2 = new Line(
                new Vector2d(0, 2),
                new Vector2d(2, 2),
                t
        );

        Point p = l1.cross(l2);
        assert p == null;


    }

    @org.junit.Test
    public void test3() {
        Task t = new Task(new CoordinateSystem2d(-10, -10, 20, 20), new ArrayList<>());

        Line l1 = new Line(
                new Vector2d(4, 0),
                new Vector2d(3, 2),
                t
        );

        Line l2 = new Line(
                new Vector2d(-1, 2),
                new Vector2d(5, 6),
                t
        );

        Point p = l1.cross(l2);


        Point target = new Point(new Vector2d(2, 4));

        System.out.printf("%.3f %.3f", p.pos.x, p.pos.y);

        assert Math.abs(p.pos.x - target.pos.x) < 0.001 && Math.abs(p.pos.y - target.pos.y) < 0.001;

    }

    @org.junit.Test
    public void test4() {
        Task t = new Task(new CoordinateSystem2d(-10, -10, 20, 20), new ArrayList<>());

        Line l1 = new Line(
                new Vector2d(0, 0),
                new Vector2d(0, 6),
                t
        );

        Line l2 = new Line(
                new Vector2d(8, 2),
                new Vector2d(0, 6),
                t
        );

        Point p = l1.cross(l2);


        Point target = new Point(new Vector2d(0, 6));

        System.out.printf("%.3f %.3f", p.pos.x, p.pos.y);


        assert Math.abs(p.pos.x - target.pos.x) < 0.001 && Math.abs(p.pos.y - target.pos.y) < 0.001;

    }

    @org.junit.Test
    public void test5() {
        ArrayList<Point> pointList = new ArrayList<>();
        pointList.add(new Point(new Vector2d(0.1, 0.3)));
        pointList.add(new Point(new Vector2d(0.2, 0.5)));
        pointList.add(new Point(new Vector2d(0.1, -0.5)));

        Task t = new Task(new CoordinateSystem2d(-10, -10, 20, 20), pointList);

        t.solve();

        Segment segment = t.getSegment();

        assert Math.abs(segment.pos1.x - 0) < 0.001 && Math.abs(segment.pos1.y-0) < 0.001;
    }
}
