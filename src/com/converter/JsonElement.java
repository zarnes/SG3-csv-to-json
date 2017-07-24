package com.converter;

abstract class JsonElement
{
    abstract String toJson(int offset);

    protected String addChars(char c, int n)
    {
        String chars = "";
        for (int i = 0; i < n; ++i)
            chars += c;
        return chars;
    }
}
