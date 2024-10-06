package bntu.fitr.gorbachev.ticketsgenerator.main.util.resbndl.resolver;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder(builderClassName = "Builder")
@AllArgsConstructor
public class ResolverToLong implements Resolver<Long> {
    @Override
    public Long assemble(String value) {
        return Long.parseLong(value);
    }

    @Override
    public String assembleToString(Long object) {
        return String.valueOf(object);
    }
}
