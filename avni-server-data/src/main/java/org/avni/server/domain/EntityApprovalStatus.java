package org.avni.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.avni.server.domain.sync.SubjectLinkedSyncEntity;
import org.avni.server.domain.sync.SyncDisabledEntityHelper;
import org.avni.server.framework.hibernate.JodaDateTimeConverter;
import org.hibernate.annotations.BatchSize;
import org.joda.time.DateTime;

import java.util.Date;

@Entity
@BatchSize(size = 100)
@JsonIgnoreProperties({"approvalStatus"})
public class EntityApprovalStatus extends SyncAttributeEntity implements SubjectLinkedSyncEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "individual_id")
    private Individual individual;

    @Column
    private Long entityId;

    @Column
    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @Column
    private String entityTypeUuid;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "approval_status_id")
    private ApprovalStatus approvalStatus;

    @Column
    private String approvalStatusComment;

    @Column
    private Boolean autoApproved;

    @Column
    @NotNull
    @Convert(converter = JodaDateTimeConverter.class)
    private DateTime statusDateTime;

    @Column(name = "address_id")
    private Long addressId;

    @Column(updatable = false)
    private boolean syncDisabled;

    @NotNull
    private Date syncDisabledDateTime;

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public enum EntityType {
        Subject,
        ProgramEnrolment,
        ProgramEncounter,
        Encounter,
        ChecklistItem
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalStatusComment() {
        return approvalStatusComment;
    }

    public void setApprovalStatusComment(String approvalStatusComment) {
        this.approvalStatusComment = approvalStatusComment;
    }

    public Boolean getAutoApproved() {
        return autoApproved;
    }

    public void setAutoApproved(Boolean autoApproved) {
        this.autoApproved = autoApproved;
    }

    public DateTime getStatusDateTime() {
        return statusDateTime;
    }

    public void setStatusDateTime(DateTime statusDateTime) {
        this.statusDateTime = statusDateTime;
    }

    public String getEntityTypeUuid() {
        return entityTypeUuid;
    }

    public void setEntityTypeUuid(String entityTypeUuid) {
        this.entityTypeUuid = entityTypeUuid;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public boolean isSyncDisabled() {
        return syncDisabled;
    }

    @Override
    public void setSyncDisabledDateTime(Date syncDisabledDateTime) {
        this.syncDisabledDateTime = syncDisabledDateTime;
    }

    public void setSyncDisabled(boolean syncDisabled) {
        this.syncDisabled = syncDisabled;
    }

    @Override
    public Date getSyncDisabledDateTime() {
        return this.syncDisabledDateTime;
    }

    @PrePersist
    public void beforeSave() {
        SyncDisabledEntityHelper.handleSave(this, this.getIndividual());
    }

    @PreUpdate
    public void beforeUpdate() {
        SyncDisabledEntityHelper.handleSave(this, this.getIndividual());
    }
}
