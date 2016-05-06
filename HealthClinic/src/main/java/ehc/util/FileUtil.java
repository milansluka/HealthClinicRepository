package ehc.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtil {

	public static String generateFile(String directoryFullPath,
			String fileName, byte[] fileContents) {
		return generateFile(directoryFullPath, fileName, fileContents, false);
	}

	public static String generateFile(String directoryFullPath,
			String fileName, byte[] fileContents, boolean replaceExisting) {
		try {
			File  f = new File(directoryFullPath);
			f.mkdirs();
			Path target = Paths.get(directoryFullPath + fileName);
			ByteArrayInputStream in = new ByteArrayInputStream(fileContents);
			if (replaceExisting) {
				Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
			} else {
				Files.copy(in, target);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return directoryFullPath + fileName;
	}
}
