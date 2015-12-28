package com.bmesta.intellij;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

import com.intellij.openapi.editor.Editor;
import com.intellij.ui.JBColor;

/**
 * @author Baptiste Mesta
 */
public class ParticleContainer extends JComponent {


    public static final int SIZE = 100;
    private final JComponent parent;
    private boolean dir;

    public ParticleContainer(Editor editor) {
        parent = editor.getContentComponent();
        parent.add(this);
        setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    updateParticles();

                    try {
                        Thread.sleep(1000 / 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


        }).start();
    }

    private void shakeEditor(JComponent parent, int dx, int dy, boolean dir) {
        final Rectangle bounds = parent.getBounds();
        parent.setBounds(bounds.x + (dir ? -dx : dx), bounds.y + (dir ? -dy : dy), bounds.width, bounds.height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderParticles(g);
    }

    private void updateParticles() {
        for (int i = 0; i <= particles.size() - 1; i++) {
            if (particles.get(i).update())
                particles.remove(i);
        }
        this.repaint();

    }

    private ArrayList<Particle> particles = new ArrayList<Particle>(500);

    public void addParticle(int x, int y) {
        int dx, dy;
        dx = (int) (Math.random() * 3 + 1) * (Math.random()>0.5?-1:1) ;
        dy = (int) (Math.random() * -3 + 1);

        int size = (int) (Math.random() * 4);
        int life = (int) (Math.random() * (120)) + 380;
        final Particle e = new Particle(x, y, dx, dy, size, life, JBColor.darkGray);
        particles.add(e);
    }

    public void renderParticles(Graphics g2d) {
        for (int i = 0; i <= particles.size() - 1; i++) {
            particles.get(i).render(g2d);
        }
    }


    public void update(Point point) {
        final int midX = SIZE / 2;
        final int midY = SIZE / 2;
        this.setBounds(point.x - midX, point.y - midY, SIZE, SIZE);
        addParticle(midX, midY);
        addParticle(midX, midY);
        addParticle(midX, midY);
        addParticle(midX, midY);
        addParticle(midX, midY);
        addParticle(midX, midY);
        shakeEditor(parent, 5, 5, dir);
        dir = !dir;
        this.repaint();
    }
}
