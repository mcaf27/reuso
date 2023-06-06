import java.util.ArrayList;

public class Voter {
  protected final String electoralCard;
  
  protected final String name;
  
  protected final String state;
  
  public static class Builder {
    private String electoralCard;
    private String name;
    private String state;
    
    public Builder electoralCard(String electoralCard) {
      this.electoralCard = electoralCard;
      return this;
    }
    
    public Builder name(String name) {
      this.name = name;
      return this;
    }
    
    public Builder state(String state) {
      this.state = state;
      return this;
    }
    
    public Voter build() {
      if (electoralCard == null)
      throw new IllegalArgumentException("electoralCard mustn't be null");
      
      if (electoralCard.isEmpty())
      throw new IllegalArgumentException("electoralCard mustn't be empty");
      
      if (name == null)
      throw new IllegalArgumentException("name mustn't be null");
      
      if (name.isEmpty())
      throw new IllegalArgumentException("name mustn't be empty");
      
      if (state == null)
      throw new IllegalArgumentException("state mustn't be null");
      
      if (state.isEmpty())
      throw new IllegalArgumentException("state mustn't be empty");
      
      return new Voter(electoralCard, name, state);
    }
  }
  
  protected Voter(String electoralCard, String name, String state) {
    this.electoralCard = electoralCard;
    this.name = name;
    this.state = state;
  }
  
  public void vote(Election election, String type, boolean isProtestVote, ArrayList<Candidate> candidates) {
    
    if (candidates != null) {
      election.strat.computeVote(election, candidates, this);
    }
    
    if (type.equals("P")) {
      if (isProtestVote) {
        election.strat.computeProtestVote(election, this, "P");
      } else {
        election.strat.computeNullVote(election, this, "P");
      }
    } else {
      if (isProtestVote) {
        election.strat.computeProtestVote(election, this, "FP");
      } else {
        election.strat.computeNullVote(election, this, "FP");
      }
    }
  }
}
