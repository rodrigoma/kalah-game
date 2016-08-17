package com.marcosbarbero.kalah.web.resources;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Index request handler.
 *
 * @author Marcos Barbero
 */
@Controller
public class IndexController {

    @GetMapping({"/", "index"})
    public ModelAndView index() {
        return new ModelAndView("index");
    }
}
