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
public class TypeVariable implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idTypeVariable;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @NotNull
    @Size(min = 1, max = 50)
    private String code;

    private Collection<Variable> variableCollection;

    public TypeVariable(Long idTypeVariable) {
        this.idTypeVariable = idTypeVariable;
    }

    public TypeVariable(Long idTypeVariable, String name, String code) {
        this.idTypeVariable = idTypeVariable;
        this.name = name;
        this.code = code;
    }
}
