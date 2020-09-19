package finance.modelling.data.ingestfinancialhistoricaltimeseries.client;

import finance.modelling.fmcommons.exception.client.ClientDailyRequestLimitReachedException;
import finance.modelling.fmcommons.exception.client.InvalidApiKeyException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@SpringBootTest
class EODHistoricalClientImplTest {

    @Autowired private EODHistoricalClientImpl client;

    @Test
    void getStockHistoricalTimeSeries() {
    }

    @Test
    void shouldReturnClientDailyRequestLimitReachedTechnicalExceptionWith402PaymentRequiredScenarioProvided() {
        Throwable mockError = mock(Throwable.class);
        EODHistoricalClientImpl spyClient = Mockito.spy(client);

        doReturn(true).when(spyClient).is402PaymentRequiredResponse(mockError);

        Throwable actualReturn = spyClient.returnTechnicalException(mockError);

        verify(spyClient, times(1)).is402PaymentRequiredResponse(mockError);

        assertThat(actualReturn.getClass(), equalTo(ClientDailyRequestLimitReachedException.class));
    }

    @Test
    void shouldReturnInvalidApiKeyTechnicalExceptionWith401UnauthorisedScenarioProvided() {
        Throwable mockError = mock(Throwable.class);
        EODHistoricalClientImpl spyClient = Mockito.spy(client);

        doReturn(false).when(spyClient).is402PaymentRequiredResponse(mockError);
        doReturn(true).when(spyClient).is401InvalidAuthorisationResponse(mockError);

        Throwable actualReturn = spyClient.returnTechnicalException(mockError);

        verify(spyClient, times(1)).is402PaymentRequiredResponse(mockError);
        verify(spyClient, times(1)).is401InvalidAuthorisationResponse(mockError);

        assertThat(actualReturn.getClass(), equalTo(InvalidApiKeyException.class));
    }

    @Test
    void shouldReturnInputExceptionWithScenarioLeadingToNoCustomTechnicalException() {
        Throwable mockError = mock(Throwable.class);
        EODHistoricalClientImpl spyClient = Mockito.spy(client);

        doReturn(false).when(spyClient).is402PaymentRequiredResponse(mockError);
        doReturn(false).when(spyClient).is401InvalidAuthorisationResponse(mockError);

        Throwable actualReturn = spyClient.returnTechnicalException(mockError);

        verify(spyClient, times(1)).is402PaymentRequiredResponse(mockError);
        verify(spyClient, times(1)).is401InvalidAuthorisationResponse(mockError);

        assertThat(actualReturn, equalTo(mockError));
    }

    @Test
    void shouldReturnIs402PaymentRequiredResponseWithRequiredExceptionMessageProvided() {
        Throwable mockError = mock(Throwable.class);
        String errorMessage402 = "402 Payment Required from GET";

        doReturn(errorMessage402).when(mockError).getMessage();

        boolean actualReturn = client.is402PaymentRequiredResponse(mockError);

        verify(mockError, times(1)).getMessage();

        assertThat(actualReturn, equalTo(true));
    }

    @Test
    void shouldReturnIsNot402PaymentRequiredResponseWith401UnauthorisedExceptionMessageProvided() {
        Throwable mockError = mock(Throwable.class);
        String errorMessage401 = "401 Unauthorized from GET";

        doReturn(errorMessage401).when(mockError).getMessage();

        boolean actualReturn = client.is402PaymentRequiredResponse(mockError);

        verify(mockError, times(1)).getMessage();

        assertThat(actualReturn, equalTo(false));
    }

    @Test
    void shouldReturnIs401UnauthorisedResponseWithRequiredExceptionMessageProvided() {
        Throwable mockError = mock(Throwable.class);
        String errorMessage401 = "401 Unauthorized from GET";

        doReturn(errorMessage401).when(mockError).getMessage();

        boolean actualReturn = client.is401InvalidAuthorisationResponse(mockError);

        verify(mockError, times(1)).getMessage();

        assertThat(actualReturn, equalTo(true));
    }

    @Test
    void shouldReturnIsNot401UnauthorisedResponseWith402PaymentRequiredExceptionMessageProvided() {
        Throwable mockError = mock(Throwable.class);
        String errorMessage402 = "402 Payment Required from GET";

        doReturn(errorMessage402).when(mockError).getMessage();

        boolean actualReturn = client.is401InvalidAuthorisationResponse(mockError);

        verify(mockError, times(1)).getMessage();

        assertThat(actualReturn, equalTo(false));
    }

    @Test
    void getRetry() {
    }

    @Test
    void shouldReturnIsNotRetryableExceptionWithClientDailyRequestLimitReachedExceptionProvided() {
        Throwable requestLimitException = new ClientDailyRequestLimitReachedException("errorMessage", new Exception());

        boolean actualReturn = client.isNotRetryableException(requestLimitException);

        assertThat(actualReturn, equalTo(true));
    }

    @Test
    void shouldReturnIsNotRetryableExceptionWithInvalidApiKeyExceptionProvided() {
        Throwable apiKeyException = new InvalidApiKeyException("errorMessage", new Exception());

        boolean actualReturn = client.isNotRetryableException(apiKeyException);

        assertThat(actualReturn, equalTo(true));
    }

    @Test
    void shouldReturnIsRetryableExceptionWithGenericRetryableExceptionProvided() {
        Throwable retryableException = new Exception();

        boolean actualReturn = client.isNotRetryableException(retryableException);

        assertThat(actualReturn, equalTo(false));
    }
}