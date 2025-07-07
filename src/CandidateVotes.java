public class CandidateVotes {
    private Candidate candidate;
    private String percentage;

    public CandidateVotes(Candidate candidate, String percentage) {
        this.candidate = candidate;
        this.percentage = percentage;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
