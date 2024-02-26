package com.deichmann.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrafficJamTest {

    @Test
    public void trafficJamWithBus() {
        TrafficJam trafficJam = new TrafficJam("CCB");
        assertEquals("/OO\\  /OO\\  -/OOOO\\", trafficJam.print());
    }

    @Test
    public void trafficJamWithBusAndPickup() {
        TrafficJam trafficJam = new TrafficJam("CPCB");
        assertEquals("/OO\\  /O\\__  /OO\\  -/OOOO\\", trafficJam.print());
    }

    @Test
    public void australianTrafficJam() {
        TrafficJam trafficJam = new TrafficJam("CPCB");
        trafficJam.turnToHead();
        assertEquals("\\OOOO/-  \\OO/  __\\O/  \\OO/", trafficJam.print());
    }

    @Test
    public void modifyTrafficJam() {
        TrafficJam trafficJam = new TrafficJam("CPCBC");
        trafficJam.removeFirst();
        assertEquals("/O\\__  /OO\\  -/OOOO\\  /OO\\", trafficJam.print());
        trafficJam.removeLast();
        assertEquals("/O\\__  /OO\\  -/OOOO\\", trafficJam.print());

    }

    @Test
    public void TrafficJamWithTrucks() {
        TrafficJam trafficJam = new TrafficJam("TTT");
        assertEquals("/O|___  /O|___  /O|___", trafficJam.print());
        trafficJam.fillTrucks();
        assertEquals("/O|###  /O|___  /O|___", trafficJam.print());
        trafficJam.fillTrucks();
        assertEquals("/O|###  /O|###  /O|___", trafficJam.print());
        trafficJam.fillTrucks();
        assertEquals("/O|###  /O|###  /O|###", trafficJam.print());
        assertFalse(trafficJam.fillTrucks());
    }
}