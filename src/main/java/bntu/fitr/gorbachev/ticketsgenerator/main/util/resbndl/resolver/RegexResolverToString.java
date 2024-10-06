package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver;

import bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.ReadableProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
@AllArgsConstructor
public class RegexResolverToString implements Resolver<String> {

    private String regex = "[&$]\\{\\s*(sys\\s*:\\s*)?\\s*(.+?)\\s*}";

    private ReadableProperties properties;

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
            String v;
            if (sysProp != null) {
                v = System.getProperty(k);
            } else {
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
}