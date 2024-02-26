package com.deichmann.model;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@NoArgsConstructor
public class TrafficJam {

    private String code;
    private String formattedTrafficCodes;
    private boolean isTurned;
    private boolean isTruckFilled;

    public TrafficJam(String code) {
        this.code = code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String print() {
        if (isInvalidCode()) return "";
        if (isTruckFilled) {
            return fillTruckSymbol();
        }
        return updateFormattedTrafficCodes();
    }

    public void turnToHead() {
        if (isInvalidCode()) return;
        log.info("Turning traffic code to head");
        reverseString();
        isTurned = true;
    }

    public void removeFirst() {
        if (isInvalidCode()) return;
        log.info("Removing first vehicle");
        code = code.substring(1);
    }

    public void removeLast() {
        if (isInvalidCode()) return;
        log.info("Removing last vehicle");
        code = code.substring(0, code.length() - 1);
    }

    public boolean fillTrucks() {
        if (isInvalidCode()) return false;
        log.info("Filling trucks");
        isTruckFilled = true;
        return formattedTrafficCodes.contains(VehicleType.TRUCK.getSymbol());
    }

    private String updateFormattedTrafficCodes() {
        final StringBuilder sb = new StringBuilder();
        for (char c : code.toCharArray()) {
            final String symbol = getSymbolForVehicle(c);
            sb.append(isTurned ? reverseSymbol(symbol) : symbol);
        }
        if (sb.length() >= 2) {
            sb.delete(isTurned ? 0 : sb.length() - 2, isTurned ? 2 : sb.length());
        }
        formattedTrafficCodes = sb.toString();
        log.info("Updating formatted traffic codes");
        isTurned = false;
        return formattedTrafficCodes;
    }

    private String fillTruckSymbol() {
        if (formattedTrafficCodes == null) {
            return "";
        }

        int index = formattedTrafficCodes.indexOf("/O|___");
        if (index != -1) {
            formattedTrafficCodes = formattedTrafficCodes.substring(0, index) + "/O|###" + formattedTrafficCodes.substring(index + 6);
            log.info("Filling truck symbol");
        }
        isTruckFilled = false;
        isTurned = false;
        return formattedTrafficCodes;
    }

    private String getSymbolForVehicle(final char vehicleCode) {
        return switch (vehicleCode) {
            case 'C' -> VehicleType.CAR.getSymbol();
            case 'B' -> VehicleType.BUS.getSymbol();
            case 'P' -> VehicleType.PICKUP.getSymbol();
            case 'T' -> VehicleType.TRUCK.getSymbol();
            default -> "";
        };
    }

    private String reverseSymbol(final String symbol) {
        return new StringBuilder(symbol).reverse().toString();
    }

    private void reverseString() {
        code = new StringBuilder(code).reverse().toString();
    }

    private boolean isInvalidCode() {
        return code == null || code.isBlank();
    }

}

