package com.bmesta.powermode;

import javax.swing.*;

import com.intellij.openapi.options.ConfigurableUi;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NotNull;

/**
 * @author Baptiste Mesta
 */
public class PowerModeConfigurableUI implements ConfigurableUi<PowerMode> {


    private JPanel mainPanel;
    private JCheckBox powerModeEnabled;

    public PowerModeConfigurableUI(PowerMode powerMode) {
        powerModeEnabled.setSelected(powerMode.isEnabled());
    }

    @Override
    public void reset(@NotNull PowerMode powerMode) {
        powerModeEnabled.setSelected(powerMode.isEnabled());
    }

    @Override
    public boolean isModified(@NotNull PowerMode powerMode) {
        return powerModeEnabled.isSelected() != powerMode.isEnabled();
    }

    @Override
    public void apply(@NotNull PowerMode powerMode) throws ConfigurationException {
        powerMode.setEnabled(powerModeEnabled.isSelected());
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return mainPanel;
    }
}
