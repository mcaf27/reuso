import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PreferenceOrderStrategy extends VotingStrategy {
    
    private Map<Voter, Candidate[]> votesPresident = new HashMap<Voter, Candidate[]>();
    private Map<Voter, Candidate[]> votesFederalDeputy = new HashMap<Voter, Candidate[]>();
    
    @Override
    public boolean computeVote(Election election, ArrayList<Candidate> candidates, Voter voter) {
        for (Candidate candidate : candidates) {
            boolean isValid = election.isVoteValid(candidate, voter);
            if (!isValid) {
                return false;
            }
        }
        
        candidates.get(0).numVotes++;
        Candidate [] candidatesArray = candidates.toArray(new Candidate[candidates.size()]);

        if (candidates.get(0) instanceof President) {
            votesPresident.put(voter, candidatesArray);
        } else {
            votesFederalDeputy.put(voter, candidatesArray);
        }
        
        return true;
    }
    
    @Override
    public String getResults() {
        
        //todo: ordem de preferência
        
        var presidentRank = new ArrayList<President>();
        var federalDeputyRank = new ArrayList<FederalDeputy>();
        
        for (Map.Entry<Integer, President> candidateEntry : presidentCandidates.entrySet()) {
            President candidate = candidateEntry.getValue();
            presidentRank.add(candidate);
        }
        
        for (Map.Entry<String, FederalDeputy> candidateEntry : federalDeputyCandidates.entrySet()) {
            FederalDeputy candidate = candidateEntry.getValue();
            federalDeputyRank.add(candidate);
        }
        
        var sortedFederalDeputyRank = federalDeputyRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());
        
        var sortedPresidentRank = presidentRank.stream()
        .sorted((o1, o2) -> o1.numVotes == o2.numVotes ? 0 : o1.numVotes < o2.numVotes ? 1 : -1)
        .collect(Collectors.toList());
        
        return super.results(4, 5, sortedPresidentRank, sortedFederalDeputyRank);
        
    }
    
    @Override
    public boolean computeNullVote(Election election, Voter voter, String type) {
        boolean isValid = election.hasVoterAlreadyVoted(voter, type);
        if (type == "P") nullPresidentVotes++;
        else nullFederalDeputyVotes++;
        
        return isValid;
    }

    @Override
    public boolean computeProtestVote(Election election, Voter voter, String type) {
        boolean isValid = election.hasVoterAlreadyVoted(voter, type);
        if (type == "P") presidentProtestVotes++;
        else federalDeputyProtestVotes++;

        return isValid;
    }
}
