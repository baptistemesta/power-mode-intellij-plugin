package com.bmesta.intellij;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import javax.swing.*;

import com.intellij.openapi.editor.Editor;
import com.intellij.ui.JBColor;

/**
 * @author Baptiste Mesta
 */
public class ParticleContainer extends JComponent {


    private final JComponent parent;
    private boolean dir;

    public ParticleContainer(Editor editor) {
        parent = editor.getContentComponent();
        parent.add(this);
        this.setBounds(parent.getBounds());
        this.setBorder(BorderFactory.createLineBorder(JBColor.RED));
        setVisible(true);
        parent.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                ParticleContainer.this.setBounds(parent.getBounds());
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {
                System.out.println("shown");

            }

            @Override
            public void componentHidden(ComponentEvent e) {
                System.out.println("hidden");
            }
        });
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
        dx = (int) (Math.random() * 4 ) * (Math.random()>0.5?-1:1) ;
        dy = (int) (Math.random() * -3 - 1);

        int size = (int) (Math.random() * 3 +1);
        int life = 15;
        final Particle e = new Particle(x, y, dx, dy, size, life, JBColor.darkGray);
        particles.add(e);
    }

    public void renderParticles(Graphics g2d) {
        for (int i = 0; i <= particles.size() - 1; i++) {
            particles.get(i).render(g2d);
        }
    }


    public void update(Point point) {
        //final int midX = SIZE / 2;
        //final int midY = SIZE / 2;
        //this.setBounds(point.x - midX, point.y - midY, SIZE, SIZE);
        addParticle(point.x, point.y);
        addParticle(point.x, point.y);
        addParticle(point.x, point.y);
        addParticle(point.x, point.y);
        addParticle(point.x, point.y);
        addParticle(point.x, point.y);
        addParticle(point.x, point.y);
        shakeEditor(parent, 5, 5, dir);
        dir = !dir;
        this.repaint();
    }
}
