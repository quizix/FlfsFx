package com.dxw.flfs.data.models.mes;

import com.dxw.flfs.data.models.erp.Feed;
import com.dxw.flfs.data.models.erp.Unit;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zhang on 2016-07-03.
 */
@Entity
@Table(name="mes_feed_requirement_detail")
@Access(AccessType.FIELD)
public class FeedRequirementDetail {
    /**
     * 内部id
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="requirementId")
    private FeedRequirement feedRequirement;

    @ManyToOne
    @JoinColumn(name="feedId")
    private Feed feed;

    @ManyToOne
    @JoinColumn(name="unitId")
    private Unit unit;

    @Column(name="date", nullable = false)
    private Date date;

    @Column(name="quantity", nullable = false)
    private float quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FeedRequirement getFeedRequirement() {
        return feedRequirement;
    }

    public void setFeedRequirement(FeedRequirement feedRequirement) {
        this.feedRequirement = feedRequirement;
    }



    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
