package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.ReadableProperties;
import lombok.NonNull;

import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <a href="https://poe.com/s/QV4d180Sbln5chXgNUs4">@RequiredArgsAnnotation</a>
 *
 * @version 15.07 - 16. 07 2024
 */
public abstract class ResourceBundleManager extends ReadableProperties {

    protected ResourceBundle resourceBundle;

    public ResourceBundleManager(@NonNull ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        initProperties();
    }

    @Override
    protected void initProperties() {
        properties = resourceBundle.keySet().stream()
                .collect(Collectors.toMap(Function.identity(), resourceBundle::getString, (nll, v) -> v, Hashtable::new));
    }
}