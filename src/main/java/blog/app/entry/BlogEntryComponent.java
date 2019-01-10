package blog.app.entry;

import blog.app.service.BlogEntry;
import ngoy.core.Component;
import ngoy.core.Input;

@Component(selector = "blog-entry", templateUrl = "blog-entry.component.html", styleUrls = { "blog-entry.component.css" })
public class BlogEntryComponent {
	@Input
	public BlogEntry entry;
}
