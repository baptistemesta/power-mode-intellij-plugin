package com.bmesta.powermode;

import com.intellij.openapi.options.ConfigurableBase;
import org.jetbrains.annotations.NotNull;

/**
 * @author Baptiste Mesta
 */
public class PowerModeConfigurable extends ConfigurableBase<PowerModeConfigurableUI, PowerMode> {

    private final PowerMode settings;

    public PowerModeConfigurable(@NotNull PowerMode settings) {
        super("power.mode", "Power mode", "power.mode");
        this.settings = settings;
    }
    public PowerModeConfigurable() {
        this(PowerMode.getInstance());
    }

    @NotNull
    @Override
    protected PowerMode getSettings() {
        if (settings == null) {
            throw new IllegalStateException("power mode is null");
        }
        return settings;
    }

    @Override
    protected PowerModeConfigurableUI createUi() {
        return new PowerModeConfigurableUI(settings);
    }
}
