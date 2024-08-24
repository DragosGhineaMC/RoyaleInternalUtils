package com.dragosghinea.royale.internal.utils.time.format;

import lombok.RequiredArgsConstructor;

import java.text.NumberFormat;
import java.util.Map;

@RequiredArgsConstructor
public class TimeCountdownImpl implements TimeCountdown {

    private final NumberFormat numberFormat = NumberFormat.getInstance();

    private final Map<String, String> names;

    public String format(long timeInSeconds, boolean useSpaces, TimeDisplay... whatToDisplay) {
        StringBuilder result = new StringBuilder();
        for (TimeDisplay timeDisplay : whatToDisplay) {
            long amount = timeDisplay.getAmount(timeInSeconds);
            if (amount > 0) {
                String name = amount == 1 ?
                        names.getOrDefault(timeDisplay.getSingularName(), timeDisplay.getSingularName())
                        :
                        names.getOrDefault(timeDisplay.getPluralName(), timeDisplay.getPluralName());

                if (useSpaces)
                    result.append(numberFormat.format(amount)).append(" ").append(name).append(" ");
                else
                    result.append(numberFormat.format(amount)).append(name);

                timeInSeconds -= amount * timeDisplay.getNumberOfSeconds();
            }
        }

        // remove trailing space
        if (result.length() != 0 && result.charAt(result.length() - 1) == ' ')
            result.deleteCharAt(result.length() - 1);

        return result.toString();
    }

    @Override
    public String formatFirstAvailable(long timeInSeconds, boolean useSpaces, TimeDisplay... whatToDisplay) {
        for (TimeDisplay timeDisplay : whatToDisplay) {
            long amount = timeDisplay.getAmount(timeInSeconds);
            if (amount > 0) {
                String name = amount == 1 ?
                        names.getOrDefault(timeDisplay.getSingularName(), timeDisplay.getSingularName())
                        :
                        names.getOrDefault(timeDisplay.getPluralName(), timeDisplay.getPluralName());

                if (useSpaces)
                    return numberFormat.format(amount) + " " + name;

                return numberFormat.format(amount) + name;
            }
        }

        return "";
    }
}
