package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder(builderClassName = "Builder")
@AllArgsConstructor
public class ResolverToBoolean implements Resolver<Boolean>{
    @Override
    public Boolean assemble(String value) {
        value = value.trim();
        return Boolean.parseBoolean(value);
    }

    @Override
    public String assembleToString(Boolean object) {
        return String.valueOf(object.booleanValue());
    }
}
