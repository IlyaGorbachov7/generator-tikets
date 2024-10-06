package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public class SplitResolveToArrayString implements Resolver<String[]> {
    private String regexSplit = ",";

    @Getter
    private RegexResolverToString regexResolver;

    public SplitResolveToArrayString(RegexResolverToString regexResolver) {
        this.regexResolver = regexResolver;
    }

    @Override
    public String[] assemble(String value) {
        return Arrays.stream(value.split(regexSplit))
                .map(String::trim)
                .map(regexResolver::assemble).toArray(String[]::new);
    }
}
