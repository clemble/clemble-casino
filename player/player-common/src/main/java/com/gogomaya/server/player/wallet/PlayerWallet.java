package com.gogomaya.server.player.wallet;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.gogomaya.server.money.Currency;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.MoneyHibernate;
import com.gogomaya.server.money.PlayerMoneyTransaction;
import com.gogomaya.server.player.PlayerAware;

@Entity
@Table(name = "PLAYER_WALLET")
@TypeDef(name = "money", typeClass = MoneyHibernate.class)
public class PlayerWallet implements PlayerAware {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = 6508845694631953306L;

    @Id
    @Column(name = "PLAYER_ID")
    private long playerId;

    @ElementCollection(targetClass = Money.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "PLAYER_WALLET_MONEY", joinColumns = @JoinColumn(name = "PLAYER_ID"))
    @Type(type = "money")
    @Columns(columns = { @Column(name = "CURRENCY"), @Column(name = "AMOUNT") })
    private Set<Money> playerMoney = new HashSet<Money>();

    @OneToMany(targetEntity = PlayerMoneyTransaction.class)
    @JoinColumn(name = "PLAYER_ID", referencedColumnName = "PLAYER_ID")
    private Set<PlayerMoneyTransaction> moneyTransactions = new HashSet<PlayerMoneyTransaction>();

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public PlayerWallet setPlayerId(long playerId) {
        this.playerId = playerId;
        return this;
    }

    public Set<Money> getMoney() {
        return this.playerMoney;
    }

    public Money getMoney(Currency currency) {
        if (currency == null)
            return Money.create(currency, 0);

        for (Money money : playerMoney)
            if (money.getCurrency() == currency)
                return money;

        return Money.create(currency, 0);
    }

    public PlayerWallet setMoney(final Set<Money> playerMoney) {
        this.playerMoney = playerMoney;
        return this;
    }

    public PlayerWallet add(final Money money) {
        if (money != null && money.getAmount() != 0) {
            Money currentAmount = getMoney(money.getCurrency());
            if (currentAmount != null)
                playerMoney.remove(currentAmount);
            playerMoney.add(currentAmount.add(money));
        }
        return this;
    }

    public PlayerWallet subtract(final Money money) {
        if (money != null && money.getAmount() == 0)
            add(money.negate());
        return this;
    }

    public Set<PlayerMoneyTransaction> getMoneyTransactions() {
        return moneyTransactions;
    }

    public PlayerWallet setMoneyTransactions(Set<PlayerMoneyTransaction> moneyTransactions) {
        this.moneyTransactions = moneyTransactions;
        return this;
    }

    public PlayerWallet addMoneyTransaction(PlayerMoneyTransaction moneyTransaction) {
        this.moneyTransactions.add(moneyTransaction);
        return this;
    }

}
