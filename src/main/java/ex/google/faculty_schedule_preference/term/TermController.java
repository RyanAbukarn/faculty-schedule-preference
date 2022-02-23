package ex.google.faculty_schedule_preference.term;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TermController {
    private final TermRepository repository;

    TermController(TermRepository repository) {
        this.repository = repository;
    }

    @GetMapping("terms")
    public String index(Model model) {
        model.addAttribute("terms", repository.findAll());
        return "term/index";
    }

    @GetMapping("terms/new")
    public String new_(Model model) {
        model.addAttribute("term", new Term());
        return "term/new";
    }

    @PostMapping("terms/create")
    public String create(@ModelAttribute Term term, Model model) {
        repository.save(term);
        return "redirect:/terms";
    }

}
