package com.converter;

abstract class JsonElement
{
    abstract String toJson(int offset);

    String addTabs(int n)
    {
        StringBuilder chars = new StringBuilder();
        for (int i = 0; i < n; ++i)
            chars.append('\t');
        return chars.toString();
    }
}
