package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver;

import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResolverToArrayBoolean implements Resolver<boolean[]> {

    @Getter
    @Setter
    protected ResolverToBoolean resolverBoolean;

    @Getter
    @Setter
    protected SplitResolverToArrayString resolverSplit;


    @Override
    public boolean[] assemble(String value) {
        value = value.trim();
        List<Boolean> booleans = Stream.of(resolverSplit.assemble(value)).map(resolverBoolean::assemble).toList();
        boolean[] result = new boolean[booleans.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = booleans.get(i);
        }
        return result;
    }

    @Override
    public String assembleToString(boolean @NonNull [] object) {
        String[] builderResult =new String[object.length];
        for (int i = 0; i < object.length; i++) {
            boolean v = object[i];
            builderResult[i] = resolverBoolean.assembleToString(v);
        }
        return resolverSplit.assembleToString(builderResult);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected RegexResolverToString resolverRegex;

        protected SplitResolverToArrayString resolverSplit;

        protected ResolverToBoolean resolverToBoolean;

        /**
         * Not required properties but if if <b>resolverSplit == null</b> then is Required
         */
        public ResolverToArrayBoolean.Builder resolverRegex(@NonNull RegexResolverToString resolverRegex) {
            this.resolverRegex = resolverRegex;
            return this;
        }

        public ResolverToArrayBoolean.Builder resolverSplit(@NonNull SplitResolverToArrayString resolverSplit) {
            this.resolverSplit = resolverSplit;
            return this;
        }

        public ResolverToArrayBoolean.Builder resolverToBoolean(@NonNull ResolverToBoolean resolverToBoolean) {
            this.resolverToBoolean = resolverToBoolean;
            return this;
        }

        public ResolverToArrayBoolean build() {
            ResolverToArrayBoolean ResolverToArrayBoolean = new ResolverToArrayBoolean();
            ResolverToArrayBoolean.resolverBoolean = Objects.requireNonNullElse(resolverToBoolean, new ResolverToBoolean());
            ResolverToArrayBoolean.resolverSplit = Objects.requireNonNullElseGet(resolverSplit,
                    () -> new SplitResolverToArrayString(Objects.requireNonNullElseGet(resolverRegex, RegexResolverToString::new)));
            return ResolverToArrayBoolean;
        }
    }
}
