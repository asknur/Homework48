public class Candidate {
    private int id;
    private String name;
    private String photo;
    private int votes;

    public Candidate(int id, String name, String photo) {
        this.name = name;
        this.photo = photo;
        this.votes = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void addVote() {
        votes++;
    }

}
