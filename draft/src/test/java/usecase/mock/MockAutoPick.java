package usecase.mock;

import domain.entity.Athlete;
import usecase.ContractEditor;
import usecase.DraftContext;
import usecase.autoPick.AutoPick;

public class MockAutoPick extends AutoPick {

    private static final DraftContext context = new DraftContext("test");
    private Athlete riggedAgent;

    public MockAutoPick(ContractEditor contractEditor, Athlete riggedAgent) {
        super(contractEditor, context);
        this.riggedAgent = riggedAgent;
    }

    @Override
    protected Athlete decidePick() {
        return riggedAgent;
    }

}
