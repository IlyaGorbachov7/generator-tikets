package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResolverToMap implements Resolver<Map<String, String>> {

    private String separateKeyValue = "=";

    private SplitResolverToArrayString splitResolver;

    public ResolverToMap(SplitResolverToArrayString splitResolver) {
        this.splitResolver = splitResolver;
    }

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

    @Override
    public String assembleToString(Map<String, String> object) {
        return object.entrySet().stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.mapping(entry -> String.join(separateKeyValue, entry.getKey(), entry.getValue()),
                                Collectors.toList()),
                        list -> splitResolver.assembleToString(list.toArray(String[]::new))
                ));
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String separateKeyValue;

        private SplitResolverToArrayString resolverSplit;

        private RegexResolverToString resolverRegex;

        /**
         * Not required properties but if if <b>resolverSplit == null</b> then is Required
         */
        public Builder resolverRegex(RegexResolverToString resolverRegex) {
            this.resolverRegex = resolverRegex;
            return this;
        }

        public Builder separateKeyValue() {
            this.separateKeyValue = separateKeyValue;
            return this;
        }

        public Builder resolverSplit(SplitResolverToArrayString resolverSplit) {
            this.resolverSplit = resolverSplit;
            return this;
        }

        public ResolverToMap build() {
            ResolverToMap resolver = new ResolverToMap();
            resolver.separateKeyValue = Objects.requireNonNullElse(separateKeyValue, resolver.separateKeyValue);
            resolver.splitResolver = Objects.requireNonNullElseGet(resolverSplit,
                    ()-> new SplitResolverToArrayString(Objects.requireNonNullElseGet(resolverRegex, RegexResolverToString::new)));
            return resolver;
        }
    }

}
