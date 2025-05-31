module com.qb.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires org.hibernate.orm.core; // Hibernate ORM Dependency
    requires org.hibernate.validator; // Hibernate ORM Dependency
    requires java.sql; // Hibernate ORM Dependency
    requires jakarta.persistence; // Hibernate ORM Dependency
    
    requires de.mkammerer.argon2; // Password encryption dependancy
    requires de.mkammerer.argon2.nolibs; // Password encryption dependancy
    requires org.apache.commons.lang3; // random number generator
    requires com.sun.jna;
    
    requires java.naming;
    requires java.base;    
    requires java.desktop;
    requires com.github.weisj.jsvg;
    requires com.jfoenix;
    
    requires jasperreports;
    requires jasperreports.fonts;
    requires commons.beanutils;
    requires barbecue;

    opens com.qb.app to javafx.fxml;
    opens com.qb.app.controllers to javafx.fxml;
    opens com.qb.app.controllers.report to javafx.fxml;
    opens com.qb.app.controllers.report.beans to 
        javafx.fxml, 
        jasperreports,
        commons.beanutils;
    opens com.qb.app.model to javafx.fxml, javafx.base;
    opens com.qb.app.model.entity to org.hibernate.orm.core;
    exports com.qb.app;
    exports com.qb.app.model;
    exports com.qb.app.controllers.report.beans;
}
