package com.bmesta.powermode;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryAdapter;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Baptiste Mesta
 */
class ParticleContainerManager extends EditorFactoryAdapter {
    private Thread thread;
    private Map<Editor, ParticleContainer> particleContainers = new HashMap<Editor, ParticleContainer>();

    public ParticleContainerManager() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (ParticleContainer particleContainer : particleContainers.values()) {
                        particleContainer.updateParticles();
                    }
                    try {
                        Thread.sleep(1000 / 60);
                    } catch (InterruptedException ignored) {
                        //thread interrupted, shutdown
                    }
                }
            }


        });
        thread.start();
    }

    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {
        final Editor editor = event.getEditor();
        particleContainers.put(editor, new ParticleContainer(editor));
    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
        particleContainers.remove(event.getEditor());
    }

    public void update(final Editor editor) {
        if (PowerMode.getInstance().isEnabled())
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateInUI(editor);
                }
            });
    }

    private void updateInUI(Editor editor) {
        Point point = editor.visualPositionToXY(editor.getCaretModel().getVisualPosition());
        final ParticleContainer particleContainer = particleContainers.get(editor);
        if (particleContainer != null) {
            particleContainer.update(point);
        }
    }

    public void dispose() {
        thread.interrupt();
        particleContainers.clear();
    }
}
