package org.elsys.ip.servlet.calculator;

import org.elsys.ip.calculator.CommandExecutor;
import org.elsys.ip.calculator.CommandFactory;
import org.elsys.ip.calculator.Memory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CalculatorServlet extends HttpServlet {
    private final Map<String, CalculatorCore> userCalculators = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo() == null ? "" : req.getPathInfo();
        List<String> collect = Arrays.stream(path.split("/")).filter(s -> !s.equals("")).collect(Collectors.toList());
        if (collect.size() == 0) {
            resp.setStatus(404);
            return;
        }

        String command = collect.get(0);
        List<String> args = collect.stream().skip(1).collect(Collectors.toList());

        String userName = "default";
        if (req.getQueryString() != null) {
            Optional<String> optionalUser = Arrays.stream(req.getQueryString().split("&")).filter(s -> s.startsWith("user=")).findFirst();
            if (optionalUser.isPresent()) {
                userName = optionalUser.get().replace("user=", "");
            }
        }

        if (!userCalculators.containsKey(userName)) {
            userCalculators.put(userName, new CalculatorCore());
        }

        String result = userCalculators.get(userName).execute(command, args);
        resp.getWriter().print(result);
    }
}
