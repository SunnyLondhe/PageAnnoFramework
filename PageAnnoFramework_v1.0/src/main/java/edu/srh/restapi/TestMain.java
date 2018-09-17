package edu.srh.restapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;

import edu.srh.api.page.PageAnnotationGenerator;
import edu.srh.api.page.PageAnnotationGeneratorImpl;
import edu.srh.exceptions.AnnotationExceptions;

public class TestMain {

	public static void main(String[] args) throws RepositoryException, RepositoryConfigException {

		StringWriter writer = new StringWriter();
		File folder = new File("resources/page_images/");
		File[] xmlFile = folder.listFiles();
		// File xmlFile = new File("resources/PAGE2017xmlWithUnitFull.xml");

		for (File file : xmlFile) {
			if (file.isFile() && getFileExtensionName(file).indexOf("xml") != -1) {
				try (BufferedReader br = new BufferedReader(new FileReader(file.getPath()))) {
					String line;
					while ((line = br.readLine()) != null) {
						writer.append(line);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				PageAnnotationGenerator generator = new PageAnnotationGeneratorImpl();
				try {
					generator.parseAnnotations("http://example.org/source1", writer.toString());
				} catch (AnnotationExceptions e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static String getFileExtensionName(File f) {
		if (f.getName().indexOf(".") == -1) {
			return "";
		} else {
			return f.getName().substring(f.getName().length() - 3, f.getName().length());
		}
	}

}
