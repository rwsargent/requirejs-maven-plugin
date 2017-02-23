package com.github.bringking.maven.requirejs.sm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;

import com.google.gson.Gson;

public class Skipper {

	private static final String PATH_DATE_DELIMITER = "|";
	private static final String DATA_DELIMITER = "\n";
	private static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	private Log mLogger;

	public Skipper(Log logger) {
		mLogger = logger;
	}

	public boolean shouldSkip(File buildProfile) throws IOException {
		Path srcDirectory = null;
		File cacheFile = null;
		boolean shouldSkip = true;
		try {
			BuildProfile profile = convertToBuildProfile(buildProfile);
			String buildDirectory = buildProfile.getParentFile().toString();
			srcDirectory = Paths.get(buildDirectory, profile.appDir, profile.baseUrl);
			if (profile.cacheFile == null) {
				mLogger.info("No cache file path provided");
				shouldSkip = false;
			}
			cacheFile = Paths.get(buildDirectory, profile.cacheFile).toFile();
			if (!cacheFile.exists()) {
				mLogger.info("No cache file as specified location: " + cacheFile.getAbsolutePath());
				shouldSkip = false;
			}
		} catch (IOException e) {
			mLogger.info(e.getMessage());
			shouldSkip = false;
		}

		FileWalker fileWalker = new FileWalker();
		Files.walkFileTree(srcDirectory, fileWalker);
		Map<String, Date> attributes = fileWalker.getFileAttributes();
		Map<String, Date> cached = readCacheFile(cacheFile);
		shouldSkip &= !hasFileBeenUpdated(attributes, cached);
		writeCachedFile(cacheFile, attributes);
		return shouldSkip;
	}

	private BuildProfile convertToBuildProfile(File buildProfile) throws FileNotFoundException, IOException {
		Gson gson = new Gson();
		BuildProfile profile = null;
		try (FileReader fileReader = new FileReader(buildProfile)) {
			String json = FileUtils.readFileToString(buildProfile);
			json = json.trim().substring(1, json.length() - 2);
			profile = gson.fromJson(json, BuildProfile.class);
		}
		return profile;
	}

	private boolean hasFileBeenUpdated(Map<String, Date> attributes, Map<String, Date> cached) {
		for (Entry<String, Date> entry : attributes.entrySet()) {
			Date currentModification = entry.getValue();
			Date cachedModification = cached.get(entry.getKey());
			if (cachedModification == null) {
				System.out.println("No date for key: " + entry.getKey());
			} else {
				if (currentModification.compareTo(cachedModification) > 0) {
					return true;
				}
			}
		}
		return false;
	}

	private Map<String, Date> readCacheFile(File cacheFile) {
		Map<String, Date> cache = new HashMap<>();
		SimpleDateFormat dateFormatter = new SimpleDateFormat(ISO_8601_FORMAT);
		try (Scanner sc = new Scanner(new FileInputStream(cacheFile))) {
			sc.useDelimiter(DATA_DELIMITER);
			while (sc.hasNext()) {
				String[] tokens = sc.next().split("\\|"); // this is a regular
															// expression, so I
															// have to escape
															// the pipe
				try {
					Date modifed = dateFormatter.parse(tokens[1]);
					cache.put(tokens[0], modifed);
				} catch (ParseException e) {
					mLogger.error("Couldn't parse date for file: " + tokens[0]);
				}
			}
		} catch (FileNotFoundException e) {
			// Should Never Happen™
		}
		return cache;
	}

	private void writeCachedFile(File cacheFile, Map<String, Date> fileAttributes) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(ISO_8601_FORMAT);
		try (FileWriter fw = new FileWriter(cacheFile)) {
			for (String path : fileAttributes.keySet()) {
				String modifiedDate = dateFormatter.format(fileAttributes.get(path));
				fw.write(path + PATH_DATE_DELIMITER + modifiedDate);
				fw.write(DATA_DELIMITER);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class FileWalker implements FileVisitor<Path> {

		Map<String, Date> fileAttributes = new HashMap<>();

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			fileAttributes.put(file.toString(), Date.from(attrs.lastModifiedTime().toInstant()));
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		public Map<String, Date> getFileAttributes() {
			return fileAttributes;
		}

	}
}
