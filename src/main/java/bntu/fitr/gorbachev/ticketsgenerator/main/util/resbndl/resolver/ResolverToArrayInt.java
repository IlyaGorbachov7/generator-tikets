package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResolverToArrayInt implements Resolver<int[]> {

    ResolverToInt resolverInt;

    SplitResolverToArrayString resolverSplit;

    @Override
    public int[] assemble(String value) {
        return Stream.of(resolverSplit.assemble(value)).mapToInt(resolverInt::assemble).toArray();
    }

    @Override
    public String assembleToString(int[] object) {
        return Arrays.stream(object).mapToObj(resolverInt::assembleToString)
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        list -> resolverSplit.assembleToString(list.toArray(String[]::new))));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        RegexResolverToString resolverRegex;

        SplitResolverToArrayString resolverSplit;

        ResolverToInt resolverToInt;

        /**
         * Not required properties but if if <b>resolverSplit == null</b> then is Required
         */
        public Builder resolverRegex(RegexResolverToString resolverRegex) {
            this.resolverRegex = resolverRegex;
            return this;
        }

        public Builder resolverSplit(SplitResolverToArrayString resolverSplit) {
            this.resolverSplit = resolverSplit;
            return this;
        }

        public Builder resolverToInt(ResolverToInt resolverToTnt) {
            this.resolverToInt = resolverToTnt;
            return this;
        }

        public ResolverToArrayInt build() {
            ResolverToArrayInt resolverToArrayInt = new ResolverToArrayInt();
            resolverToArrayInt.resolverInt = Objects.requireNonNullElse(resolverToInt, new ResolverToInt());
            resolverToArrayInt.resolverSplit = Objects.requireNonNullElseGet(resolverSplit,
                    ()-> new SplitResolverToArrayString(Objects.requireNonNullElseGet(resolverRegex, RegexResolverToString::new)));
            return resolverToArrayInt;
        }
    }
}