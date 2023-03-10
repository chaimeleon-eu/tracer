package es.upv.grycap.tracer.model.dto.response;

import es.upv.grycap.tracer.model.dto.BlockchainType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class RespTracesBCBase {

    protected BlockchainType blockchain;

}
