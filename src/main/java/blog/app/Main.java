package blog.app;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static ngoy.core.Provider.useValue;

import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import blog.app.detail.DetailComponent;
import blog.app.home.HomeComponent;
import blog.app.service.BlogService;
import ngoy.Ngoy;
import ngoy.router.Location;
import ngoy.router.RouterConfig;
import ngoy.router.RouterModule;

@Controller
@RequestMapping({ "/*", "/detail/*" })
public class Main implements InitializingBean {
	private Ngoy<AppComponent> ngoy;

	@Autowired
	private HttpServletRequest request;

	@GetMapping()
	public void home(HttpServletResponse response) throws Exception {
		// re-create while developing to have changes picked-up
//		System.out.print("requestUri:" + request.getRequestURI());
		createApp();
		ngoy.render(response.getOutputStream());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		createApp();
	}

	private void createApp() {
		RouterConfig routerConfig = RouterConfig //
				.baseHref("/")
				.location(useValue(Location.class, () -> request.getRequestURI()))
				.route("index", HomeComponent.class)
				.route("detail/:id", DetailComponent.class)
				.build();

		ngoy = Ngoy.app(AppComponent.class)
				.modules(RouterModule.forRoot(routerConfig))
				.modules(Main.class.getPackage())
				.build();
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.createApp();

		BlogService blogService = main.ngoy.getInjector()
				.get(BlogService.class);

		List<String> paths = blogService.getEntries()
				.stream()
				.map(entry -> format("/detail/%s", entry.get("id")))
				.collect(toList());
		paths.add("/index");

		main.ngoy.renderSite(Paths.get("docs"), paths.toArray(new String[paths.size()]));
	}
}