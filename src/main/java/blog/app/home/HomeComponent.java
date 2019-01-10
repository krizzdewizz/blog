package blog.app.home;

import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import blog.app.service.BlogEntry;
import blog.app.service.BlogService;
import ngoy.core.Component;
import ngoy.core.Inject;

@Component(selector = "home", templateUrl = "home.component.html", styleUrls = { "home.component.css" })
public class HomeComponent {
	public static final Set<String> TOC_EXCLUDE = new HashSet<>(asList( //
	));

	@Inject
	public BlogService blogService;

	public Collection<BlogEntry> getEntries() {
		return blogService.getEntries();
	}
}
