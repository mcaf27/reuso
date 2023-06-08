import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class VotingStrategy {
    public Map<Integer, President> presidentCandidates = new HashMap<Integer, President>();
    public Map<String, FederalDeputy> federalDeputyCandidates = new HashMap<String, FederalDeputy>();

    protected int nullPresidentVotes = 0;
    protected int nullFederalDeputyVotes = 0;
    protected int presidentProtestVotes = 0;
    protected int federalDeputyProtestVotes = 0;
    public boolean secondTurn;

    public abstract boolean computeVote(
        Election election, ArrayList<Candidate> candidates, Voter voter
    );
    
    public boolean computeNullVote(Election election, Voter voter, String type) {
        boolean already = election.hasVoterAlreadyVoted(voter, type);
        
        if (!already) {
            if (type.equals("P")) nullPresidentVotes++;
            else nullFederalDeputyVotes++;
            return true;
        } 
        
        return false;
    }

    public boolean computeProtestVote(Election election, Voter voter, String type) {
        boolean already = election.hasVoterAlreadyVoted(voter, type);
        
        if (!already) {
            if (type.equals("P")) this.presidentProtestVotes++;
            else this.federalDeputyProtestVotes++;
            return true;
        } 
        
        return false;
    }

    public abstract String getResults();

    public String results(
        int totalVotesP,
        int totalVotesFD,
        List<Candidate> sortedPresidentRank,
        List<Candidate> sortedFederalDeputyRank
    ) {

        if (sortedFederalDeputyRank.size() <= 1 || sortedPresidentRank.size() <= 0) {
            return "Quantidade de votos insuficiente";
        }

        var decimalFormater = new DecimalFormat("0.00");
        var builder = new StringBuilder();
        builder.append("Resultado da eleicao:\n");

        builder.append("  Votos presidente:\n");
        builder.append("  Total: " + totalVotesP + "\n");
        builder.append("  Votos nulos: " + this.nullPresidentVotes + " ("
            + decimalFormater.format((double) this.nullPresidentVotes / (double) totalVotesFD * 100) + "%)\n");
        builder.append("  Votos brancos: " + this.presidentProtestVotes + " ("
            + decimalFormater.format((double) this.presidentProtestVotes / (double) totalVotesFD * 100) + "%)\n");
        builder.append("\tNumero - Partido - Nome  - Votos  - % dos votos totais\n");
        
        for (Candidate candidate : sortedPresidentRank) {
          builder.append("\t" + candidate.number + " - " + candidate.party + " - " + candidate.name + " - "
              + candidate.numVotes + " - "
              + decimalFormater.format((double) candidate.numVotes / (double) totalVotesP * 100)
              + "%\n");
        }

        Candidate electPresident = sortedPresidentRank.get(0);
        double presidentPercentage = (double) electPresident.numVotes / (double) totalVotesP * 100;

        if (presidentPercentage >= 50) {
			builder.append("A elei??o foi decidida no primeiro turno!");
            builder.append("\n\n  Presidente eleito:\n");
            builder.append("  " + electPresident.name + " do " + electPresident.party + " com "
                + decimalFormater.format((double) electPresident.numVotes / (double) totalVotesP * 100) + "% dos votos\n");
		} else {
			builder.append("Elei??o ser? decidida no segundo turno!");
		}

        builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
    
        builder.append("\n\n  Votos deputado federal:\n");
        builder.append("  Votos nulos: " + this.nullFederalDeputyVotes + " ("
            + decimalFormater.format((double) this.nullFederalDeputyVotes / (double) totalVotesFD * 100) + "%)\n");
        builder.append("  Votos brancos: " + this.federalDeputyProtestVotes + " ("
            + decimalFormater.format((double) this.federalDeputyProtestVotes / (double) totalVotesFD * 100) + "%)\n");
        builder.append("  Total: " + totalVotesFD + "\n");
        builder.append("\tNumero - Partido - Nome - Votos - % dos votos totais\n");
        for (Candidate candidate : sortedFederalDeputyRank) {
          builder.append(
              "\t" + candidate.number + " - " + candidate.party + " - " + candidate.name + " - "
                  + candidate.numVotes + " - "
                  + decimalFormater.format((double) candidate.numVotes / (double) totalVotesFD * 100)
                  + "%\n");
        }

        Candidate firstDeputy = sortedFederalDeputyRank.get(0);
        Candidate secondDeputy = sortedFederalDeputyRank.get(1);
        builder.append("\n\n  Deputados eleitos:\n");
        builder.append("  1º " + firstDeputy.name + " do " + firstDeputy.party + " com "
            + decimalFormater.format((double) firstDeputy.numVotes / (double) totalVotesFD * 100) + "% dos votos\n");
        builder.append("  2º " + secondDeputy.name + " do " + secondDeputy.party + " com "
            + decimalFormater.format((double) secondDeputy.numVotes / (double) totalVotesFD * 100) + "% dos votos\n");

            
        return builder.toString();
    }
}
