package com.marcosbarbero.kalah.web.resources;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller to bind the service layer to view layer of the game.
 *
 * @author Marcos Barbero
 */
@Slf4j
@Controller
public class GameController {

    @GetMapping("/game")
    public ModelAndView start(@RequestParam Integer stones, ModelAndView view) {
        view.addObject("stones", stones);
        return view;
    }

}
