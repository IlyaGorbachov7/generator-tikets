package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

@RequiredArgsConstructor
@AllArgsConstructor
public class ResolverToMap implements Resolver<Map<String, String>> {
    private String separateKeyValue = "=";

    public ResolverToMap(SplitResolveToArrayString splitResolver) {
        this.splitResolver = splitResolver;
    }

    private SplitResolveToArrayString splitResolver;

    public Map<String, String> assemble(String value, Supplier<Map<String, String>> supplier) {
        resolve(value, supplier.get());
        return supplier.get();
    }

    @Override
    public Map<String, String> assemble(String value) {
        Map<String, String> map = new HashMap<>();
        resolve(value, map);
        return map;
    }

    private void resolve(String value, Map<String, String> map) {
        Arrays.stream(splitResolver.assemble(value))
                .filter(keyValue -> keyValue.length() > 2)
                .forEach(keyValue -> {
                    int indexSeparate = keyValue.indexOf(separateKeyValue);
                    if (indexSeparate > 0) {
                        String k = keyValue.substring(0, indexSeparate).trim();
                        String v = keyValue.substring(indexSeparate + 1).trim();
                        map.put(k, v);
                    }
                });
    }

}
