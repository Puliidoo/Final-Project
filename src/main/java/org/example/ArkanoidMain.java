package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ArkanoidMain extends JPanel implements KeyListener, ActionListener {
    Timer timer;
    int jugadorX = 310;
    int puntuacion = 0;
    boolean enPartida = true;

    // Pilota
    int bolaX = 120, bolaY = 350;
    int dirX = -2, dirY = -4;


    // hola
    // Blocs
    boolean[][] blocs = new boolean[3][7]; // 3 files, 7 columnes
    int blocWidth = 80;
    int blocHeight = 30;
    // Màrgens als costats per als blocs
    int margeX = 50;                 // inici matriu blocs
    int blocTotalWidth = blocWidth * blocs[0].length; // 560

    public ArkanoidMain() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(8, this);
        timer.start();
        inicialitzarBlocs();
    }

    private void inicialitzarBlocs() {
        for (int i = 0; i < blocs.length; i++)
            for (int j = 0; j < blocs[0].length; j++)
                blocs[i][j] = true;
    }

    @Override
    public void paint(Graphics g) {
        // Fons
        g.setColor(Color.white);
        g.fillRect(1, 1, 692, 592);

        // Blocs
        for (int i = 0; i < blocs.length; i++) {
            for (int j = 0; j < blocs[0].length; j++) {
                if (blocs[i][j]) {
                    int x = margeX + j * blocWidth;
                    int y = 50 + i * blocHeight;
                    g.setColor(Color.red);
                    g.fillRect(x, y, blocWidth, blocHeight);
                    g.setColor(Color.black);
                    g.drawRect(x, y, blocWidth, blocHeight);
                }
            }
        }

        // Raqueta
        g.setColor(Color.blue);
        g.fillRect(jugadorX, 550, 100, 8);

        // Pilota
        g.setColor(Color.green);
        g.fillOval(bolaX, bolaY, 15, 15);

        // Punts
        g.setColor(Color.black);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Punts: " + puntuacion, 520, 30);

        // Fi de partida
        if (!enPartida) {
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Partida acabada!", 250, 300);
            timer.stop();
            String nom = JOptionPane.showInputDialog("Introdueix el teu nom:");
            if (nom != null && !nom.isEmpty()) {
                GestorPuntuaciones.guardarPuntuacion(nom, puntuacion);
            }
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!enPartida) return;
        // Moviment de la pilota
        bolaX += dirX;
        bolaY += dirY;

        // Rebot amb parets exteriors
        if (bolaX < 0 || bolaX > 670) dirX = -dirX;
        if (bolaY < 0) dirY = -dirY;
        if (bolaY > 570) enPartida = false;

        // **Rebot a marge esquerre/dreta de blocs**
        if (bolaX < margeX || bolaX > margeX + blocTotalWidth - 15) {
            dirX = -dirX;
        }

        Rectangle bolaRect = new Rectangle(bolaX, bolaY, 15, 15);
        Rectangle raquetaRect = new Rectangle(jugadorX, 550, 100, 8);

        // Rebot amb raqueta (només si la pilota està per sota dels blocs i per sobre de la raqueta)
        if (bolaRect.intersects(raquetaRect) && bolaY + 15 >= 500) {
            dirY = -dirY;
        }

        // Col·lisions amb blocs
        A:
        for (int i = 0; i < blocs.length; i++) {
            for (int j = 0; j < blocs[0].length; j++) {
                if (blocs[i][j]) {
                    int x = margeX + j * blocWidth;
                    int y = 50 + i * blocHeight;
                    Rectangle blocRect = new Rectangle(x, y, blocWidth, blocHeight);
                    if (bolaRect.intersects(blocRect)) {
                        blocs[i][j] = false;
                        puntuacion++;
                        // Decideix rebot horitzontal o vertical
                        if (bolaX + 15 <= x || bolaX >= x + blocWidth) dirX = -dirX;
                        else dirY = -dirY;
                        break A;
                    }
                }
            }
        }

        repaint();
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && jugadorX < 600) jugadorX += 20;
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && jugadorX > 10) jugadorX -= 20;
    }

    public static void main(String[] args) {
        JFrame obj = new JFrame();
        ArkanoidMain joc = new ArkanoidMain();
        obj.setBounds(10, 10, 700, 600);
        obj.setTitle("Arkanoid Java");
        obj.setResizable(false);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(joc);
        obj.setVisible(true);
    }
}
