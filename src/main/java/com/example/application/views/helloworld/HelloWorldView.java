package com.example.application.views.helloworld;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

@PageTitle("Hello World")
@Route(value = "")
@PermitAll
public class HelloWorldView extends HorizontalLayout {

    public HelloWorldView(AuthenticationContext authenticationContext) {
        add(new Span("Principal Name: " + authenticationContext.getPrincipalName().orElse("N/A")));
        add(new Span("Roles: " + String.join(", ", authenticationContext.getGrantedRoles())));
    }

}
