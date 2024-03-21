package es.upv.grycap.tracer.model.dto.request.cache;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateTraceCacheDTO {
    
    @NotNull
    protected Collection<UpdateField> fields;
    
    protected boolean runBlockchainCommit = false;

}
