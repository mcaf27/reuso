import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RegularStrategy extends VotingStrategy {
    
    private Map<Voter, Integer> votersPresident = new HashMap<Voter, Integer>();
    private Map<Voter, Integer> votersFederalDeputy = new HashMap<Voter, Integer>();
    private Map<Voter, FederalDeputy> tempFDVote = new HashMap<Voter, FederalDeputy>();
    
    public RegularStrategy(boolean secondTurn) {
        this.secondTurn = secondTurn;
    }
    
    @Override
    public boolean computeVote(Election election, ArrayList<Candidate> candidates, Voter voter) {
        Candidate candidate = candidates.get(0);
        
        boolean isValid = election.isVoteValid(candidate, voter);
        
        if (!isValid) {
            return false;
        }
        
        candidate.numVotes++;
        
        if (candidate instanceof President) {
            votersPresident.put(voter, 1);
        } else {
            if (votersFederalDeputy.get(voter) == null) {
                votersFederalDeputy.put(voter, 1);
                tempFDVote.put(voter, (FederalDeputy) candidate);
            } else {
                votersFederalDeputy.put(voter, this.votersFederalDeputy.get(voter) + 1);
                tempFDVote.remove(voter);
            }
        }
        
        return true;
    }
   
    @Override
    public String getResults() {
        var presidentRank = new ArrayList<Candidate>();
        var federalDeputyRank = new ArrayList<Candidate>();

        int totalVotesP = super.presidentProtestVotes + super.nullPresidentVotes;
        for (Map.Entry<Integer, President> candidateEntry : presidentCandidates.entrySet()) {
            President candidate = candidateEntry.getValue();
            totalVotesP += candidate.numVotes;
            presidentRank.add(candidate);
        }
        
        int totalVotesFD = super.federalDeputyProtestVotes + super.nullFederalDeputyVotes;
        for (Map.Entry<String, FederalDeputy> candidateEntry : federalDeputyCandidates.entrySet()) {
            FederalDeputy candidate = candidateEntry.getValue();
            totalVotesFD += candidate.numVotes;
            federalDeputyRank.add(candidate);
        }
        
        var sortedFederalDeputyRank = federalDeputyRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());
        
        var sortedPresidentRank = presidentRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());
        
        return super.results(totalVotesP, totalVotesFD, sortedPresidentRank, sortedFederalDeputyRank);
    }
}