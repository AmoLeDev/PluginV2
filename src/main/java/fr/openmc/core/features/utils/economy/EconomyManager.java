package fr.openmc.core.features.utils.economy;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;

public class EconomyManager {
    @Getter private static Map<UUID, Double> balances;
    @Getter static EconomyManager instance;

    public EconomyManager() {
        balances = EconomyData.loadBalances();
        instance = this;
    }

    public double getBalance(UUID player) {
        return balances.getOrDefault(player, 0.0);
    }

    public void addBalance(UUID player, double amount) {
        double balance = getBalance(player);
        balance += amount;
        balances.put(player, balance);
        saveBalances(player);
    }

    public boolean withdrawBalance(UUID player, double amount) {
        double balance = getBalance(player);
        if(balance >= amount) {
            balance -= amount;
            balances.put(player, balance);
            saveBalances(player);
            return true;
        }
        return false;
    }

    public void setBalance(UUID player, double amount) {
        balances.put(player, amount);
        saveBalances(player);
    }

    public void saveBalances(UUID player) {
        EconomyData.saveBalances(player, getBalance(player));
    }

    public String getFormattedBalance(UUID player) {
        String balance = String.valueOf(getBalance(player));
        Currency currency = Currency.getInstance("EUR");
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        format.setCurrency(currency);
        BigDecimal bd = new BigDecimal(balance);
        return format.format(bd);
    }

    public String getFormattedNumber(double number) {
        Currency currency = Currency.getInstance("EUR");
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        format.setCurrency(currency);
        BigDecimal bd = new BigDecimal(number);
        return format.format(bd);
    }

}
