/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package online.grisk.atenea.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author Pablo Ríos Ramírez
 * @email pa.riosramirez@gmail.com
 * @web www.pabloriosramirez.com
 */
@Getter
@Setter
@NoArgsConstructor
public class Variable implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idVariable;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @NotNull
    @Size(min = 1, max = 50)
    private String code;

    @NotNull
    @Size(min = 1, max = 50)
    private String coordinate;

    @NotNull
    @Size(min = 1, max = 100)
    private String defaultValue;

    private TypeVariable typeVariable;

    @NotNull
    private boolean bureau;

    public Variable(Long idVariable) {
        this.idVariable = idVariable;
    }

    public Variable(String name, String code, String coordinate, String defaultValue, boolean bureau) {
        this.idVariable = idVariable;
        this.name = name;
        this.code = code;
        this.coordinate = coordinate;
        this.defaultValue = defaultValue;
        this.bureau = bureau;
    }

    public Variable(@NotNull @Size(min = 1, max = 100) String name, @NotNull @Size(min = 1, max = 50) String code, @NotNull @Size(min = 1, max = 50) String coordinate, @NotNull @Size(min = 1, max = 100) String defaultValue, TypeVariable typeVariable, @NotNull boolean bureau) {
        this.name = name;
        this.code = code;
        this.coordinate = coordinate;
        this.defaultValue = defaultValue;
        this.typeVariable = typeVariable;
        this.bureau = bureau;
    }
}
