package com.dragosghinea.royale.internal.utils.command.parameters;

import com.dragosghinea.royale.internal.utils.command.parameters.mapper.MapFromStack;
import com.dragosghinea.royale.internal.utils.exception.InvalidCommandParameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.EmptyStackException;
import java.util.Map;
import java.util.Stack;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResolveMethodParametersImpl implements ResolveMethodParameters {

    Map<Class<?>, MapFromStack<?>> mappers;

    @Override
    public Object[] processParametersFromString(Stack<String> args, Class<?>... parameterTypes) throws InvalidCommandParameter {
        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            // if not mid parameter parsing and no more arguments
            // fill with null
            if (args.isEmpty()) {
                parameters[i] = null;
                continue;
            }

            Class<?> parameterType = parameterTypes[i];
            MapFromStack<?> mapper = mappers.get(parameterType);

            if (mapper == null) {
                throw new IllegalArgumentException("No mapper found for parameter type: " + parameterType);
            }

            try {
                parameters[i] = mapper.map(args);
            } catch (InvalidCommandParameter e) {
                throw new InvalidCommandParameter(i, e.getParameterType(), e.getCause());
            } catch (EmptyStackException e) {
                throw new InvalidCommandParameter(i, parameterType, e);
            }
        }

        return parameters;
    }
}
