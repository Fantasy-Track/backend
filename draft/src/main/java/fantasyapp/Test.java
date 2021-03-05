package fantasyapp;

import fantasyapp.draftInstance.DraftInstanceModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import domain.entity.Athlete;
import domain.repository.AgentRepository;

public class Test {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new DraftInstanceModule("CsT32z"));
        Athlete athlete = injector.getInstance(AgentRepository.class).getFreeAgentWithHighestProjection();
        System.out.println(athlete.name);
        System.out.println(athlete.projections.toString());
    }

}
