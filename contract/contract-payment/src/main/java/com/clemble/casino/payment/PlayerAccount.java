package com.clemble.casino.payment;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.player.PlayerAware;

@Entity
@Table(name = "PLAYER_ACCOUNT")
public class PlayerAccount implements PlayerAware {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = 6508845694631953306L;

    @Id
    @Column(name = "PLAYER_ID")
    private String player;

    @ElementCollection(targetClass = Money.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "PLAYER_ACCOUNT_AMOUNT", joinColumns = @JoinColumn(name = "PLAYER_ID"))
    @Type(type = "com.clemble.casino.payment.money.MoneyHibernate")
    @Columns(columns = { @Column(name = "CURRENCY"), @Column(name = "AMOUNT") })
    private Set<Money> playerMoney = new HashSet<Money>();

    @Override
    public String getPlayer() {
        return player;
    }

    public PlayerAccount setPlayer(String player) {
        this.player = player;
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

    public PlayerAccount setMoney(final Set<Money> playerMoney) {
        this.playerMoney = playerMoney;
        return this;
    }

    public PlayerAccount add(final Money money) {
        if (money != null && money.getAmount() != 0) {
            Money currentAmount = getMoney(money.getCurrency());
            if (currentAmount != null)
                playerMoney.remove(currentAmount);
            playerMoney.add(currentAmount.add(money));
        }
        return this;
    }

    public PlayerAccount subtract(final Money money) {
        if (money != null && money.getAmount() > 0)
            add(money.negate());
        return this;
    }

    @Override
    public String toString() {
        return "PlayerWallet [playerId=" + player + ", playerMoney=" + playerMoney + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (player != null ? player.hashCode() : 0);
        result = prime * result + ((playerMoney == null) ? 0 : playerMoney.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerAccount other = (PlayerAccount) obj;
        if (player != other.player)
            return false;
        if (playerMoney == null) {
            if (other.playerMoney != null)
                return false;
        } else if (!playerMoney.equals(other.playerMoney))
            return false;
        return true;
    }

}
