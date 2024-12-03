package com.example.springsecurityexample.match;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.Arrays;

//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class MatchResource extends EntityModel<Match> {
    public MatchResource(Match match, Link... links) {
        super(match, Arrays.asList(links));
        //add(linkTo(MatchController.class).slash(match.getUid1()).withSelfRel());
    }

}
