package movableCircles.Backup;

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.Timer;
/*<applet code="Circle.class" CodeBase="" width=2000 height=1000></applet> */
public class Circle extends JApplet implements KeyListener,ActionListener{
    public static int THEOX;
    public static int THEOY;
    public static int WIDTH = 2000;
    public static int HEIGHT = 1200;
    public static final int INITIALWIDTH = 2000;
    public static final int INITIALHEIGHT = 1200;
    public static final double G[] = {6.67, -11};
    public static double[] sun;
    public static double[] radius;
    public static ArrayList<Ball> balls;
    //public static double scaler = 100;
    private ArrayList<Integer> keysDown;
    public static boolean changed;
    public static Sun suns;
    public static double zoom;
    public static boolean change;
    
    public static JComboBox choosePlanet;
    public static JButton selectFocus;
    
    private PaintSurface canvas;
    
    public static int index;
    public static boolean focused;
    
    public void init() {
        focused = false;
        index = 0;
        zoom = 1;
        keysDown = new ArrayList<Integer>();
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
        THEOX = 200;
        THEOY = 100;
        this.sun = new double[3];
        this.sun[0] = 1.99;
        this.sun[1] = 30;
        this.radius = new double[2];
        this.radius[0] = 695.7;
        this.radius[1] = 6;
        radius = normalise(radius);
        // 1 pixel = 100,000km
        double rs[] = {695.7 * 2, 6};
        rs = normalise(rs);
        sun[2] = rs[0];
        double rp[] = {6.4 * 2 , 6};
        rp = normalise(rp); 
        suns = new Sun((int) radius[0]);  
        
        balls = new ArrayList<Ball>();   
        
        //Mercury
        rp[0] = 2.440 * 2;
        rp[1] = 6;
        rp = normalise(rp);
        this.radius[0] = 57.91;
        this.radius[1] = 9;
        balls.add(new Ball((int) WIDTH / 2,(int) HEIGHT / 2, (int) rp[0], (int) (radius[0]), getSpeed(), Color.GRAY));
        //Venus
        rp[0] = 6.052 * 2;
        rp[1] = 6;
        rp = normalise(rp);
        this.radius[0] = 108.2;
        this.radius[1] = 9;
        balls.add(new Ball((int) WIDTH / 2,(int) HEIGHT / 2, (int) rp[0], (int) (radius[0]), getSpeed(), Color.RED));
        //Earth
        rp[0] = 6.371 * 2;
        rp[1] = 6;
        rp = normalise(rp);
        this.radius[0] = 149.6;
        this.radius[1] = 9;
        balls.add(new Ball((int) WIDTH / 2,(int) HEIGHT / 2, (int) rp[0], (int) (radius[0]), getSpeed(), Color.BLUE));
        //Mars
        rp[0] = 3.390 * 2;
        rp[1] = 6;
        rp = normalise(rp);
        this.radius[0] = 227.9;
        this.radius[1] = 9;
        balls.add(new Ball((int) WIDTH / 2,(int) HEIGHT / 2, (int) rp[0], (int) (radius[0]), getSpeed(), Color.magenta));
        //Jupiter
        rp[0] = 69.9 * 2;
        rp[1] = 6;
        rp = normalise(rp);
        this.radius[0] = 778.5;
        this.radius[1] = 9;
        balls.add(new Ball((int) WIDTH / 2,(int) HEIGHT / 2, (int) rp[0], (int) (radius[0]), getSpeed(), Color.LIGHT_GRAY));
        //Saturn
        rp[0] = 58.2 * 2;
        rp[1] = 6;
        rp = normalise(rp);
        this.radius[0] = 1429.5;
        this.radius[1] = 9;
        balls.add(new Ball((int) WIDTH / 2,(int) HEIGHT / 2, (int) rp[0], (int) (radius[0]), getSpeed(), Color.GRAY));
        //Uranus
        rp[0] = 25.4 * 2;
        rp[1] = 6;
        rp = normalise(rp);
        this.radius[0] = 2871;
        this.radius[1] = 9;
        balls.add(new Ball((int) WIDTH / 2,(int) HEIGHT / 2, (int) rp[0], (int) (radius[0]), getSpeed(), Color.WHITE));
        //Neptune
        rp[0] = 24.6 * 2;
        rp[1] = 6;
        rp = normalise(rp);
        this.radius[0] = 4498;
        this.radius[1] = 9;
        balls.add(new Ball((int) WIDTH / 2,(int) HEIGHT / 2, (int) rp[0], (int) (radius[0]), getSpeed(), Color.BLUE));
        
        setFocus(0);
        this.setSize(WIDTH, HEIGHT);
        this.setBackground(Color.BLACK);
        canvas = new PaintSurface();
        JScrollPane scroller = new JScrollPane(canvas);
        this.add(canvas, BorderLayout.CENTER);
        JLabel label = new JLabel("Random Balls");
        this.add(label, BorderLayout.NORTH);
        this.add(buttonPanel(), BorderLayout.EAST);
        
        Timer t = new Timer(0, e -> {canvas.repaint();});
        t.start();
    }
    
    public JPanel buttonPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createRigidArea(new Dimension(0,560)));
        JLabel label = new JLabel("Select the planet to focus on");
        panel.add(label);
        String[] planets = {"SUN","MERCURY", "VENUS", "EARTH", "MARS", "JUPITER", "SATURN", "URANUS", "NEPTUNE"};
        choosePlanet = new JComboBox(planets);
        selectFocus = new JButton("Set Focus");
        selectFocus.addActionListener(this);
                
        panel.add(choosePlanet);
        panel.add(selectFocus);
        panel.add(Box.createRigidArea(new Dimension(0,560)));
        return panel;
    }
    public double[] normalise(double[] toNorm){
        if (toNorm[1] != 7){
            toNorm[0] *= Math.pow(10,toNorm[1] - 7);
            toNorm[1] = 7;
        }
        return toNorm;
    }
    
    public double getSpeed(){
        // let 1 pixel = 10E m
        double v = sun[0] * G[0] * Math.pow(10,(G[1] + sun[1] - radius[1]));
        v /= radius[0];
        v /= Math.pow(Math.E, 20);
        return v;
    }
    
    //radius and omega v = omega * r;
    //theta = 1;    
@Override
    public void keyPressed(KeyEvent e) {
        if(!keysDown.contains(e.getKeyCode()) && e.getKeyCode() != 86){
            keysDown.add(new Integer(e.getKeyCode()));
        }
        action();
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    public void action(){
        boolean scalerChange = false;
        if (keysDown.contains(KeyEvent.VK_UP)){
            THEOY -= 20;
        }
        if (keysDown.contains(KeyEvent.VK_DOWN)){
            THEOY += 20;
        }
        if (keysDown.contains(KeyEvent.VK_RIGHT)){
            THEOX += 20;
        }
        if (keysDown.contains(KeyEvent.VK_LEFT)){
            THEOX -= 20;
        }
        if (keysDown.contains(KeyEvent.VK_PERIOD)){
            zoom += 0.5;
            reCentre();
        }
        if (keysDown.contains(KeyEvent.VK_COMMA)){
            zoom -= 0.5;
            reCentre();
        }
         if (keysDown.contains(KeyEvent.VK_SHIFT)){
            zoom = 1;
            reCentre();
        }
        if (scalerChange){
            //reScale();
        }
        
        canvas.repaint();
        this.setFocusable(true);
        this.requestFocusInWindow();
    }
    public void reCentre(){
        THEOX = 0;
        THEOY = 0;
        WIDTH = (int) (INITIALWIDTH / zoom);
        HEIGHT = (int) (INITIALHEIGHT / zoom);
    }
    
    
    @Override
    public void keyReleased(KeyEvent e) {
        keysDown.remove(new Integer(e.getKeyCode()));
        this.setFocusable(true);
        this.requestFocusInWindow();
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selectFocus){
            int index = choosePlanet.getSelectedIndex();
            setFocus(index);
        }
        this.setFocusable(true);
        this.requestFocusInWindow();
    }
    public void setFocus(int i){
        if (i == 0){
            focused = false;
            reCentre();
        } else{
            index = i - 1;
            focused = true;            
        }
    }
    
    
}
class PaintSurface extends JComponent{
    
    public void paint(Graphics g){
        g.setColor(Color.black);
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(Circle.zoom,Circle.zoom);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Rectangle2D rect = new Rectangle(0,0, (int) (Circle.WIDTH ), (int) (Circle.HEIGHT));
        rect.setRect(0,0,(int) Circle.WIDTH, (int) Circle.HEIGHT);
        g2.fill(rect);
        
        Circle.suns.x = (Circle.WIDTH / 2 - (Circle.suns.width / 2) + Circle.THEOX);
        Circle.suns.y = (Circle.HEIGHT / 2 - (Circle.suns.height / 2) + Circle.THEOY);
        g2.setColor(Color.yellow);
        if (Circle.focused){
            int[] d = Circle.balls.get(Circle.index).specialMove();
            g2.translate(-d[0], -d[1]);
        }
        g2.fill(Circle.suns);
        
        for (int i = 0; i < Circle.balls.size(); i++){
            Ball ball = Circle.balls.get(i);
            ball.move();
            g2.setColor(ball.colour);
            g2.fill(ball);
            
        }
       
    }
}
class Ball extends Ellipse2D.Float{
    int d;
    double angle;
    int radius;
    double xSpeed;
    double count;
    double ySpeed;
    boolean last;
    boolean changed;
    boolean sun;
    Color colour;
    
    public Ball( int d, int v){
        super((float)Math.random() * (Circle.WIDTH - 2 * d) + d,(float) Math.random() * (Circle.HEIGHT -2 * d) + d,d,d);
        this.xSpeed = (Math.random() * v) - v/2;
        this.ySpeed = (Math.random() * v) - v/2;
        if (super.y + ySpeed < 0  || super.y + ySpeed > Circle.HEIGHT){
            super.y =(float) ( super.y + 2 * ySpeed);
            super.y %=  Circle.HEIGHT;
        }
        if (super.x + xSpeed < 0 || super.x + xSpeed > Circle.WIDTH){
            super.x = (float) (super.x + 2 * xSpeed);
            super.x %=  Circle.WIDTH;
        }
        d = (int) super.width;
        
    }
    public Ball (int x, int y, int d, int a, double v, Color c){
        super((float) x - d/2, (float) y + a - d/2, (float)(d),(float)(d));
        if (super.width == 0){
            reScale();
        }
        this.xSpeed = v;
        this.d = d;
        this.angle = 0;
        this.radius = a;
        this.count = 0;
        this.changed = false;
        this.colour = c;
        sun = false;
        if (v == 0){ sun = true;}
    }
    public void setX(float x){
        super.x = x;
    }
    public void setY(float y){
        super.x = y;
    }
    public void reScale(){
        if (this.width == 0){
            this.width = 1;
            this.height = 1;
        }
        this.width = (float) (this.width);
        this.height = (float) (this.height);
        if (this.width == 0){
            this.width = 1;
            this.height = 1;
        }
    }
    public void move(){
        /*if (super.y <= 0 || super.y >= Circle.HEIGHT - d && !last){
            ySpeed = -ySpeed;
            last = true;
        } else if (super.y <= 0 || super.y >= Circle.HEIGHT - d && last){
            last = true;
        } else if (last){
            last = false;
        }
        if (super.x <= 0 || super.x >= Circle.WIDTH - d && !last){
            xSpeed = -xSpeed;
            last = true;
        } else if (super.x <= 0 || super.x >= Circle.WIDTH - d && last) {
            last = true;
        } else if (last){
            last = false;
        }
        super.x += xSpeed;
        super.y += ySpeed;*/
        Sun sun = Circle.suns;
        
        double dx = radius * Math.sin(angle);
        double dy = radius * Math.cos(angle);
        
        int rad = sun.radius;
        
        this.x = (float) (sun.getCenterX() + dx);
        this.y = (float) (sun.getCenterY() + dy);
        
        //super.x += radius * Math.cos(angle) * 0.001* xSpeed;
        //super.y -= radius * Math.sin(angle) * 0.001* xSpeed;
        //if ( this.count % this.xSpeed == 0){
             angle += 0.001 * xSpeed;
        //}
        this.count++;
    }
    public int[] specialMove(){
        Sun sun = Circle.suns;
        double dx = radius * Math.sin(angle);
        double dy = radius * Math.cos(angle);
        int rad = sun.radius;
        this.x = (float) (sun.x + dx) + rad;
        this.y = (float) (sun.y + dy) + rad;
        angle += 0.001 * xSpeed;
        
        int[] ret = new int[2];
        ret[0] = (int) (dx + rad);
        ret[1] = (int) (dy + rad);
        
        return ret;
    }
    
}
class Sun extends Ellipse2D.Float{
    final boolean SUN = true;
    int radius;
    public Sun(int radius){
        super(Circle.WIDTH, Circle.HEIGHT, (float)(radius ), (float)(radius ));
        this.radius = (int) (radius);
    }
}
