package org.keyin.User;

import org.keyin.BinarySearchTree;
import org.keyin.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserInputController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/enter-numbers")
    public String showUserInputPage() {
        return "enter-numbers";
    }
    @GetMapping("/")
    public ModelAndView showHomePage() {
        ModelAndView modelAndView = new ModelAndView("index"); // Return the name of your homepage Thymeleaf template

        return modelAndView;
    }
    @PostMapping("/process-numbers")
    public String processUserInput(@RequestParam("numbers") String numbers, Model model) {

        System.out.println("User input numbers: " + numbers);


        BinarySearchTree bst = new BinarySearchTree();
        String[] numberArray = numbers.split("\\s+");
        for (String number : numberArray) {
            try {
                int value = Integer.parseInt(number);
                bst.insert(value);
            } catch (NumberFormatException e) {

            }
        }


        String treeJson = bst.toJson();


        User user = new User();
        user.setUserInput(numbers);
        user.setTree(treeJson);
        userRepository.save(user);


        model.addAttribute("jsonData", treeJson);

        return "process-numbers"; // Show the "process-numbers" page with JSON data
    }

    @GetMapping("/previous-trees")
    public String showPreviousTrees(Model model) {

        List<User> userInputList = userRepository.findAll();


        List<String> treeJsonList = new ArrayList<>();


        for (User userInput : userInputList) {
            BinarySearchTree bst = new BinarySearchTree();
            String[] numberArray = userInput.getUserInput().split("\\s+");
            for (String number : numberArray) {
                try {
                    int value = Integer.parseInt(number);
                    bst.insert(value);
                } catch (NumberFormatException e) {

                }
            }
            String treeJson = bst.toJson();
            treeJsonList.add(treeJson);
        }


        model.addAttribute("treeJsonList", treeJsonList);

        return "previous-trees";
    }
}

