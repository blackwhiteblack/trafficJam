package com.deichmann.api;

import com.deichmann.model.TrafficJam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrafficJamControllerTest {

    private final TrafficJam trafficJamMock = mock(TrafficJam.class);
    private TrafficJamController trafficJamController;

    @BeforeEach
    void setUp() {
        trafficJamController = new TrafficJamController(trafficJamMock);
    }

    @Test
    void testInvalidInputWithNullCode() {
        ResponseEntity<String> responseEntity = whenPrintTrafficJamIsCalled(null);

        thenResponseIsBadRequestAndBodyIs(responseEntity);
    }

    @Test
    void testInvalidInput() {
        String invalidCode = "1234B";

        ResponseEntity<String> responseEntity = whenPrintTrafficJamIsCalled(invalidCode);

        thenResponseIsBadRequestAndBodyIs(responseEntity);
    }

    @Test
    void testPrintWithException() {
        String code = "CBPT";
        givenTrafficJamPrintThrowsException();

        ResponseEntity<String> responseEntity = whenPrintTrafficJamIsCalled(code);

        thenResponseIsInternalServerErrorAndBodyIs(responseEntity);
    }

    @Test
    void testPrint() {
        String code = "CBPT";
        givenTrafficJamPrints("Printed Traffic Jam");

        ResponseEntity<String> responseEntity = whenPrintTrafficJamIsCalled(code);

        thenVerifyTrafficJamPrintIsCalled(code);
        thenResponseEntityBodyIsExpected(responseEntity, "Printed Traffic Jam");
    }

    @Test
    void testTurnToHead() {
        String code = "CBPT";
        givenTrafficJamPrints("Turned Traffic Jam to Head");

        ResponseEntity<String> responseEntity = whenTurnToHeadIsCalled(code);

        thenVerifyTrafficJamTurnToHeadIsCalled();
        thenVerifyTrafficJamPrintIsCalled(code);
        thenResponseEntityBodyIsExpected(responseEntity, "Turned Traffic Jam to Head");
    }

    @Test
    void testRemoveFirst() {
        String code = "CBPT";
        givenTrafficJamPrints("Updated Traffic Jam after removing first vehicle");

        ResponseEntity<String> responseEntity = whenRemoveFirstIsCalled(code);

        thenVerifyTrafficJamRemoveFirstIsCalled(code);
        thenVerifyTrafficJamPrintIsCalled(code);
        thenResponseEntityBodyIsExpected(responseEntity, "Updated Traffic Jam after removing first vehicle");
    }

    @Test
    void testRemoveLast() {
        String code = "CBPT";
        givenTrafficJamPrints("Updated Traffic Jam after removing last vehicle");

        ResponseEntity<String> responseEntity = whenRemoveLastIsCalled(code);

        thenVerifyTrafficJamRemoveLastIsCalled();
        thenVerifyTrafficJamPrintIsCalled(code);
        thenResponseEntityBodyIsExpected(responseEntity, "Updated Traffic Jam after removing last vehicle");
    }

    @Test
    void testFillTrucks() {
        String code = "CBPT";
        givenTrafficJamPrints("Filled trucks in Traffic Jam");

        ResponseEntity<String> responseEntity = whenFillTrucksIsCalled(code);

        thenVerifyTrafficJamFillTrucksIsCalled(code);
        thenResponseEntityBodyIsExpected(responseEntity, "Filled trucks in Traffic Jam");
    }

    private void givenTrafficJamPrints(final String printResult) {
        when(trafficJamMock.print()).thenReturn(printResult);
    }

    private void givenTrafficJamPrintThrowsException() {
        when(trafficJamMock.print()).thenThrow(new RuntimeException("Error occurred"));
    }

    private ResponseEntity<String> whenPrintTrafficJamIsCalled(final String code) {
        return trafficJamController.print(code);
    }

    private ResponseEntity<String> whenRemoveFirstIsCalled(String code) {
        return trafficJamController.removeFirstVehicle(code);
    }

    private ResponseEntity<String> whenRemoveLastIsCalled(String code) {
        return trafficJamController.removeLastVehicle(code);
    }

    private ResponseEntity<String> whenTurnToHeadIsCalled(String code) {
        return trafficJamController.turnToHead(code);
    }

    private ResponseEntity<String> whenFillTrucksIsCalled(String code) {
        return trafficJamController.fillTrucks(code);
    }

    private void thenVerifyTrafficJamPrintIsCalled(final String code) {
        verify(trafficJamMock).setCode(code);
        verify(trafficJamMock).print();
    }

    private void thenVerifyTrafficJamTurnToHeadIsCalled() {
        verify(trafficJamMock).turnToHead();
    }

    private void thenVerifyTrafficJamRemoveFirstIsCalled(String code) {
        verify(trafficJamMock).setCode(code);
        verify(trafficJamMock).removeFirst();
    }

    private void thenVerifyTrafficJamRemoveLastIsCalled() {
        verify(trafficJamMock).removeLast();
    }

    private void thenVerifyTrafficJamFillTrucksIsCalled(final String code) {
        verify(trafficJamMock).setCode(code);
        verify(trafficJamMock, times(2)).print();
        verify(trafficJamMock).fillTrucks();
    }

    private void thenResponseEntityBodyIsExpected(final ResponseEntity<String> responseEntity, final String expectedBody) {
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedBody, responseEntity.getBody());
    }

    private void thenResponseIsBadRequestAndBodyIs(final ResponseEntity<String> responseEntity) {
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid code format", responseEntity.getBody());
        verify(trafficJamMock, never()).print();
        verify(trafficJamMock, never()).setCode(anyString());
    }

    private void thenResponseIsInternalServerErrorAndBodyIs(final ResponseEntity<String> responseEntity) {
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("An error occurred", responseEntity.getBody());
    }
}