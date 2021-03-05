package usecase.postProcess;

import domain.repository.ResultRepository;
import mock.MeetBank;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.scoring.ResultsBank;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ResultsUploaderTest {

    private ResultsUploader uploader;
    private ResultRepository resultRepository;

    @BeforeMethod
    public void setUp() {
        resultRepository = mock(ResultRepository.class);
        uploader = new ResultsUploader(resultRepository);
    }

    @Test
    public void testUploadAllResults() {
        uploader.uploadAllResults(List.of(ResultsBank.result1, ResultsBank.result2, ResultsBank.result3), MeetBank.testMeet1);

        verify(resultRepository).addResult(ResultsBank.result1);
        verify(resultRepository).addResult(ResultsBank.result3);
        verify(resultRepository).addResult(ResultsBank.result2, "T1");
    }
}