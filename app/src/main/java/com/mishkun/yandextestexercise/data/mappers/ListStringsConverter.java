package com.mishkun.yandextestexercise.data.mappers;

import java.util.ArrayList;
import java.util.List;

import io.requery.Converter;
import io.requery.Nullable;

/**
 * Created by Mishkun on 21.04.2017.
 */

public class ListStringsConverter implements Converter<ArrayList<String>, String> {
    @Override
    @SuppressWarnings("unchecked")
    public Class<ArrayList<String>> getMappedType() {
        return (Class) ArrayList.class;
    }

    @Override
    public Class<String> getPersistedType() {
        return String.class;
    }

    @Override
    public Integer getPersistedSize() {
        return null;
    }

    @Override
    public String convertToPersisted(ArrayList<String> value) {
        if (value == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (Object integer : value) {
            if (index > 0) {
                sb.append(";");
            }
            sb.append(integer);
            index++;
        }
        return sb.toString();
    }

    @Override
    public ArrayList<String> convertToMapped(Class<? extends ArrayList<String>> type, String value) {
        ArrayList<String> list = new ArrayList<>();
        if (value != null) {
            String[] parts = value.split(";");
            for (String part : parts) {
                if (part.length() > 0) {
                    list.add(part.trim());
                }
            }
        }
        return list;
    }
}
