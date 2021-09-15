package com.schauzov.crudapp.model;

import javax.persistence.*;

@Entity
@Table(name = "locale",
        uniqueConstraints = {@UniqueConstraint(name = "locale_code_uniq", columnNames = {"locale_code"})}
)
public class ProductLocale {
    @Id
    @SequenceGenerator(name = "locale_info_seq", sequenceName = "locale_info_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "locale_info_seq")
    @Column(name = "locale_id")
    private Long localeId;

    @Column(name = "locale_code")
    private String localeCode;

    public ProductLocale() {
    }

    public ProductLocale(String localeCode) {
        this.localeCode = localeCode;
    }

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
