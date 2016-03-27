/*
 * Copyright 2015 Baptiste Mesta
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bmesta.powermode;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollingModel;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.editor.event.EditorFactoryAdapter;
import com.intellij.openapi.editor.event.EditorFactoryEvent;

/**
 * @author Baptiste Mesta
 */
class ParticleContainerManager extends EditorFactoryAdapter {

    private Thread thread;
    private Map<Editor, ParticleContainer> particleContainers = new HashMap<>();

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
        VisualPosition visualPosition = editor.getCaretModel().getVisualPosition();
        Point point = editor.visualPositionToXY(visualPosition);
        ScrollingModel scrollingModel = editor.getScrollingModel();
        point.x = point.x - scrollingModel.getHorizontalScrollOffset();
        point.y = point.y - scrollingModel.getVerticalScrollOffset();
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
