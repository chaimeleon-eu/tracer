package es.upv.grycap.tracer.model.dto.request.cache;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateField {
    

    @NotNull(message="The field name cannot be null.")
    @NotEmpty(message="The field name cannot be null.")
    protected String fieldName;
    
    protected String fieldValue;

}
