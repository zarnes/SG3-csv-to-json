package com.converter;

public class JsonElementException extends Exception
{
    JsonElementException(String e)
    {
        MainFrame.log.append("Ligne " + MainFrame.currentLine + " : " + e + "\n");
    }
}
