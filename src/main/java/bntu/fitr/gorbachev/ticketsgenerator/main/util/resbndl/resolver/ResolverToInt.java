package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder(builderClassName = "Builder")
@AllArgsConstructor
public class ResolverToInt implements  Resolver<Integer>{
    @Override
    public Integer assemble(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public String assembleToString(Integer object) {
        return String.valueOf(object);
    }
}