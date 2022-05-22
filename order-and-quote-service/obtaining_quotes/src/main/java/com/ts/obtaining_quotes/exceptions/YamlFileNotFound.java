package com.ts.obtaining_quotes.exceptions;

import java.io.FileNotFoundException;

public class YamlFileNotFound extends FileNotFoundException {
    public YamlFileNotFound(String s) {
        super(s);
    }
}
