import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class VotingStrategy {
    public Map<Integer, President> presidentCandidates = new HashMap<Integer, President>();
    public Map<String, FederalDeputy> federalDeputyCandidates = new HashMap<String, FederalDeputy>();

    protected int nullPresidentVotes;
    protected int nullFederalDeputyVotes;
    protected int presidentProtestVotes;
    protected int federalDeputyProtestVotes;
    public boolean secondTurn;

    public abstract boolean computeVote(
        Election election, ArrayList<Candidate> candidates, Voter voter
    );
    
    public abstract boolean computeNullVote(Election election, Voter voter, String type);
    public abstract boolean computeProtestVote(Election election, Voter voter, String type);
    public abstract String getResults();

    public String results(
        int totalVotesP,
        int totalVotesFD,
        List<President> sortedPresidentRank,
        List<FederalDeputy> sortedFederalDeputyRank
    ) {
        var decimalFormater = new DecimalFormat("0.00");
        var builder = new StringBuilder();
        builder.append("Resultado da eleicao:\n");

        builder.append("  Votos presidente:\n");
        builder.append("  Total: " + totalVotesP + "\n");
        builder.append("  Votos nulos: " + nullPresidentVotes + " ("
            + decimalFormater.format((double) nullPresidentVotes / (double) totalVotesFD * 100) + "%)\n");
        builder.append("  Votos brancos: " + presidentProtestVotes + " ("
            + decimalFormater.format((double) presidentProtestVotes / (double) totalVotesFD * 100) + "%)\n");
        builder.append("\tNumero - Partido - Nome  - Votos  - % dos votos totais\n");
        
        for (President candidate : sortedPresidentRank) {
          builder.append("\t" + candidate.number + " - " + candidate.party + " - " + candidate.name + " - "
              + candidate.numVotes + " - "
              + decimalFormater.format((double) candidate.numVotes / (double) totalVotesP * 100)
              + "%\n");
        }

        President electPresident = sortedPresidentRank.get(0);
        builder.append("\n\n  Presidente eleito:\n");
        builder.append("  " + electPresident.name + " do " + electPresident.party + " com "
            + decimalFormater.format((double) electPresident.numVotes / (double) totalVotesP * 100) + "% dos votos\n");
        builder.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
    
        builder.append("\n\n  Votos deputado federal:\n");
        builder.append("  Votos nulos: " + nullFederalDeputyVotes + " ("
            + decimalFormater.format((double) nullFederalDeputyVotes / (double) totalVotesFD * 100) + "%)\n");
        builder.append("  Votos brancos: " + federalDeputyProtestVotes + " ("
            + decimalFormater.format((double) federalDeputyProtestVotes / (double) totalVotesFD * 100) + "%)\n");
        builder.append("  Total: " + totalVotesFD + "\n");
        builder.append("\tNumero - Partido - Nome - Estado - Votos - % dos votos totais\n");
        for (FederalDeputy candidate : sortedFederalDeputyRank) {
          builder.append(
              "\t" + candidate.number + " - " + candidate.party + " - " + candidate.state + " - " + candidate.name + " - "
                  + candidate.numVotes + " - "
                  + decimalFormater.format((double) candidate.numVotes / (double) totalVotesFD * 100)
                  + "%\n");
        }

        FederalDeputy firstDeputy = sortedFederalDeputyRank.get(0);
        FederalDeputy secondDeputy = sortedFederalDeputyRank.get(1);
        builder.append("\n\n  Deputados eleitos:\n");
        builder.append("  1º " + firstDeputy.name + " do " + firstDeputy.party + " com "
            + decimalFormater.format((double) firstDeputy.numVotes / (double) totalVotesFD * 100) + "% dos votos\n");
        builder.append("  2º " + secondDeputy.name + " do " + secondDeputy.party + " com "
            + decimalFormater.format((double) secondDeputy.numVotes / (double) totalVotesFD * 100) + "% dos votos\n");

            
        return builder.toString();
    }
}
