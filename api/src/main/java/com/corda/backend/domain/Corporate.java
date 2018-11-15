package com.corda.backend.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Corporate.
 */
@Entity
@Table(name = "corporate")
public class Corporate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "corporate_id")
    private String corporate_id;

    @Column(name = "corporate_name")
    private String corporate_name;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorporate_id() {
        return corporate_id;
    }

    public Corporate corporate_id(String corporate_id) {
        this.corporate_id = corporate_id;
        return this;
    }

    public void setCorporate_id(String corporate_id) {
        this.corporate_id = corporate_id;
    }

    public String getCorporate_name() {
        return corporate_name;
    }

    public Corporate corporate_name(String corporate_name) {
        this.corporate_name = corporate_name;
        return this;
    }

    public void setCorporate_name(String corporate_name) {
        this.corporate_name = corporate_name;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Corporate corporate = (Corporate) o;
        if (corporate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), corporate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Corporate{" +
            "id=" + getId() +
            ", corporate_id='" + getCorporate_id() + "'" +
            ", corporate_name='" + getCorporate_name() + "'" +
            "}";
    }
}
