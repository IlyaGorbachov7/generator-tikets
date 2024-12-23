package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder(builderClassName = "Builder")
@AllArgsConstructor
public class ResolverToDouble implements Resolver<Double> {

    @Override
    public Double assemble(String value) {
        return Double.parseDouble(value);
    }

    @Override
    public String assembleToString(Double object) {
        return String.valueOf(object.doubleValue());
    }
}
