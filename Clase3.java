import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Clase3 extends JPanel implements MouseListener, MouseMotionListener{

    // Definición de códigos de recorte
    private static final int RANGO = 0; // 0000 -> 0 en binario
    private static final int IZQ = 1;   // 0001 -> 1 en binario
    private static final int DER = 2;  // 0010 -> 2 en binario
    private static final int ABAJO = 4; // 0100 -> 4 en binario
    private static final int ARRIBA = 8;    // 1000 -> 8 en binario

    // Función para calcular el código de recorte de un punto
    private static int espacioLinea(double x, double y, double xmin, double ymin, double xmax, double ymax){
        int espacio = RANGO;
        if (x < xmin)      // izquierda de la ventana
            espacio |= IZQ;
        else if (x > xmax) // derecha de la ventana
            espacio |= DER;
        if (y < ymin)      // debajo de la ventana
            espacio |= ABAJO;
        else if (y > ymax) // arriba de la ventana
            espacio |= ARRIBA;
        return espacio;
    }

    // Función para recortar una línea utilizando el algoritmo Cohen-Sutherland
    public static void cohenSutherland(Graphics2D g, double x0, double y0, double x1, double y1, double xmin, double ymin, double xmax, double ymax){
        int punto1 = espacioLinea(x0, y0, xmin, ymin, xmax, ymax);
        int punto2 = espacioLinea(x1, y1, xmin, ymin, xmax, ymax);
        boolean acepta = false;

        while (true) {
            if ((punto1 | punto2) == 0){ // Ambos puntos están dentro de la ventana
                acepta = true;
                break;
            } else if ((punto1 & punto2) != 0){ // Ambos puntos están fuera de la ventana y en la misma región de recorte
                break;
            } else{
                // Calcular intersección y actualizar los puntos fuera de la ventana
                double x, y;
                int puntoFinal = (punto1 != 0) ? punto1 : punto2;

                if ((puntoFinal & ARRIBA) != 0){           // punto arriba de la ventana
                    x = x0 + (x1 - x0) * (ymax - y0) / (y1 - y0);
                    y = ymax;
                } else if ((puntoFinal & ABAJO) != 0){ // punto debajo de la ventana
                    x = x0 + (x1 - x0) * (ymin - y0) / (y1 - y0);
                    y = ymin;
                } else if ((puntoFinal & DER) != 0){  // punto a la derecha de la ventana
                    y = y0 + (y1 - y0) * (xmax - x0) / (x1 - x0);
                    x = xmax;
                } else {                                 // punto a la izquierda de la ventana
                    y = y0 + (y1 - y0) * (xmin - x0) / (x1 - x0);
                    x = xmin;
                }

                if (puntoFinal == punto1){
                    x0 = x;
                    y0 = y;
                    punto1 = espacioLinea(x0, y0, xmin, ymin, xmax, ymax);
                } else{
                    x1 = x;
                    y1 = y;
                    punto2 = espacioLinea(x1, y1, xmin, ymin, xmax, ymax);
                }
            }
        }

        if (acepta){
            g.setColor(Color.GREEN);
            g.drawLine((int)x0, (int)y0, (int)x1, (int)y1); // Dibujar línea recortada
        } else{
            System.out.println("Toda la línea está fuera del rectangulo");
        }
    }

    private double startX, startY, endX, endY;


    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Medidas rectangulo de clipping (xmin, ymin, xmax, ymax)
        double xmin = -200;
        double ymin = -100;
        double xmax = 200;
        double ymax = 100;

        // Dibujar rectangulo de clipping
        g2d.setColor(Color.BLACK);
        g2d.drawRect((int)xmin, (int)ymin, (int)(xmax - xmin), (int)(ymax - ymin));

        // Dibujar línea dibujada con el mouse
        g2d.setColor(Color.RED);
        g2d.drawLine((int) startX, (int) startY, (int) endX, (int) endY);

        // Aplicar el algoritmo de Cohen-Sutherland
        cohenSutherland(g2d, startX, startY, endX, endY, xmin, ymin, xmax, ymax);
    }

    // Todos los @Override son usados para pintar la linea con el mouse
    @Override
    public void mouseClicked(MouseEvent e){}

    @Override
    public void mousePressed(MouseEvent e){
        startX = e.getX();
        startY = e.getY();
        endX = startX;
        endY = startY;
    }

    @Override
    public void mouseReleased(MouseEvent e){
        endX = e.getX();
        endY = e.getY();
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e){}

    @Override
    public void mouseExited(MouseEvent e){}

    @Override
    public void mouseDragged(MouseEvent e){
        endX = e.getX();
        endY = e.getY();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e){}

    public Clase3(){
        addMouseListener(this);
        addMouseMotionListener(this);
    }


    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Algoritmo de Cohen-Sutherland");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Clase3());
            frame.setSize(600, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
