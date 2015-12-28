package com.bmesta.intellij;

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
    private Map<Editor, ParticleContainer> particleContainers = new HashMap<Editor, ParticleContainer>();

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
}
