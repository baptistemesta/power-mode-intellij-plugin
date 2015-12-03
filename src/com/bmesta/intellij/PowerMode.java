package com.bmesta.intellij;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ex.DefaultFrameEditorComponentProvider;
import com.intellij.openapi.wm.ex.FrameEditorComponentProvider;

/**
 * @author Baptiste Mesta
 */
public class PowerMode extends DefaultFrameEditorComponentProvider implements FrameEditorComponentProvider {

    @Override
    public JComponent createEditorComponent(Project project) {
        System.out.println("create editor for project "+project);
        FileEditorManagerEx instanceEx = FileEditorManagerEx.getInstanceEx(project);
        instanceEx.getCurrentWindow().getSelectedEditor().getComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println("key typed"+e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("key pressed"+e);

            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("key released"+e);

            }
        });
        return instanceEx.getComponent();
    }
}
