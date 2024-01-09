package org.tinyurl;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.mongodb.morphia.Datastore;
import org.tinyurl.config.Configurations;
import org.tinyurl.config.MongoUtils;
import org.tinyurl.dao.TinyurlDAO;
import org.tinyurl.entity.Tinyurl;
import org.tinyurl.resource.TinyurlResource;
import org.tinyurl.service.ITinyurlService;
import org.tinyurl.service.TinyurlService;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class Main extends Application<Configurations> {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        try {
            main.run(args);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void configureCors(Environment environment) {
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_HEADERS_HEADER, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_METHODS_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,x-access-token");
        filter.setInitParameter("allowCredentials", "true");
    }

    @Override
    public void run(Configurations configurations, Environment environment) {
        Datastore ds = MongoUtils.createDatastore(configurations.getMongoDBConfig());

        TinyurlDAO tinyurlDAO = new TinyurlDAO(ds);
//        TinyurlDAO tinyurlDAO = new TinyurlDAO(hibernate.getSessionFactory());

        ITinyurlService tinyurlService = new TinyurlService(tinyurlDAO);

        TinyurlResource tinyurlResource = new TinyurlResource(tinyurlService);
        environment.jersey().register(tinyurlResource);
        configureCors(environment);
    }

    @Override
    public void initialize(Bootstrap<Configurations> bootstrap) {
//        bootstrap.addBundle(hibernate);
    }

//    private final HibernateBundle<Configurations> hibernate = new HibernateBundle<Configurations>(Tinyurl.class) {
//        @Override
//        public DataSourceFactory getDataSourceFactory(Configurations configuration) {
//            return configuration.getDataSourceFactory();
//        }
//    };
}