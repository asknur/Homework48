
import com.sun.net.httpserver.HttpExchange;
import server.BasicServer;
import server.ContentType;
import server.Cookie;
import server.ResponseCodes;
import utils.Utils;


import java.io.IOException;
import java.util.*;

public class VotingServer extends BasicServer {
    private final List<Candidate> candidates = CandidateStorage.getCandidates();

    public VotingServer(String host, int port) throws IOException {
        super(host, port);
        registerGet("/", this::handleCandidatesPage);
        registerPost("/vote", this::handleVote);
        registerGet("/thankyou", this::handleThanks);
        registerGet("/votes", this::handleVotes);
    }

    private void handleCandidatesPage(HttpExchange exchange) {
        Map<String, Object> model = new HashMap<>();
        model.put("candidates", candidates);
        renderTemplate(exchange, "candidates.ftlh", model);
    }


    private void handleVote(HttpExchange exchange) {
        Map<String, String> form = Utils.parseUrlEncoded(getBody(exchange), "&");
        int id = Integer.parseInt(form.getOrDefault("candidateId", "-1"));
        Optional<Candidate> c = CandidateStorage.getById(id);
        if (c.isPresent()) {
            c.get().addVote();
            CandidateStorage.save();
            Cookie cookie = new Cookie<>("voted", String.valueOf(id));
            cookie.setMaxAge(10);
            setCookie(exchange, cookie);
            redirect303(exchange, "/thankyou");
        } else {
            redirect303(exchange, "/");
        }
    }

    private void handleThanks(HttpExchange exchange) {
        String cookieStr = getCookies(exchange);
        Map<String, String> cookies = Cookie.parse(cookieStr);
        int id = Integer.parseInt(cookies.getOrDefault("voted", "-1"));

        Optional<Candidate> candidate = CandidateStorage.getById(id);
        if (candidate.isEmpty()) {
            respond404(exchange);
            return;
        }
        Candidate c = candidate.get();
        int totalVotes = candidates.stream().mapToInt(Candidate::getVotes).sum();
        int percent = totalVotes > 0 ? (c.getVotes() * 100 / totalVotes) : 0;

        Map<String, Object> model = new HashMap<>();
        model.put("candidate", c);
        model.put("percent", percent);
        renderTemplate(exchange, "thankyou.ftlh", model);
    }

    private void handleVotes(HttpExchange exchange) {
        List<Candidate> sorted = new ArrayList<>(candidates);
        sorted.sort(Comparator.comparing(Candidate::getVotes).reversed());
        int total = sorted.stream().mapToInt(Candidate::getVotes).sum();

        List<Map<String, Object>> voteList = new ArrayList<>();
        for (Candidate c : sorted) {
            int percent = total > 0 ? (c.getVotes() * 100 / total) : 0;
            Map<String, Object> map = new HashMap<>();
            map.put("photo", c.getPhoto());
            map.put("percent", percent);
            voteList.add(map);
        }
        Map<String, Object> model = new HashMap<>();
        model.put("votes", voteList);
        renderTemplate(exchange, "votes.ftlh", model);
    }




}
