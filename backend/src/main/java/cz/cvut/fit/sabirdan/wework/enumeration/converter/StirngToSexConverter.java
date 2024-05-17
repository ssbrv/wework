package cz.cvut.fit.sabirdan.wework.enumeration.converter;

import cz.cvut.fit.sabirdan.wework.enumeration.Sex;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

public class StirngToSexConverter implements Converter<String, Sex> {
    @Nullable
    @Override
    public Sex convert(String source) {
        if (source.isBlank() || source.equals("-"))
            return null;

        return EnumUtils.getEnum(Sex.class, source.toUpperCase());
    }
}
