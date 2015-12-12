package com.bmesta.intellij;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import javax.swing.*;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Baptiste Mesta
 */
public class PowerMode2 implements ApplicationComponent {

    private Canvas render;

    private ArrayList<Particle> particles = new ArrayList<Particle>(500);
    private BufferStrategy bufferstrat;
    private Editor currentEditor;
    private MyJComponent myJComponent;

    @Override
    public void initComponent() {

        final TypedAction typedAction = EditorActionManager.getInstance().getTypedAction();
        final TypedActionHandler rawHandler = typedAction.getRawHandler();
        typedAction.setupRawHandler(new TypedActionHandler() {
            @Override
            public void execute(@NotNull final Editor editor, final char c, @NotNull final DataContext dataContext) {

                // Run key handler outside of the key typed command for creating our own undoable commands
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        initCanvas(editor);
                        updateCanvas(editor);
                    }
                });
                rawHandler.execute(editor, c, dataContext);
            }
        });
    }

    private void updateCanvas(@NotNull Editor editor) {
        currentEditor = editor;
        Point point = currentEditor.visualPositionToXY(editor.getCaretModel().getVisualPosition());
        render.setBounds(point.x,point.y,render.getWidth(),render.getHeight());
        System.out.println("set the canvas to "+point);
        addParticle(true, point.x, point.y);
        addParticle(false, point.x, point.y);
        addParticle(true, point.x, point.y);
        addParticle(false, point.x, point.y);
        addParticle(true, point.x, point.y);
        addParticle(false, point.x, point.y);
        render();
        render.repaint();
    }

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
        int life = (int) Math.random() * (120) + 380;
        particles.add(new Particle(x, y, dx, dy, size, life, Color.blue));
    }

    public void render() {
        do {
            do {
                Graphics2D g2d = (Graphics2D) bufferstrat.getDrawGraphics();
                g2d.fillRect(0, 0, render.getWidth(), render.getHeight());

                renderParticles(g2d);

                g2d.dispose();
                render.repaint();
            } while (bufferstrat.contentsRestored());
            bufferstrat.show();
        } while (bufferstrat.contentsLost());
    }

    public void renderParticles(Graphics2D g2d) {
        for (int i = 0; i <= particles.size() - 1; i++) {
            particles.get(i).render(g2d);
        }
    }

    private void initCanvas(@NotNull Editor editor) {
        if (render == null) {
            myJComponent = new MyJComponent();
            myJComponent.setSize(100,100);
            myJComponent.setVisible(true);
            render = new Canvas();
            JComponent parent = editor.getContentComponent();
            parent.add(render);
            parent.add(myJComponent);
            render.createBufferStrategy(2);
            render.setSize(100,100);
            bufferstrat = render.getBufferStrategy();
            render.setBackground(JBColor.BLUE);
            render.setVisible(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {

                        update();
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
    }

    private void update() {
        if (currentEditor == null)
            return;
        ApplicationManager.getApplication().runReadAction(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= particles.size() - 1; i++) {
                    if (particles.get(i).update())
                        particles.remove(i);
                }
            }
        });

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "PowerMode";
    }

    private static class MyJComponent extends JPanel {
    }

}
