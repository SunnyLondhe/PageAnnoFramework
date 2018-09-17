package edu.srh.restapi;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;

import edu.srh.api.json.JsonMapper;
import edu.srh.api.json.JsonMapperImp;
import edu.srh.exceptions.AnnotationExceptions;

public class TestJSON {
	public static void main(String[] args) throws IOException, RepositoryException, RepositoryConfigException, IllegalAccessException, InstantiationException, AnnotationExceptions {
//		String content = readFile("resources/test.json", StandardCharsets.UTF_8);
		String content = readFile("resources/danah.json", StandardCharsets.UTF_8);
//		String content = readFile("resources/jsonldfull.json", StandardCharsets.UTF_8);
//		String content = readFile("resources/annojson.json", StandardCharsets.UTF_8);
		JsonMapper jsonMapper = new JsonMapperImp();
		jsonMapper.parseJson(content);
		
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
