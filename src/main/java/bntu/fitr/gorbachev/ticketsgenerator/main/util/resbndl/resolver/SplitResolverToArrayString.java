package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SplitResolverToArrayString implements Resolver<String[]> {
    private String regexSplit = ",";

    @Getter
    private RegexResolverToString regexResolver;

    public SplitResolverToArrayString(RegexResolverToString regexResolver) {
        this.regexResolver = regexResolver;
    }

    @Override
    public String[] assemble(String value) {
        return Arrays.stream(value.split(regexSplit))
                .map(String::trim)
                .map(regexResolver::assemble).toArray(String[]::new);
    }

    @Override
    public String assembleToString(String[] object) {
        return Stream.of(object).collect(Collectors.joining(regexSplit));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private RegexResolverToString resolverRegex;

        private String regexSplit;

        public Builder resolverRegex(RegexResolverToString resolverRegex) {
            this.resolverRegex = resolverRegex;
            return this;
        }

        public Builder regexSplit(String regexSplit) {
            this.regexSplit = regexSplit;
            return this;
        }

        public SplitResolverToArrayString build() {
            SplitResolverToArrayString resolver = new SplitResolverToArrayString();
            resolver.regexResolver = Objects.requireNonNullElseGet(resolverRegex, RegexResolverToString::new);
            resolver.regexSplit = Objects.requireNonNullElse(regexSplit, resolver.regexSplit);
            return resolver;
        }
    }
}
