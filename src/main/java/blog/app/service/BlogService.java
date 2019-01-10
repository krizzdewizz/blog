package blog.app.service;

import static java.lang.String.format;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import ngoy.core.Injectable;

@Injectable
public class BlogService {

	public BlogEntry getEntry(String id) {
		return getEntriesMap().get(id);
	}

	public Collection<BlogEntry> getEntries() {
		return getEntriesMap().values();
	}

	private Map<String, BlogEntry> getEntriesMap() {

		String baseUrl = "/blog/entries/";

		try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(format("%sindex.properties", baseUrl))))) {

			String line;
			BlogEntry entry = null;
			Map<String, BlogEntry> all = new LinkedHashMap<>();

			while ((line = in.readLine()) != null) {

				line = line.trim();
				if (line.isEmpty()) {
					continue;
				}

				if (line.startsWith("[")) {
					entry = new BlogEntry();
					String title = line.substring(1, line.length() - 1);
					String id = toId(title);
					entry.put("title", title);
					entry.put("id", id);
					if (all.containsKey(id)) {
						throw new RuntimeException(format("error while parsing blog entries: duplicate title/id: %s", id));
					}
					all.put(id, entry);
				} else {

					if (entry == null) {
						throw new RuntimeException("error while parsing blog entries: missing section []");
					}

					int pos = line.indexOf('=');
					if (pos < 0) {
						throw new RuntimeException(format("error while parsing blog entries: missing = in line '%s'", line));
					}
					String prop = line.substring(0, pos);
					String value = line.substring(pos + 1);
					entry.put(prop, prop.equals("url") ? format("%s%s", baseUrl, value) : value);
				}
			}
			return all;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String toId(String title) {
		return title.replaceAll("\\s", "")
				.toLowerCase();
	}
}
