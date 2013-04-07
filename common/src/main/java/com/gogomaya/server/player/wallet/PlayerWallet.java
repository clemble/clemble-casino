package com.gogomaya.server.player.wallet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

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
    private long playerId;

    @Type(type = "money")
    @Columns(columns = {
        @Column(name = "CURRENCY"),
        @Column(name = "AMOUNT")
    })
    private Money playerMoney;

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public PlayerWallet setPlayerId(long playerId) {
        this.playerId = playerId;
        return this;
    }

    public Money getPlayerMoney() {
        return this.playerMoney;
    }

    public void setPlayerMoney(final Money playerMoney) {
        this.playerMoney = playerMoney;
    }

}
