package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.ReadableProperties;
import lombok.*;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolve situation:
 * For example you have properties file with properties value:
 * <p>
 * <b>app.directory</b>=.ticket-generator
 * <p>
 * <b>app.directory.serializer</b>=&{  app.directory }/serializer
 * <p>
 * Then value => <I>&{app.directory}/serializer</I> --> <i>.ticket-generator/serializer</i>
 * <p>
 * ___________________Example 1________________
 * <p>
 * <b>int.value=4</b>
 * <p>
 * <b>int.values= 1, 2, ${int.value}, 5</b>
 * <p>
 * <b>str.value = Hello Word</b>
 * <p>
 * <b>str.values = read, learn, Okey ${str.value}</b>
 * <p>
 * ___________________Example 2________________
 * <p>
 * <b>app.dir=&{sys: user.home}/${app.dir}/anyFolder</b>
 * </p>
 * <i>Result :: app.dir=User/userName/appdir/anyFolder</i>
 * <p>
 * <p>
 * Supported: ${} or &{} and you get access system properties from ${sys: systemProperty}
 * <p>
 * <b>Also supported nesting of keys</b>
 * <p>
 * <i>For example: </i> app.with.nesty=${str.values},${str.value},
 * where<p>
 * <b>str.values=String1, String2, ${str.value}, ${str.value2}</b>
 * <p>
 * <i>Res: [String1, String2, HiBrooo, AAAAA, Hello Word]</i>
 *
 * @version 05.10.2024
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)  // NoArgsConstructor = PUBLIC is very necessary
public class RegexResolverToString implements Resolver<String> {

    protected String regex = "[&$]\\{\\s*(sys\\s*:\\s*)?\\s*(.+?)\\s*}";

    @Setter // also need because if RegexResolverToString.properties == null, then this field should be initialized
    protected ReadableProperties properties;

    public RegexResolverToString(ReadableProperties properties) {
        this.properties = properties;
    }

    @Override
    public String assemble(String value) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        StringBuilder stringBuilder = new StringBuilder(value);
        while (matcher.find()) {
            String sysProp = matcher.group(1);
            String k = matcher.group(2);
            String v = null;
            if (sysProp != null) {
                v = System.getProperty(k);
            } else if (Objects.nonNull(getProperties())) {
                Object obj = getProperties().getProperties().getOrDefault(k, null);
                v = ((Objects.nonNull(obj)) ? ((obj instanceof String) ?
                        (String) obj : obj.toString()) : null);
            }
            if (v != null) {
                v = assemble(v); // This code solves the problem of nesting properties
                int indexStart = matcher.start();
                int indexEnd = matcher.end();
                String replaceKeyInstruction = value.substring(indexStart, indexEnd);
                indexStart = stringBuilder.indexOf(replaceKeyInstruction);
                indexEnd = indexStart + replaceKeyInstruction.length();
                stringBuilder.replace(indexStart, indexEnd, v);
            }
        }
        return stringBuilder.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String assembleToString(String object) {
        return object;
    }

    public static class Builder {

        protected ReadableProperties properties;

        protected String regex;

        public Builder properties(@NonNull ReadableProperties properties) {
            this.properties = properties;
            return this;
        }

        public Builder regex(@NonNull String regex) {
            this.regex = regex;
            return this;
        }

        /**
         * This method requires mandatory initialize filed <code>Builder.properties</code>, else throw NullPointerException
         * <p>
         * This method don't allow to create RegexResolverToString with filed properties == null
         */
        public RegexResolverToString build() {
            RegexResolverToString resolver = new RegexResolverToString(); // resolver.properties is null because constructor with no param
            resolver.properties = Objects.requireNonNull(properties);
            resolver.regex = Objects.requireNonNullElse(regex, resolver.regex);
            return resolver;
        }

        /**
         * This build method allows to create RegexResolverToString with field
         * <p>
         * <code><i>RegexResolverToString.properties</i> == null</code>
         */
        public RegexResolverToString buildNullable() {
            RegexResolverToString resolver = new RegexResolverToString(); // resolver.properties is null because constructor with no param
            resolver.properties = (properties != null) ? properties : (null);
            resolver.regex = (regex != null) ? regex : resolver.regex;
            return resolver;
        }

    }
}
