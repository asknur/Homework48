import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;

public class CandidateStorage {
    private static final Path PATH = Paths.get("data/candidates.json");
    private static final Gson gson = new Gson();
    private static List<Candidate> candidates = loadCandidates();

    private static List<Candidate> loadCandidates() {
        try {
            if (Files.notExists(PATH)) return new ArrayList<>();
            String json = Files.readString(PATH);
            Type type = new TypeToken<List<Candidate>>() {}.getType();
            return gson.fromJson(json, type);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Candidate> getCandidates() {
        return candidates;
    }

    public static Optional<Candidate> getById(int id) {
        if (id < 0 || id >= candidates.size()) return Optional.empty();
        return Optional.of(candidates.get(id));
    }

    public static void save() {
        try {
            String json = gson.toJson(candidates);
            Files.writeString(PATH, json, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
