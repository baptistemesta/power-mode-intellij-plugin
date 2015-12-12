package com.bmesta.intellij;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import javax.swing.*;

import com.intellij.openapi.editor.Editor;
import com.intellij.ui.JBColor;

/**
 * @author Baptiste Mesta
 */
public class ParticleContainer extends Canvas {


    private BufferStrategy bufferstrat;

    public ParticleContainer(Editor editor) {
        JComponent parent = editor.getContentComponent();
        parent.add(this);
        createBufferStrategy(2);
        setSize(100, 100);
        bufferstrat = getBufferStrategy();
        setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    updateParticles();
                    render();

                    try {
                        Thread.sleep(1000 / 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }

    private void updateParticles() {

        for (int i = 0; i <= particles.size() - 1; i++) {
            if (particles.get(i).update())
                particles.remove(i);
        }

    }

    private ArrayList<Particle> particles = new ArrayList<Particle>(500);

    public void addParticle(boolean bool, int x, int y) {
        int dx, dy;
        if (bool) {
            dx = (int) (Math.random() * 5);
            dy = (int) (Math.random() * 5);
        } else {
            dx = (int) (Math.random() * -5);
            dy = (int) (Math.random() * -5);
        }
        int size = (int) (Math.random() * 12);
        int life = (int) (Math.random() * (120)) + 380;
        particles.add(new Particle(x, y, dx, dy, size, life, JBColor.green));
    }

    public void renderParticles(Graphics2D g2d) {
        for (int i = 0; i <= particles.size() - 1; i++) {
            particles.get(i).render(g2d);
        }
    }

    public void render() {
        do {
            do {
                Graphics2D g2d = (Graphics2D) bufferstrat.getDrawGraphics();
                g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

                renderParticles(g2d);

                g2d.dispose();
                this.repaint();
            } while (bufferstrat.contentsRestored());
            bufferstrat.show();
        } while (bufferstrat.contentsLost());
    }

    public void update(Point point) {
        this.setBounds(point.x, point.y, getWidth(), getHeight());
        System.out.println("set the canvas to " + point);
        addParticle(true, point.x, point.y);
        addParticle(false, point.x, point.y);
        addParticle(true, point.x, point.y);
        addParticle(false, point.x, point.y);
        addParticle(true, point.x, point.y);
        addParticle(false, point.x, point.y);
        render();
        this.repaint();
    }
}
