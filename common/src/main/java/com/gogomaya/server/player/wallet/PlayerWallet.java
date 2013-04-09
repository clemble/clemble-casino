package com.gogomaya.server.player.wallet;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.gogomaya.server.money.Currency;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.money.MoneyHibernate;
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

    @ElementCollection(targetClass = Money.class)
    @CollectionTable(name = "PLAYER_WALLET_MONEY", joinColumns = @JoinColumn(name = "PLAYER_ID"))
    @Type(type = "money")
    @Columns(columns = { @Column(name = "CURRENCY"), @Column(name = "AMOUNT") })
    private Set<Money> playerMoney;

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
            return null;

        for (Money money : playerMoney)
            if (money.getCurrency() == currency)
                return money;

        return null;
    }

    public void setMoney(final Set<Money> playerMoney) {
        this.playerMoney = playerMoney;
    }

    public void add(final Money money) {
        if (money == null || money.getAmount() == 0)
            return;

        Money currentAmount = getMoney(money.getCurrency());
        playerMoney.remove(currentAmount);

        playerMoney.add(currentAmount.add(money));
    }

    public void subtract(final Money money) {
        if (money == null || money.getAmount() == 0)
            return;

        add(money.negate());
    }

}
