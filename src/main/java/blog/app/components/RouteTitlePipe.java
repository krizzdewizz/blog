package blog.app.components;

import blog.app.service.BlogEntry;
import blog.app.service.BlogService;
import ngoy.core.Inject;
import ngoy.core.Pipe;
import ngoy.core.PipeTransform;
import ngoy.router.Route;
import ngoy.router.RouteParams;

@Pipe("routeTitle")
public class RouteTitlePipe implements PipeTransform {
	@Inject
	public BlogService blogService;

	@Inject
	public RouteParams routeParams;

	@Override
	public Object transform(Object obj, Object... args) {
		Route route = (Route) obj;
		String path = route.getPath();

		if (path.equals("index")) {
			return "Home";
		} else if (path.startsWith("detail")) {
			BlogEntry entry = blogService.getEntry(routeParams.get("id"));
			if (entry != null) {
				return entry.get("title");
			}
		}

		return "";
	}
}
