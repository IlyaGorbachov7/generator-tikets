package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver;

import lombok.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResolverToArrayDouble implements Resolver<double[]> {

    @Getter
    @Setter
    protected ResolverToDouble resolverDouble;

    @Getter
    @Setter
    protected SplitResolverToArrayString resolverSplit;


    @Override
    public double[] assemble(String value) {
        value = value.trim();
        return Stream.of(resolverSplit.assemble(value)).mapToDouble(resolverDouble::assemble).toArray();
    }

    @Override
    public String assembleToString(double[] object) {
        return Arrays.stream(object).mapToObj(resolverDouble::assembleToString)
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        list -> resolverSplit.assembleToString(list.toArray(String[]::new))));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        protected RegexResolverToString resolverRegex;

        protected SplitResolverToArrayString resolverSplit;

        protected ResolverToDouble resolverToDouble;


        /**
         * Not required properties but if if <b>resolverSplit == null</b> then is Required
         */
        public ResolverToArrayDouble.Builder resolverRegex(@NonNull RegexResolverToString resolverRegex) {
            this.resolverRegex = resolverRegex;
            return this;
        }

        public ResolverToArrayDouble.Builder resolverSplit(@NonNull SplitResolverToArrayString resolverSplit) {
            this.resolverSplit = resolverSplit;
            return this;
        }

        public ResolverToArrayDouble.Builder resolverToDouble(@NonNull ResolverToDouble resolverToDouble) {
            this.resolverToDouble = resolverToDouble;
            return this;
        }

        public ResolverToArrayDouble build() {
            ResolverToArrayDouble resolverToArrayDouble = new ResolverToArrayDouble();
            resolverToArrayDouble.resolverDouble = Objects.requireNonNullElse(resolverToDouble, new ResolverToDouble());
            resolverToArrayDouble.resolverSplit = Objects.requireNonNullElseGet(resolverSplit,
                    () -> new SplitResolverToArrayString(Objects.requireNonNullElseGet(resolverRegex, RegexResolverToString::new)));
            return resolverToArrayDouble;
        }
    }
}
