package bntu.fitr.gorbachev.ticketsgenerator.main.util.loc;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.ReadableProperties;
import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver.RegexResolverToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
@AllArgsConstructor
public class LocaleResolver extends RegexResolverToString {

    private String regexRelatedKey = "#(\\d+)";

    public LocaleResolver(ReadableProperties properties) {
        super(properties);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String assemble(String value, String... relatedValues) {
        Pattern pattern = Pattern.compile(regexRelatedKey);
        Matcher matcher = pattern.matcher(value);
        StringBuilder stringBuilder = new StringBuilder(value);
        int inter = 0;
        if (relatedValues != null) {
            while (matcher.find()) {
                String v = "";
                if (inter < relatedValues.length) {
                    v = relatedValues[inter++];
                }
                int indexStart = matcher.start();
                int indexEnd = matcher.end();
                String replaceKeyInstruction = value.substring(indexStart, indexEnd);
                indexStart = stringBuilder.indexOf(replaceKeyInstruction);
                indexEnd = indexStart + replaceKeyInstruction.length();
                stringBuilder.replace(indexStart, indexEnd, v);

            }
        }
        return assemble(stringBuilder.toString());
    }

    public static class Builder extends RegexResolverToString.Builder {
        @Override
        public LocaleResolver.Builder properties(@NonNull ReadableProperties properties) {
            return (Builder) super.properties(properties);
        }

        @Override
        public LocaleResolver.Builder regex(@NonNull String regex) {
            return (Builder) super.regex(regex);
        }

        @Override
        public LocaleResolver build() {
            LocaleResolver resolver = new LocaleResolver(); // resolver.properties is null because constructor with no param
            resolver.properties = Objects.requireNonNull(properties);
            resolver.regex = Objects.requireNonNullElse(regex, resolver.regex);
            return resolver;
        }

        @Override
        public LocaleResolver buildNullable() {
            LocaleResolver resolver = new LocaleResolver(); // resolver.properties is null because constructor with no param
            resolver.properties = (properties != null) ? properties : (null);
            resolver.regex = (regex != null) ? regex : resolver.regex;
            return resolver;
        }
    }
}
