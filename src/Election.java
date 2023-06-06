import java.util.HashMap;
import java.util.Map;

public class Election {
    
    public VotingStrategy strat;
    
    private final String password;
    
    private boolean status;
    
    public final int MAX = 3;
    private Map<Voter, Integer> votersPresident = new HashMap<Voter, Integer>();
    private Map<Voter, Integer> votersFederalDeputy = new HashMap<Voter, Integer>();
    private Map<Integer, President> presidentCandidates = new HashMap<Integer, President>();
    private Map<String, FederalDeputy> federalDeputyCandidates = new HashMap<String, FederalDeputy>();
    private Map<Voter, FederalDeputy> tempFDVote = new HashMap<Voter, FederalDeputy>();
    
    protected Election(String password, boolean preferenceOrder, boolean secondTurn) {
        this.password = password;
        this.status = false;
        
        if (preferenceOrder) this.strat = new PreferenceOrderStrategy();
        else this.strat = new RegularStrategy(secondTurn);
    }
    
    public boolean isVoteValid(Candidate candidate, Voter voter) {
        if (candidate instanceof President) {
            if (hasVoterAlreadyVoted(voter, "P")) {
                return false;
            }
            return true;
        } else if (candidate instanceof FederalDeputy) {
            if (hasVoterAlreadyVoted(voter, "FD")) {
                return false;
            }
            
            if (tempFDVote.get(voter) != null && tempFDVote.get(voter).equals(candidate)) {
                return false;
            }
            return true;   
        } else {
            return false;
        }
    }
    
    public boolean hasVoterAlreadyVoted(Voter voter, String type) {
        if (type == "P") return votersPresident.get(voter) != null && votersPresident.get(voter) >= 1;
        else return votersFederalDeputy.get(voter) != null && votersFederalDeputy.get(voter) >= 2;
    }
    
    public President getPresidentByNumber(int number) {
        return this.presidentCandidates.get(number);
    }
    
    public FederalDeputy getFederalDeputyByNumber(String state, int number) {
        return this.federalDeputyCandidates.get(state + number);
    }
    
    public void addFederalDeputyCandidate(FederalDeputy candidate, String password) {
        if (!this.password.equals(password))
        throw new Error("Senha inválida");
        
        if (this.federalDeputyCandidates.get(candidate.state + candidate.number) != null)
        throw new Error("Numero de candidato indisponível");
        
        this.federalDeputyCandidates.put(candidate.state + candidate.number, candidate);
    }
    
    public void removeFederalDeputyCandidate(FederalDeputy candidate, String password) {
        if (!this.password.equals(password))
        throw new Error("Senha inválida");
        
        this.federalDeputyCandidates.remove(candidate.state + candidate.number);
    }
    
    public void addPresidentCandidate(President candidate, String password) {
        if (!this.password.equals(password))
        throw new Error("Senha inválida");
        
        if (this.presidentCandidates.get(candidate.number) != null)
        throw new Error("Numero de candidato indisponível");
        
        this.presidentCandidates.put(candidate.number, candidate);
        
    }
    
    public void removePresidentCandidate(President candidate, String password) {
        if (!this.password.equals(password))
        throw new Error("Senha inválida");
        
        this.presidentCandidates.remove(candidate.number);
    }
    
    public boolean getStatus() {
        return this.status;
    }
    
    public void start(String password) {
        if (!this.password.equals(password))
            throw new Error("Senha inválida");

        this.strat.presidentCandidates = presidentCandidates;
        this.strat.federalDeputyCandidates = federalDeputyCandidates;
        this.status = true;
    }
    
    public void finish(String password) {
        if (!this.password.equals(password))
        throw new Error("Senha inválida");
        
        this.status = false;
    }
}
