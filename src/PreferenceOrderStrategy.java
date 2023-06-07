import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PreferenceOrderStrategy extends VotingStrategy {
    
    private Map<Voter, ArrayList<Candidate>> votesPresident = new HashMap<Voter, ArrayList<Candidate>>();
    private Map<Voter, ArrayList<Candidate>> votesFederalDeputy = new HashMap<Voter, ArrayList<Candidate>>();

    public static Map<String, Object> getCandidatesByVotes(Map<Voter, ArrayList<Candidate>> voteMap, int last) {
        Map<Integer, Candidate> candidateVotesMap = new LinkedHashMap<>();
        ArrayList<Candidate> allCandidates = new ArrayList<>();

        if (voteMap.size() == 0) {
            Map<String, Object> result = new HashMap<>();
            result.put("rankedCandidates", allCandidates);
            result.put("totalVotes", 0);

            return result;
        }

        int totalVotes = 0;

        for (ArrayList<Candidate> candidateList : voteMap.values()) {
            int count = 0;
            for (Candidate candidate : candidateList) {
                int number = candidate.getNumber();
                int numVotes = candidate.numVotes;
                totalVotes += numVotes;
                if (!candidateVotesMap.containsKey(number)) {
                    candidateVotesMap.put(number, candidate);
                } else {
                    Candidate existingCandidate = candidateVotesMap.get(number);
                    existingCandidate.numVotes += numVotes;
                }
                count++;
                if (count >= last) {
                    break;
                }
            }
        }

        allCandidates = new ArrayList<>(candidateVotesMap.values());
        Collections.sort(allCandidates, (c1, c2) -> Integer.compare(c2.numVotes, c1.numVotes));

        Map<String, Object> result = new HashMap<>();
        result.put("rankedCandidates", allCandidates);
        result.put("totalVotes", totalVotes);

        return result;
    }
    
    @Override
    public boolean computeVote(Election election, ArrayList<Candidate> candidates, Voter voter) {
        for (Candidate candidate : candidates) {
            boolean isValid = election.isVoteValid(candidate, voter);
            if (!isValid) {
                return false;
            }
        }
        
        for (Candidate candidate : candidates) {
            candidate.numVotes++;
        }

        if (candidates.get(0) instanceof President) {
            votesPresident.put(voter, candidates);
        } else {
            votesFederalDeputy.put(voter, candidates);
        }
        
        return true;
    }

    private List<Candidate> getRankedList(String type, int last) {
        var votes = type.equals("P") ? votesPresident : votesFederalDeputy;

        if (votes.size() == 0) {
            return new ArrayList<Candidate>();
        }

        Map<String, Object> result = getCandidatesByVotes(votes, last);

        @SuppressWarnings("unchecked")
        var rankedList = (ArrayList<Candidate>) result.get("rankedCandidates");
        int totalVotes = (int) result.get("totalVotes");

        if (totalVotes > (1/2) * rankedList.get(0).numVotes) {
            return rankedList;
        }
        return getRankedList(type, last + 1);
    }
    
    @Override
    public String getResults() {
        
        List<Candidate> presidentRank = getRankedList("P", 0);
        List<Candidate> federalDeputyRank = getRankedList("FD", 1);

        int numVoters = votesPresident.size();
        
        return super.results(numVoters, numVoters * 2, presidentRank, federalDeputyRank);
        
    }
}
