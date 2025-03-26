package pl.smartCommute.app.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class TestUtils {

    public static String getJsonResponseFromResource(final String filePath) throws IOException{
        final File file = new File(TestUtils.class.getResource(filePath).getFile());
        final String response = FileUtils.readFileToString(file,"UTF-8");
        return response;
    }
}
