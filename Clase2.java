import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class Clase2 extends JPanel{
    private int startX, startY, endX, endY;

 
   //Clase paintComponent para poder pintar la linea 
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        if (startX != 0 && startY != 0 && endX != 0 && endY != 0){
            // Dibujar línea usando el algoritmo de Bresenham
            algoritmoBresenham(g2d, startX, startY, endX, endY);
        }
    }
    
    // Clase  para poder pintar la linea con el mouse
    public Clase2(){
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                startX = e.getX();
                startY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e){
                endX = e.getX();
                endY = e.getY();
                repaint();
            }
        });
    }

    // Algoritmo de Bresenham para dibujar una línea
    private void algoritmoBresenham(Graphics2D g2d, int x0, int y0, int x1, int y1){
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int bx = (x0 < x1) ? 1 : -1;
        int by = (y0 < y1) ? 1 : -1;

        int d = dx - dy;

        while (x0 != x1 || y0 != y1){
            g2d.drawLine(x0, y0, x0, y0);
            int dx2 = 2 * d;
            if (dx2 > -dy){
                d -= dy;
                x0 += bx;
            }
            if (dx2 < dx){
                d += dx;
                y0 += by;
            }
        }
    }

    public static void main(String[] args){
        // Crear un nuevo Frame
        JFrame frame = new JFrame("Algoritmo de Bresenham");

        //Usando el frame añadimos el frame a la clase, le ponemos un tamaño y lo volvemos visible
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Clase2());
        frame.setSize(400, 350);
        frame.setVisible(true);
    }
}