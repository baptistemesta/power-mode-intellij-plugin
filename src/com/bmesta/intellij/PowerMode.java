package com.bmesta.intellij;

import java.awt.*;
import javax.swing.*;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import org.jetbrains.annotations.NotNull;

/**
 * @author Baptiste Mesta
 */
public class PowerMode implements ApplicationComponent {

    private ParticleContainer particleContainer;

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
        Point point = editor.visualPositionToXY(editor.getCaretModel().getVisualPosition());
        particleContainer.update(point);
    }


    private void initCanvas(@NotNull Editor editor) {
        if (particleContainer == null) {
            particleContainer = new ParticleContainer(editor);
        }
    }


    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "PowerMode";
    }


}
