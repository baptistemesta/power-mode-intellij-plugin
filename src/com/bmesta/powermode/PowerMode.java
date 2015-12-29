package com.bmesta.powermode;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Baptiste Mesta
 */
@State(
        name = "PowerMode",
        storages = {@Storage(
                file = "$APP_CONFIG$/power.mode.xml"
        )}
)
public class PowerMode implements ApplicationComponent, PersistentStateComponent<PowerMode> {

    public static final String SEPARATOR = "_";
    public static final String ENABLED = "ENABLED";
    public static final String PLUGIN_ID = "com.bmesta.powermode";

    @com.intellij.util.xmlb.annotations.Transient
    private ParticleContainerManager particleContainerManager;

    private boolean enabled;

    public static PowerMode getInstance() {
        return ApplicationManager.getApplication().getComponent(PowerMode.class);
    }

    @Override
    public void initComponent() {

        final EditorActionManager editorActionManager = EditorActionManager.getInstance();
        final EditorFactory editorFactory = EditorFactory.getInstance();
        particleContainerManager = new ParticleContainerManager();
        editorFactory.addEditorFactoryListener(particleContainerManager, new Disposable() {
            @Override
            public void dispose() {

            }
        });
        final TypedAction typedAction = editorActionManager.getTypedAction();
        final TypedActionHandler rawHandler = typedAction.getRawHandler();
        typedAction.setupRawHandler(new TypedActionHandler() {
            @Override
            public void execute(@NotNull final Editor editor, final char c, @NotNull final DataContext dataContext) {
                updateEditor(editor);
                rawHandler.execute(editor, c, dataContext);
            }
        });
    }

    private void updateEditor(@NotNull final Editor editor) {
        //TODO configurable
        particleContainerManager.update(editor);
    }


    @Override
    public void disposeComponent() {
        particleContainerManager.dispose();
        particleContainerManager = null;
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "PowerMode";
    }


    @Nullable
    @Override
    public PowerMode getState() {
        return this;
    }

    @Override
    public void loadState(PowerMode state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
