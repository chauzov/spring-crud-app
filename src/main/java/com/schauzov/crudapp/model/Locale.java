package com.schauzov.crudapp.model;

import javax.persistence.*;

@Entity
@Table(name = "locale")
public class Locale {
    @Id
    @SequenceGenerator(name = "locale_info_seq", sequenceName = "locale_info_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "locale_info_seq")
    @Column(name = "locale_id", nullable = false)
    private Long localeId;

    @Column(name = "locale_code")
    private String localeCode;

    public Long getLocaleId() {
        return localeId;
    }

    public void setLocaleId(Long localeId) {
        this.localeId = localeId;
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }
}
