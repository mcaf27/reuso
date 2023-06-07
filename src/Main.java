public class Main {
    public static void main(String [] args) {

        boolean preferenceOrder = args[0].equals("true");
        boolean secondTurn = args[1].equals("true");
        String electionPassword = "password";

        Election currentElection = Election.getInstance(electionPassword, preferenceOrder, secondTurn);

        new Urna(currentElection);
        
        President presidentCandidate1 = new President.Builder().name("João").number(123).party("PDS1").build();
        currentElection.addPresidentCandidate(presidentCandidate1, electionPassword);
        President presidentCandidate2 = new President.Builder().name("Maria").number(124).party("ED").build();
        currentElection.addPresidentCandidate(presidentCandidate2, electionPassword);
        President presidentCandidate3 = new President.Builder().name("José").number(125).party("ALG1").build();
        currentElection.addPresidentCandidate(presidentCandidate3, electionPassword);
        
        FederalDeputy federalDeputyCandidate1 = new FederalDeputy.Builder().name("Carlos").number(12345).party("PDS1")
            .state("MG").build();
        currentElection.addFederalDeputyCandidate(federalDeputyCandidate1, electionPassword);
        FederalDeputy federalDeputyCandidate2 = new FederalDeputy.Builder().name("Cleber").number(54321).party("PDS2")
            .state("MG").build();
        currentElection.addFederalDeputyCandidate(federalDeputyCandidate2, electionPassword);
        FederalDeputy federalDeputyCandidate3 = new FederalDeputy.Builder().name("Sofia").number(11211).party("IHC")
            .state("MG").build();
        currentElection.addFederalDeputyCandidate(federalDeputyCandidate3, electionPassword);
    
        Urna.loadVoters();
        Urna.loadTSEProfessionals();
        Urna.startMenu();

    }
}