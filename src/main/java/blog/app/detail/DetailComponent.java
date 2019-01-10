package blog.app.detail;

import blog.app.service.BlogEntry;
import blog.app.service.BlogService;
import ngoy.core.Component;
import ngoy.core.Inject;
import ngoy.core.OnInit;
import ngoy.router.RouteParams;

@Component(selector = "blog-detail", templateUrl = "detail.component.html", styleUrls = { "detail.component.css" })
public class DetailComponent implements OnInit {
	@Inject
	public RouteParams routeParams;

	@Inject
	public BlogService blogService;

	public BlogEntry entry;

	@Override
	public void ngOnInit() {
		entry = blogService.getEntry(routeParams.get("id"));
	}
}
