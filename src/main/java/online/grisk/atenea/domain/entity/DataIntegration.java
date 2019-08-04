/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package online.grisk.atenea.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @author Pablo Ríos Ramírez
 * @email pa.riosramirez@gmail.com
 * @web www.pabloriosramirez.com
 */

@Getter
@Setter
@NoArgsConstructor
public class DataIntegration implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idDataIntegration;

    @NotNull
    private long organization;

    private Date createdAt;

    @NotNull
    private boolean enabled;

    @NotNull
    private boolean bureau;

    @JsonIgnore
    private byte[] analyticsFile;

    private String analyticsFileName;

    private Collection<Variable> variableCollection;

    public DataIntegration(Long idDataIntegration) {
        this.idDataIntegration = idDataIntegration;
    }

    public DataIntegration(Long idDataIntegration, long organization, Date createdAt, boolean enabled, boolean bureau) {
        this.idDataIntegration = idDataIntegration;
        this.organization = organization;
        this.createdAt = createdAt;
        this.enabled = enabled;
        this.bureau = bureau;
    }

    public DataIntegration(long organization, Date createdAt, boolean enabled, boolean bureau) {
        this.organization = organization;
        this.createdAt = createdAt;
        this.enabled = enabled;
        this.bureau = bureau;
    }

    public DataIntegration(long organization, Date createdAt, boolean enabled, boolean bureau, Collection<Variable> variableCollection) {
        this.organization = organization;
        this.createdAt = createdAt;
        this.enabled = enabled;
        this.bureau = bureau;
        this.variableCollection = variableCollection;
    }

    public DataIntegration(@NotNull long organization, @NotNull Date createdAt, @NotNull boolean enabled,
                           @NotNull boolean bureau, byte[] analyticsFile, String analyticsFileName) {
        this.organization = organization;
        this.createdAt = createdAt;
        this.enabled = enabled;
        this.bureau = bureau;
        this.analyticsFile = analyticsFile;
        this.analyticsFileName = analyticsFileName;
    }
}
