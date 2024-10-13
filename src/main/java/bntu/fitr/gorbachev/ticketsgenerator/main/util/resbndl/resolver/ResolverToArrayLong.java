package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver;

import lombok.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResolverToArrayLong implements Resolver<long[]> {

    @Getter
    @Setter
    protected ResolverToLong resolverLong;

    @Getter
    @Setter
    protected SplitResolverToArrayString resolverSplit;

    @Override
    public long[] assemble(String value) {
        return Stream.of(resolverSplit.assemble(value)).mapToLong(resolverLong::assemble).toArray();
    }

    @Override
    public String assembleToString(long[] object) {
        return Arrays.stream(object).mapToObj(resolverLong::assembleToString)
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        list -> resolverSplit.assembleToString(list.toArray(String[]::new))));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected RegexResolverToString resolverRegex;

        protected SplitResolverToArrayString resolverSplit;

        protected ResolverToLong resolverToLong;

        /**
         * Not required properties but if if <b>resolverSplit == null</b> then is Required
         */
        public Builder resolverRegex(@NonNull RegexResolverToString resolverRegex) {
            this.resolverRegex = resolverRegex;
            return this;
        }

        public Builder resolverSplit(@NonNull SplitResolverToArrayString resolverSplit) {
            this.resolverSplit = resolverSplit;
            return this;
        }

        public Builder resolverToLong(@NonNull ResolverToLong resolverToLong) {
            this.resolverToLong = resolverToLong;
            return this;
        }

        public ResolverToArrayLong build() {
            ResolverToArrayLong resolverToArrayLong = new ResolverToArrayLong();
            resolverToArrayLong.resolverLong = Objects.requireNonNullElse(resolverToLong, new ResolverToLong());
            resolverToArrayLong.resolverSplit = Objects.requireNonNullElseGet(resolverSplit,
                    ()-> new SplitResolverToArrayString(Objects.requireNonNullElseGet(resolverRegex, RegexResolverToString::new)));
            return resolverToArrayLong;
        }

    }
}
